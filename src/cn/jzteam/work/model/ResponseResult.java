package cn.jzteam.work.model;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
    private int code = 0;
    private String msg = "";
    private String detailMsg = "";
    private T data;

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

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
