package com.mape.service;

import com.mape.entity.Role;
import java.util.Optional;

public interface RoleService extends GenericService<Role> {

  Optional<Role> findByName(String name);

}
