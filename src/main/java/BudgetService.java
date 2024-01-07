import budget.Period;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class BudgetService {
    private final BudgetRepo budgetRepo;

    public BudgetService(BudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    private static double getDailyBudget(int budgetOfMonth, int daysOfMonth) {
        return (double) budgetOfMonth / daysOfMonth;
    }

    public Double query(LocalDate startDate, LocalDate endDate) {
        List<Budget> budgets = budgetRepo.getAll();

        if (new Period(startDate, endDate).invalidPeriod()) {
            return 0.0;
        }

        return getBudget(budgets, new Period(startDate, endDate));
    }

    private Double getBudget(List<Budget> budgets, Period period) {
        Double budget = 0.0;
        LocalDate startDate = period.startDate();
        LocalDate endDate = period.endDate();


        YearMonth startYearMonth = YearMonth.from(startDate);
        YearMonth endYearMonth = YearMonth.from(endDate);
        for (int i = 0; i < budgets.size(); i++) {

            YearMonth budgetYearMonth = YearMonth.parse(budgets.get(i).yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));

            if (!startYearMonth.equals(endYearMonth)) {
                if (startYearMonth.isBefore(budgetYearMonth) && endYearMonth.isAfter(budgetYearMonth)) {
                    budget += (double) budgets.get(i).amount;
                } else if (startYearMonth.equals(budgetYearMonth)) {
                    LocalDate lastDay = budgetYearMonth.atEndOfMonth();
                    budget += budgets.get(i).dailyBudget() * (DAYS.between(startDate, lastDay) + 1);
                } else {
                    LocalDate firstDay = budgets.get(i).getFirstDay();
                    budget += budgets.get(i).dailyBudget() * (DAYS.between(firstDay, endDate) + 1);
                }

            } else if (startYearMonth.equals(budgetYearMonth) || endYearMonth.equals(budgetYearMonth) || (startYearMonth.isBefore(budgetYearMonth) && endYearMonth.isAfter(budgetYearMonth))) {
                double dailyBudget = budgets.get(i).dailyBudget();
                budget += dailyBudget * (DAYS.between(startDate, endDate) + 1);
            }
        }

        return budget;
    }

}
