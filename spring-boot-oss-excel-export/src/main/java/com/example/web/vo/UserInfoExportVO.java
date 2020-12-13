package com.example.web.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.util.Date;

/**
 * 用户信息导出示例
 *
 * @author 程序员小强
 * @date 2020-11-06 20:57
 */
@Data
//头部标题行高（一个汉字大约18,超过长度会换行展示标题）
@HeadRowHeight(18)
//列宽(一个汉字大约长度2)
@ColumnWidth(20)
//头字体设置成12 bold是否加粗
@HeadFontStyle(fontHeightInPoints = 12, bold = false)
//内容字体设置成11
@ContentFontStyle(fontHeightInPoints = 12)
//内容全边框
@ContentStyle(borderLeft = BorderStyle.THIN, borderRight = BorderStyle.THIN, borderTop = BorderStyle.THIN, borderBottom = BorderStyle.THIN)
public class UserInfoExportVO {

    private Integer id;

    /**
     * 用户id
     */
    @ExcelProperty("用户id")
    private String userId;

    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String userName;

    /**
     * 密码
     */
    @ExcelProperty("密码")
    private String userPassword;

    /**
     * 真实姓名
     */
    @ExcelProperty("真实姓名")
    private String realName;

    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    private String mobile;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ExcelProperty("修改时间")
    private Date updateTime;

    /**
     * remark
     */
    private String remark;
}
