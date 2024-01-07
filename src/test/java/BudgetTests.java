import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetTests {

    private BudgetService budgetService;
    private FakeBudgetRepo fakeBudgetRepo;

    @BeforeEach
    void setUp() {
        fakeBudgetRepo = new FakeBudgetRepo();
        budgetService = new BudgetService(fakeBudgetRepo);
    }

    @Test
    void dateIsInvalid() {
        LocalDate startDate, endDate;
        fakeBudgetRepo.setBudgets(List.of());
        startDate = LocalDate.of(2024, 1, 10);
        endDate = LocalDate.of(2024, 1, 3);
        totalAmountShouldBe(startDate, endDate, 0.0);
    }

    @Test
    void noBudget() {
        fakeBudgetRepo.setBudgets(List.of(new Budget("202402", 2900)));
        LocalDate startDate, endDate;
        startDate = LocalDate.of(2024, 1, 3);
        endDate = LocalDate.of(2024, 1, 10);
        totalAmountShouldBe(startDate, endDate, 0.0);
    }

    @Test
    void sameDate() {
        fakeBudgetRepo.setBudgets(List.of(new Budget("202401", 310)));
        LocalDate startDate, endDate;
        startDate = LocalDate.of(2024, 1, 3);
        endDate = LocalDate.of(2024, 1, 3);
        totalAmountShouldBe(startDate, endDate, 10);
    }

    @Test
    void allMonth() {
        fakeBudgetRepo.setBudgets(List.of(new Budget("202401", 310), new Budget("202402", 2900)));
        LocalDate startDate, endDate;
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);
        totalAmountShouldBe(startDate, endDate, 310);
    }
    @Test
    void crossMonth() {
        fakeBudgetRepo.setBudgets(List.of(new Budget("202401", 310), new Budget("202402", 2900)));
        LocalDate startDate, endDate;
        startDate = LocalDate.of(2024, 1, 18);
        endDate = LocalDate.of(2024, 2, 5);
        totalAmountShouldBe(startDate, endDate, 640);
    }

    private void totalAmountShouldBe(LocalDate startDate, LocalDate endDate, double expected) {
        assertEquals(expected, budgetService.query(startDate, endDate));
    }
}
