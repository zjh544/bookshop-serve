package com.zjh.bookshop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt配置信息
 */
@ConfigurationProperties(prefix = "audience")
@Component
@Data
public class JwtConfig {

	private String header;
	private String tokenHead;
	private String clientId;
	private String base64secret;
	private String name;
	private long expiresSecond;//过期时间
	private String[] whiteList;//白名单?
	private long refreshExpiresSecond;//后端技术调整后token过期非常短，短到10分钟就过期了，所以在登录之后会给前端一个refreshToken字段同AccessToken一并传过来。token失效后利用refreshToken去延长用户的登录信息
}