package com.zjh.bookshop.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//狂神的Spring两步骤
//1、找对象
//2、放到spring中待用
@Configuration
public class ElasticSearchClientConfig {

	//spring <bean id="restHighLevelClient" class="RestHighLevelClient">
	@Bean
	public RestHighLevelClient restHighLevelClient(){
		RestHighLevelClient client = new RestHighLevelClient(
						RestClient.builder(
										new HttpHost("192.168.73.132", 9200, "http")));
		return client;
	}
}
