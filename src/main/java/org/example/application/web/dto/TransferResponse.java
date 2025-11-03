package org.example.application.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransferResponse {
    public UUID id;
    public String sourceAccount;
    public String destinationAccount;
    public BigDecimal amount;
    public BigDecimal fee;
    public BigDecimal total;
    public LocalDate scheduledDate;
    public LocalDate transferDate;
    public String status;
}