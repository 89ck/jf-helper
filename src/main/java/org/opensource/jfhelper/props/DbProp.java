package org.opensource.jfhelper.props;

import com.jfinal.plugin.activerecord.dialect.*;

import org.opensource.jfhelper.annocation.JFinalProperties;
import org.opensource.jfhelper.utils.Const;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 实现数据库的注解转换
 *
 * @author seiya
 */
@Component
@JFinalProperties(prefix = Const.DATABASE)
@ConfigurationProperties(prefix = Const.DATABASE)
public class DbProp {

    /**
     * 支持的数据库类型
     */
    public final static String MYSQL = "mysql", SQLSERVER = "sqlserver", ORACLE = "oracle", POSTGRESQL = "postgresql", SQLITE = "sqlite";


    /**
     * 数据库的烈性， 默认使用MySQL
     */
    private String type = MYSQL;

    /**
     * 数据库驱动
     */
    private String driverClass;

    /**
     * 数据库连接地址
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 是否打印SQL
     */
    private boolean showSql = false;

    /**
     * 加载sql模板的路径
     */
    private String sqlTemplatePath = "mapper/mapper.jfs";

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取数据库代理的规则， 默认使用Mysql数据库
     * @return
     */
    public Dialect getDialect() {
        switch (this.type) {
            case ORACLE:
                return new OracleDialect();
            case SQLSERVER:
                return new SqlServerDialect();
            case POSTGRESQL:
                return new PostgreSqlDialect();
            case SQLITE:
                return new Sqlite3Dialect();
            case MYSQL:
            default:
                return new MysqlDialect();
        }

    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public String getSqlTemplatePath() {
        return sqlTemplatePath;
    }

    public void setSqlTemplatePath(String sqlTemplatePath) {
        this.sqlTemplatePath = sqlTemplatePath;
    }

    @Override
    public String toString() {
        return "DataBaseConfiguration{" +
                "type='" + type + '\'' +
                ", driverClass='" + driverClass + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", showSql=" + showSql +
                ", sqlTemplatePath='" + sqlTemplatePath + '\'' +
                '}';
    }
}
