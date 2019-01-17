package com.sunkaisens.shard.mapper;

import org.apache.ibatis.annotations.Param;

import com.sunkaisens.shard.entity.Role;

/**
 * 说明：角色Mapper
 */
public interface RoleMapper {

	public void saveRole(Role role);

	public Integer countRole(@Param("tableName") String tableName);

}
