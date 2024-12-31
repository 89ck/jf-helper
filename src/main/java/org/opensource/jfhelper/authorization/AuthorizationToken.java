package org.opensource.jfhelper.authorization;

import java.io.Serializable;
import java.util.Objects;

/**
 * 授权token的信息
 *
 * @author seiya
 */
@SuppressWarnings("serial")
public class AuthorizationToken implements Serializable {

    /**
     * token的标识主键
     */
    private final String id;

    /**
     * token存储的数据
     */
    private Object data;

    /**
     * token的过期时间
     */
    private long expirationTime;


    public AuthorizationToken(String id, Object data, long expirationTime) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        this.expirationTime = expirationTime;
        this.id = id;
        this.data = data;
    }

    public AuthorizationToken(String id, Object data) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }
        this.id = id;
        this.data = data;
    }

    public AuthorizationToken(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }
        this.id = id;
    }

    /**
     * 唯一token的标识主键
     */
    public String getId() {
        return id;
    }

    /**
     * token的过期时间
     *
     * @return
     */
    public long getExpirationTime() {
        return expirationTime;
    }

    /**
     * 重置token的过期时间
     *
     * @param expirationTime
     */
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * token存储的数据
     *
     * @return
     */
    public Object getData() {
        return data;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizationToken that = (AuthorizationToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
