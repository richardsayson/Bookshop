package com.example.bookshopcc106;

public class cartModel {

   public Long price,quantity,totalAmount;

    public Long getTotalAmount() {
        return totalAmount;
    }

    public cartModel(Long price, Long quantity, Long totalAmount, String title, String url) {
        this.price = price;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.title = title;
        this.url = url;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String title,url;
    cartModel(){}

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
