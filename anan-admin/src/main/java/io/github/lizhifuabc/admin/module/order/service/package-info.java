/**
 * 业务逻辑和事务在Service层
 * <br>合理拆分 service 业务
 * <br>谨慎使用 @Transactional 事务注解，事务在类的内部方法调用是不会生效的
 * <br>事务下沉：数据在 service 层准备好，然后传递给 manager 层，由 manager 层添加@Transactional 进行数据库操作。
 *
 * @author lizhifu
 * @since 2025/2/14
 */
package io.github.lizhifuabc.admin.module.order.service;