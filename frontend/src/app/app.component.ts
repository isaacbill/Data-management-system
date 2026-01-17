import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, MatToolbarModule, MatButtonModule],
  template: `
    <mat-toolbar color="primary">
      <span>Data Management System</span>
      <span style="flex:1"></span>
      <a mat-button routerLink="/generate">Generate Excel</a>
      <a mat-button routerLink="/process-excel">Excel → CSV</a>
      <a mat-button routerLink="/upload-csv">CSV → DB</a>
      <a mat-button routerLink="/report">Report</a>
    </mat-toolbar>

    <div style="padding:16px; max-width:1100px; margin:0 auto;">
      <router-outlet></router-outlet>
    </div>
  `
})
export class AppComponent {}
