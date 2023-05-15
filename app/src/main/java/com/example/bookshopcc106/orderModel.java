package com.example.bookshopcc106;

public class orderModel {
    String paymentmethod,status,title,url,verify;
    Long price,quantity,total;

    orderModel(){
    }

    public orderModel(String paymentmethod, String status, String title, String url, String verify, Long price, Long quantity, Long total) {
        this.paymentmethod = paymentmethod;
        this.status = status;
        this.title = title;
        this.url = url;
        this.verify = verify;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
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

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
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
