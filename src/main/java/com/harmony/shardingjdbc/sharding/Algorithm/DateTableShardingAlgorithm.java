package com.harmony.shardingjdbc.sharding.Algorithm;

import cn.hutool.core.date.DateUtil;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * @describe 精确分片算法 按月分表
 * @author: wangkuan
 * @create: 2021-03-08 16:35:16
 **/
@Service
public class DateTableShardingAlgorithm implements PreciseShardingAlgorithm<Date> {
    private final Logger logger = LoggerFactory.getLogger(DateTableShardingAlgorithm.class);

    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Date> shardingValue) {
        Date creatTime = shardingValue.getValue();
        if (creatTime != null) {
            //查询所在月初日期后缀 例如：2021-03-01
            String mondayStr = DateUtil.beginOfMonth(creatTime).toDateStr();
            for (String table : tableNames) {
                if (table.endsWith(mondayStr)) {
                    return table;
                }
            }
        }
        throw new UnsupportedOperationException();
    }

}
