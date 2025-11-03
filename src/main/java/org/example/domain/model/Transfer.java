package org.example.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
public class Transfer {
    private UUID id;
    private final AccountNumber source;
    private final AccountNumber destination;
    private final BigDecimal amount;
    private final BigDecimal fee;
    private final LocalDate scheduledDate;
    private final LocalDate transferDate;
    private final TransferStatus status;

    public Transfer(UUID id, AccountNumber source, AccountNumber destination,
                    BigDecimal amount, BigDecimal fee,
                    LocalDate scheduledDate, LocalDate transferDate,
                    TransferStatus status) {

        this.id = id;
        this.source = Objects.requireNonNull(source);
        this.destination = Objects.requireNonNull(destination);
        this.amount = amount.setScale(2);
        this.fee = fee.setScale(2);
        this.scheduledDate = Objects.requireNonNull(scheduledDate);
        this.transferDate = Objects.requireNonNull(transferDate);
        this.status = Objects.requireNonNull(status);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public AccountNumber getSource() { return source; }
    public AccountNumber getDestination() { return destination; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getFee() { return fee; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public LocalDate getTransferDate() { return transferDate; }
    public TransferStatus getStatus() { return status; }

    public BigDecimal total() { return amount.add(fee).setScale(2); }
}