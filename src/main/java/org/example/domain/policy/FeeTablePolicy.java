package org.example.domain.policy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
public class FeeTablePolicy implements FeePolicy{
    private static class Bracket {
        final int from, to;
        final BigDecimal fixed;
        final BigDecimal percentage;
        Bracket(int from, int to, String fixed, String pct) {
            this.from = from; this.to = to;
            this.fixed = new BigDecimal(fixed);
            this.percentage = new BigDecimal(pct);
        }
        boolean matches(long days) { return days >= from && days <= to; }
        BigDecimal feeFor(BigDecimal amount) {
            BigDecimal perc = amount.multiply(percentage);
            return fixed.add(perc).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private static final List<Bracket> RULES = Arrays.asList(
            new Bracket(0, 0,   "3.00",  "0.025"),
            new Bracket(1, 10, "12.00",  "0.000"),
            new Bracket(11,20,  "0.00",  "0.082"),
            new Bracket(21,30,  "0.00",  "0.069"),
            new Bracket(31,40,  "0.00",  "0.047"),
            new Bracket(41,50,  "0.00",  "0.017")
    );

    @Override
    public BigDecimal calculate(BigDecimal amount, long daysDiff) {
        return RULES.stream()
                .filter(r -> r.matches(daysDiff))
                .findFirst()
                .map(r -> r.feeFor(amount))
                .orElseThrow(() -> new IllegalArgumentException("Não há taxa aplicável para " + daysDiff + " dia(s)."));
    }
}