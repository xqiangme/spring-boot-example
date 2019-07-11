package com.example.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockConstant {


    /**
     * 账户 -权限
     * key :账户名
     * value:拥有的权限
     */
    public static final Map<String, Set<String>> MOCK_ACCOUNT_MAP = new HashMap<>();


    static {

        Set<String> adminPermission = new HashSet<>();
        adminPermission.add("/index");
        adminPermission.add("/user/list");
        adminPermission.add("/user/add");
        adminPermission.add("/user/del");
        adminPermission.add("/user/detail");
        adminPermission.add("/login/getUserInfo");

        MOCK_ACCOUNT_MAP.put("admin", adminPermission);


        Set<String> zhangsanPermission = new HashSet<>();
        zhangsanPermission.add("/index");
        zhangsanPermission.add("/user/list");
        zhangsanPermission.add("/user/detail");
        zhangsanPermission.add("/login/getUserInfo");
        MOCK_ACCOUNT_MAP.put("zhangsan", zhangsanPermission);

        Set<String> lisiPermission = new HashSet<>();
        lisiPermission.add("/index");
        lisiPermission.add("/user/list");
        lisiPermission.add("/user/add");
        lisiPermission.add("/user/del");
        lisiPermission.add("/login/getUserInfo");
        MOCK_ACCOUNT_MAP.put("lisi", lisiPermission);
    }


}
