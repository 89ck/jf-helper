package org.opensource.jfhelper.utils;

import java.util.*;

import org.opensource.jfhelper.annocation.JFinalProperties;
import org.opensource.jfhelper.exception.ValidException;
import org.opensource.jfhelper.props.JFinalProp;
import org.yaml.snakeyaml.Yaml;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 解析YML文件的工具列, 主要用于解析配置文件中的数据 <br/>
 * 不支持列表的bean对象配置.
 *
 * @author seiya
 */
public class YmlKit {

    private static Map<String, Object> cfgMap;

    private final static char[] CAME_CASE_KEYS = {'-', '_'};

    public static void loadFile(String filePath) {
        Yaml yaml = new Yaml();
        if (FileUtil.isFile(filePath)) {
            cfgMap = toCameCase(yaml.load(FileUtil.getInputStream(filePath)));
        } else {
            throw new ValidException("The file in the current path does not exist !");
        }
    }

    /**
     * 递归循环map的key,并把key转成驼峰命名的key
     * @param map
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, Object> toCameCase(Map<String, Object> map) {
        Map<String, Object> tempMap = new HashMap<>(10);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = toCameCase((Map) value);
            }
            if (value instanceof List){
                List<Map<String, Object>> list = new ArrayList<>();
                for (Map<String, Object> item : new ArrayList<>((List<Map<String, Object>>) value)) {
                    list.add(toCameCase(item));
                }
                value = list;
            }
            tempMap.put(field2CameCase(entry.getKey()), value);
        }
        return tempMap;
    }

    /**
     * 非驼峰的字符串转驼峰, 比如 dev-mode, dev_mode 转换为 devMode
     * @param str
     * @return
     */
    private static String field2CameCase(String str) {
        for (char cameCaseKey : CAME_CASE_KEYS) {
            str = StrUtil.toCamelCase(str, cameCaseKey);
        }
        return str;
    }

    /**
     * 将YML文件转换为Bean, 默认使用的前缀是 jFinal
     *
     * @param clazz 用于转换的实体类。
     * @return 转换后的实体类
     */
    public static <T> T toBean(Class<T> clazz) {
        if (CollUtil.isEmpty(cfgMap)) {
            throw new ValidException("please execute loadFile first !");
        }
        return BeanUtil.toBean(cfgMap.get(Const.jFinal), clazz);
    }

    /**
     * 根据前前缀的值，切分前缀
     *
     * @param prefix 前缀字符串
     * @return 按照包的.切分数据
     */
    private static List<String> splitPrefixStr(String prefix) {
        if (StrUtil.isNotBlank(prefix)) {
            return StrUtil.split(prefix, ".");
        } else {
            return null;
        }
    }

    /**
     * 将YML文件转换为Bean
     *
     * @param clazz  转换的实体类
     * @param prefix 解析中使用到的数据的前缀
     * @return 返回的实体类
     */

    @SuppressWarnings("unchecked")
	public static <T> T toBean(Class<T> clazz, String prefix) {
        if (CollUtil.isEmpty(cfgMap)) {
            throw new ValidException("please execute loadFile first !");
        }
        List<String> prefixes = splitPrefixStr(prefix);
        if (CollUtil.isNotEmpty(prefixes)) {
            Map<String, Object> tempMap = new HashMap<>(cfgMap);
            int index = 1;
            for (String str : prefixes) {
                Object obj = tempMap.get(str);
                if (obj instanceof Map) {
                    tempMap = (Map<String, Object>) obj;
                    if (index == prefixes.size()) {
                        break;
                    }
                }
            }
            return BeanUtil.toBean(tempMap, clazz);
        } else {
            return toBean(clazz);
        }
    }

    /**
     * 获取配置对象，只有配置对象使用@ConfigurationProperties注解的类才能获取到配置对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getObject(Class<T> clazz) {
        JFinalProperties config = clazz.getAnnotation(JFinalProperties.class);
        String prefix = config.prefix();
        return toBean(clazz, prefix);
    }


    public static void main(String[] args) {
        YmlKit.loadFile("classpath:application.yml");
        JFinalProp jFinalProp = toBean(JFinalProp.class);
        System.out.println(jFinalProp);
    }
}
