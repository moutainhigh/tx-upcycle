package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.txupcycle.protocol.annotation.ApidocElement;
import lombok.Data;

import java.io.Serializable;

@Data
class Bean<T> implements Serializable {
    @ApidocElement(value = "ceshi")
    String testCol;
    @ApidocElement(value = "Bean4测试对象")
    Bean4 bean4;
    T data;
}