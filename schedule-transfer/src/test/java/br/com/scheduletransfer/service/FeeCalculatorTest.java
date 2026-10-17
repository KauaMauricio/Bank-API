package br.com.scheduletransfer.service;

import br.com.scheduletransfer.FeeCalculator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FeeCalculatorTest {
    private final FeeCalculator calculator = new FeeCalculator();
    private final LocalDate today = LocalDate.of(2026, 10, 14);
    private final BigDecimal amount = new BigDecimal("1000.00");

    @Test void sameDayFeeIsTwoPointFivePercent() {
        assertEquals(new BigDecimal("25.00"), calculator.calculate(amount, today, today));
    }
    @Test void oneToTenDaysFeeIsFixed() {
        assertEquals(new BigDecimal("12.00"), calculator.calculate(amount, today, today.plusDays(10)));
    }
    @Test void elevenToTwentyDaysFeeIsEightPointTwoPercent() {
        assertEquals(new BigDecimal("82.00"), calculator.calculate(amount, today, today.plusDays(11)));
    }
    @Test void twentyOneToThirtyDaysFeeIsSixPointNinePercent() {
        assertEquals(new BigDecimal("69.00"), calculator.calculate(amount, today, today.plusDays(21)));
    }
    @Test void thirtyOneToFortyDaysFeeIsFourPointSevenPercent() {
        assertEquals(new BigDecimal("47.00"), calculator.calculate(amount, today, today.plusDays(31)));
    }
    @Test void fortyOneToFiftyDaysFeeIsOnePointSevenPercent() {
        assertEquals(new BigDecimal("17.00"), calculator.calculate(amount, today, today.plusDays(50)));
    }
    @Test void rejectsDatesAfterFiftyDays() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(amount, today, today.plusDays(51)));
    }
    @Test void rejectsPastDates() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(amount, today, today.minusDays(1)));
    }
}
