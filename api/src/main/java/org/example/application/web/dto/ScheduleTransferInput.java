package org.example.application.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ScheduleTransferInput {

    @NotBlank @Pattern(regexp = "^\\d{10}$", message = "Conta de origem deve ter 10 dígitos.")
    private String sourceAccount;

    @NotBlank @Pattern(regexp = "^\\d{10}$", message = "Conta de destino deve ter 10 dígitos.")
    private String destinationAccount;

    @NotNull @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero.")
    private BigDecimal amount;

    @NotNull @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transferDate;

    // getters/setters
    public String getSourceAccount() { return sourceAccount; }
    public void setSourceAccount(String sourceAccount) { this.sourceAccount = sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDate transferDate) { this.transferDate = transferDate; }
}