package org.opensource.jfhelper.utils;

/**
 * 返回结果
 *
 * @author liyingfu
 */
public class Ret {

    public static final String STATE_OK = "ok";
    public static final String STATE_FAIL = "fail";

    public static final int CODE_OK = 200;
    public static final int CODE_FAIL = 500;

    /**
     * 用户未登录的状态码
     */
    public static final int UN_LOGIN = 401;

    /**
     * 找不到状态码
     */
    public static final int NOT_FOUND = 404;

    /**
     * 未绑定小程序
     */
    public static final int NOT_BIND = 402;

    /**
     * 空数据
     */
    public static final int EMPTY_DATA = 555;

    /**
     * 超出范围
     */
    public static final int OUT_RANGE = 886;

    /**
     * 状态码
     */
    private int code;
    /**
     * 状态
     */
    private String state;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据
     */
    private Object data;

    /**
     * 当前返回值的时间戳
     */
    private Long timestamp;

    public int getCode() {
        return code;
    }

    /**
     * 设置状态码
     *
     * @param code
     * @return
     */
    public Ret setCode(int code) {
        this.code = code;
        return this;
    }

    public String getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state
     * @return
     */
    public Ret setState(String state) {
        this.state = state;
        return this;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 设置错误消息
     *
     * @param message
     * @return
     */
    public Ret setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data
     * @return
     */
    public Ret setData(Object data) {
        this.data = data;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    private Ret() {
        super();
        this.setTimestamp(System.currentTimeMillis());
    }

    /**
     * 返回成功
     *
     * @return
     */
    public static Ret ok() {
        Ret ret = new Ret();
        ret.setCode(CODE_OK);
        ret.setState(STATE_OK);
        ret.setTimestamp(System.currentTimeMillis());
        return ret;
    }

    /**
     * 返回成功
     *
     * @param message
     * @return
     */
    public static Ret ok(String message) {
        Ret ret = new Ret();
        ret.setCode(CODE_OK);
        ret.setState(STATE_OK);
        ret.setMessage(message);
        ret.setTimestamp(System.currentTimeMillis());
        return ret;
    }

    /**
     * 返回失败
     *
     * @param message
     * @return
     */
    public static Ret fail(String message) {
        Ret ret = new Ret();
        ret.setCode(CODE_FAIL);
        ret.setState(STATE_FAIL);
        ret.setMessage(message);
        ret.setTimestamp(System.currentTimeMillis());
        return ret;
    }

    /**
     * 返回失败
     *
     * @param message
     * @param data
     * @return
     */
    public static Ret fail(String message, Object data) {
        Ret ret = new Ret();
        ret.setCode(CODE_FAIL);
        ret.setState(STATE_FAIL);
        ret.setMessage(message);
        ret.setData(data);
        ret.setTimestamp(System.currentTimeMillis());
        return ret;
    }

    /**
     * 判断是否成功
     *
     * @return
     */
    public boolean isSucceed() {
        return this.getCode() == CODE_OK;
    }


}
