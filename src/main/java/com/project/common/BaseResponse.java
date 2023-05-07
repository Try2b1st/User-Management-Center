package com.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 基本地返回对象类型
 *
 * @author 下水道的小老鼠
 */
@Data
public class BaseResponse<T> implements Serializable {
    private Integer code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(Integer code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
