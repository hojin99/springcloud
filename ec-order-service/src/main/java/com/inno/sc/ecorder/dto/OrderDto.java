package com.inno.sc.ecorder.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDto implements Serializable {

    private String orderId;
    private String userId;

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
}
