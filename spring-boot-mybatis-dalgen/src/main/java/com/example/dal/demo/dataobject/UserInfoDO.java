package com.example.dal.demo.dataobject;

import java.util.Date;

/**
 * The table 用户表
 */
public class UserInfoDO{

    /**
     * ID
     */
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户唯一标识
     */
    private String userId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 状态 1-启用 2-禁用 
     */
    private Integer status;

    /**
     * 删除标记 1-正常 2删除
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * ID
     */
    public void setId(Integer id){
        this.id = id;
    }

    /**
     * ID
     *
     * @return the string
     */
    public Integer getId(){
        return id;
    }

    /**
     * 邮箱
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * 邮箱
     *
     * @return the string
     */
    public String getEmail(){
        return email;
    }

    /**
     * 手机号码
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * 手机号码
     *
     * @return the string
     */
    public String getPhone(){
        return phone;
    }

    /**
     * 头像
     */
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    /**
     * 头像
     *
     * @return the string
     */
    public String getAvatar(){
        return avatar;
    }

    /**
     * 用户唯一标识
     */
    public void setUserId(String userId){
        this.userId = userId;
    }

    /**
     * 用户唯一标识
     *
     * @return the string
     */
    public String getUserId(){
        return userId;
    }

    /**
     * 备注
     */
    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    /**
     * 备注
     *
     * @return the string
     */
    public String getRemarks(){
        return remarks;
    }

    /**
     * 密码
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * 密码
     *
     * @return the string
     */
    public String getPassword(){
        return password;
    }

    /**
     * 真实姓名
     */
    public void setRealName(String realName){
        this.realName = realName;
    }

    /**
     * 真实姓名
     *
     * @return the string
     */
    public String getRealName(){
        return realName;
    }

    /**
     * 用户名
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * 用户名
     *
     * @return the string
     */
    public String getUsername(){
        return username;
    }

    /**
     * 状态 1-启用 2-禁用 
     */
    public void setStatus(Integer status){
        this.status = status;
    }

    /**
     * 状态 1-启用 2-禁用 
     *
     * @return the string
     */
    public Integer getStatus(){
        return status;
    }

    /**
     * 删除标记 1-正常 2删除
     */
    public void setDelFlag(Integer delFlag){
        this.delFlag = delFlag;
    }

    /**
     * 删除标记 1-正常 2删除
     *
     * @return the string
     */
    public Integer getDelFlag(){
        return delFlag;
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
     * 更新时间
     */
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }

    /**
     * 更新时间
     *
     * @return the string
     */
    public Date getUpdateTime(){
        return updateTime;
    }
}
