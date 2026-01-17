package com.student.data.management.system.service;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvToDbService {

  private final JdbcTemplate jdbcTemplate;

  @Value("${app.io.csvBatchSize:10000}")
  private int batchSize;

  public CsvToDbService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public long uploadCsvToDb(File csvFile) throws Exception {
    String sql = """
      INSERT INTO students(student_id, first_name, last_name, dob, class_name, score)
      VALUES(?,?,?,?,?,?)
      ON CONFLICT (student_id) DO UPDATE SET
        first_name = EXCLUDED.first_name,
        last_name  = EXCLUDED.last_name,
        dob        = EXCLUDED.dob,
        class_name = EXCLUDED.class_name,
        score      = EXCLUDED.score
      """;

    long inserted = 0;
    long skipped = 0;

    try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
      String[] line;
      boolean headerSkipped = false;

      List<Object[]> batch = new ArrayList<>(batchSize);

      while ((line = reader.readNext()) != null) {
        if (!headerSkipped) {
          headerSkipped = true;
          continue;
        }
        if (line.length < 6) { skipped++; continue; }

        String idStr = safeTrim(line[0]);
        String dobStr = safeTrim(line[3]);
        String scoreStr = safeTrim(line[5]);

        if (idStr.isEmpty() || dobStr.isEmpty() || scoreStr.isEmpty()) {
          skipped++;
          continue;
        }

        long studentId;
        int csvScore;
        LocalDate dob;

        try {
          studentId = parseLongSafe(idStr);
          csvScore = parseIntSafe(scoreStr);
          dob = LocalDate.parse(dobStr); // expects yyyy-MM-dd
        } catch (Exception ex) {
          skipped++;
          continue;
        }

        String firstName = safeTrim(line[1]);
        String lastName  = safeTrim(line[2]);
        String className = safeTrim(line[4]);

        // Requirement: DB score = Excel score + 5, but CSV score = Excel score + 10
        // => DB score = CSV score - 5
        int dbScore = csvScore - 5;

        batch.add(new Object[]{
            studentId, firstName, lastName, Date.valueOf(dob), className, dbScore
        });

        if (batch.size() >= batchSize) {
          jdbcTemplate.batchUpdate(sql, batch);
          inserted += batch.size();
          batch.clear();
        }
      }

      if (!batch.isEmpty()) {
        jdbcTemplate.batchUpdate(sql, batch);
        inserted += batch.size();
      }
    }

    System.out.println("CSV upload complete. inserted=" + inserted + " skipped=" + skipped);
    return inserted;
  }

  private static String safeTrim(String s) {
    return s == null ? "" : s.trim();
  }

  private static long parseLongSafe(String s) {
    s = safeTrim(s);
    if (s.isEmpty()) return 0L;
    int dot = s.indexOf('.');
    if (dot >= 0) s = s.substring(0, dot);
    return Long.parseLong(s);
  }

  private static int parseIntSafe(String s) {
    s = safeTrim(s);
    if (s.isEmpty()) return 0;
    int dot = s.indexOf('.');
    if (dot >= 0) s = s.substring(0, dot);
    return Integer.parseInt(s);
  }
}
