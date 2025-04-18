-- 导出 anan 的数据库结构
DROP DATABASE IF EXISTS `anan`;
CREATE DATABASE IF NOT EXISTS `anan` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `anan`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_config
-- ----------------------------
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config`
(
    `config_id`    bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `config_name`  varchar(255) NOT NULL COMMENT '参数名字',
    `config_key`   varchar(255) NOT NULL COMMENT '参数key',
    `config_value` text         NOT NULL,
    `remark`       varchar(255)          DEFAULT NULL,
    `create_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`config_id`),
    UNIQUE index `uk_config_key` (`config_key`) COMMENT '配置键唯一索引'
) COMMENT = '系统配置';

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department`
(
    `department_id` bigint      NOT NULL AUTO_INCREMENT COMMENT '部门主键id',
    `name`          varchar(50) NOT NULL COMMENT '部门名称',
    `user_id`       bigint               DEFAULT NULL COMMENT '部门负责人id',
    `parent_id`     bigint      NOT NULL DEFAULT 0 COMMENT '部门的父级id',
    `sort`          int         NOT NULL DEFAULT 0 COMMENT '排序',
    `status`        tinyint     NOT NULL DEFAULT 0 COMMENT '部门状态（0正常 1停用）',
    `deleted_flag`  tinyint     NOT NULL DEFAULT 0 COMMENT '删除标志（0存在 1删除）',
    `deleted_time`  datetime             default NULL COMMENT '删除时间',
    `create_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`department_id`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_user_id` (`user_id`)
) COMMENT = '部门';

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`
(
    `dict_id`       bigint       NOT NULL AUTO_INCREMENT COMMENT '字典id',
    `dict_name`     varchar(500) NOT NULL COMMENT '字典名字',
    `dict_code`     varchar(500) NOT NULL COMMENT '字典编码',
    `remark`        varchar(1000)         DEFAULT NULL COMMENT '字典备注',
    `disabled_flag` tinyint      NOT NULL DEFAULT 0 COMMENT '禁用状态',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`dict_id`),
    UNIQUE KEY `uk_dict_code` (`dict_code`)
) COMMENT ='字典表';

-- ----------------------------
-- Table structure for t_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_data`;
CREATE TABLE `t_dict_data`
(
    `dict_data_id`  bigint       NOT NULL AUTO_INCREMENT COMMENT '字典数据id',
    `dict_id`       bigint       NOT NULL COMMENT '字典id',
    `data_value`    varchar(500) NOT NULL COMMENT '字典项值',
    `data_label`    varchar(500) NOT NULL COMMENT '字典项显示名称',
    `remark`        varchar(1000)         DEFAULT NULL COMMENT '备注',
    `sort`          int          NOT NULL DEFAULT 0 COMMENT '排序',
    `disabled_flag` tinyint      NOT NULL DEFAULT 0 COMMENT '禁用状态',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`dict_data_id`)
) COMMENT ='字典数据表';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `user_id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `login_name`         varchar(30)  NOT NULL COMMENT '登录帐号',
    `login_pwd`          varchar(100) NOT NULL COMMENT '登录密码',
    `actual_name`        varchar(30)  NOT NULL COMMENT '用户名称',
    `avatar`             varchar(200)          DEFAULT NULL,
    `gender`             tinyint      NOT NULL DEFAULT 0 COMMENT '性别',
    `phone`              varchar(15)           DEFAULT NULL COMMENT '手机号码',
    `department_id`      bigint       NOT NULL COMMENT '部门id',
    `position_id`        bigint                DEFAULT NULL COMMENT '职务ID',
    `email`              varchar(100)          DEFAULT NULL COMMENT '邮箱',
    `disabled_flag`      tinyint      NOT NULL COMMENT '是否被禁用 0否1是',
    `deleted_flag`       tinyint      NOT NULL DEFAULT 0 COMMENT '删除标志（0存在 1删除）',
    `deleted_time`       datetime              default NULL COMMENT '删除时间',
    `administrator_flag` tinyint      NOT NULL DEFAULT 0 COMMENT '是否为超级管理员: 0 不是，1是',
    `remark`             varchar(200)          DEFAULT NULL COMMENT '备注',
    `create_time`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`),
    unique key  `uk_login_name` (`login_name`)
) COMMENT = '用户表';

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`
(
    `file_id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `folder_type`       tinyint      NOT NULL COMMENT '文件夹类型',
    `file_name`         varchar(100)          DEFAULT NULL COMMENT '文件名称',
    `file_size`         int                   DEFAULT NULL COMMENT '文件大小',
    `file_key`          varchar(200) NOT NULL COMMENT '文件key，用于文件下载',
    `file_type`         varchar(50)  NOT NULL COMMENT '文件类型',
    `creator_id`        bigint                DEFAULT NULL COMMENT '创建人，即上传人',
    `creator_user_type` int                   DEFAULT NULL COMMENT '创建人用户类型',
    `creator_name`      varchar(100)          DEFAULT NULL COMMENT '创建人姓名',
    `create_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`file_id`),
    UNIQUE INDEX `uk_file_key` (`file_key`),
    INDEX `module_id_module_type` (`folder_type`),
    INDEX `module_type` (`folder_type`)
) COMMENT = '文件';

