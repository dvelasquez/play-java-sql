package com.valentys.sdk.data;

import com.valentys.sdk.data.collections.DataParameterCollection;

public class DataService implements Cloneable {

    //[start] Variables

    private DataParameterCollection parameters = null;
    private String storeProcedure = null;

    //[end] Variables

    public DataService(String storeProcedure) {
        this.storeProcedure = storeProcedure;
        this.parameters = new DataParameterCollection();
    }

    public DataService(String storeProcedure, DataParameterCollection parameters) {
        this.storeProcedure = storeProcedure;
        this.parameters = parameters;
    }

    //[start] Getter's And Setter's

    public DataParameterCollection getParameters() {
        return parameters;
    }

    public void setParameters(DataParameterCollection parameters) {
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
