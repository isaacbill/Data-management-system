package com.student.data.management.system.util;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class XlsxSaxReader {

    public static void readFirstSheetRows(File xlsxFile, Consumer<List<String>> rowConsumer) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(xlsxFile)) {
            XSSFReader reader = new XSSFReader(pkg);
            StylesTable styles = reader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            DataFormatter formatter = new DataFormatter();

            try (InputStream sheet = reader.getSheetsData().next()) {

                XSSFSheetXMLHandler.SheetContentsHandler handler = new XSSFSheetXMLHandler.SheetContentsHandler() {
                    private List<String> row;
                    private int lastCol = -1;

                    @Override
                    public void startRow(int rowNum) {
                        row = new ArrayList<>();
                        lastCol = -1;
                    }

                    @Override
                    public void endRow(int rowNum) {
                        rowConsumer.accept(row);
                    }

                    @Override
                    public void cell(String cellReference, String formattedValue, org.apache.poi.xssf.usermodel.XSSFComment comment) {
                        int col = colIndex(cellReference);

                        while (lastCol + 1 < col) {
                            row.add("");
                            lastCol++;
                        }

                        row.add(formattedValue == null ? "" : formattedValue.trim());
                        lastCol = col;
                    }

                    @Override
                    public void headerFooter(String text, boolean isHeader, String tagName) {
                        // ignore
                    }
                };

                XMLReader parser = XMLReaderFactory.createXMLReader();
                XSSFSheetXMLHandler sheetHandler =
                        new XSSFSheetXMLHandler(styles, null, strings, handler, formatter, false);

                parser.setContentHandler(sheetHandler);
                parser.parse(new InputSource(sheet));
            }
        }
    }

    private static int colIndex(String cellRef) {
        // "A1" -> 0, "B1" -> 1 ...
        int col = 0;
        for (int i = 0; i < cellRef.length(); i++) {
            char c = cellRef.charAt(i);
            if (Character.isDigit(c)) break;
            col = col * 26 + (Character.toUpperCase(c) - 'A' + 1);
        }
        return col - 1;
    }
}