-- ----------------------------
-- Table structure for t_login_fail
-- ----------------------------
DROP TABLE IF EXISTS `t_login_fail`;
CREATE TABLE `t_login_fail`
(
    `login_fail_id`         bigint   NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `user_id`               bigint   NOT NULL COMMENT '用户id',
    `user_type`             int      NOT NULL COMMENT '用户类型',
    `login_name`            varchar(1000)     DEFAULT NULL COMMENT '登录名',
    `login_fail_count`      int               DEFAULT NULL COMMENT '连续登录失败次数',
    `lock_flag`             tinyint  NULL     DEFAULT 0 COMMENT '锁定状态:1锁定，0未锁定',
    `login_lock_begin_time` datetime          DEFAULT NULL COMMENT '连续登录失败锁定开始时间',
    `create_time`           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`login_fail_id`),
    UNIQUE INDEX `uid_and_utype` (`user_id`, `user_type`)
) COMMENT = '登录失败次数记录表';

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log`
(
    `login_log_id`    bigint        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`         int           NOT NULL COMMENT '用户id',
    `user_type`       int           NOT NULL COMMENT '用户类型',
    `user_name`       varchar(1000) NOT NULL COMMENT '用户名',
    `login_ip`        varchar(1000)          DEFAULT NULL COMMENT '用户ip',
    `login_ip_region` varchar(1000)          DEFAULT NULL COMMENT '用户ip地区',
    `user_agent`      text COMMENT 'user-agent信息',
    `login_device`    varchar(1000)          DEFAULT NULL COMMENT '登录设备',
    `login_result`    int           NOT NULL COMMENT '登录结果：0成功 1失败 2 退出',
    `remark`          varchar(2000)          DEFAULT NULL COMMENT '备注',
    `create_time`     datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`login_log_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_login_time` (`create_time`)
) COMMENT ='用户登录日志';

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`
(
    `menu_id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `menu_name`       varchar(200) NOT NULL COMMENT '菜单名称',
    `menu_type`       int          NOT NULL COMMENT '类型',
    `parent_id`       bigint       NOT NULL COMMENT '父菜单ID',
    `sort`            int          NOT NULL DEFAULT 0 COMMENT '排序',
    `path`            varchar(255)          DEFAULT NULL COMMENT '路由地址',
    `component`       varchar(255)          DEFAULT NULL COMMENT '组件路径',
    `perms_type`      int                   DEFAULT NULL COMMENT '权限类型',
    `api_perms`       text         NULL COMMENT '后端权限字符串',
    `web_perms`       text         NULL COMMENT '前端权限字符串',
    `icon`            varchar(100)          DEFAULT NULL COMMENT '菜单图标',
    `context_menu_id` bigint                DEFAULT NULL COMMENT '功能点关联菜单ID',
    `frame_flag`      tinyint      NOT NULL DEFAULT 0 COMMENT '是否为外链',
    `frame_url`       text         NULL COMMENT '外链地址',
    `cache_flag`      tinyint      NOT NULL DEFAULT 0 COMMENT '是否缓存',
    `visible_flag`    tinyint      NOT NULL DEFAULT 1 COMMENT '显示状态',
    `disabled_flag`   tinyint      NOT NULL DEFAULT 0 COMMENT '禁用状态',
    `deleted_flag`    tinyint      NOT NULL DEFAULT 0 COMMENT '删除标志（0存在 1删除）',
    `deleted_time`    datetime              default NULL COMMENT '删除时间',
    `create_user_id`  bigint       NOT NULL COMMENT '创建人',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user_id`  bigint                DEFAULT NULL COMMENT '更新人',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`menu_id`)
) COMMENT = '菜单表';

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`
(
    `message_id`         bigint        NOT NULL AUTO_INCREMENT COMMENT '消息id',
    `message_type`       smallint      NOT NULL COMMENT '消息类型',
    `receiver_user_type` int           NOT NULL COMMENT '接收者用户类型',
    `receiver_user_id`   bigint        NOT NULL COMMENT '接收者用户id',
    `data_id`            varchar(500)  NULL     DEFAULT '' COMMENT '相关数据id',
    `title`              varchar(1000) NOT NULL COMMENT '标题',
    `content`            text          NOT NULL COMMENT '内容',
    `read_flag`          tinyint       NOT NULL DEFAULT 0 COMMENT '是否已读',
    `read_time`          datetime               DEFAULT NULL COMMENT '已读时间',
    `create_time`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`message_id`),
    INDEX `idx_msg` (`message_type`, `receiver_user_type`, `receiver_user_id`)
) COMMENT = '通知消息';

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`
(
    `notice_id`              bigint       NOT NULL AUTO_INCREMENT,
    `notice_type_id`         bigint       NOT NULL COMMENT '类型1公告 2动态',
    `title`                  varchar(200) NOT NULL COMMENT '标题',
    `all_visible_flag`       tinyint      NOT NULL COMMENT '是否全部可见',
    `scheduled_publish_flag` tinyint      NOT NULL COMMENT '是否定时发布',
    `publish_time`           datetime     NOT NULL COMMENT '发布时间',
    `content_text`           text         NOT NULL COMMENT '文本内容',
    `content_html`           text         NOT NULL COMMENT 'html内容',
    `attachment`             varchar(1000)         DEFAULT NULL COMMENT '附件',
    `page_view_count`        int          NOT NULL DEFAULT 0 COMMENT '页面浏览量，传说中的pv',
    `user_view_count`        int          NOT NULL DEFAULT 0 COMMENT '用户浏览量，传说中的uv',
    `source`                 varchar(1000)         DEFAULT NULL COMMENT '来源',
    `author`                 varchar(1000)         DEFAULT NULL COMMENT '作者',
    `document_number`        varchar(1000)         DEFAULT NULL COMMENT '文号，如：〔2022〕字第36号',
    `deleted_flag`           tinyint      NOT NULL DEFAULT 0 COMMENT '删除标志（0存在 1删除）',
    `deleted_time`           datetime              default NULL COMMENT '删除时间',
    `create_user_id`         bigint                DEFAULT NULL COMMENT '创建人',
    `create_time`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`notice_id`)
) COMMENT = '通知';

-- ----------------------------
-- Table structure for t_notice_type
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_type`;
CREATE TABLE `t_notice_type`
(
    `notice_type_id`   bigint        NOT NULL AUTO_INCREMENT COMMENT '通知类型',
    `notice_type_name` varchar(1000) NOT NULL COMMENT '类型名称',
    `create_time`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`notice_type_id`)
) COMMENT = '通知类型';

