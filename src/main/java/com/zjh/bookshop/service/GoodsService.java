package com.zjh.bookshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.bookshop.entity.Goods;
import com.zjh.bookshop.form.GoodsAddForm;
import com.zjh.bookshop.vo.GoodsInfoVo;
import com.zjh.bookshop.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-03-24
 */
public interface GoodsService extends IService<Goods> {

	Page<GoodsVo> goodsDetail(int page, int limit, String query);

	/**
	 * 分页查询
	 * @param page：当前页数
	 * @param limit：每页条目数
	 * @param query：查询参数
	 * @return
	 */
	Page<Goods> pageQuery(int page, int limit, String query);

	/**
	 * 添加商品
	 * @param form：商品信息
	 * @return
	 */
	boolean saveGoods(GoodsAddForm form);

	/**
	 * 修改商品
	 * @param form：商品信息
	 * @return
	 */
	boolean updateGoods(int goodsId,int goodsDetailId,GoodsAddForm form);

	/**
	 * 获取商品信息
	 * @param id：商品id
	 * @return
	 */
	GoodsInfoVo getInfoById(Integer id);

	Page<Goods> getGoodsByCat(int page, int limit, int cat);
}
