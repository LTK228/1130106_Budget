import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Budget {

    public final String yearMonth;
    public final int amount;

    public Budget(String yearMonth, int amount) {

        this.yearMonth = yearMonth;
        this.amount = amount;
    }
    private int daysOfMonth() {
        YearMonth budgetYearMonth = YearMonth.parse(this.yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
        return budgetYearMonth.lengthOfMonth();
    }
    public double dailyBudget() {
        return (double) this.amount / this.daysOfMonth();
    }

}
