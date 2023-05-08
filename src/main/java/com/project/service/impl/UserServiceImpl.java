package com.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.common.BaseResponse;
import com.project.common.ErrorCode;
import com.project.common.ResultUtils;
import com.project.exception.BusinessException;
import com.project.pojo.User;
import com.project.service.UserService;
import com.project.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.contant.UserStaticNumber.USER_LOGIN_STATE;

/**
* @author 下水道的小老鼠
* &#064;description  针对表【user(用户表
)】的数据库操作Service实现
* &#064;createDate  2023-05-03 16:22:48
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final String SALT = "password";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.用户校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号，密码或者校验密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");

        }


        //校验编码
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        if(planetCode.length() != 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验编码过长或者过短");
        }
        if(!simpleDateFormat.format(date).equals(planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "校验编码错误");
        }
        //不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(userAccount);
        /*
          判断是否含有特殊字符
           @param username
         * @return true为包含，false为不包含
         */
        if (m.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名含有特殊字符！");
        }

        //用户名不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名太热门，请更换！");

        }

        //校验两次密码
        if(! userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不相同");

        }

        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        //为用户设置默认头像
        user.setAvatarUrl("https://pngimg.com/uploads/github/github_PNG40.png");
        boolean saveResult = this.save(user);
        if (!saveResult) {
            log.error("数据库插入错误，请尽快联系代码维护工作人员!!!");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统繁忙，请稍等");

        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.用户校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码过短");
        }

        //不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(userAccount);
        /*
          判断是否含有特殊字符
           @param username
         * @return true为包含，false为不包含
         */
        if (m.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名包含特殊字符");
        }

        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //用户是否已经注册,数据是否在数据库中
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(queryWrapper);
        if(user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在！是否已经注册给账号？");
        }
        //3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }
    @Override
    public User getSafetyUser(User originUser){
        if(originUser == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"脱敏用户参数为空");
        }
        User safetyUser = originUser;
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }

    @Override
    public int userLoginOut(HttpServletRequest request){
        //移除session
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




