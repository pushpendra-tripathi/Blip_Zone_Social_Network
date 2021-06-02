package com.starlord.blipzone.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BlogModel {
    int id;
    List<CommentModel> commentList;
    List<LikeModel> likeList;
    String createdAt;
    String lastUpdatedOn;
    String content;
    String imageUrl;
    int viewType;
    int user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CommentModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentModel> commentList) {
        this.commentList = commentList;
    }

    public List<LikeModel> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeModel> likeList) {
        this.likeList = likeList;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
