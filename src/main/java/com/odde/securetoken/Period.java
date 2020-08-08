package com.odde.securetoken;

import java.time.LocalDate;
import java.time.YearMonth;

public class Period {
    private final LocalDate startTime;
    private final LocalDate endTime;

    public Period(LocalDate startTime, LocalDate endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public boolean isSameMonth() {
        return YearMonth.from(startTime).equals(YearMonth.from(endTime));
    }
}
