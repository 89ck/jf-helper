package org.opensource.jfhelper.exception;

import java.util.Objects;

/**
 * 验证抛出的错误，用于验证提示
 *
 * @author seiya
 */
@SuppressWarnings("serial")
public class ValidException extends RuntimeException {

    private int code = 500;
    private Object data;

    /**
     * 构造函数
     *
     * @param message
     */
    public ValidException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message
     * @param code
     */
    public ValidException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ValidException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    /**
     * 获取错误码， 默认500
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
