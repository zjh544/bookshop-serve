package com.zjh.bookshop;

import com.alibaba.fastjson.JSON;
import com.zjh.bookshop.entity.Goods;
import com.zjh.bookshop.service.GoodsService;
import com.zjh.bookshop.utils.EsUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class EsUtilTest {

	@Autowired
	private EsUtil esUtil;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	//创建索引
	@Test
	void createIndex() throws IOException {
		boolean b = esUtil.createIndex("bookshop_goods");
		System.out.println(b);
	}

	//批量添加数据
	@Test
	void addData() throws IOException {
		List<Goods> goodsList = goodsService.list(null);

		BulkRequest bulkRequest = new BulkRequest();

		for (int i = 0; i < goodsList.size(); i++) {
			bulkRequest.add(new IndexRequest("bookshop_goods").source(JSON.toJSONString(goodsList.get(i)), XContentType.JSON));
		}

		BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		System.out.println(bulkResponse.hasFailures());
	}

	//分页查询
	@Test
	void searchListData() throws IOException {
		List<Map<String, Object>> list = esUtil.searchListData("bookshop_goods", new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()), 5, 0, "", "", "");
		System.out.println(list);
	}
}
