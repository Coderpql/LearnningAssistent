package com.learning.wow.learningassistence.db;

import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by wow on 2017/10/8.
 */

public class Note extends DataSupport{
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    private String title;
    private String mDate;
    private String data;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDate() {

        return mDate;
    }

    public String getData() {
        return data;
    }

    public String getTitle() {

        return title;
    }
}
