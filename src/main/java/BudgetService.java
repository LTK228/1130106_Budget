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

        if (!startDate.isAfter(endDate)) {
            return getBudget(budgets, startDate, endDate);
        }


        return 0.0;
    }

    private Double getBudget(List<Budget> budgets, LocalDate startDate, LocalDate endDate) {
        Double budget = 0.0;


        YearMonth startYearMonth = YearMonth.from(startDate);
        YearMonth endYearMonth = YearMonth.from(endDate);
        for (int i = 0; i < budgets.size(); i++) {


            String yearMonth = budgets.get(i).yearMonth;
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
