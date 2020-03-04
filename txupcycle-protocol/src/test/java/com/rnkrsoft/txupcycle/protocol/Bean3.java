package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.txupcycle.protocol.annotation.ApidocElement;
import lombok.Data;

import java.io.Serializable;

@Data
class Bean3 extends Bean2 implements Serializable {
    @ApidocElement(value = "性别")
    boolean sex;
}