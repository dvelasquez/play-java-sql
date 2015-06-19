package sdk.data;

public class DataParameter {

    public enum Direction {
        OUT, IN, INOUT
    }

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

    // [start] Variables

    private String name;
    private Object value;
    private Direction direction;
    private int sqltype;

    // [end]

    // [start] Getter's And Setter's

    public Direction getDirection() {
        return direction;
    }

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

    // [end]

}