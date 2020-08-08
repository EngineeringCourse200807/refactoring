package com.odde.securetoken;


import java.time.LocalDate;

public class Budget {

    private LocalDate date;

    private int amount;

    public Budget(LocalDate date, int amount) {
        this.date = date;
        this.amount = amount;
    }

    Budget() {

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDailyAmount() {
        return amount / date.lengthOfMonth();
    }
}
