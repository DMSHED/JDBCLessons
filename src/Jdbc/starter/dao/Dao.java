package Jdbc.starter.dao;

import Jdbc.starter.entity.EmployeeEntity;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    boolean delete(K id);

    void update(E employeeEntity);

    E save(E employeeEntity);

    Optional<E> findById(K id);

    List<E> findAll();


}
