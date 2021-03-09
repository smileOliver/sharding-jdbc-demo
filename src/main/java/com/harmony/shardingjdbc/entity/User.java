package com.harmony.shardingjdbc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户表")
public class User extends BaseEntity {

    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("电子邮箱")
    private String email;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("城市ID")
    private Integer cityId;
    @ApiModelProperty("性别")
    private Integer sex;


}
