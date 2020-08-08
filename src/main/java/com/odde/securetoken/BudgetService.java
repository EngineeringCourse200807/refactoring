package com.odde.securetoken;

import java.time.LocalDate;

public class BudgetService {

    BudgetRepo repo;

    BudgetService(BudgetRepo repo) {
        this.repo = repo;
    }

    int query(LocalDate startTime, LocalDate endTime) {
        if (endTime.isBefore(startTime)) {
            return 0;
        }
        return repo.findAll().stream().
                mapToInt(budget -> budget.getOverlappingAmount(new Period(startTime, endTime))).
                sum();
    }

}
