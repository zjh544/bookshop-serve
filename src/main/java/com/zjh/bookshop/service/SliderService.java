package com.zjh.bookshop.service;

import com.zjh.bookshop.entity.Slider;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-04-08
 */
public interface SliderService extends IService<Slider> {

	Boolean saveSlider(Slider slider);

	Boolean updateSort(String id);

	Boolean updateSlider(Slider slider);

	Boolean removeSlider(Integer id);
}
