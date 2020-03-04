package com.rnkrsoft.txupcycle.agent.internal;

/**
 * Created by rnkrsoft.com on 2020/03/04.
 * 交易单元
 * @param <Request>
 * @param <Response>
 */
public interface TransactionUnit<Request, Response> {
    /**
     * 执行交易单元
     * @param request 请求对象
     * @return 应答对象
     */
    Response execute(Request request);
}
