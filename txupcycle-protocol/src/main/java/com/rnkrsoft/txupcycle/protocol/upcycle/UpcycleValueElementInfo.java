package com.rnkrsoft.txupcycle.protocol.upcycle;

import com.rnkrsoft.txupcycle.protocol.enums.ElementType;
import com.rnkrsoft.txupcycle.protocol.enums.PatternType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpcycleValueElementInfo extends AbstractUpcycleElementInfo{
    /**
     * 要素类型
     */
    @Getter
    final ElementType type = ElementType.VALUE;
    /**
     * 字典描述
     */
    @Getter
    final Map<String, String> enums = new HashMap();
    /**
     * 缺省值
     */
    @Getter
    final List<String> defaults = new ArrayList();
    /**
     * 提交表单时，该字段的校验规则
     */
    @Getter
    final List<String> patterns = new ArrayList();
    /**
     * 字段描述
     */
    @Getter
    String desc;
    /**
     * Java类
     */
    @Getter
    Class<?> javaClass;
    /**
     * 是否必输
     */
    @Getter
    boolean required;
    /**
     * 最小长度
     */
    @Getter
    int minLen;
    /**
     * 最大长度
     */
    @Getter
    int maxLen;
    /**
     * 校验规则何种类型，正则表达式、ONGL表达式
     */
    @Getter
    PatternType patternType = PatternType.REGEX;
    /**
     * 字段的用法
     */
    @Getter
    String usage;

    public UpcycleValueElementInfo(UpcycleInterfaceInfo interfaceInfo, UpcycleElementSet elementSet, String fullName) {
        super(interfaceInfo, elementSet, fullName);
    }

    @Override
    public String getTypeName() {
        return type.getCode();
    }
}
