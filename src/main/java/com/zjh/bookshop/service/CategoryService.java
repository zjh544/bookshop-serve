package com.zjh.bookshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.bookshop.vo.CategoryTreeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-03-23
 */
public interface CategoryService extends IService<Category> {

	/**
	 * 按分类等级进行分页查询
	 * @param page：当前页数
	 * @param limit：每页条目数
	 * @param level：第几级分类
	 * @return
	 */
	Page<CategoryTreeVo> pageLevelQuery(Integer page, Integer limit, Integer level);

	/**
	 * 分页查询
	 * @param page：当前页数
	 * @param limit：每页条目数
	 * @param query：查询参数
	 * @return
	 */
	Page<Category> pageQuery(Integer page, Integer limit, String query);

	/**
	 * 获取子分类
	 * @param catId
	 * @return
	 */
	List<Category> getChildren(Integer catId);
}
