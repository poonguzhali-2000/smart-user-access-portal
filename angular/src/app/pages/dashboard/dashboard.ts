import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    ButtonModule,
    CommonModule,
    TableModule,
    TagModule,
    ProgressSpinnerModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {
  loading = true;
  role = '';
  users: User[] = [];
  
  constructor( private router: Router, private userService: UserService ) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role') || '';
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    setTimeout(() => {
      this.userService.getUsers().subscribe({
        next: (response) => {
          this.users = response;
          this.loading = false;
          console.log(response);
        },
        error: (error) => {
          console.error(error);
        }
      });
    }, 3000);
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/']);
  }

  getSeverity(status: string) {
    switch (status) {
      case 'Active':
        return 'success';
      case 'Inactive':
        return 'danger';
      default:
        return 'info';
    }
  }
}