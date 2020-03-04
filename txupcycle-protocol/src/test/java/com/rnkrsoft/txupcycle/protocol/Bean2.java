package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.txupcycle.protocol.annotation.ApidocElement;
import lombok.Data;

import java.io.Serializable;

@Data
class Bean2 extends Bean1 implements Serializable {
    @ApidocElement(value = "年龄")
    int age;
}