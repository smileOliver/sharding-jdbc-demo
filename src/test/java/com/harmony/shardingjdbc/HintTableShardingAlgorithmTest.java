package com.harmony.shardingjdbc;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.harmony.shardingjdbc.entity.Order;
import com.harmony.shardingjdbc.service.OrderService;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe hint分片算法测试
 * @author: wangkuan
 * @create: 2021-03-10 11:03:30
 **/
@SpringBootTest
public class HintTableShardingAlgorithmTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void test() {
        //Hint分片算法必须使用HintManager工具类
        HintManager hintManager = HintManager.getInstance();
        hintManager.addTableShardingValue("t_order", 1);
        // 直接指定对应具体的数据库
        //hintManager.setDatabaseShardingValue(1);
        //在读写分离数据库中，Hint 可以强制读主库（主从复制是存在一定延时，但在业务场景中，可能更需要保证数据的实时性）
        //hintManager.setMasterRouteOnly();
        List<Order> orderList = new ArrayList<>();
        orderList = orderService.list(Wrappers.<Order>lambdaQuery().eq(Order::getUserId, 136739252332238L));
        System.out.println(JSONObject.toJSONString(orderList));
    }

}
