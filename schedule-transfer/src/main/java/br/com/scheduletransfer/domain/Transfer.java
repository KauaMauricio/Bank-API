package br.com.scheduletransfer.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String sourceAccount;

    @Column(nullable = false, length = 120)
    private String destinationAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal fee;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferStatus status;

    protected Transfer() { }

    public Transfer(String sourceAccount, String destinationAccount, BigDecimal amount,
                    BigDecimal fee, BigDecimal totalAmount, LocalDate scheduledDate,
                    LocalDateTime createdAt) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.fee = fee;
        this.totalAmount = totalAmount;
        this.scheduledDate = scheduledDate;
        this.createdAt = createdAt;
        this.status = TransferStatus.SCHEDULED;
    }

    public void cancel() {
        if (status == TransferStatus.CANCELLED) {
            throw new IllegalStateException("A transferência já está cancelada.");
        }
        status = TransferStatus.CANCELLED;
    }

    public Long getId() { return id; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getFee() { return fee; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public TransferStatus getStatus() { return status; }
}
