package br.com.scheduletransfer;

import br.com.scheduletransfer.domain.Transfer;
import br.com.scheduletransfer.dto.CreateTransferRequest;
import br.com.scheduletransfer.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {
    private final TransferRepository repository;
    private final FeeCalculator feeCalculator;

    public TransferService(TransferRepository repository, FeeCalculator feeCalculator) {
        this.repository = repository;
        this.feeCalculator = feeCalculator;
    }

    @Transactional
    public Transfer create(CreateTransferRequest request) {
        if (request.getSourceAccount().trim().equalsIgnoreCase(request.getDestinationAccount().trim())) {
            throw new IllegalArgumentException("A conta de origem deve ser diferente da conta de destino.");
        }
        LocalDate today = LocalDate.now();
        BigDecimal amount = request.getAmount().setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal fee = feeCalculator.calculate(amount, today, request.getScheduledDate());
        BigDecimal total = amount.add(fee).setScale(2, java.math.RoundingMode.HALF_UP);
        Transfer transfer = new Transfer(request.getSourceAccount().trim(),
                request.getDestinationAccount().trim(), amount, fee, total,
                request.getScheduledDate(), LocalDateTime.now());
        return repository.save(transfer);
    }

    @Transactional(readOnly = true)
    public Transfer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transferência não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<Transfer> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Transfer cancel(Long id) {
        Transfer transfer = findById(id);
        transfer.cancel();
        return repository.save(transfer);
    }
}
