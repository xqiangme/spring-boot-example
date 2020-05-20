package com.job.admin.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * IP地址获取
 *
 * @author mengq
 */
@Slf4j
public class IpAddressUtil {

    public static final String LOCAL_IP = "127.0.0.1";
    public static final String SEPARATE = " | ";
    public static final String DEFAULT_IP = "0:0:0:0:0:0:0:1";
    public static final String UN_KNOWN = "unKnown";
    /**
     * 太平洋开放接口，获取IP真实地址
     */
    public static final String ADDRESS_URL_WHOIS = "http://whois.pconline.com.cn/ip.jsp?ip={0}";
    public static final String ADDRESS_URL_API = "http://ip-api.com/json/{0}?lang=zh-CN";

    /**
     * 根据Ip地址获取地理位置
     *
     * @param ipAddress
     * @return
     */
    public static String getAddressByIp(String ipAddress) {
        if (null == ipAddress || "".equals(ipAddress)) {
            return "";
        }
        try {
            String addressRes = HttpUtil.get(MessageFormat.format(ADDRESS_URL_WHOIS, ipAddress), 3000);
            if (StringUtils.isBlank(addressRes)) {
                String apiGetAddrRes = HttpUtil.get(MessageFormat.format(ADDRESS_URL_API, ipAddress), 3000);
                JSONObject jsonObj = JSON.parseObject(apiGetAddrRes);
                addressRes = (jsonObj.get("country") == null ? "" : jsonObj.get("country") + " ") +
                        (jsonObj.get("regionName") == null ? "" : jsonObj.get("regionName") + " ") +
                        (jsonObj.get("city") == null ? "" : jsonObj.get("city"));
            }
            if (StringUtils.isBlank(addressRes)) {
                addressRes = "";
            }
            return addressRes;
        } catch (Exception e) {
            log.error("getAddressByIp exception ", e);
            return "";
        }
    }
}