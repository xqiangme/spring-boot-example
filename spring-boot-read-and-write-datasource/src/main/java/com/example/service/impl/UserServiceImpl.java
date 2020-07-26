package com.example.service.impl;

import com.example.datasource.dynamic.DataSourceTypeEnum;
import com.example.datasource.annotation.TargetDataSource;
import com.example.entity.UserInfo;
import com.example.mapper.UserInfoMapper;
import com.example.service.UserService;
import com.example.web.bo.UserAddBO;
import com.example.web.bo.UserUpdateBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 程序员小强
 * @date 2020-07-26 00:52
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;


    /**
     * 新增人员
     * 指定使用主库
     *
     * @param addBO
     */
    @Override
    @TargetDataSource(DataSourceTypeEnum.MASTER)
    public void addUser(UserAddBO addBO) {
        logger.info("[ 新增人员 ] start param:{}", addBO);
        String userId = UUID.randomUUID().toString().replaceAll("-", "");

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserName(addBO.getUserName());
        userInfo.setRealName(addBO.getRealName());
        userInfo.setMobile(addBO.getMobile());
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setRemark(addBO.getRemark());
        //demo项目部分参数值写死
        userInfo.setUserPassword("123456");
        userInfo.setDelFlag(0);
        userInfoMapper.insert(userInfo);
        logger.info("[ 新增人员 ] end userId:{},userName:{}", userId, addBO.getUserName());
    }

    /**
     * 修改人员信息
     *
     * @param updateBO
     */
    @Override
    @TargetDataSource(DataSourceTypeEnum.MASTER)
    public void updateUser(UserUpdateBO updateBO) {
        logger.info("[ 修改人员信息 ] start param:{}", updateBO);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(updateBO.getUserId());
        userInfo.setUserName(updateBO.getUserName());
        userInfo.setRealName(updateBO.getRealName());
        userInfo.setMobile(updateBO.getMobile());
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setRemark(updateBO.getRemark());
        userInfoMapper.updateByUserIdSelective(userInfo);
        logger.info("[ 修改人员信息 ] end userId:{},userName:{}", updateBO.getUserId(), updateBO.getUserName());
    }

    /**
     * 查询所有 人员信息
     *
     * @return 人员信息 列表
     */
    @Override
    @TargetDataSource(DataSourceTypeEnum.SLAVE1)
    public List<UserInfo> getAll() {
        logger.info("[ 查询所有人员列表 ] 指定使用从库1 ");
        return userInfoMapper.getAll();
    }

    /**
     * 根据业务主键ID查询
     *
     * @param userId 业务主键
     */
    @Override
    @TargetDataSource(DataSourceTypeEnum.SLAVE2)
    public UserInfo getByUserId(String userId) {
        logger.info("[ 根据业务主键ID查询 ] 指定使用从库2 userId:{}", userId);
        return userInfoMapper.getByUserId(userId);
    }

}