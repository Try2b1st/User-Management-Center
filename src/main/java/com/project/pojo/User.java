package com.project.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * &#064;TableName  user
 * @author 下水道的小老鼠
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id

     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登陆账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 逻辑删除 0=存在
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色  0 -- 普通用户  1 -- 管理员
     */
    private Integer userRole;

    /**
     * 校验编号 没想好怎么设计
     */
    private  String planetCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}