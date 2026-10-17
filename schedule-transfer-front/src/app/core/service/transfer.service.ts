
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Transfer {
  id: number;
  sourceAccount: string;
  destinationAccount: string;
  amount: number;
  fee: number;
  totalAmount: number;
  scheduledDate: string;
  createdAt: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/transfers';

  findAll(): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(this.apiUrl);
  }

  findById(id: number): Observable<Transfer> {
    return this.http.get<Transfer>(`${this.apiUrl}/${id}`);
  }

  create(data: {
    sourceAccount: string;
    destinationAccount: string;
    amount: number;
    scheduledDate: string;
  }): Observable<Transfer> {
    return this.http.post<Transfer>(this.apiUrl, data);
  }

  cancel(id: number): Observable<Transfer> {
    return this.http.patch<Transfer>(
      `${this.apiUrl}/${id}/cancel`,
      {}
    );
  }
}
