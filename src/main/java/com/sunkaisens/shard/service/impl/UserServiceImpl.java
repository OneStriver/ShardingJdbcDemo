package com.sunkaisens.shard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunkaisens.shard.entity.User;
import com.sunkaisens.shard.mapper.UserMapper;
import com.sunkaisens.shard.service.UserService;

/**
 * 系统用户
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public void saveUser(User user) {
		userMapper.saveUser(user);
	}

	@Override
	public Integer countUser(String tableName) {
		return userMapper.countUser(tableName);
	}
	
}
