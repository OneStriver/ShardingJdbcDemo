package com.sunkaisens.shard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class GlobalParamConfig {

	private Integer maxDataCount;
	private String userAllTableName;
	private String roleAllTableName;

	public Integer getMaxDataCount() {
		return maxDataCount;
	}

	public void setMaxDataCount(Integer maxDataCount) {
		this.maxDataCount = maxDataCount;
	}

	public String getUserAllTableName() {
		return userAllTableName;
	}

	public void setUserAllTableName(String userAllTableName) {
		this.userAllTableName = userAllTableName;
	}

	public String getRoleAllTableName() {
		return roleAllTableName;
	}

	public void setRoleAllTableName(String roleAllTableName) {
		this.roleAllTableName = roleAllTableName;
	}

}
