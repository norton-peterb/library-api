package com.example.library.api.app.bean;

import java.util.Date;

public class CheckoutResponse {
    private String message;
    private Date dueDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
