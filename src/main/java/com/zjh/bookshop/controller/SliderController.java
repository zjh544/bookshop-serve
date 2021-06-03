package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjh.bookshop.entity.Admin;
import com.zjh.bookshop.entity.Slider;
import com.zjh.bookshop.entity.User;
import com.zjh.bookshop.service.SliderService;
import com.zjh.bookshop.vo.AddressVo;
import com.zjh.bookshop.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-04-08
 */
@Api("轮播图控制器")
@RestController
@RequestMapping("/bookshop/slider")
@CrossOrigin
public class SliderController {

	@Autowired
	private SliderService sliderService;

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

	@ApiOperation("添加轮播图")
	@PostMapping
	public R add(@ApiParam("轮播图对象") @RequestBody Slider slider) {
		Boolean success = sliderService.saveSlider(slider);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("轮播图列表")
	@GetMapping
	public R sliderList(){
		QueryWrapper<Slider> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("sort");
		List<Slider> list = sliderService.list(queryWrapper);
		return R.ok().data("list", list);
	}

	@ApiOperation("排序")
	@GetMapping("/{id}")
	public R sort(@ApiParam("排序") @PathVariable String id){
		Boolean success = sliderService.updateSort(id);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("修改轮播图")
	@PutMapping
	public R sliderEdit(@RequestBody Slider slider){
		Boolean success = sliderService.updateSlider(slider);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("删除轮播图")
	@DeleteMapping("/{id}")
	public R sliderDelete(@PathVariable("id") Integer id){
		Boolean success = sliderService.removeSlider(id);
		return success ? R.ok(): R.error();
	}
}

