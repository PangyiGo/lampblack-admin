package com.osen.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-10-25
 * Time: 15:40
 * Description: 系统角色类型
 */
@NoArgsConstructor
@AllArgsConstructor
public enum RolesType {

    /*
    超级管理员
     */
    Admin("ROLE_Admin"),

    /*
    油烟设备终端用户
     */
    LampblackUser("ROLE_LampblackUser"),

    /*
    油烟设备代理商
     */
    LampblackAgent("ROLE_LampblackAgent"),

    /*
    VOC设备终端用户
     */
    VocUser("ROLE_VocUser"),

    /*
    VOC设备代理商
     */
    VocAgent("ROLE_VocProxy"),

    /*
    冷链设备终端用户
     */
    ColdChainUser("ROLE_ColdChainUser"),

    /*
    冷链设备代理商
     */
    ColdChainAgent("ROLE_ColdChainProxy");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
