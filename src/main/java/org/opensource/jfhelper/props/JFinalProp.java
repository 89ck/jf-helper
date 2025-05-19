package org.opensource.jfhelper.props;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.jfinal.config.Routes;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.json.IJsonFactory;
import com.jfinal.json.JacksonFactory;
import org.opensource.jfhelper.annocation.JfYml;
import org.opensource.jfhelper.exception.ValidException;
import org.opensource.jfhelper.utils.Const;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础的配置参数
 *
 * @author seiya
 */
@Component
@JfYml(prefix = Const.jFinal)
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = Const.jFinal)
public class JFinalProp {

    /**
     * 默认编码类型， UTF-8
     */
    private String encoding = "UTF-8";

    /**
     * 默认非devMode模式.开发时建议设置为true
     */
    private Boolean devMode = Boolean.FALSE;

    /**
     * 默认注入模式， 开启注入
     */
    private Boolean inject = Boolean.TRUE;

    /**
     * 是否使用 jFinal 的原始注入注解， 默认false, 请使用@Resource注解注入 + @Bean注解标记接口实现类
     */
    private Boolean useInject = Boolean.FALSE;

    /**
     * 默认json解析器， 默认使用 fastjson
     */
    private String jsonLib = "fastjson";

    /**
     * 是否使用rest风格的请求，即 Content-Type	application/json;charset=utf-8, 默认为true
     */
    private Boolean useRest = Boolean.TRUE;


    /**
     * 设置路由的接口,课设置多个路由,按先后顺序加载
     */
    private List<String> routes;

    /**
     * 设置静态文件的结尾扩展名
     */
    private List<String> staticExtNames = Arrays.asList(
            ".js", ".css", ".jpg", ".png", ".gif", ".ico", ".html", ".htm", ".map", ".mp4", ".mp3"
    );

    /**
     * 数据库的配置
     */
    @NestedConfigurationProperty
    private DbProp database;

    /**
     * 微信的配置
     */
    @NestedConfigurationProperty
    private WeChatProp wechat;

    /**
     * 微信小程序的配置
     */
    @NestedConfigurationProperty
    private WxMiniProp wxMini;


    /**
     * 增加Redis缓存的配置
     */
    @NestedConfigurationProperty
    private List<RedisProp> redis;


    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Boolean getDevMode() {
        return devMode;
    }

    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    public Boolean getInject() {
        return inject;
    }

    public void setInject(Boolean inject) {
        this.inject = inject;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public DbProp getDatabase() {
        return database;
    }

    public void setDatabase(DbProp database) {
        this.database = database;
    }

    public WeChatProp getWechat() {
        return wechat;
    }

    public void setWechat(WeChatProp wechat) {
        this.wechat = wechat;
    }

    public WxMiniProp getWxMini() {
        return wxMini;
    }

    public void setWxMini(WxMiniProp wxMini) {
        this.wxMini = wxMini;
    }

    public void setJsonLib(String jsonLib) {
        this.jsonLib = jsonLib;
    }

    public List<String> getStaticExtNames() {
        return staticExtNames;
    }

    public void setStaticExtNames(List<String> staticExtNames) {
        this.staticExtNames = staticExtNames;
    }

    public List<RedisProp> getRedis() {
        return redis;
    }

    public void setRedis(List<RedisProp> redis) {
        this.redis = redis;
    }


    public List<Routes> getRoutes() {
        List<Routes> routes = new ArrayList<>();
        if (CollUtil.isNotEmpty(this.routes)) {
            for (String route : this.routes) {
                routes.add(ReflectUtil.newInstance(route));
            }
        }
        return routes;
    }


    public IJsonFactory getJsonLib() {
        JsonType jsonType = JsonType.valueOf(this.jsonLib.toUpperCase());
        switch (jsonType) {
            case FASTJSON:
                return FastJsonFactory.me();
            case JACKSON:
                return JacksonFactory.me();
            default:
                try {
                    return ReflectUtil.newInstance(this.jsonLib);
                } catch (Exception e) {
                    throw new ValidException("not support jsonType !");
                }
        }
    }

    public Boolean getUseRest() {
        return useRest;
    }

    public void setUseRest(Boolean useRest) {
        this.useRest = useRest;
    }

    public Boolean getUseInject() {
        return useInject;
    }

    public void setUseInject(Boolean useInject) {
        this.useInject = useInject;
    }

    @Override
    public String toString() {
        return "JFinalProp{" +
                "encoding='" + encoding + '\'' +
                ", devMode=" + devMode +
                ", inject=" + inject +
                ", useInject=" + useInject +
                ", jsonLib='" + jsonLib + '\'' +
                ", routes=" + routes +
                ", staticExtNames=" + staticExtNames +
                ", database=" + database +
                ", wechat=" + wechat +
                ", wxMini=" + wxMini +
                ", redis=" + redis +
                '}';
    }
}
