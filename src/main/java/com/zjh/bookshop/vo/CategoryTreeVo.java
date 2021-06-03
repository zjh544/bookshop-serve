package com.zjh.bookshop.vo;

import com.zjh.bookshop.entity.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "CategoryTreeVo对象", description = "商品分类树")
public class CategoryTreeVo extends Category {

	@ApiModelProperty("子商品分类树")
	List<CategoryTreeVo> children = new ArrayList<>();
}
