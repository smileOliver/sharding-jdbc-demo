package com.harmony.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harmony.shardingjdbc.entity.User;
import com.harmony.shardingjdbc.mapper.UserMapper;
import com.harmony.shardingjdbc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @describe
 * @author: wangkuan
 * @create: 2021-03-09 17:09:29
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

}
