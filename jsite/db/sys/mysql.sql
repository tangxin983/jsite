-- ----------------------------
-- Table structure for `sys_area`
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` char(32) NOT NULL COMMENT '编号',
  `parent_id` char(32) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `code` varchar(100) default NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '区域名称',
  `type` char(1) default NULL COMMENT '区域类型',
  `create_by` char(32) default NULL COMMENT '创建者',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` char(32) default NULL COMMENT '更新者',
  `update_time` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) default NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL default '0' COMMENT '删除标记',
  PRIMARY KEY  (`id`),
  KEY `sys_area_parent_id` (`parent_id`),
  KEY `sys_area_parent_ids` (`parent_ids`(255)),
  KEY `sys_area_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区域表';

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` char(32) NOT NULL COMMENT '编号',
  `parent_id` char(32) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '菜单名称',
  `href` varchar(255) default NULL COMMENT '链接',
  `target` varchar(20) default NULL COMMENT '目标',
  `icon` varchar(100) default NULL COMMENT '图标',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  `is_show` char(1) NOT NULL COMMENT '是否在菜单中显示',
  `permission` varchar(200) default NULL COMMENT '权限标识',
  `create_by` char(32) default NULL COMMENT '创建者',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` char(32) default NULL COMMENT '更新者',
  `update_time` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) default NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL default '0' COMMENT '删除标记',
  PRIMARY KEY  (`id`),
  KEY `sys_menu_parent_id` (`parent_id`),
  KEY `sys_menu_parent_ids` (`parent_ids`(255)),
  KEY `sys_menu_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Table structure for `sys_org`
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `id` char(32) NOT NULL COMMENT '编号',
  `parent_id` char(32) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `area_id` char(32) NOT NULL COMMENT '归属区域',
  `code` varchar(100) default NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '机构名称',
  `type` char(1) NOT NULL COMMENT '机构类型',
  `grade` char(1) NOT NULL COMMENT '机构等级',
  `address` varchar(255) default NULL COMMENT '联系地址',
  `zip_code` varchar(100) default NULL COMMENT '邮政编码',
  `master` varchar(100) default NULL COMMENT '负责人',
  `phone` varchar(200) default NULL COMMENT '电话',
  `fax` varchar(200) default NULL COMMENT '传真',
  `email` varchar(200) default NULL COMMENT '邮箱',
  `create_by` char(32) default NULL COMMENT '创建者',
  `create_date` datetime default NULL COMMENT '创建时间',
  `update_by` char(32) default NULL COMMENT '更新者',
  `update_date` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) default NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL default '0' COMMENT '删除标记',
  PRIMARY KEY  (`id`),
  KEY `sys_office_parent_id` (`parent_id`),
  KEY `sys_office_parent_ids` (`parent_ids`(255)),
  KEY `sys_office_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` char(32) NOT NULL COMMENT '编号',
  `office_id` char(32) default NULL COMMENT '归属机构',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `en_name` varchar(100) NOT NULL COMMENT '角色英文名(即act_id_group的id_)',
  `data_scope` char(1) default NULL COMMENT '数据范围',
  `create_by` char(32) default NULL COMMENT '创建者',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` char(32) default NULL COMMENT '更新者',
  `update_time` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) default NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL default '0' COMMENT '删除标记',
  PRIMARY KEY  (`id`),
  KEY `sys_role_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` char(32) NOT NULL COMMENT '角色编号',
  `menu_id` char(32) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY  (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单';

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` char(32) NOT NULL COMMENT '编号',
  `company_id` char(32) default NULL COMMENT '归属公司',
  `office_id` char(32) default NULL COMMENT '归属部门',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `salt` varchar(64) default NULL,
  `no` varchar(100) default NULL COMMENT '工号',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `email` varchar(200) default NULL COMMENT '邮箱',
  `phone` varchar(200) default NULL COMMENT '电话',
  `mobile` varchar(200) default NULL COMMENT '手机',
  `user_type` char(1) default NULL COMMENT '用户类型',
  `login_ip` varchar(100) default NULL COMMENT '最后登陆IP',
  `login_time` datetime default NULL COMMENT '最后登陆时间',
  `create_by` char(32) default NULL COMMENT '创建者',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` char(32) default NULL COMMENT '更新者',
  `update_time` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) default NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL default '0' COMMENT '删除标记',
  PRIMARY KEY  (`id`),
  KEY `sys_user_office_id` (`office_id`),
  KEY `sys_user_login_name` (`login_name`),
  KEY `sys_user_company_id` (`company_id`),
  KEY `sys_user_update_date` (`update_time`),
  KEY `sys_user_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` char(32) NOT NULL COMMENT '用户编号',
  `role_id` char(32) NOT NULL COMMENT '角色编号',
  PRIMARY KEY  (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色';

-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` char(32) NOT NULL COMMENT '编号',
  `label` varchar(100) NOT NULL COMMENT '字典名称',
  `value` varchar(100) NOT NULL COMMENT '字典值',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `create_by` char(32) default NULL COMMENT '创建者',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` char(32) default NULL COMMENT '更新者',
  `update_time` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) default NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL default '0' COMMENT '删除标记',
  PRIMARY KEY  (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`label`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';