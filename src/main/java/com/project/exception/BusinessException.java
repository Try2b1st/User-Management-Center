package com.project.exception;

import com.project.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author 下水道的小老鼠
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final String description;

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
