import { Routes } from '@angular/router';
import { GenerateComponent } from './pages/generate/generate.component';
import { ProcessExcelComponent } from './pages/process-excel/process-excel.component';
import { UploadCsvComponent } from './pages/upload-csv/upload-csv.component';
import { ReportComponent } from './pages/report/report.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'generate' },
  { path: 'generate', component: GenerateComponent },
  { path: 'process-excel', component: ProcessExcelComponent },
  { path: 'upload-csv', component: UploadCsvComponent },
  { path: 'report', component: ReportComponent },
];
