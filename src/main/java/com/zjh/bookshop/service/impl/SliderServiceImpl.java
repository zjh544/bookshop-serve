package com.zjh.bookshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjh.bookshop.entity.Slider;
import com.zjh.bookshop.mapper.SliderMapper;
import com.zjh.bookshop.service.SliderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.utils.PictureUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-04-08
 */
@Service
public class SliderServiceImpl extends ServiceImpl<SliderMapper, Slider> implements SliderService {

	@Override
	public Boolean saveSlider(Slider slider) {
		Collection<Slider> sliderCollection = this.list(null);
		slider.setSort(sliderCollection.size()+1);

		String copyFile = null;
		try {
			copyFile = PictureUtil.copyFile(slider.getPic(),"slider");
		} catch (IOException e) {
			e.printStackTrace();
		}
		slider.setPic(copyFile);

		return this.save(slider);
	}

	@Override
	public Boolean updateSort(String id) {
		String[] ids = id.split(",");
		boolean success=true;
		for (int i = 0; i < ids.length; i++) {
			Slider slider = this.getById(ids[i]);
			slider.setSort(i+1);
			success=this.saveOrUpdate(slider)&&success;
		}
		return success;
	}

	@Override
	public Boolean updateSlider(Slider slider) {
		if (slider.getPic() != null) {
			String copyFile = null;
			try {
				copyFile = PictureUtil.copyFile(slider.getPic(),"slider");
			} catch (IOException e) {
				e.printStackTrace();
			}
			slider.setPic(copyFile);
		}
		return this.saveOrUpdate(slider);
	}

	@Override
	public Boolean removeSlider(Integer id) {
		boolean success = this.removeById(id);

		QueryWrapper<Slider> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("sort");
		List<Slider> sliderList = this.list(queryWrapper);
		for (int i = 0; i < sliderList.size(); i++) {
			if (sliderList.get(i).getSort() != i + 1) {
				sliderList.get(i).setSort(i+1);
			}
		}
		return this.saveOrUpdateBatch(sliderList)&&success;
	}
}
