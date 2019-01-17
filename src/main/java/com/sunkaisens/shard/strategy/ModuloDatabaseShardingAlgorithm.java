package com.sunkaisens.shard.strategy;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @Description: 分库规则
 */
public class ModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Integer> {

    @Override
    public String doEqualSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
    	System.err.println(">>>>>>>>分库规则(doEqualSharding方法)>>>>>>>>");
        for (String databaseName : databaseNames) {
        	System.err.println("当前数据写入的库名:"+databaseName);
        	return databaseName;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
    	System.err.println(">>>>>>>>分库规则(doInSharding方法)>>>>>>>>");
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        for (Integer value : shardingValue.getValues()) {
            for (String tableName : databaseNames) {
                if (tableName.endsWith(value % 2 + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> databaseNames, ShardingValue<Integer> shardingValue) {
    	System.err.println(">>>>>>>>分库规则(doBetweenSharding方法)>>>>>>>>");
        Collection<String> result = new LinkedHashSet<>(databaseNames.size());
        Range<Integer> range = (Range<Integer>) shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : databaseNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
