package budget;

import java.time.LocalDate;

public record Period(LocalDate startDate, LocalDate endDate) {
    public boolean invalidPeriod() {
        return startDate().isAfter(endDate());
    }
}