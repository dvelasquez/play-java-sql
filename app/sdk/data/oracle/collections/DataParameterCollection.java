package sdk.data.oracle.collections;

public class DataParameterCollection extends sdk.data.collections.DataParameterCollection {


    private static final long serialVersionUID = 1L;


    public void addOut(String parameterName, sdk.data.oracle.DbTypes type) {
        super.addOut(parameterName, type.getDbtype());
    }

    public void addInOut(String parameterName, sdk.data.oracle.DbTypes type) {
        super.addInOut(parameterName, type.getDbtype());
    }


}
