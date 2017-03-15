package Model;

import Views.ViewPresets.AttributeType;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by marro on 15/03/2017.
 */
public class DatabaseTestMain {

    public static void main(String[] args) {
        DatabaseManager model = new DatabaseManager();
        try {
            model.loadDB("db/2weekcampaign2.cat");
        } catch (SQLException | CorruptTableException e) {
            e.printStackTrace();
        }

        Map<String, Number> testModel = model.getTotalBouncesForAttribute(AttributeType.AGE);

        testModel.forEach((type, value) -> System.out.println(type + ": " + value));
    }
}
