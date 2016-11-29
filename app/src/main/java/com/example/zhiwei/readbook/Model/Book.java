package com.example.zhiwei.readbook.Model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhiwei on 2016/5/20.
 */
public class Book extends DataSupport implements Serializable {

    private int id;
    private String name;
    private String author;
    private int pages;
    private int readPages;
    private String coverFile;
    private Date addDate;
    private Date finishDate;
    private boolean isFinish;
    private List<Exception> exceptionList = new ArrayList<>();

    public Book() {

    }

    public Book(String name, String author, int pages, int readPages, String coverFile) {
        this.name = name;
        this.author = author;
        this.pages = pages;
        this.readPages = readPages;
        this.coverFile = coverFile;
        addDate = new Date(System.currentTimeMillis());
        isFinish = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getReadPages() {
        return readPages;
    }

    public void setReadPages(int readPages) {
        this.readPages = readPages;
    }

    public String getCoverFile() {
        return coverFile;
    }

    public void setCoverFile(String coverFile) {
        this.coverFile = coverFile;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public List<Exception> getExceptionList() {
        return exceptionList;
    }

    public void setExceptionList(List<Exception> exceptionList) {
        this.exceptionList = exceptionList;
    }
}
