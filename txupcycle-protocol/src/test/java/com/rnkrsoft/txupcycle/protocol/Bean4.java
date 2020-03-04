package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.txupcycle.protocol.annotation.ApidocElement;
import lombok.Data;

import java.io.Serializable;

@Data
class Bean4 implements Serializable {
    @ApidocElement(value = "测试")
    boolean test;
}