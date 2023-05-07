package com.project.common;

/**
 * 返回工具类
 *
 * @author 下水道的小老鼠
 */
public class ResultUtils {

    /**
     * 成功
     * @param data 数据
     * @return 通用返回类
     * @param <T> 类型
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"OK","无业务上的错误");
    }

    /**
     *
     * @param code 数据
     * @param data 数据
     * @param message 数据
     * @param description 数据
     * @return 数据
     * @param <T> 数据
     */
    public static <T> BaseResponse<T> error(int code, T data, String message, String description){
        return new BaseResponse<>(code, data, message, description);
    }

}
