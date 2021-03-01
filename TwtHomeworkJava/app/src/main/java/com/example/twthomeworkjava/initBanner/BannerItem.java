package com.example.twthomeworkjava.initBanner;

public class BannerItem {
        private long news_id;
        private String url;
        private String thumbnail;
        private String title;
        public void setNews_id(long news_id) {
            this.news_id = news_id;
        }
        public long getNews_id() {
            return news_id;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
        public String getThumbnail() {
            return thumbnail;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

    }

