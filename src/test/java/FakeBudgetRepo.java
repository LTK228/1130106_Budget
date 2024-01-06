import java.util.List;

public class FakeBudgetRepo implements BudgetRepo {
    private List<Budget> budgets;

    @Override
    public List<Budget> getAll() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }
}
