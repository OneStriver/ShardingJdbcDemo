package com.sunkaisens.shard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunkaisens.shard.entity.Role;
import com.sunkaisens.shard.mapper.RoleMapper;
import com.sunkaisens.shard.service.RoleService;

/**
 * 系统用户
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public void saveRole(Role role) {
		roleMapper.saveRole(role);
	}

	@Override
	public Integer countRole(String tableName) {
		return roleMapper.countRole(tableName);
	}
	
}
