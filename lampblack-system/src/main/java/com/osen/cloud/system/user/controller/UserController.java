package com.osen.cloud.system.user.controller;

import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.system.user.vo.InsertUserVo;
import com.osen.cloud.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: PangYi
 * Date: 2019-09-03
 * Time: 15:20
 * Description: 基本用户控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 添加用户
     *
     * @param userVo 请求数据
     * @return 信息
     */
    @PostMapping("/users")
    public RestResult create(@RequestBody InsertUserVo userVo) {
        if (!userService.create(userVo.getUser(), userVo.getRoles()))
            return RestResultUtil.failed();
        return RestResultUtil.success();
    }
}



