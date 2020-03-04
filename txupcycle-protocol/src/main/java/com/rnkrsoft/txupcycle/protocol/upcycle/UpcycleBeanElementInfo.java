package com.rnkrsoft.txupcycle.protocol.upcycle;

import com.rnkrsoft.txupcycle.protocol.ElementInfo;
import com.rnkrsoft.txupcycle.protocol.enums.ElementType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UpcycleBeanElementInfo extends AbstractUpcycleElementInfo{
    /**
     * 要素类型
     */
    @Getter
    final ElementType type = ElementType.BEAN;
    /**
     * 元素
     */
    final List<UpcycleElementInfo> elements = new ArrayList();
    /**
     * Java类
     */
    @Getter
    Class<?> javaClass;
    /**
     * 字段描述
     */
    @Getter
    String desc;
    /**
     * 字段的用法
     */
    @Getter
    String usage;

    public UpcycleBeanElementInfo(UpcycleInterfaceInfo interfaceInfo, UpcycleElementSet elementSet, String fullName) {
        super(interfaceInfo, elementSet, fullName);
    }

    @Override
    public String getTypeName() {
        return type.getCode();
    }
}
