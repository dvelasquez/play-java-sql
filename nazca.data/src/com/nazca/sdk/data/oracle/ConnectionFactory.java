package com.nazca.sdk.data.oracle;

import com.nazca.sdk.data.DataConnectionFactory;
import com.nazca.sdk.data.DataParameter;
import com.nazca.sdk.data.DataParameter.Direction;
import com.nazca.sdk.data.DataService;
import play.Configuration;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ConnectionFactory extends DataConnectionFactory {

    @Override
    @SuppressWarnings("unchecked")
    public OracleRepository ExecuteQuery(final DataService service) throws Exception {
        // --------------------------------------------------------------------
        Object result = Execute(service, new play.libs.F.Function<CallableStatement, Object>() {
            @Override
            public Object apply(CallableStatement cs) throws Throwable {

                // execute and retrieve the result set's Array
                cs.execute();

                ArrayList<ResultSet> rawData = new ArrayList<>();

                for (int i = 0; i < service.getParameters().size(); i++) {
                    DataParameter parameter = service.getParameters().get(i);

                    if (parameter.getDirection() == Direction.OUT) {

                        //Retrieve data from the result set.
                        ResultSet rs = (ResultSet) cs.getObject(i + 1);
                        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();

                        crs.populate(rs);
                        rs.close();


                        rawData.add(rs);
                    }
                }

                return rawData;

            }
        });
        return new OracleRepository((ArrayList<ResultSet>) result);
        // --------------------------------------------------------------------
    }

    @Override
    public void AddParameter(CallableStatement cs, Integer index, DataParameter parameter) throws Exception {
        if (parameter.getDirection() == Direction.OUT || parameter.getDirection() == Direction.INOUT) {
            try {
                cs.registerOutParameter(index, parameter.getSqltype());
                return;
            } catch (SQLException e) {
                /* Messages.get("ERROR.E0001", parameter.getName() , value)); */
                throw new Exception(e.getMessage());
            }
        }

        // String parameterName = String.format("%s%s",
        // (parameter.getName().indexOf(getPrefix()) >= 0 ? "" : getPrefix()),
        // parameter.getName());

        Object value = parameter.getValue();
        try {
            if (value instanceof String) {
                // VARCHAR
                cs.setString(index, value.toString());

            } else if (value instanceof Boolean) {
                // BOOLEAN , BIT
                cs.setBoolean(index, (Boolean) value);

            } else if (value instanceof Short) {
                // SHORT
                cs.setShort(index, (Short) value);

            } else if (value instanceof Double) {
                // DECIMAL , FLOAT
                cs.setDouble(index, (Double) value);

            } else if (value instanceof Integer) {
                // INTEGER , BIGINT
                cs.setInt(index, (Integer) value);

            } else if (value instanceof Long) {
                // BIGINT
                cs.setLong(index, (Long) value);

            } else if (value instanceof Date) {
                // DATE
                cs.setDate(index, (java.sql.Date) value);

            } else if (value instanceof java.io.File) {
                // BINRAY DATA
                long Length = ((java.io.File) value).length();
                java.io.InputStream stream = new java.io.FileInputStream(((java.io.File) value));

                String driver = Configuration.root().getString("db.default.driver");
                switch (driver) {
                    default:
                    case "com.mysql.jdbc.Driver": // LIKE MYSQL
                        cs.setBlob(index, stream, Length);
                        break;

                    case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
                        // LIKE MSSQL (JDBC FOR MICROSOFT)
                    case "net.sourceforge.jtds.jdbc.Driver": // LIKE MSSQL
                    case "com.sql.jdbc.Driver":
                        // BUG!!!!, Instead of getting the real lenght, the java
                        // fail with "abstractmethoderror"
                        cs.setBinaryStream(index, stream, (int) Length);
                        break;
                }

            } else if (value == null) {
                // NULL VALUE
                cs.setNull(index, java.sql.Types.VARCHAR);
            }

        } catch (Exception ex) {
//            throw new nazca.exceptions.FrameworkException(3300, parameter.getName(), ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Object ExecuteScalar(final DataService service) throws Exception {

        // --------------------------------------------------------------------
        Object result = Execute(service, new play.libs.F.Function<CallableStatement, Object>() {
            @Override
            public Object apply(CallableStatement cs) throws Throwable {

                cs.execute();

                for (int i = 0; i < service.getParameters().size(); i++) {
                    DataParameter parameter = service.getParameters().get(i);

                    if (parameter.getDirection() == Direction.OUT) {
                        return cs.getObject(i + 1);
                    }
                }

                throw new Exception("");
            }
        });
        return result;
        // --------------------------------------------------------------------
    }

}
