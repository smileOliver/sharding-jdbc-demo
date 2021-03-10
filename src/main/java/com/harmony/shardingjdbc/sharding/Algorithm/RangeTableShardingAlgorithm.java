package com.harmony.shardingjdbc.sharding.Algorithm;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @describe 范围分片算法，用于BETWEEN
 * @author: wangkuan
 * @create: 2021-03-08 16:59:32
 **/
@Service
public class RangeTableShardingAlgorithm implements RangeShardingAlgorithm<Integer> {
    @Override
    public Collection<String> doSharding(Collection<String> tableNames, RangeShardingValue<Integer> shardingValue) {

        Set<String> result = new LinkedHashSet<>();
        // between and 的起始值
        int lower = shardingValue.getValueRange().lowerEndpoint();
        int upper = shardingValue.getValueRange().upperEndpoint();
        // 循环范围计算分库逻辑
        for (int i = lower; i <= upper; i++) {
            for (String table : tableNames) {
                if (table.endsWith(String.valueOf(i % tableNames.size()))) {
                    result.add(table);
                }
            }
        }
        return result;
    }
}
