package sdk.data.collections;

import java.util.ArrayList;

public class DataParameterCollection extends ArrayList<sdk.data.DataParameter> implements AutoCloseable, Cloneable {

    private static final long serialVersionUID = 1L;

    public void add(String parameterName) {
        super.add(new sdk.data.DataParameter(parameterName, null));
    }

    public <T> void add(String parameterName, T value) {
        super.add(new sdk.data.DataParameter(parameterName, value));
    }

    public void addOut(String parameterName, int sqlType) {
        super.add(new sdk.data.DataParameter(parameterName, sdk.data.DataParameter.Direction.OUT, sqlType));
    }

    public void addInOut(String parameterName, int sqlType) {
        super.add(new sdk.data.DataParameter(parameterName, sdk.data.DataParameter.Direction.INOUT, sqlType));
    }

    @Override
    public void close() throws Exception {
        /*this.clear();*/ // Implements the "Auto Resource Management"
    }

    public Object clone() {
        return super.clone();
    }
}
