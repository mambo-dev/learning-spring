package com.example.demo.util;

import com.example.demo.bean.Smp;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmpExcelParser {
    private SmpExcelParser() {}

    public static List<Smp> parse(InputStream inputStream) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        List<Smp> smpList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            int rownum = 4;
            int maxRows = sheet.getPhysicalNumberOfRows();

            for (; rownum <= maxRows; rownum++) {
                Row row = sheet.getRow(rownum);
                Cell firstCell = row.getCell(0);
                String firstCellValue = firstCell.getStringCellValue();
                try {
                    Date date = dateFormat.parse(firstCellValue);
                    ZonedDateTime datetime = Instant.ofEpochMilli(date.getTime())
                            .atZone(ZoneId.of("Asia/Seoul"))
                            .withNano(0)
                            .withSecond(0)
                            .withMinute(0)
                            .withHour(0);

                    for (int i = 1; i <= 24; i++) {
                        datetime = datetime.withHour(i - 1);
                        Cell cell = row.getCell(i);
                        CellType cellType = cell.getCellType();
                        double cellValue;
                        if (CellType.NUMERIC.equals(cellType)) {
                            cellValue = cell.getNumericCellValue();
                        } else if (CellType.STRING.equals(cellType)) {
                            cellValue = Double.parseDouble(cell.getStringCellValue());
                        } else {
                            continue;
                        }

                        Smp smp = new Smp();
                        smp.setDatetime(datetime.toInstant().toEpochMilli());
                        smp.setValue(cellValue);
                        smpList.add(smp);
                    }
                } catch (ParseException e) {
                    // ignored
                }
            }
        }
        return smpList;
    }
}
