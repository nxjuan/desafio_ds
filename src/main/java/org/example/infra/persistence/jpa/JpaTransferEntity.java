package org.example.infra.persistence.jpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "transfers")
public class JpaTransferEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 10)
    private String sourceAccount;

    @Column(nullable = false, length = 10)
    private String destinationAccount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal fee;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Column(nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false, length = 30)
    private String status;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSourceAccount() { return sourceAccount; }
    public void setSourceAccount(String sourceAccount) { this.sourceAccount = sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public LocalDate getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDate transferDate) { this.transferDate = transferDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
