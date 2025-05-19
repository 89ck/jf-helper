package org.opensource.jfhelper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.jfinal.aop.AopManager;
import com.jfinal.config.*;
import com.jfinal.ext.cors.CORSInterceptor;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import org.opensource.jfhelper.authorization.AuthorizationTokenCache;
import org.opensource.jfhelper.authorization.AuthorizationTokenManager;
import org.opensource.jfhelper.authorization.DefaultAuthorizationTokenCache;
import org.opensource.jfhelper.core.JfAopFactory;
import org.opensource.jfhelper.interceptor.RestInterceptor;
import org.opensource.jfhelper.interceptor.StaticFileHandler;
import org.opensource.jfhelper.interceptor.ValidInterceptor;
import org.opensource.jfhelper.props.JFinalProp;
import org.opensource.jfhelper.props.RedisProp;
import org.opensource.jfhelper.utils.Const;
import org.opensource.jfhelper.utils.YmlKit;

import java.util.List;
import java.util.Objects;

/**
 * 默认实现jfinal的config，初始化yml格式的配置文件
 *
 * @author seiya
 */
public abstract class JfHelper extends JFinalConfig {

    private static JFinalProp config;

    private static final Log logger = Log.getLog(JfHelper.class);

    /**
     * 设置默认的授权token缓存实现类
     */
    public AuthorizationTokenCache getAuthorizationTokenCache() {
        return new DefaultAuthorizationTokenCache();
    }

    /**
     * 设置默认的配置文件路径， 默认使用 classpath:application.yml
     */
    public String getCfgFilePath() {
        Prop prop;
        try {
            prop = PropKit.use(".env");
        } catch (Exception e) {
            logger.error("未找到.env文件，默认使用application.yml配置文件");
            prop = new Prop();
        }
        String env = "env";
        if (StrUtil.isBlank(prop.get(env))) {
            return Const.CONFIG_PATH;
        } else {
            return "classpath:application-" + prop.get(env) + ".yml";
        }
    }

    /**
     * 启动应用后执行的第一个方法
     */
    @Override
    public void configConstant(Constants constants) {
        logger.debug("init constants");
        initConfigurationProperties();
        constants.setEncoding(config.getEncoding());
        constants.setDevMode(config.getDevMode());
        // 判断，是否使用自定义的 aopFactory 注入注解
        if (!config.getUseInject()) {
            AopManager.me().setAopFactory(new JfAopFactory(this.getClass().getPackage().getName()));
        }
        constants.setInjectDependency(config.getInject());
        constants.setInjectSuperClass(config.getInject());
        constants.setJsonFactory(config.getJsonLib());
        constants.setResolveJsonRequest(config.getUseRest());
    }

    /**
     * 加载配置文件的属性数据
     */
    public void initConfigurationProperties() {
        // 1. 加载yaml配置文件；
        YmlKit.loadFile(getCfgFilePath());
        config = YmlKit.getObject(JFinalProp.class);
        if (Objects.isNull(config)) {
            throw new RuntimeException("配置文件中无jFinal开头的配置信息");
        }
        Context.setConfig(config);
    }

    @Override
    public void configRoute(Routes routes) {
        logger.debug("init routes");
        // 扫描当前入口类下的所有的包下的controller包名
        routes.scan(this.getClass().getPackage().getName());
        // 加入自定义的路由
        if (CollUtil.isNotEmpty(config.getRoutes())) {
            for (Routes route : config.getRoutes()) {
                routes.add(route);
            }
        }
    }

    @Override
    public void configEngine(Engine engine) {
        logger.debug("init engine");
        engine.setToClassPathSourceFactory();
        engine.setBaseTemplatePath(null);
        engine.setCompressorOn();
        engine.setDevMode(config.getDevMode());
        engine.setEncoding(config.getEncoding());
    }

    /**
     * 追加jFinal自动生成的 _MappingKit.mapping(arp) 方法。
     *
     * @param arp
     */
    public abstract void addMappingKit(ActiveRecordPlugin arp);

