package com.harmony.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkuan
 */
@Data
@TableName(value = "t_order")
public class Order {

    @TableId
    private Long orderId;

    private Integer orderType;

    private Long userId;

    private String userName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
