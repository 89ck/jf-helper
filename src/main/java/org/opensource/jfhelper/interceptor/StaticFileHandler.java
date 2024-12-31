package org.opensource.jfhelper.interceptor;

import com.jfinal.handler.Handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensource.jfhelper.Context;

/**
 * 静态文件直接放行的操作
 *
 * @author seiya
 */
public class StaticFileHandler extends Handler {
	
    /**
     * 设置静态文件的结尾判断符号
     */
    private final static List<String> EXT_NAMES = Context.getConfig().getStaticExtNames();

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (isStaticFile(target)) {
            return;
        }
        next.handle(target, request, response, isHandled);
    }

    /**
     * 验证是否是静态文件的结尾,
     * @param target
     * @return
     */
    private boolean isStaticFile(String target) {
        for (String end : EXT_NAMES) {
            if (target.endsWith(end)) {
                return true;
            }
        }
        return false;
    }
}
