package com.project.service;

import com.project.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 下水道的小老鼠
 * &#064;description  针对表【user(用户表
 * )】的数据库操作Service
 * &#064;createDate  2023-05-03 16:22:48
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param username      用户名
     * @param userPassword  密码
     * @param checkPassword 校验密码
     * @param planetCode    校验编号
     * @return id
     */
    long userRegister(String username, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @param request      请求体
     * @return 返回登录对象（脱敏 隐藏敏感信息）
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户信息脱敏
     *
     * @param originUser 原本的用户信息
     * @return 返回脱敏后的用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 用户退出登录
     *
     * @param request 获取session
     * @return int
     */
    int userLoginOut(HttpServletRequest request);
}
