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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getOverlappingAmount(Period period) {
        return getDailyAmount() * period.getOverlappingDayCount(getPeriod());
    }

    private int getDailyAmount() {
        return amount / date.lengthOfMonth();
    }

    private Period getPeriod() {
        return new Period(date, date.withDayOfMonth(date.lengthOfMonth()));
    }
}
