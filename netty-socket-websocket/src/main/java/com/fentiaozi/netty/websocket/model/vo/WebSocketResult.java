/**
 * Copyright (c) 2018 visi开源 All rights reserved.
 * <p>
 * https://www.visi.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.fentiaozi.netty.websocket.model.vo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author keliang
 * @since 1.0.0
 */

public class WebSocketResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编码：0表示成功，其他值表示失败
     */
    private int code = 0;
    /**
     * 消息内容
     */
    private String msg = "success";
    private int method = 0;

    /**
     * 消息内容
     */
    private String methodDesc = "";

    /**
     * 响应数据
     */
    private T data;

    public WebSocketResult<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public WebSocketResult<T> ok(T data, int method, String methodDesc) {
        this.setData(data);
        this.method = method;
        this.methodDesc = methodDesc;
        return this;
    }

    public boolean success() {
        return code == 0 ? true : false;
    }

    public WebSocketResult<T> error() {
        this.code = 500;
        this.msg = "服务器内部异常";
        return this;
    }

    public WebSocketResult<T> error(int code) {
        this.code = code;
        this.msg = "服务器内部异常";
        return this;
    }

    public WebSocketResult<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public WebSocketResult<T> error(String msg) {
        this.code = 500;
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 返回JSON字符串
     */
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
