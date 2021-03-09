package com.harmony.shardingjdbc.sharding.Algorithm;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.Date;

/**
 * @describe 范围分片算法，用于BETWEEN
 * @author: wangkuan
 * @create: 2021-03-08 16:59:32
 **/
public class RangeTableShardingAlgorithm implements RangeShardingAlgorithm<Date> {
    @Override
    public Collection<String> doSharding(Collection<String> tableNames, RangeShardingValue<Date> shardingValue) {

        return null;
    }
}
