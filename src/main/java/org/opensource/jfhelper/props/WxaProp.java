package org.opensource.jfhelper.props;

import org.opensource.jfhelper.annocation.JFinalProperties;
import org.opensource.jfhelper.utils.Const;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序的基础配置类
 * @author seiya
 */
@Component
@JFinalProperties(prefix = Const.WX_APP)
@ConfigurationProperties(prefix = Const.WX_APP)
public class WxaProp {

    /**
     * 小程序的appId
     */
    private String appId;

    /**
     * 小程序的密钥
     */
    private String appSecret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String toString() {
        return "WxaConfiguration{" +
                "appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                '}';
    }
}
