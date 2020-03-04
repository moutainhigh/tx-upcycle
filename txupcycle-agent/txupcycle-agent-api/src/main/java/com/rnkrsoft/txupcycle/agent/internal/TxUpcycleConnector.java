package com.rnkrsoft.txupcycle.agent.internal;

import com.rnkrsoft.txupcycle.protocol.ServiceInfo;
import com.rnkrsoft.txupcycle.protocol.upcycle.UpcycleServiceInfo;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2020/03/04.
 * 交易重组连接器，用于承担与服务器之间的信息交换
 * 相对于服务器来说，ServiceInfo所代表的元信息是只读属性的数据，
 * 而UpcycleServiceInfo所代表的元信息是可变更的数据
 */
public interface TxUpcycleConnector {
    /**
     * 推送服务元信息
     * @param services 服务元信息
     */
    void push(List<ServiceInfo> services);

    /**
     * 拉取服务器定义的重组再生服务定义
     * @return  重组再生服务元信息列表
     */
    List<UpcycleServiceInfo> fetch();
}
