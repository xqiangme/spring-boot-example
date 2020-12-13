package com.example.service.impl;

import com.example.comment.ExcelExportComment;
import com.example.common.enums.ExcelExportBizTypeEnum;
import com.example.dao.entity.UserInfo;
import com.example.dao.mapper.UserInfoMapper;
import com.example.dao.query.UserInfoQuery;
import com.example.service.UserService;
import com.example.util.BeanCopierUtil;
import com.example.util.PageUtils;
import com.example.web.param.UserInfoParam;
import com.example.web.vo.UserInfoExportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息-使用通用导出Demo
 *
 * @author 程序员小强
 * @date 2020-11-06 20:56
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoMapper userInfo1Mapper;
    @Resource
    private ExcelExportComment excelExportComment;


    @Override
    public Integer exportUserList(UserInfoParam param) {
        //调用通用导出组件-返回任务ID，异步执行分页导出
        return excelExportComment.invoke((a) -> exportUser(param), param, ExcelExportBizTypeEnum.USER_INFO, UserInfoExportVO.class);
    }


    /**
     * 写好分页接口
     *
     * @param param
     */
    private List<UserInfoExportVO> exportUser(UserInfoParam param) {
        UserInfoQuery userInfoQuery = new UserInfoQuery();
        userInfoQuery.setStartRow(PageUtils.getStartRow(param.getPage(), param.getPageSize()));
        userInfoQuery.setPageSize(param.getPageSize());
        userInfoQuery.setMobile(param.getMobile());

        List<UserInfo> userInfoList = userInfo1Mapper.listPageByCondition(userInfoQuery);
        if (CollectionUtils.isEmpty(userInfoList)) {
            return new ArrayList<>(0);
        }

        return BeanCopierUtil.copyList(userInfoList, UserInfoExportVO.class);
    }
}
