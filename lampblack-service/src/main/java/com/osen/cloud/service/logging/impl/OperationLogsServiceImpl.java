package com.osen.cloud.service.logging.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.system_logs.OperationLogs;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.model.logging.OperationLogsMapper;
import com.osen.cloud.service.logging.OperationLogsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 17:47
 * Description: 系统操作日志登记服务接口
 */
@Service
public class OperationLogsServiceImpl extends ServiceImpl<OperationLogsMapper, OperationLogs> implements OperationLogsService {

    @Override
    public void insertOperationLogs(OperationLogs operationLogs) {
        operationLogs.setCreateTime(LocalDateTime.now());
        super.save(operationLogs);
    }

    @Override
    public Map<String, Object> findAllLogsToAccount(Map<String, Object> params) {
        // 参数
        String account = (String) params.get("account");
        int pageNumber = (int) params.get("pageNumber");
        // 查询条件
        Page<OperationLogs> logsPage = new Page<>(pageNumber, ConstUtil.PAGE_NUMBER);
        LambdaQueryWrapper<OperationLogs> queryWrapper = Wrappers.<OperationLogs>lambdaQuery().eq(OperationLogs::getAccount, account).orderByDesc(OperationLogs::getCreateTime);
        // 查询
        IPage<OperationLogs> logsIPage = super.page(logsPage, queryWrapper);
        Map<String, Object> resultMap = new HashMap<>(0);
        if (logsIPage.getTotal() <= 0) {
            resultMap.put("total", 0);
            resultMap.put("logs", null);
        } else {
            resultMap.put("total", logsIPage.getTotal());
            resultMap.put("logs", logsIPage.getRecords());
        }
        return resultMap;
    }
}
