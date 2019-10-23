package com.osen.cloud.system.logging.util;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.system_logs.OperationLogs;
import com.osen.cloud.service.logging.OperationLogsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 17:58
 * Description: 操作日志记录工具类
 */
@Component
@Slf4j
public class OperationLogsUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OperationLogsService operationLogsService;

    /**
     * 处理操作日志记录
     *
     * @param request 请求
     * @param message 提示
     * @param account 操作用户账号
     */
    @Async
    public void handlerOperation(HttpServletRequest request, String message, String account) {
        OperationLogs operationLogs = new OperationLogs();
        // 解析请求IP地址
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        operationLogs.setAccount(account);
        operationLogs.setDescription(message);
        operationLogs.setIp(ip);
        try {
            // 获取IP定制所属地
            String resJson = restTemplate.getForObject("http://ip-api.com/json/{ip}?lang=zh-CN", String.class, ip);
            IpVo ipVo = JSON.parseObject(resJson, IpVo.class);
            if (ipVo != null && "success".equals(ipVo.getStatus())) {
                String address = ipVo.getCountry() + "|" + ipVo.getRegionName() + "|" + ipVo.getCity();
                operationLogs.setAddress(address);
            } else {
                operationLogs.setAddress("内网访问");
            }
        } catch (Exception e) {
            operationLogs.setAddress("未知地址");
        }
        operationLogsService.insertOperationLogs(operationLogs);
    }

}

@NoArgsConstructor
@AllArgsConstructor
@Data
class IpVo implements Serializable {

    private String country;

    private String regionName;

    private String city;

    private String query;

    /**
     * success,fail
     */
    private String status;

}
