package com.example.web.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生表
 *
 * @author edz
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
@ContentStyle(borderLeft = BorderStyle.THIN, borderRight = BorderStyle.THIN, borderTop = BorderStyle.THIN, borderBottom = BorderStyle.THIN)
public class StudentExportVO {

    /**
     * 主键id
     */
    @ExcelProperty("自增ID")
    private Integer id;

    /**
     * 名称
     */
    @ExcelProperty("名称")
    private String name;

    /**
     * 班级ID
     */
    @ExcelProperty("班级ID")
    private String classId;

    /**
     * 学生ID
     */
    @ExcelProperty("学生ID")
    private String studentId;

    /**
     * 年龄
     */
    @ExcelProperty("年龄")
    private Integer age;

    /**
     * 性别 0-未知, 1-男,2-女
     */
    @ExcelProperty("性别")
    private Integer sex;

    /**
     * 入学时间 yyyyMMdd
     */
    @ExcelProperty("入学时间")
    private Integer schoolDate;

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
     * 成绩进步率 示例 2.01
     */
    @ExcelProperty("成绩进步率")
    private BigDecimal progressRate;


}
