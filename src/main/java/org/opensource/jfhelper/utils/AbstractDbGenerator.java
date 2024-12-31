package org.opensource.jfhelper.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import javax.sql.DataSource;

import org.opensource.jfhelper.Context;

import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.TypeMapping;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法 详见 JFinal 俱乐部:
 * https://jfinal.com/club
 * 
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 * <br/>
 * 使用时只需要继承DbGenerator后，在main方法中调用 generate方法即可
 * 
 * @author seiya
 */
public abstract class AbstractDbGenerator {

	/**
	 * 获取默认的model的文件包的信息，比如 org.opensource.helper.model
	 * 
	 * @return
	 */
	public abstract String getModelPackageName();
	
	/**
	 * 获取需要移除的表前缀，默认没有
	 * @return
	 */
	public String[] getRemovedTableNamePrefixes() {
		return null;
	};
	
	/**
	 * 添加到黑名单中的表名。 默认没有
	 * @return
	 */
	public String[] getAddBlackTableNames() {
		return null;
	}

	public DataSource getDataSource() {
		DruidPlugin druidPlugin = Context.newDruidPlugin();
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public void generate() {
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = getModelPackageName();

		// base model 所使用的包名
		String baseModelPackageName = modelPackageName + ".base";

		// base model 文件保存路径
		String baseModelOutputDir = System.getProperty("user.dir") + "/src/main/java/"
				+ baseModelPackageName.replace('.', '/');

		System.out.println("输出路径：" + baseModelOutputDir);

		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";

		// 创建生成器
		Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName,
				modelOutputDir);

		// 配置是否生成备注
		generator.setGenerateRemarks(true);

		// 设置数据库方言
		generator.setDialect(Context.getConfig().getDatabase().getDialect());

		// 设置是否生成链式 setter 方法，强烈建议配置成 false，否则 fastjson 反序列化会跳过有返回值的 setter 方法
		generator.setGenerateChainSetter(false);

		// 添加不需要生成的表名到黑名单
		if (Objects.nonNull(getAddBlackTableNames())) {
			generator.addBlacklist();
		}

		// 设置是否在 Model 中生成 dao 对象
		generator.setGenerateDaoInModel(true);

		// 设置是否生成字典文件
		generator.setGenerateDataDictionary(false);

		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非
		// OscUser
		if(Objects.nonNull(getRemovedTableNamePrefixes())) {
			generator.setRemovedTableNamePrefixes(getRemovedTableNamePrefixes());
		}

		// 将 mysql 8 以及其它原因之下生成 jdk 8 日期类型映射为 java.util.Date，便于兼容老项目，也便于习惯使用
		// java.util.Date 的同学
		TypeMapping tm = new TypeMapping();
		tm.addMapping(LocalDateTime.class, Date.class);
		tm.addMapping(LocalDate.class, Date.class);
		// tm.addMapping(LocalTime.class, LocalTime.class); // LocalTime 暂时不变
		generator.setTypeMapping(tm);

		// 生成
		generator.generate();
	}

	public static void main(String[] args) {
		
	}
}
