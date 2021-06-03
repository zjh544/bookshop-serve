package com.zjh.bookshop.service;

import com.zjh.bookshop.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.bookshop.vo.AddressVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjh
 * @since 2021-04-04
 */
public interface AddressService extends IService<Address> {

	Boolean addAddress(Integer userId, AddressVo addressVo);

	List<AddressVo> addressList(Integer userId);

	Boolean updateAddress(AddressVo addressVo);
}
