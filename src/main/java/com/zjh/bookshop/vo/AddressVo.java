package com.zjh.bookshop.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("地址VO")
@Data
public class AddressVo {

	@ApiModelProperty(value = "id")
	private Integer id;

	@ApiModelProperty(value = "姓名")
	private String name;

	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "省市区")
	private String[] area;

	@ApiModelProperty(value = "详细地址")
	private String detail;
}
