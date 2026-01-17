import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  standalone: true,
  selector: 'app-upload-csv',
  imports: [MatCardModule, MatButtonModule],
  template: `
    <mat-card>
      <h2>Upload CSV → Database</h2>

      <input type="file" (change)="onFile($event)" accept=".csv" />

      <div style="margin-top:12px;">
        <button mat-raised-button color="primary" (click)="upload()" [disabled]="!file || loading">
          {{ loading ? 'Uploading...' : 'Upload to DB' }}
        </button>
      </div>

      <p style="margin-top:12px;" *ngIf="message">{{ message }}</p>
    </mat-card>
  `
})
export class UploadCsvComponent {
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

    this.api.uploadCsv(this.file).subscribe({
      next: (res) => {
        this.message = `Done. Rows processed: ${res?.rowsProcessed ?? res?.inserted ?? JSON.stringify(res)}`;
        this.loading = false;
      },
      error: (err) => {
        this.message = `Error: ${err?.error?.message ?? err.message}`;
        this.loading = false;
      }
    });
  }
}
