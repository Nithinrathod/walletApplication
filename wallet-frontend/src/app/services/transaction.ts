import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';

@Injectable({ providedIn: 'root' })
export class Transaction {
  constructor(private http: HttpClient) {}

  transfer(data: { receiverUserId: string; amount: number }) {
    return this.http.post(`${environment.apiUrl}/transaction/transfer`, data);
  }

  addMoney(data: { amount: number }) {
    return this.http.post(`${environment.apiUrl}/transaction/addMoney`, data);
  }

  getHistory() {
    return this.http.get<any[]>(`${environment.apiUrl}/transaction/history`);
  }
}