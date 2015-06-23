package com.valentys.sdk.data.odata;

public class Field {
    private String name;
    private String specification;
    private String type;

    public Field(String name, String specification, String type) {
        this.name = name;
        this.specification = specification;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
