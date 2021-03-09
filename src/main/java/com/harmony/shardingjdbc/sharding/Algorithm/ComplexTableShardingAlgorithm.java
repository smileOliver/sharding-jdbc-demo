package com.harmony.shardingjdbc.sharding.Algorithm;

import com.google.common.collect.Lists;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;


/**
 * @describe 复合分片算法示例
 * @author: wangkuan
 * @create: 2021-03-08 17:00:46
 **/
public class ComplexTableShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {

    private final Logger logger = LoggerFactory.getLogger(ComplexTableShardingAlgorithm.class);

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, ComplexKeysShardingValue<String> shardingValue) {
        Map<String, Collection<String>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.containsKey("user_id")) {
            Collection<String> tradeMasterNos = columnNameAndShardingValuesMap.get("user_id");
            String tradeMasterNo = tradeMasterNos.iterator().next();
            String datasourceSuffix = tradeMasterNo.substring(0, 1);
            for (String availableTargetName : tableNames) {
                if (availableTargetName.endsWith(datasourceSuffix)) {
                    return Lists.newArrayList(availableTargetName);
                }
            }
        }
        if (columnNameAndShardingValuesMap.containsKey("order_id")) {
            Collection<String> payOrderNos = columnNameAndShardingValuesMap.get("order_id");
            String payOrderNo = payOrderNos.iterator().next();
            String datasourceSuffix = payOrderNo.substring(0, 1);
            for (String availableTargetName : tableNames) {
                if (availableTargetName.endsWith(datasourceSuffix)) {
                    return Lists.newArrayList(availableTargetName);
                }
            }
        }
        throw new UnsupportedOperationException();
    }
}
