package org.opensource.jfhelper.authorization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的授权token数据信息的缓存实现
 *
 * @author seiya
 */
public class DefaultAuthorizationTokenCache implements AuthorizationTokenCache {

    private static final Map<String, AuthorizationToken> TOKEN_CACHE = new ConcurrentHashMap<>();

    @Override
    public void put(AuthorizationToken token) {
        TOKEN_CACHE.put(token.getId(), token);
    }

    @Override
    public void remove(AuthorizationToken token) {
        TOKEN_CACHE.remove(token.getId());
    }

    @Override
    public boolean contains(AuthorizationToken token) {
        return TOKEN_CACHE.containsKey(token.getId());
    }

    @Override
    public List<AuthorizationToken> getAll() {
        return new ArrayList<>(TOKEN_CACHE.values());
    }

    @Override
    public AuthorizationToken get(String tokenId) {
        return TOKEN_CACHE.get(tokenId);
    }
}
