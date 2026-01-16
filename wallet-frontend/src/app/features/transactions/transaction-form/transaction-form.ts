import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Navbar } from '../../../components/navbar/navbar';
import { Transaction } from '../../../services/transaction';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './transaction-form.html',
  styleUrls: ['./transaction-form.css']
})
export class TransactionForm implements OnInit {
  isTransfer = false;
  receiverId = '';
  amount: number | null = null;
  loading = false;
  message = '';
  status = ''; 

  constructor(
    private router: Router,
    private transactionService: Transaction,
    private cd: ChangeDetectorRef 
  ) {}

  ngOnInit() {
    this.isTransfer = this.router.url.includes('/transfer');
  }

  submit() {
    if (!this.amount || this.amount <= 0) return;
    
    this.loading = true;
    this.message = '';
    this.status = '';

    const observer = {
      next: (res: any) => {
        this.loading = false;
        this.status = 'SUCCESS';
        this.message = res.message || 'Transaction successful!';
        
        if (!this.isTransfer) this.amount = null;
        if (this.isTransfer) { 
           this.receiverId = ''; 
           this.amount = null; 
        }
        
        this.cd.detectChanges(); 
      },
      error: (err: any) => {
        this.loading = false;
        this.status = 'ERROR';
        
        
        const errorMsg = err.error?.message || '';
        if (errorMsg.toLowerCase().includes('blocked')) {
           this.message = 'Transaction Rejected: Your wallet is blocked. Please contact support.';
        } else {
           this.message = errorMsg || 'Transaction failed. Please try again.';
        }
        
        this.cd.detectChanges(); 
      }
    };

    if (this.isTransfer) {
      this.transactionService.transfer({ 
        receiverUserId: this.receiverId, 
        amount: this.amount 
      }).subscribe(observer);
    } else {
      this.transactionService.addMoney({ 
        amount: this.amount 
      }).subscribe(observer);
    }
  }
}
