/**
 * 对第三方平台封装的层，预处理返回结果及转化异常信息；
 * 对 Service 层通用能力的下沉，如缓存方案、中间件通用处理；
 * 与 DAO 层交互，对多个 DAO 的组合复用。
 * 复杂业务，service提供数据给Manager层，然后把事务下沉到Manager层
 * Manager层不允许相互调用（即不同业务下的manager禁止调用，如 用户 UserManager 禁止调用 订单 OrderManager ），以避免事务嵌套
 * 专注于不带业务sql语言，也可以在manager层进行通用业务的dao层封装
 * 避免复杂的join查询，数据库压力比java大很多，所以要严格控制好sql，所以可以在manager层进行拆分，比如复杂查询
 * 可以在manager层使用mybatis-plus的 BaseService，因为 manager层不会被其他业务调用，所以不会引起其他业务看到更多的BaseService方法
 * manager层不是必须的，而是有需要的话才去使用
 *
 * @author lizhifu
 * @since 2025/2/14
 */
package io.github.lizhifuabc.admin.module.order.manager;