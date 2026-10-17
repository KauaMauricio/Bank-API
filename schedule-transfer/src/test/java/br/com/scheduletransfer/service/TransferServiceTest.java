package br.com.scheduletransfer.service;

import br.com.scheduletransfer.domain.Transfer;
import br.com.scheduletransfer.dto.CreateTransferRequest;
import br.com.scheduletransfer.repository.TransferRepository;
import br.com.scheduletransfer.FeeCalculator;
import br.com.scheduletransfer.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    @Mock private TransferRepository repository;
    @Mock private FeeCalculator feeCalculator;
    @InjectMocks private TransferService service;

    @Test void createAddsFeeToTransferredAmount() {
        CreateTransferRequest request = new CreateTransferRequest();
        request.setSourceAccount("123");
        request.setDestinationAccount("456");
        request.setAmount(new BigDecimal("100.00"));
        request.setScheduledDate(LocalDate.now().plusDays(1));
        when(feeCalculator.calculate(any(), any(), any())).thenReturn(new BigDecimal("12.00"));
        when(repository.save(any(Transfer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transfer result = service.create(request);

        assertEquals(new BigDecimal("100.00"), result.getAmount());
        assertEquals(new BigDecimal("12.00"), result.getFee());
        assertEquals(new BigDecimal("112.00"), result.getTotalAmount());
        verify(repository).save(any(Transfer.class));
    }

    @Test void rejectsSameSourceAndDestinationAccount() {
        CreateTransferRequest request = new CreateTransferRequest();
        request.setSourceAccount("123");
        request.setDestinationAccount(" 123 ");
        request.setAmount(new BigDecimal("100.00"));
        request.setScheduledDate(LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> service.create(request));
        verify(repository, never()).save(any());
    }
}
