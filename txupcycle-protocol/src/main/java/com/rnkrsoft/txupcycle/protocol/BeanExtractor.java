/**
 * RNKRSOFT OPEN SOURCE SOFTWARE LICENSE TERMS ver.1
 * - 氡氪网络科技(重庆)有限公司 开源软件许可条款(版本1)
 * 氡氪网络科技(重庆)有限公司 以下简称Rnkrsoft。
 * 这些许可条款是 Rnkrsoft Corporation（或您所在地的其中一个关联公司）与您之间达成的协议。
 * 请阅读本条款。本条款适用于所有Rnkrsoft的开源软件项目，任何个人或企业禁止以下行为：
 * .禁止基于删除开源代码所附带的本协议内容、
 * .以非Rnkrsoft的名义发布Rnkrsoft开源代码或者基于Rnkrsoft开源源代码的二次开发代码到任何公共仓库,
 * 除非上述条款附带有其他条款。如果确实附带其他条款，则附加条款应适用。
 * <p/>
 * 使用该软件，即表示您接受这些条款。如果您不接受这些条款，请不要使用该软件。
 * 如下所述，安装或使用该软件也表示您同意在验证、自动下载和安装某些更新期间传输某些标准计算机信息以便获取基于 Internet 的服务。
 * <p/>
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
 * <p/>
 * 4.免责声明
 * 该软件按“原样”授予许可。 使用本文档的风险由您自己承担。Rnkrsoft 不提供任何明示的担保、保证或条件。
 * 5.版权声明
 * 本协议所对应的软件为 Rnkrsoft 所拥有的自主知识产权，如果基于本软件进行二次开发，在不改变本软件的任何组成部分的情况下的而二次开发源代码所属版权为贵公司所有。
 */
package com.rnkrsoft.txupcycle.protocol;

import com.rnkrsoft.interfaces.EnumBase;
import com.rnkrsoft.interfaces.EnumIntegerCode;
import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Reflector;
import com.rnkrsoft.txupcycle.protocol.annotation.ApidocElement;
import com.rnkrsoft.txupcycle.protocol.enums.ColumnType;
import com.rnkrsoft.txupcycle.protocol.enums.ElementSetType;
import com.rnkrsoft.txupcycle.protocol.enums.FilterType;
import com.rnkrsoft.txupcycle.protocol.enums.PatternType;

import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by rnkrsoft.com on 2018/1/9.
 * 解析级联接口时
 */
class BeanExtractor {
    static Set<Class> NOT_SUPPORTS_BASE_TYPE = new HashSet();
    static Set<Class> SUPPORTS_BASE_TYPE = new HashSet();
    static Set<String> EXCLUDE_FIELDS = new HashSet();
    static Set<String> RESULT_FIELDS = new HashSet();
    static Set<String> PAGE_FIELDS = new HashSet();

    static {
        NOT_SUPPORTS_BASE_TYPE.add(Short.TYPE);
        NOT_SUPPORTS_BASE_TYPE.add(Short.class);
        NOT_SUPPORTS_BASE_TYPE.add(Double.TYPE);
        NOT_SUPPORTS_BASE_TYPE.add(Double.class);
        NOT_SUPPORTS_BASE_TYPE.add(Byte.TYPE);
        NOT_SUPPORTS_BASE_TYPE.add(Byte.class);
        NOT_SUPPORTS_BASE_TYPE.add(BigDecimal.class);
        NOT_SUPPORTS_BASE_TYPE.add(BigInteger.class);
        NOT_SUPPORTS_BASE_TYPE.add(java.util.Date.class);
        NOT_SUPPORTS_BASE_TYPE.add(java.sql.Date.class);
        NOT_SUPPORTS_BASE_TYPE.add(java.sql.Time.class);
        NOT_SUPPORTS_BASE_TYPE.add(java.sql.Timestamp.class);

        SUPPORTS_BASE_TYPE.add(Boolean.TYPE);
        SUPPORTS_BASE_TYPE.add(Boolean.class);
        SUPPORTS_BASE_TYPE.add(Integer.TYPE);
        SUPPORTS_BASE_TYPE.add(Integer.class);
        SUPPORTS_BASE_TYPE.add(Long.TYPE);
        SUPPORTS_BASE_TYPE.add(Long.class);
        SUPPORTS_BASE_TYPE.add(String.class);

        EXCLUDE_FIELDS.add("hash");
        EXCLUDE_FIELDS.add("serialVersionUID");
        EXCLUDE_FIELDS.add("serialPersistentFields");
        EXCLUDE_FIELDS.add("CASE_INSENSITIVE_ORDER");
        EXCLUDE_FIELDS.add("HASHING_SEED");
        EXCLUDE_FIELDS.add("hash32");


        RESULT_FIELDS.add("code");
        RESULT_FIELDS.add("desc");

        PAGE_FIELDS.add("pageSize");
        PAGE_FIELDS.add("pageNo");
        PAGE_FIELDS.add("pageNum");
        PAGE_FIELDS.add("total");
        PAGE_FIELDS.add("records");
        PAGE_FIELDS.add("nodes");
    }

