package com.project.mapper;

import com.project.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 下水道的小老鼠
* &#064;description  针对表【user(用户表
)】的数据库操作Mapper
* &#064;createDate  2023-05-03 16:22:48
* &#064;Entity  com.project.pojo.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




