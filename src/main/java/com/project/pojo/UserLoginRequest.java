package com.project.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求参数
 *
 * @author 下水道的小老鼠
 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;

    private String userPassword;
}
