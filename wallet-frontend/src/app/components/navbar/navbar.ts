import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router'; 
import { CommonModule } from '@angular/common';
import { Auth } from '../../services/auth'; 
import { Notification } from '../../services/notification'; 
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive], 
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class Navbar implements OnInit, OnDestroy {
  private intervalId: any;
  private lastCount = 0;

  constructor(
    private auth: Auth,
    private notificationService: Notification,
    private toast: ToastService 
  ) {}

  ngOnInit() {
    this.startPolling();
  }

  ngOnDestroy() {
    if (this.intervalId) clearInterval(this.intervalId);
  }

  logout() {
    this.auth.logout();
    clearInterval(this.intervalId);
  }

  startPolling() {
    this.checkNotifications(true);
    this.intervalId = setInterval(() => {
      if (this.auth.isAuthenticated()) {
        this.checkNotifications(false);
      }
    }, 5000); 
  }

  checkNotifications(isInit: boolean) {
    if (!this.auth.isAuthenticated()) return;

    this.notificationService.getNotifications().subscribe({
      next: (data) => {
        if (!data) return;

        if (!isInit && data.length > this.lastCount) {
          const latest = data.sort((a: any, b: any) => 
             new Date(b.sentAt).getTime() - new Date(a.sentAt).getTime()
          )[0];

          if (latest) {
             // Send message to Toast Component
             this.toast.show(latest.message); 
          }
        }
        this.lastCount = data.length;
      },
      error: () => { }
    });
  }
}