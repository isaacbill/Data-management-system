package com.student.data.management.system.service;

import com.opencsv.CSVWriter;
import com.student.data.management.system.db.StudentSearchRepository;
import com.student.data.management.system.model.Student;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportExportService {

    private final StudentSearchRepository repo;

    public ReportExportService(StudentSearchRepository repo) {
        this.repo = repo;
    }

    public void exportCsv(OutputStream os, Long studentId, String className) throws Exception {
        List<Student> data = repo.listAll(studentId, className);

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            writer.writeNext(new String[]{"studentId", "firstName", "lastName", "DOB", "class", "score"});

            for (Student s : data) {
                writer.writeNext(new String[]{
                        String.valueOf(s.getStudentId()),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getDob() == null ? "" : s.getDob().toString(),
                        s.getClassName(),
                        String.valueOf(s.getScore())
                });
            }
        }
    }

    public void exportExcel(OutputStream os, Long studentId, String className) throws Exception {
        List<Student> data = repo.listAll(studentId, className);

        try (SXSSFWorkbook wb = new SXSSFWorkbook(500)) {
            Sheet sheet = wb.createSheet("Students");

            Row h = sheet.createRow(0);
            h.createCell(0).setCellValue("studentId");
            h.createCell(1).setCellValue("firstName");
            h.createCell(2).setCellValue("lastName");
            h.createCell(3).setCellValue("DOB");
            h.createCell(4).setCellValue("class");
            h.createCell(5).setCellValue("score");

            int r = 1;
            DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

            for (Student s : data) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(s.getStudentId() == null ? 0 : s.getStudentId());
                row.createCell(1).setCellValue(s.getFirstName());
                row.createCell(2).setCellValue(s.getLastName());
                row.createCell(3).setCellValue(s.getDob() == null ? "" : s.getDob().format(fmt));
                row.createCell(4).setCellValue(s.getClassName());
                row.createCell(5).setCellValue(s.getScore());
            }

            wb.write(os);
            wb.dispose();
        }
    }

    public void exportPdf(OutputStream os, Long studentId, String className) throws Exception {
        List<Student> data = repo.listAll(studentId, className);

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(document, os);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        document.add(new Paragraph("Student Report", titleFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        table.addCell("studentId");
        table.addCell("firstName");
        table.addCell("lastName");
        table.addCell("DOB");
        table.addCell("class");
        table.addCell("score");

        for (Student s : data) {
            table.addCell(String.valueOf(s.getStudentId()));
            table.addCell(nullToEmpty(s.getFirstName()));
            table.addCell(nullToEmpty(s.getLastName()));
            table.addCell(s.getDob() == null ? "" : s.getDob().toString());
            table.addCell(nullToEmpty(s.getClassName()));
            table.addCell(String.valueOf(s.getScore()));
        }

        document.add(table);
        document.close();
    }

    private String nullToEmpty(String v) {
        return v == null ? "" : v;
    }
}
