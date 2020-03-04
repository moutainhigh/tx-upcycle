package com.rnkrsoft.txupcycle.protocol.upcycle;

import com.rnkrsoft.txupcycle.protocol.enums.ColumnType;
import com.rnkrsoft.txupcycle.protocol.enums.ElementType;
import lombok.Getter;

public abstract class AbstractUpcycleElementInfo implements UpcycleElementInfo{
    /**
     * 字段的完整名称，使用类全限定名
     */
    @Getter
    protected final String fullName;
    /**
     * 字段所属类型
     */
    @Getter
    protected final ColumnType columnType;
    /**
     * 是否为只读字段
     */
    @Getter
    protected boolean readonly = false;
    /**
     * 字段所属接口对象
     */
    @Getter
    protected final UpcycleInterfaceInfo interfaceInfo;
    /**
     * 字段所属的元素集合
     */
    @Getter
    final UpcycleElementSet elementSet;

    public AbstractUpcycleElementInfo(UpcycleInterfaceInfo interfaceInfo, UpcycleElementSet elementSet, String fullName) {
        this.interfaceInfo = interfaceInfo;
        this.elementSet = elementSet;
        this.fullName = fullName;
        this.columnType = isBelongToRequest() ? ColumnType.REQUEST : ColumnType.RESPONSE;
    }


    @Override
    public <T extends UpcycleElementInfo> T as(Class<T> targetClass) {
        return (T) this;
    }

    @Override
    public String getName() {
        int lastDotIndex = fullName.lastIndexOf(".");
        String name = fullName.substring(lastDotIndex + 1);
        return name;
    }

    @Override
    public String getParentName() {
        int lastDotIndex = fullName.lastIndexOf(".");
        String parentName = fullName.substring(0, lastDotIndex);
        return parentName;
    }

    @Override
    public boolean isBelongToRequest() {
        return interfaceInfo.getRequest() == elementSet;
    }

    @Override
    public boolean isBelongToResponse() {
        return interfaceInfo.getResponse() == elementSet;
    }

    @Override
    public boolean isValue() {
        return getType() == ElementType.VALUE;
    }

    @Override
    public boolean isForm() {
        return getType() == ElementType.FORM;
    }

    @Override
    public boolean isBean() {
        return getType() == ElementType.BEAN;
    }
}
