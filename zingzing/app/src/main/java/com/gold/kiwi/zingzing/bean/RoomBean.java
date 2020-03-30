package com.gold.kiwi.zingzing.bean;

public class RoomBean {
    int room_seq;
    String room_name;
    String room_pw;
    String user_name;
    String sys_date;
    String is_room;

    public String getIs_room() {
        return is_room;
    }

    public void setIs_room(String is_room) {
        this.is_room = is_room;
    }

    public int getRoom_seq() {
        return room_seq;
    }

    public void setRoom_seq(int room_seq) {
        this.room_seq = room_seq;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_pw() {
        return room_pw;
    }

    public void setRoom_pw(String room_pw) {
        this.room_pw = room_pw;
    }

    public String getSys_date() {
        return sys_date;
    }

    public void setSys_date(String sys_date) {
        this.sys_date = sys_date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
