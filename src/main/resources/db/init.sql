/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : jueapp

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 30/12/2020 19:27:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_detail
-- ----------------------------
DROP TABLE IF EXISTS `account_detail`;
CREATE TABLE `account_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for notify_message
-- ----------------------------
DROP TABLE IF EXISTS `notify_message`;
CREATE TABLE `notify_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_action_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_action_log`;
CREATE TABLE `sys_action_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clazz` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `ipaddr` varchar(255) DEFAULT NULL,
  `message` text,
  `method` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `oper_name` varchar(255) DEFAULT NULL,
  `record_id` bigint(20) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `oper_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32gm4dja0jetx58r9dc2uljiu` (`oper_by`),
  CONSTRAINT `FK32gm4dja0jetx58r9dc2uljiu` FOREIGN KEY (`oper_by`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) DEFAULT NULL COMMENT '部门名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父级ID',
  `pids` varchar(255) DEFAULT NULL COMMENT '所有父级编号',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建用户',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新用户',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态（1:正常,2:冻结,3:删除）',
  PRIMARY KEY (`id`),
  KEY `FKifwd1h4ciusl3nnxrpfpv316u` (`create_by`),
  KEY `FK83g45s1cjqqfpifhulqhv807m` (`update_by`),
  CONSTRAINT `FK83g45s1cjqqfpifhulqhv807m` FOREIGN KEY (`update_by`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FKifwd1h4ciusl3nnxrpfpv316u` FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (1, '总公司', 0, '[0]', 1, '', '2018-12-02 17:41:23', '2019-02-23 02:41:28', 1, 1, 1);