-- ----------------------------
-- Table structure for t_notice_view_record
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_view_record`;
CREATE TABLE `t_notice_view_record`
(
    `notice_id`        bigint   NOT NULL COMMENT '通知公告id',
    `user_id`          bigint   NOT NULL COMMENT '用户id',
    `page_view_count`  int      NULL     DEFAULT 0 COMMENT '查看次数',
    `first_ip`         varchar(255)      DEFAULT NULL COMMENT '首次ip',
    `first_user_agent` varchar(1000)     DEFAULT NULL COMMENT '首次用户设备等标识',
    `last_ip`          varchar(255)      DEFAULT NULL COMMENT '最后一次ip',
    `last_user_agent`  varchar(1000)     DEFAULT NULL COMMENT '最后一次用户设备等标识',
    `create_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`notice_id`, `user_id`),
    UNIQUE INDEX `uk_notice_user` (`notice_id`, `user_id`) COMMENT '资讯用户'
) COMMENT = '通知查看记录';

-- ----------------------------
-- Table structure for t_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operate_log`;
CREATE TABLE `t_operate_log`
(
    `operate_log_id`    bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `operate_user_id`   bigint      NOT NULL COMMENT '用户id',
    `operate_user_type` int         NOT NULL COMMENT '用户类型',
    `operate_user_name` varchar(50) NOT NULL COMMENT '用户名称',
    `module`            varchar(50)          DEFAULT NULL COMMENT '操作模块',
    `content`           varchar(500)         DEFAULT NULL COMMENT '操作内容',
    `url`               varchar(100)         DEFAULT NULL COMMENT '请求路径',
    `method`            varchar(100)         DEFAULT NULL COMMENT '请求方法',
    `param`             text        NULL COMMENT '请求参数',
    `ip`                varchar(255)         DEFAULT NULL COMMENT '请求ip',
    `ip_region`         varchar(1000)        DEFAULT NULL COMMENT '请求ip地区',
    `user_agent`        text        NULL COMMENT '请求user-agent',
    `success_flag`      tinyint              DEFAULT NULL COMMENT '请求结果 0失败 1成功',
    `fail_reason`       longtext    NULL COMMENT '失败原因',
    `create_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`operate_log_id`)
) COMMENT = '操作记录';

