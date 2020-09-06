package com.example.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单信息
 *
 * @author 程序员小强
 */
@Data
public class OrderInfoModel implements Serializable {

    private static final long serialVersionUID = 4582290160208154430L;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 标题
     */
    private String title;

    /**
     * 备注
     */
    private String remarks;
}