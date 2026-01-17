package com.student.data.management.system.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import com.student.data.management.system.db.StudentSearchRepository;
import com.student.data.management.system.service.ReportExportService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportController {

  private final StudentSearchRepository repo;
  private final ReportExportService exportService;

  public ReportController(StudentSearchRepository repo, ReportExportService exportService) {
    this.repo = repo;
    this.exportService = exportService;
  }

  // Pagination + search + filter
  // GET /api/students?page=0&size=20&studentId=10&className=Class1
  @GetMapping("/students")
  public Map<String, Object> students(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) Long studentId,
      @RequestParam(required = false) String className
  ) {
    return repo.search(studentId, className, page, size);
  }

  // Export CSV
  @GetMapping("/export/csv")
  public void exportCsv(
      @RequestParam(required = false) Long studentId,
      @RequestParam(required = false) String className,
      HttpServletResponse response
  ) throws Exception {
    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=students.csv");
    exportService.exportCsv(response.getOutputStream(), studentId, className);
  }

  // Export Excel
  @GetMapping("/export/excel")
  public void exportExcel(
      @RequestParam(required = false) Long studentId,
      @RequestParam(required = false) String className,
      HttpServletResponse response
  ) throws Exception {
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");
    exportService.exportExcel(response.getOutputStream(), studentId, className);
  }

  // Export PDF
  @GetMapping("/export/pdf")
  public void exportPdf(
      @RequestParam(required = false) Long studentId,
      @RequestParam(required = false) String className,
      HttpServletResponse response
  ) throws Exception {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=students.pdf");
    exportService.exportPdf(response.getOutputStream(), studentId, className);
  }
}