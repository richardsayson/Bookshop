package com.example.bookshopcc106;

public class checkModel {

    public String title,paymentmethod,status,url;
    public Long price,quantity,total;
    checkModel(){}

    public checkModel(String title, String paymentmethod, String status, String url, Long price, Long quantity, Long total) {
        this.title = title;
        this.paymentmethod = paymentmethod;
        this.status = status;
        this.url = url;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
