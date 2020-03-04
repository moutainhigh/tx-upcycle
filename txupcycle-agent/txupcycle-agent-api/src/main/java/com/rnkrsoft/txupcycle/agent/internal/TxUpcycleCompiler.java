package com.rnkrsoft.txupcycle.agent.internal;

import com.rnkrsoft.reflection4j.extension.SPI;
import com.rnkrsoft.txupcycle.protocol.upcycle.UpcycleServiceInfo;

import java.io.File;

/**
 * Created by rnkrsoft.com on 2020/03/04.
 * 再生重组单元编译器
 */
@SPI("jdk")
public interface TxUpcycleCompiler {
    /**
     * 将一个javaSourceFile编译生成字节码文件
     * @param javaSourceFile Java源代码文件
     * @return 字节码文件的文件对象
     */
    File compile2ByteClassFile(File javaSourceFile);

    /**
     * 将一个再生重组服务元信息编译生成Java源代码文件
     * @param serviceInfo 再生重组服务元信息
     * @return Java源代码文件对应的文件对象
     */
    File generate2JavaSourceFile(UpcycleServiceInfo serviceInfo);

    /**
     * 将一个再生重组服务元信息编译生成字节码文件
     * @param serviceInfo 再生重组服务元信息
     * @return 字节码文件
     */
    File compile(UpcycleServiceInfo serviceInfo);
}
