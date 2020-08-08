package com.odde.securetoken;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Period {
    private final LocalDate startTime;
    private final LocalDate endTime;

    public Period(LocalDate startTime, LocalDate endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getOverlappingDayCount(Period another) {
        LocalDate overlappingStart = startTime.isAfter(another.startTime) ? startTime : another.startTime;
        LocalDate overlappingEnd = endTime.isBefore(another.endTime) ? endTime : another.endTime;
        return new Period(overlappingStart, overlappingEnd).getDayCount();
    }

    private int getDayCount() {
        return (int) DAYS.between(startTime, endTime) + 1;
    }
}
