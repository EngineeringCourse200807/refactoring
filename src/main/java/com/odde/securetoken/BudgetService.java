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
            Budget budget = findBudget(startTime, all);
            int dailyAmount = budget.getAmount() / budget.getDate().lengthOfMonth();
            return dailyAmount * ((int) DAYS.between(startTime, endTime) + 1);
        }
        //不是同一个月的 但是是同一年的
        int budget = calBudgetMonth(startTime, endTime, all, 0);
        if (budget != 0) {
            return budget;
        }
        //不同年份的
        for (int i = startTime.getYear(); i < endTime.getYear() + 1; i++) {
            int amount = 0;
            if (i == startTime.getYear()) {
                budget += calBudgetMonth(startTime, endOfYear(startTime.getYear()), all, amount);
            } else if (i == endTime.getYear()) {
                budget += calBudgetMonth(startOfYear(endTime.getYear()), endTime, all, amount);
            } else {
                budget += calBudgetMonth(startOfYear(i), endOfYear(i), all, amount);
            }
        }
        return budget;
    }

    private int calBudgetMonth(LocalDate startTime, LocalDate endTime, List<Budget> all, int total) {
        if (startTime.getYear() == endTime.getYear()) {
            for (LocalDate current = startTime; current.isBefore(endTime) || current.isEqual(endTime); current = current.plusMonths(1)) {
                if (current.getMonthValue() == startTime.getMonthValue()) {
                    Budget budget = findBudget(startTime, all);
                    int dailyAmount = budget.getAmount() / budget.getDate().lengthOfMonth();
                    total += dailyAmount * ((int) DAYS.between(startTime, startTime.withDayOfMonth(startTime.lengthOfMonth())) + 1);
                } else if (current.getMonthValue() == endTime.getMonthValue()) {
                    Budget budget = findBudget(endTime, all);
                    int dailyAmount = budget.getAmount() / budget.getDate().lengthOfMonth();
                    total += dailyAmount * ((int) DAYS.between(endTime.withDayOfMonth(1), endTime) + 1);
                } else {
                    Budget budget = findBudget(current.withDayOfMonth(1), all);
                    int dailyAmount = budget.getAmount() / budget.getDate().lengthOfMonth();
                    total += dailyAmount * ((int) DAYS.between(current.withDayOfMonth(1), current.withDayOfMonth(1).withDayOfMonth(current.withDayOfMonth(1).lengthOfMonth())) + 1);
                }
            }
        }
        return total;
    }

    private LocalDate endOfYear(int year) {
        return LocalDate.of(year, Month.DECEMBER, 31);
    }

    private Budget findBudget(LocalDate startTime, List<Budget> all) {
        boolean noneMatch = all.stream().noneMatch(x -> isSameMonth(startTime, x.getDate()));
        if (noneMatch) {
            Budget budget = new Budget();
            budget.setDate(startTime.withDayOfMonth(1));
            budget.setAmount(0);
            return budget;
        }
        return all.stream().filter(x -> isSameMonth(startTime, x.getDate())).findFirst().get();
    }

    private boolean isSameMonth(LocalDate startTime, LocalDate endTime) {
        return YearMonth.from(startTime).equals(YearMonth.from(endTime));
    }

    private LocalDate startOfYear(int year) {
        return LocalDate.of(year, Month.JANUARY, 1);
    }
}
