package com.example.web.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderRateParam implements Serializable {

    private static final long serialVersionUID = -1L;

    private String orderId;

}
