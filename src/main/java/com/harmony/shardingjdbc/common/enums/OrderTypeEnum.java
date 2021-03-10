package com.harmony.shardingjdbc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @describe 订单类型枚举
 * @author: wangkuan
 * @create: 2021-03-10 09:37:54
 **/
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    PURCHASE_ORDER(10, "purchase_order", "采购订单"),
    SALE_ORDER(20, "sale_order", "销售订单");

    private final Integer value;
    private final String code;
    private final String desc;
}
