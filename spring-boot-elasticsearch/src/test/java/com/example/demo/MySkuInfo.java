package com.example.demo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

@Data
public class MySkuInfo implements Serializable {

    private static final long serialVersionUID = -1638863591911946639L;

    /**
     * 商品规格ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品规格标题
     * analyzer 指定索引的是用的分词分词器
     */
    private String title;

    private String goodsName;

    /**
     * 商品价格，单位为：元
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stockNum;

    /**
     * 销售量
     */
    private Integer salesNum;

    /**
     * 评价数量
     */
    private Integer appraiseNum;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 商品图片
     */
    private String imageUrl;

    /**
     * 类目ID
     */
    private Long categoryId;

    /**
     * 分类名称，
     * 关键字 (keyword)不分词
     */
    private String categoryName;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 品牌名称，
     * 关键字 (keyword)不分词
     */
    private String brandName;

    /**
     * 规格JSON
     */
    private String spec;

    /**
     * 规格JSON 转 > Map
     */
    private Map<String, Object> specMap;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
