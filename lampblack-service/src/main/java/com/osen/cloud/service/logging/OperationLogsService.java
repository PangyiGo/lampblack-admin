package com.osen.cloud.service.logging;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.logs.OperationLogs;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 17:45
 * Description: 系统操作日志登记服务接口
 */
public interface OperationLogsService extends IService<OperationLogs> {

    /**
     * 插入操作记录
     *
     * @param operationLogs 实体
     */
    void insertOperationLogs(OperationLogs operationLogs);

    /**
     * 查询指定用户操作日志记录
     *
     * @param params 参数
     * @return 信息
     */
    Map<String, Object> findAllLogsToAccount(Map<String, Object> params);
}
