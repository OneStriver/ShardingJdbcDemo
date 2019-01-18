package com.sunkaisens.shard.service;

import com.sunkaisens.shard.entity.User;

/** 
 * 用户接口类
 */
public interface UserService {
	
	public void saveUser(User user);
	
	public Integer countTableData(String tableName);
	
}
