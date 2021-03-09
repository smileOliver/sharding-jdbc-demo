package com.harmony.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wangkuan
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 5591173598094955862L;

    @ApiModelProperty("id")
    @TableId
    private Long id;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModified;
}
