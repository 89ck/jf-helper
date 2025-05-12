package org.opensource.jfhelper.annocation;

import java.lang.annotation.*;

/**
 * 用于注解某个接口的实现类
 *
 * @author liyingfu
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

//    /**
//     * 是否是第一优先级
//     *
//     * @return
//     */
//    boolean isPrimary() default false;
}
