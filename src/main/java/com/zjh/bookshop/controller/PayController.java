package com.zjh.bookshop.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjh.bookshop.config.AlipayConfig;
import com.zjh.bookshop.entity.Orders;
import com.zjh.bookshop.service.OrdersService;
import com.zjh.bookshop.vo.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/bookshop/pay")
@Api(tags = "订单控制器")
@Slf4j
@CrossOrigin
public class PayController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayConfig alipayConfig;

//    @RequestMapping
//    public R pay() {
//        String orderNo = "test1";
//        String orderName = "文城书店订单";
//        BigDecimal orderPrice = BigDecimal.valueOf(115);
//        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
//        alipayRequest.setNotifyUrl(alipayConfig.getNotify_url());//在公共参数中设置回跳和通知地址
//        alipayRequest.setReturnUrl(alipayConfig.getReturn_url());
//        alipayRequest.setBizContent("{" +
//                "    \"out_trade_no\":\"" + orderNo + "\"," +//商户订单号
//                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +//销售产品码
//                "    \"total_amount\":" + orderPrice.doubleValue() + "," +//交易金额
//                "    \"subject\":\"" + orderName + "\"," +//订单标题
//                "    \"body\":\"" + orderName + "\"," +//对交易或商品的描述
//                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +//?公用回传参数
//                "    \"extend_params\":{" +//扩展参数
//                "    \"sys_service_provider_id\":\"2088621955186364\"" +//可删，商户UID
//                "    }"+
//                "  }");//填充业务参数
//        String form="";
//        try {
//            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return R.ok().data("content", form);
//    }

    @PostMapping
    public R pay(@RequestBody Map<String, Object> param) {
        String orderNo = param.get("orderNo").toString();
        String orderName = "文城书店";
        QueryWrapper<Orders> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Orders one = ordersService.getOne(queryWrapper);
        BigDecimal orderPrice = one.getPrice();
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setNotifyUrl(alipayConfig.getNotify_url());//在公共参数中设置回跳和通知地址
			alipayRequest.setReturnUrl(alipayConfig.getReturn_url());
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderNo + "\"," +//商户订单号
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +//销售产品码
                "    \"total_amount\":" + orderPrice.doubleValue() + "," +//交易金额
                "    \"subject\":\"" + orderName + "\"," +//订单标题
                "    \"body\":\"" + orderName + "\"," +//对交易或商品的描述
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +//?公用回传参数
                "    \"extend_params\":{" +//扩展参数
                "    \"sys_service_provider_id\":\"2088621955186364\"" +//商户UID
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return R.ok().data("content", form);
    }

    @RequestMapping("/notify_url")
    public void notifyUrl(@RequestParam Map<String,String> param) throws AlipayApiException {
         System.out.println(JSONObject.toJSONString(param));
        boolean signVerified = AlipaySignature.rsaCheckV1(param, alipayConfig.getAlipay_public_key(), alipayConfig.getCharset(),alipayConfig.getSign_type());//调用SDK验证签名
        if(signVerified){
            // TODO 验签成功则继续业务操作，最后在response中返回success
            log.info("支付成功");
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("failure");
        }
    }

    @RequestMapping("/return_url")
    public void returnUrl(@RequestParam Map<String,String> param, HttpServletResponse response) throws AlipayApiException, IOException {
    	System.out.println(JSONObject.toJSONString(param));
    	boolean signVerified = AlipaySignature.rsaCheckV1(param, alipayConfig.getAlipay_public_key(), alipayConfig.getCharset(),alipayConfig.getSign_type());//调用SDK验证签名
			String orderNo = param.get("out_trade_no");
			QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("order_no",orderNo);
			Orders orders = ordersService.getOne(queryWrapper);
			orders.setState(1);
			ordersService.saveOrUpdate(orders);
			if(signVerified){
				// TODO 验签成功则继续业务操作，最后在response中返回success
				log.info("支付成功");
				response.sendRedirect("http://47.106.245.151:8080/#/orders");
			}else{
				// TODO 验签失败则记录异常日志，并在response中返回failure.
				System.out.println("failure");
			}
    }

//    @RequestMapping("/alnotify")
//    public void alnotify(@RequestParam Map<String,String> param) throws AlipayApiException {
//        // System.out.println(JSONObject.toJSONString(param));
//        boolean signVerified = AlipaySignature.rsaCheckV1(param, alipayConfig.getAlipay_public_key(), alipayConfig.getCharset(),alipayConfig.getSign_type());//调用SDK验证签名
//        String orderNo = param.get("out_trade_no");
//        QueryWrapper<Order> queryWrapper  = new QueryWrapper<>();
//        queryWrapper.eq("order_no", orderNo);
//        Order one = orderService.getOne(queryWrapper);
//        one.setState(1);
//        orderService.saveOrUpdate(one);
//        if(signVerified){
//            // TODO 验签成功则继续业务操作，最后在response中返回success
//            log.info("支付成功");
//        }else{
//            // TODO 验签失败则记录异常日志，并在response中返回failure.
//            System.out.println("failure");
//        }
//    }
}
