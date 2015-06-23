package com.valentys.sdk.data.odata;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private Integer offset = 0;
    private Integer limit = 10;
    private Integer total;
    private List<Field> fields = new ArrayList<Field>();
    private List<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    public Result(int total) {
        this.total = total;
    }

    public Result(int offset, int limit, int total) {
        this.offset = offset;
        this.limit = limit;
        this.total = total;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getTotal() {
        return total;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<ArrayList<String>> getData() {
        return data;
    }

}
