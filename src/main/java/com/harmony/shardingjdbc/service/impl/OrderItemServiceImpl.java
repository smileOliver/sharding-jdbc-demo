package com.harmony.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harmony.shardingjdbc.entity.OrderItem;
import com.harmony.shardingjdbc.mapper.OrderItemMapper;
import com.harmony.shardingjdbc.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @describe
 * @author: wangkuan
 * @create: 2021-03-09 21:04:06
 **/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

}
