package com.example.twthomeworkjava;

import java.util.List;

public class NewsBean {

        private String date;
        private List<NewsItem> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<NewsItem> getStories() {
        return stories;
    }

    public void setStories(List<NewsItem> stories) {
        this.stories = stories;
    }
}

