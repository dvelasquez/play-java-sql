package sdk.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EntityRepository {
    // region Variables

    private java.util.ArrayList<ResultSet> rawData;
    private java.util.Map<Class<?>, EntityTable<?>> caching; // Cache Pattern

    // endregion Variables

    // region Constructor's
    public EntityRepository(ResultSet rawData) {
        this.rawData = new java.util.ArrayList<>();
        this.rawData.add(rawData);
    }

    public EntityRepository(java.util.ArrayList<ResultSet> rawData) {
        this.rawData = rawData;
    }
    // endregion Constructor's

    // region Getter's And Setter's
    private java.util.Map<Class<?>, EntityTable<?>> getCaching() {
        if (caching == null) {
            caching = new java.util.HashMap<>();
        }
        return caching;
    }

    public java.util.ArrayList<ResultSet> getRawData() {
        return rawData;
    }

    // endregion Getter's And Setter's

    // region Method's
    public <T> EntityTable<T> GetModel(Class<T> model) throws Exception {
        return GetModel(model, 0);
    }

    @SuppressWarnings("unchecked")
    public <T> EntityTable<T> GetModel(Class<T> model, int index) throws Exception {
        // Responsive Cache Pattern
        if (this.getCaching().containsKey(model)) {
            return (EntityTable<T>) this.getCaching().get(model);
        }

        EntityTable<T> reflectedClass = new EntityTable<T>();

        if (this.getRawData() != null) {
            FillEntity(reflectedClass, model, index);
        }

        return reflectedClass;
    }

    @SuppressWarnings("unchecked")
    private <T> T FillEntity(EntityTable<T> table, Class<T> model, int index) throws Exception {
        String columnName = "";
        java.lang.reflect.Type classType = null;
        java.lang.reflect.Type dbType = null;

        try {

            if (this.getRawData() != null) {
                while (this.getRawData().get(index).next()) {

                    Object instance = model.newInstance();
                    for (Field fieldVo : model.getDeclaredFields()) {

                        columnName = "";
                        classType = null;
                        dbType = null;

                        // -------------------------------------------
                        // Capitalize the first letter of the property
                        String propNameCap = fieldVo.getName().substring(0, 1).toUpperCase() + fieldVo.getName().substring(1);
                        Method mtdObj = model.getMethod("set" + propNameCap, fieldVo.getType());

                        Object nodeValue = null;

                        // Get object Value (reflect itself mark the correct
                        // type)
                        try {
                            nodeValue = this.getRawData().get(index).getObject(propNameCap);
                        } catch (Exception ex) {
                            // column not found :S ; Dont throw Exception
                        }

                        if (nodeValue != null) {
                            // For logging into the Error Framework
                            columnName = propNameCap;
                            classType = fieldVo.getType();
                            dbType = nodeValue.getClass();

                            BoxValue(model.getName(), propNameCap, nodeValue, dbType, classType, mtdObj, instance);
                        }
                        // -------------------------------------------

                    }
                    table.add((T) (instance));

                }
            }
            // Add to the caching
            this.getCaching().put(model, (EntityTable<?>) table);

        } catch (SQLException | InstantiationException | IllegalAccessException | SecurityException | NoSuchMethodException | IllegalArgumentException ex) {
            //throw new Exception(3000, model.getName(), columnName, classType, dbType, ex.getMessage(), ex.getClass().getSimpleName());
            throw new Exception(ex);
        }

        return null;
    }

    public abstract void BoxValue(String classname, String columnName, Object nodeValue, java.lang.reflect.Type databaseType, java.lang.reflect.Type fieldType, Method methodInvoker, Object entity) throws Exception;

    // endregion Method's
}
