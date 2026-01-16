import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router'; 
import { Auth } from '../../../services/auth';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink], 
  templateUrl: './register.html',
  styleUrls: ['./register.css'] 
})
export class Register {
  user = { username: '', email: '', password: '' };
  loading = false;
  errorMessage = '';

  constructor(private auth: Auth, private router: Router) {}

  onSubmit() {
    this.loading = true;
    this.errorMessage = ''; 

    this.auth.register(this.user).subscribe({
      next: () => {
        alert('Registration successful! Please login.');
        this.router.navigate(['/login']);
      },
      error: (err: any) => { 
        this.errorMessage = err.error?.message || 'Registration failed. Try again.';
        this.loading = false;
      }
    });
  }
}
