package com.osen.cloud.system.system_socket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * User: PangYi
 * Date: 2019-09-04
 * Time: 15:22
 * Description: 启动配置
 */
@Configuration
public class SocketServerApplication {

    @Autowired
    private SocketServerStarter socketServerStarter;

    /**
     * 启动Socket Server服务器
     */
    @PostConstruct
    public void socketInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketServerStarter.startServerSocket();
            }
        }, "Socket Server Thread-01").start();
    }
}
