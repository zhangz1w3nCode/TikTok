package com.java.pojo;

import java.util.Date;

public class Vlog {
    private String id;

    private String vlogerId;

    private String url;

    private String cover;

    private String title;

    private Integer width;

    private Integer height;

    private Integer likeCounts;

    private Integer commentsCounts;

    private Integer isPrivate;

    private Date createdTime;

    private Date updatedTime;

    public Vlog(String id, String vlogerId, String url, String cover, String title, Integer width, Integer height, Integer likeCounts, Integer commentsCounts, Integer isPrivate, Date createdTime, Date updatedTime) {
        this.id = id;
        this.vlogerId = vlogerId;
        this.url = url;
        this.cover = cover;
        this.title = title;
        this.width = width;
        this.height = height;
        this.likeCounts = likeCounts;
        this.commentsCounts = commentsCounts;
        this.isPrivate = isPrivate;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public Vlog() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(Integer likeCounts) {
        this.likeCounts = likeCounts;
    }

    public Integer getCommentsCounts() {
        return commentsCounts;
    }

    public void setCommentsCounts(Integer commentsCounts) {
        this.commentsCounts = commentsCounts;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}