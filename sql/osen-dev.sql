/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.54
Source Server Version : 50727
Source Host           : localhost:3306
Source Database       : osen-dev

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2019-08-28 08:40:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for alarm_contact
-- ----------------------------
DROP TABLE IF EXISTS `alarm_contact`;
CREATE TABLE `alarm_contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) DEFAULT NULL COMMENT '设备号',
  `name` varchar(20) DEFAULT NULL COMMENT '联系人名称',
  `phone` varchar(15) DEFAULT NULL COMMENT '联系人电话',
  `email` varchar(30) DEFAULT NULL COMMENT '联系人邮件',
  `interval` int(11) DEFAULT NULL COMMENT '报警时间周期',
  `is_auto` int(11) DEFAULT '1' COMMENT '是否自动发送报警',
  `alarm_time` datetime DEFAULT NULL COMMENT '报警时间',
  PRIMARY KEY (`id`),
  KEY `device_no` (`device_no`),
  CONSTRAINT `alarm_contact_ibfk_1` FOREIGN KEY (`device_no`) REFERENCES `device` (`device_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for alarm_history
-- ----------------------------
DROP TABLE IF EXISTS `alarm_history`;
CREATE TABLE `alarm_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) DEFAULT NULL COMMENT '设备号',
  `date_time` datetime DEFAULT NULL COMMENT '接受日期',
  `lampblack` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度',
  `lampblack_flag` char(5) DEFAULT NULL COMMENT '油烟浓度状态值',
  `pm` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度',
  `pm_flag` char(5) DEFAULT NULL COMMENT '颗粒物状态值',
  `nmhc` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃浓度',
  `nmhc_flag` char(5) DEFAULT NULL COMMENT '非甲烷总烃状态值',
  `fan_flag` int(11) DEFAULT NULL COMMENT '风机状态值',
  `purifier_flag` int(11) DEFAULT NULL COMMENT '净化器状态值',
  PRIMARY KEY (`id`),
  KEY `device_no` (`device_no`,`date_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for alarm_realtime
