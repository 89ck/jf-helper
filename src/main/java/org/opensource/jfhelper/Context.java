package org.opensource.jfhelper;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import org.opensource.jfhelper.props.JFinalProp;
import org.opensource.jfhelper.utils.YmlKit;

/**
 * 全局缓存的数据信息
 * @author liyingfu
 */
public final class Context {

    /**
     * JFinal 配置
     */
    private static JFinalProp configProp;

    public static JFinalProp getConfig() {
        return configProp;
    }

    public static void setConfig(JFinalProp configProp) {
        Context.configProp = configProp;
    }

    /**
     * 获取指定的配置文件中的数据
     * @param clazz      数据类型
     * @param prefix     配置文件中的前缀 比如 jFinal.database.driverClass
     * @return 返回的数据
     */
    public static <T> T getObject(Class<T> clazz, String prefix) {
        return YmlKit.toBean(clazz, prefix);
    }



    public static DruidPlugin newDruidPlugin(){
        return new JfHelper(){
            @Override
            public void addMappingKit(ActiveRecordPlugin arp) {}
        }.createDruidPlugin();
    }




}
