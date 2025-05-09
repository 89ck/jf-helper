package org.opensource.jfhelper.props;

import org.opensource.jfhelper.annocation.JfYml;
import org.opensource.jfhelper.utils.Const;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信公众号的基础配置文件。
 * @author seiya
 */
@Component
@JfYml(prefix = Const.WECHAT)
@ConfigurationProperties(prefix = Const.WECHAT)
public class WeChatProp {

    /**
     * 公众号的appId
     */
    private String appId;

    /**
     * 公众号的appSecret
     */
    private String appSecret;

    /**
     * 公众号自定义token
     */
    private String token = "TEST_MY_WECHAT";

    /**
     * 是否使用消息加密
     */
    private boolean messageEncrypt = false;

    /**
     * 消息加密的文本
     */
    private String encodingAesKey;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isMessageEncrypt() {
        return messageEncrypt;
    }

    public void setMessageEncrypt(boolean messageEncrypt) {
        this.messageEncrypt = messageEncrypt;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    @Override
    public String toString() {
        return "WxConfiguration{" +
                "appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", token='" + token + '\'' +
                ", messageEncrypt=" + messageEncrypt +
                ", encodingAesKey='" + encodingAesKey + '\'' +
                '}';
    }
}