INSERT INTO `sys_dept` VALUES (2, '技术部门', 1, '[0],[1]', 1, '', '2018-12-02 17:51:04', '2019-04-27 13:12:46', 1, 1, 1);
INSERT INTO `sys_dept` VALUES (3, '市场部门', 1, '[0],[1]', 2, '', '2018-12-02 17:51:42', '2019-04-27 13:12:20', 1, 1, 1);
INSERT INTO `sys_dept` VALUES (4, '研发部门', 1, '[0],[1]', 3, '', '2018-12-02 17:51:55', '2019-04-27 13:12:20', 1, 1, 1);
INSERT INTO `sys_dept` VALUES (5, '测试部门', 1, '[0],[1]', 4, '', '2018-12-02 17:52:07', '2019-04-27 13:12:20', 1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `name` varchar(255) DEFAULT NULL COMMENT '字典键名',
  `type` tinyint(4) DEFAULT NULL COMMENT '字典类型',
  `value` text COMMENT '字典键值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建用户',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新用户',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态（1:正常,2:冻结,3:删除）',
  PRIMARY KEY (`id`),
  KEY `FKag4shuprf2tjot9i1mhh37kk6` (`create_by`),
  KEY `FKoyng5jlifhsme0gc1lwiub0lr` (`update_by`),
  CONSTRAINT `FKag4shuprf2tjot9i1mhh37kk6` FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FKoyng5jlifhsme0gc1lwiub0lr` FOREIGN KEY (`update_by`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, '数据状态', 'DATA_STATUS', 2, '1:正常,2:冻结,3:删除', '', '2018-10-05 16:03:11', '2018-10-05 16:11:41', 1, 1, 1);
INSERT INTO `sys_dict` VALUES (2, '字典类型', 'DICT_TYPE', 2, '2:键值对', '', '2018-10-05 20:08:55', '2019-01-17 23:39:23', 1, 1, 1);
INSERT INTO `sys_dict` VALUES (3, '用户性别', 'USER_SEX', 2, '1:男,2:女', '', '2018-10-05 20:12:32', '2018-10-05 20:12:32', 1, 1, 1);
INSERT INTO `sys_dict` VALUES (4, '菜单类型', 'MENU_TYPE', 2, '1:目录,2:菜单,3:按钮', '', '2018-10-05 20:24:57', '2019-11-06 20:08:46', 1, 1, 1);
INSERT INTO `sys_dict` VALUES (5, '搜索栏状态', 'SEARCH_STATUS', 2, '1:正常,2:冻结', '', '2018-10-05 20:25:45', '2019-02-26 00:34:34', 1, 1, 1);
INSERT INTO `sys_dict` VALUES (6, '日志类型', 'LOG_TYPE', 2, '1:业务,2:登录,3:系统', '', '2018-10-05 20:28:47', '2019-02-26 00:31:43', 1, 1, 1);
INSERT INTO `sys_dict`(`id`, `title`, `name`, `type`, `value`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`, `status`) VALUES (7, '达人性别', 'ROLE_SEX', 2, 'man:男,woman:女', '', '2018-10-05 20:28:47', '2019-02-26 00:31:43', 1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `catalog` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `free` bit(1) DEFAULT NULL,
  `md5` varchar(255) DEFAULT NULL,
  `mime` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `sha1` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父级编号',
  `pids` varchar(255) DEFAULT NULL COMMENT '所有父级编号',
  `url` varchar(255) DEFAULT NULL COMMENT 'URL地址',
  `perms` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型（1:一级菜单,2:子级菜单,3:不是菜单）',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建用户',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新用户',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态（1:正常,2:冻结,3:删除）',
  PRIMARY KEY (`id`),
  KEY `FKoxg2hi96yr9pf2m0stjomr3we` (`create_by`),
  KEY `FKsiko0qcr8ddamvrxf1tk4opgc` (`update_by`),
  CONSTRAINT `FKoxg2hi96yr9pf2m0stjomr3we` FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FKsiko0qcr8ddamvrxf1tk4opgc` FOREIGN KEY (`update_by`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '菜单管理', 2, '[0],[2]', '/system/menu/index', 'system:menu:index', '', 2, 3, '', '2018-09-29 00:02:10', '2019-02-24 16:10:40', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (2, '系统管理', 0, '[0]', '#', '#', 'fa fa-cog', 1, 2, '', '2018-09-29 00:05:50', '2019-02-27 21:34:56', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (3, '添加', 1, '[0],[2],[1]', '/system/menu/add', 'system:menu:add', '', 3, 1, '', '2018-09-29 00:06:57', '2019-02-24 16:12:59', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (4, '角色管理', 2, '[0],[2]', '/system/role/index', 'system:role:index', '', 2, 2, '', '2018-09-29 00:08:14', '2019-02-24 16:10:34', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (5, '添加', 4, '[0],[2],[4]', '/system/role/add', 'system:role:add', '', 3, 1, '', '2018-09-29 00:09:04', '2019-02-24 16:12:04', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (6, '主页', 0, '[0]', '/index', 'index', 'layui-icon layui-icon-home', 1, 1, '', '2018-09-29 00:09:56', '2019-02-27 21:34:56', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (7, '用户管理', 2, '[0],[2]', '/system/user/index', 'system:user:index', '', 2, 1, '', '2018-09-29 00:43:50', '2019-04-05 17:43:25', 1, 2, 1);
INSERT INTO `sys_menu` VALUES (8, '编辑', 1, '[0],[2],[1]', '/system/menu/edit', 'system:menu:edit', '', 3, 2, '', '2018-09-29 00:57:33', '2019-02-24 16:13:05', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (9, '详细', 1, '[0],[2],[1]', '/system/menu/detail', 'system:menu:detail', '', 3, 3, '', '2018-09-29 01:03:00', '2019-02-24 16:13:12', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (10, '修改状态', 1, '[0],[2],[1]', '/system/menu/status', 'system:menu:status', '', 3, 4, '', '2018-09-29 01:03:43', '2019-02-24 16:13:21', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (11, '编辑', 4, '[0],[2],[4]', '/system/role/edit', 'system:role:edit', '', 3, 2, '', '2018-09-29 01:06:13', '2019-02-24 16:12:10', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (12, '授权', 4, '[0],[2],[4]', '/system/role/auth', 'system:role:auth', '', 3, 3, '', '2018-09-29 01:06:57', '2019-02-24 16:12:17', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (13, '详细', 4, '[0],[2],[4]', '/system/role/detail', 'system:role:detail', '', 3, 4, '', '2018-09-29 01:08:00', '2019-02-24 16:12:24', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (14, '修改状态', 4, '[0],[2],[4]', '/system/role/status', 'system:role:status', '', 3, 5, '', '2018-09-29 01:08:22', '2019-02-24 16:12:43', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (15, '编辑', 7, '[0],[2],[7]', '/system/user/edit', 'system:user:edit', '', 3, 2, '', '2018-09-29 21:17:17', '2019-02-24 16:11:03', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (16, '添加', 7, '[0],[2],[7]', '/system/user/add', 'system:user:add', '', 3, 1, '', '2018-09-29 21:17:58', '2019-02-24 16:10:28', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (17, '修改密码', 7, '[0],[2],[7]', '/system/user/pwd', 'system:user:pwd', '', 3, 3, '', '2018-09-29 21:19:40', '2019-02-24 16:11:11', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (18, '权限分配', 7, '[0],[2],[7]', '/system/user/role', 'system:user:role', '', 3, 4, '', '2018-09-29 21:20:35', '2019-02-24 16:11:18', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (19, '详细', 7, '[0],[2],[7]', '/system/user/detail', 'system:user:detail', '', 3, 5, '', '2018-09-29 21:21:00', '2019-02-24 16:11:26', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (20, '修改状态', 7, '[0],[2],[7]', '/system/user/status', 'system:user:status', '', 3, 6, '', '2018-09-29 21:21:18', '2019-02-24 16:11:35', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (21, '字典管理', 2, '[0],[2]', '/system/dict/index', 'system:dict:index', '', 2, 5, '', '2018-10-05 00:55:52', '2019-02-24 16:14:24', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (22, '字典添加', 21, '[0],[2],[21]', '/system/dict/add', 'system:dict:add', '', 3, 1, '', '2018-10-05 00:56:26', '2019-02-24 16:14:10', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (23, '字典编辑', 21, '[0],[2],[21]', '/system/dict/edit', 'system:dict:edit', '', 3, 2, '', '2018-10-05 00:57:08', '2019-02-24 16:14:34', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (24, '字典详细', 21, '[0],[2],[21]', '/system/dict/detail', 'system:dict:detail', '', 3, 3, '', '2018-10-05 00:57:42', '2019-02-24 16:14:41', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (25, '修改状态', 21, '[0],[2],[21]', '/system/dict/status', 'system:dict:status', '', 3, 4, '', '2018-10-05 00:58:27', '2019-02-24 16:14:49', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (26, '行为日志', 2, '[0],[2]', '/system/actionLog/index', 'system:actionLog:index', '', 2, 6, '', '2018-10-14 16:52:01', '2019-02-27 21:34:15', 1, 1, 3);
INSERT INTO `sys_menu` VALUES (27, '日志详细', 26, '[0],[2],[26]', '/system/actionLog/detail', 'system:actionLog:detail', '', 3, 1, '', '2018-10-14 21:07:11', '2019-02-27 21:34:15', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (28, '日志删除', 26, '[0],[2],[26]', '/system/actionLog/delete', 'system:actionLog:delete', '', 3, 2, '', '2018-10-14 21:08:17', '2019-02-27 21:34:15', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (30, '开发中心', 0, '[0]', '#', '#', 'fa fa-gavel', 1, 3, '', '2018-10-19 16:38:23', '2019-02-27 21:34:56', 1, 1, 3);
INSERT INTO `sys_menu` VALUES (31, '代码生成', 30, '[0],[30]', '/dev/code', '#', '', 2, 1, '', '2018-10-19 16:39:04', '2019-03-13 17:43:58', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (125, '表单构建', 30, '[0],[30]', '/dev/build', '#', '', 2, 2, '', '2018-11-25 16:12:23', '2019-02-24 16:16:40', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (136, '部门管理', 2, '[0],[2]', '/system/dept/index', 'system:dept:index', '', 2, 4, '', '2018-12-02 16:33:23', '2019-02-24 16:10:50', 1, 1, 3);
INSERT INTO `sys_menu` VALUES (137, '添加', 136, '[0],[2],[136]', '/system/dept/add', 'system:dept:add', '', 3, 1, '', '2018-12-02 16:33:23', '2019-02-24 16:13:34', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (138, '编辑', 136, '[0],[2],[136]', '/system/dept/edit', 'system:dept:edit', '', 3, 2, '', '2018-12-02 16:33:23', '2019-02-24 16:13:42', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (139, '详细', 136, '[0],[2],[136]', '/system/dept/detail', 'system:dept:detail', '', 3, 3, '', '2018-12-02 16:33:23', '2019-02-24 16:13:49', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (140, '改变状态', 136, '[0],[2],[136]', '/system/dept/status', 'system:dept:status', '', 3, 4, '', '2018-12-02 16:33:23', '2019-02-24 16:13:57', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (146, '数据接口', 30, '[0],[30]', '/dev/swagger', '#', '', 2, 3, '', '2018-12-09 21:11:11', '2019-02-24 23:38:18', 1, 1, 1);
INSERT INTO `sys_menu` VALUES (147, '实名管理', 2, '[0],[2]', '/system/realNameAuth/index', 'system:realNameAuth:index', '', 2, 2, '                        ', NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (148, '达人管理', 2, '[0],[2]', '/system/juerole/index', 'system:juerole:index', '', 2, 3, '                        ', NULL, NULL, NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) DEFAULT NULL COMMENT '角色名称（中文名）',
  `name` varchar(255) DEFAULT NULL COMMENT '标识名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建用户',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新用户',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态（1:正常,2:冻结,3:删除）',
  PRIMARY KEY (`id`),
  KEY `FKdkwvv0rm6j3d5l6hwsy2dplol` (`create_by`),
  KEY `FKrouqqi3f2bgc5o83wdstlh6q4` (`update_by`),
  CONSTRAINT `FKdkwvv0rm6j3d5l6hwsy2dplol` FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FKrouqqi3f2bgc5o83wdstlh6q4` FOREIGN KEY (`update_by`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', '', '2018-09-29 00:12:40', '2019-01-18 21:09:51', 1, 1, 1);
INSERT INTO `sys_role` VALUES (2, '经纪人组', 'group', '', '2018-09-30 16:04:32', '2019-04-28 00:10:00', 1, 1, 1);
INSERT INTO `sys_role` VALUES (3, '用户组', 'group1', '', '2018-09-30 16:24:20', '2019-04-28 00:11:09', 1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `FKf3mud4qoc7ayew8nml4plkevo` (`menu_id`),
  CONSTRAINT `FKf3mud4qoc7ayew8nml4plkevo` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`),
  CONSTRAINT `FKkeitxsgxwayackgqllio4ohn5` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (3, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (3, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (3, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (2, 5);
INSERT INTO `sys_role_menu` VALUES (1, 6);
INSERT INTO `sys_role_menu` VALUES (2, 6);
INSERT INTO `sys_role_menu` VALUES (3, 6);
INSERT INTO `sys_role_menu` VALUES (1, 7);
INSERT INTO `sys_role_menu` VALUES (2, 7);
INSERT INTO `sys_role_menu` VALUES (1, 8);
INSERT INTO `sys_role_menu` VALUES (3, 8);
INSERT INTO `sys_role_menu` VALUES (1, 9);
INSERT INTO `sys_role_menu` VALUES (3, 9);
INSERT INTO `sys_role_menu` VALUES (1, 10);
INSERT INTO `sys_role_menu` VALUES (3, 10);
INSERT INTO `sys_role_menu` VALUES (1, 11);
INSERT INTO `sys_role_menu` VALUES (2, 11);
INSERT INTO `sys_role_menu` VALUES (1, 12);
INSERT INTO `sys_role_menu` VALUES (2, 12);
INSERT INTO `sys_role_menu` VALUES (1, 13);
INSERT INTO `sys_role_menu` VALUES (2, 13);
INSERT INTO `sys_role_menu` VALUES (1, 14);
INSERT INTO `sys_role_menu` VALUES (2, 14);
INSERT INTO `sys_role_menu` VALUES (1, 15);
INSERT INTO `sys_role_menu` VALUES (2, 15);
INSERT INTO `sys_role_menu` VALUES (1, 16);
INSERT INTO `sys_role_menu` VALUES (2, 16);
INSERT INTO `sys_role_menu` VALUES (1, 17);
INSERT INTO `sys_role_menu` VALUES (2, 17);
INSERT INTO `sys_role_menu` VALUES (1, 18);
INSERT INTO `sys_role_menu` VALUES (2, 18);
INSERT INTO `sys_role_menu` VALUES (1, 19);
INSERT INTO `sys_role_menu` VALUES (2, 19);
INSERT INTO `sys_role_menu` VALUES (1, 20);
INSERT INTO `sys_role_menu` VALUES (2, 20);
INSERT INTO `sys_role_menu` VALUES (1, 21);
INSERT INTO `sys_role_menu` VALUES (3, 21);
INSERT INTO `sys_role_menu` VALUES (1, 22);
INSERT INTO `sys_role_menu` VALUES (3, 22);
INSERT INTO `sys_role_menu` VALUES (1, 23);
INSERT INTO `sys_role_menu` VALUES (3, 23);
INSERT INTO `sys_role_menu` VALUES (1, 24);
INSERT INTO `sys_role_menu` VALUES (3, 24);
INSERT INTO `sys_role_menu` VALUES (1, 25);
INSERT INTO `sys_role_menu` VALUES (3, 25);
INSERT INTO `sys_role_menu` VALUES (1, 26);
INSERT INTO `sys_role_menu` VALUES (1, 27);
INSERT INTO `sys_role_menu` VALUES (1, 28);
INSERT INTO `sys_role_menu` VALUES (1, 30);
INSERT INTO `sys_role_menu` VALUES (1, 31);
INSERT INTO `sys_role_menu` VALUES (1, 125);
INSERT INTO `sys_role_menu` VALUES (1, 136);
INSERT INTO `sys_role_menu` VALUES (3, 136);
INSERT INTO `sys_role_menu` VALUES (1, 137);
INSERT INTO `sys_role_menu` VALUES (3, 137);
INSERT INTO `sys_role_menu` VALUES (1, 138);
INSERT INTO `sys_role_menu` VALUES (3, 138);
INSERT INTO `sys_role_menu` VALUES (1, 139);
INSERT INTO `sys_role_menu` VALUES (3, 139);
INSERT INTO `sys_role_menu` VALUES (1, 140);
INSERT INTO `sys_role_menu` VALUES (3, 140);
INSERT INTO `sys_role_menu` VALUES (1, 146);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `password` char(64) DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '密码盐',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `picture` varchar(255) DEFAULT NULL COMMENT '头像',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别（1:男,2:女）',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话号码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态（1:正常,2:冻结,3:删除）',
  `address` varchar(255) DEFAULT NULL,
  `broker` bit(1) DEFAULT NULL,
  `current_month_money` decimal(19,2) DEFAULT NULL,
  `last_month_money` decimal(19,2) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `picture_url` varchar(255) DEFAULT NULL,
  `real_name_auth` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FKb3pkx0wbo6o8i8lj0gxr37v1n` (`dept_id`),
  CONSTRAINT `FKb3pkx0wbo6o8i8lj0gxr37v1n` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '超级管理员', '4f1b5eea8488c27345d4158877c3c9d552599905446db16bd795c942cc529177', 'Xmwvuc', NULL, NULL, 1, '10086@163.com', '10086', '', '2018-08-09 23:00:03', '2019-11-06 20:09:17', 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKhh52n8vd4ny9ff4x9fb8v65qx` (`role_id`),
  CONSTRAINT `FKb40xxfch70f5qnyfw8yme1n1s` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `FKhh52n8vd4ny9ff4x9fb8v65qx` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for t_pay_order
-- ----------------------------
DROP TABLE IF EXISTS `t_pay_order`;
CREATE TABLE `t_pay_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  `bank_type` varchar(255) DEFAULT NULL,
  `body` varchar(255) NOT NULL COMMENT '支付描述，例如：中国联通充值中心-话费充值-50元',
  `business_id` int(11) NOT NULL COMMENT '角色ID或者任务ID',
  `business_type` tinyint(1) NOT NULL COMMENT '1角色支付，2任务支付',
  `create_by` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `fee` decimal(19,2) DEFAULT NULL,
  `handle_rate` decimal(19,2) DEFAULT NULL,
  `min_charge_handle_fee` decimal(19,2) DEFAULT NULL,
  `notify_message` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `origin_fee` decimal(19,2) DEFAULT NULL,
  `out_trade_no` varchar(255) DEFAULT NULL,
  `owner` int(11) DEFAULT NULL,
  `pay_status` tinyint(1) NOT NULL COMMENT '1新建，2成功，3失败',
  `time_end` varchar(255) DEFAULT NULL,
  `pay_time_stamp` varchar(255) DEFAULT NULL,
  `total_fee` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `accepted_by_id` int(11) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `apply_persons` varchar(255) DEFAULT NULL,
  `birthda_day` date DEFAULT NULL,
  `create_by_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `role_desc` varchar(255) DEFAULT NULL,
  `fee` decimal(19,2) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `remrks` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `show_info` varchar(255) DEFAULT NULL,
  `skill` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `role_type` varchar(255) DEFAULT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_roleapply
-- ----------------------------
DROP TABLE IF EXISTS `t_roleapply`;
CREATE TABLE `t_roleapply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7caq3avv6hafjir2q24makaep` (`roleid`),
  CONSTRAINT `FK7caq3avv6hafjir2q24makaep` FOREIGN KEY (`roleid`) REFERENCES `t_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `accepted_by_id` int(11) DEFAULT NULL,
  `apply_persons` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `create_by_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `task_desc` varchar(255) DEFAULT NULL,
  `fee` decimal(19,2) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `task_name` varchar(255) DEFAULT NULL,
  `ontop` tinyint(4) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `recommend` tinyint(4) DEFAULT NULL,
  `remrks` varchar(255) DEFAULT NULL,
  `skill` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `task_time` datetime DEFAULT NULL,
  `validate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_taskapply
-- ----------------------------
DROP TABLE IF EXISTS `t_taskapply`;
CREATE TABLE `t_taskapply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `taskid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3q76o7pe9lqce6r77nv1oix9` (`taskid`),
  CONSTRAINT `FK3q76o7pe9lqce6r77nv1oix9` FOREIGN KEY (`taskid`) REFERENCES `t_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_transaction
-- ----------------------------
DROP TABLE IF EXISTS `t_transaction`;
CREATE TABLE `t_transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `transaction_type` tinyint(4) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  `bank_card` varchar(255) DEFAULT NULL,
  `birthda_day` date DEFAULT NULL,
  `broker` tinyint(4) NOT NULL,
  `cast_history` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `picture_url` varchar(255) DEFAULT NULL,
  `real_name_auth` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `sex` varchar(255) DEFAULT NULL,
  `short_video_url` varchar(255) DEFAULT NULL,
  `talent_role_id` int(11) DEFAULT NULL,
  `talents` varchar(255) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_favor_role
-- ----------------------------
DROP TABLE IF EXISTS `user_favor_role`;
CREATE TABLE `user_favor_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `classify_name` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKju9qaysvfgn3kyfoo984r2d8` (`role_id`),
  CONSTRAINT `FKju9qaysvfgn3kyfoo984r2d8` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_favor_task
-- ----------------------------
DROP TABLE IF EXISTS `user_favor_task`;
CREATE TABLE `user_favor_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `classify_name` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqbh91q45pf0irq7dlq6hhl8cc` (`task_id`),
  CONSTRAINT `FKqbh91q45pf0irq7dlq6hhl8cc` FOREIGN KEY (`task_id`) REFERENCES `t_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
