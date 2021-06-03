package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Goods;
import com.zjh.bookshop.form.GoodsAddForm;
import com.zjh.bookshop.service.GoodsService;
import com.zjh.bookshop.vo.GoodsInfoVo;
import com.zjh.bookshop.vo.R;
import com.zjh.bookshop.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-03-24
 */
@Api(tags = "商品控制器")
@RestController
@RequestMapping("/bookshop/goods")
@CrossOrigin
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@ApiOperation("商品列表")
	@GetMapping("/{page}/{limit}")
	public R list(@ApiParam(name = "page", value = "当前页面") @PathVariable(value = "page") int page,
								@ApiParam(name = "limit", value = "每页条数目") @PathVariable(value = "limit") int limit,
								@ApiParam(name = "query", value = "查询参数") @RequestParam(value = "query", required = false) String query) {
		Page<GoodsVo> pageQuery = goodsService.goodsDetail(page, limit, query);
		return R.ok().data("total", pageQuery.getTotal()).data("rows", pageQuery.getRecords());
	}

	@ApiOperation("上传文件")
	@RequestMapping("/upload")
	public R upload(MultipartFile file) {
		String path = "";
		String fileName = "";
		try {
//			log.info(GoodsController.class.getResource("/").getPath());
			String parentPath = GoodsController.class.getResource("/static/tmp/").getPath();
//			log.info(parentPath);
			fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));//截取文件后缀
			path = parentPath + fileName;
//			log.info(path);
			File tmpFile = new File(path);
			file.transferTo(tmpFile);//将上传的文件写入指定文件夹
		} catch (Exception e) {
			e.printStackTrace();
			return R.error();
		}
		return R.ok().data("path", "/static/tmp/" + fileName).data("url", "http://47.106.245.151:18080/tmp" + fileName);
	}

	@ApiOperation("添加商品")
	@PostMapping
	public R add(@RequestBody GoodsAddForm form) {
		boolean save = goodsService.saveGoods(form);
		return save ? R.ok() : R.error();
	}

	@ApiOperation("修改商品")
	@PutMapping("/{goodsId}/{goodsDetailId}")
	public R edit(@ApiParam(name = "goodsId", value = "商品id") @PathVariable(value = "goodsId") int goodsId,
								@ApiParam(name = "goodsDetailId", value = "商品详情id") @PathVariable(value = "goodsDetailId") int goodsDetailId,
								@RequestBody GoodsAddForm form) {
		boolean save = goodsService.updateGoods(goodsId, goodsDetailId, form);
		return save ? R.ok() : R.error();
	}

	@ApiOperation("删除商品")//逻辑删除
	@DeleteMapping("/{id}")
	public R delete(@ApiParam(name = "id", value = "商品id") @PathVariable("id") Integer id) {
		boolean delete = goodsService.removeById(id);
		return delete ? R.ok() : R.error();
	}

	@ApiOperation("主页商品列表")
	@GetMapping("/index/{page}/{limit}")
	public R indexList(@ApiParam(name = "page", value = "当前页面") @PathVariable(value = "page") int page,
								@ApiParam(name = "limit", value = "每页条数目") @PathVariable(value = "limit") int limit,
								@ApiParam(name = "query", value = "查询参数") @RequestParam(value = "query", required = false) String query) {
		Page<Goods> pageQuery = goodsService.pageQuery(page, limit, query);
		return R.ok().data("total", pageQuery.getTotal()).data("rows", pageQuery.getRecords());
	}

	@ApiOperation("获取商品信息")
	@RequestMapping("/{id}")
	public R getInfo(@PathVariable Integer id) {
		GoodsInfoVo goodsInfoVo = goodsService.getInfoById(id);
		return R.ok().data("data", goodsInfoVo);
	}

	@ApiOperation("通过分类获取商品")
	@GetMapping("/cat/{page}/{limit}")
	public R getGoodsByCat(@ApiParam(name = "page", value = "当前页面") @PathVariable(value = "page") int page,
												 @ApiParam(name = "limit", value = "每页条数目") @PathVariable(value = "limit") int limit,
												 @ApiParam(name = "query", value = "查询参数") @RequestParam(value = "query", required = false) int cat) {
		Page<Goods> goodsPage = goodsService.getGoodsByCat(page, limit, cat);
		return R.ok().data("total", goodsPage.getTotal()).data("rows", goodsPage.getRecords());
	}
}

