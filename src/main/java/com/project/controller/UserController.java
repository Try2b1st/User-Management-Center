package com.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.pojo.User;
import com.project.pojo.UserLoginRequest;
import com.project.pojo.UserRegisterRequest;
import com.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.contant.UserStaticNumber.ADMIN_ROLE;
import static com.project.contant.UserStaticNumber.USER_LOGIN_STATE;

/**
 * @author 下水道的小老鼠
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 请求包装类
     * @return 是否成功
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String userCheckPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, userCheckPassword)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, userCheckPassword);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 请求包装类
     * @param request          session
     * @return 返回脱敏后的用户数据
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 管理员权限 查询用户信息
     *
     * @param username 用户名
     * @param request  请求参数
     * @return 返回 用户信息
     */
    @GetMapping("/search")
    public List<User> searchUser(String username, HttpServletRequest request) {
        if(isNotAdminRole(request)){
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username", username);
        }

        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    /**
     * 管理员权限 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if(isNotAdminRole(request)){
            return false;
        }
        if (id < 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 判断用户是否为管理院
     *
     * @param request 回去用户登录状态
     * @return 返回boolean
     */
    private boolean isNotAdminRole(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user == null || user.getUserRole() != ADMIN_ROLE;
    }

}
