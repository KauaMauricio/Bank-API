
import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransferService } from '../core/service/transfer.service';

@Component({
  selector: 'app-transfer-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transfer-form.html',
  styleUrl: './transfer-form.scss'
})
export class TransferForm {
  private readonly transferService = inject(TransferService);

  @Output() transferCreated = new EventEmitter<void>();

  sourceAccount = '';
  destinationAccount = '';
  amount: number | null = null;
  scheduledDate = '';

  loading = false;
  errorMessage = '';
  successMessage = '';

  submit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (
      !this.sourceAccount.trim() ||
      !this.destinationAccount.trim() ||
      this.amount === null ||
      !this.scheduledDate
    ) {
      this.errorMessage = 'Preencha todos os campos.';
      return;
    }

    if (this.sourceAccount.trim() === this.destinationAccount.trim()) {
      this.errorMessage = 'As contas de origem e destino devem ser diferentes.';
      return;
    }

    if (!Number.isFinite(this.amount) || this.amount <= 0) {
      this.errorMessage = 'Informe um valor maior que zero.';
      return;
    }

    this.loading = true;

    this.transferService.create({
      sourceAccount: this.sourceAccount.trim(),
      destinationAccount: this.destinationAccount.trim(),
      amount: this.amount,
      scheduledDate: this.scheduledDate
    }).subscribe({
      next: () => {
        this.successMessage = 'Transferência agendada com sucesso!';
        this.sourceAccount = '';
        this.destinationAccount = '';
        this.amount = null;
        this.scheduledDate = '';
        this.loading = false;
        this.transferCreated.emit();
      },
      error: (error) => {
        console.error('Erro ao criar transferência:', error);

        this.errorMessage =
          error.error?.message ||
          'Não foi possível agendar a transferência. Confira os dados e tente novamente.';

        this.loading = false;
      }
    });
  }
}
