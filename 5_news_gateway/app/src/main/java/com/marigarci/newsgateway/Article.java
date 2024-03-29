package com.marigarci.newsgateway;

import java.io.Serializable;

public class Article implements Serializable {
    private String author;
    private String title;
    private String description;
    private String urlToImage;
    private String publishedAt;
    private String urlToArticle;

    public Article(String author, String title, String description, String urlToImage, String publishedAt, String urlToArticle) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.urlToArticle = urlToArticle;
    }

    public String getAuthor() { return author;}
    public String getTitle() {
        return title;
    }
    public String getUrlToArticle() {
        return urlToArticle;
    }
    public String getDescription() {
        return description;
    }
    public String getUrlToImage() {
        return urlToImage;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
    public String getPublishedAt() {
        return publishedAt;
    }
    public void setUrlToArticle(String urlToArticle) {
        this.urlToArticle = urlToArticle;
    }

}

