import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // 1. Import ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { Navbar } from '../../../components/navbar/navbar';
import { Transaction } from '../../../services/transaction';
import { Auth } from '../../../services/auth';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './history.html',
  styleUrls: ['./history.css']
})
export class History implements OnInit {
  transactions: any[] = [];
  loading = true;
  userId: string | null = '';

  constructor(
    private transactionService: Transaction,
    private auth: Auth,
    private cd: ChangeDetectorRef // 2. Inject ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.userId = this.auth.getUserId();
    
    this.transactionService.getHistory().subscribe({
      next: (data) => {
        // Sort by date descending (newest first)
        this.transactions = data.sort((a: any, b: any) => 
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        this.loading = false;
        this.cd.detectChanges(); // 3. FORCE UPDATE (Fixes "Loading..." issue)
      },
      error: (err) => {
        console.error('Failed to fetch history', err);
        this.loading = false;
        this.cd.detectChanges(); // 3. FORCE UPDATE (Even on error)
      }
    });
  }
}