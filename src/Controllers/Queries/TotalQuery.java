package Controllers.Queries;

import java.time.Instant;

/**
 * Created by marro on 26/04/2017.
 */
public class TotalQuery extends Query {

    private Instant startDate;
    private Instant endDate;

    public TotalQuery(TotalQueryBuilder b) {
        super(b);
        this.startDate = b.getStartDate();
        this.endDate = b.getEndDate();
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
}