    static boolean notSupportPrimitiveType(Class type) {
        return NOT_SUPPORTS_BASE_TYPE.contains(type);
    }

    static boolean supportPrimitiveType(Class type) {
        return SUPPORTS_BASE_TYPE.contains(type);
    }

    DefaultDocScanner docScanner;


    BeanExtractor(DefaultDocScanner docScanner) {
        this.docScanner = docScanner;
    }

    /**
     * 提取类得信息存放到interfaceInfo对象中得elementSet中
     *
     * @param clazz
     * @param method
     * @param interfaceInfo
     * @param elementSet
     */
    public void extract(Class clazz, Method method, InterfaceInfo interfaceInfo, ElementSet elementSet) {
        ColumnType columnType = interfaceInfo.getRequest() == elementSet ? ColumnType.REQUEST : ColumnType.RESPONSE;
        //获取类型所有字段
        Reflector reflector = GlobalSystemMetadata.reflector(clazz);
        List<Field> fields = reflector.getFields();
        for (Field field : fields) {
            ApidocElement element = field.getAnnotation(ApidocElement.class);
            if (element == null) {//如果字段未标注注解，如果排除字段跳过，如果是必须包含字段，继续处理
                if (EXCLUDE_FIELDS.contains(field.getName())) {
                    continue;
                }
            }
            extract(clazz, method, interfaceInfo, elementSet, columnType, null, field, columnType.getCode(), 0);
        }
    }

    void extract(Class clazz, Method method, InterfaceInfo interfaceInfo, ElementSet root, ColumnType columnType, List<ElementInfo> elements, String parentName, int deep) {
        //获取类型所有字段
        Reflector reflector = GlobalSystemMetadata.reflector(clazz);
        List<Field> fields = reflector.getFields();
        for (Field field : fields) {
            extract(clazz, method, interfaceInfo, root, columnType, elements, field, parentName, deep);
        }
    }

