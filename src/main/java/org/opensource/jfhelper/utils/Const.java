package org.opensource.jfhelper.utils;

/**
 * 常量类
 * @author seiya
 */
public final class Const {
    /**
     * 设置配置文件的路径
     */
    public static final String CONFIG_PATH = "classpath:application.yml";

    /**
     * jFinal的基础配置类
     */
    public static final String jFinal = "jFinal";

    /**
     * 数据库的固定配置前缀
     */
    public static final String DATABASE = jFinal + ".database";

    /**
     * 微信配置的前缀
     */
    public static final String WX_WEB = jFinal + ".wechat";

    /**
     * 微信小程序的前缀
     */
    public static final String WX_APP = jFinal + ".wx-mini";

    /**
     * 访问授权的token
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * 防重复提交的token名
     */
    public static final String SUBMIT_TOKEN = "_STASH_TOKEN";
}
