package com.zjh.bookshop.controller;


import com.zjh.bookshop.config.JwtConfig;
import com.zjh.bookshop.entity.User;
import com.zjh.bookshop.service.CartService;
import com.zjh.bookshop.utils.RedisUtil;
import com.zjh.bookshop.vo.CartVo;
import com.zjh.bookshop.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-03-31
 */
@Api("购物车控制器")
@RestController
@RequestMapping("/bookshop/cart")
@CrossOrigin
public class CartController {

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private CartService cartService;

	@ApiOperation("添加到购物车")
	@PostMapping("/{goodsId}")
	public R addressAdd(HttpServletRequest request, @PathVariable("goodsId") Integer goodsId) {
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);//?
		boolean success = cartService.addCart(user.getId(), goodsId);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("获取购物车")
	@GetMapping
	public R get(HttpServletRequest request) {
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		CartVo vo = cartService.getCart(user.getId());
		return R.ok().data("data", vo);
	}

	@ApiOperation("修改购物车")
	@PutMapping("/{goodsId}")
	public R addressAdd(HttpServletRequest request, @RequestBody Map<String,Object> param, @PathVariable("goodsId")Integer goodsId){
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		CartVo vo  = cartService.updateCart(user.getId(), goodsId, param);
		return R.ok().data("data", vo);
	}

	@ApiOperation("删除购物车中商品")
	@DeleteMapping("/{goodsId}")
	public R selectAll(HttpServletRequest request, @PathVariable("goodsId")Integer goodsId){
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		CartVo vo = cartService.deleteGoods(goodsId, user.getId());
		return R.ok().data("data", vo);
	}

//	@ApiOperation("添加到购物车")
//	@PostMapping
//	public R addressAdd(HttpServletRequest request, @RequestBody Map<String, Object> param) {
//		String authHeader = request.getHeader(jwtConfig.getHeader());
//		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
//		User user = (User) redisUtil.get(authToken);//?
//		CartVo vo = cartService.addCart(user.getId(), param);
//		return R.ok().data("data", vo);
//	}
//
//	@ApiOperation("获取购物车")
//	@GetMapping
//	public R get(HttpServletRequest request) {
//		String authHeader = request.getHeader(jwtConfig.getHeader());
//		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
//		User user = (User) redisUtil.get(authToken);
//		CartVo vo = cartService.getCart(user.getId());
//		return R.ok().data("data", vo);
//	}
//
//	@ApiOperation("修改购物车")
//	@PutMapping("/{goodsId}")
//	public R addressAdd(HttpServletRequest request, @RequestBody Map<String,Object> param, @PathVariable("goodsId")Integer goodsId){
//		String authHeader = request.getHeader(jwtConfig.getHeader());
//		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
//		User user = (User) redisUtil.get(authToken);
//		CartVo vo  = cartService.updateCart(user.getId(), goodsId, param);
//		return R.ok().data("data", vo);
//	}
//
//	@ApiOperation("删除购物车中商品")
//	@DeleteMapping("/{goodsId}")
//	public R selectAll(HttpServletRequest request, @PathVariable("goodsId")Integer goodsId){
//		String authHeader = request.getHeader(jwtConfig.getHeader());
//		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
//		User user = (User) redisUtil.get(authToken);
//		CartVo vo  = cartService.deleteGoods(goodsId, user.getId());
//		return R.ok().data("data", vo);
//	}
}

