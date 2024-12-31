package org.opensource.jfhelper.authorization;

import java.util.List;

/**
 * 授权token缓存
 *
 * @author seiya
 */
public interface AuthorizationTokenCache {

    /**
     * 添加token
     *
     * @param token
     */
    void put(AuthorizationToken token);

    /**
     * 移除token
     *
     * @param token
     */
    void remove(AuthorizationToken token);

    /**
     * 判断token是否存在
     *
     * @param token
     * @return
     */
    boolean contains(AuthorizationToken token);

    /**
     * 获取全部token
     *
     * @return
     */
    List<AuthorizationToken> getAll();

    /**
     * 获取唯一的token信息
     * @param tokenId
     * @return
     */
    AuthorizationToken get(String tokenId);

}
