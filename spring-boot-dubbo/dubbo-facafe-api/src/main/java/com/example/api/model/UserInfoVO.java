package com.example.api.model;

import lombok.*;
import java.io.Serializable;
/**
 * 参数类 - 人员
 *
 * @author 码农猿
 */
@Data
public class UserInfoVO implements Serializable {
    private Integer id;
    private String username;
    private String password;


    public UserInfoVO(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserInfoVO() {
    }

    @Override
    public String toString() {
        return "UserInfoVO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
