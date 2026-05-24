package com.vaxtrack.database.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic CRUD contract for all DAO classes.
 * Demonstrates: Interface + Generics + Polymorphism
 */
public interface CrudDAO<T> {
    void insert(T entity) throws SQLException;
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(int id) throws SQLException;
}
