package com.zjh.bookshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zjh
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GoodsDetail对象", description="")
public class GoodsDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @ApiModelProperty(value = "国际标准书号")
    @TableField("isbn")
    private String isbn;

    @ApiModelProperty(value = "翻译者")
    private String translator;

    @ApiModelProperty(value = "简介")
    private String introduction;

    @ApiModelProperty(value = "页数")
    private Integer pages;

    @ApiModelProperty(value = "开本")
    private Integer format;

    @ApiModelProperty(value = "装帧")
    private String binding;

    @ApiModelProperty(value = "出版社id")
    private Integer publisherId;

    @ApiModelProperty(value = "版次")
    private Integer edition;

    @ApiModelProperty(value = "印次")
    private Integer impression;


}
