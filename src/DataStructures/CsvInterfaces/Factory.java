package DataStructures.CsvInterfaces;

import java.io.IOException;

/**
 * Created by rhys on 23/02/17.
 */
public interface Factory {

    public Factory open(Class clsStructure, String fileName) throws IOException;

    public Factory close() throws IOException;

    public Object next() throws IOException;
}
