package com.zjh.bookshop.service;

import com.zjh.bookshop.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.bookshop.vo.CartVo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-03-31
 */
public interface CartService extends IService<Cart> {

	/**
	 * 添加到购物车
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	Boolean addCart(Integer userId, Integer goodsId);

	/**
	 * 获取购物车
	 * @param userId
	 * @return
	 */
	CartVo getCart(Integer userId);

	/**
	 * 修改购物车
	 * @param userId
	 * @param goodsId
	 * @param param
	 * @return
	 */
	CartVo updateCart(Integer userId, Integer goodsId, Map<String, Object> param);

	/**
	 * 删除购物车中商品
	 * @param goodsId
	 * @return
	 */
	CartVo deleteGoods(Integer goodsId, Integer userId);
}
