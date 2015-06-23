package com.valentys.sdk.data.oracle;

public enum DbTypes {
    CURSOR(-10),
    BIGINT(-5),
    BIT(-7),
    BLOB(2004),
    CLOB(2005),
    BOOLEAN(16),
    CHAR(1),
    DATE(91),
    DECIMAL(3),
    DOUBLE(8),
    FLOAT(6),
    INTEGER(4),
    LONGVARCHAR(-1),
    NULL(0),
    NUMBER(2),
    RAW(-2),
    SMALLINT(5),
    TIME(92),
    TIMESTAMP(93),
    TINYINT(-6),
    VARBINARY(-3),
    VARCHAR(12);

    private final int dbtype;

    DbTypes(int dbtype) {
        this.dbtype = dbtype;
    }

    public int getDbtype() {
        return dbtype;
    }
}
