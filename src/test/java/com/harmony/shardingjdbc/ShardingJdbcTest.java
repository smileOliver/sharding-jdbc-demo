package com.harmony.shardingjdbc;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harmony.shardingjdbc.entity.*;
import com.harmony.shardingjdbc.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class ShardingJdbcTest {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Test
    void insertUserTest() {
        for (int i = 85; i < 90; i++) {
            User user = new User();
            user.setName("张牛" + i);
            user.setCityId(i);
            user.setEmail("8888888@163.com");
            user.setSex(2);
            user.setPassword("010000");
            user.setPhone("199299299");
            userService.save(user);
        }
    }

    /**
     * 分页查询
     */
    @Test
    public void queryListByPage() {
        Page page = new Page();
        page.setCurrent(2);
        page.setSize(5);
        IPage<User> pages = userService.page(page, Wrappers.<User>lambdaQuery()
                .eq(User::getSex, 2));
        pages.getRecords().forEach(s -> {
            System.out.println(JSONObject.toJSONString(s));
        });
    }

    @Test
    public void queryOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 1355508394484801537L);
        User user = userService.getOne(queryWrapper);
        System.out.printf(JSONObject.toJSONString(user));
    }

    @Test
    public void insertUserConfigTest() {
        UserConfig userConfig = new UserConfig();
        userConfig.setUserId(2334234925L);
        userConfig.setName("白居易");
        userConfig.setContent("这是个配置信息");
        userConfig.setConfigDesc("描述一下");
        userConfigService.save(userConfig);
    }

    /**
     * 添加仓库信息
     * 不做分库分表
     */
    @Test
    public void insertWarehouseTest() {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseCode("CS_ZHONGTUO_03");
        warehouse.setWarehouseName("长沙中拓3号仓");
        warehouseService.save(warehouse);
    }

    @Test
    public void addOrderTest() {
        Long userId = 13673925471609899L;
        String userName = "皇阿玛";
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderType(10);
        order.setUserName(userName);
        orderService.save(order);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getOrderId());
        orderItem.setUserId(userId);
        orderItem.setUserName(userName);
        orderItem.setType(10);
        orderItem.setGoodsId(10044029L);
        orderItem.setGoodsName("臭豆腐");
        orderItem.setGoodsCount(1);
        orderItem.setGoodsPrice(new BigDecimal("100.00"));
        orderItemService.save(orderItem);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrderId(order.getOrderId());
        orderItem2.setUserId(userId);
        orderItem2.setUserName(userName);
        orderItem2.setType(10);
        orderItem2.setGoodsId(1002330L);
        orderItem2.setGoodsName("盐水鸭");
        orderItem2.setGoodsCount(2);
        orderItem2.setGoodsPrice(new BigDecimal("120.00"));
        orderItemService.save(orderItem2);

    }

}
