package com.rnt.agentic_backend.service;

import com.rnt.agentic_backend.model.SignalDefinition;
import com.rnt.agentic_backend.util.GapFiller;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class RequirementImporterService {

    public List<SignalDefinition> parseExcel(MultipartFile file) {
        List<SignalDefinition> signals = new ArrayList<>();
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            boolean headerSkipped = false;
            while (rows.hasNext()) {
                Row row = rows.next();
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String signalName = safeCellValue(row, 1); // Column B
                String description = safeCellValue(row, 2); // Column C

                // --- ADD THE FILTER HERE ---
                String sn = signalName.trim().toLowerCase();
                if (
                    sn.isBlank() ||
                    sn.matches("^[0-9. ]+.*(input|output|interface|excitation|battery).*$") ||
                    sn.matches("^[0-9. ]+$") ||
                    sn.equals("input") || sn.equals("output") || sn.equals("interfaces") || sn.equals("excitation") || sn.equals("battery")
                ) {
                    continue; // skip unwanted rows
                }

                SignalDefinition signal = new SignalDefinition();
                signal.setSignalName(signalName);
                signal.setDescription(description);
                signal.setDataType(safeCellValue(row, 3));
                signal.setRange(safeCellValue(row, 4));
                signal.setUnit(safeCellValue(row, 5));
                signal.setInitialValue(safeCellValue(row, 6));
                signal.setComment(safeCellValue(row, 7));

                GapFiller.fillGaps(signal);

                signals.add(signal);
            }

            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
        return signals;
    }

    private String safeCellValue(Row row, int cellNum) {
        try {
            Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String value = switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> Double.toString(cell.getNumericCellValue());
                case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
                case FORMULA -> cell.getCellFormula();
                default -> "";
            };
            return value.trim();
        } catch (Exception e) {
            return "";
        }
    }
}
