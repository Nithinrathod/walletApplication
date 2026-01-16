import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';
import { Auth } from './auth';

@Injectable({ providedIn: 'root' })
export class Wallet {
  constructor(private http: HttpClient, private auth: Auth) {}

  getWalletDetails() {
    const userId = this.auth.getUserId();
    return this.http.get<any>(`${environment.apiUrl}/wallet/${userId}`);
  }

  createWallet() {
    return this.http.post<any>(`${environment.apiUrl}/wallet/create`, {
      balance: 0.0,
      status: 'ACTIVE'
    });
  }

  
  unblockWallet() {
    const userId = this.auth.getUserId();
    // Calls: PUT http://localhost:8090/wallet/{userId}/unblock
    return this.http.put<any>(`${environment.apiUrl}/wallet/${userId}/unblock`, {});
  }
}
