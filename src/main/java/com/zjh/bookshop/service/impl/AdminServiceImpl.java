package com.zjh.bookshop.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.entity.Admin;
import com.zjh.bookshop.mapper.AdminMapper;
import com.zjh.bookshop.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-03-21
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

	@Override
	public Page<Admin> pageQuery(int page, int limit, String query) {
		Page<Admin> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(limit);
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(query)) {
			queryWrapper.like("username", query).or().like("name",query);
		}
		queryWrapper.orderByDesc("create_time");
		this.page(pageQuery, queryWrapper);
		System.out.println(pageQuery.getRecords());
		return pageQuery;
	}

	@Override
	public Admin getByName(String username) {
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		return this.getOne(queryWrapper);
	}
}
