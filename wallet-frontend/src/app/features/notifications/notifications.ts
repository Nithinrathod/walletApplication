import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Navbar } from '../../components/navbar/navbar';
import { Notification } from '../../services/notification'; // Imports the service

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './notifications.html',
  styleUrls: ['./notifications.css']
})
export class Notifications implements OnInit {
  notifications: any[] = [];
  loading = true;

  constructor(
    private notificationService: Notification,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.notificationService.getNotifications().subscribe({
      next: (data: any) => { // 👈 Added type ': any'
        // Sort by date (newest first)
        this.notifications = data.sort((a: any, b: any) => 
          new Date(b.sentAt).getTime() - new Date(a.sentAt).getTime()
        );
        this.loading = false;
        this.cd.detectChanges();
      },
      error: (err: any) => { // 👈 Added type ': any'
        console.error('Failed to load notifications', err);
        this.loading = false;
        this.cd.detectChanges();
      }
    });
  }
}