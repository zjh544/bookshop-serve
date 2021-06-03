package com.zjh.bookshop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.entity.*;
import com.zjh.bookshop.mapper.OrderMapper;
import com.zjh.bookshop.service.*;
import com.zjh.bookshop.vo.AddressVo;
import com.zjh.bookshop.vo.CartGoodsVo;
import com.zjh.bookshop.vo.OrderVo;
import com.zjh.bookshop.vo.UserOrdersVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-04-06
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrdersService {

	@Autowired
	private CartService cartService;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private OrderGoodsService orderGoodsService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private ExpressService expressService;

	@Override
	public void createOrder(Integer addressId, String orderNo, Integer userId) {
		QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
		cartQueryWrapper.eq("user_id", userId);
		Cart cart = cartService.getOne(cartQueryWrapper);
		String cartInfo = cart.getInfo();
		List<CartGoodsVo> cartGoodsVoList = JSONObject.parseArray(cartInfo, CartGoodsVo.class);
		List<CartGoodsVo> selectedGoods = new ArrayList<>();
		List<CartGoodsVo> updateCart = new ArrayList<>();
		cartGoodsVoList.forEach(cartGoodsVo -> {
			if (cartGoodsVo.getGoodsSelected()) {
				selectedGoods.add(cartGoodsVo);
			}else {
				updateCart.add(cartGoodsVo);
			}
		});
		//更新购物车
		cartInfo = JSONObject.toJSONString(updateCart);
		cart.setInfo(cartInfo);

		List<OrderGoods> orderGoodsList = new ArrayList<>();
		List<Goods> goodsList = new ArrayList<>();
		BigDecimal total = new BigDecimal("0.00");
		for (CartGoodsVo cartGoodsVo:selectedGoods){
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderNo(orderNo);
			orderGoods.setGoodsId(cartGoodsVo.getGoodsId());
			orderGoods.setGoodsName(cartGoodsVo.getGoodsName());
			orderGoods.setGoodsPic(cartGoodsVo.getSmallPic());
			orderGoods.setGoodsNumber(cartGoodsVo.getGoodsNumber());
			orderGoods.setGoodsPrice(cartGoodsVo.getGoodsPrice());

			Goods goods = goodsService.getById(cartGoodsVo.getGoodsId());
			goods.setStock(goods.getStock()-cartGoodsVo.getGoodsNumber());

			goodsList.add(goods);
			orderGoodsList.add(orderGoods);
			total=total.add(cartGoodsVo.getGoodsTotalPrice());
		}
		Orders orders = new Orders();
		orders.setUserId(userId);
		orders.setAddressId(addressId);
		orders.setOrderNo(orderNo);
		orders.setPrice(total);

		this.save(orders);
		cartService.saveOrUpdate(cart);
		orderGoodsService.saveBatch(orderGoodsList);
		goodsService.saveOrUpdateBatch(goodsList);
	}

	@Override
	public OrderVo getVoByNo(String orderNo) {
		QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
		ordersQueryWrapper.eq("order_no",orderNo);
		Orders orders = this.getOne(ordersQueryWrapper);

		QueryWrapper<OrderGoods> orderGoodsQueryWrapper = new QueryWrapper<>();
		orderGoodsQueryWrapper.eq("order_no",orderNo);
		List<OrderGoods> orderGoodsList = orderGoodsService.list(orderGoodsQueryWrapper);

		Address address = addressService.getById(orders.getAddressId());
		AddressVo addressVo = new AddressVo();
		addressVo.setId(address.getId());
		addressVo.setName(address.getName());
		addressVo.setPhone(address.getPhone());
		addressVo.setArea(StringUtils.split(address.getArea(),", "));
		addressVo.setDetail(address.getDetail());

		Express express = new Express();
		if (orders.getExpressId() != null) {
			express = expressService.getById(orders.getExpressId());
		}

		OrderVo orderVo = new OrderVo();
		orderVo.setOrders(orders);
		orderVo.setOrderGoodsList(orderGoodsList);
		orderVo.setAddressVo(addressVo);
		orderVo.setExpress(express);
		return orderVo;
	}

	@Override
	public Page<OrderVo> pageQuery(int page, int limit, String query) {
		Page<Orders> ordersPage = new Page<>();
		ordersPage.setCurrent(page);
		ordersPage.setSize(limit);
		QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
		ordersQueryWrapper.orderByDesc("create_time");
		if(!StringUtils.isEmpty(query)){
			ordersQueryWrapper.like("order_no",query);
		}
		List<Orders> ordersList = this.page(ordersPage, ordersQueryWrapper).getRecords();
		List<OrderVo> orderVos = new ArrayList<>();
		ordersList.forEach(orders -> {
			OrderVo orderVo = getVoByNo(orders.getOrderNo());
			orderVos.add(orderVo);
		});
		Page<OrderVo> pageResult = new Page<>();
		pageResult.setRecords(orderVos);
		pageResult.setTotal(ordersList.size());
		return pageResult;
	}

	@Override
	public Boolean addDeliver(Orders orders) {
		Orders newOrders = this.getById(orders.getId());
		newOrders.setExpressId(orders.getExpressId());
		newOrders.setExpressNo(orders.getExpressNo());
		newOrders.setDeliveryTime(new Date());
		return this.saveOrUpdate(newOrders);
	}

	@Override
	public List<UserOrdersVo> getUserOrders(Integer userId) {
		QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
		ordersQueryWrapper.eq("user_id",userId);
		ordersQueryWrapper.orderByDesc("create_time");
		List<Orders> ordersList = this.list(ordersQueryWrapper);
		List<UserOrdersVo> userOrdersVos = new ArrayList<>();
		ordersList.forEach(orders -> {
			QueryWrapper<OrderGoods> orderGoodsQueryWrapper = new QueryWrapper<>();
			orderGoodsQueryWrapper.eq("order_no",orders.getOrderNo());
			List<OrderGoods> orderGoodsList = orderGoodsService.list(orderGoodsQueryWrapper);
			UserOrdersVo userOrdersVo = new UserOrdersVo();
			userOrdersVo.setOrders(orders);
			userOrdersVo.setOrderGoodsList(orderGoodsList);
			userOrdersVos.add(userOrdersVo);
		});
		return userOrdersVos;
	}

	@Override
	public boolean cancelOrder(Integer ordersId) {
		Orders orders = this.getById(ordersId);
		orders.setState(2);
		return this.saveOrUpdate(orders);
	}
}
