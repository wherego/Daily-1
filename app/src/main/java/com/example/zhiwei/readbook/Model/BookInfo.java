package com.example.zhiwei.readbook.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by zhiwei on 2016/5/23.
 */
public class BookInfo implements Serializable {
    private String id;          //豆瓣图书库中id
    private String title;       //标题
    private String author;      //作者
    private String coverUrl;    //封面图片地址
    private Bitmap cover;       //封面图片
    private int pages;          //页数
    private String summary;     //简介

    public BookInfo(String id, String title, String author, String coverUrl, Bitmap cover, int pages, String summary) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.cover = cover;
        this.pages = pages;
        this.summary = summary;
    }

    public BookInfo(String id, String title, String author, String coverUrl, int pages, String summary) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.pages = pages;
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
