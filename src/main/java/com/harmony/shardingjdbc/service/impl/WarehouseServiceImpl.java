package com.harmony.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harmony.shardingjdbc.entity.Warehouse;
import com.harmony.shardingjdbc.mapper.WarehouseMapper;
import com.harmony.shardingjdbc.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @describe
 * @author: wangkuan
 * @create: 2021-03-09 17:52:33
 **/
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

}
