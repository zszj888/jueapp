package com.somecom.demo.model;

public class ResultVo {
    Integer ret;
    String msg;
    Object data;


    public static ResultVo err(Object data) {
        ResultVo v = new ResultVo();
        v.setData(data);
        v.setRet(500);
        v.setMsg("failed");
        return v;
    }

    public static ResultVo ok(Object data) {
        ResultVo v = new ResultVo();
        v.setData(data);
        v.setRet(200);
        v.setMsg("success");
        return v;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
