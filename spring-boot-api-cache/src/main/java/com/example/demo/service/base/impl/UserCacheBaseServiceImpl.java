package com.example.demo.service.base.impl;

import com.example.cache.annotation.CacheData;
import com.example.demo.bean.UserInfo;
import com.example.demo.param.UserListQueryBO;
import com.example.demo.param.UserListQueryBO2;
import com.example.demo.service.base.UserCacheBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户基本处理Service
 *
 * @author mengq
 * @version 1.0
 */
@Service
public class UserCacheBaseServiceImpl implements UserCacheBaseService {
    private static final Logger log = LoggerFactory.getLogger(UserCacheBaseServiceImpl.class);

    /**
     * 测试所需的模拟数据
     */
    private static Map<Integer, UserInfo> MOCK_USER_MAP = new HashMap<>();

    static {
        MOCK_USER_MAP.put(1, new UserInfo(1, "zhangsan", "张三", 1));
        MOCK_USER_MAP.put(2, new UserInfo(2, "lisi", "李四", 1));
        MOCK_USER_MAP.put(3, new UserInfo(3, "wangwu", "王五", 2));
        MOCK_USER_MAP.put(4, new UserInfo(4, "xiaoliu", "小六", 1));
        MOCK_USER_MAP.put(5, new UserInfo(5, "xiaoqi", "小七", 2));
        MOCK_USER_MAP.put(6, new UserInfo(6, "xiaoba", "小八", 0));
        MOCK_USER_MAP.put(7, new UserInfo(7, "nihao", "你好", 3));
        MOCK_USER_MAP.put(8, new UserInfo(8, "wohao", "我好", 1));
        MOCK_USER_MAP.put(9, new UserInfo(9, "tahao", "他好", 2));
        MOCK_USER_MAP.put(10, new UserInfo(10, "dajiahao", "大家好", 2));
        MOCK_USER_MAP.put(11, new UserInfo(11, "womenhao", "我们好", 1));
        MOCK_USER_MAP.put(12, new UserInfo(12, "feichanghao", "非常好", 3));
        MOCK_USER_MAP.put(13, new UserInfo(13, "xiaoxiao", "小小", 2));
        MOCK_USER_MAP.put(14, new UserInfo(14, "dada", "大大", 0));
        MOCK_USER_MAP.put(15, new UserInfo(15, "haha", "哈哈", 1));
    }

    /**
     * 无参数方法测试
     *
     * @return
     */
    @Override
    @CacheData(keyPrefix = "userListAll")
    public List<UserInfo> listAll() {
        log.info("UserCacheBaseServiceImpl >> listAll");


        //睡眠-模拟查询时间
        this.sleep(500);
        return new ArrayList<>(MOCK_USER_MAP.values());
    }

    /**
     * 根据用户ID查询
     *
     * @param id
     */
    @Override
    @CacheData(expireTime = 20 * 60)
    public UserInfo getUserById(Integer id) {
        log.info("UserCacheBaseServiceImpl >> getUserById id:{}", id);
        if (null == id) {
            return null;
        }
        //睡眠-模拟查询时间
        this.sleep(1000);
        return MOCK_USER_MAP.get(id);
    }

    /**
     * 根据用户ID和状态查询
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @CacheData(expireTime = 25 * 60, storageNullFlag = false)
    public UserInfo getUserByIdAndStatus(Integer id, Integer status) {
        log.info("UserCacheBaseServiceImpl >> getUserByIdAndStatus id:{},status:{}", id, status);
        if (null == id) {
            return null;
        }
        //睡眠-模拟查询时间
        this.sleep(1000);
        UserInfo userInfo = MOCK_USER_MAP.get(id);
        if (userInfo.getStatus().equals(status)) {
            return userInfo;
        }
        return null;
    }

    /**
     * 根据用户ID查询-可存储null值
     *
     * @param id
     * @return
     */
    @Override
    @CacheData
    public UserInfo getUserByIdStorageNull(Integer id) {
        return this.getUserById(id);
    }

    /**
     * 普通参数测试-分页查询用户
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    @CacheData(expireTime = 20 * 60)
    public List<UserInfo> listPageUser(Integer page, Integer pageSize) {
        List<UserInfo> userInfoList = new ArrayList<>(MOCK_USER_MAP.values());
        //计算偏移
        int start = pageSize * (page - 1);
        int end = start + pageSize;
        if (end > userInfoList.size()) {
            end = userInfoList.size();
        }
        log.info("UserCacheBaseServiceImpl >> listPageUser page:{},pageSize:{},start:{},end:{}", page, +pageSize, start, +end);

        //睡眠-模拟查询时间
        this.sleep(2000);
        List<UserInfo> subUserList = userInfoList.subList(start, end);

        return new ArrayList<>(subUserList);
    }

    /**
     * 对象参数测试-分页查询用户
     *
     * @param queryBO
     * @return
     */
    @Override
    @CacheData(expireTime = 20 * 60)
    public List<UserInfo> listPageUserByObjParam(UserListQueryBO queryBO) {
        int page = queryBO.getPage();
        int pageSize = queryBO.getPageSize();

        //模拟程序查询
        List<UserInfo> userInfoList = new ArrayList<>(MOCK_USER_MAP.size());
        UserInfo mapValue = null;
        boolean canReturnFlag = true;
        boolean userNameNotNullFlag = (null != queryBO.getUserName() && !"".equals(queryBO.getUserName()));
        boolean userStatusNotNullFlag = (null != queryBO.getStatus());
        for (Map.Entry<Integer, UserInfo> map : MOCK_USER_MAP.entrySet()) {
            mapValue = map.getValue();
            //检索条件都不为空
            if (userNameNotNullFlag && userStatusNotNullFlag) {
                canReturnFlag = mapValue.getUserName().contains(queryBO.getUserName()) && mapValue.getStatus().equals(queryBO.getStatus());
            } else if (userNameNotNullFlag) {
                //用户名称检索
                canReturnFlag = mapValue.getUserName().contains(queryBO.getUserName());
            } else if (userStatusNotNullFlag) {
                //用户状态检索
                canReturnFlag = mapValue.getStatus().equals(queryBO.getStatus());
            }
            if (!canReturnFlag) {
                continue;
            }
            userInfoList.add(mapValue);
        }
        //计算偏移
        int start = pageSize * (page - 1);
        int end = start + pageSize;
        if (end > userInfoList.size()) {
            end = userInfoList.size();
        }
        if (start > end) {
            start = end;
        }
        log.info("UserCacheBaseServiceImpl >> listPageUserByCondition page:" + page + " pageSize：" + pageSize + " start:" + start + " end:" + end);

        //睡眠-模拟查询时间
        this.sleep(2000);
        List<UserInfo> subUserList = userInfoList.subList(start, end);

        return new ArrayList<>(subUserList);
    }

    /**
     * 普通参数+对象参数测试-分页查询用户
     *
     * @param page
     * @param pageSize
     * @param queryBO
     * @return
     */
    @Override
    @CacheData(expireTime = 20 * 50)
    public List<UserInfo> listPageUserByObjAndParam(Integer page, Integer pageSize, UserListQueryBO2 queryBO) {
        UserListQueryBO listQueryBo = new UserListQueryBO();
        listQueryBo.setPage(page);
        listQueryBo.setPageSize(pageSize);
        listQueryBo.setUserName(queryBO.getUserName());
        listQueryBo.setStatus(queryBO.getStatus());
        return this.listPageUserByObjParam(listQueryBo);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.info("UserCacheBaseServiceImpl >> sleep  exception:", e);
        }
    }
}
