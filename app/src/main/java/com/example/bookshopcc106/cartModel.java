package com.example.bookshopcc106;

public class cartModel {

   public Long price,quantity,total;
    public String title,url;
    cartModel(){}


    public cartModel(Long price, Long quantity, Long total, String title, String url) {
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.title = title;
        this.url = url;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
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
