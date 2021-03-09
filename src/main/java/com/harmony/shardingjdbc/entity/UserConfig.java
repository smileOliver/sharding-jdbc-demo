package com.harmony.shardingjdbc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户配置信息")
public class UserConfig extends BaseEntity {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("配置信息")
    private String content;

    @ApiModelProperty("配置描述")
    private String configDesc;

}
