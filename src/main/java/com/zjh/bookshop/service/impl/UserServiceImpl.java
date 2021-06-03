package com.zjh.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Admin;
import com.zjh.bookshop.entity.User;
import com.zjh.bookshop.mapper.UserMapper;
import com.zjh.bookshop.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.vo.RegisterVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-03-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Override
	public Page<User> pageQuery(int page, int limit, String query) {
		Page<User> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(limit);
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(query)) {
			queryWrapper.like("username", query);
		}
		queryWrapper.orderByDesc("create_time");
		this.page(pageQuery, queryWrapper);
		System.out.println(pageQuery.getRecords());
		return pageQuery;
	}

	@Override
	public User getByName(String username) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		return this.getOne(queryWrapper);
	}

	@Override
	public Boolean register(RegisterVo registerVo) {
		boolean success;
		if (registerVo.getPassword().equals(registerVo.getConfirmPwd())) {
			String md5Password = DigestUtils.md5DigestAsHex(registerVo.getPassword().getBytes());
			User user = new User();
			user.setUsername(registerVo.getUsername());
			user.setPassword(md5Password);
			success = this.save(user);
		} else {
			return false;
		}
		return success;
	}

	@Override
	public boolean editProfile(User user) {
		User byId = this.getById(user.getId());
		if (!user.getPassword().equals(byId.getPassword())) {
			String newPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
			user.setPassword(newPassword);
		}
		return this.saveOrUpdate(user);
	}
}
