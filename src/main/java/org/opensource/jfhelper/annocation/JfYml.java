package org.opensource.jfhelper.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置类的注解实现, 实现了这个注解的类可以成为配置文件
 * @author seiya
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JfYml {

    /**
     * 前缀字符串， 用于根据前缀自动匹配数据。
     * @return
     */
    String prefix() default "";
}
