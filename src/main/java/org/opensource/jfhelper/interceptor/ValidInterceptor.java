package org.opensource.jfhelper.interceptor;

import org.opensource.jfhelper.exception.ValidException;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.Ret;

/**
 * 全局捕获ValidException，并抛出异常提示。 <br/>
 * 抛出的形式以Json返回。
 * 
 * @author seiya
 *
 */
public class ValidInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {

		try {
			inv.invoke();
		} catch (ValidException e) {
			inv.getController().renderJson(Ret.fail(e.getMessage()));
		}

	}

}
