package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Publisher;
import com.zjh.bookshop.entity.User;
import com.zjh.bookshop.service.PublisherService;
import com.zjh.bookshop.vo.AddressVo;
import com.zjh.bookshop.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjh
 * @since 2021-03-25
 */
@Api("出版社控制器")
@RestController
@RequestMapping("/bookshop/publisher")
@CrossOrigin
public class PublisherController {

	@Autowired
	private PublisherService publisherService;

	@ApiOperation("出版社列表")
	@GetMapping("/{page}/{limit}")
	public R list(@ApiParam(name = "page", value = "当前页面") @PathVariable(value = "page") int page,
								@ApiParam(name = "limit", value = "每页条数目") @PathVariable(value = "limit") int limit,
								@ApiParam(name = "query", value = "查询参数") @RequestParam(value = "query", required = false) String query) {
		Page<Publisher> pageQuery = publisherService.pageQuery(page, limit, query);
		return R.ok().data("total", pageQuery.getTotal()).data("rows", pageQuery.getRecords());
	}

	@ApiOperation("添加出版社")
	@PostMapping
	public R addressAdd(@RequestBody Publisher publisher){
		boolean success = publisherService.save(publisher);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("修改出版社")
	@PutMapping
	public R addressEdit(@RequestBody Publisher publisher){
		boolean success = publisherService.saveOrUpdate(publisher);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("删除出版社")
	@DeleteMapping("/{id}")
	public R addressDelete(@PathVariable("id") Integer id){
		boolean success = publisherService.removeById(id);
		return success ? R.ok(): R.error();
	}

}

