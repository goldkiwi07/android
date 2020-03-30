package com.gold.kiwi.zingzing.bean;

public class ResultBean {
    Boolean result;
    String msg;

    @Override
    public String toString() {
        return "ResultBean{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
