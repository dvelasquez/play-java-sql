package sdk.data;

public class DataService implements Cloneable {

    //[start] Variables

    private sdk.data.collections.DataParameterCollection parameters = null;
    private String storeProcedure = null;

    //[end] Variables

    public DataService(String storeProcedure) {
        this.storeProcedure = storeProcedure;
        this.parameters = new sdk.data.collections.DataParameterCollection();
    }

    public DataService(String storeProcedure, sdk.data.collections.DataParameterCollection parameters) {
        this.storeProcedure = storeProcedure;
        this.parameters = parameters;
    }

    //[start] Getter's And Setter's

    public sdk.data.collections.DataParameterCollection getParameters() {
        return parameters;
    }

    public void setParameters(sdk.data.collections.DataParameterCollection parameters) {
        this.parameters = parameters;
    }

    public String getStoreProcedure() {
        return storeProcedure;
    }

    public void setStoreProcedure(String storeProcedure) {
        this.storeProcedure = storeProcedure;
    }

    //[end]

    //[start] Method's

    public Boolean HasParameters() {
        return (parameters != null && parameters.size() > 0);
    }

    //[end] Method's

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
