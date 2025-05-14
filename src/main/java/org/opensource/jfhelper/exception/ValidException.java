package org.opensource.jfhelper.exception;

/**
 * 验证抛出的错误，用于验证提示
 *
 * @author seiya
 */
@SuppressWarnings("serial")
public class ValidException extends RuntimeException {

    private int code = 500;

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
     * @param message
     * @param code
     */
    public ValidException(String message, int code) {
        super(message);
        this.code = code;
    }

    /**
     * 获取错误码， 默认500
     *
     * @return
     */
    public int getCode() {
        return code;
    }
}
