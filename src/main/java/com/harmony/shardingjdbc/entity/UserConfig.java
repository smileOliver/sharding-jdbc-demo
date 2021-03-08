package com.harmony.shardingjdbc.entity;

import lombok.Data;

@Data
public class UserConfig extends BaseEntity {

    private Long userId;

    private String name;

    private String content;

    private String configDesc;

}
