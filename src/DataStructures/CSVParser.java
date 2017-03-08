package DataStructures;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * CSVParser takes a CSV file and parses it to a list of String arrays which can be passed to the DatabaseManager.
 * This class requires OpenCSV to run.
 */
public class CSVParser {

    public static List<String[]> parseLog(File file) {

        List<String[]> csvRows = null;


        try (CSVReader reader = new CSVReaderBuilder(new FileReader(file.getPath()))
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withSkipLines(1)
                .build()) {
            csvRows = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvRows;
    }
}
