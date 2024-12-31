package org.opensource.jfhelper.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当存在这个注解的时候，赋值默认忽略 <br/>
 * 可以用于整个类， 或单独的某个参数
 * 
 * @author seiya
 *
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Ignore {

}
