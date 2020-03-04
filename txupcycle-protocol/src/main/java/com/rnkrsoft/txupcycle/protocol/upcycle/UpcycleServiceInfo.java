package com.rnkrsoft.txupcycle.protocol.upcycle;

import java.util.HashSet;
import java.util.Set;

/**
 * 再生重组的服务信息
 */
public class UpcycleServiceInfo {
    /**
     * 接口列表
     */
    final Set<UpcycleInterfaceInfo> interfaces = new HashSet<UpcycleInterfaceInfo>();
    /**
     * 通道名
     */
    String channel;
    /**
     * 服务名
     */
    String name;
    /**
     * 服务描述
     */
    String desc;
    /**
     * 服务类全限定名
     */
    String serviceClassName;
    /**
     * 用法
     */
    String usage;
}
