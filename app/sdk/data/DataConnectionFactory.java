package sdk.data;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import play.Configuration;
import play.db.DB;
import sdk.data.DataParameter.Direction;
import sdk.data.oracle.ConnectionState;

public class DataConnectionFactory implements sdk.data.IDataActions {

    private String prefix = "";
    private String connectionName = "default";
    private HashMap<String, ConnectionState> nodes = new HashMap<>(); // String connection , and Active State

    public DataConnectionFactory() {

        nodes.put(this.getConnectionName(), new ConnectionState(true));

        // SECOND: REPLICATION NODE STEP (IF EXIST'S)
        if (Configuration.root().keys().contains("valentys.replication.enable")) {
            if (Configuration.root().getBoolean("valentys.replication.enable")) {

                String replicationNodes = Configuration.root().getString("valentys.replication.nodes");
                if (replicationNodes.trim().length() > 0) {

                    // Get all replicated Nodes
                    String[] _nodes = replicationNodes.split(",");
                    for (final String node : _nodes) {
                        nodes.put(node, new ConnectionState(false));
                    }

                }
            }
        }

    }

    public DataConnectionFactory(String connectionName) {
        this.connectionName = connectionName;
    }

    public void AddParameter(CallableStatement cs, Integer index, DataParameter parameter) throws Exception {

        if (parameter.getDirection() == Direction.OUT || parameter.getDirection() == Direction.INOUT) {
            try {
                cs.registerOutParameter(parameter.getName(), parameter.getSqltype());
                return;
            } catch (SQLException e) {
                /* Messages.get("ERROR.E0001", parameter.getName() , value)); */
                throw new Exception(e.getMessage());
            }
        }

        String parameterName = String.format("%s%s", (parameter.getName().indexOf(getPrefix()) >= 0 ? "" : getPrefix()), parameter.getName());

        Object value = parameter.getValue();
        try {
            if (value instanceof String) {
                // VARCHAR
                cs.setString(parameterName, value.toString());

            } else if (value instanceof Boolean) {
                // BOOLEAN , BIT
                cs.setBoolean(parameterName, (Boolean) value);

            } else if (value instanceof Short) {
                // SHORT
                cs.setShort(parameterName, (Short) value);

            } else if (value instanceof Double) {
                // DECIMAL , FLOAT
                cs.setDouble(parameterName, (Double) value);

            } else if (value instanceof Integer) {
                // INTEGER , BIGINT
                cs.setInt(parameterName, (Integer) value);

            } else if (value instanceof Long) {
                // BIGINT
                cs.setLong(parameterName, (Long) value);

            } else if (value instanceof Timestamp) {
                // DATE
                cs.setTimestamp(parameterName, (Timestamp) value);

            } else if (value instanceof Date) {
                // DATE
                cs.setDate(parameterName, (java.sql.Date) value);

            } else if (value instanceof java.io.File) {
                // BINRAY DATA
                long Length = ((java.io.File) value).length();
                java.io.InputStream stream = new java.io.FileInputStream(((java.io.File) value));

                String driver = Configuration.root().getString("db.default.driver");
                switch (driver) {
                    default:
                    case "com.mysql.jdbc.Driver": // LIKE MYSQL
                        cs.setBlob(parameterName, stream, Length);
                        break;

                    case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
                        // LIKE MSSQL (JDBC FOR MICROSOFT)
                    case "net.sourceforge.jtds.jdbc.Driver": // LIKE MSSQL
                    case "com.sql.jdbc.Driver":
                        // BUG!!!!, Instead of getting the real lenght, the java
                        // fail with "abstractmethoderror"
                        cs.setBinaryStream(parameterName, stream, (int) Length);
                        break;
                }

            } else if (value == null) {
                // NULL VALUE
                cs.setNull(parameterName, Types.VARCHAR);
            }

        } catch (Exception ex) {
//            throw new Exception(3300, parameter.getName(), ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    private Object _Execute(DataService service, play.libs.F.Function<CallableStatement, Object> delegate, String connectionName) throws Throwable {
        Connection conn = DB.getConnection(connectionName);
        CallableStatement cs;

        try {

            cs = conn.prepareCall(buildStoreProcedureCall(service));

            // For each parameter, set the value ;)
            for (int index = 1; index <= service.getParameters().size(); index++) {
                AddParameter(cs, index, service.getParameters().get(index - 1));
            }

            Object result = delegate.apply(cs);
            return result;

        } finally {
            try {
                // cs.closeOnCompletion();
                // cs.close();
                conn.close();
            } catch (Exception ex) {
            }
        }
    }

    protected Object Execute(final DataService service, final play.libs.F.Function<CallableStatement, Object> delegate) throws Exception {

        // FIRST EXECUTE THE MAIN CALL (THE ACTIVE CONNECTION IS THE MAIN
        String activeConnectionName = "";
        for (Map.Entry<String, ConnectionState> entry : nodes.entrySet()) {
            String connectionName = entry.getKey();
            ConnectionState state = entry.getValue();

            // SEARCH FOR THE ACTIVE CONNECTION , WHICH IS THE FIRST AVAILABLE NODE
            if (state.isActive()) {
                activeConnectionName = connectionName;
                break;
            }
        }

        Object result;
        try {
            result = _Execute(service, delegate, activeConnectionName);
        } catch (Throwable ex) {

            // IF FAIL'S THE CONNECTION
            if (ex instanceof Exception) {

                // RETURN THE FRAMEWORK EXCEPTION
                throw (Exception) ex;

            } else if (ex instanceof SQLTimeoutException || ex instanceof SQLRecoverableException) {

                @SuppressWarnings("unused")
                String message = java.text.MessageFormat.format("La connection principal {0} , ha sufrido un fallo!, es necesario que revise que fue lo que paso!", activeConnectionName);
                // SEND NOTIFICATION
                //Logger.log(Level.SEVERE, "DataConnectionFactory", "ACTIVE_DATABASE_TIMEOUT", message);

                // IF THE EXCEPTION IS FOR TIMEOUT, MEANS WHICH THE DATABASE IS UNAVAILABLE, SO CHANGE THE ACTIVECONNECTION TO OTHER NEXT NODE
                boolean unavailableSet = false;
                for (Map.Entry<String, ConnectionState> entry : nodes.entrySet()) {
                    ConnectionState state = entry.getValue();

                    if (unavailableSet) {
                        state.setActive(true);
                        break;
                    }

                    // IF FINDED, SET THE CONNECTION UNAVAILABLE
                    if (state.isActive()) {
                        state.setActive(false);
                        unavailableSet = true;
                    }


                }

                // TRY AGAIN , BUT FOR THE NEXT CONNECTION
                return Execute(service, delegate);

                // NEVER, TRY TO RECONNECT TO THE OTHER NODE, WHICH HAS LEAST GOING UNAVAILABLE
                // BECAUSE , THAT DATABASE HAS TO UNSYNC, WITH THE MAIN DATABASE

            } else {

                // WRAP EXCEPTION INTO FRAMEWORK EXCEPTION
                throw new Exception(ex);
            }
        }

        boolean findFirstActiveNode = false;
        for (Map.Entry<String, ConnectionState> entry : nodes.entrySet()) {
            final String connectionName = entry.getKey();
            ConnectionState state = entry.getValue();

            //CHECK NODE IN THE LIST; FOR THE FIRST ACTIVE, IN THE SEQUENCE, THE FIRST ACTIVE, MEANS, WICH NODE IS READY, THE FIRST NODE , IS THE MAIN PRIMARY CONNECTION
            if (state.isActive()) {
                findFirstActiveNode = true;
                continue;
            }

            // EXECUTE ALL OTHER REPLICATION NODES , IN A THREAD
            if (findFirstActiveNode) {
                // Call execute in a diferent Thread
                new Thread(new Runnable() {
                    public void run() {
                        try {

                            _Execute(service, delegate, connectionName);
                        } catch (Throwable ex) {

                            @SuppressWarnings("unused")
                            String message = java.text.MessageFormat.format("La connection de replicacion {0} , ha sufrido un fallo en uno de los nodos!, es necesario que revise que fue lo que paso!", connectionName);
                            // SEND NOTIFICATION
                            //Logger.log(Level.SEVERE, "DataConnectionFactory", "NODE_DATABASE_TIMEOUT", message);
                        }
                    }
                }).start();

            }
        }

        return result;
    }

    protected String buildStoreProcedureCall(DataService service) {

        String driver = Configuration.root().getString("db.default.driver");
        String statement = "";

        switch (driver) {
            default:
            case "com.mysql.jdbc.Driver": // LIKE MYSQL (APPEND $)
                setPrefix("$");
                statement = "{call %s(%s)}";
                break;

            case "net.sourceforge.jtds.jdbc.Driver":
                // LIKE MSSQL (APPEND @)
            case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
                // LIKE MSSQL (JDBC FOR MICROSOFT)
            case "com.sql.jdbc.Driver":
                setPrefix("@");
                statement = "{call %s(%s)}";
                break;
        }

        // For each parameter add the ? character to indicates an parameter
        String signature = "";
        for (int i = 0; i < service.getParameters().size(); i++) {
            signature += (i == 0) ? "?" : ",?";
        }

        statement = String.format(statement, service.getStoreProcedure(), signature);

        return statement;
    }

    @Override
    public void ExecuteAction(DataService service) throws Exception {

        // --------------------------------------------------------------------
        Execute(service, new play.libs.F.Function<CallableStatement, Object>() {
            @Override
            public Object apply(CallableStatement cs) throws Throwable {

                try {
                    cs.executeUpdate();
                    cs.close();
                } catch (Exception ex) {

                    throw new Exception(ex);
                }

                return null;
            }
        });
        // --------------------------------------------------------------------
    }

    @Override
    public sdk.data.EntityRepository ExecuteQuery(final DataService service) throws Exception {

        // --------------------------------------------------------------------
        Object result = Execute(service, new play.libs.F.Function<CallableStatement, Object>() {
            @Override
            public Object apply(CallableStatement cs) throws Throwable {

                // --------------------------------------------------------------------
                ArrayList<ResultSet> resultsets = new ArrayList<>();

                // Retrieve data from the result set.
                ResultSet rs = cs.executeQuery();
                CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
                crs.populate(rs);
                resultsets.add(crs);
                rs.close();

                while (cs.getMoreResults()) {

                    // Retrieve data from the result set.
                    rs = cs.getResultSet();
                    crs = RowSetProvider.newFactory().createCachedRowSet();
                    crs.populate(rs);
                    resultsets.add(crs);
                    rs.close();

                }
                cs.close();

                return resultsets;
                // --------------------------------------------------------------------
            }
        });

        @SuppressWarnings("unchecked")
        sdk.data.EntityRepository rep = new DefaultEntityRepository((ArrayList<ResultSet>) result);

        return rep;
        // --------------------------------------------------------------------

    }

    @Override
    public Object ExecuteScalar(DataService service) throws Exception {

        // --------------------------------------------------------------------
        Object result = Execute(service, new play.libs.F.Function<CallableStatement, Object>() {
            @Override
            public Object apply(CallableStatement cs) throws Throwable {
                ResultSet rs = null;
                try {

                    rs = cs.executeQuery();
                    Object firstValue = null;

                    if (rs.next()) {
                        firstValue = rs.getObject(1);
                    }
                    rs.close();

                    return firstValue;
                } catch (Exception ex) {
                    try {
                        rs.close();
                    } catch (Exception _ex) {
                    }

                    throw new Exception(ex);
                }

            }
        });

        return result;
        // --------------------------------------------------------------------
    }

    @Override
    public sdk.data.EntityRepository ExecuteSql(final DataService service) throws Exception {

        // --------------------------------------------------------------------
        Connection conn = DB.getConnection(this.getConnectionName());
        PreparedStatement cs;

        try {
            ArrayList<ResultSet> resultsets = new ArrayList<>();

            cs = conn.prepareStatement(service.getStoreProcedure());

            // Retrieve data from the result set.
            ResultSet rs = cs.executeQuery();
            CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(rs);
            resultsets.add(crs);
            rs.close();

            while (cs.getMoreResults()) {

                // Retrieve data from the result set.
                rs = cs.getResultSet();
                crs = RowSetProvider.newFactory().createCachedRowSet();
                crs.populate(rs);
                resultsets.add(crs);

                rs.close();

            }
            cs.close();

            sdk.data.EntityRepository rep = new DefaultEntityRepository(resultsets);

            return rep;

        } catch (Throwable ex) {
            if (ex instanceof Exception) {
                throw (Exception) ex;
            } else {
                throw new Exception(ex);
            }
        } finally {
            try {
                // cs.closeOnCompletion();

                conn.close();
            } catch (Exception ex) {
            }
        }
        // --------------------------------------------------------------------

    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getConnectionName() {
        return connectionName;
    }


    public HashMap<String, ConnectionState> getNodesStates() {
        return this.nodes;
    }


    public boolean resetPoolNodes() {

        for (Map.Entry<String, ConnectionState> entry : nodes.entrySet()) {
            ConnectionState state = entry.getValue();

            if ("default".equalsIgnoreCase(entry.getKey())) {
                state.setActive(true);
            } else {
                state.setActive(false);
            }
        }

        return true;
    }
}
