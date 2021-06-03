package com.zjh.bookshop.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.bookshop.entity.Express;
import com.zjh.bookshop.entity.Publisher;
import com.zjh.bookshop.service.ExpressService;
import com.zjh.bookshop.vo.R;
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
 * @since 2021-04-10
 */
@Api("快递控制器")
@RestController
@RequestMapping("/bookshop/express")
@CrossOrigin
public class ExpressController {

	@Autowired
	private ExpressService expressService;

	@ApiOperation("快递公司列表")
	@GetMapping("/{page}/{limit}")
	public R list(@ApiParam(name = "page", value = "当前页面") @PathVariable(value = "page") int page,
								@ApiParam(name = "limit", value = "每页条数目") @PathVariable(value = "limit") int limit,
								@ApiParam(name = "query", value = "查询参数") @RequestParam(value = "query", required = false) String query) {
		Page<Express> pageQuery = expressService.pageQuery(page, limit, query);
		return R.ok().data("total", pageQuery.getTotal()).data("rows", pageQuery.getRecords());
	}

	@ApiOperation("添加快递公司")
	@PostMapping
	public R addressAdd(@RequestBody Express express){
		Boolean success = expressService.saveExpress(express);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("修改快递公司")
	@PutMapping
	public R addressEdit(@RequestBody Express express){
		Boolean success = expressService.updateExpress(express);
		return success ? R.ok(): R.error();
	}

	@ApiOperation("删除快递公司")
	@DeleteMapping("/{id}")
	public R addressDelete(@PathVariable("id") Integer id){
		boolean success = expressService.removeById(id);
		return success ? R.ok(): R.error();
	}
	
}

