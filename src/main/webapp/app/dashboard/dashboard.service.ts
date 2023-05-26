import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  constructor(protected http: HttpClient) {}

  getEnvironment(id: string): Observable<any> {
    return this.http.get(`api/board/environments/${id}`);
  }

  getEnvironments(): Observable<any> {
    return this.http.get('api/board/environments');
  }

  getTags(): Observable<any> {
    return this.http.get('api/tags');
  }
}
