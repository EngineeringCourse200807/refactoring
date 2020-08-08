package com.odde.securetoken;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class BudgetService {

    BudgetRepo repo;

    BudgetService(BudgetRepo repo) {
        this.repo = repo;
    }

    int query(LocalDate startTime, LocalDate endTime) {
        if (endTime.isBefore(startTime)) {
            return 0;
        }
        //同一个月的数据
        List<Budget> all = repo.findAll();
        if (isSameMonth(startTime, endTime)) {
            boolean noneMatch = all.stream().noneMatch(x -> isSameMonth(startTime, x.getDate()));
            if (noneMatch) {
                return 0;
            }
            Budget budget = all.stream().filter(x -> isSameMonth(startTime, x.getDate())).findFirst().get();
            int dailyAmount = budget.getAmount() / budget.getDate().lengthOfMonth();
            return dailyAmount * ((int) DAYS.between(startTime, endTime) + 1);
        }
        //不是同一个月的 但是是同一年的
        int budget = 0;
        budget = calBudgetMonth(startTime, endTime, all, budget);
        if (budget != 0) {
            return budget;
        }
        //不同年份的
        for (int i = startTime.getYear(); i < endTime.getYear() + 1; i++) {
            int amount = 0;
            LocalDate date;
            if (i == startTime.getYear()) {
                LocalDate endDate = LocalDate.of(startTime.getYear(), Month.DECEMBER, 31);
                budget += calBudgetMonth(startTime, endDate, all, amount);
            } else if (i == endTime.getYear()) {
                LocalDate startDate = LocalDate.of(endTime.getYear(), Month.JANUARY, 1);
                budget += calBudgetMonth(startDate, endTime, all, amount);
            } else {
                LocalDate startDate = LocalDate.of(i, Month.JANUARY, 1);
                LocalDate endDate = LocalDate.of(i, Month.DECEMBER, 31);
                budget += calBudgetMonth(startDate, endDate, all, amount);
            }
        }
        return budget;
    }

    private int calBudgetMonth(LocalDate startTime, LocalDate endTime, List<Budget> all, int budget) {
        if (startTime.getYear() == endTime.getYear()) {
            for (int i = startTime.getMonth().getValue(); i < endTime.getMonth().getValue() + 1; i++) {
                LocalDate date;
                if (i == startTime.getMonthValue()) {
                    date = LocalDate.of(startTime.getYear(), Month.of(i), startTime.getDayOfMonth());
                } else if (i == endTime.getMonthValue()) {
                    date = LocalDate.of(startTime.getYear(), Month.of(i), endTime.getDayOfMonth());
                    int signalBudget = getSignalBudget(date, all, true);
                    budget += signalBudget;
                    continue;
                } else {
                    date = LocalDate.of(startTime.getYear(), Month.of(i), 1);

                }
                int signalBudget = getSignalBudget(date, all, false);
                budget += signalBudget;
            }
        }
        return budget;
    }

    private int getSignalBudget(LocalDate date, List<Budget> budgets, boolean endMonth) {
        boolean anyMatch = budgets.stream().anyMatch(x -> isSameMonth(date, x.getDate()));
        if (anyMatch) {
            Budget budget = budgets.stream().filter(x -> isSameMonth(date, x.getDate())).findFirst().get();
            int dayOfMonth = budget.getDate().getDayOfMonth();
            int length = budget.getDate().lengthOfMonth();
            int i = budget.getAmount() / length;
            int day = date.getDayOfMonth();
            if (endMonth) {

                return i * day;
            } else {
                return i * (length - day + 1);

            }
        }
        return 0;
    }

    private boolean isSameMonth(LocalDate startTime, LocalDate endTime) {
        return YearMonth.from(startTime).equals(YearMonth.from(endTime));
    }
}
