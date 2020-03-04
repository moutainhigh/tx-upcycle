/**
 * RNKRSOFT OPEN SOURCE SOFTWARE LICENSE TERMS ver.1
 * - 氡氪网络科技(重庆)有限公司 开源软件许可条款(版本1)
 * 氡氪网络科技(重庆)有限公司 以下简称Rnkrsoft。
 * 这些许可条款是 Rnkrsoft Corporation（或您所在地的其中一个关联公司）与您之间达成的协议。
 * 请阅读本条款。本条款适用于所有Rnkrsoft的开源软件项目，任何个人或企业禁止以下行为：
 * .禁止基于删除开源代码所附带的本协议内容、
 * .以非Rnkrsoft的名义发布Rnkrsoft开源代码或者基于Rnkrsoft开源源代码的二次开发代码到任何公共仓库,
 * 除非上述条款附带有其他条款。如果确实附带其他条款，则附加条款应适用。
 * <p>
 * 使用该软件，即表示您接受这些条款。如果您不接受这些条款，请不要使用该软件。
 * 如下所述，安装或使用该软件也表示您同意在验证、自动下载和安装某些更新期间传输某些标准计算机信息以便获取基于 Internet 的服务。
 * <p>
 * 如果您遵守这些许可条款，将拥有以下权利。
 * 1.阅读源代码和文档
 * 如果您是个人用户，则可以在任何个人设备上阅读、分析、研究Rnkrsoft开源源代码。
 * 如果您经营一家企业，则禁止在任何设备上阅读Rnkrsoft开源源代码,禁止分析、禁止研究Rnkrsoft开源源代码。
 * 2.编译源代码
 * 如果您是个人用户，可以对Rnkrsoft开源源代码以及修改后产生的源代码进行编译操作，编译产生的文件依然受本协议约束。
 * 如果您经营一家企业，不可以对Rnkrsoft开源源代码以及修改后产生的源代码进行编译操作。
 * 3.二次开发拓展功能
 * 如果您是个人用户，可以基于Rnkrsoft开源源代码进行二次开发，修改产生的元代码同样受本协议约束。
 * 如果您经营一家企业，不可以对Rnkrsoft开源源代码进行任何二次开发，但是可以通过联系Rnkrsoft进行商业授予权进行修改源代码。
 * 完整协议。本协议以及开源源代码附加协议，共同构成了Rnkrsoft开源软件的完整协议。
 * <p>
 * 4.免责声明
 * 该软件按“原样”授予许可。 使用本文档的风险由您自己承担。Rnkrsoft 不提供任何明示的担保、保证或条件。
 * 5.版权声明
 * 本协议所对应的软件为 Rnkrsoft 所拥有的自主知识产权，如果基于本软件进行二次开发，在不改变本软件的任何组成部分的情况下的而二次开发源代码所属版权为贵公司所有。
 */
package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.extension.Extension;
import com.rnkrsoft.reflection4j.resource.ClassScanner;
import com.rnkrsoft.txupcycle.protocol.annotation.ApidocInterface;
import com.rnkrsoft.txupcycle.protocol.annotation.ApidocService;
import com.rnkrsoft.txupcycle.protocol.enums.ElementSetType;
import com.rnkrsoft.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rnkrsoft.com on 2018/1/9.
 */
@Slf4j
@Extension("default")
public class DefaultDocScanner implements DocScanner {
    List<String> packages = new ArrayList();
    final Map<ServiceInfo, Set<InterfaceInfo>> SERVICES = new ConcurrentHashMap();
    final Map<String, InterfaceInfo> INTERFACES = new ConcurrentHashMap();
    @Setter
    @Getter
    ClassLoader classLoader;
    /**
     * 请求对象过滤器
     */
    List<InterfaceObjectHandler> requestObjectFilters = new ArrayList();
    /**
     * 应答对象过滤器
     */
    List<InterfaceObjectHandler> responseObjectFilters = new ArrayList();
    /**
     * 请求忽略字段过滤器
     */
    List<InterfaceColumnFilter> requestIgnoreColumnFilters = new ArrayList();
    /**
     * 应答忽略字段过滤器
     */
    List<InterfaceColumnFilter> responseIgnoreColumnFilters = new ArrayList();

