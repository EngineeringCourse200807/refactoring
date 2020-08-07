package com.odde.securetoken;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetTest {

    BudgetService service = new BudgetService(new SubBudget());

    @Test
    public void sameMonthTest() {
        int query = service.query(LocalDate.of(2020, Month.MAY, 3), LocalDate.of(2020, Month.MAY, 7));
        assertEquals(50, query);
    }


    @Test
    public void differentMonthTest() {
        int query = service.query(LocalDate.of(2020, Month.MAY, 3), LocalDate.of(2020, Month.JUNE, 7));
        assertEquals(360, query);
    }


    @Test
    public void differentYearTest() {
        int query = service.query(LocalDate.of(2020, Month.MAY, 3), LocalDate.of(2021, Month.JUNE, 7));
        assertEquals(1280, query);
    }

    @Test
    public void sameDate() {
        int query = service.query(LocalDate.of(2020, Month.MAY, 3), LocalDate.of(2020, Month.MAY, 3));
        assertEquals(10, query);
    }


    @Test
    public void end_start_test() {
        int query = service.query(LocalDate.of(2020, Month.MAY, 7), LocalDate.of(2020, Month.MAY, 3));
        assertEquals(0, query);
    }

    @Test
    public void test() {
        int query = new BudgetService(new InnerBudget()).query(LocalDate.of(2020, Month.MAY, 10), LocalDate.of(2020, Month.OCTOBER, 10));
        assertEquals(220, query);
    }

    @Test
    public void tes() {
        int query = new BudgetService(new InnerBudget()).query(LocalDate.of(2020, Month.JUNE, 10), LocalDate.of(2020, Month.JUNE, 20));
        assertEquals(0, query);
    }

    @Test
    public void te() {
        int query = service.query(LocalDate.of(2020, Month.APRIL, 15), LocalDate.of(2020, Month.MAY, 20));
        assertEquals(200, query);
    }


    @Test
    public void t() {
        int query = service.query(LocalDate.of(2020, Month.JULY, 15), LocalDate.of(2020, Month.AUGUST, 15));
        assertEquals(170, query);
    }


    @Test
    public void yt() {
        int query = new BudgetService(new InnBudget()).query(LocalDate.of(2020, Month.MAY, 10), LocalDate.of(2020, Month.JULY, 10));
        assertEquals(230, query);
    }


    class SubBudget implements BudgetRepo {
        @Override
        public List<Budget> findAll() {
            List<Budget> list = new ArrayList<>();
            list.add(new Budget(LocalDate.of(2020, Month.MAY, 1), 310));
            list.add(new Budget(LocalDate.of(2020, Month.JUNE, 1), 300));
            list.add(new Budget(LocalDate.of(2020, Month.JULY, 1), 310));
            list.add(new Budget(LocalDate.of(2021, Month.MAY, 1), 310));
            list.add(new Budget(LocalDate.of(2021, Month.JUNE, 1), 300));
            list.add(new Budget(LocalDate.of(2021, Month.JULY, 1), 310));

            return list;
        }
    }

    class InnerBudget implements BudgetRepo {
        @Override
        public List<Budget> findAll() {
            List<Budget> list = new ArrayList<>();
            list.add(new Budget(LocalDate.of(2020, Month.MAY, 1), 310));
            return list;
        }
    }


    class InnBudget implements BudgetRepo {
        @Override
        public List<Budget> findAll() {
            List<Budget> list = new ArrayList<>();
            list.add(new Budget(LocalDate.of(2020, Month.MAY, 1), 310));
            list.add(new Budget(LocalDate.of(2020, Month.JULY, 1), 31));

            return list;
        }
    }

}
