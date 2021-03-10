package com.harmony.shardingjdbc.sharding.Algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @describe Hint方式分片策略(强制路由)
 * @author: wangkuan
 * @create: 2021-03-08 17:02:02
 **/
@Slf4j
@Service
public class HintTableShardingAlgorithm implements HintShardingAlgorithm<Integer> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, HintShardingValue<Integer> shardingValue) {
        List<String> shardingResult = Lists.newArrayList();
        for (String table : tableNames) {
            // hint分片算法的ShardingValue有两种具体类型:
            // ListShardingValue和RangeShardingValue
            // 使用哪种取决于HintManager.addDatabaseShardingValue(String, String, ShardingOperator,...),ShardingOperator的类型
            for (Integer value : shardingValue.getValues()) {
                if (table.endsWith(String.valueOf(value % 2))) {
                    shardingResult.add(table);
                }
            }
        }
        return shardingResult;
    }
}
