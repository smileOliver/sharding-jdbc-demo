package com.harmony.shardingjdbc.base;

import java.io.Serializable;


public class BaseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    public BaseResult() {
    }

    public BaseResult(final int code, final String msg, final T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> BaseResult<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, null);
    }

    public static <T> BaseResult<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, null);
    }

    public static <T> BaseResult<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg);
    }

    public static <T> BaseResult<T> failed() {
        return restResult(null, CommonConstants.FAIL, null);
    }

    public static <T> BaseResult<T> failed(String msg) {
        return restResult(null, CommonConstants.FAIL, msg);
    }

    public static <T> BaseResult<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, null);
    }

    public static <T> BaseResult<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg);
    }

    private static <T> BaseResult<T> restResult(T data, int code, String msg) {
        BaseResult<T> apiResult = new BaseResult();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static <T> BaseResult.BaseResultBuilder<T> builder() {
        return new BaseResult.BaseResultBuilder();
    }

    public String toString() {
        int var10000 = this.getCode();
        return "BaseResult(code=" + var10000 + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public int getCode() {
        return this.code;
    }

    public BaseResult<T> setCode(final int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public BaseResult<T> setMsg(final String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public BaseResult<T> setData(final T data) {
        this.data = data;
        return this;
    }

    public static class BaseResultBuilder<T> {
        private int code;
        private String msg;
        private T data;

        BaseResultBuilder() {
        }

        public BaseResult.BaseResultBuilder<T> code(final int code) {
            this.code = code;
            return this;
        }

        public BaseResult.BaseResultBuilder<T> msg(final String msg) {
            this.msg = msg;
            return this;
        }

        public BaseResult.BaseResultBuilder<T> data(final T data) {
            this.data = data;
            return this;
        }

        public BaseResult<T> build() {
            return new BaseResult(this.code, this.msg, this.data);
        }

        public String toString() {
            return "BaseResult.BaseResultBuilder(code=" + this.code + ", msg=" + this.msg + ", data=" + this.data + ")";
        }
    }
}

