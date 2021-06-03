package com.zjh.bookshop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjh.bookshop.entity.Cart;
import com.zjh.bookshop.entity.Goods;
import com.zjh.bookshop.mapper.CartMapper;
import com.zjh.bookshop.mapper.GoodsMapper;
import com.zjh.bookshop.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.vo.CartGoodsVo;
import com.zjh.bookshop.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-03-31
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public Boolean addCart(Integer userId, Integer goodsId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",userId);
		Cart one = this.getOne(queryWrapper);
		List<CartGoodsVo> cartGoodsVoList = new ArrayList<>();
		boolean success;
		if (one != null) {//有购物车
			String cartInfo = one.getInfo();
			if (StringUtils.isNotBlank(cartInfo)) {//购物车中有商品
				cartGoodsVoList=JSONObject.parseArray(cartInfo,CartGoodsVo.class);
				boolean flag = false;
				for (CartGoodsVo vo:cartGoodsVoList){
					if (vo.getGoodsId().equals(goodsId)) {//同一种商品
						flag = true;
						vo.setGoodsNumber(vo.getGoodsNumber()+1);
						vo.setGoodsSelected(true);
						vo.setGoodsTotalPrice(vo.getGoodsTotalPrice().add(vo.getGoodsPrice()));
					}
				}
				if (!flag) {//新加入商品
					CartGoodsVo cartGoodsVo = createVo(userId, goodsId);
					cartGoodsVoList.add(cartGoodsVo);
				}
			}else {//购物车中没有商品
				CartGoodsVo cartGoodsVo = createVo(userId, goodsId);
				cartGoodsVoList.add(cartGoodsVo);
			}
			String info = JSONObject.toJSONString(cartGoodsVoList);
			one.setInfo(info);
			success = this.saveOrUpdate(one);
		}else {//没有购物车
			Cart cart = new Cart();
			cart.setUserId(userId);
			CartGoodsVo cartGoodsVo = createVo(userId, goodsId);
			cartGoodsVoList.add(cartGoodsVo);
			String info = JSONObject.toJSONString(cartGoodsVoList);//?
			cart.setInfo(info);
			success = this.save(cart);
		}
		return success;
	}

	private CartGoodsVo createVo(Integer userId, Integer goodsId){
		Goods goods = goodsMapper.selectById(goodsId);
		CartGoodsVo cartGoodsVo = new CartGoodsVo();
		cartGoodsVo.setUserId(userId);
		cartGoodsVo.setGoodsId(goodsId);
		cartGoodsVo.setGoodsSelected(true);
		cartGoodsVo.setSmallPic(goods.getPic());
		cartGoodsVo.setGoodsName(goods.getName());
		cartGoodsVo.setAuthor(goods.getAuthor());
		if (goods.getOriginalPrice() != null) {
			cartGoodsVo.setOriginalPrice(goods.getOriginalPrice());
		}
		cartGoodsVo.setGoodsPrice(goods.getPrice());
		cartGoodsVo.setGoodsNumber(1);
		cartGoodsVo.setGoodsTotalPrice(goods.getPrice());
		cartGoodsVo.setGoodsStock(goods.getStock());
		return cartGoodsVo;
	}

	private void fillDate(CartVo cartVo, List<CartGoodsVo> cartGoodsVoList) {
		Integer total = 0;
		BigDecimal totalPrice = new BigDecimal(0.00);
		Boolean selectAll = true;
		for (CartGoodsVo cartGoodsVo:cartGoodsVoList ) {
			total+=cartGoodsVo.getGoodsNumber();
			if (!cartGoodsVo.getGoodsSelected()) {
				selectAll = false;
			}else{
				totalPrice = totalPrice.add(cartGoodsVo.getGoodsTotalPrice());
			}
		}
		cartVo.setCartTotalPrice(totalPrice);
		cartVo.setSelectedAll(selectAll);
		cartVo.setCartTotalNumber(total);
	}

	@Override
	public CartVo getCart(Integer userId) {
		CartVo vo = new CartVo();
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		Cart one = this.getOne(queryWrapper);
		String cartInfo = one.getInfo();
		List<CartGoodsVo> cartGoodsVoList = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
		fillDate(vo, cartGoodsVoList);
		vo.setCartGoodsVoList(cartGoodsVoList);
		return vo;
	}

	@Override
	public CartVo updateCart(Integer userId, Integer goodsId, Map<String, Object> param) {
		int number = Integer.parseInt(param.get("number").toString());
		Boolean selected = Boolean.parseBoolean(param.get("selected").toString());
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		Cart one = this.getOne(queryWrapper);
		String cartInfo = one.getInfo();
		List<CartGoodsVo> cartGoodsVoList = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
		cartGoodsVoList.forEach(item -> {
			if (item.getGoodsId().intValue() == goodsId.intValue()) {
				item.setGoodsTotalPrice(item.getGoodsPrice().multiply(BigDecimal.valueOf(number)));
				item.setGoodsNumber(number);
				item.setGoodsSelected(selected);
			}
		});
		cartInfo = JSONObject.toJSONString(cartGoodsVoList);
		one.setInfo(cartInfo);
		this.saveOrUpdate(one);
		CartVo vo = new CartVo();
		fillDate(vo, cartGoodsVoList);
		vo.setCartGoodsVoList(cartGoodsVoList);
		return vo;
	}

	@Override
	public CartVo deleteGoods(Integer goodsId, Integer userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		Cart one = this.getOne(queryWrapper);
		String cartInfo = one.getInfo();
		List<CartGoodsVo> cartGoodsVoList = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
		for (int i = 0; i < cartGoodsVoList.size(); i++) {
			if (cartGoodsVoList.get(i).getGoodsId().equals(goodsId)) {
				cartGoodsVoList.remove(i);
				break;
			}
		}
		cartInfo = JSONObject.toJSONString(cartGoodsVoList);
		one.setInfo(cartInfo);
		this.saveOrUpdate(one);
		CartVo vo = new CartVo();
		fillDate(vo, cartGoodsVoList);
		vo.setCartGoodsVoList(cartGoodsVoList);
		return vo;
	}

