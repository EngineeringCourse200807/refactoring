package com.odde.securetoken;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class BudgetService {

    BudgetRepo repo;

    BudgetService(BudgetRepo repo) {
        this.repo = repo;
    }

    int query(LocalDate startTime, LocalDate endTime) {
        List<Budget> all = repo.findAll();
        if (endTime.isBefore(startTime)) {
            return 0;
        }
        //同一个月的数据
        if (startTime.getYear() == endTime.getYear() && startTime.getMonth() == endTime.getMonth()) {
            int i = endTime.getDayOfMonth() - startTime.getDayOfMonth();
            boolean anyMatch = all.stream().anyMatch(x -> startTime.getYear() == x.getDate().getYear() && startTime.getMonth() == x.getDate().getMonth());
            if (!anyMatch) {
                return 0;
            }
            Budget budget = all.stream().filter(x -> startTime.getYear() == x.getDate().getYear() && startTime.getMonth() == x.getDate().getMonth()).findFirst().get();
            int average = budget.getAmount() / budget.getDate().lengthOfMonth();
            return average * (endTime.getDayOfMonth() - startTime.getDayOfMonth() + 1);
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
        boolean anyMatch = budgets.stream().anyMatch(x -> date.getYear() == x.getDate().getYear() && date.getMonth() == x.getDate().getMonth());
        if (anyMatch) {
            Budget budget = budgets.stream().filter(x -> date.getYear() == x.getDate().getYear() && date.getMonth() == x.getDate().getMonth()).findFirst().get();
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
}
