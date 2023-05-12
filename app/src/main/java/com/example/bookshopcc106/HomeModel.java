package com.example.bookshopcc106;

public class HomeModel {
    private String title,author;
    private Long price;
    private String url;

    public HomeModel(String title, String author, Long price, String url) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.url = url;

    }

    HomeModel(){

    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Long getPrice() {
        return price;
    }
    public String getUrl() {
        return url;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public void setUrl(String url) {
        this.url = url;
    }
}
