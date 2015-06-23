package com.valentys.sdk.data;

import javax.sql.rowset.serial.SerialClob;
import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class DefaultEntityRepository extends EntityRepository {

    public DefaultEntityRepository(java.sql.ResultSet rawData) {
        super(rawData);
    }

    public DefaultEntityRepository(java.util.ArrayList<java.sql.ResultSet> rawData) {
        super(rawData);
    }

    @Override
    public void BoxValue(String classname, String columnName, Object nodeValue, Type databaseType, Type fieldType, Method methodInvoker, Object entity) throws Exception {
        // Finally set the value into it's container field

        try {

            if (databaseType == SerialClob.class
                    && fieldType == String.class) {

                SerialClob clob = (SerialClob) nodeValue;
                StringBuilder sb = new StringBuilder();
                Reader reader = clob.getCharacterStream();
                BufferedReader br = new BufferedReader(reader);

                int b;
                while (-1 != (b = br.read())) {
                    sb.append((char) b);
                }

                nodeValue = sb.toString();
            }

            if (databaseType == java.math.BigDecimal.class
                    && fieldType == Integer.class) {
                nodeValue = ((java.math.BigDecimal) nodeValue).intValueExact();
            }

            if (databaseType == java.math.BigDecimal.class
                    && fieldType == Boolean.class) {
                nodeValue = ((java.math.BigDecimal) nodeValue).intValueExact() == 1;
            }

            methodInvoker.invoke(entity, nodeValue);

        } catch (Exception ex) {
            //throw new valentys.exceptions.FrameworkException(6200, columnName, fieldType.toString());
            throw new Exception(columnName + " - " + fieldType.toString());
        }


        try {
            methodInvoker.invoke(entity, nodeValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //throw new valentys.exceptions.FrameworkException(3000, classname, columnName, fieldType, databaseType, ex.getMessage(), ex.getClass().getSimpleName());
            throw new Exception(ex);
        }
    }


}
