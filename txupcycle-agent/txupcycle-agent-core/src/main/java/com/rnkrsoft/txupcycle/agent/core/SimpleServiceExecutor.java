package com.rnkrsoft.txupcycle.agent.core;

import com.rnkrsoft.txupcycle.agent.internal.ServiceExecutor;
import com.rnkrsoft.txupcycle.agent.internal.TransactionUnit;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2020/03/04.
 * 简单的服务执行器
 */
public class SimpleServiceExecutor implements ServiceExecutor {
    private Map<String, TransactionUnit> units = new HashMap<String, TransactionUnit>();
    @Override
    public String execute(Map<String, String> header, String jsonReq) {
        String txNo = header.get("txNo");
//        TransactionUnit txUnit = units.get(txNo);
        TransactionUnit txUnit = new TransactionUnit() {
            @Override
            public Object execute(Object o) {
                //1.数据校验层
                if(o == null){

                }
                //2.数据转换层
                String str1 = "";
                //3.服务调用层
                // String str2 = xxxService.fun1(str);
                // if(str2 == null){
                //     String str3 = yyyService.fun2(str1);
                //     return str3;
                //  }else{
                //     String str4 =  zzzService.fun3(str2);
                //     return str4;
                // }
                return null;
            }
        };
        //TODO agent加载交易单元时将请求类和应答类对象缓存起来
        Class requestClass = null;
        Class responseClass = null;
        //TODO jsonReq转换为request
        Object request = null;
        Object response = txUnit.execute(request);
        //TODO response转换为jsonRsp
        String jsonRsp = null;
        return jsonRsp;
    }

    @Override
    public int execute(InputStream is, OutputStream os) {
        //todo
        return 0;
    }

    @Override
    public int execute(SocketChannel socketChannel) {
        //todo
        return 0;
    }
}
