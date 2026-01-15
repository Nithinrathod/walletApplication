import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';

@Injectable({ providedIn: 'root' })
export class Notification {
  constructor(private http: HttpClient) {}

  getNotifications() {
    return this.http.get<any[]>(`${environment.apiUrl}/notification/history`);
  }
}