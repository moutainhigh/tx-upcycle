package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.reflection4j.GenericHelper;
import com.rnkrsoft.reflection4j.extension.ExtensionLoader;
import com.rnkrsoft.txupcycle.protocol.enums.ElementSetType;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;


/**
 * Created by rnkrsoft.com on 2018/1/9.
 */
public class Example1Test {

    @Test
    public void testScan() throws Exception {
        DocScanner docScanner = ExtensionLoader.getExtensionLoader(DocScanner.class).getExtension();
        docScanner.addScanPackage("com.rnkrsoft.txupcycle.protocol");
        {
            docScanner.init(true);
            InterfaceInfo interfaceInfo = docScanner.listInterface(DemoInterface1.class.getName(), "fun1");
            System.out.println(interfaceInfo.getRequest().getElements());
            System.out.println(interfaceInfo.getResponse().getElements());
            Assert.assertEquals(2, interfaceInfo.getRequest().getElements().size());
            Assert.assertEquals(2, interfaceInfo.getResponse().getElements().size());
            Assert.assertEquals(0, interfaceInfo.getRequest().getPagination().size());
            Assert.assertEquals(0, interfaceInfo.getResponse().getPagination().size());
        }
        docScanner.register(new InterfaceObjectHandler() {
            @Override
            public ElementSetType getObjectType() {
                return ElementSetType.REQUEST;
            }

            @Override
            public boolean accept(Class serviceClass, Method method, Class requestClass) {
                return requestClass == Bean.class;
            }

            @Override
            public Class handle(Class serviceClass, Method method, Class paramClass) {
                Class[] classes = GenericHelper.extractInterfaceMethodParams(serviceClass, method.getName(), method.getParameterTypes(), 0);
                if (classes.length > 1) {
                    return paramClass;
                } else if (classes.length == 0) {
                    return paramClass;
                }
                return classes[0];
            }
        });
        docScanner.register(new InterfaceObjectHandler() {

            @Override
            public ElementSetType getObjectType() {
                return ElementSetType.RESPONSE;
            }

            @Override
            public boolean accept(Class serviceClass, Method method, Class requestClass) {
                return requestClass == Bean.class;
            }

            @Override
            public Class handle(Class serviceClass, Method method, Class paramClass) {
                Class[] classes = GenericHelper.extractInterfaceMethodReturn(serviceClass, method.getName(), method.getParameterTypes());
                if (classes.length > 1) {
                    return paramClass;
                } else if (classes.length == 0) {
                    return paramClass;
                }
                return classes[0];
            }
        });
        {
            docScanner.init(true);
            InterfaceInfo interfaceInfo = docScanner.listInterface(DemoInterface1.class.getName(), "fun1");
            System.out.println(interfaceInfo.getRequest().getElements());
            System.out.println(interfaceInfo.getResponse().getElements());
            Assert.assertEquals(3, interfaceInfo.getRequest().getElements().size());
            Assert.assertEquals(1, interfaceInfo.getResponse().getElements().size());
        }
    }

    @Test
    public void testCascadeMenu(){
        DocScanner docScanner = ExtensionLoader.getExtensionLoader(DocScanner.class).getExtension();
        docScanner.addScanPackage("com.rnkrsoft.suyan4j.doc.example1");
        docScanner.init(false);

    }
}