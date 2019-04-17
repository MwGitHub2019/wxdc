package com.example.repository;

import com.example.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {

    //根据类目编号和状态查询商品
    List<ProductInfo> findByProductStatusAndCategoryTypeIn(Integer status,List<Integer> typeList);
}
