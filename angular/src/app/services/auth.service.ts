import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { LoginRequest } from '../models/login-request';

import { LoginResponse } from '../models/login-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl ='http://localhost:8080/api/users';

  constructor( private http: HttpClient ) {}

  login( request: LoginRequest ): Observable<LoginResponse> {
    return this.http.post<LoginResponse>( `${this.apiUrl}/login`, request );
    }
}