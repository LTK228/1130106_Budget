import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetTests {

    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        budgetService = new BudgetService();
    }

    @Test
    void dateIsInValid() {
        LocalDate startDate, endDate;
        startDate = LocalDate.of(2024, 1, 10);
        endDate = LocalDate.of(2024, 1, 3);
        totalAmountShouldBe(startDate, endDate, 0.0);
    }

    private void totalAmountShouldBe(LocalDate startDate, LocalDate endDate, double expected) {
        assertEquals(expected, budgetService.query(startDate, endDate));
    }
}
