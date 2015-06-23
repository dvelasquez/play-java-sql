package com.valentys.sdk.data;

import com.valentys.sdk.collections.generics.IQueryable;
import com.valentys.sdk.collections.generics.QueryableList;
import org.simpleframework.xml.Root;
import play.libs.F;

@Root
public final class EntityTable<T> extends QueryableList<T> implements IQueryable<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public EntityTable<T> where(F.Function<T, Boolean> filterFunction) throws Exception {
        EntityTable<T> resultSet = new EntityTable<>();

        for (T element : this) {
            try {
                Boolean result = filterFunction.apply(element);
                if (result == true) {
                    resultSet.add(element);
                }
            } catch (Throwable ex) {
//                throw new sdk.exceptions.Exception(2100, element.getClass().getSimpleName(), ex.getMessage());        //Error in Delegate Method
                throw new Exception(ex.getMessage());        //Error in Delegate Method

            }
        }

        return resultSet;
    }

    @Override
    public EntityTable<T> not(F.Function<T, Boolean> filterFunction) throws Exception {
        EntityTable<T> resultSet = new EntityTable<>();

        for (T element : this) {
            try {
                Boolean result = filterFunction.apply(element);
                if (result == false) {
                    resultSet.add(element);
                }
            } catch (Throwable ex) {
//                throw new sdk.exceptions.Exception(2600, element.getClass().getSimpleName(), ex.getMessage());        //Error in Delegate Method
                throw new Exception(ex);        //Error in Delegate Method

            }
        }

        return resultSet;
    }

    /*
     * @param expression
     * @throws Exception
     */
    @Override
    public EntityTable<T> distinct(final F.Function<T, Object> filterFunction) throws Exception {
        EntityTable<T> distincts = new EntityTable<>();

        for (int index = this.size() - 1; index >= 0; index--) {
            try {
                T element = this.get(index);
                boolean finded = false;
                Object result = filterFunction.apply(element);

                for (int dIndex = distincts.size() - 1; dIndex >= 0; dIndex--) {
                    Object dresult = filterFunction.apply(distincts.get(dIndex));
                    if (result.equals(dresult)) {
                        finded = true;
                        break;
                    }
                }

                if (!finded) {
                    distincts.add(element);
                }

            } catch (Throwable ex) {
//                throw new sdk.exceptions.Exception(4100, this.get(index).getClass().getSimpleName(), ex.getMessage());        //Error in Delegate Method
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

        return distincts;

    }

}
