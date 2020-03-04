package com.rnkrsoft.txupcycle.protocol.upcycle;

import com.rnkrsoft.txupcycle.protocol.ElementInfo;
import com.rnkrsoft.txupcycle.protocol.ElementSet;
import com.rnkrsoft.txupcycle.protocol.InterfaceInfo;
import com.rnkrsoft.txupcycle.protocol.enums.ColumnType;
import com.rnkrsoft.txupcycle.protocol.enums.ElementType;


public interface UpcycleElementInfo {
    /**
     * 字段类型
     *
     * @return 字段类型
     */
    ColumnType getColumnType();
    /**
     * 获取接口信息
     *
     * @return 接口信息
     */
    UpcycleInterfaceInfo getInterfaceInfo();

    /**
     * 获取字段信息所在集合
     *
     * @return 字段信息所在集合
     */
    UpcycleElementSet getElementSet();

    /**
     * 字段属于请求对象
     *
     * @return 是否属于请求
     */
    boolean isBelongToRequest();

    /**
     * 字段数与应答对象
     *
     * @return 是否属于应答
     */
    boolean isBelongToResponse();

    /**
     * 要素类型
     *
     * @return 要素类型
     */
    String getTypeName();

    /**
     * 要素类型
     *
     * @return 要素类型
     */
    ElementType getType();

    /**
     * 如果为ValueElementInfo返回真
     *
     * @return ValueElementInfo返回真
     */
    boolean isValue();

    /**
     * 如果为FormElementInfo返回真
     *
     * @return ormElementInfo返回真
     */
    boolean isForm();

    /**
     * 如果为BeanElementInfo返回真
     *
     * @return BeanElementInfo返回真
     */
    boolean isBean();
    /**
     * 将字段转换为具体的字段类型
     *
     * @param targetClass 目标类型
     * @param <T>         目标类型
     * @return 字段
     */
    <T extends UpcycleElementInfo> T as(Class<T> targetClass);

    /**
     * 要素名称，通常为英文
     *
     * @return 要素名称
     */
    String getName();

    /**
     * 父级名称
     *
     * @return 父级名称
     */
    String getParentName();

    /**
     * 获取字段完整名称
     *
     * @return 完整名称
     */
    String getFullName();

    /**
     * 要素描述
     * 如果描述为空，则取name的值
     *
     * @return 要素描述
     */
    String getDesc();

    /**
     * 要素用法
     * 如果用法为空，则取desc的值
     *
     * @return 要素用法
     */
    String getUsage();
}
