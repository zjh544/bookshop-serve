package com.zjh.bookshop.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("商品添加表单对象")
@Data
public class GoodsAddForm {

	@ApiModelProperty("商品名称")
	@NotNull(message = "商品名称不能为空")
	private String name;

	@ApiModelProperty("分类")
	@NotEmpty(message = "商品分类不能为空")
	private Integer[] catIds;

	@ApiModelProperty("作者")
	private String author;

	@ApiModelProperty("原价")
	@DecimalMin("0")//必须是数字，最小值为0
	private BigDecimal originalPrice;

	@ApiModelProperty("价格")
	@DecimalMin("0")//必须是数字，最小值为0
	private BigDecimal price;

	@ApiModelProperty("库存")
	@Min(0)
	private Integer stock;

	@ApiModelProperty("商品图片")
	@NotEmpty(message = "商品图片不能为空")
	private String goodsPic;

	@ApiModelProperty("出版社id")
	private Integer publisherId;

	@ApiModelProperty("国际标准书号")
	private String isbn;

	@ApiModelProperty("翻译者")
	private String translator;

	@ApiModelProperty("简介")
	private String introduction;

	@ApiModelProperty("页数")
	@Min(0)
	private Integer pages;

	@ApiModelProperty("开本")
	private Integer format;

	@ApiModelProperty("装帧")
	private String binding;

	@ApiModelProperty("版次")
	private Integer edition;

	@ApiModelProperty("印次")
	private Integer impression;

	@ApiModelProperty("内容图片")
	private String[] contentPic;

}
