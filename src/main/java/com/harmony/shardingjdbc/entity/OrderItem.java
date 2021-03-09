package com.harmony.shardingjdbc.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wangkuan
 */
@Data
public class OrderItem {

    private Long orderId;

    private Integer type;

    private Long userId;

    private String userName;

    private Long goodsId;

    private String goodsName;

    private Integer goodsCount;

    private BigDecimal goodsPrice;

}
