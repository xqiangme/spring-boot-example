

#创建两个 数据库 db_test1,与 db_test2

#分别在两个库-创建测试表
CREATE TABLE `user_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
  `user_id` varchar(32) NOT NULL COMMENT '人员id',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `user_password` varchar(32) NOT NULL COMMENT '密码',
  `real_name` varchar(64) NOT NULL COMMENT '真实姓名',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记 0正常 1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='人员信息表';



#DB1 模拟数据
INSERT INTO `db_test1`.`user_info`(`id`, `user_id`, `user_name`, `user_password`, `real_name`, `mobile`, `remark`, `create_time`, `update_time`, `del_flag`) VALUES (1, '1001', 'zhangsan', '123456', '张三', '13235717777', '我是DB1 - 张三', now(), now(), 0);

#DB2 模拟数据
INSERT INTO `db_test2`.`user_info`(`id`, `user_id`, `user_name`, `user_password`, `real_name`, `mobile`, `remark`, `create_time`, `update_time`, `del_flag`) VALUES (1, '1001', 'lisi', '333222', '李四', '15678298934', '我是DB2 - 李四', now(), now(), 0);
INSERT INTO `db_test2`.`user_info`(`id`, `user_id`, `user_name`, `user_password`, `real_name`, `mobile`, `remark`, `create_time`, `update_time`, `del_flag`) VALUES (2, '1002', 'wangwu', '555666', '王五', '18778298934', '我是DB2 - 王五', now(), now(), 0);
