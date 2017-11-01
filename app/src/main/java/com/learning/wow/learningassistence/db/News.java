package com.learning.wow.learningassistence.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wow on 2017/10/10.
 */

public class News extends DataSupport {
    private String title;
    private String text;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }
}
