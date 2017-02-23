package DataStructures.CsvInterfaces;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by rhys on 23/02/17.
 */
public interface Factory {

    public Factory open(Type clsStructure, String fileName) throws IOException;

    public Factory close() throws IOException;

    public Object next() throws IOException;
}
