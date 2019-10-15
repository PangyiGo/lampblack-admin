package com.osen.cloud.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.osen.cloud.common.entity.Role;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.common.entity.UserDevice;
import com.osen.cloud.common.entity.UserRole;
import com.osen.cloud.common.except.type.ServiceException;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.SecurityUtil;
import com.osen.cloud.model.user.UserMapper;
import com.osen.cloud.service.role.RoleService;
import com.osen.cloud.service.user.UserService;
import com.osen.cloud.service.user_device.UserDeviceService;
import com.osen.cloud.service.user_role.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.osen.cloud.common.enums.InfoMessage.InsertUser_Error;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 14:50
 * Description: 基本用户服务接口实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDeviceService userDeviceService;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().eq(User::getAccount, username);
        return super.getOne(wrapper, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean create(User user, List<String> roles) {
        if (this.findByUsername(user.getAccount()) != null)
            throw new ServiceException(InsertUser_Error.getCode(), InsertUser_Error.getMessage());
        //插入角色是否异常
        List<Integer> ids = new ArrayList<>(0);
        for (String role : roles) {
            ids.add(Integer.valueOf(role));
        }
        Collection<Role> listByIds = roleService.listByIds(ids);
        if (listByIds.size() != ids.size())
            return false;
        //初始密码
        user.setPassword(ConstUtil.INIT_PASSWORD);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 设置父级ID
        user.setPid(SecurityUtil.getUserId());
        //添加用户
        if (super.save(user)) {
            Integer userId = user.getId();
            List<UserRole> userRoles = new ArrayList<>();
            for (Integer role : ids) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(role);
                userRoles.add(userRole);
            }
            return userRoleService.saveBatch(userRoles);
        }
        return false;
    }

    @Override
    public Map<String, Object> findAllUserToPage(Integer number, String company) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        // 获取当前用户ID
        Role role = roleService.findRoleByUserId(SecurityUtil.getUserId()).get(0);
        // 代理商
        if (role.getName().equals(ConstUtil.PROXY)) {
            wrapper.eq(User::getPid, SecurityUtil.getUserId());
            if (StringUtils.isNotEmpty(company)) {
                wrapper.and(query -> query.like(User::getAccount, company).or().like(User::getCompany, company));
            }
        } else if (role.getName().equals(ConstUtil.ADMIN)) {
            // 用户账号和项目名称模糊查询
            if (StringUtils.isNotEmpty(company)) {
                wrapper.like(User::getAccount, company).or().like(User::getCompany, company);
            }
        }
        // 时间降序排列
        wrapper.orderByAsc(User::getCreateTime);
        // 分页查询
        Page<User> page = new Page<>(number, ConstUtil.PAGE_NUMBER);
        IPage<User> resultPage = super.page(page, wrapper);
        // 总数
        Long total = resultPage.getTotal();
        // 总记录
        List<User> pageRecords = resultPage.getRecords();
        // 查询每个用的角色名称
        for (User pageRecord : pageRecords) {
            List<Role> roleByUserId = roleService.findRoleByUserId(pageRecord.getId());
            pageRecord.setRoles(roleByUserId);
        }
        // 封装
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("users", pageRecords);
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUserByAccount(User account) {
        // 删除指定用户的角色关联
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", account.getId()));
        // 删除指定用户设备关联
        userDeviceService.remove(new QueryWrapper<UserDevice>().eq("user_id", account.getId()));
        // 删除用户
        return super.removeById(account.getId());
    }

    @Override
    public boolean updateUserToAccount(User user) {
        user.setUpdateTime(LocalDateTime.now());
        if (user.getPassword() != null && !user.getPassword().equals(""))
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.<User>lambdaUpdate().eq(User::getAccount, user.getAccount());
        return this.update(user, updateWrapper);
    }

    @Override
    public boolean resetPasswordToAccount(String account) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.<User>lambdaUpdate().set(User::getPassword, ConstUtil.INIT_PASSWORD).set(User::getUpdateTime, LocalDateTime.now()).eq(User::getAccount, account);
        return this.update(wrapper);
    }
}
