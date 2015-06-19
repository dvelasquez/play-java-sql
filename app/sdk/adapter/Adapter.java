package sdk.adapter;

import sdk.data.DataService;


public abstract class Adapter extends play.mvc.Controller {


    private static sdk.data.IDataActions connection = null;

    protected static sdk.data.IDataActions getConnection() {
        if (connection == null) {
            connection = new sdk.data.DataConnectionFactory();
        }
        return connection;
    }

    // [end]
    // [start] IDataActions Implementation
    public static void ExecuteAction(DataService service) throws Exception {
        getConnection().ExecuteAction(service);
    }

    public static sdk.data.EntityRepository ExecuteQuery(DataService service) throws Exception {
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
