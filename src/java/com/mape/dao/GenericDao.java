package com.mape.dao;

import com.mape.entity.Entity;
import java.util.List;
import java.util.Optional;

//CRUD
public interface GenericDao<T extends Entity> {

  Optional<T> findById(Long id);

  List<T> findAll();

  default List<T> findPartOfAll(int from, int count) {
    return findAll().subList(from, from + count);
  }

  void deleteById(Long id);

  void create(T entity);

  void update(T entity);

  int count();

  default void save(T entity) {
    if (entity.isSaved()) {
      update(entity);
    } else {
      create(entity);
    }
  }
}
