package com.harmony.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 5591173598094955862L;

    @TableId
    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
