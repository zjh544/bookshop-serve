package com.zjh.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.entity.Express;
import com.zjh.bookshop.entity.Publisher;
import com.zjh.bookshop.entity.Slider;
import com.zjh.bookshop.mapper.ExpressMapper;
import com.zjh.bookshop.service.ExpressService;
import com.zjh.bookshop.utils.PictureUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-04-10
 */
@Service
public class ExpressServiceImpl extends ServiceImpl<ExpressMapper, Express> implements ExpressService {

	@Override
	public Page<Express> pageQuery(int page, int limit, String query) {
		Page<Express> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(limit);
		QueryWrapper<Express> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(query)) {
			queryWrapper.like("name", query);
		}
		queryWrapper.orderByDesc("create_time");
		this.page(pageQuery, queryWrapper);
		System.out.println(pageQuery.getRecords());
		return pageQuery;
	}

	@Override
	public Boolean saveExpress(Express express) {
		String copyFile = null;
		try {
			copyFile = PictureUtil.copyFile(express.getPic(),"express");
		} catch (IOException e) {
			e.printStackTrace();
		}
		express.setPic(copyFile);

		return this.save(express);
	}

	@Override
	public Boolean updateExpress(Express express) {
		if (express.getPic() != null) {
			String copyFile = null;
			try {
				copyFile = PictureUtil.copyFile(express.getPic(),"express");
			} catch (IOException e) {
				e.printStackTrace();
			}
			express.setPic(copyFile);
		}
		return this.saveOrUpdate(express);
	}
}
