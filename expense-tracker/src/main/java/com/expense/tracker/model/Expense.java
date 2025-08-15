package com.expense.tracker.model;

import java.sql.Date;

public class Expense {
    private int id;
    private int userId;
    private double amount;
    private String type;
    private Date date;
    private String name;

    public Expense() {
    }

    public Expense(int id, int userId, double amount, String type, Date date, String name) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
