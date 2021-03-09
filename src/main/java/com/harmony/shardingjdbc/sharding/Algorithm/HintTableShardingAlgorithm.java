package com.harmony.shardingjdbc.sharding.Algorithm;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;

/**
 * @describe Hint方式分片策略
 * @author: wangkuan
 * @create: 2021-03-08 17:02:02
 **/
public class HintTableShardingAlgorithm implements HintShardingAlgorithm<String> {
    @Override
    public Collection<String> doSharding(Collection<String> tableNames, HintShardingValue<String> shardingValue) {
        return null;
    }
}
