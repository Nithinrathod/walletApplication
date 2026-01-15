import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { Dashboard } from './features/dashboard/dashboard';
import { TransactionForm } from './features/transactions/transaction-form/transaction-form';
import { History } from './features/transactions/history/history';
import { Notifications } from './features/notifications/notifications';
import { authGuard } from './core/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
  { path: 'transactions/add', component: TransactionForm, canActivate: [authGuard] },
  { path: 'transactions/transfer', component: TransactionForm, canActivate: [authGuard] },
  { path: 'transactions/history', component: History, canActivate: [authGuard] },
  { path: 'notifications', component: Notifications, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];