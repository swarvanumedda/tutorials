package com.baeldung.univocity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.util.List;

import org.junit.Test;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class UnivocityCSVParsingUnitTest {
    
    private String csvPath = "src/test/resources/univocity/csv/example.csv";

    @Test
    public void givenDefaultSettings_whenParserInvoked_thenRowsCountMatches() throws Exception {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        CsvParser parser = new CsvParser(parserSettings);
        List<String[]> parsedResult = parser.parseAll(new FileReader(csvPath));
        assertEquals(4, parsedResult.size());
    }
    
    @Test
    public void givenSettingsWithRowListProcessor_whenParserInvoked_thenFileContentMatches() throws Exception {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        CsvParser parser = new CsvParser(parserSettings);
        parser.parse(new FileReader(csvPath));
        assertEquals("Id,Name,DOB,Address,Company,Salary", String.join("," , rowProcessor.getHeaders()));
        assertEquals("1,Erik Ekhedal,19840114,10/3, 34 PNCRoad,Electrolux,10000", String.join(",", rowProcessor.getRows().get(1)));
    }
    
    @Test
    public void givenSettingsWithCustomNullAndEmptyValues_whenParserInvoked_thenRowsCountMatches() throws Exception {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setNullValue("####");
        parserSettings.setEmptyValue("$$$$");
        CsvParser parser = new CsvParser(parserSettings);
        parser.parse(new FileReader(csvPath));
        assertEquals("2,Swarvanu Medda,19901230,Howrah, \"Kolkata\",####,2000", String.join(",", rowProcessor.getRows().get(2)));
        assertEquals("3,Jens Roland,19810310,$$$$,Dell,12000", String.join(",", rowProcessor.getRows().get(3)));
    }
}