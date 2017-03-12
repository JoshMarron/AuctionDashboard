package Controllers;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CSVParser takes a CSV file and parses it to a list of String arrays which can be passed to the DatabaseManager.
 * This class requires OpenCSV to run.
 */
public class CSVParser {

    public static List<String[]> parseLog(File file, int start, int step) {

        List<String[]> csvRows = null;

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(file.getPath()))
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withSkipLines(start)
                .build()) {
            csvRows = new ArrayList<>();
            Iterator<String[]> openIterator = reader.iterator();
            int i = 0;
            System.out.println("parsing " + step);
            while (openIterator.hasNext() && i < step) {
                csvRows.add(openIterator.next());
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvRows;
    }
}
