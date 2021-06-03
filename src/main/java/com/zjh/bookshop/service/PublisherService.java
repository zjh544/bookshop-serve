package com.zjh.bookshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.bookshop.entity.Publisher;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-03-25
 */
public interface PublisherService extends IService<Publisher> {

	/**
	 * 分页查询
	 * @param page：当前页数
	 * @param limit：每页条目数
	 * @param query：查询参数
	 * @return
	 */
	Page<Publisher> pageQuery(int page, int limit, String query);

}