    @Override
    public DocScanner register(InterfaceObjectHandler handler) {
        ElementSetType elementSetType = handler.getObjectType();
        if (elementSetType == ElementSetType.REQUEST) {
            requestObjectFilters.add(handler);
        } else if (elementSetType == ElementSetType.RESPONSE) {
            responseObjectFilters.add(handler);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }
        return this;
    }

    @Override
    public DocScanner register(InterfaceColumnFilter filter) {
        ElementSetType elementSetType = filter.getObjectType();
        if (elementSetType == ElementSetType.REQUEST) {
            requestIgnoreColumnFilters.add(filter);
        } else if (elementSetType == ElementSetType.RESPONSE) {
            responseIgnoreColumnFilters.add(filter);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }
        return this;
    }

    @Override
    public DocScanner addScanPackage(String... packages) {
        for (String package0 : packages) {
            this.packages.add(package0);
        }
        return this;
    }

    @Override
    public List<String> getScanPackages() {
        List<String> scanPackages = new ArrayList<String>(packages);
        Collections.sort(scanPackages);
        return scanPackages;
    }

    @Override
    public DocScanner init(boolean ifThrow, String... packages) {
        ErrorContextFactory.store();
        ErrorContextFactory.instance().activity("init docScanner...");
        SERVICES.clear();
        INTERFACES.clear();
        scan(ifThrow, packages);
        ErrorContextFactory.recall();
        return this;
    }

    synchronized void scan(boolean ifThrow, String... docScanPackages) {
        //使用类扫描，扫描符合条件的类
        if (this.classLoader == null) {
            this.classLoader = Thread.currentThread().getContextClassLoader();
        }
        ClassScanner classScanner = new ClassScanner(classLoader, true);
        ClassScanner.Filter filter = new ClassScanner.Filter() {
            public boolean accept(Class clazz) {
                return clazz != null && clazz.isInterface() && clazz.isAnnotationPresent(ApidocService.class);
            }
        };
        for (String docScanPackage : this.packages) {
            classScanner.scan(docScanPackage, filter);
        }
        for (String docScanPackage : docScanPackages) {
            classScanner.scan(docScanPackage, filter);
        }
        for (Class<?> serviceClass : classScanner.getClasses()) {
            scan(ifThrow, serviceClass);
        }
    }

