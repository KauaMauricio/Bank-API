package br.com.scheduletransfer.controller;

import br.com.scheduletransfer.domain.Transfer;
import br.com.scheduletransfer.dto.CreateTransferRequest;
import br.com.scheduletransfer.dto.TransferResponse;
import br.com.scheduletransfer.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService service;

    public TransferController(TransferService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponse create(@Valid @RequestBody CreateTransferRequest request) {
        return TransferResponse.from(service.create(request));
    }

    @GetMapping
    public List<TransferResponse> findAll() {
        return service.findAll().stream().map(TransferResponse::from).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TransferResponse findById(@PathVariable Long id) {
        return TransferResponse.from(service.findById(id));
    }

    @PatchMapping("/{id}/cancel")
    public TransferResponse cancel(@PathVariable Long id) {
        return TransferResponse.from(service.cancel(id));
    }
}
