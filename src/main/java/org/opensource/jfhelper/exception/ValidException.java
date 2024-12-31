package org.opensource.jfhelper.exception;

/**
 * 验证抛出的错误，用于验证提示
 * @author seiya
 */
@SuppressWarnings("serial")
public class ValidException extends RuntimeException{

    /**
     * 构造函数
     * @param message
     */
    public ValidException(String message) {
        super(message);
    }
}
