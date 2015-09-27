package com.nazca.sdk.adapter;

import com.nazca.sdk.data.DataConnectionFactory;
import com.nazca.sdk.data.DataService;
import com.nazca.sdk.data.EntityRepository;
import com.nazca.sdk.data.IDataActions;


public abstract class Adapter extends play.mvc.Controller {


    private static IDataActions connection = null;

    protected static IDataActions getConnection() {
        if (connection == null) {
            connection = new DataConnectionFactory();
        }
        return connection;
    }

    // [end]
    // [start] IDataActions Implementation
    public static void ExecuteAction(DataService service) throws Exception {
        getConnection().ExecuteAction(service);
    }

    public static EntityRepository ExecuteQuery(DataService service) throws Exception {
        return getConnection().ExecuteQuery(service);
    }

    public static Object ExecuteScalar(DataService service) throws Exception {
        return getConnection().ExecuteScalar(service);
    }

    public static Object ExecuteSql(DataService service) throws Exception {
        return getConnection().ExecuteSql(service);
    }
    // [end]
}
