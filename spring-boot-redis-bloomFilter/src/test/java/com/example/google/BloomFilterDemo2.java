package com.example.google;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.math.BigDecimal;

/**
 * @author 程序员小强
 * @date 2020-06-27 16:28
 * @desc
 */
public class BloomFilterDemo2 {


public static void main(String[] args) {

    //创建一个插入对象为100万，误报率为0.01%的布隆过滤器
    BloomFilter<Integer> bloomFilter = BloomFilter
            .create(Funnels.integerFunnel(), 1000000, 0.01);

    //插入数据 0 ~ 100w
    for (int i = 0; i < 1000000; i++) {
        bloomFilter.put(i);
    }

    int count = 0;
    //测试误判
    for (int i = 1000000; i < 2000000; i++) {
        if (bloomFilter.mightContain(i)) {
            //累加误判次数
            count++;
        }
    }
    System.out.println("总共的误判数:" + count);
    System.out.println("误判率：" + new BigDecimal(count).divide(BigDecimal.valueOf(1000000)));
}
}