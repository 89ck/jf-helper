package org.opensource.jfhelper.authorization;

import cn.hutool.core.util.IdUtil;
import com.jfinal.core.Const;

import java.util.*;

/**
 * 授权token的管理器
 *
 * @author seiya
 */
public class AuthorizationTokenManager {

    private static AuthorizationTokenCache tokenCache;

    /**
     * token的过期时长的设置区间
     */
    private static long secondsOfTimeOut;

    private AuthorizationTokenManager() {

    }

    public static void init(AuthorizationTokenCache tokenCache) {
        if (tokenCache == null) {
            return;
        }

        AuthorizationTokenManager.tokenCache = tokenCache;

        // Token最小过期时间的一半时间作为任务运行的间隔时间
        long halfTimeOut = Const.MIN_SECONDS_OF_TOKEN_TIME_OUT * 1000 / 2;
        new Timer("AuthorizationTokenManager", true)
                .schedule(new TimerTask() {
                              @Override
                              public void run() {
                                  removeTimeOutToken();
                              }
                          },
                        halfTimeOut,
                        halfTimeOut);
    }

    /**
     * 创建token信息，并返回tokenId
     *
     * @param tokenData token的关联数据
     * @return
     */
    public static String createToken(Object tokenData) {
        return createToken(tokenData, Const.MIN_SECONDS_OF_TOKEN_TIME_OUT);
    }

    /**
     * 通过tokenId获取本地缓存的token的数据
     *
     * @param tokenId
     * @return
     */
    public static AuthorizationToken getToken(String tokenId) {
        return tokenCache.get(tokenId);
    }

    /**
     * 创建token信息，并设置token的过期时间，如果过期时间小于最小默认过期时间，使用最小默认过期时间。
     *
     * @param tokenData        token的关联数据
     * @param secondsOfTimeOut token的过期时间
     */
    public static String createToken(Object tokenData, int secondsOfTimeOut) {
        if (secondsOfTimeOut < Const.MIN_SECONDS_OF_TOKEN_TIME_OUT) {
            secondsOfTimeOut = Const.MIN_SECONDS_OF_TOKEN_TIME_OUT;
        }
        AuthorizationTokenManager.secondsOfTimeOut = secondsOfTimeOut;

        String tokenId = null;
        AuthorizationToken token = null;
        int safeCounter = 8;
        do {
            if (safeCounter-- == 0) {
                throw new RuntimeException("Can not create tokenId.");
            }
            tokenId = IdUtil.fastSimpleUUID();
            token = new AuthorizationToken(tokenId, tokenData, System.currentTimeMillis() + getMinTimeout());
        } while (tokenCache.contains(token));

        tokenCache.put(token);

        return tokenId;
    }

    /**
     * 验证授权token是否存在，以便判断是否登录
     *
     * @param tokenId token的唯一标识
     * @return 存在返回true，不存在返回false
     */
    public static boolean validateToken(String tokenId) {
        AuthorizationToken token = new AuthorizationToken(tokenId);
        if (tokenCache.contains(token)) {
            token = getToken(tokenId);
            // 当过期时间不足最小过期时间的一半时，重置刷新过期时间
            if (token.getExpirationTime() <= (System.currentTimeMillis() - getMinTimeout())) {
                refresh(token);
            }
            return true;
        } else {
            return false;
        }
    }

    private static long getMinTimeout() {
        return secondsOfTimeOut * 1000L;
    }

    /**
     * 根据token的过期时间，移除所有过期的token
     */
    private static void removeTimeOutToken() {
        List<AuthorizationToken> tokenInCache = tokenCache.getAll();
        if (tokenInCache == null) {
            return;
        }

        List<AuthorizationToken> timeOutTokens = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        // find and save all time out tokens
        for (AuthorizationToken token : tokenInCache) {
            if (token.getExpirationTime() <= currentTime) {
                timeOutTokens.add(token);
            }
        }

        // remove all time out tokens
        for (AuthorizationToken token : timeOutTokens) {
            tokenCache.remove(token);
        }
    }

    /**
     * 刷新token数据，重新设置token信息
     *
     * @param token
     */
    public static void refresh(AuthorizationToken token) {
        token.setExpirationTime(System.currentTimeMillis() + getMinTimeout());
        tokenCache.remove(token);
        tokenCache.put(token);
    }

    /**
     * 移除token信息
     * @param token
     */
    public static void remove(AuthorizationToken token) {
        tokenCache.remove(token);
    }
}
