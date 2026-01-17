package com.student.data.management.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.student.data.management.system.config.StorageProperties;
import com.student.data.management.system.service.ExcelToCsvService;
import com.student.data.management.system.util.OsPaths;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProcessingController {

  private final ExcelToCsvService excelToCsvService;
  private final StorageProperties storageProperties;

  public ProcessingController(ExcelToCsvService excelToCsvService, StorageProperties storageProperties) {
    this.excelToCsvService = excelToCsvService;
    this.storageProperties = storageProperties;
  }

  @PostMapping("/process/excel")
  public ResponseEntity<?> processExcel(@RequestParam("file") MultipartFile file) throws Exception {
    if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "No file uploaded"));

    Path base = OsPaths.baseDir(storageProperties);
    Files.createDirectories(base);

    String savedName = "uploaded_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
    Path saved = base.resolve(savedName);
    file.transferTo(saved.toFile());

    Path csv = excelToCsvService.convertExcelToCsv(saved.toFile());

    return ResponseEntity.ok(Map.of(
        "message", "CSV generated (score +10 applied)",
        "excelSavedPath", saved.toString(),
        "csvPath", csv.toString()
    ));
  }
}