package com.sunkaisens.shard.mapper;

import org.apache.ibatis.annotations.Param;

import com.sunkaisens.shard.entity.User;

/**
 * 说明：用户Mapper
 */
public interface UserMapper {

	public void saveUser(User user);
	
	public Integer countTableData(@Param("tableName") String tableName);

}
