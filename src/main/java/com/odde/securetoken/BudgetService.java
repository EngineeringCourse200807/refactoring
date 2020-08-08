package com.odde.securetoken;

import java.time.LocalDate;
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
        return queryBudget(new Period(startTime, endTime));
    }

    private Budget findBudget(LocalDate startTime, List<Budget> all) {
        boolean noneMatch = all.stream().noneMatch(x -> new Period(startTime, x.getDate()).isSameMonth());
        if (noneMatch) {
            Budget budget = new Budget();
            budget.setDate(startTime.withDayOfMonth(1));
            budget.setAmount(0);
            return budget;
        }
        return all.stream().filter(x -> new Period(startTime, x.getDate()).isSameMonth()).findFirst().get();
    }

    private int getBudgetOfPeriod(LocalDate startTime, LocalDate endTime, List<Budget> all) {
        Budget budget = findBudget(startTime, all);
        return budget.getDailyAmount() * ((int) DAYS.between(startTime, endTime) + 1);
    }

    private int queryBudget(Period period) {
        //同一个月的数据
        List<Budget> all = repo.findAll();
        if (period.isSameMonth()) {
            return getBudgetOfPeriod(period.getStartTime(), period.getEndTime(), all);
        }
        //不是同一个月的 但是是同一年的
        LocalDate startTime = period.getStartTime();
        LocalDate endTime = period.getEndTime();
        int total = 0;
        for (LocalDate current = startTime; current.isBefore(endTime) || current.isEqual(endTime); current = current.plusMonths(1)) {
            if (new Period(current, startTime).isSameMonth()) {
                total += getBudgetOfPeriod(startTime, startTime.withDayOfMonth(startTime.lengthOfMonth()), all);
            } else if (new Period(current, endTime).isSameMonth()) {
                total += getBudgetOfPeriod(endTime.withDayOfMonth(1), endTime, all);
            } else {
                total += getBudgetOfPeriod(current.withDayOfMonth(1), current.withDayOfMonth(current.lengthOfMonth()), all);
            }
        }
        return total;
    }

}
