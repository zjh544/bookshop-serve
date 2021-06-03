package com.zjh.bookshop.vo;

import com.zjh.bookshop.entity.Address;
import com.zjh.bookshop.entity.Express;
import com.zjh.bookshop.entity.OrderGoods;
import com.zjh.bookshop.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderVo {

	Orders orders;

	List<OrderGoods> orderGoodsList;

	AddressVo addressVo;

	Express express;
}
