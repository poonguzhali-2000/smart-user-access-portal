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
import { AccessRecordService } from '../../services/access-record.service';

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
  userLoading = false;
  recordLoading = false;
  role = '';
  email = '';
  users: User[] = [];
  records: any[] = [];
  
  totalRecords = 0;
  page = 0;
  first = 0;
  rows = 5;
  searchText = '';

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
    private accessRecordService: AccessRecordService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role') || '';
    this.email = localStorage.getItem('email') || '';
    if (this.role === 'ADMIN') {
      this.loadUsers();
    }
    this.loadRecords();
  }
  
  loadUsers() {
    if (this.role !== 'ADMIN') {
      return;
    }
    this.userLoading = true;
    this.userService.getUsers(this.page, this.rows, this.searchText).subscribe({
      next: (response) => {
        this.users = response.content;
        this.totalRecords = response.totalElements;
        this.userLoading = false;
      },
      error: (error) => {
        this.userLoading = false;
        console.error(error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load users'
        });
      }
    });
  }
    
    loadRecords() {
      this.recordLoading = true;
      const request = this.role === 'ADMIN' ? this.accessRecordService.getAllRecords()
       : this.accessRecordService.getMyRecords();
       
       request.subscribe({
        next: (response) => {
          this.records = response;
          this.recordLoading = false;
        },
        error: (error) => {
          this.recordLoading = false;
          console.error(error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to load access records'
          });
        }
      });
    }

  onPageChange(event: any) {
    this.first = event.first;
    this.rows = event.rows;
    this.page = Math.floor(event.first / event.rows);
    if (this.role === 'ADMIN') {
      this.loadUsers();
    }
  }
  
  onSearch() {
    this.page = 0;
    this.first = 0;
    if (this.role === 'ADMIN') {
      this.loadUsers();
    }
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
        if (this.role === 'ADMIN') {
          this.loadUsers();
        }
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
        if (this.role === 'ADMIN') {
          this.loadUsers();
        }
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