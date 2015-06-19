package sdk.data;

import sdk.data.oracle.ConnectionState;

import java.sql.CallableStatement;
import java.util.HashMap;

public interface IDataActions {

	String getConnectionName();
	
    void ExecuteAction(sdk.data.DataService service) throws Exception;

    sdk.data.EntityRepository ExecuteQuery(sdk.data.DataService service) throws Exception;

    sdk.data.EntityRepository ExecuteSql(sdk.data.DataService service) throws Exception;

    Object ExecuteScalar(sdk.data.DataService service) throws Exception;

    void AddParameter(CallableStatement cs, Integer index, DataParameter parameter) throws Exception;

    public boolean resetPoolNodes();

    public HashMap<String, ConnectionState> getNodesStates();

}
