package com.osen.cloud.system.system_logging.controller;

import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.logging.OperationLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 17:57
 * Description: 操作日志记录控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class OperationLogsController {

    @Autowired
    private OperationLogsService operationLogsService;

    /**
     * 查询指定用户操作记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/logs/queryToAccount")
    public RestResult findAllLogsToAccount(@RequestBody Map<String, Object> params) {
        Map<String, Object> allLogsToAccount = operationLogsService.findAllLogsToAccount(params);
        return RestResultUtil.success(allLogsToAccount);
    }
}
