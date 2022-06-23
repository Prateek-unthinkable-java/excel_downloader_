package com.example.excel_downloader_.helper;

import com.example.excel_downloader_.model.Tutorial;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Id","Title","Description","Published"};
    static String SHEET = "Tutorials";
    public static ByteArrayInputStream tutorialsToExcel(List<Tutorial> tutorials){
        try(Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            //creating a sheet
            Sheet sheet = workbook.createSheet(SHEET);
            Row headerRow = sheet.createRow(0);

            //styling the header row
            XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
            headerStyle.setBorderTop(BorderStyle.valueOf((short) 1));
            headerStyle.setBorderBottom(BorderStyle.valueOf((short) 1));
//            headerStyle.setBorderLeft(BorderStyle.valueOf((short) 1));
//            headerStyle.setBorderRight(BorderStyle.valueOf((short) 1));
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//            headerStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
//            headerStyle.setFillPattern(FillPatternType.SQUARES);

            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setBold(true);
            headerStyle.setFont(font);

            for (int col=0; col < HEADERs.length; col++){
                sheet.setColumnWidth(col+1, 25*256);
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
                cell.setCellStyle(headerStyle);
            }

            int rowId = 1;

            //styling data rows
            XSSFCellStyle rowStyle = (XSSFCellStyle) workbook.createCellStyle();
            rowStyle.setWrapText(true);
            rowStyle.setAlignment(HorizontalAlignment.CENTER);

            for (Tutorial tuts : tutorials){
                Row row = sheet.createRow(rowId++);
                row.setRowStyle(rowStyle);
                row.setHeight((short) 500);
                row.createCell(0).setCellValue(tuts.getId());
                row.createCell(1).setCellValue(tuts.getTitle());
                row.createCell(2).setCellValue(tuts.getDescription());
                row.createCell(3).setCellValue(tuts.getPublished());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch(IOException ioException) {
            throw new RuntimeException("failed to load data int excel format: "+ioException.getMessage());
        }
    }
}
