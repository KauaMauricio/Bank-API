package br.com.scheduletransfer.service;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class FeeCalculator {
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    public BigDecimal calculate(BigDecimal amount, LocalDate createdDate, LocalDate scheduledDate) {
        long days = ChronoUnit.DAYS.between(createdDate, scheduledDate);
        if (days < 0 || days > 50) {
            throw new IllegalArgumentException("A data deve estar entre hoje e 50 dias a partir de hoje.");
        }
        if (days == 0) return percent(amount, "0.025");
        if (days <= 10) return new BigDecimal("12.00");
        if (days <= 20) return percent(amount, "0.082");
        if (days <= 30) return percent(amount, "0.069");
        if (days <= 40) return percent(amount, "0.047");
        if (days <= 50) return percent(amount, "0.017");
        return ZERO;
    }

    private BigDecimal percent(BigDecimal amount, String rate) {
        return amount.multiply(new BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP);
    }
}
