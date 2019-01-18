package com.sunkaisens.shard.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;
import com.sunkaisens.shard.config.GlobalParamConfig;
import com.sunkaisens.shard.config.SpringContextHolder;
import com.sunkaisens.shard.service.UserService;

/**
 * @Description: 分表规则
 */
public class ModuloTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Integer> {
	
	//存储有数据但是还没有达到阈值的表
	private List<String> overTableNameList = new ArrayList<String>();
	private List<String> allTableNameList = new ArrayList<String>();
	private GlobalParamConfig moduleMenuConfig = (GlobalParamConfig) SpringContextHolder.getBean(GlobalParamConfig.class);
	private UserService commonService = (UserService) SpringContextHolder.getBean(UserService.class);
	
    @Override
    public String doEqualSharding(Collection<String> tableNames, ShardingValue<Integer> shardingValue) {
    	System.err.println(">>>>>>>>分表规则(doEqualSharding方法)>>>>>>>>");
    	overTableNameList.clear();
    	allTableNameList.clear();
    	String firstTableName = (String)(tableNames.toArray())[0];
    	if(firstTableName.startsWith("user")) {
    		Integer maxDataCount = moduleMenuConfig.getUserMaxDataCount();
            for (String tableName : tableNames) {
            	Integer select = commonService.countTableData(tableName);
            	if(select>=maxDataCount){
            		overTableNameList.add(tableName);
            	}
            	allTableNameList.add(tableName);
            }
            allTableNameList.removeAll(overTableNameList);
            String returnTableName = allTableNameList.get(0);
            System.err.println("当前数据写入的User表名为:"+returnTableName);
            return returnTableName;
		} else if (firstTableName.startsWith("role")) {
			Integer maxDataCount = moduleMenuConfig.getRoleMaxDataCount();
			for (String tableName : tableNames) {
				Integer select = commonService.countTableData(tableName);
				if (select >= maxDataCount) {
					overTableNameList.add(tableName);
				}
				allTableNameList.add(tableName);
			}
			allTableNameList.removeAll(overTableNameList);
			String returnTableName = allTableNameList.get(0);
			System.err.println("当前数据写入的Role表名为:" + returnTableName);
			return returnTableName;
		}
    	throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<Integer> shardingValue) {
    	System.err.println(">>>>>>>>分表规则(doInSharding方法)>>>>>>>>");
        Collection<String> result = new LinkedHashSet<>(tableNames.size());
        for (Integer value : shardingValue.getValues()) {
            for (String tableName : tableNames) {
                if (tableName.endsWith(value % 2 + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> tableNames, ShardingValue<Integer> shardingValue) {
    	System.err.println(">>>>>>>>分表规则(doBetweenSharding方法)>>>>>>>>");
        Collection<String> result = new LinkedHashSet<>(tableNames.size());
        Range<Integer> range = (Range<Integer>) shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : tableNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
