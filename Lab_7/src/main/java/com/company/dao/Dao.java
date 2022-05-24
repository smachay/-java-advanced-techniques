package com.company.dao;

import java.util.ArrayList;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(int id);

    ArrayList<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
