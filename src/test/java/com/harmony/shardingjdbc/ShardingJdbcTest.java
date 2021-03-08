package com.harmony.shardingjdbc;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harmony.shardingjdbc.entity.Order;
import com.harmony.shardingjdbc.entity.OrderItem;
import com.harmony.shardingjdbc.entity.User;
import com.harmony.shardingjdbc.entity.UserConfig;
import com.harmony.shardingjdbc.mapper.OrderItemMapper;
import com.harmony.shardingjdbc.mapper.OrderMapper;
import com.harmony.shardingjdbc.mapper.UserConfigMapper;
import com.harmony.shardingjdbc.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class ShardingJdbcTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConfigMapper userConfigMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    void contextLoads() {
        for (int i = 80; i < 81; i++) {
            User user = new User();
            user.setName("张牛" + i);
            user.setCityId(i);
            user.setEmail("8888888@163.com");
            user.setSex(2);
            user.setPassword("010000");
            user.setPhone("199299299");
            userMapper.insert(user);
        }

    }

    @Test
    public void queryList() {
        Page page = new Page();
        page.setCurrent(2);
        page.setSize(5);
        IPage<User> pages = userMapper.selectPage(page, Wrappers.<User>lambdaQuery()
                .eq(User::getSex, 2));
        pages.getRecords().forEach(s -> {
            System.out.println(JSONObject.toJSONString(s));
        });
    }

    @Test
    public void queryOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 1355508394484801537L);
        User user = userMapper.selectOne(queryWrapper);
        System.out.printf(JSONObject.toJSONString(user));
    }

    @Test
    public void insertUserConfigTest() {
        UserConfig userConfig = new UserConfig();
        userConfig.setUserId(1999459925L);
        userConfig.setName("张牛3");
        userConfig.setContent("这是个配置信息");
        userConfig.setConfigDesc("描述一下");
        userConfigMapper.insert(userConfig);
    }

    @Test
    public void addOrderTest() {
        Long userId = 1367392654716035073L;
        String userName = "张牛";
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderType(10);
        order.setUserName(userName);
        orderMapper.insert(order);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getOrderId());
        orderItem.setUserId(userId);
        orderItem.setUserName(userName);
        orderItem.setType(10);
        orderItem.setGoodsId(10000029L);
        orderItem.setGoodsName("吊烧鸡");
        orderItem.setGoodsCount(1);
        orderItem.setGoodsPrice(new BigDecimal("100.00"));
        orderItemMapper.insert(orderItem);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrderId(order.getOrderId());
        orderItem2.setUserId(userId);
        orderItem2.setUserName(userName);
        orderItem2.setType(10);
        orderItem2.setGoodsId(10000030L);
        orderItem2.setGoodsName("吊烧鸭");
        orderItem2.setGoodsCount(2);
        orderItem2.setGoodsPrice(new BigDecimal("120.00"));
        orderItemMapper.insert(orderItem2);

    }

}
