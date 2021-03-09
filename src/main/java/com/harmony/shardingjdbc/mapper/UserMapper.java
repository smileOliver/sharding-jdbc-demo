package com.harmony.shardingjdbc.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harmony.shardingjdbc.config.DataSourceConfig;
import com.harmony.shardingjdbc.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 若该表需要分片则添加上注解 @DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
 *
 * @author wangkuan
 */
@DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
