package Controllers;

import Controllers.DashboardMainFrameController;
import Controllers.Queries.AttributeDataQuery;
import Controllers.Queries.TimeDataQuery;
import Controllers.Results.AttributeQueryResult;
import Controllers.Results.TimeQueryResult;
import Model.DatabaseManager;
import Views.DashboardMainFrame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by marro on 26/04/2017.
 */
public class DashboardDoubleMainFrameController extends DashboardMainFrameController {

    private DatabaseManager secondModel;
    private GraphCache secondCache;
    private ExecutorService secondHelpers = Executors.newFixedThreadPool(4);

    public DashboardDoubleMainFrameController(DashboardMainFrame frame, DatabaseManager model, DatabaseManager secondModel) {
        super(frame, model);
        this.secondCache = new GraphCache(secondModel);
        this.secondModel = secondModel;
    }

    @Override
    public void requestTimeChart(TimeDataQuery query) {
        secondHelpers.submit(() -> {
            TimeQueryResult mainResult;
            TimeQueryResult secondResult;

            // Resolve query on first dataset
            if (this.getCache().isInCache(query)) {
                mainResult = (TimeQueryResult) this.getCache().hitCache(query);
            } else {
                mainResult = (TimeQueryResult) this.getModel().resolveQuery(query);
            }

            // Resolve query on second dataset
            if (secondCache.isInCache(query)) {
                secondResult = (TimeQueryResult) secondCache.hitCache(query);
            } else {
                secondResult = (TimeQueryResult) secondModel.resolveQuery(query);
            }

            // Display the chart
            this.getFrame().displayDoubleChart(query.getMetric(), query.getGranularity(), mainResult.getData(), secondResult.getData());
        });
    }

    @Override
    public void requestAttributeChart(AttributeDataQuery query) {
        secondHelpers.submit(() -> {
            AttributeQueryResult mainResult;
            AttributeQueryResult secondResult;

            // Resolve query on main dataset
            if (this.getCache().isInCache(query)) {
                mainResult = (AttributeQueryResult) this.getCache().hitCache(query);
            } else {
                mainResult = (AttributeQueryResult) this.getModel().resolveQuery(query);
            }

            // Resolve query on second dataset
            if (secondCache.isInCache(query)) {
                secondResult = (AttributeQueryResult) secondCache.hitCache(query);
            } else {
                secondResult = (AttributeQueryResult) secondModel.resolveQuery(query);
            }

            this.getFrame().displayDoubleChart(query.getMetric(), query.getAttribute(), mainResult.getData(), secondResult.getData());
        });
    }
}
