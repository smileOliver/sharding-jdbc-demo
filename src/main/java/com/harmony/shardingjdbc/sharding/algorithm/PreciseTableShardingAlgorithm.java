package com.harmony.shardingjdbc.sharding.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @describe 精确分片算法
 * @author: wangkuan
 * @create: 2021-03-10 10:00:14
 **/
@Service
public class PreciseTableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * availableTargetNames 所有分片库的集合
     * shardingValue 为分片属性，其中 logicTableName 为逻辑表，columnName 分片健（字段），value 为从 SQL 中解析出的分片健的值
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String table : availableTargetNames) {
            if (table.endsWith(String.valueOf((shardingValue.getValue()) % availableTargetNames.size()))) {
                return table;
            }
        }
        throw new UnsupportedOperationException();
    }
}
