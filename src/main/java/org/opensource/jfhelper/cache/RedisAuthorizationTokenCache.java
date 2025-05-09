package org.opensource.jfhelper.cache;

import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import org.opensource.jfhelper.Context;
import org.opensource.jfhelper.authorization.AuthorizationToken;
import org.opensource.jfhelper.authorization.AuthorizationTokenCache;
import org.opensource.jfhelper.props.RedisProp;
import org.opensource.jfhelper.utils.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Redis 缓存实现
 *
 * @author liyingfu
 */
public class RedisAuthorizationTokenCache implements AuthorizationTokenCache {

    private final static String CACHE_NAME = Const.ANTI_SHAKE_TOKEN_CACHE_NAME;

    private static final String KEY_ = "AT_";

    private static Cache CACHE;

    /**
     * 获取缓存实例
     *
     * @return
     */
    private synchronized Cache getCache() {
        if (Objects.nonNull(CACHE)) {
            return CACHE;
        }
        for (RedisProp redisProp : Context.getConfig().getRedis()) {
            if (StrUtil.equals(redisProp.getCacheName(), CACHE_NAME)) {
                CACHE = Redis.use(CACHE_NAME);
                break;
            }
        }
        if (Objects.isNull(CACHE)) {
            CACHE = Redis.use();
        }
        return CACHE;
    }

    @Override
    public void put(AuthorizationToken token) {
        getCache().setex(KEY_ + token.getId(), (token.getExpirationTime() - System.currentTimeMillis()) / 1000, token);
    }

    @Override
    public void remove(AuthorizationToken token) {
        getCache().del(KEY_ + token.getId());
    }

    @Override
    public boolean contains(AuthorizationToken token) {
        Set<String> keys = getCache().keys(KEY_ + token.getId());
        return keys.size() > 0;
    }

    @Override
    public List<AuthorizationToken> getAll() {
        List<AuthorizationToken> tokens = new ArrayList<>();
        Set<String> keys = getCache().keys(KEY_ + "*");
        for (String key : keys) {
            tokens.add(getCache().get(key));
        }
        return tokens;
    }

    @Override
    public AuthorizationToken get(String tokenId) {
        return getCache().get(KEY_ + tokenId);
    }
}
