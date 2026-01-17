import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  standalone: true,
  selector: 'app-generate',
  imports: [FormsModule, MatCardModule, MatFormFieldModule, MatInputModule, CommonModule,MatButtonModule],
  template: `
    <mat-card>
      <h2>Generate Excel</h2>

      <mat-form-field appearance="outline" style="width: 320px;">
        <mat-label>Number of records</mat-label>
        <input matInput type="number" [(ngModel)]="records" min="1">
      </mat-form-field>

      <div style="margin-top:12px;">
        <button mat-raised-button color="primary" (click)="generate()" [disabled]="loading">
          {{ loading ? 'Generating...' : 'Generate' }}
        </button>
      </div>

      <p style="margin-top:12px;" *ngIf="message">{{ message }}</p>
    </mat-card>
  `
})
export class GenerateComponent {
  records = 1000000;
  loading = false;
  message = '';

  constructor(private api: ApiService) {}

  generate() {
    this.message = '';
    this.loading = true;

    this.api.generateExcel(this.records).subscribe({
      next: (res) => {
        this.message = `Done. File created at: ${res?.path ?? JSON.stringify(res)}`;
        this.loading = false;
      },
      error: (err) => {
        this.message = `Error: ${err?.error?.message ?? err.message}`;
        this.loading = false;
      }
    });
  }
}
