package org.opensource.jfhelper.core;

import cn.hutool.core.lang.ClassScanner;
import com.jfinal.aop.AopFactory;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.validate.Validator;
import org.opensource.jfhelper.annocation.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 AopFactory 的自定义扩展，用于支持@Resource注入注解 和 @Bean 接口实现类标记注解
 *
 * @author liyingfu
 */
public class JfAopFactory extends AopFactory {

    private static Set<Class<?>> classes;

    private final static Map<Class<?>, Class<?>> FIELD_IMPL_CLASS_MAP = new ConcurrentHashMap<>(100);

    public JfAopFactory(String classPackage) {
        super();
        classes = ClassScanner.scanAllPackageByAnnotation(classPackage, Component.class);
    }

    @Override
    protected void doInject(Class<?> targetClass, Object targetObject) throws ReflectiveOperationException {
        targetClass = getUsefulClass(targetClass);
        Field[] fields = targetClass.getDeclaredFields();
        if (fields.length != 0) {
            for (Field field : fields) {
                Resource inject = field.getAnnotation(Resource.class);
                if (inject == null) {
                    continue;
                }

                Class<?> fieldInjectedClass = getResourceClass(field);
                if (fieldInjectedClass == Void.class) {
                    fieldInjectedClass = field.getType();
                }

                Object fieldInjectedObject = doGet(fieldInjectedClass);
                field.setAccessible(true);
                field.set(targetObject, fieldInjectedObject);
            }
        }

        // 是否对超类进行注入
        if (injectSuperClass) {
            Class<?> c = targetClass.getSuperclass();
            if (c != Controller.class && c != Object.class && c != Validator.class && c != Model.class && c != null) {
                doInject(c, targetObject);
            }
        }

    }

    /**
     * 获取注入的类
     *
     * @param field
     * @return
     */
    private Class<?> getResourceClass(Field field) {
        Class<?> returnClass = FIELD_IMPL_CLASS_MAP.get(field.getType());
        if (Objects.isNull(returnClass)) {
            boolean isInject = false;
            for (Class<?> aClass : classes) {

                Class<?>[] interfaces = aClass.getInterfaces();
                if (Arrays.asList(interfaces).contains(field.getType())) {
                    returnClass = aClass;
                    isInject = true;
                    break;
                }
            }
            if (!isInject) {
                returnClass = Void.class;
            }
            FIELD_IMPL_CLASS_MAP.put(field.getType(), returnClass);
        }
        return returnClass;
    }
}
