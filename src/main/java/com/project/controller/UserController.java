package com.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.common.BaseResponse;
import com.project.common.ErrorCode;
import com.project.common.ResultUtils;
import com.project.exception.BusinessException;
import com.project.pojo.User;
import com.project.pojo.UserLoginRequest;
import com.project.pojo.UserRegisterRequest;
import com.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String userCheckPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        if (StringUtils.isAnyBlank(userAccount, userPassword, userCheckPassword, planetCode)) {
            return new BaseResponse<>(1, null, "data have null");
        }

        Long result = userService.userRegister(userAccount, userPassword, userCheckPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 请求包装类
     * @param request          session
     * @return 返回脱敏后的用户数据
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户退出登录
     *
     * @param request 请求行
     * @return 没有用处（目前）
     */
    @PostMapping("loginOut")
    public BaseResponse<Integer> userLoginOut(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return ResultUtils.success(userService.userLoginOut(request));
    }

    /**
     * 管理员权限 查询用户信息
     *
     * @param username 用户名
     * @param request  请求参数
     * @return 返回 用户信息
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        if (isNotAdminRole(request)) {
            return null;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username", username);
        }

        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 当登陆后，返回用户在数据库中的信息（脱敏）
     *
     * @param request 我们将用户信息放到session中
     * @return 返回用户在数据库中的信息（脱敏）
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object objUser = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) objUser;
        if (currentUser == null) {
            return null;
        }
        User user = userService.getById(currentUser.getId());
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    /**
     * 管理员权限 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (isNotAdminRole(request)) {
            return null;
        }
        if (id < 0) {
            return null;
        }
        Boolean result = userService.removeById(id);
        return ResultUtils.success(result);
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
