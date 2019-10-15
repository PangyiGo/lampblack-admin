package com.osen.cloud.system.user.controller;

import com.osen.cloud.common.entity.Role;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.common.except.type.ControllerException;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.service.role.RoleService;
import com.osen.cloud.system.user.vo.InsertUserVo;
import com.osen.cloud.service.user.UserService;
import com.osen.cloud.system.user.vo.PageUserVo;
import com.osen.cloud.system.user.vo.ReturnPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RoleService roleService;

    /**
     * 添加用户
     *
     * @param userVo 请求数据
     * @return 信息
     */
    @PostMapping("/user/add")
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
        // 查询用户指定角色列表
        List<Role> roles = roleService.findRoleByUserId(user.getId());
        user.setRoles(roles);
        return RestResultUtil.success(user);
    }

    /**
     * 通过用户名获取用户信息
     * 指定账号
     *
     * @return 信息
     */
    @PostMapping("/user/get/{account}")
    public RestResult getUserInfo(@PathVariable("account") String account) {
        User user = userService.findByUsername(account);
        return RestResultUtil.success(user);
    }

    /**
     * 分页查询所用用户信息列表
     * 根据不同用户角色查询相对应用户列表
     *
     * @param pageUserVo 分页参数封装
     * @return 信息
     */
    @PostMapping("/user/get/all")
    public RestResult getAllUserToPage(@RequestBody PageUserVo pageUserVo) {
        Map<String, Object> allUserToPage = userService.findAllUserToPage(pageUserVo.getPageNumber(), pageUserVo.getCompany());
        ReturnPageVo returnPageVo = new ReturnPageVo();
        returnPageVo.setTotalNumber((Long) allUserToPage.get("total"));
        returnPageVo.setUsers((List<User>) allUserToPage.get("users"));
        return RestResultUtil.success(returnPageVo);
    }

    /**
     * 删除指定账号用户
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/user/delete/{account}")
    public RestResult deleteUserByAccount(@PathVariable("account") String account) {
        // 查询指定用户
        if (account.equals(SecurityUtil.getUsername()))
            throw new ControllerException("不能删除当前登录用户");
        User user = userService.findByUsername(account);
        if (user == null)
            throw new ControllerException("查询不到删除指定用户");
        boolean userByAccount = userService.deleteUserByAccount(user);
        if (userByAccount)
            return RestResultUtil.success();
        else
            return RestResultUtil.failed();
    }

    /**
     * 更新用户信息
     *
     * @param user 用户
     * @return 信息
     */
    @PostMapping("/user/update")
    public RestResult updateUserToAccount(@RequestBody User user) {
        boolean b = userService.updateUserToAccount(user);
        if (b)
            return RestResultUtil.success();
        else
            return RestResultUtil.failed();
    }

    /**
     * 重置用户密码
     *
     * @param account 账号
     * @return 信息
     */
    @PostMapping("/user/resetPassword/{account}")
    public RestResult resetPassword(@PathVariable("account") String account) {
        boolean toAccount = userService.resetPasswordToAccount(account);
        if (toAccount)
            return RestResultUtil.success("重置密码成功");
        else
            return RestResultUtil.error(ConstUtil.UNOK, "重置密码失败");
    }
}



