package com.zjh.bookshop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("购物车VO")
@Data
public class CartGoodsVo {
	@ApiModelProperty("用户ID")
	private Integer userId;
	@ApiModelProperty("商品ID")
	private Integer goodsId;
	@ApiModelProperty("是否选中")
	private Boolean goodsSelected;
	@ApiModelProperty("商品图")
	private String smallPic;
	@ApiModelProperty("商品名称")
	private String goodsName;
	@ApiModelProperty("作者")
	private String author;
	@ApiModelProperty("图书原价")
	private BigDecimal originalPrice;
	@ApiModelProperty("商品价格")
	private BigDecimal goodsPrice;
	@ApiModelProperty("商品加入购物车的数量")
	private Integer goodsNumber;
	@ApiModelProperty("购物车商品总价格")
	private BigDecimal goodsTotalPrice;
	@ApiModelProperty("商品库存")
	private Integer goodsStock;
}