    @Override
    public synchronized void scan(boolean ifThrow, Class<?> serviceClass) {
        ApidocService apidocService = (ApidocService) serviceClass.getAnnotation(ApidocService.class);
        String name = serviceClass.getName();
        String usage = "";
        String desc = "";
        String version = "";
        String channel = "";
        if (apidocService != null) {
            if (!StringUtils.isBlank(apidocService.name())) {
                name = apidocService.name();
            }
            if (!StringUtils.isBlank(apidocService.usage())) {
                usage = apidocService.usage();
            }
            if (!StringUtils.isBlank(apidocService.value())) {
                desc = apidocService.value();
            }
            version = apidocService.version();
            channel = apidocService.channel();
        }
        if (StringUtils.isBlank(version)) {
            version = "1.0.0";
        }
        ServiceInfo serviceInfo = new ServiceInfo(this, channel, name, desc, serviceClass, serviceClass.getName(), usage);
        Set<InterfaceInfo> interfaces = null;
        if (SERVICES.containsKey(serviceInfo)) {
            interfaces = SERVICES.get(serviceInfo);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("scan service '{}'", serviceClass);
            }
            interfaces = serviceInfo.getInterfaces();
            SERVICES.put(serviceInfo, interfaces);
        }
        Method[] methods = serviceClass.getMethods();
        //对每一个方法进行遍历，如果没有ApidocInterface注解则直接跳过
        for (Method m : methods) {
            ApidocInterface apidocInterface = m.getAnnotation(ApidocInterface.class);
            String methodName = m.getName();
            String name0 = methodName;
            String version0 = null;
            String usage0 = usage;
            String desc0 = "";

            if (apidocInterface == null) {
                continue;
            } else {
                desc0 = apidocInterface.value();
                if (!StringUtils.isBlank(apidocInterface.name())) {
                    name0 = apidocInterface.name();
                }
                if (!StringUtils.isBlank(apidocInterface.usage())) {
                    usage0 = apidocInterface.usage();
                }
                version0 = apidocInterface.version();
            }
            // 如果接口没有定义版本号，则使用服务上的接口，如果还是没有定义，则使用1.0.0
            if (StringUtils.isBlank(version0)) {
                version0 = version;
            }
            Class requestClass = getRequestClass(serviceClass, m);

            Class responseClass = getResponseClass(serviceClass, m);

            InterfaceInfo interfaceInfo = INTERFACES.get(serviceClass.getName() + ":" + name0 + ":" + version0);
            if (interfaceInfo == null) {
                interfaceInfo = new InterfaceInfo(this, serviceInfo, name0, version0, desc0, usage0, name, methodName, requestClass, responseClass);
                interfaces.add(interfaceInfo);
                INTERFACES.put(interfaceInfo.getFullName(), interfaceInfo);
            } else {
                if (interfaceInfo.getFullName().equals(name + ":" + name0 + ":" + version0)) {
                    log.info("'{}'重新加载....", interfaceInfo.getFullName());
                } else {
                    throw ErrorContextFactory.instance()
                            .code(ErrorCodeEnum.EXISTS_DUPLICATE_INTERFACE_DEFINE.getCode(), ErrorCodeEnum.EXISTS_DUPLICATE_INTERFACE_DEFINE.getDesc(), serviceClass, interfaceInfo.getFullMethodName())
                            .runtimeException();
                }

            }
            BeanExtractor extractor = new BeanExtractor(this);
            //检测是否符合分页接口规范
            if (interfaceInfo.getRequest().isPageable() && !interfaceInfo.getResponse().isPageable()) {
                throw ErrorContextFactory.instance()
                        .code(ErrorCodeEnum.NOT_DEFINED_RESPONSE_PAGINATE.getCode(), ErrorCodeEnum.NOT_DEFINED_RESPONSE_PAGINATE.getDesc(), serviceClass, methodName)
                        .runtimeException();
            }
            if (!interfaceInfo.getRequest().isPageable() && interfaceInfo.getResponse().isPageable()) {
                throw ErrorContextFactory.instance()
                        .code(ErrorCodeEnum.NOT_DEFINED_REQUEST_PAGINATE.getCode(), ErrorCodeEnum.NOT_DEFINED_REQUEST_PAGINATE.getDesc(), serviceClass, methodName)
                        .runtimeException();
            }
        }
    }

    @Override
    public void scan(Class<?> serviceClass) {
        scan(true, serviceClass);
    }

    @Override
    public DocScanner define(ColumnDefineMetadata metadata) {
        return null;
    }

    @Override
    public List<ColumnDefineMetadata> listDefinedColumns(String... names) {
        return null;
    }

    @Override
    public List<String> listDefinedColumnNames() {
        return null;
    }

    Class getResponseClass(Class<?> serviceClass, Method m) {
        Class responseClass = m.getReturnType();
        if (responseClass == Void.TYPE) {
            throw ErrorContextFactory.instance()
                    .code(ErrorCodeEnum.NOT_EXISTS_RESPONSE.getCode(), ErrorCodeEnum.NOT_EXISTS_RESPONSE.getDesc(), serviceClass, m.getName())
                    .runtimeException();
        }
        //调用应答类型过滤器进行处理
        if (!responseObjectFilters.isEmpty()) {
            for (InterfaceObjectHandler handler : responseObjectFilters) {
                if (handler.accept(serviceClass, m, responseClass)) {
                    responseClass = handler.handle(serviceClass, m, responseClass);
                    break;
                }
            }
        }
        if (!Serializable.class.isAssignableFrom(responseClass)) {
            throw ErrorContextFactory.instance()
                    .code(ErrorCodeEnum.NOT_IMPLEMENT_SERIALIZABLE.getCode(), ErrorCodeEnum.NOT_IMPLEMENT_SERIALIZABLE.getDesc(), responseClass)
                    .runtimeException();
        }
        return responseClass;
    }

    Class getRequestClass(Class<?> serviceClass, Method m) {
        Class requestClass;
        if (m.getParameterTypes().length > 0) {
            //获取方法的请求参数
            if (m.getParameterTypes().length == 1) {
                requestClass = m.getParameterTypes()[0];
                //调用请求类型过滤器进行处理
                if (!requestObjectFilters.isEmpty()) {
                    for (InterfaceObjectHandler handler : requestObjectFilters) {
                        if (handler.accept(serviceClass, m, requestClass)) {
                            requestClass = handler.handle(serviceClass, m, requestClass);
                            break;
                        }
                    }
                }

            } else {
                throw ErrorContextFactory.instance()
                        .code(ErrorCodeEnum.EXISTS_TOO_MANY_REQUEST.getCode(), ErrorCodeEnum.EXISTS_TOO_MANY_REQUEST.getDesc(), serviceClass, m.getName())
                        .runtimeException();
            }
        } else {
            throw ErrorContextFactory.instance()
                    .code(ErrorCodeEnum.NOT_EXISTS_REQUEST.getCode(), ErrorCodeEnum.NOT_EXISTS_REQUEST.getDesc(), serviceClass, m.getName())
                    .runtimeException();
        }
        if (!Serializable.class.isAssignableFrom(requestClass)) {
            throw ErrorContextFactory.instance()
                    .code(ErrorCodeEnum.NOT_IMPLEMENT_SERIALIZABLE.getCode(), ErrorCodeEnum.NOT_IMPLEMENT_SERIALIZABLE.getDesc(), requestClass)
                    .runtimeException();
        }
        return requestClass;
    }

    @Override
    public List<ServiceInfo> listService() {
        List<ServiceInfo> list = new ArrayList(SERVICES.keySet());
        Collections.sort(list);
        return list;
    }

    @Override
    public ServiceInfo lookupService(String serviceName) {
        for (ServiceInfo serviceInfo : SERVICES.keySet()) {
            if (serviceInfo.getServiceClassName().equals(serviceName)) {
                return serviceInfo;
            }
        }
        return null;
    }

    @Override
    public ServiceInfo lookupService(Class serviceClass) {
        for (ServiceInfo serviceInfo : SERVICES.keySet()) {
            if (serviceInfo.getServiceClass() == serviceClass) {
                return serviceInfo;
            }
        }
        return null;
    }

    @Override
    public List<InterfaceInfo> listInterface(String... serviceNames) {
        List<InterfaceInfo> list = new ArrayList();
        for (String serviceName : serviceNames) {
            for (ServiceInfo serviceInfo : SERVICES.keySet()) {
                if (serviceInfo.getName().equals(serviceName)) {
                    list.addAll(serviceInfo.getInterfaces());
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public List<InterfaceInfo> listInterface(Class<?>... serviceClasses) {
        List<InterfaceInfo> list = new ArrayList();
        for (Class clazz : serviceClasses) {
            for (ServiceInfo serviceInfo : SERVICES.keySet()) {
                if (serviceInfo.getName().equals(clazz.getName())) {
                    list.addAll(serviceInfo.getInterfaces());
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public InterfaceInfo listInterface(String serviceName, String interfaceName) {
        return listInterface(serviceName, interfaceName, "1.0.0");
    }

    @Override
    public InterfaceInfo listInterface(String serviceName, String interfaceName, String version) {
        for (ServiceInfo serviceInfo : SERVICES.keySet()) {
            if (serviceInfo.getName().equals(serviceName)) {
                for (InterfaceInfo interfaceInfo : serviceInfo.getInterfaces()) {
                    if ((interfaceInfo.getName().equals(interfaceName) || interfaceInfo.getMethodName().equals(interfaceName)) && interfaceInfo.getVersion().equals(version)) {
                        return interfaceInfo;
                    }
                }
            }
        }
        return null;
    }
}
