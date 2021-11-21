package akvelon.zuora.denysenko.persistence;

import java.util.List;

public interface Repository<T> {

    T create(T t);

    T getById(long id);

    T update(T t);

    T deleteById(long id);

    List<T> getList(String sort, String order);

    T getByName(String name);

}
