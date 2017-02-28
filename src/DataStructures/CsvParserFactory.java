package DataStructures;

import DataStructures.CsvInterfaces.Factory;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

//TODO: add documentation on how to use this class
/**
 * Created by rhys on 23/02/17.
 */
public class CsvParserFactory implements Factory {
    private CsvBeanReader beanReader = null;
    private String[] headers = null;
    private Class clsStructure;
    private FileReader FR;

    public CsvParserFactory open(Class clsStructure, String fileName) throws IOException {
        this.clsStructure = clsStructure;
        FR = new FileReader(fileName);


        this.beanReader = new CsvBeanReader(FR, CsvPreference.STANDARD_PREFERENCE);

        //remove all spaces and replace them with under scores in the headers
        this.headers = Arrays.stream(beanReader.getHeader(true))
                .map(s -> s.replace(" ", "_"))
                .collect(Collectors.toList())
                .toArray(new String[]{});


        return this;
    }

    public CsvParserFactory close() throws IOException {
        if (beanReader != null) {
            beanReader.close();
        }
        return this;
    }

    public Object next() throws IOException {
        if (beanReader != null) {
            try {
                return beanReader.read(clsStructure, headers);
            }
            catch (EOFException e){
                return null;
            }
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