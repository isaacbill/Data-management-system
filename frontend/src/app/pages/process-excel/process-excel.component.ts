import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  standalone: true,
  selector: 'app-process-excel',
  imports: [MatCardModule, MatButtonModule],
  template: `
    <mat-card>
      <h2>Process Excel → CSV</h2>

      <input type="file" (change)="onFile($event)" accept=".xlsx" />

      <div style="margin-top:12px;">
        <button mat-raised-button color="primary" (click)="upload()" [disabled]="!file || loading">
          {{ loading ? 'Processing...' : 'Upload & Process' }}
        </button>
      </div>

      <p style="margin-top:12px;" *ngIf="message">{{ message }}</p>
    </mat-card>
  `
})
export class ProcessExcelComponent {
  file: File | null = null;
  loading = false;
  message = '';

  constructor(private api: ApiService) {}

  onFile(e: Event) {
    const input = e.target as HTMLInputElement;
    this.file = input.files?.[0] ?? null;
    this.message = this.file ? `Selected: ${this.file.name}` : '';
  }

  upload() {
    if (!this.file) return;
    this.loading = true;
    this.message = '';

    this.api.processExcel(this.file).subscribe({
      next: (res) => {
        this.message = `Done. CSV created at: ${res?.csvPath ?? JSON.stringify(res)}`;
        this.loading = false;
      },
      error: (err) => {
        this.message = `Error: ${err?.error?.message ?? err.message}`;
        this.loading = false;
      }
    });
  }
}
