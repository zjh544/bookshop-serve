package com.zjh.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.entity.*;
import com.zjh.bookshop.form.GoodsAddForm;
import com.zjh.bookshop.mapper.GoodsMapper;
import com.zjh.bookshop.service.*;
import com.zjh.bookshop.utils.PictureUtil;
import com.zjh.bookshop.vo.GoodsInfoVo;
import com.zjh.bookshop.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-03-24
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

	@Autowired
	private GoodsDetailService goodsDetailService;

	@Autowired
	private PublisherService publisherService;

	@Autowired
	private GoodsPicService goodsPicService;

	@Autowired
	private CategoryService categoryService;

	@Override
	public Page<GoodsVo> goodsDetail(int page, int limit, String query) {
		Page<Goods> goodsPage = pageQuery(page, limit, query);
		List<GoodsVo> list = new ArrayList<>();
		List<Goods> goodsList = goodsPage.getRecords();
		goodsList.forEach(goods -> {
			QueryWrapper<GoodsDetail> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("goods_id", goods.getId());
			GoodsDetail goodsDetail = goodsDetailService.getOne(queryWrapper);
			Publisher publisher = publisherService.getById(goodsDetail.getPublisherId());
			GoodsVo goodsVo = new GoodsVo();
			goodsVo.setGoods(goods);
			goodsVo.setGoodsDetail(goodsDetail);
			goodsVo.setPublisher(publisher);
			list.add(goodsVo);
		});
		Page<GoodsVo> goodsVoPage = new Page<>();
		goodsVoPage.setTotal(goodsPage.getTotal());
		goodsVoPage.setRecords(list);
		return goodsVoPage;
	}

	@Override
	public Page<Goods> pageQuery(int page, int limit, String query) {
		Page<Goods> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(limit);
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(query)) {
			if (query.equals("折扣")){
				queryWrapper.eq("discount",1);
			}else {
				queryWrapper.like("name", query);
				queryWrapper.orderByDesc("create_time");
			}
		}else {
			queryWrapper.orderByDesc("create_time");
		}
		this.page(pageQuery, queryWrapper);

		return pageQuery;
	}

	@Override
	public boolean saveGoods(GoodsAddForm form) {
		try {
			Goods goods = new Goods();
			goods.setCate1(form.getCatIds()[0]);
			goods.setCate2(form.getCatIds()[1]);
			goods.setCate3(form.getCatIds()[2]);
			goods.setName(form.getName());
			goods.setAuthor(form.getAuthor());
			if (form.getOriginalPrice() != null) {
				goods.setOriginalPrice(form.getOriginalPrice());
				goods.setDiscount(1);
			}
			goods.setPrice(form.getPrice());
			goods.setStock(form.getStock());

			String goodsPic = form.getGoodsPic();
			goodsPic= PictureUtil.class.getResource(goodsPic).getPath();
//			String smallPic = PictureUtil.zoomBySize(goodsPic,"goods", 200, 200);
			String newPic = PictureUtil.zoomBySize(goodsPic,"goods", 500, 500);
			goods.setPic(newPic);

			boolean success = this.save(goods);

			GoodsDetail goodsDetail = new GoodsDetail();
			goodsDetail.setGoodsId(goods.getId());
			goodsDetail.setPublisherId(form.getPublisherId());
			goodsDetail.setIsbn(form.getIsbn());
			if (form.getTranslator()!=null){
				goodsDetail.setTranslator(form.getTranslator());
			}
			goodsDetail.setIntroduction(form.getIntroduction());
			goodsDetail.setPages(form.getPages());
			goodsDetail.setFormat(form.getFormat());
			goodsDetail.setBinding(form.getBinding());
			goodsDetail.setEdition(form.getEdition());
			goodsDetail.setImpression(form.getImpression());

			success = goodsDetailService.save(goodsDetail)&&success;

			String[] contentPics = form.getContentPic();
			List<GoodsPic> pics=new ArrayList<>();
			for (String contentPic:contentPics){
				String copyFile = PictureUtil.copyFile(contentPic,"goodsContent");
				GoodsPic pic = new GoodsPic();
				pic.setGoodsId(goods.getId());
				pic.setPic(copyFile);
				pics.add(pic);
			}

			success = goodsPicService.saveBatch(pics) && success;

			return success;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean updateGoods(int goodsId,int goodsDetailId,GoodsAddForm form) {
		try {
			Goods goods = new Goods();
			goods.setId(goodsId);
			goods.setCate1(form.getCatIds()[0]);
			goods.setCate2(form.getCatIds()[1]);
			goods.setCate3(form.getCatIds()[2]);
			goods.setName(form.getName());
			goods.setAuthor(form.getAuthor());
			if (form.getOriginalPrice() != null) {
				goods.setOriginalPrice(form.getOriginalPrice());
				goods.setDiscount(1);
			}else {
				goods.setDiscount(0);
			}
			goods.setPrice(form.getPrice());
			goods.setStock(form.getStock());

			String goodsPic = form.getGoodsPic();
			if (goodsPic!=null){
				goodsPic= PictureUtil.class.getResource(goodsPic).getPath();
				String pic = PictureUtil.zoomBySize(goodsPic,"goods", 500, 500);
				goods.setPic(pic);
			}

			boolean success = this.saveOrUpdate(goods);

			GoodsDetail goodsDetail = new GoodsDetail();
			goodsDetail.setId(goodsDetailId);
			goodsDetail.setGoodsId(goods.getId());
			goodsDetail.setPublisherId(form.getPublisherId());
			goodsDetail.setIsbn(form.getIsbn());
			if (form.getTranslator()!=null){
				goodsDetail.setTranslator(form.getTranslator());
			}
			goodsDetail.setIntroduction(form.getIntroduction());
			goodsDetail.setPages(form.getPages());
			goodsDetail.setFormat(form.getFormat());
			goodsDetail.setBinding(form.getBinding());
			goodsDetail.setEdition(form.getEdition());
			goodsDetail.setImpression(form.getImpression());

			success = goodsDetailService.saveOrUpdate(goodsDetail)&&success;

			//?需要把原来的删掉吗
			String[] contentPics = form.getContentPic();
			if (contentPics!=null){
				List<GoodsPic> pics=new ArrayList<>();
				for (String contentPic:contentPics){
					String copyFile = PictureUtil.copyFile(contentPic,"goodsContent");
					GoodsPic pic = new GoodsPic();
					pic.setGoodsId(goods.getId());
					pic.setPic(copyFile);
					pics.add(pic);
				}
				success = goodsPicService.saveBatch(pics) && success;
			}
			return success;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public GoodsInfoVo getInfoById(Integer id) {
		Goods goods = this.getById(id);

		QueryWrapper<GoodsDetail> goodsDetailQueryWrapper = new QueryWrapper<>();
		goodsDetailQueryWrapper.eq("goods_id",id);
		GoodsDetail goodsDetail = goodsDetailService.getOne(goodsDetailQueryWrapper);

		QueryWrapper<Publisher> publisherQueryWrapper = new QueryWrapper<>();
		publisherQueryWrapper.eq("id",goodsDetail.getPublisherId());
		Publisher publisher = publisherService.getOne(publisherQueryWrapper);

		QueryWrapper<GoodsPic> goodsPicQueryWrapper = new QueryWrapper<>();
		goodsPicQueryWrapper.eq("goods_id",id);
		List<GoodsPic> goodsPicList = goodsPicService.list(goodsPicQueryWrapper);
		List<String> urls = new ArrayList<>();
		for (GoodsPic goodsPic:goodsPicList){
			urls.add(goodsPic.getPic());
		}

		GoodsInfoVo goodsInfoVo = new GoodsInfoVo();
		goodsInfoVo.setGoods(goods);
		goodsInfoVo.setGoodsDetail(goodsDetail);
		goodsInfoVo.setPublisher(publisher);
		goodsInfoVo.setUrls(urls);
		return goodsInfoVo;
	}

	@Override
	public Page<Goods> getGoodsByCat(int page, int limit, int cat) {
		Category category = categoryService.getById(cat);
		Page<Goods> goodsPage = new Page<>();
		if (category.getLevel()==0){
			goodsPage = pageQueryByCat(page, limit, cat, "cate1");
		} else if (category.getLevel() == 1) {
			goodsPage = pageQueryByCat(page, limit, cat, "cate2");
		}else {
			goodsPage = pageQueryByCat(page, limit, cat, "cate3");
		}
		return goodsPage;
	}

	public Page<Goods> pageQueryByCat(int page, int limit, int query,String level) {
		Page<Goods> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(limit);
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(level,query);
		queryWrapper.orderByDesc("create_time");
		this.page(pageQuery, queryWrapper);

		return pageQuery;
	}
}
