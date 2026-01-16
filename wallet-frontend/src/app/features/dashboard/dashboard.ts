import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Wallet } from '../../services/wallet';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit {
  wallet: any;
  walletExists = true; 
  creating = false;
  unblocking = false; 

  constructor(
    private walletService: Wallet, 
    private router: Router,
    private cd: ChangeDetectorRef 
  ) {}

  ngOnInit() {
    this.fetchWallet();
  }

  fetchWallet() {
    this.walletService.getWalletDetails().subscribe({
      next: (res) => {
        this.wallet = res;
        this.walletExists = true;
        this.cd.detectChanges();
      },
      error: (err) => {
        this.walletExists = false;
        this.cd.detectChanges();
      }
    });
  }

  createWallet() {
    this.creating = true;
    this.walletService.createWallet().subscribe({
      next: (res) => {
        this.wallet = res;
        this.walletExists = true;
        this.creating = false;
        this.cd.detectChanges();
      },
      error: (err) => {
        alert('Error creating wallet: ' + err.message);
        this.creating = false;
        this.cd.detectChanges();
      }
    });
  }

  
  unblock() {
    if (!confirm('Are you sure you want to unblock your wallet?')) return;

    this.unblocking = true;
    this.walletService.unblockWallet().subscribe({
      next: (res) => {
        alert('Wallet Unblocked Successfully!');
        this.wallet.status = 'ACTIVE'; 
        this.unblocking = false;
        this.fetchWallet(); // Refresh data
        this.cd.detectChanges();
      },
      error: (err) => {
        alert('Failed to unblock: ' + (err.error?.message || 'Unknown error'));
        this.unblocking = false;
        this.cd.detectChanges();
      }
    });
  }

  navigate(path: string) {
    this.router.navigate([`/transactions/${path}`]);
  }
}
