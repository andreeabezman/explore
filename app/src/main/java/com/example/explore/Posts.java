package com.example.explore;

public class Posts {

    private String title;
    private String content;
    private String picture;
    private long date;
    private int postsid;
/*
    public Posts(String title, String content, String picture, int date, int postsid) {
        this.title = title;
        this.content = content;
        this.picture = picture;
        this.date = date;
        this.postsid = postsid;
    }

    public Posts() {

    }
*/
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPostsid() {
        return postsid;
    }

    public void setPostsid(int postsid) {
        this.postsid = postsid;
    }
}
