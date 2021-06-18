package com.starlord.blipzone.models;

import java.util.List;

public class UserModel {
    int id;
    String userName;
    String firstName;
    String lastName;
    String profileImage;
    String about;
    boolean isActive;
    boolean blueTick;
    int followers;
    int following;
    List<BlogModel> blogList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isBlueTick() {
        return blueTick;
    }

    public void setBlueTick(boolean blueTick) {
        this.blueTick = blueTick;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public List<BlogModel> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<BlogModel> blogList) {
        this.blogList = blogList;
    }
}
