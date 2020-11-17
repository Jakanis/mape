package com.mape.dao;

import com.mape.entity.Role;
import java.util.Optional;

//CRUD
public interface RoleDao extends GenericDao<Role> {

  Optional<Role> findByName(String name);
}
