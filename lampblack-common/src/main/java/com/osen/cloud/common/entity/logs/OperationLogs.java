package com.osen.cloud.common.entity.logs;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 17:41
 * Description: 系统操作日志登记实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("operation_logs")
public class OperationLogs extends Model<OperationLogs> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String account;

    private String ip;

    private String address;

    private String description;

    private LocalDateTime createTime;
}
