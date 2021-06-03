package com.zjh.bookshop.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApiModel("购物车对象")
@Data
public class CartVo {

	List<CartGoodsVo> cartGoodsVoList = new ArrayList<>();
	Boolean selectedAll;
	BigDecimal cartTotalPrice;
	Integer cartTotalNumber;
}
