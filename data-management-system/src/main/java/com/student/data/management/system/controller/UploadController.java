package com.student.data.management.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.student.data.management.system.config.StorageProperties;
import com.student.data.management.system.service.CsvToDbService;
import com.student.data.management.system.util.OsPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadController {

  private final CsvToDbService csvToDbService;
  private final StorageProperties storageProperties;

  public UploadController(CsvToDbService csvToDbService, StorageProperties storageProperties) {
    this.csvToDbService = csvToDbService;
    this.storageProperties = storageProperties;
  }

  @PostMapping("/upload/csv")
  public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) throws Exception {
    if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "No file uploaded"));

    Path base = OsPaths.baseDir(storageProperties);
    Files.createDirectories(base);

    String savedName = "uploaded_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
    Path saved = base.resolve(savedName);
    file.transferTo(saved.toFile());

    long count = csvToDbService.uploadCsvToDb(saved.toFile());

    return ResponseEntity.ok(Map.of(
        "message", "CSV uploaded to DB (DB score = Excel score +5 applied)",
        "csvSavedPath", saved.toString(),
        "rowsProcessed", count
    ));
  }
}