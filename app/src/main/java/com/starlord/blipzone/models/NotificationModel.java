package com.starlord.blipzone.models;

public class NotificationModel {
    int type;
    String content;
    String notifierUserName;
    String notifierUserImage;
    int notifierUserId;
    int postId;
    String postImageUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotifierUserName() {
        return notifierUserName;
    }

    public void setNotifierUserName(String notifierUserName) {
        this.notifierUserName = notifierUserName;
    }

    public String getNotifierUserImage() {
        return notifierUserImage;
    }

    public void setNotifierUserImage(String notifierUserImage) {
        this.notifierUserImage = notifierUserImage;
    }

    public int getNotifierUserId() {
        return notifierUserId;
    }

    public void setNotifierUserId(int notifierUserId) {
        this.notifierUserId = notifierUserId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }
}
