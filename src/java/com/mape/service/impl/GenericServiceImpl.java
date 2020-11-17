package com.mape.service.impl;

import com.mape.dao.GenericDao;
import com.mape.entity.Entity;
import com.mape.service.GenericService;
import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<T extends Entity> implements GenericService<T> {

  GenericDao<T> genericDao;

  @Override
  public Optional<T> findById(Long id) {
    return genericDao.findById(id);
  }

  @Override
  public List<T> findPartOfAll(int from, int count) {
    return genericDao.findPartOfAll(from, count);
  }

  @Override
  public List<T> findAll() {
    return genericDao.findAll();
  }

  @Override
  public void deleteById(Long id) {
    genericDao.deleteById(id);
  }

  @Override
  public int count() {
    return genericDao.count();
  }

  @Override
  public void create(T entity) {
    genericDao.create(entity);
  }

  @Override
  public void update(T entity) {
    genericDao.update(entity);
  }

  @Override
  public void save(T entity) {
    genericDao.save(entity);
  }
}
