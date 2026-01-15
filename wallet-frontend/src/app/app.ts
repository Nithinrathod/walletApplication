import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from './components/toast/toast';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastComponent], 
  template: `<app-toast></app-toast> <router-outlet></router-outlet>`,
  styleUrls: []
})
export class App {}