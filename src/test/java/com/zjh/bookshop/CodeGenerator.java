package com.zjh.bookshop;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

public class CodeGenerator {

	public static void main(String[] args) {
		//创建代码生成器
		AutoGenerator mpg = new AutoGenerator();

		//全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(projectPath+"/src/main/java");
		gc.setAuthor("zjh");
		gc.setOpen(false);//生成后是否打开资源管理器
		gc.setFileOverride(false);//重新生成时文件是否覆盖
		gc.setServiceName("%sService");//去掉Service接口的首字母I
		gc.setIdType(IdType.ID_WORKER);//主键策略
		gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
		gc.setSwagger2(true);//开启Swagger2模式

		mpg.setGlobalConfig(gc);

		//数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl("jdbc:mysql://localhost:3306/bookshop?autoReconnect=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8");
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("123456");
		dsc.setDbType(DbType.MYSQL);

		mpg.setDataSource(dsc);

		//包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName("bookshop");//模块名
		pc.setParent("com.zjh");
		pc.setController("controller");//controller.api
		pc.setEntity("entity");
		pc.setService("service");
		pc.setMapper("mapper");

		mpg.setPackageInfo(pc);

		//策略配置
		StrategyConfig strategy = new StrategyConfig();
		//"shs_front_chat","shs_front_consignee","shs_front_forum","shs_front_order","shs_front_order_goods","shs_front_replay","shs_front_require","shs_front_user","shs_front_user_cart"
		// "shs_backend_attribute","shs_backend_category","shs_backend_goods","shs_backend_goods_attr","shs_backend_goods_pics","shs_backend_manager","shs_backend_permission","shs_backend_report_1","shs_backend_report_2","shs_backend_report_3","shs_backend_role","shs_backend_role_manager","shs_backend_role_permission"
		strategy.setInclude("recommend");//设置需要映射的表名
		strategy.setNaming(NamingStrategy.underline_to_camel);//下划线转驼峰命名
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setEntityLombokModel(true);//lombok 模型 @Accessors(chain = true) setter链式操作
		strategy.setLogicDeleteFieldName("deleted");//逻辑删除字段名
		strategy.setEntityBooleanColumnRemoveIsPrefix(true);//去掉布尔值的is_前缀

		//自动填充
		TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
		TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
		ArrayList<TableFill> tableFills = new ArrayList<>();
		tableFills.add(createTime);
		tableFills.add(updateTime);
		strategy.setTableFillList(tableFills);

		strategy.setVersionFieldName("version");//乐观锁
		strategy.setRestControllerStyle(true);//restful api风格控制器
		strategy.setControllerMappingHyphenStyle(true);//url中驼峰转连字符：localhost:8080/hello_id_2

		mpg.setStrategy(strategy);

		//执行
		mpg.execute();
	}
}
