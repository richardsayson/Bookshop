package com.example.bookshopcc106;

public class checkModel {

   public Long price,quantity;


   public String title,url;
    checkModel(){}

    public checkModel(Long price, Long quantity, String title, String url) {
        this.price = price;
        this.quantity = quantity;
        this.title = title;
        this.url = url;
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
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}