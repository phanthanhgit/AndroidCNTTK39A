package com.phanthanh.cnttk39a.model;

public class Comment {
    private int id;
    private String content;
    private int userid;
    private String username;
    private String avatar;

    public Comment(int id, String content, int userid, String username, String avatar) {
        this.id = id;
        this.content = content;
        this.userid = userid;
        this.username = username;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