-- ----------------------------
-- Table structure for t_password_log
-- ----------------------------
DROP TABLE IF EXISTS `t_password_log`;
CREATE TABLE `t_password_log`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      bigint       NOT NULL COMMENT '用户id',
    `user_type`    tinyint      NOT NULL COMMENT '用户类型',
    `old_password` varchar(255) NOT NULL COMMENT '旧密码',
    `new_password` varchar(255)          DEFAULT NULL COMMENT '新密码',
    `create_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `user_and_type_index` (`user_id`, `user_type`)
) COMMENT = '密码修改记录';

-- ----------------------------
-- Table structure for t_position
-- ----------------------------
DROP TABLE IF EXISTS `t_position`;
CREATE TABLE `t_position`
(
    `position_id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '职务ID',
    `position_name` varchar(200) NOT NULL COMMENT '职务名称',
    `level`         varchar(200)          DEFAULT NULL COMMENT '职级',
    `sort`          int          NOT NULL DEFAULT 0 COMMENT '排序',
    `remark`        varchar(200)          DEFAULT NULL COMMENT '备注',
    `deleted_flag`  tinyint      NOT NULL DEFAULT 0 COMMENT '删除标志（0存在 1删除）',
    `deleted_time`  datetime              default NULL COMMENT '删除时间',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`position_id`)
) COMMENT = '职位表';

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `role_id`     bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name`   varchar(20) NOT NULL COMMENT '角色名称',
    `role_code`   varchar(500)         DEFAULT NULL COMMENT '角色编码',
    `remark`      varchar(255)         DEFAULT NULL COMMENT '角色描述',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`role_id`),
    UNIQUE INDEX `role_code_uni` (`role_code`)
) COMMENT = '角色表';

-- ----------------------------
-- Table structure for t_role_user
-- ----------------------------
DROP TABLE IF EXISTS `t_role_user`;
CREATE TABLE `t_role_user`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `role_id`     bigint   NOT NULL COMMENT '角色id',
    `user_id`     bigint   NOT NULL COMMENT '用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_role_user` (`role_id`, `user_id`)
) COMMENT = '角色-用户';

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`
(
    `role_menu_id` bigint   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_id`      bigint   NOT NULL COMMENT '角色id',
    `menu_id`      bigint   NOT NULL COMMENT '菜单id',
    `create_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`role_menu_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_menu_id` (`menu_id`)
) COMMENT = '角色-菜单';

-- ----------------------------
-- Table structure for t_serial_number
-- ----------------------------
DROP TABLE IF EXISTS `t_serial_number`;
CREATE TABLE `t_serial_number`
(
    `serial_number_id`  int         NOT NULL,
    `business_name`     varchar(50) NOT NULL COMMENT '业务名称',
    `format`            varchar(50)          DEFAULT NULL COMMENT '格式[yyyy]表示年,[mm]标识月,[dd]表示日,[nnn]表示三位数字',
    `rule_type`         varchar(20) NOT NULL COMMENT '规则格式。none没有周期, year 年周期, month月周期, day日周期',
    `init_number`       int         NOT NULL COMMENT '初始值',
    `step_random_range` int         NOT NULL COMMENT '步长随机数',
    `remark`            varchar(255)         DEFAULT NULL COMMENT '备注',
    `last_number`       bigint               DEFAULT NULL COMMENT '上次产生的单号, 默认为空',
    `last_time`         datetime             DEFAULT NULL COMMENT '上次产生的单号时间',
    `create_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`serial_number_id`),
    UNIQUE INDEX `key_name` (`business_name`)
) COMMENT = '单号生成器定义表';

