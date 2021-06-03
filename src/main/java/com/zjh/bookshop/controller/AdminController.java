package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.config.JwtConfig;
import com.zjh.bookshop.entity.Admin;
import com.zjh.bookshop.service.AdminService;
import com.zjh.bookshop.utils.JwtUtil;
import com.zjh.bookshop.utils.RedisUtil;
import com.zjh.bookshop.vo.R;
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
 * @since 2021-03-21
 */
@Api("管理员控制器")
@RestController
@RequestMapping("/bookshop/admin")
@CrossOrigin
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private RedisUtil redisUtil;

	@ApiOperation("用户列表")
	@GetMapping("/{page}/{limit}")
	public R list(@ApiParam(name = "page", value = "当前页数") @PathVariable(value = "page") int page,
								@ApiParam(name = "limit", value = "每页条目数") @PathVariable(value = "limit") int limit,
								@ApiParam(name = "query", value = "查询参数") @RequestParam(value = "query", required = false) String query) {
		Page<Admin> pageQuery = adminService.pageQuery(page, limit, query);
		return R.ok().data("total", pageQuery.getTotal()).data("rows", pageQuery.getRecords());
	}

	@ApiOperation("校验用户名")
	@GetMapping("/validateName")
	public R validName(String username) {
		Admin byName = adminService.getByName(username);
		return byName == null ? R.ok().data("data", false) : R.ok().data("data", true);
	}

	@ApiOperation("添加用户")
	@PostMapping
	public R add(@ApiParam("用户对象") @RequestBody Admin admin) {
		String md5Password = DigestUtils.md5DigestAsHex(admin.getPassword().getBytes());
		admin.setPassword(md5Password);
		boolean success = adminService.save(admin);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("修改用户")
	@PutMapping
	public R update(@ApiParam("用户对象") @RequestBody Admin admin) {
		boolean success = adminService.saveOrUpdate(admin);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("逻辑删除用户")
	@DeleteMapping("/{id}")
	public R delete(@ApiParam("管理员id") @PathVariable("id") Integer id) {
		boolean success = adminService.removeById(id);
		return success ? R.ok().data("data", false) : R.ok().data("data", true);
	}

	@ApiOperation("登录")
	@PostMapping("/login")
	public R login(@ApiParam("用户名和密码") @RequestBody Map<String, String> param) {
		String username = param.get("username");
		String password = param.get("password");
		String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
		Admin admin = adminService.getByName(username);
		if (admin == null || !md5Password.equals(admin.getPassword())) {//将为经过加密和已经过加密的密码进行判断
			return R.error().message("用户名或密码错误");
		}
		//生成jwt
		String token = JwtUtil.createJwtToken(username, jwtConfig.getExpiresSecond());
		R r = R.ok().data("token", jwtConfig.getTokenHead() + " " + token).data("username", username);
		//将token和userDetail缓存至Redis，最长存放一天
		redisUtil.set(token, admin, 1l, TimeUnit.DAYS);
		return r;
	}

	@ApiOperation("退出登录")
	@PostMapping("/logout")
	public R logout(HttpServletRequest request) {
		String authHeader = request.getHeader(jwtConfig.getHeader());
		String authToken = authHeader.substring(jwtConfig.getTokenHead().length() + 1);
		redisUtil.remove(authToken);
		return R.ok();
	}
}

