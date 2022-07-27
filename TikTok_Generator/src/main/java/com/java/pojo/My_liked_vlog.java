package com.java.pojo;

public class My_liked_vlog {
    private String id;

    private String userId;

    private String vlogId;

    public My_liked_vlog(String id, String userId, String vlogId) {
        this.id = id;
        this.userId = userId;
        this.vlogId = vlogId;
    }

    public My_liked_vlog() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getVlogId() {
        return vlogId;
    }

    public void setVlogId(String vlogId) {
        this.vlogId = vlogId == null ? null : vlogId.trim();
    }
}