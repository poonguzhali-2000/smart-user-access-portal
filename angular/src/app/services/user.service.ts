import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/api/users';

  constructor( private http: HttpClient ) {}

  // getUsers(): Observable<User[]> {
  //   return this.http.get<User[]>(`${this.apiUrl}?delay=3000`);
  // }

  getUsers(page: number, size: number, search: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}?page=${page}&size=${size}&search=${search}&delay=500`);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  updateUser(user: User) {
    return this.http.put(`${this.apiUrl}/${user.id}`, user);
  }
}