    void extract(Class clazz, Method method, InterfaceInfo interfaceInfo, ElementSet root, ColumnType columnType, List<ElementInfo> elements, Field field, String parentName, int deep) {
        String parentName0 = parentName.isEmpty() ? parentName : parentName + ".";
        if (deep++ > 10) {
            //深度过深，存在循环
            throw ErrorContextFactory.instance()
                    .code(ErrorCodeEnum.TOO_MANY_DEEP_OBJECT_DEFINE.getCode(), ErrorCodeEnum.TOO_MANY_DEEP_OBJECT_DEFINE.getDesc(), clazz)
                    .runtimeException();
        }
        boolean unique = false;
        //描述
        String desc = "";
        //占位符
        String placeholder = "";
        //最小长度
        int minLen = 0;
        //最大长度
        int maxLen = 255;
        //是否必输
        boolean required = true;
        //枚举类
        Class enumClass = null;
        //样式表
        String[] stylesheets = {};
        //CSS class
        String[] cssClasses = {};
        //默认值
        String[] defaults = {};
        //表达式类型
        PatternType patternType = null;
        //表达式数组
        String[] patterns = {};
        //是否只读
        boolean readonly = false;
        int uiWidth = -1;
        //额外参数
        final Properties extras = new Properties();
        ApidocElement element = field.getAnnotation(ApidocElement.class);
        if (element != null) {
            unique = element.unique();
            desc = element.value();
            placeholder = element.placeholder();
            maxLen = element.maxLen();
            minLen = element.minLen();
            required = element.required();
            enumClass = element.enumClass();
            defaults = element.defaults();
            patternType = element.patternType();
            patterns = element.pattern();
            readonly = element.readonly();
        } else {
            //没标注注解直接不提取该字段
            return;
        }
        if (root.getElementSetType() == ElementSetType.REQUEST) {
            for (InterfaceColumnFilter filter : docScanner.requestIgnoreColumnFilters) {
                if (filter.match(method, clazz, field, parentName0 + field.getName()) == FilterType.IGNORE) {
                    return;
                }
            }
        } else {
            for (InterfaceColumnFilter filter : docScanner.responseIgnoreColumnFilters) {
                if (filter.match(method, clazz, field, parentName0 + field.getName()) == FilterType.IGNORE) {
                    return;
                }
            }
        }
        ElementInfo elementInfo = null;
        if (List.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType())) {//如果为类集
            Class paramActualType = getParamActualType(clazz, field);
            if (paramActualType == String.class || paramActualType == Integer.TYPE || paramActualType == Integer.class) {//如果枚举加在List和Set上
                ValueElementInfo.ValueElementInfoBuilder valueElementInfoBuilder = ValueElementInfo.builder(interfaceInfo, root, parentName0 + field.getName())
                        .unique(unique)
                        .javaClass(paramActualType)
                        .desc(desc)
                        .required(required)
                        .minLen(minLen)
                        .maxLen(maxLen)
                        .defaults(defaults)
                        .patternType(patternType)
                        .pattern(patterns)
                        .readonly(readonly)
                        .collectionClass(field.getType())
                        .extras(extras);
                if (enumClass != EnumBase.class) {
                    if (EnumStringCode.class.isAssignableFrom(enumClass)
                            || EnumIntegerCode.class.isAssignableFrom(enumClass)) {
                        extractEnum(enumClass, valueElementInfoBuilder);
                    } else {
                        throw ErrorContextFactory.instance()
                                .code(ErrorCodeEnum.ENUM_CLASS_IS_ILLEGAL.getCode(), ErrorCodeEnum.ENUM_CLASS_IS_ILLEGAL.getDesc(), clazz, field.getName(), enumClass)
                                .runtimeException();
                    }
                } else {
                    if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                        valueElementInfoBuilder.enumDesc("true", "是")
                                .enumDesc("false", "否");
                    }
                }
                ValueElementInfo valueElementInfo = valueElementInfoBuilder.build();
                elementInfo = valueElementInfo;
            } else {
                //对多条的数据进行提取
                List<ElementInfo> tempElements = new ArrayList();
                extract(paramActualType, method, interfaceInfo, root, columnType, tempElements, parentName0 + field.getName(), deep);
                FormElementInfo.FormElementInfoBuilder formInfoBuilder = FormElementInfo.builder(interfaceInfo, root, parentName0 + field.getName()).desc(desc);
                formInfoBuilder.beanClass(paramActualType)
                        .javaClass(field.getType())
                        .required(required)
                        .extras(extras)
                        .element(tempElements.toArray(new ElementInfo[tempElements.size()]));
                elementInfo = formInfoBuilder.build();
            }
        } else if (notSupportPrimitiveType(field.getType())) {//处理不支持的原生数据类型
            throw ErrorContextFactory.instance()
                    .code(ErrorCodeEnum.NOT_SUPPORT_PRIMITIVE_TYPE_DEFINE.getCode(), ErrorCodeEnum.NOT_SUPPORT_PRIMITIVE_TYPE_DEFINE.getDesc(), clazz, field.getType())
                    .runtimeException();
        } else if (supportPrimitiveType(field.getType())) {//处理支持的原生数据类型
            ValueElementInfo.ValueElementInfoBuilder valueElementInfoBuilder = ValueElementInfo.builder(interfaceInfo, root, parentName0 + field.getName())
                    .unique(unique)
                    .javaClass(field.getType())
                    .desc(desc)
                    .required(required)
                    .defaults(defaults)
                    .readonly(readonly)
                    .extras(extras);
            if (enumClass != EnumBase.class) {
                if (EnumStringCode.class.isAssignableFrom(enumClass)
                        || EnumIntegerCode.class.isAssignableFrom(enumClass)) {
                    extractEnum(enumClass, valueElementInfoBuilder);
                } else {
                    throw ErrorContextFactory.instance()
                            .code(ErrorCodeEnum.ENUM_CLASS_IS_ILLEGAL.getCode(), ErrorCodeEnum.ENUM_CLASS_IS_ILLEGAL.getDesc(), clazz.getName(), field.getName(), enumClass)
                            .runtimeException();
                }
            } else {
                if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                    valueElementInfoBuilder.enumDesc("true", "是").enumDesc("false", "否");
                }
            }
            if (field.getType() == Integer.class || field.getType() == Integer.TYPE) {

            } else if (field.getType() == Long.class || field.getType() == Long.TYPE) {
            } else if (field.getType() == Boolean.class || field.getType() == Boolean.TYPE) {
            } else if (field.getType() == String.class) {
            }
            valueElementInfoBuilder.minLen(minLen)
                    .javaClass(field.getType())
                    .maxLen(maxLen)
                    .patternType(patternType)
                    .pattern(patterns);
            elementInfo = valueElementInfoBuilder.build();
        } else {//如果字段只能为定义的对象类型，必须实现序列化接口
            if (!Serializable.class.isAssignableFrom(field.getType())) {
                throw ErrorContextFactory.instance()
                        .code(ErrorCodeEnum.NOT_IMPLEMENT_SERIALIZABLE.getCode(), ErrorCodeEnum.NOT_IMPLEMENT_SERIALIZABLE.getDesc(), field.getType())
                        .runtimeException();
            }
            if (clazz == field.getType()) {
                //字段存在循环定义
                throw ErrorContextFactory.instance()
                        .code(ErrorCodeEnum.TYPE_EXISTS_CIRCULAR_DEFINITION.getCode(), ErrorCodeEnum.TYPE_EXISTS_CIRCULAR_DEFINITION.getDesc(), clazz)
                        .message("字段存在循环定义，正在解析的类被用作了当前类的字段'{}'", field.getName())
                        .solution("将类'{}'的字段'{}'从'{}'类型修改为新的类型", clazz, field.getName(), field.getType())
                        .runtimeException();
            }
            BeanElementInfo beanElementInfo = BeanElementInfo.builder(interfaceInfo, root, parentName0 + field.getName())
                    .javaClass(field.getType())
                    .desc(desc)
                    .extras(extras)
                    .build();
            List<ElementInfo> tempElements = new ArrayList();
            //提取Bean上的元信息
            extract(field.getType(), method, interfaceInfo, root, columnType, tempElements, parentName0 + field.getName(), deep);
            beanElementInfo.getElements().addAll(tempElements);
            elementInfo = beanElementInfo;
        }
        if (elements == null) {
            if (field.getType() == RequestPage.Pagination.class){
                root.getPagination().add(elementInfo);
            }else if(field.getType() == ResponsePage.Pagination.class){
                root.getPagination().add(elementInfo);
            }else if(field.getType() == Response.Result.class){
                root.getResult().add(elementInfo);
            }else {
                root.getElements().add(elementInfo);
            }
        } else {
            elements.add(elementInfo);
        }
        //进行全字段名注册
        root.getFullNameElements().put(parentName0 + elementInfo.getName(), elementInfo);
    }

    private Class getParamActualType(Class clazz, Field field) {
        Class modelClass = null;
        //检查泛型
        if (field.getGenericType() instanceof ParameterizedType) {
            //获取泛型
            ParameterizedType target = (ParameterizedType) field.getGenericType();
            Type[] parameters = target.getActualTypeArguments();
            if (parameters.length > 0) {
                if (parameters[0] instanceof Class) {
                    modelClass = (Class<?>) parameters[0];
                } else if (parameters[0] instanceof TypeVariable) {
                    Type superclassType = clazz.getGenericSuperclass();
                    if (superclassType instanceof ParameterizedType) {
                        ParameterizedType superclassType1 = (ParameterizedType) superclassType;
                        Type modelType = superclassType1.getActualTypeArguments()[0];
                        modelClass = (Class<?>) modelType;
                    } else if (superclassType instanceof Class) {
                        throw ErrorContextFactory.instance()
                                .code(ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_COLLECTION.getCode(), ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_COLLECTION.getDesc(), clazz)
                                .runtimeException();
                    } else {
                        throw ErrorContextFactory.instance()
                                .code(ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_FIELD.getCode(), ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_FIELD.getDesc(), clazz)
                                .runtimeException();
                    }
                } else {
                    throw ErrorContextFactory.instance()
                            .code(ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_FIELD.getCode(), ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_FIELD.getDesc(), parameters[0])
                            .runtimeException();
                }
            } else {
                throw ErrorContextFactory.instance()
                        .code(ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_COLLECTION.getCode(), ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_COLLECTION.getDesc(), clazz)
                        .runtimeException();
            }
        }
        if (modelClass == null) {
            throw ErrorContextFactory.instance()
                    .code(ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_COLLECTION.getCode(), ErrorCodeEnum.NOT_SUPPORT_NON_GENERIC_COLLECTION.getDesc(), clazz)
                    .runtimeException();
        }
        return modelClass;
    }


    void extractEnum(Class<?> enumClass, ValueElementInfo.ValueElementInfoBuilder builder) {
        Object[] values = null;
        if (enumClass.isEnum()) {
            values = enumClass.getEnumConstants();
        } else {
            try {
                Method valuesMethod = enumClass.getMethod("values");
                values = (Object[]) valuesMethod.invoke(null);
            } catch (NoSuchMethodException e) {
                //不应该发生
                return;
            } catch (InvocationTargetException e) {
                //不应该发生
                return;
            } catch (IllegalAccessException e) {
                //不应该发生
                return;
            }
        }
        Method getCodeMethod = null;
        Method getDescMethod = null;
        try {
            getCodeMethod = enumClass.getMethod("getCode");
            getDescMethod = enumClass.getMethod("getDesc");
        } catch (NoSuchMethodException e) {
            return;
        }
        if (getCodeMethod != null && getDescMethod != null) {
            for (Object val : values) {
                try {
                    Object code = getCodeMethod.invoke(val);
                    String desc = (String) getDescMethod.invoke(val);
                    builder.enumDesc(code == null ? "" : code.toString(), desc);
                } catch (IllegalAccessException e) {
                    //nothing
                } catch (InvocationTargetException e) {
                    //nothing
                }
            }
        }
    }
}
