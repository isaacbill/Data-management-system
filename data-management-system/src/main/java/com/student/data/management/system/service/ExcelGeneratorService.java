package com.student.data.management.system.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import com.student.data.management.system.config.StorageProperties;
import com.student.data.management.system.util.OsPaths;
import com.student.data.management.system.util.Randoms;

@Service
public class ExcelGeneratorService {

  private final StorageProperties storageProperties;

  public ExcelGeneratorService(StorageProperties storageProperties) {
    this.storageProperties = storageProperties;
  }

  public Path generateExcel(long records) throws Exception {
    if (records < 1) throw new IllegalArgumentException("records must be > 0");

    Path base = OsPaths.baseDir(storageProperties);
    Files.createDirectories(base);

    String fileName = "students_" + System.currentTimeMillis() + ".xlsx";
    Path outFile = base.resolve(fileName);

    // streaming workbook
    try (SXSSFWorkbook wb = new SXSSFWorkbook(500); // keep only 500 rows in memory
         OutputStream os = Files.newOutputStream(outFile)) {

      Sheet sheet = wb.createSheet("Students");
      Row header = sheet.createRow(0);
      header.createCell(0).setCellValue("studentId");
      header.createCell(1).setCellValue("firstName");
      header.createCell(2).setCellValue("lastName");
      header.createCell(3).setCellValue("DOB");
      header.createCell(4).setCellValue("class");
      header.createCell(5).setCellValue("score");

      DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

      for (int i = 1; i <= records; i++) {
        Row row = sheet.createRow(i);
        row.createCell(0).setCellValue(i);
        row.createCell(1).setCellValue(Randoms.randomName(3, 8));
        row.createCell(2).setCellValue(Randoms.randomName(3, 8));
        row.createCell(3).setCellValue(Randoms.randomDob().format(fmt));
        row.createCell(4).setCellValue(Randoms.randomClassName());
        row.createCell(5).setCellValue(Randoms.randomScoreExcel());
      }

      wb.write(os);
      wb.dispose(); // cleanup temp files for SXSSF
    }

    return outFile;
  }
}
