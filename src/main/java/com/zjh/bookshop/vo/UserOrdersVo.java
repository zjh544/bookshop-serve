package com.zjh.bookshop.vo;

import com.zjh.bookshop.entity.OrderGoods;
import com.zjh.bookshop.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class UserOrdersVo {

	Orders orders;

	List<OrderGoods> orderGoodsList;
}
