package com.zjh.bookshop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("商品详情页面")
@Data
public class GoodsInfoVo extends GoodsVo {

	@ApiModelProperty("内容图片")
	List<String> urls;
}
