package com.valentys.sdk.adapter;

import com.valentys.sdk.data.DataConnectionFactory;
import com.valentys.sdk.data.DataService;
import com.valentys.sdk.data.EntityRepository;
import com.valentys.sdk.data.IDataActions;


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
