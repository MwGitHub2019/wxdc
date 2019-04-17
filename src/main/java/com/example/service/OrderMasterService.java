package com.example.service;

import com.example.common.ResultResponse;
import com.example.dto.OrderMasterDto;

public interface OrderMasterService {

    ResultResponse insterOrder(OrderMasterDto orderMasterDto);
}
