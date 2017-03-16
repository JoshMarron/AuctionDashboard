package Model;

import Model.DBEnums.DateEnum;
import Views.ViewPresets.AttributeType;

import java.sql.SQLException;
import java.time.Instant;
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

        Map<String, Number> testModel = model.getBounceRateForAttribute(AttributeType.CONTEXT);
        Map<Instant, Number> testModel2 = model.getBounceRatePer(DateEnum.DAYS);

        testModel.forEach((type, value) -> System.out.println(type + ": " + value));
        testModel2.forEach((type, value) -> System.out.println(type + ": " + value));

        System.out.println(testModel2.values().stream().reduce((a, b) -> a.doubleValue() + b.doubleValue()));
    }
}
