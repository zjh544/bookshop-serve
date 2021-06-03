package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.config.JwtConfig;
import com.zjh.bookshop.entity.Admin;
import com.zjh.bookshop.entity.User;
import com.zjh.bookshop.service.UserService;
import com.zjh.bookshop.utils.JwtUtil;
import com.zjh.bookshop.utils.RedisUtil;
import com.zjh.bookshop.vo.R;
import com.zjh.bookshop.vo.RegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-03-30
 */
@Api("会员控制器")
@RestController
@RequestMapping("/bookshop/user")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private RedisUtil redisUtil;

	@ApiOperation("用户列表")
	@GetMapping("/{page}/{limit}")
	public R list(@ApiParam(name = "page", value = "当前页数") @PathVariable(value = "page") int page,
								@ApiParam(name = "limit", value = "每页条目数") @PathVariable(value = "limit") int limit,
								@ApiParam(name = "query", value = "查询参数") @RequestParam(value = "query", required = false) String query) {
		Page<User> pageQuery = userService.pageQuery(page, limit, query);
		return R.ok().data("total", pageQuery.getTotal()).data("rows", pageQuery.getRecords());
	}

	@ApiOperation("判断用户名是否被注册")
	@GetMapping("/validate")
	public R check(@RequestParam("username") String username) {
		User user = userService.getByName(username);
		return user==null ? R.ok() : R.error();
	}

	@ApiOperation("注册")
	@PostMapping("/register")
	public R register(@RequestBody RegisterVo registerVo) {
		Boolean success = userService.register(registerVo);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("登录")
	@PostMapping("/login")
	public R login(@ApiParam("用户名和密码") @RequestBody Map<String, String> param) {
		String username = param.get("username");
		String password = param.get("password");
		String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
		User user = userService.getByName(username);
		if (user == null || !md5Password.equals(user.getPassword())) {//将为经过加密和已经过加密的密码进行判断
			return R.error().message("用户名或密码错误");
		}
		//生成jwt
		String token = JwtUtil.createJwtToken(username, jwtConfig.getExpiresSecond());
		R r = R.ok().data("token", jwtConfig.getTokenHead() + " " + token).data("username", username);//?为什么加头
		//将token和userDetail缓存至Redis，最长存放一天
		redisUtil.set(token, user, 1l, TimeUnit.DAYS);//?为什么一天
		return r;
	}

	@ApiOperation("退出登录")
	@PostMapping("/logout")
	public R logout(HttpServletRequest request) {
		String authHeader = request.getHeader(jwtConfig.getHeader());//?为什么需要这个头，能不能换一个
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);//?具体怎么截取的
		redisUtil.remove(authToken);
		return R.ok();
	}

	@ApiOperation("获取个人信息")
	@GetMapping("/profile")
	public R getProfile(HttpServletRequest request){
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		User user = (User) redisUtil.get(authToken);
		User profile = userService.getById(user.getId());
		return R.ok().data("profile",profile);
	}

	@ApiOperation("修改个人信息")
	@PutMapping("/profile")
	public R update(@ApiParam("用户对象") @RequestBody User user) {
		boolean success = userService.editProfile(user);
		return success ? R.ok() : R.error();
	}
}

