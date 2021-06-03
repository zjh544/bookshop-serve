package com.zjh.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Admin;
import com.zjh.bookshop.entity.Publisher;
import com.zjh.bookshop.mapper.PublisherMapper;
import com.zjh.bookshop.service.PublisherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-03-25
 */
@Service
public class PublisherServiceImpl extends ServiceImpl<PublisherMapper, Publisher> implements PublisherService {

	@Override
	public Page<Publisher> pageQuery(int page, int limit, String query) {
		Page<Publisher> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(limit);
		QueryWrapper<Publisher> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(query)) {
			queryWrapper.like("name", query);
		}
		queryWrapper.orderByDesc("create_time");
		this.page(pageQuery, queryWrapper);
		System.out.println(pageQuery.getRecords());
		return pageQuery;
	}
}
