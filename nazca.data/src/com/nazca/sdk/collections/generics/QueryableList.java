package com.nazca.sdk.collections.generics;

import play.libs.F;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by davidmunozgaete on 04-07-14.
 */
public class QueryableList<T> extends ArrayList<T> implements List<T>, IQueryable<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public T first() throws Exception {
        try {
            return this.get(0);
        } catch (Exception ex) {
            throw new Exception(ex); //Index out of bound's Exception
        }
    }

    @Override
    public T firstOrDefault() {
        return (this.size() > 0) ? this.get(0) : null;
    }

    @Override
    public T last() throws Exception {
        try {
            return this.get(this.size() - 1);
        } catch (Exception ex) {
            throw new Exception(ex); //Index out of bound's Exception
        }
    }

    @Override
    public T lastOrDefault() {
        return (this.size() > 0) ? this.get(this.size() - 1) : null;
    }

    @Override
    public IQueryable<T> where(F.Function<T, Boolean> filterFunction) throws Exception {
        QueryableList<T> resultSet = new QueryableList<>();

        for (T element : this) {
            try {
                Boolean result = filterFunction.apply(element);
                if (result == true) {
                    resultSet.add(element);
                }
            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method

            }
        }

        return resultSet;
    }

    @Override
    public T single(F.Function<T, Boolean> filterFunction) throws Exception {

        for (T element : this) {
            try {
                Boolean result = filterFunction.apply(element);
                if (result == true) {
                    return element;
                }
            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

        throw new Exception();            //Index out of bound's Exception

    }

    @Override
    public T singleOrDefault(F.Function<T, Boolean> filterFunction) throws Exception {

        for (T element : this) {
            try {
                Boolean result = filterFunction.apply(element);
                if (result == true) {
                    return element;
                }
            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

        return null;

    }

    /**
     * @param expression
     * @throws Exception
     */
    @Override
    public void orderBy(final F.Function<T, Object> expression) throws Exception {

        Collections.sort(this, new java.util.Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                try {
                    Object value_ex1 = expression.apply(o1);
                    Object value_ex2 = expression.apply(o2);

                    //Comparador por Tipo
                    if (value_ex1 instanceof Integer) {

                        //--------------------------------------------------
                        //Comparacion por Numeros
                        Integer value1 = (Integer) value_ex1;
                        Integer value2 = (Integer) value_ex2;

                        if (value_ex1 == value_ex2) {
                            return 0;
                        } else {
                            return value1 < value2 ? -1 : 1;
                        }
                        //--------------------------------------------------

                    } else if (value_ex1 instanceof String) {

                        //--------------------------------------------------
                        //Comparacion por String
                        String value1 = (String) value_ex1;
                        String value2 = (String) value_ex2;

                        return value2.compareTo(value1);
                        //--------------------------------------------------

                    }

                } catch (Throwable ex) {
                }
                return 0;
            }
        });
    }

    /**
     * @param expression
     * @throws Exception|
     */
    @Override
    public void orderByDescending(final F.Function<T, Object> expression) throws Exception {

        this.orderBy(expression);
        Collections.reverse(this);
    }

    @Override
    public T each(F.Callback<T> delegate) throws Exception {

        for (T element : this) {
            try {
                delegate.invoke(element);
            } catch (Throwable ex) {
                throw new Exception(ex);        //Error in Delegate Method
            }
        }

        return null;

    }

    @Override
    public IQueryable<T> not(F.Function<T, Boolean> filterFunction) throws Exception, Exception {
        QueryableList<T> resultSet = new QueryableList<>();

        for (T element : this) {
            try {
                Boolean result = filterFunction.apply(element);
                if (result == false) {
                    resultSet.add(element);
                }
            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method

            }
        }

        return resultSet;
    }

    @Override
    public void remove(F.Function<T, Boolean> filterFunction) throws Exception {

        for (int index = this.size() - 1; index >= 0; index--) {
            try {
                Boolean result = filterFunction.apply(this.get(index));
                if (result == true) {
                    this.remove(index);
                }
            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

    }

    @Override
    public void delete(F.Function<T, Boolean> filterFunction) throws Exception {
        remove(filterFunction);
    }

    /*
     * @param expression
     * @throws Exception
     */
    @Override
    public IQueryable<T> distinct(final F.Function<T, Object> filterFunction) throws Exception, Exception {
        QueryableList<T> distincts = new QueryableList<>();

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
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

        return distincts;

    }

    @SuppressWarnings("unchecked")
    @Override
    public <L extends Number> L sum(F.Function<T, L> filterFunction) throws Exception {
        Number sum = 0;

        for (int index = this.size() - 1; index >= 0; index--) {
            try {
                Number result = filterFunction.apply(this.get(index));

                if (result instanceof Double) {
                    sum = new Double(sum.doubleValue() + result.doubleValue());
                } else if (result instanceof Integer || result instanceof Integer) {
                    sum = new Integer(sum.intValue() + result.intValue());
                } else if (result instanceof Float) {
                    sum = new Float(sum.floatValue() + result.floatValue());
                }


            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

        return (L) sum;

    }

    @SuppressWarnings("unchecked")
    @Override
    public <L extends Number> L min(F.Function<T, L> filterFunction) throws Exception {

        Number min = Float.MAX_VALUE;
        for (int index = this.size() - 1; index >= 0; index--) {
            try {
                Number result = filterFunction.apply(this.get(index));

                if (result.floatValue() < min.floatValue()) {
                    min = result;
                }

            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

        return (L) (min.floatValue() == Float.MAX_VALUE ? 0 : min);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <L extends Number> L max(F.Function<T, L> filterFunction) throws Exception {

        Number max = Float.MIN_VALUE;
        for (int index = this.size() - 1; index >= 0; index--) {
            try {
                Number result = filterFunction.apply(this.get(index));

                if (result.floatValue() < max.floatValue()) {
                    max = result;
                }

            } catch (Throwable ex) {
                throw new Exception(ex.getMessage());        //Error in Delegate Method
            }
        }

        return (L) (max.floatValue() == Float.MIN_VALUE ? 0 : max);
    }
}

