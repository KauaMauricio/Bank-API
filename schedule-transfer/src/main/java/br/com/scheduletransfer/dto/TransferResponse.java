package br.com.scheduletransfer.dto;

import br.com.scheduletransfer.domain.Transfer;
import br.com.scheduletransfer.domain.TransferStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransferResponse {
    private Long id;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal totalAmount;
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;
    private TransferStatus status;

    public static TransferResponse from(Transfer transfer) {
        TransferResponse response = new TransferResponse();
        response.id = transfer.getId();
        response.sourceAccount = transfer.getSourceAccount();
        response.destinationAccount = transfer.getDestinationAccount();
        response.amount = transfer.getAmount();
        response.fee = transfer.getFee();
        response.totalAmount = transfer.getTotalAmount();
        response.scheduledDate = transfer.getScheduledDate();
        response.createdAt = transfer.getCreatedAt();
        response.status = transfer.getStatus();
        return response;
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
