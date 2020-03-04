package com.rnkrsoft.txupcycle.protocol;


import com.rnkrsoft.txupcycle.protocol.annotation.ApidocInterface;
import com.rnkrsoft.txupcycle.protocol.annotation.ApidocService;

@ApidocService("演示服务")
interface DemoInterface1 {

    void fun();

    @ApidocInterface("方法1")
    Bean<Bean1> fun1(Bean<Bean3> request);
}