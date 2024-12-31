package org.opensource.jfhelper.props;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.jfinal.config.Routes;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.json.IJsonFactory;
import com.jfinal.json.JacksonFactory;
import org.opensource.jfhelper.annocation.JFinalProperties;
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
@JFinalProperties(prefix = Const.jFinal)
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "jFinal")
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
     * 默认json解析器， 默认使用 fastjson
     */
    private String jsonLib = "fastjson";


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
    private WxProp wechat;

    /**
     * 微信小程序的配置
     */
    @NestedConfigurationProperty
    private WxaProp wxMini;

    /**
     * 增加Redis缓存的配置
     */
    @NestedConfigurationProperty
    private RedisProp redis;


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

    public WxProp getWechat() {
        return wechat;
    }

    public void setWechat(WxProp wechat) {
        this.wechat = wechat;
    }

    public WxaProp getWxMini() {
        return wxMini;
    }

    public void setWxMini(WxaProp wxMini) {
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

    public RedisProp getRedis() {
        return redis;
    }

    public void setRedis(RedisProp redis) {
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

    @Override
    public String toString() {
        return "JFinalProp{" +
                "encoding='" + encoding + '\'' +
                ", devMode=" + devMode +
                ", inject=" + inject +
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