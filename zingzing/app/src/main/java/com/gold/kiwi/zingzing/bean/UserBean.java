package com.gold.kiwi.zingzing.bean;

public class UserBean {
    String user_seq;
    String user_id;
    String user_name;
    String user_pw;
    String sys_date;

    @Override
    public String toString() {
        return "UserBean{" +
                "user_seq='" + user_seq + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_pw='" + user_pw + '\'' +
                ", sys_date='" + sys_date + '\'' +
                '}';
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_seq() {
        return user_seq;
    }

    public void setUser_seq(String user_seq) {
        this.user_seq = user_seq;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSys_date() {
        return sys_date;
    }

    public void setSys_date(String sys_date) {
        this.sys_date = sys_date;
    }
}
