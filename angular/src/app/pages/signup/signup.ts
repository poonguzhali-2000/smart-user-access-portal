import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-signup',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        PasswordModule,
        ButtonModule,
        CardModule,
        ToastModule
    ],
    providers: [MessageService],
    templateUrl: './signup.html',
    styleUrl: './signup.css'
})

export class Signup {
    signupForm: FormGroup;
    loading = false;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private messageService: MessageService
    ) {
      this.signupForm = this.fb.group({
        fullName: ['', [Validators.required]],
        email: ['',[Validators.required, Validators.email]],
        password: ['',[Validators.required, Validators.minLength(4)]],
        confirmPassword: ['',[Validators.required]]
      });
    }

    onSignup() {
      if (this.signupForm.invalid) {
        this.signupForm.markAllAsTouched();
        return;
      }

      if (
        this.signupForm.value.password !==
        this.signupForm.value.confirmPassword
      ) {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Passwords do not match'
        });
        return;
      }
      this.loading = true;
      const signupData = {
        fullName: this.signupForm.value.fullName,
        email: this.signupForm.value.email,
        password: this.signupForm.value.password,
        role: 'GENERAL_USER'
      };
      this.authService.signup(signupData).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Registration Successful'
          });
          this.loading = false;
          setTimeout(() => {
            this.router.navigate(['/']);
          }, 3000);
        },
        error: (error) => {
          console.error(error);        
          this.loading = false;        
        }        
      }); 
    }    
  }