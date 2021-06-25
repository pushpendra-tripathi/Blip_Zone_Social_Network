package com.starlord.blipzone.models;

import java.io.Serializable;
import java.util.List;

public class BlogModel implements Serializable {
    int id;
    List<CommentModel> commentList;
    LikeModel likeModel;
    int commentCount;
    boolean isLiked;
    boolean owner;
    UserModel userModel;
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

    public LikeModel getLikeModel() {
        return likeModel;
    }

    public void setLikeModel(LikeModel likeModel) {
        this.likeModel = likeModel;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
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
