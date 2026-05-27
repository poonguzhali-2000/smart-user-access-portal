import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccessRecordService {

  private apiUrl = 'http://localhost:8080/api/records';

  constructor(private http: HttpClient) {}

  // ADMIN - Get all records
  getAllRecords(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/all`);
  }

  // GENERAL USER - Get own records
  getMyRecords(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/my`);
  }
}