package com.zjh.bookshop.vo;

import com.zjh.bookshop.entity.Goods;
import com.zjh.bookshop.entity.GoodsDetail;
import com.zjh.bookshop.entity.Publisher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("管理商品Vo")
@Data
public class GoodsVo {

	@ApiModelProperty("商品")
	Goods goods;

	@ApiModelProperty("商品详情")
	GoodsDetail goodsDetail;

	@ApiModelProperty("出版社详情")
	Publisher publisher;

}
