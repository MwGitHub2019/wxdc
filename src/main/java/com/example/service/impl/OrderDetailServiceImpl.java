package com.example.service.impl;

import com.example.dao.impl.BatchDaoImpl;
import com.example.entity.OrderDetail;
import com.example.service.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends BatchDaoImpl<OrderDetail> implements OrderDetailService {

    @Override
    @Transactional //加入事务管理
    public void batchInsert(List<OrderDetail> list) {

        super.batchInsert(list);
    }
}
