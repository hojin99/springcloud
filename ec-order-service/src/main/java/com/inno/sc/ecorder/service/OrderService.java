package com.inno.sc.ecorder.service;

import com.inno.sc.ecorder.dto.OrderDto;
import com.inno.sc.ecorder.jpa.OrderEntity;

public interface OrderService {
    OrderDto createOder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