-- ----------------------------
DROP TABLE IF EXISTS `alarm_realtime`;
CREATE TABLE `alarm_realtime` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) DEFAULT NULL COMMENT '设备号',
  `date_time` datetime DEFAULT NULL COMMENT '接受日期',
  `lampblack` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度',
  `lampblack_flag` char(5) DEFAULT NULL COMMENT '油烟浓度状态值',
  `pm` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度',
  `pm_flag` char(5) DEFAULT NULL COMMENT '颗粒物状态值',
  `nmhc` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃浓度',
  `nmhc_flag` char(5) DEFAULT NULL COMMENT '非甲烷总烃状态值',
  `fan_flag` int(11) DEFAULT NULL COMMENT '风机状态值',
  `purifier_flag` int(11) DEFAULT NULL COMMENT '净化器状态值',
  PRIMARY KEY (`id`),
  KEY `device_no` (`device_no`,`date_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_day
-- ----------------------------
DROP TABLE IF EXISTS `data_day`;
CREATE TABLE `data_day` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) DEFAULT NULL COMMENT '设备号',
  `date_time` datetime DEFAULT NULL COMMENT '接收日期',
  `lampblack` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度',
  `lampblack_flag` char(5) DEFAULT NULL COMMENT '油烟浓度状态值',
  `lampblack_min` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度最小值',
  `lampblack_max` varchar(255) DEFAULT NULL COMMENT '油烟浓度最大值',
  `pm` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度',
  `pm_min` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度最小值',
  `pm_max` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度最大值',
  `pm_flag` char(5) DEFAULT NULL COMMENT '颗粒物状态值',
  `nmhc` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃浓度',
  `nmhc_min` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃最小值',
  `nmhc_max` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃最大值',
  `nmhc_flag` char(5) DEFAULT NULL COMMENT '非甲烷总烃状态值',
  `fan_flag` int(11) DEFAULT NULL COMMENT '风机状态值',
  `purifier_flag` int(11) DEFAULT NULL COMMENT '净化器状态值',
  PRIMARY KEY (`id`),
  KEY `device_no` (`device_no`,`date_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_history
-- ----------------------------
DROP TABLE IF EXISTS `data_history`;
CREATE TABLE `data_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) DEFAULT NULL COMMENT '设备号',
  `date_time` datetime DEFAULT NULL COMMENT '接受日期',
  `lampblack` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度',
  `lampblack_flag` char(5) DEFAULT NULL COMMENT '油烟浓度状态值',
  `pm` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度',
  `pm_flag` char(5) DEFAULT NULL COMMENT '颗粒物状态值',
  `nmhc` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃浓度',
  `nmhc_flag` char(5) DEFAULT NULL COMMENT '非甲烷总烃状态值',
  `fan_flag` int(11) DEFAULT NULL COMMENT '风机状态值',
  `purifier_flag` int(11) DEFAULT NULL COMMENT '净化器状态值',
  PRIMARY KEY (`id`),
  KEY `device_no` (`device_no`,`date_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_hour
-- ----------------------------
DROP TABLE IF EXISTS `data_hour`;
CREATE TABLE `data_hour` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) DEFAULT NULL COMMENT '设备号',
  `date_time` datetime DEFAULT NULL COMMENT '接收日期',
  `lampblack` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度',
  `lampblack_flag` char(5) DEFAULT NULL COMMENT '油烟浓度状态值',
  `lampblack_min` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度最小值',
  `lampblack_max` varchar(255) DEFAULT NULL COMMENT '油烟浓度最大值',
  `pm` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度',
  `pm_min` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度最小值',
  `pm_max` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度最大值',
  `pm_flag` char(5) DEFAULT NULL COMMENT '颗粒物状态值',
  `nmhc` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃浓度',
  `nmhc_min` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃最小值',
  `nmhc_max` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃最大值',
  `nmhc_flag` char(5) DEFAULT NULL COMMENT '非甲烷总烃状态值',
  `fan_flag` int(11) DEFAULT NULL COMMENT '风机状态值',
  `purifier_flag` int(11) DEFAULT NULL COMMENT '净化器状态值',
  PRIMARY KEY (`id`),
  KEY `device_no` (`device_no`,`date_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_minute
-- ----------------------------
DROP TABLE IF EXISTS `data_minute`;
CREATE TABLE `data_minute` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) DEFAULT NULL COMMENT '设备号',
  `date_time` datetime DEFAULT NULL COMMENT '接收日期',
  `lampblack` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度',
  `lampblack_flag` char(5) DEFAULT NULL COMMENT '油烟浓度状态值',
  `lampblack_min` decimal(6,2) DEFAULT NULL COMMENT '油烟浓度最小值',
  `lampblack_max` varchar(255) DEFAULT NULL COMMENT '油烟浓度最大值',
  `pm` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度',
  `pm_min` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度最小值',
  `pm_max` decimal(6,2) DEFAULT NULL COMMENT '颗粒物浓度最大值',
  `pm_flag` char(5) DEFAULT NULL COMMENT '颗粒物状态值',
  `nmhc` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃浓度',
  `nmhc_min` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃最小值',
  `nmhc_max` decimal(6,2) DEFAULT NULL COMMENT '非甲烷总烃最大值',
  `nmhc_flag` char(5) DEFAULT NULL COMMENT '非甲烷总烃状态值',
  `fan_flag` int(11) DEFAULT NULL COMMENT '风机状态值',
  `purifier_flag` int(11) DEFAULT NULL COMMENT '净化器状态值',
  PRIMARY KEY (`id`),
  KEY `device_no` (`device_no`,`date_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_no` varchar(50) NOT NULL COMMENT '设备号',
  `name` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `province` varchar(10) DEFAULT NULL COMMENT '省',
  `city` varchar(10) DEFAULT NULL COMMENT '市',
  `area` varchar(20) DEFAULT NULL COMMENT '区',
  `address` varchar(50) DEFAULT NULL COMMENT ' 详细地址',
  `longitude` varchar(50) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(50) DEFAULT NULL COMMENT '纬度',
  `link` varchar(100) DEFAULT NULL COMMENT '监控地址',
  `is_live` int(11) DEFAULT NULL COMMENT '是否在线',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `channel` varchar(10) DEFAULT NULL COMMENT '通道',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `device_index` (`device_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '菜单名称',
  `component` varchar(50) DEFAULT NULL COMMENT '组件路由地址',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级ID',
  `is_frame` int(11) DEFAULT NULL COMMENT '是否外链',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `path` varchar(50) DEFAULT '' COMMENT '链接地址',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  `remark` varchar(20) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) DEFAULT NULL COMMENT '角色标识',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单标识',
  PRIMARY KEY (`id`),
  KEY `conn_role` (`role_id`),
  KEY `conn_menu` (`menu_id`),
  CONSTRAINT `conn_menu` FOREIGN KEY (`menu_id`) REFERENCES `system_menu` (`id`),
  CONSTRAINT `conn_role` FOREIGN KEY (`role_id`) REFERENCES `system_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '账号',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `email` varchar(20) DEFAULT NULL COMMENT '邮箱',
  `company` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(80) DEFAULT NULL COMMENT '地址',
  `status` int(11) DEFAULT NULL COMMENT '账号状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`,`account`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户标识',
  `role_id` int(11) NOT NULL COMMENT '角色标识',
  PRIMARY KEY (`id`),
  KEY `conn_user_id` (`user_id`),
  KEY `conn_role_id` (`role_id`),
  CONSTRAINT `conn_role_id` FOREIGN KEY (`role_id`) REFERENCES `system_role` (`id`),
  CONSTRAINT `conn_user_id` FOREIGN KEY (`user_id`) REFERENCES `system_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_device
-- ----------------------------
DROP TABLE IF EXISTS `user_device`;
CREATE TABLE `user_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户标识',
  `device_id` int(11) NOT NULL COMMENT '设备标识',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `device_id` (`device_id`),
  CONSTRAINT `user_device_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `system_user` (`id`),
  CONSTRAINT `user_device_ibfk_2` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
