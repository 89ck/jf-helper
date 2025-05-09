package org.opensource.jfhelper.props;

import org.opensource.jfhelper.annocation.JfYml;
import org.opensource.jfhelper.utils.Const;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Protocol;

/**
 * 实现Redis的缓存配置
 * @author seiya
 */
@Component
@JfYml(prefix = Const.REDIS)
@ConfigurationProperties(prefix = Const.REDIS)
public class RedisProp {

    /**
     * 设置Redis的统一默认缓存名
     */
    private String cacheName = "_def_redis_cache";
    /**
     * Redis服务器地址 默认 localhost
     */
    protected String host = Protocol.DEFAULT_HOST;
    /**
     * Redis服务器端口号 默认 6379
     */
    protected Integer port = Protocol.DEFAULT_PORT;
    /**
     * 连接超时时间, jfinal 默认 2000 ms
     */
    protected Integer timeout = Protocol.DEFAULT_TIMEOUT;
    /**
     * Redis服务器密码， 默认无密码
     */
    protected String password;
    /**
     * Redis 默认使用的数据库编号, 不填使用jfinal默认的
     */
    protected Integer database = Protocol.DEFAULT_DATABASE;
    /**
     * Redis客户端名称， 默认无
     */
    protected String clientName;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
