package DataStructures;
import com.opencsv.CSVReader;
import Views.LogType;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by marro on 28/02/2017.
 */
public class CSVParser {

    public static List<String[]> parseLog(File file) {

        List<String[]> csvRows = null;


        try(CSVReader reader = new CSVReaderBuilder(new FileReader(file.getPath()))
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
