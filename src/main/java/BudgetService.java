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
                    int i1 = budgetYearMonth.lengthOfMonth();
                    budget += (double) budgets.get(i).amount / i1 * (DAYS.between(startDate, lastDay) + 1);
                } else {
                    int i1 = budgetYearMonth.lengthOfMonth();
                    LocalDate firstDay = budgetYearMonth.atDay(1);
                    budget += (double) budgets.get(i).amount / i1 * (DAYS.between(firstDay, endDate) + 1);
                }

            } else if (startYearMonth.equals(budgetYearMonth) || endYearMonth.equals(budgetYearMonth) || (startYearMonth.isBefore(budgetYearMonth) && endYearMonth.isAfter(budgetYearMonth))) {
                int i1 = budgetYearMonth.lengthOfMonth();
                budget += (double) budgets.get(i).amount / i1 * (DAYS.between(startDate, endDate) + 1);
            }
        }

        return budget;
    }
}
