package com.example.bookshopcc106;

public class cartModel {
    String title,url;
    Long price;

    cartModel(){}
    public cartModel(String title, String url, Long price) {
        this.title = title;
        this.url = url;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
