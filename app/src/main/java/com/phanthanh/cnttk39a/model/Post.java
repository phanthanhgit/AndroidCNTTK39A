package com.phanthanh.cnttk39a.model;

public class Post {
    private int id;
    private String title;
    private String content;
    private String tag;
    private String type;
    private int userid;
    private long view;
    private String createdat;
    private String username;
    private String avatar;

    public Post(int id, String title, String content, String tag, String type, int userid, long view, String createdat, String username, String avatar) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.type = type;
        this.userid = userid;
        this.view = view;
        this.createdat = createdat;
        this.username = username;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public long getView() {
        return view;
    }

    public void setView(long view) {
        this.view = view;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
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
