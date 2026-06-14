package br.com.avcar.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T insert(T e);
    void update(T e);
    void deleteById(ID id);
}
