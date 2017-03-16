package Model;

import Model.DBEnums.DateEnum;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Static helper methods class
 */
public class DBUtils {

    public static Map<Instant, Number> truncateInstantMap(Map<Instant, Number> map, DateEnum dateEnum) {
        Map<Instant, Number> resultMap = new HashMap<>();

        switch(dateEnum) {
            case DAYS:
                map.forEach((date, val) -> resultMap.put(date.truncatedTo(ChronoUnit.DAYS), val));
                break;
            case HOURS:
                map.forEach((date, val) -> resultMap.put(date.truncatedTo(ChronoUnit.HOURS), val));
                break;
            case WEEKS:
                map.forEach((date, val) -> resultMap.put(date.truncatedTo(ChronoUnit.WEEKS), val));
                break;
        }

        return resultMap;
    }
}
