package com.example.demo.response;

import com.example.demo.eunm.ResultEnum;

import java.util.List;

/**
 * @author MG02004
 * @createTime 2022/10/8 17:34
 * @description
 */
public class Result<T> {

    private int code;

    private String msg;

    private List<T> list;

    private Long time;

    public Result(int code, String msg, List<T> list) {
        this.code = code;
        this.msg = msg;
        this.list = list;
        this.time = System.currentTimeMillis();
    }

    public static Result BuildResponseResult(int code, String msg, List list){
        Result result = new Result(code, msg, list);
        return result;
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
