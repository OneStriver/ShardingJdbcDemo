package com.sunkaisens.shard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class GlobalParamConfig {

	private Integer userMaxDataCount;
	private String userAllTableName;
	private Integer roleMaxDataCount;
	private String roleAllTableName;

	public Integer getUserMaxDataCount() {
		return userMaxDataCount;
	}

	public void setUserMaxDataCount(Integer userMaxDataCount) {
		this.userMaxDataCount = userMaxDataCount;
	}

	public String getUserAllTableName() {
		return userAllTableName;
	}

	public void setUserAllTableName(String userAllTableName) {
		this.userAllTableName = userAllTableName;
	}

	public Integer getRoleMaxDataCount() {
		return roleMaxDataCount;
	}

	public void setRoleMaxDataCount(Integer roleMaxDataCount) {
		this.roleMaxDataCount = roleMaxDataCount;
	}

	public String getRoleAllTableName() {
		return roleAllTableName;
	}

	public void setRoleAllTableName(String roleAllTableName) {
		this.roleAllTableName = roleAllTableName;
	}

}
