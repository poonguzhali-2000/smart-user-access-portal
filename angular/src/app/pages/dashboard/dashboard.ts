import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  providers: [MessageService],
  imports: [
    ButtonModule,
    CommonModule,
    TableModule,
    TagModule,
    ProgressSpinnerModule,
    DialogModule,
    FormsModule,
    InputTextModule,
    ToastModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {
  loading = true;
  role = '';
  email = '';
  users: User[] = [];
  displayEditDialog = false;
  selectedUser: User = {
    id: 0,
    fullName: '',
    email: '',
    role: ''
  };
  
  constructor(
    private router: Router,
    private userService: UserService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role') || '';
    this.email = localStorage.getItem('email') || '';
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    setTimeout(() => {
      this.userService.getUsers().subscribe({
        next: (response) => {
          if (this.role === 'ADMIN') {
            this.users = response;
          } else {
            this.users = response.filter(
              user => user.email === this.email
            );
          }
          this.loading = false;
          console.log(response);
        },
        error: (error) => {
          console.error(error);
        }
      });
    }, 3000);
  }

  editUser(user: User) {
    this.selectedUser = { ...user };
    this.displayEditDialog = true;
  }

  updateUser() {
    this.userService.updateUser(this.selectedUser).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'User updated successfully'
        });
        this.displayEditDialog = false;
        this.loadUsers();
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to update user'
        });
        console.error(error);
      }
    });
  }

  deleteUser(id: number) {
    if (!confirm('Are you sure to delete this user?')) {
        return;
    }
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Deleted',
          detail: 'User deleted successfully'
        });
        this.loadUsers();
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete user'
        });
        console.error(error);
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    this.router.navigate(['/']);
  }
  
  getSeverity(role: string) {
    switch (role) {
      case 'ADMIN':
        return 'danger';
      case 'GENERAL_USER':
        return 'success';
      default:
        return 'info';
    }
  }
}