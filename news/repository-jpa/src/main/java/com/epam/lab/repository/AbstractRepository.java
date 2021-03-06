package com.epam.lab.repository;

import java.util.List;

public interface AbstractRepository<T> {

    long insert(T entity);

    T findById(long id);

    List<T> findAll(int from, int howMany);

    void update(T entity);

    void delete(long id);

    long countAll();

}
