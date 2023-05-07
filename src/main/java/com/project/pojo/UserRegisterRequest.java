package com.project.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求参数
 *
 * @author 下水道的小老鼠
 */
@Data
public class UserRegisterRequest implements Serializable {
    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
