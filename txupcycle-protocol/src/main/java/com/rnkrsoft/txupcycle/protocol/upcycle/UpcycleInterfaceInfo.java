package com.rnkrsoft.txupcycle.protocol.upcycle;

import com.rnkrsoft.txupcycle.protocol.enums.ElementSetType;
import lombok.Getter;

/**
 * 重组再生之后的接口信息
 */
public class UpcycleInterfaceInfo {
    /**
     * 接口名称
     */
    @Getter
    String name;
    /**
     * 接口版本号
     */
    @Getter
    String version;
    /**
     * 接口描述
     */
    @Getter
    String desc;
    /**
     * 用法
     */
    @Getter
    String usage;
    /**
     * 接口类
     */
    @Getter
    String className;

    @Getter
    UpcycleElementSet request;

    @Getter
    UpcycleElementSet response;

    public UpcycleInterfaceInfo(String name, String version, String desc, String usage, String className, String requestClass, String responseClass) {
        this.name = name;
        this.version = version;
        this.desc = desc;
        this.usage = usage;
        this.className = className;
        this.request = new UpcycleElementSet(requestClass, ElementSetType.REQUEST);
        this.response = new UpcycleElementSet(responseClass, ElementSetType.REQUEST);
    }
}
