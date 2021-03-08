package com.harmony.shardingjdbc.entity;

import lombok.Data;

@Data
public class User extends BaseEntity {

    private String name;
    private String phone;
    private String email;
    private String password;
    private Integer cityId;
    private Integer sex;


}
