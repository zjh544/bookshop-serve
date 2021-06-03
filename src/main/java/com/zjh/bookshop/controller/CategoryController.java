package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Category;
import com.zjh.bookshop.service.CategoryService;
import com.zjh.bookshop.vo.R;
import com.zjh.bookshop.vo.CategoryTreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-03-23
 */
@Api(tags = "商品分类控制器")
@RestController
@RequestMapping("/bookshop/category")
@CrossOrigin
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@ApiOperation("分类列表")
	@GetMapping("/{page}/{limit}/{level}")
	public R list(@ApiParam("当前页数") @PathVariable("page") Integer page,
								@ApiParam("每页条目数") @PathVariable("limit") Integer limit,
								@ApiParam("分类等级") @PathVariable("level") Integer level) {
		Page<CategoryTreeVo> categoryTreeVoPage = categoryService.pageLevelQuery(page, limit, level);
		return R.ok().data("total", categoryTreeVoPage.getTotal()).data("rows", categoryTreeVoPage.getRecords());
	}

	@ApiOperation("添加分类")
	@PostMapping
	public R add(@RequestBody Category category) {
		boolean success = categoryService.save(category);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("修改分类")
	@PutMapping
	public R put(@RequestBody Category category) {
		boolean success = categoryService.saveOrUpdate(category);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("删除分类")
	@DeleteMapping("/{id}")
	public R delete(@ApiParam("分类id") @PathVariable("id") Integer id) {
		boolean success = categoryService.removeById(id);
		return success ? R.ok() : R.error();
	}

	@ApiOperation("获取分类信息")
	@GetMapping("/{id}")
	public R getInfo(@ApiParam("分类id") @PathVariable("id") Integer id) {
		Category byId = categoryService.getById(id);
		return R.ok().data("data", byId);
	}
}

