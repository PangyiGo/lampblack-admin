package com.osen.cloud.system.user.controller;

import com.osen.cloud.common.entity.User;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.system.user.vo.InsertUserVo;
import com.osen.cloud.service.user.UserService;
import com.osen.cloud.system.user.vo.PageUserVo;
import com.osen.cloud.system.user.vo.ReturnPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    /**
     * 通过用户名获取用户信息
     *
     * @return 信息
     */
    @PostMapping("/user/get")
    public RestResult getUserInfo() {
        String username = SecurityUtil.getUsername();
        User user = userService.findByUsername(username);
        return RestResultUtil.success(user);
    }

    /**
     * 分页查询所用用户信息列表
     *
     * @param pageUserVo 分页参数封装
     * @return 信息
     */
    @PostMapping("/user/get/all")
    public RestResult getAllUserToPage(PageUserVo pageUserVo) {
        Map<String, Object> allUserToPage = userService.findAllUserToPage(pageUserVo.getPageNumber(), pageUserVo.getCompany());
        ReturnPageVo returnPageVo = new ReturnPageVo();
        returnPageVo.setTotalNumber((Long) allUserToPage.get("total"));
        returnPageVo.setUsers((List<User>) allUserToPage.get("users"));
        return RestResultUtil.success(returnPageVo);
    }
}



