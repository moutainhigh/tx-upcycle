package com.rnkrsoft.txupcycle.agent.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2020/03/04.
 * 服务执行器，用于接收原生通信报文
 */
public interface ServiceExecutor {
    /**
     * 接受JSON格式的请求，并执行服务，返回JSON格式的应答
     * 单接口：
     * 1.反序列化字符串为对象形式，读头信息中的交易码部分，根据交易码进行判断走生成服务类的逻辑，找到交易单元类实例（交易单元中仅仅只有一个接口调用）
     * 多接口：
     * 1.反序列化字符串为对象形式，读头信息中的交易码部分，根据交易码进行判断走生成服务类的逻辑，找到交易单元类实例（交易单元中有多个接口调用，并且存在多个转换类代码的调用）
     * @param header HTTP头信息
     * @param request JSON字符串的请求
     * @return JSON格式的应答
     */
    String execute(Map<String, String> header, String request);

    /**
     * 接收输入流形式的请求，并执行服务，将结果写入输出流中
     * @param is 输入流
     * @param os 输出流
     * @return 返回-1执行失败，返回0执行成功
     * @see #execute(Map,String)
     */
    int execute(InputStream is, OutputStream os);

    /**
     * 接受套接字通道
     * @param socketChannel 套接字通道
     * @return 返回-1执行失败，返回0执行成功
     * @see #execute(Map,String)
     */
    int execute(SocketChannel socketChannel);
}
