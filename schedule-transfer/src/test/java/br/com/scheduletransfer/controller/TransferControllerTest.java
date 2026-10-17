package br.com.scheduletransfer.controller;

import br.com.scheduletransfer.domain.Transfer;
import br.com.scheduletransfer.dto.CreateTransferRequest;
import br.com.scheduletransfer.dto.TransferResponse;
import br.com.scheduletransfer.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    private TransferService service;
    private TransferController controller;

    @BeforeEach
    void setUp() {
        service = mock(TransferService.class);
        controller = new TransferController(service);
    }

    @Test
    void deveCriarTransferencia() {
        CreateTransferRequest request = new CreateTransferRequest();
        request.setSourceAccount("123");
        request.setDestinationAccount("456");
        request.setAmount(new BigDecimal("100.00"));
        request.setScheduledDate(LocalDate.now().plusDays(1));

        Transfer transfer = criarTransferencia();

        when(service.create(request)).thenReturn(transfer);

        TransferResponse response = controller.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("123", response.getSourceAccount());
        assertEquals("456", response.getDestinationAccount());
        assertEquals(new BigDecimal("100.00"), response.getAmount());

        verify(service).create(request);
    }

    @Test
    void deveListarTransferencias() {
        List<Transfer> transferencias =
                Collections.singletonList(criarTransferencia());

        when(service.findAll()).thenReturn(transferencias);

        List<TransferResponse> response = controller.findAll();

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());

        verify(service).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremTransferencias() {
        when(service.findAll()).thenReturn(Collections.emptyList());

        List<TransferResponse> response = controller.findAll();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(service).findAll();
    }

    @Test
    void deveBuscarTransferenciaPorId() {
        Transfer transfer = criarTransferencia();

        when(service.findById(1L)).thenReturn(transfer);

        TransferResponse response = controller.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("123", response.getSourceAccount());

        verify(service).findById(1L);
    }

    @Test
    void deveCancelarTransferencia() {
        Transfer transfer = criarTransferencia();

        when(service.cancel(1L)).thenReturn(transfer);

        TransferResponse response = controller.cancel(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(service).cancel(1L);
    }

    private Transfer criarTransferencia() {
        Transfer transfer = new Transfer(
                "123",
                "456",
                new BigDecimal("100.00"),
                new BigDecimal("3.00"),
                new BigDecimal("103.00"),
                LocalDate.now().plusDays(1),
                LocalDateTime.now()
        );

        // Define um ID para simular uma transferência já persistida.
        ReflectionTestUtils.setField(transfer, "id", 1L);

        return transfer;
    }
}