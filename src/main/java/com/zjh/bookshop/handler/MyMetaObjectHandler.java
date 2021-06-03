package com.zjh.bookshop.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
	//private static final Logger LOGGER = LoggerFactory.getLogger(MyMetaObjectHandler.class);
//插入时的填充策略
	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill ....");
		this.setFieldValByName("createTime", new Date(), metaObject);//字段名，要填充的值
		this.setFieldValByName("updateTime", new Date(), metaObject);//版本号3.0.6以及之前的版本

	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update fill ....");
		this.setFieldValByName("updateTime", new Date(), metaObject);
	}
}
