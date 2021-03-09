package com.harmony.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @describe 仓库信息表
 * @author: wangkuan
 * @create: 2021-03-09 17:49:33
 **/
@ApiModel(value = "仓库信息表")
@Data
public class Warehouse extends BaseEntity {

    /**
     * 采用数据id自增
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("仓库名")
    private String warehouseName;

    @ApiModelProperty("仓库编码")
    private String warehouseCode;

    @ApiModelProperty("仓库地址")
    private String warehouseAddress;
}
