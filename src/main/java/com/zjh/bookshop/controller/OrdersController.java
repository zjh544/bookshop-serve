package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.config.JwtConfig;
import com.zjh.bookshop.entity.Orders;
import com.zjh.bookshop.entity.User;
import com.zjh.bookshop.service.OrdersService;
import com.zjh.bookshop.utils.OrderCodeFactory;
import com.zjh.bookshop.utils.RedisUtil;
import com.zjh.bookshop.vo.AddressVo;
import com.zjh.bookshop.vo.OrderVo;
import com.zjh.bookshop.vo.R;
import com.zjh.bookshop.vo.UserOrdersVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-04-06
 */
@Api("订单控制器")
@RestController
@RequestMapping("/bookshop/orders")
@CrossOrigin
public class OrdersController {

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private OrdersService ordersService;

	@PostMapping()
	public R createOrder(HttpServletRequest request, @RequestBody Map<String, Integer> param) {
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		String orderNo = OrderCodeFactory.getOrderCode(null);
		Integer addressId = param.get("addressId");
		ordersService.createOrder(addressId, orderNo,user.getId());
		return R.ok().data("orderNo", orderNo);
	}

	@GetMapping("/{orderNo}")
	public R getOrderVo(@PathVariable("orderNo")String orderNo){
		OrderVo orderVo = ordersService.getVoByNo(orderNo);
		return R.ok().data("vo", orderVo);
	}

	@ApiOperation(value = "订单列表")
	@GetMapping("/{page}/{limit}")
	public R list(@ApiParam(name="page",value = "当前页数")@PathVariable(value = "page") int page, @PathVariable(value = "limit") @ApiParam(name="limit",value = "每页条目数")int limit,
								@ApiParam(name="query",value = "查詢參數") @RequestParam(value="query",required = false)String query){
		Page<OrderVo> pageQuery =  ordersService.pageQuery(page,limit,query);
		return R.ok().data("total",pageQuery.getTotal()).data("rows",pageQuery.getRecords());
	}

	@ApiOperation("添加物流信息")
	@PostMapping("/deliver")
	public R deliverAdd(@RequestBody Orders orders){
		Boolean success = ordersService.addDeliver(orders);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("添加物流信息")
	@PutMapping("/deliver")
	public R deliverUpdate(@RequestBody Orders orders){
		boolean success = ordersService.saveOrUpdate(orders);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("个人订单")
	@GetMapping("/user")
	public R getUserOrders(HttpServletRequest request){
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		List<UserOrdersVo> list = ordersService.getUserOrders(user.getId());
		return R.ok().data("list", list);
	}

	@ApiOperation("取消订单")
	@GetMapping("/cancel/{ordersId}")
	public R cancelOrder(@PathVariable Integer ordersId){
		boolean success = ordersService.cancelOrder(ordersId);
		return success ? R.ok(): R.error();
	}
}

