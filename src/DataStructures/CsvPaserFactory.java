package DataStructures;

import DataStructures.CsvInterfaces.Factory;
import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by rhys on 23/02/17.
 */
public class CsvPaserFactory implements Factory {
    private String jsonFormatted = "";
    private CSVReader reader = null;
    private Type structure;

    public CsvPaserFactory open(Type clsStructure, String fileName) throws IOException {
        this.structure = clsStructure;
        reader = new CSVReader(new FileReader(fileName));
        String[] headers = reader.readNext();
        ArrayList<String> h = new ArrayList<>(Arrays.asList(headers));
        jsonFormatted = "{" + h.stream().map(s -> s.replace(" ", "_") + ": \"%s\"").collect(Collectors.joining(", ")) + "}";
        return this;
    }

    public CsvPaserFactory close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        return this;
    }

    public Object next() throws IOException {
        if (reader != null) {
            String s = String.format(jsonFormatted, reader.readNext());
            System.out.println(s);
            Gson g = new Gson();
            return g.fromJson(s, structure);
        }
        return null;
    }
}
    /* if the first line is the header
    String[] header = reader.readNext();
            for (String l:header) {
                    System.out.print(l+"\t\t" );
                    }
                    System.out.println();
// iterate over reader.readNext until it returns null
                    String[] line;
                    while ((line = reader.readNext())!=null)
                    {
                    for (String l:line) {
                    System.out.print(l+"\t\t");
                    }
                    LocalDate localDate = LocalDate.parse(line[0].split(" ")[0]);
                    LocalTime localTime = LocalTime.parse(line[0].split(" ")[1]);
                    LocalDateTime localDateTime = localDate.atTime(localTime);
                    Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
                    System.out.print(instant);
                    System.out.println();
                    } */