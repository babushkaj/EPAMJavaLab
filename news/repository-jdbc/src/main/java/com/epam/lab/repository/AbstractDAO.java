package com.epam.lab.repository;

import java.util.List;

public interface AbstractDAO<T> {
    long insert(T entity);

    T select(long id);

    List<T> selectAll();

    void update(T entity);

    void delete(long id);

}
