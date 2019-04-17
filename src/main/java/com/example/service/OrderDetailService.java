package com.example.service;

import com.example.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    //批量插入

    void batchInsert(List<OrderDetail> list);
}
