package com.example.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The table 学生表
 */
public class StudentInfo {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 班级ID
     */
    private String classId;

    /**
     * 学生ID
     */
    private String studentId;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别 0-未知, 1-男,2-女 
     */
    private Integer sex;

    /**
     * 入学时间 yyyyMMdd
     */
    private Integer schoolDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 成绩进步率 示例 2.01
     */
    private BigDecimal progressRate;


    /**
     * 主键id
     */
    public void setId(Integer id){
        this.id = id;
    }

    /**
     * 主键id
     *
     * @return the string
     */
    public Integer getId(){
        return id;
    }

    /**
     * 名称
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 名称
     *
     * @return the string
     */
    public String getName(){
        return name;
    }

    /**
     * 班级ID
     */
    public void setClassId(String classId){
        this.classId = classId;
    }

    /**
     * 班级ID
     *
     * @return the string
     */
    public String getClassId(){
        return classId;
    }

    /**
     * 学生ID
     */
    public void setStudentId(String studentId){
        this.studentId = studentId;
    }

    /**
     * 学生ID
     *
     * @return the string
     */
    public String getStudentId(){
        return studentId;
    }

    /**
     * 年龄
     */
    public void setAge(Integer age){
        this.age = age;
    }

    /**
     * 年龄
     *
     * @return the string
     */
    public Integer getAge(){
        return age;
    }

    /**
     * 性别 0-未知, 1-男,2-女 
     */
    public void setSex(Integer sex){
        this.sex = sex;
    }

    /**
     * 性别 0-未知, 1-男,2-女 
     *
     * @return the string
     */
    public Integer getSex(){
        return sex;
    }

    /**
     * 入学时间 yyyyMMdd
     */
    public void setSchoolDate(Integer schoolDate){
        this.schoolDate = schoolDate;
    }

    /**
     * 入学时间 yyyyMMdd
     *
     * @return the string
     */
    public Integer getSchoolDate(){
        return schoolDate;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 创建时间
     *
     * @return the string
     */
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 修改时间
     */
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }

    /**
     * 修改时间
     *
     * @return the string
     */
    public Date getUpdateTime(){
        return updateTime;
    }

    /**
     * 成绩进步率 示例 2.01
     */
    public void setProgressRate(BigDecimal progressRate){
        this.progressRate = progressRate;
    }

    /**
     * 成绩进步率 示例 2.01
     *
     * @return the string
     */
    public BigDecimal getProgressRate(){
        return progressRate;
    }
}
