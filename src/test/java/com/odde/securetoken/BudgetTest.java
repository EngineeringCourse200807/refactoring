package com.odde.securetoken;

import org.junit.jupiter.api.Test;

import java.time.Month;

import static java.time.LocalDate.of;
import static java.time.Month.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BudgetTest {

    BudgetRepo stubBudgetRepo = mock(BudgetRepo.class);
    BudgetService budgetService = new BudgetService(stubBudgetRepo);

    @Test
    public void sameMonth() {
        givenBudgets(budget(2020, MAY, 310));

        int actual = budgetService.query(of(2020, MAY, 3), of(2020, MAY, 7));

        assertEquals(50, actual);
    }

    @Test
    public void twoMonths() {
        givenBudgets(budget(2020, MAY, 310), budget(2020, JUNE, 300));

        int actual = budgetService.query(of(2020, MAY, 3), of(2020, JUNE, 7));

        assertEquals(290 + 70, actual);
    }

    @Test
    public void twoYears() {
        givenBudgets(
                budget(2020, MAY, 310), budget(2020, JUNE, 300), budget(2020, JULY, 310),
                budget(2021, MAY, 310), budget(2021, JUNE, 300));

        int actual = budgetService.query(of(2020, MAY, 3), of(2021, JUNE, 7));

        assertEquals(290 + 300 + 310 + 310 + 70, actual);
    }

    @Test
    public void sameDate() {
        givenBudgets(budget(2020, MAY, 310));

        int actual = budgetService.query(of(2020, MAY, 3), of(2020, MAY, 3));

        assertEquals(10, actual);
    }

    @Test
    public void start_is_after_end() {
        givenBudgets(budget(2020, MAY, 310));

        int actual = budgetService.query(of(2020, MAY, 7), of(2020, MAY, 3));

        assertEquals(0, actual);
    }

    @Test
    public void two_month_but_one_has_no_budget() {
        givenBudgets(budget(2020, MAY, 310));

        int actual = budgetService.query(of(2020, MAY, 10), of(2020, OCTOBER, 10));

        assertEquals(220, actual);
    }

    @Test
    public void no_budget() {
        int actual = budgetService.query(of(2020, JUNE, 10), of(2020, JUNE, 20));

        assertEquals(0, actual);
    }

    @Test
    public void two_month_but_first_has_no_budget() {
        givenBudgets(budget(2020, MAY, 310));

        int actual = budgetService.query(of(2020, APRIL, 15), of(2020, MAY, 20));

        assertEquals(200, actual);
    }

    @Test
    public void two_month_but_second_has_no_budget() {
        givenBudgets(budget(2020, JULY, 310));

        int actual = budgetService.query(of(2020, JULY, 15), of(2020, AUGUST, 15));

        assertEquals(170, actual);
    }

    @Test
    public void three_month_but_middle_has_no_budget() {
        givenBudgets(budget(2020, MAY, 310), budget(2020, JULY, 31));

        int actual = budgetService.query(of(2020, MAY, 10), of(2020, JULY, 10));

        assertEquals(230, actual);
    }

    @Test
    public void three_years() {
        givenBudgets(budget(2020, MAY, 310), budget(2021, JULY, 31), budget(2022, APRIL, 300));

        int actual = budgetService.query(of(2020, MAY, 10), of(2022, APRIL, 10));

        assertEquals(220 + 31 + 100, actual);
    }

    private Budget budget(int year, Month month, int amount) {
        return new Budget() {{
            setDate(of(year, month, 1));
            setAmount(amount);
        }};
    }

    private void givenBudgets(Budget... budgets) {
        when(stubBudgetRepo.findAll()).thenReturn(asList(budgets));
    }

}
