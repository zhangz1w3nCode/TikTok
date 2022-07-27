package com.java.pojo;

import java.util.Date;

public class Comment {
    private String id;

    private String vlogerId;

    private String fatherCommentId;

    private String vlogId;

    private String commentUserId;

    private String content;

    private Integer likeCounts;

    private Date createTime;

    public Comment(String id, String vlogerId, String fatherCommentId, String vlogId, String commentUserId, String content, Integer likeCounts, Date createTime) {
        this.id = id;
        this.vlogerId = vlogerId;
        this.fatherCommentId = fatherCommentId;
        this.vlogId = vlogId;
        this.commentUserId = commentUserId;
        this.content = content;
        this.likeCounts = likeCounts;
        this.createTime = createTime;
    }

    public Comment() {
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

    public String getFatherCommentId() {
        return fatherCommentId;
    }

    public void setFatherCommentId(String fatherCommentId) {
        this.fatherCommentId = fatherCommentId == null ? null : fatherCommentId.trim();
    }

    public String getVlogId() {
        return vlogId;
    }

    public void setVlogId(String vlogId) {
        this.vlogId = vlogId == null ? null : vlogId.trim();
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId == null ? null : commentUserId.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(Integer likeCounts) {
        this.likeCounts = likeCounts;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}