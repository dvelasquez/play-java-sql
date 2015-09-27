package com.nazca.sdk.data.oracle.collections;

import com.nazca.sdk.data.oracle.DbTypes;

public class DataParameterCollection extends com.nazca.sdk.data.collections.DataParameterCollection {


    private static final long serialVersionUID = 1L;


    public void addOut(String parameterName, DbTypes type) {
        super.addOut(parameterName, type.getDbtype());
    }

    public void addInOut(String parameterName, DbTypes type) {
        super.addInOut(parameterName, type.getDbtype());
    }


}
