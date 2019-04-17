package com.example.service.impl;

import com.example.common.OrderEnums;
import com.example.common.PayEnums;
import com.example.common.ProductEnums;
import com.example.common.ResultResponse;
import com.example.dto.OrderDetailDto;
import com.example.dto.OrderMasterDto;
import com.example.entity.OrderDetail;
import com.example.entity.OrderMaster;
import com.example.entity.ProductInfo;
import com.example.exception.CustomException;
import com.example.repository.OrderMasterRepository;
import com.example.service.OrderDetailService;
import com.example.service.OrderMasterService;
import com.example.service.ProductInfoService;
import com.example.util.BigDecimalUtil;
import com.example.util.IDUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public ResultResponse insterOrder(OrderMasterDto orderMasterDto) {
        /**
         * @Valid: 用于配合JSR303注解 验证参数 只能在 controller层验证
         * validetor：在service层验证
         *
         */
        //取出订单项
        @NotEmpty(message = "订单项不能为空")
        @Valid
        List<OrderDetailDto> items = orderMasterDto.getItems();

        //创建集合来存储Orderdetail
        List<OrderDetail> orderDetailList = Lists.newArrayList();

        //初始化订单的总金额
        BigDecimal totalPrice = new BigDecimal("0");


        //遍历订单项 获取商品详情
        for (OrderDetailDto orderDetailDto : items) {

            //查询订单
            ResultResponse<ProductInfo> resultResponse = productInfoService.queryById(orderDetailDto.getProductId());

            //判断ResultResponse的code即可
            if (resultResponse.getCode() == ResultResponse.fail().getCode()){
                throw new CustomException(resultResponse.getMsg());
            }
            //得到商品
            ProductInfo productInfo = resultResponse.getData();
            //比较库存
            if (productInfo.getProductStock() < orderDetailDto.getProductQuantity()){
                throw new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //创建订单项
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productIcon(productInfo.getProductIcon())
                    .productId(orderDetailDto.getProductId()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(orderDetailDto.getProductQuantity())
                    .build();
            //添加到订单项集合中
            orderDetailList.add(orderDetail);
            //减少库存
            productInfo.setProductStock(productInfo.getProductStock() - orderDetailDto.getProductQuantity());
            //更新商品数据
            productInfoService.updateProduct(productInfo);
            //计算价格
            totalPrice = BigDecimalUtil.add(totalPrice,BigDecimalUtil.multi(productInfo.getProductPrice(),orderDetailDto.getProductQuantity() ) );
        }

        //生成订单id
        String order_id = IDUtils.createIdbyUUID();
        //构建订单信息
        OrderMaster orderMaster = OrderMaster.builder().orderId(order_id)
                .buyerAddress(orderMasterDto.getAddress())
                .buyerName(orderMasterDto.getName())
                .buyerOpenid(orderMasterDto.getOpenid())
                .buyerPhone(orderMasterDto.getPhone())
                .orderAmount(totalPrice)
                .orderStatus(OrderEnums.NEW.getCode())
                .payStatus(PayEnums.WAIT.getCode()).build();

        //将订单id设置到订单项中
        List<OrderDetail> orderDetails = orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(order_id);
            return orderDetail;
        }).collect(Collectors.toList());

        //批量插入订单项
        orderDetailService.batchInsert(orderDetails);
        //插入订单
        orderMasterRepository.save(orderMaster);

        HashMap<String,String> map = Maps.newHashMap();
        map.put("orderId",order_id );
        return ResultResponse.success(map);
    }
}
