package org.opensource.jfhelper.utils;

/**
 * 常量类
 *
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
    public static final String WECHAT = jFinal + ".wechat";

    /**
     * 微信小程序的前缀
     */
    public static final String WXMINI = jFinal + ".wxmini";

    /**
     * 访问授权的token
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * 防重复提交的token名
     */
    public static final String SUBMIT_TOKEN = "_STASH_TOKEN";

    /**
     * redis的配置前缀
     */
    public static final String REDIS = jFinal + ".redis";

    /**
     * 防抖缓存的tokenCache的名字
     */
    public static final String ANTI_SHAKE_TOKEN_CACHE_NAME = "ANTI_SHAKE_TOKEN_CACHE";
}
