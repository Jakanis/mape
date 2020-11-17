package com.mape.service.impl;

import com.mape.dao.RoleDao;
import com.mape.entity.Role;
import com.mape.service.RoleService;
import java.util.Optional;

public class RoleServiceImpl extends GenericServiceImpl<Role> implements
    RoleService {

  RoleDao roleDao;

  public RoleServiceImpl(RoleDao roleDao) {
    this.genericDao = roleDao;
    this.roleDao = roleDao;
  }

  @Override
  public Optional<Role> findByName(String name) {
    return roleDao.findByName(name);
  }

  @Override
  public void create(Role entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void update(Role entity) {
    throw new UnsupportedOperationException();
  }
}