//	@Override
//	public CartVo addCart(Integer userId, Map<String, Object> param) {
//		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("user_id", userId);
//		Cart one = this.getOne(queryWrapper);
//		Integer goodsId = Integer.parseInt(param.get("goodsId").toString());
//		List<CartGoodsVo> cartGoodsVoList = new ArrayList<>();
//		if (one!=null) { // 用户有购物车
//			String cartInfo = one.getInfo();
//			if (!StringUtils.isBlank(cartInfo)) { // 有购物车有商品
//				cartGoodsVoList = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
//				boolean flag = false;
//				for (CartGoodsVo vo: cartGoodsVoList) {
//					if(vo.getGoodsId().intValue() == goodsId.intValue()) {
//						flag = true;
//						vo.setGoodsNumber(vo.getGoodsNumber() + 1);
//						vo.setGoodsSelected(true);
//						vo.setGoodsTotalPrice(vo.getGoodsTotalPrice().add(vo.getGoodsPrice()));//？add()
//					}
//				}
//
//				if(!flag) { // 购物车没有要添加的商品
//					CartGoodsVo vo1 = createVo(userId,goodsId, cartGoodsVoList.size() + 1);
//					cartGoodsVoList.add(vo1);
//				}
//				cartInfo = JSONObject.toJSONString(cartGoodsVoList);
//			} else {// 有购物车没商品
//				cartGoodsVoList = new ArrayList<>();
//				CartGoodsVo vo1 = createVo(userId, goodsId, 1);
//				cartGoodsVoList.add(vo1);
//				cartInfo = JSONObject.toJSONString(cartGoodsVoList);
//			}
//			one.setInfo(cartInfo);
//			this.saveOrUpdate(one);
//		} else {//没购物车
//			Cart cart = new Cart();
//			cart.setUserId(userId);
//			cartGoodsVoList = new ArrayList<>();
//			CartGoodsVo vo1 = createVo(userId, goodsId, 1);
//			cartGoodsVoList.add(vo1);
//			String cartInfo = JSONObject.toJSONString(cartGoodsVoList);
//			cart.setInfo(cartInfo);
//			this.save(cart);
//		}
//		CartVo vo = new CartVo();
//		vo.setCartGoodsVoList(cartGoodsVoList);
//		fillDate(vo, cartGoodsVoList);
//		return vo;
//	}
//
//	@Override
//	public CartVo getCart(Integer id) {
//		CartVo vo = new CartVo();
//		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("user_id", id);
//		Cart one = this.getOne(queryWrapper);
//		String cartInfo = one.getInfo();
//		List<CartGoodsVo> cartGoodsVoList = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
//		fillDate(vo, cartGoodsVoList);
//		vo.setCartGoodsVoList(cartGoodsVoList);
//		return vo;
//	}
//
//	@Override
//	public CartVo updateCart(Integer userId, Integer goodsId, Map<String, Object> param) {
//		Integer number = Integer.parseInt(param.get("number").toString());
//		Boolean selected = Boolean.parseBoolean(param.get("selected").toString());
//		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("user_id", userId);
//		Cart one = this.getOne(queryWrapper);
//		String cartInfo = one.getInfo();
//		List<CartGoodsVo> cartGoodsVoList = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
//		cartGoodsVoList.forEach(item -> {
//			if (item.getGoodsId().intValue() == goodsId.intValue()) {
//				item.setGoodsNumber(number);
//				item.setGoodsSelected(selected);
//			}
//		});
//		cartInfo = JSONObject.toJSONString(cartGoodsVoList);
//		one.setInfo(cartInfo);
//		this.saveOrUpdate(one);
//		CartVo vo = new CartVo();
//		fillDate(vo, cartGoodsVoList);
//		vo.setCartGoodsVoList(cartGoodsVoList);
//		return vo;
//	}
//
//	@Override
//	public CartVo deleteGoods(Integer goodsId, Integer userId) {
//		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("user_id", userId);
//		Cart one = this.getOne(queryWrapper);
//		String cartInfo = one.getInfo();
//		List<CartGoodsVo> cartProductVos = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
//		List<CartGoodsVo> cartProductVos2 = new ArrayList<>();
//		cartProductVos.forEach(item -> {
//			if (item.getGoodsId().intValue() != goodsId) {
//				cartProductVos2.add(item);
//			}
//		});
//		cartInfo = JSONObject.toJSONString(cartProductVos2);
//		one.setInfo(cartInfo);
//		this.saveOrUpdate(one);
//		CartVo vo = new CartVo();
//		fillDate(vo, cartProductVos2);
//		vo.setCartGoodsVoList(cartProductVos2);
//		return vo;
//	}
//
//	private CartGoodsVo createVo(Integer userId, Integer goodsId, Integer id) {
//		CartGoodsVo vo1 = new CartGoodsVo();
//		Goods goods = goodsMapper.selectById(goodsId);
//		vo1.setId(id);
//		vo1.setUserId(userId);
//		vo1.setGoodsId(goodsId);
//		vo1.setGoodsNumber(1);
//		vo1.setGoodsName(goods.getName());
//		vo1.setSmallPic(goods.getSmallPic());
//		vo1.setGoodsPrice(goods.getPrice());
//		vo1.setGoodsTotalPrice(goods.getPrice());
//		vo1.setGoodsStock(goods.getStock());
//		vo1.setGoodsSelected(true);
//		return vo1;
//	}
//
//	private void fillDate(CartVo vo, List<CartGoodsVo> cartGoodsVoList) {
//		Integer total = 0;
//		BigDecimal totalPrice = new BigDecimal(0.00);
//		Boolean selectAll = true;
//		for (CartGoodsVo vo1:cartGoodsVoList ) {
//			total+=vo1.getGoodsNumber();
//			if (!vo1.getGoodsSelected()) {
//				selectAll = false;
//			}
//			totalPrice = totalPrice.add(vo1.getGoodsTotalPrice());
//		}
//		vo.setCartTotalPrice(totalPrice);
//		vo.setSelectedAll(selectAll);
//		vo.setCartTotalNumber(total);
//	}
}
