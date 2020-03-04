package com.rnkrsoft.txupcycle.agent.compiler;

import com.rnkrsoft.reflection4j.extension.Extension;
import com.rnkrsoft.txupcycle.agent.internal.TxUpcycleCompiler;
import com.rnkrsoft.txupcycle.protocol.upcycle.UpcycleServiceInfo;

import java.io.File;
@Extension("jdk")
public class JdkTxUpcycleCompiler implements TxUpcycleCompiler {
    @Override
    public File compile2ByteClassFile(File javaSourceFile) {
        return null;
    }

    @Override
    public File generate2JavaSourceFile(UpcycleServiceInfo serviceInfo) {
        //todo 生成的Java源文件实现TransactionUnit接口，并且在服务执行器中缓存交易单元
        return null;
    }

    @Override
    public File compile(UpcycleServiceInfo serviceInfo) {
        return null;
    }
}
