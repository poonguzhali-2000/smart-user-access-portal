import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = () => {

  const router = inject(Router);

  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  
  if (!token) {
    router.navigate(['/']);
    return false;
  }

  // Prevent invalid roles
  if (role !== 'ADMIN' && role !== 'GENERAL_USER') {
    localStorage.clear();
    router.navigate(['/']);
    return false;
  }

  return true;
};