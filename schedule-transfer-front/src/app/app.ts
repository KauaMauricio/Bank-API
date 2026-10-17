
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { Transfer, TransferService } from './core/service/transfer.service';
import { TransferForm } from './transfers/transfer-form';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, TransferForm],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  private readonly transferService = inject(TransferService);

  transfers: Transfer[] = [];
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.loadTransfers();
  }

  loadTransfers(): void {
    this.loading = true;
    this.errorMessage = '';

    this.transferService.findAll().subscribe({
      next: (data) => {
        this.transfers = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar transferências:', error);
        this.errorMessage =
          'Não foi possível carregar as transferências. Verifique se a API Java está funcionando.';
        this.loading = false;
      }
    });
  }

  onTransferCreated(): void {
    this.loadTransfers();
  }

  getTotalAmount(): number {
    return this.transfers.reduce(
      (total, transfer) => total + transfer.amount,
      0
    );
  }

  getTotalFees(): number {
    return this.transfers.reduce(
      (total, transfer) => total + transfer.fee,
      0
    );
  }

}
