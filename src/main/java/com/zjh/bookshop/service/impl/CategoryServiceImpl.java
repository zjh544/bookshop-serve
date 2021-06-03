package com.zjh.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Category;
import com.zjh.bookshop.mapper.CategoryMapper;
import com.zjh.bookshop.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.vo.CategoryTreeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-03-23
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

	@Override
	public Page<CategoryTreeVo> pageLevelQuery(Integer page, Integer limit, Integer level) {
		List<CategoryTreeVo> list = new ArrayList<>();
		List<Category> ls = new ArrayList<>();
		Page<Category> CategoriesPage = null;
		if (page != -1 && limit != -1) {//查询所有
			CategoriesPage = this.pageQuery(page, limit, null);
			ls = CategoriesPage.getRecords();
		} else {
			QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("level", 0);
			queryWrapper.orderByAsc( "id");
			ls = this.baseMapper.selectList(queryWrapper);
		}
		Page<CategoryTreeVo> pageResult = new Page<>();
		ls.forEach(category -> {
			CategoryTreeVo vo = new CategoryTreeVo();
			BeanUtils.copyProperties(category, vo);//使用BeanUtils.copyProperties进行对象之间的属性赋值
			list.add(vo);//一级分类
			if (level > 1) {
				List<Category> children = this.getChildren(category.getId());//二级分类列表
				children.forEach(category1 -> {
					CategoryTreeVo vo1 = new CategoryTreeVo();
					BeanUtils.copyProperties(category1, vo1);
					vo.getChildren().add(vo1);
					if (level > 2) {
						List<Category> children2 = this.getChildren(category1.getId());//三级分类列表
						children2.forEach(category2 -> {
							CategoryTreeVo vo2 = new CategoryTreeVo();
							BeanUtils.copyProperties(category2, vo2);
							vo1.getChildren().add(vo2);
							vo2.setChildren(null);
						});
					} else {
						vo1.setChildren(null);
					}
				});
			} else {
				vo.setChildren(null);
			}
		});
		pageResult.setTotal(CategoriesPage == null ? ls.size() : CategoriesPage.getTotal());
		pageResult.setRecords(list);
		return pageResult;
	}

	@Override
	public Page<Category> pageQuery(Integer page, Integer limit, String query) {
		Page<Category> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(limit);
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("level", 0);
		queryWrapper.orderByAsc("id");
		this.page(pageQuery, queryWrapper);
		return pageQuery;
	}

	@Override
	public List<Category> getChildren(Integer id) {
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("pid", id);
		return this.baseMapper.selectList(queryWrapper);
	}
}
