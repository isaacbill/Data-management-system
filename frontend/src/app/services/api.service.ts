import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

export interface Student {
  studentId: number;
  firstName: string;
  lastName: string;
  dob: string;       // yyyy-MM-dd
  className: string; // Class1..Class5
  score: number;
}

export interface PageResponse {
  content: Student[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  generateExcel(records: number) {
    return this.http.post<any>(`${this.base}/api/generate`, null, {
      params: new HttpParams().set('records', records)
    });
  }

  processExcel(file: File) {
    const fd = new FormData();
    fd.append('file', file);
    return this.http.post<any>(`${this.base}/api/process/excel`, fd);
  }

  uploadCsv(file: File) {
    const fd = new FormData();
    fd.append('file', file);
    return this.http.post<any>(`${this.base}/api/upload/csv`, fd);
  }

  getStudents(page: number, size: number, studentId?: number, className?: string) {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (studentId !== undefined && studentId !== null) {
      params = params.set('studentId', studentId);
    }
    if (className) {
      params = params.set('className', className);
    }

    return this.http.get<PageResponse>(`${this.base}/api/students`, { params });
  }

  downloadExport(type: 'csv' | 'excel' | 'pdf', studentId?: number, className?: string) {
    let params = new HttpParams();
    if (studentId !== undefined && studentId !== null) params = params.set('studentId', studentId);
    if (className) params = params.set('className', className);

    return this.http.get(`${this.base}/api/export/${type}`, {
      params,
      responseType: 'blob'
    });
  }
}
