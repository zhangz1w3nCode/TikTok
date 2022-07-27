package com.java.pojo;

public class Fans {
    private String id;

    private String vlogerId;

    private String fanId;

    private Integer isFanFriendOfMine;

    public Fans(String id, String vlogerId, String fanId, Integer isFanFriendOfMine) {
        this.id = id;
        this.vlogerId = vlogerId;
        this.fanId = fanId;
        this.isFanFriendOfMine = isFanFriendOfMine;
    }

    public Fans() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getVlogerId() {
        return vlogerId;
    }

    public void setVlogerId(String vlogerId) {
        this.vlogerId = vlogerId == null ? null : vlogerId.trim();
    }

    public String getFanId() {
        return fanId;
    }

    public void setFanId(String fanId) {
        this.fanId = fanId == null ? null : fanId.trim();
    }

    public Integer getIsFanFriendOfMine() {
        return isFanFriendOfMine;
    }

    public void setIsFanFriendOfMine(Integer isFanFriendOfMine) {
        this.isFanFriendOfMine = isFanFriendOfMine;
    }
}