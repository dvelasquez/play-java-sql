package com.nazca.sdk.data;

import com.nazca.sdk.data.oracle.ConnectionState;

import java.sql.CallableStatement;
import java.util.HashMap;

public interface IDataActions {

    String getConnectionName();

    void ExecuteAction(DataService service) throws Exception;

    EntityRepository ExecuteQuery(DataService service) throws Exception;

    EntityRepository ExecuteSql(DataService service) throws Exception;

    Object ExecuteScalar(DataService service) throws Exception;

    void AddParameter(CallableStatement cs, Integer index, DataParameter parameter) throws Exception;

    public boolean resetPoolNodes();

    public HashMap<String, ConnectionState> getNodesStates();

}
