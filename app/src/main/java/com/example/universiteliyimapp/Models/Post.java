package com.example.universiteliyimapp.Models;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String title;
    private  String description;
    private String picture;
    private String userId;
    private String userPhoto;
    private String categories;
    private Object timeStamp;
    private  String DisplayName;


    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public Post(String title, String description, String picture, String userId, String userPhoto, String categories,String DisplayName) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.categories = categories;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.DisplayName=DisplayName;
    }

    public Post() {


    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getCategories() {
        return categories;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
