package com.zjh.bookshop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjh.bookshop.entity.Address;
import com.zjh.bookshop.mapper.AddressMapper;
import com.zjh.bookshop.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.bookshop.vo.AddressVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjh
 * @since 2021-04-04
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

	@Override
	public Boolean addAddress(Integer userId, AddressVo addressVo) {
		Address address = new Address();
		address.setUserId(userId);
		address.setName(addressVo.getName());
		address.setPhone(addressVo.getPhone());
		address.setArea(Arrays.toString(addressVo.getArea()).replace("[","").replace("]",""));
		address.setDetail(addressVo.getDetail());
		return this.save(address);
	}

	@Override
	public List<AddressVo> addressList(Integer userId) {
		QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",userId);
		List<Address> addressList = this.list(queryWrapper);
		List<AddressVo> addressVos = new ArrayList<>();
		for (Address address:addressList) {
			AddressVo addressVo = new AddressVo();
			addressVo.setId(address.getId());
			addressVo.setName(address.getName());
			addressVo.setPhone(address.getPhone());
			addressVo.setArea(StringUtils.split(address.getArea(),", "));
			addressVo.setDetail(address.getDetail());
			addressVos.add(addressVo);
		}
		return addressVos;
	}

	@Override
	public Boolean updateAddress(AddressVo addressVo) {
		Address address = this.getById(addressVo.getId());
		address.setName(addressVo.getName());
		address.setPhone(addressVo.getPhone());
		address.setArea(Arrays.toString(addressVo.getArea()).replace("[","").replace("]",""));
		address.setDetail(addressVo.getDetail());
		return this.saveOrUpdate(address);
	}

}
