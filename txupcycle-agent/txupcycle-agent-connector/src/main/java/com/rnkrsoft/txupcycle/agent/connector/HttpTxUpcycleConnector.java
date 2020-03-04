package com.rnkrsoft.txupcycle.agent.connector;

import com.rnkrsoft.txupcycle.agent.internal.TxUpcycleConnector;
import com.rnkrsoft.txupcycle.protocol.ServiceInfo;
import com.rnkrsoft.txupcycle.protocol.upcycle.UpcycleServiceInfo;

import java.util.List;

public class HttpTxUpcycleConnector implements TxUpcycleConnector {
    @Override
    public void push(List<ServiceInfo> services) {

    }

    @Override
    public List<UpcycleServiceInfo> fetch() {
        return null;
    }
}
