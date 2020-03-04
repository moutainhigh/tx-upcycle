package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.txupcycle.protocol.annotation.ApidocElement;
import lombok.Data;

import java.io.Serializable;

@Data
class Bean1 implements Serializable {
    @ApidocElement(value = "姓名")
    String name;
}