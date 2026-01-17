package com.student.data.management.system.service;

import com.opencsv.CSVWriter;
import com.student.data.management.system.config.StorageProperties;
import com.student.data.management.system.util.OsPaths;
import com.student.data.management.system.util.XlsxSaxReader;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ExcelToCsvService {

  private final StorageProperties storageProperties;

  public ExcelToCsvService(StorageProperties storageProperties) {
    this.storageProperties = storageProperties;
  }

  public Path convertExcelToCsv(File excelFile) throws Exception {
    Path base = OsPaths.baseDir(storageProperties);
    Files.createDirectories(base);

    String csvName = "students_" + System.currentTimeMillis() + ".csv";
    Path csvPath = base.resolve(csvName);

    try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toFile()))) {
      // Write header
      writer.writeNext(new String[]{"studentId","firstName","lastName","DOB","class","score"});

      final boolean[] firstRowSkipped = {false};

      XlsxSaxReader.readFirstSheetRows(excelFile, (List<String> row) -> {
        try {
          // First row from Excel is header; skip it
          if (!firstRowSkipped[0]) {
            firstRowSkipped[0] = true;
            return;
          }

          // Expect columns:
          // 0 studentId, 1 firstName, 2 lastName, 3 DOB, 4 class, 5 score
          // Add 10 to score for CSV
          String studentId = safeGet(row, 0);
          String firstName = safeGet(row, 1);
          String lastName  = safeGet(row, 2);
          String dob       = safeGet(row, 3);
          String className = safeGet(row, 4);
          String scoreStr  = safeGet(row, 5);

          int score = parseIntSafe(scoreStr);
          int csvScore = score + 10;

          writer.writeNext(new String[]{
              studentId, firstName, lastName, dob, className, String.valueOf(csvScore)
          });
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }

    return csvPath;
  }

  private static String safeGet(List<String> row, int idx) {
    if (idx < 0 || idx >= row.size()) return "";
    return row.get(idx) == null ? "" : row.get(idx).trim();
  }

  private static int parseIntSafe(String s) {
    try {
      if (s == null || s.isBlank()) return 0;
      // sometimes sax raw values might be "70.0"
      if (s.contains(".")) s = s.substring(0, s.indexOf('.'));
      return Integer.parseInt(s.trim());
    } catch (Exception e) {
      return 0;
    }
  }
}