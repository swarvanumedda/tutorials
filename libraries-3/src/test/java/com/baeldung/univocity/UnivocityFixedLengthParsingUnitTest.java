package com.baeldung.univocity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.util.List;

import org.junit.Test;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.fixed.FixedWidthFields;
import com.univocity.parsers.fixed.FixedWidthParser;
import com.univocity.parsers.fixed.FixedWidthParserSettings;

public class UnivocityFixedLengthParsingUnitTest {

    private String filePath = "src/test/resources/univocity/fixedlength/example.txt";

    @Test
    public void givenDefaultSettings_whenParserInvoked_thenRowsCountMatches() throws Exception {
        FixedWidthParserSettings parserSettings = getParserSettings();
        FixedWidthParser parser = new FixedWidthParser(parserSettings);
        List<String[]> parsedResult = parser.parseAll(new FileReader(filePath));
        assertEquals(4, parsedResult.size());
    }

    @Test
    public void givenSettingsWithRowListProcessor_whenParserInvoked_thenFileContentMatches() throws Exception {
        FixedWidthParserSettings parserSettings = getParserSettings();
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        FixedWidthParser parser = new FixedWidthParser(parserSettings);
        parser.parse(new FileReader(filePath));
        assertEquals("Id,Name,DOB,Address,Company,Salary", String.join("," , rowProcessor.getHeaders()));
        assertEquals("1,Erik Ekhedal,19840114,10/3, 34 PNCRoad,Electrolux,10000", String.join(",", rowProcessor.getRows().get(1)));
    }

    @Test
    public void givenSettingsWithCustomNullValues_whenParserInvoked_thenRowsCountMatches() throws Exception {
        FixedWidthParserSettings parserSettings = getParserSettings();
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setNullValue("####");
        FixedWidthParser parser = new FixedWidthParser(parserSettings);
        parser.parse(new FileReader(filePath));
        assertEquals("2,Swarvanu Medda,19901230,Howrah, \"Kolkata\",####,2000", String.join(",", rowProcessor.getRows().get(2)));
    }
    
    private FixedWidthParserSettings getParserSettings() {
        FixedWidthFields lengths = new FixedWidthFields(2, 14, 8, 17, 10, 6);
        FixedWidthParserSettings parserSettings = new FixedWidthParserSettings(lengths);
        parserSettings.getFormat().setPadding('_');
        parserSettings.getFormat().setLineSeparator("\n");
        parserSettings.setRecordEndsOnNewline(true);        
        return parserSettings;
    }
}