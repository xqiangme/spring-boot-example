package com.example.google;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * @author 程序员小强
 * @date 2020-06-27 16:28
 * @desc
 */
public class BloomFilterDemo1 {

public static void main(String[] args) {
    //创建一个插入对象为100万，误报率为0.01%的布隆过滤器
    BloomFilter<CharSequence> bloomFilter = BloomFilter
            .create(Funnels.stringFunnel(Charset.forName("utf-8")), 1000000, 0.01);

    // 判断指定元素是否存在
    System.out.println(bloomFilter.mightContain("1001"));
    System.out.println(bloomFilter.mightContain("1002"));

    // 将元素添加进布隆过滤器
    bloomFilter.put("1001");
    bloomFilter.put("1002");

    // 判断指定元素是否存在
    System.out.println(bloomFilter.mightContain("1001"));
    System.out.println(bloomFilter.mightContain("1002"));
}
}