package com.rnkrsoft.txupcycle.protocol.upcycle;

import com.rnkrsoft.txupcycle.protocol.ElementInfo;
import com.rnkrsoft.txupcycle.protocol.enums.ElementType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UpcycleFormElementInfo extends AbstractUpcycleElementInfo{
    /**
     * 要素类型
     */
    @Getter
    final ElementType type = ElementType.FORM;
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
     * Bean类
     */
    @Getter
    Class<?> beanClass;
    /**
     * 字段描述
     */
    @Getter
    String desc;
    /**
     * 用法
     */
    @Getter
    String usage;
    /**
     * 是否必输
     */
    @Getter
    boolean required;

    public UpcycleFormElementInfo(UpcycleInterfaceInfo interfaceInfo, UpcycleElementSet elementSet, String fullName) {
        super(interfaceInfo, elementSet, fullName);
    }

    @Override
    public String getTypeName() {
        return type.getCode();
    }
}
