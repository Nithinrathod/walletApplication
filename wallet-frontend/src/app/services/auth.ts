import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environments/environments'; // Ensure path is correct

@Injectable({
  providedIn: 'root',
})
export class Auth {
  // Use the API URL from environment (http://localhost:8090)
  private baseUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient, private router: Router) {}

  register(userData: any) {
    // Calls POST http://localhost:8090/auth/register
    return this.http.post<any>(`${this.baseUrl}/register`, userData);
  }

  login(credentials: any) {
    // Calls POST http://localhost:8090/auth/login
    return this.http.post<any>(`${this.baseUrl}/login`, credentials);
  }

  // Helper to save JWT token after login
  saveToken(token: string, userId: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('userId', userId);
  }

  getToken() { return localStorage.getItem('token'); }
  getUserId() { return localStorage.getItem('userId'); }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
  
  isAuthenticated() { return !!this.getToken(); }
}