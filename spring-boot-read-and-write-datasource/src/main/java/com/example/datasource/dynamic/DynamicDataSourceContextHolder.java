package com.example.datasource.dynamic;

/**
 * 通过ThreadLocal将数据源设置到每个线程上下文中
 * 用于切换读/写模式数据源
 * 原理:
 * 1.利用ThreadLocal保存当前线程数据源模式
 * 2.操作结束后清除该数据，避免内存泄漏，同时也为了后续在该线程进行写操作时任然为读模式
 *
 * @author 程序员小强
 * @date 2020-07-26
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<DataSourceTypeEnum> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceType(DataSourceTypeEnum dataSourceType) {
        CONTEXT_HOLDER.set(dataSourceType);
    }

    /**
     * 获取数据源路由key
     * 默认主库
     */
    public static DataSourceTypeEnum getDataSourceType() {
        return CONTEXT_HOLDER.get() == null ? DataSourceTypeEnum.MASTER : CONTEXT_HOLDER.get();
    }

    public static void removeDataSourceType() {
        CONTEXT_HOLDER.remove();
    }

}