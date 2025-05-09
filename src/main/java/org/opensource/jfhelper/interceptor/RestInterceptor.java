package org.opensource.jfhelper.interceptor;

import java.io.File;
import java.lang.reflect.Method;

import org.opensource.jfhelper.annocation.Get;
import org.opensource.jfhelper.annocation.Post;
import org.opensource.jfhelper.annocation.Rest;
import org.opensource.jfhelper.annocation.View;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.render.Render;

/**
 * 统一返回JSON的拦截器
 *
 * @author seiya
 */
public class RestInterceptor implements Interceptor {

    private final POST post = Aop.get(POST.class);
    private final GET get = Aop.get(GET.class);

    /**
     * 使用返回值直接返回JSON数据
     */
    @Override
    public void intercept(Invocation inv) {
        // 使用注解处理请求的方法是POST 还是 GET
        if (inv.getMethod().isAnnotationPresent(Post.class)) {
            post.intercept(inv);
        } else if (inv.getMethod().isAnnotationPresent(Get.class)) {
            get.intercept(inv);
        } else {
            inv.invoke();
        }

        // 获取方法的返回值的信息
        Method invMethod = inv.getMethod();
        Controller controller = inv.getController();

        Class<?> returnType = invMethod.getReturnType();

        // 如果是void的返回值，标记rest返回空，标记view或者不标记不做特殊处理。
        if (returnType.isAssignableFrom(Void.class) || returnType.isAssignableFrom(void.class)) {
            if (invMethod.isAnnotationPresent(Rest.class)) {
                controller.renderNull();
            }
        } else {
            // 如果有返回值， 标记view的根据字符串或者render返回， 其他根据返回的参数类型返回结果
            if (invMethod.isAnnotationPresent(View.class)) {
                if (returnType.isAssignableFrom(String.class)) {
                    controller.render((String) inv.getReturnValue());
                } else if (returnType.isAssignableFrom(Render.class)) {
                    controller.render((Render) inv.getReturnValue());
                } else if (returnType.isAssignableFrom(Void.class) || returnType.isAssignableFrom(void.class)) {
                    System.out.println("pass");
                } else {
                    controller.renderNull();
                }
            } else {
                if (returnType.isAssignableFrom(File.class)) {
                    controller.renderFile((File) inv.getReturnValue());
                } else if (returnType.isAssignableFrom(String.class)) {
                    controller.renderText(inv.getReturnValue());
                } else if(returnType.isAssignableFrom(void.class) || returnType.isAssignableFrom(Void.class)){
                    System.out.println("pass");
                } else {
                    controller.renderJson((Object) inv.getReturnValue());
                }
            }
        }

    }
}
