package com.harmony.shardingjdbc.sharding.Algorithm;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @describe 复合分片算法示例
 * @author: wangkuan
 * @create: 2021-03-08 17:00:46
 **/
@Service
public class ComplexTableShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, ComplexKeysShardingValue<Long> shardingValue) {
        // 得到每个分片健对应的值
        Collection<Long> orderIdValues = this.getShardingValue(shardingValue, "order_id");
        Collection<Long> userIdValues = this.getShardingValue(shardingValue, "user_id");

        List<String> shardingSuffix = new ArrayList<>();
        // 对两个分片健同时取模的方式分库
        for (Long userId : userIdValues) {
            for (Long orderId : orderIdValues) {
                String suffix = userId % 2 + "_" + orderId % 2;
                for (String table : tableNames) {
                    if (table.endsWith(suffix)) {
                        shardingSuffix.add(table);
                    }
                }
            }
        }
        return shardingSuffix;
    }

    private Collection<Long> getShardingValue(ComplexKeysShardingValue<Long> shardingValues, final String key) {
        Collection<Long> valueSet = new ArrayList<>();
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey(key)) {
            valueSet.addAll(columnNameAndShardingValuesMap.get(key));
        }
        return valueSet;
    }
}
