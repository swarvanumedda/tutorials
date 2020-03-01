package com.baeldung.univocity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.util.List;

import org.junit.Test;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class UnivocityTSVParsingUnitTest {
    
    private String tsvPath = "src/test/resources/univocity/tsv/example.tsv";

    @Test
    public void givenDefaultSettings_whenParserInvoked_thenRowsCountMatches() throws Exception {
        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        TsvParser parser = new TsvParser(parserSettings);
        List<String[]> parsedResult = parser.parseAll(new FileReader(tsvPath));
        assertEquals(4, parsedResult.size());
    }
    
    @Test
    public void givenSettingsWithRowListProcessor_whenParserInvoked_thenFileContentMatches() throws Exception {
        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(new FileReader(tsvPath));
        assertEquals("Id,Name,DOB,Address,Company,Salary", String.join("," , rowProcessor.getHeaders()));
        assertEquals("1,Erik Ekhedal,19840114,10/3, 34 PNCRoad,Electrolux,10000", String.join(",", rowProcessor.getRows().get(1)));
    }
    
    @Test
    public void givenSettingsWithCustomNullValues_whenParserInvoked_thenRowsCountMatches() throws Exception {
        TsvParserSettings parserSettings = new TsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setNullValue("####");
        TsvParser parser = new TsvParser(parserSettings);
        parser.parse(new FileReader(tsvPath));
        assertEquals("2,Swarvanu Medda,19901230,Howrah, \"Kolkata\",####,2000", String.join(",", rowProcessor.getRows().get(2)));
    }
}