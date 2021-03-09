package com.harmony.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harmony.shardingjdbc.entity.Order;
import com.harmony.shardingjdbc.mapper.OrderMapper;
import com.harmony.shardingjdbc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @describe
 * @author: wangkuan
 * @create: 2021-03-09 21:00:55
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

}
