package com.example.service;

import com.example.common.ResultResponse;
import com.example.entity.ProductInfo;
import org.springframework.stereotype.Service;

@Service
public interface ProductInfoService {

    ResultResponse queryList();

    ResultResponse<ProductInfo> queryById(String productId);

    void updateProduct(ProductInfo productInfo);
}
