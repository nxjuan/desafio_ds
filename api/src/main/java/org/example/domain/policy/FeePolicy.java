package org.example.domain.policy;

import java.math.BigDecimal;

public interface FeePolicy {
    BigDecimal calculate(BigDecimal amount, long daysDiff);
}