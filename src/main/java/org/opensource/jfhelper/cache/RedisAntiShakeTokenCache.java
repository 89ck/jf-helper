package org.opensource.jfhelper.cache;

import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.token.ITokenCache;
import com.jfinal.token.Token;
import org.opensource.jfhelper.Context;
import org.opensource.jfhelper.props.RedisProp;
import org.opensource.jfhelper.utils.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 防抖提交的Redis缓存实现
 *
 * @author liyingfu
 */
public class RedisAntiShakeTokenCache implements ITokenCache {

    private final static String CACHE_NAME = Const.ANTI_SHAKE_TOKEN_CACHE_NAME;

    private static final String KEY_ = "AST_";

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
    public void put(Token token) {
        getCache().setex(KEY_ + token.getId(), (token.getExpirationTime() - System.currentTimeMillis()) / 1000, token);
    }

    @Override
    public void remove(Token token) {
        getCache().del(KEY_ + token.getId());
    }

    @Override
    public boolean contains(Token token) {
        Set<String> keys = getCache().keys(KEY_ + token.getId());
        return keys.size() > 0;
    }

    @Override
    public List<Token> getAll() {
        Set<String> keys = getCache().keys(KEY_ + "*");
        List<Token> tokens = new ArrayList<>();
        for (String key : keys) {
            tokens.add(getCache().get(key));
        }
        return tokens;
    }
}