-- ----------------------------
-- Table structure for t_serial_number_record
-- ----------------------------
DROP TABLE IF EXISTS `t_serial_number_record`;
CREATE TABLE `t_serial_number_record`
(
    `serial_number_id` int      NOT NULL,
    `record_date`      date     NOT NULL COMMENT '记录日期',
    `last_number`      bigint   NOT NULL DEFAULT 0 COMMENT '最后更新值',
    `last_time`        datetime NOT NULL COMMENT '最后更新时间',
    `count`            bigint   NOT NULL DEFAULT 0 COMMENT '更新次数',
    `create_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `uk_generator` (`serial_number_id`, `record_date`)
) COMMENT = 'serial_number记录表';

-- ----------------------------
-- Table structure for t_table_column
-- ----------------------------
DROP TABLE IF EXISTS `t_table_column`;
CREATE TABLE `t_table_column`
(
    `table_column_id` bigint   NOT NULL AUTO_INCREMENT,
    `user_id`         bigint   NOT NULL COMMENT '用户id',
    `user_type`       int      NOT NULL COMMENT '用户类型',
    `table_id`        int      NOT NULL COMMENT '表格id',
    `columns`         text     NULL COMMENT '具体的表格列，存入的json',
    `create_time`     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`table_column_id`),
    UNIQUE INDEX `uni_user_table` (`user_id`, `table_id`)
) COMMENT = '表格的自定义列存储';

-- ----------------------------
-- Table structure for t_biz_visible_range
-- ----------------------------
CREATE TABLE `t_biz_visible_range`
(
    `biz_type`    varchar(100) NOT NULL COMMENT '业务类型，如：消息通知，企业管理',
    `biz_id`      bigint(20)   NOT NULL COMMENT '业务id',
    `data_type`   tinyint(4)   NOT NULL COMMENT '数据类型1员工 2部门',
    `data_id`     bigint(20)   NOT NULL COMMENT '员工or部门id',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_notice_data` (`biz_id`, `data_type`, `data_id`)
) COMMENT ='可见范围';

SET FOREIGN_KEY_CHECKS = 1;
