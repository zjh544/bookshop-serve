package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjh.bookshop.config.JwtConfig;
import com.zjh.bookshop.entity.Address;
import com.zjh.bookshop.entity.User;
import com.zjh.bookshop.service.AddressService;
import com.zjh.bookshop.utils.RedisUtil;
import com.zjh.bookshop.vo.AddressVo;
import com.zjh.bookshop.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-04-04
 */
@Api("地址控制器")
@RestController
@RequestMapping("/bookshop/address")
@CrossOrigin
public class AddressController<RedisUtils> {

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private AddressService addressService;

	@ApiOperation("添加地址")
	@PostMapping
	public R addressAdd(HttpServletRequest request, @RequestBody AddressVo addressVo){
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		Boolean success = addressService.addAddress(user.getId(),addressVo);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("地址列表")
	@GetMapping()
	public R addressList(HttpServletRequest request){
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		List<AddressVo> list = addressService.addressList(user.getId());
		return R.ok().data("list", list);
	}

	@ApiOperation("修改地址")
	@PutMapping
	public R addressEdit(@RequestBody AddressVo addressVo){
		Boolean success = addressService.updateAddress(addressVo);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("删除地址")
	@DeleteMapping("/{id}")
	public R addressDelete(@PathVariable("id") Integer id){
		Boolean success = addressService.removeById(id);
		return success ? R.ok(): R.error();
	}
}