    @Override
    public void configPlugin(Plugins plugins) {
        logger.debug("init plugins");
        if (Objects.nonNull(config.getDatabase())) {
            DruidPlugin druidPlugin = newDruidPlugin(plugins);
            ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
            addMappingKit(arp);
            // 加载数据库文件的配置路径
            arp.setShowSql(config.getDatabase().isShowSql());
            arp.getEngine().setCompressorOn();
            arp.addSqlTemplate(config.getDatabase().getSqlTemplatePath());
            arp.setDevMode(config.getDevMode());
            arp.setDialect(config.getDatabase().getDialect());
            plugins.add(arp);
        }

        // 加载Redis缓存的插件
        if (Objects.nonNull(config.getRedis())) {
            List<RedisProp> redisProps = config.getRedis();
            for (RedisProp redisProp : redisProps) {
                RedisPlugin redisPlugin = null;
                if (StrUtil.isBlank(redisProp.getClientName())) {
                    redisPlugin = new RedisPlugin(redisProp.getCacheName(), redisProp.getHost(), redisProp.getPort(), redisProp.getTimeout(), redisProp.getPassword(), redisProp.getDatabase());
                } else {
                    redisPlugin = new RedisPlugin(redisProp.getCacheName(), redisProp.getHost(), redisProp.getPort(), redisProp.getTimeout(), redisProp.getPassword(), redisProp.getDatabase(), redisProp.getClientName());
                }
                plugins.add(redisPlugin);
            }

        }


    }

    /**
     * 创建druid数据库连接池的持剑
     *
     * @param plugins
     * @return
     */
    private DruidPlugin newDruidPlugin(Plugins plugins) {
        Log4jFilter log4jFilter = getFilter();
        DruidPlugin druidPlugin = createDruidPlugin();
        druidPlugin.addFilter(log4jFilter);
        plugins.add(druidPlugin);
        return druidPlugin;
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        logger.debug("init interceptors");
        interceptors.add(new CORSInterceptor());
        interceptors.add(new RestInterceptor());
        interceptors.add(new ValidInterceptor());
    }

    public DruidPlugin createDruidPlugin() {
        if (Objects.isNull(config) || Objects.isNull(config.getDatabase())) {
            initConfigurationProperties();
        }
        return new DruidPlugin(config.getDatabase().getUrl(), config.getDatabase().getUsername(),
                config.getDatabase().getPassword(), config.getDatabase().getDriverClass());
    }

    @Override
    public void configHandler(Handlers me) {
        logger.debug("Handlers");
        me.add(new StaticFileHandler());
    }

    private Log4jFilter getFilter() {
        Log4jFilter filter = new Log4jFilter();
        filter.setStatementLogEnabled(config.getDatabase().isShowSql());
        filter.setStatementLogErrorEnabled(config.getDatabase().isShowSql());
        filter.setStatementExecutableSqlLogEnable(config.getDatabase().isShowSql());
        return filter;
    }

    @Override
    public void onStart() {
        super.onStart();
        initSdkConfiguration();
        AuthorizationTokenManager.init(this.getAuthorizationTokenCache());
    }

    /**
     * 加载三方的sdk的相关工具类。
     */
    private static void initSdkConfiguration() {
        if (Objects.nonNull(config.getWxMini())) {
            // 加载小程序的sdk工具包
            WxaConfig wxaConfig = new WxaConfig();
            wxaConfig.setAppId(config.getWxMini().getAppId());
            wxaConfig.setAppSecret(config.getWxMini().getAppSecret());
            WxaConfigKit.putWxaConfig(wxaConfig);
        }
        if (Objects.nonNull(config.getWechat())) {
            // 加载公众号的sdk工具包
            ApiConfig apiConfig = new ApiConfig();
            apiConfig.setAppId(config.getWechat().getAppId());
            apiConfig.setAppSecret(config.getWechat().getAppSecret());
            apiConfig.setToken(config.getWechat().getToken());
            apiConfig.setEncryptMessage(config.getWechat().isMessageEncrypt());
            apiConfig.setEncodingAesKey(config.getWechat().getEncodingAesKey());
            ApiConfigKit.putApiConfig(apiConfig);
        }
    }

}
