import { Component, ViewChild } from '@angular/core';
import { ApiService, Student } from '../../services/api.service';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-report',
  imports: [
    FormsModule,
    CommonModule,
    MatCardModule, MatTableModule, MatPaginatorModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatButtonModule
  ],
  template: `
    <mat-card>
      <h2>Student Report</h2>

      <div style="display:flex; gap:12px; flex-wrap:wrap; align-items:flex-end;">
        <mat-form-field appearance="outline" style="width:220px;">
          <mat-label>Search StudentId</mat-label>
          <input matInput type="number" [(ngModel)]="studentId" />
        </mat-form-field>

        <mat-form-field appearance="outline" style="width:220px;">
          <mat-label>Filter by Class</mat-label>
          <mat-select [(ngModel)]="className">
            <mat-option value="">All</mat-option>
            <mat-option *ngFor="let c of classes" [value]="c">{{c}}</mat-option>
          </mat-select>
        </mat-form-field>

        <button mat-raised-button color="primary" (click)="search()">Search</button>
        <button mat-stroked-button (click)="reset()">Reset</button>

        <span style="flex:1"></span>

        <button mat-stroked-button (click)="export('csv')">Export CSV</button>
        <button mat-stroked-button (click)="export('excel')">Export Excel</button>
        <button mat-stroked-button (click)="export('pdf')">Export PDF</button>
      </div>

      <div style="margin-top:12px;" *ngIf="message">{{message}}</div>

      <table mat-table [dataSource]="rows" style="width:100%; margin-top:12px;">
        <ng-container matColumnDef="studentId">
          <th mat-header-cell *matHeaderCellDef>StudentId</th>
          <td mat-cell *matCellDef="let r">{{r.studentId}}</td>
        </ng-container>

        <ng-container matColumnDef="firstName">
          <th mat-header-cell *matHeaderCellDef>First</th>
          <td mat-cell *matCellDef="let r">{{r.firstName}}</td>
        </ng-container>

        <ng-container matColumnDef="lastName">
          <th mat-header-cell *matHeaderCellDef>Last</th>
          <td mat-cell *matCellDef="let r">{{r.lastName}}</td>
        </ng-container>

        <ng-container matColumnDef="dob">
          <th mat-header-cell *matHeaderCellDef>DOB</th>
          <td mat-cell *matCellDef="let r">{{r.dob}}</td>
        </ng-container>

        <ng-container matColumnDef="className">
          <th mat-header-cell *matHeaderCellDef>Class</th>
          <td mat-cell *matCellDef="let r">{{r.className}}</td>
        </ng-container>

        <ng-container matColumnDef="score">
          <th mat-header-cell *matHeaderCellDef>Score</th>
          <td mat-cell *matCellDef="let r">{{r.score}}</td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="cols"></tr>
        <tr mat-row *matRowDef="let row; columns: cols;"></tr>
      </table>

      <mat-paginator
        [length]="totalElements"
        [pageSize]="size"
        [pageSizeOptions]="[10,20,50,100]"
        (page)="onPage($event)">
      </mat-paginator>
    </mat-card>
  `
})
export class ReportComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  classes = ['Class1','Class2','Class3','Class4','Class5'];

  cols = ['studentId','firstName','lastName','dob','className','score'];
  rows: Student[] = [];

  studentId: number | null = null;
  className = '';

  page = 0;
  size = 20;
  totalElements = 0;

  message = '';

  constructor(private api: ApiService) {
    this.load();
  }

  load() {
    this.message = '';
    this.api.getStudents(this.page, this.size, this.studentId ?? undefined, this.className || undefined)
      .subscribe({
        next: (res) => {
          this.rows = res.content;
          this.totalElements = res.totalElements;
        },
        error: (err) => {
          this.message = `Error: ${err?.error?.message ?? err.message}`;
        }
      });
  }

  search() {
    this.page = 0;
    if (this.paginator) this.paginator.firstPage();
    this.load();
  }

  reset() {
    this.studentId = null;
    this.className = '';
    this.search();
  }

  onPage(e: PageEvent) {
    this.page = e.pageIndex;
    this.size = e.pageSize;
    this.load();
  }

  export(type: 'csv'|'excel'|'pdf') {
    this.api.downloadExport(type, this.studentId ?? undefined, this.className || undefined)
      .subscribe({
        next: (blob) => this.saveBlob(blob, this.buildFileName(type)),
        error: (err) => this.message = `Export error: ${err?.error?.message ?? err.message}`
      });
  }

  private buildFileName(type: string) {
    const ext = type === 'excel' ? 'xlsx' : type;
    const parts = ['students'];
    if (this.className) parts.push(this.className);
    if (this.studentId != null) parts.push('id' + this.studentId);
    return parts.join('_') + '.' + ext;
  }

  private saveBlob(blob: Blob, filename: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
