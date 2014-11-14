SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `oa_leave`
-- ----------------------------
DROP TABLE IF EXISTS `oa_leave`;
CREATE TABLE `oa_leave` (
  `id` char(32) NOT NULL COMMENT '编号',
  `process_instance_id` varchar(32) default NULL COMMENT '流程实例编号',
  `start_time` datetime default NULL COMMENT '开始时间',
  `end_time` datetime default NULL COMMENT '结束时间',
  `leave_type` varchar(20) NOT NULL COMMENT '请假类型',
  `reason` varchar(255) NOT NULL COMMENT '请假理由',
  `apply_user` char(32) NOT NULL COMMENT '申请人',
  `apply_time` datetime default NULL COMMENT '申请时间',
  `reality_start_time` datetime default NULL COMMENT '实际开始时间',
  `reality_end_time` datetime default NULL COMMENT '实际结束时间',
  `process_status` char(1) default NULL COMMENT '流程状态',
  `create_by` varchar(64) default NULL COMMENT '创建者',
  `create_date` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(64) default NULL COMMENT '更新者',
  `update_date` datetime default NULL COMMENT '更新时间',
  `remarks` varchar(255) default NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL default '0' COMMENT '删除标记',
  PRIMARY KEY  (`id`),
  KEY `oa_leave_create_by` (`create_by`),
  KEY `oa_leave_process_instance_id` (`process_instance_id`),
  KEY `oa_leave_del_flag` (`del_flag`),
  KEY `oa_leave_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='请假流程表';