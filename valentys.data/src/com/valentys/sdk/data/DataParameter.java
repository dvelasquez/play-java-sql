package com.valentys.sdk.data;

public class DataParameter {

    private String name;
    private Object value;
    private Direction direction;

    // [start] Variables
    private int sqltype;
    public DataParameter(String ParameterName, Object Value) {
        this.name = ParameterName;
        this.value = Value;
        this.direction = Direction.IN;
    }
    public DataParameter(String ParameterName, Direction direction, int sqltype) {
        this.name = ParameterName;
        this.sqltype = sqltype;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    // [end]

    // [start] Getter's And Setter's

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String parameterName) {
        this.name = parameterName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getSqltype() {
        return sqltype;
    }

    public void setSqltype(int sqltype) {
        this.sqltype = sqltype;
    }

    public enum Direction {
        OUT, IN, INOUT
    }

    // [end]

}