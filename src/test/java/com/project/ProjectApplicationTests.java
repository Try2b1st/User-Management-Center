package com.project;

import com.project.pojo.User;
import com.project.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class ProjectApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setId(0L);
        user.setUsername("dog");
        user.setUserAccount("root");
        user.setAvatarUrl("https://cn.bing.com/images/search?q=%E5%A4%B4%E5%83%8F%E5%9B%BE%E7%89%87&FORM=IQFRBA&id=294EC054FF50E8CDA64277289E2B4E18466EE9E5");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("1234567890");
        user.setEmail("1111111111");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void testRegister(){
        String userAccount = "bilibili";
        String userPassword = "";
        String checkPassword = "123456789";

        //判空
        long result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        //账号长度
        userAccount = "sb";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        //密码长度
        userPassword = "123456";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        //判断账号是否有特殊字符
        userAccount = "bili bili";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        //两次密码不相同
        userPassword = "222233338";
        checkPassword = "5555555555";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        //账号以注册过
        userAccount = "root";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        //正常插入
        userAccount = "bilibili";
        userPassword = "222233335";
        checkPassword = "222233335";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertTrue(result > 0);
    }

}
