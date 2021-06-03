package com.zjh.bookshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.bookshop.vo.OrderVo;
import com.zjh.bookshop.vo.UserOrdersVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-04-06
 */
public interface OrdersService extends IService<Orders> {

	void createOrder(Integer addressId, String orderNo, Integer userId);

	OrderVo getVoByNo(String orderNo);

	/**
	 * 分页查询
	 * @param page
	 * @param limit
	 * @param query
	 * @return
	 */
	Page<OrderVo> pageQuery(int page, int limit, String query);

	Boolean addDeliver(Orders orders);

	List<UserOrdersVo> getUserOrders(Integer userId);

	boolean cancelOrder(Integer ordersId);
}
