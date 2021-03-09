package com.harmony.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harmony.shardingjdbc.entity.UserConfig;
import com.harmony.shardingjdbc.mapper.UserConfigMapper;
import com.harmony.shardingjdbc.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @describe
 * @author: wangkuan
 * @create: 2021-03-09 17:29:31
 **/
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {

    @Autowired
    private UserConfigMapper userConfigMapper;

}
