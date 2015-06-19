package sdk.collections.generics;

import play.libs.F;

public interface IQueryable<T> {

    T first() throws Exception;

    T firstOrDefault();

    T last() throws Exception;

    T lastOrDefault();

    T single(F.Function<T, Boolean> filterFunction) throws Exception;

    T singleOrDefault(F.Function<T, Boolean> filterFunction) throws Exception;

    public <L extends Number> L sum(F.Function<T, L> filterFunction) throws Exception;

    <L extends Number> L max(F.Function<T, L> filterFunction) throws Exception;

    <L extends Number> L min(F.Function<T, L> filterFunction) throws Exception;

    IQueryable<T> distinct(F.Function<T, Object> filterFunction) throws Exception;

    void remove(F.Function<T, Boolean> filterFunction) throws Exception;

    void delete(F.Function<T, Boolean> filterFunction) throws Exception;

    void orderBy(F.Function<T, Object> expression) throws Exception;

    void orderByDescending(F.Function<T, Object> expression) throws Exception;

    T each(F.Callback<T> delegate) throws Exception;

    IQueryable<T> where(F.Function<T, Boolean> filterFunction) throws Exception;

    IQueryable<T> not(F.Function<T, Boolean> filterFunction) throws Exception;


}
