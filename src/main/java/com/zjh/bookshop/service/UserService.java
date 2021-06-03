package com.zjh.bookshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Admin;
import com.zjh.bookshop.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.bookshop.vo.RegisterVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-03-30
 */
public interface UserService extends IService<User> {

	/**
	 * 分页查询
	 * @param page：当前页数
	 * @param limit：每页条目数
	 * @param query：查询参数
	 * @return
	 */
	Page<User> pageQuery(int page, int limit, String query);

	/**
	 * 判断用户名是否被注册
	 * @param username：用户名
	 * @return
	 */
	User getByName(String username);

	Boolean register(RegisterVo registerVo);

	boolean editProfile(User user);
}
