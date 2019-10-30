package com.example.newsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Article {
    @SerializedName("source")
    @Expose
    private Source source;

    @SerializedName("author")
    @Expose
    private Source author;

    @SerializedName("title")
    @Expose
    private Source title;

    @SerializedName("description")
    @Expose
    private Source description;

    @SerializedName("url")
    @Expose
    private Source url;

    @SerializedName("urlImage")
    @Expose
    private Source urlImage;

    @SerializedName("publishedAt")
    @Expose
    private Source publishedAt;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Source getAuthor() {
        return author;
    }

    public void setAuthor(Source author) {
        this.author = author;
    }

    public Source getTitle() {
        return title;
    }

    public void setTitle(Source title) {
        this.title = title;
    }

    public Source getDescription() {
        return description;
    }

    public void setDescription(Source description) {
        this.description = description;
    }

    public Source getUrl() {
        return url;
    }

    public void setUrl(Source url) {
        this.url = url;
    }

    public Source getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(Source urlImage) {
        this.urlImage = urlImage;
    }

    public Source getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Source publishedAt) {
        this.publishedAt = publishedAt;
    }
}
