package com.mape.service;

import com.mape.entity.Entity;
import java.util.List;
import java.util.Optional;

public interface GenericService<T extends Entity> {

  void create(T entity);

  Optional<T> findById(Long id);

  List<T> findAll();

  List<T> findPartOfAll(int from, int count);

  void update(T entity);

  void deleteById(Long id);

  int count();

  default void save(T entity) {
    if (entity.isSaved()) {
      update(entity);
    } else {
      create(entity);
    }
  }

}
