package Controllers;

import Controllers.Queries.AttributeDataQuery;
import Controllers.Queries.AttributeQueryBuilder;
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
public class DashboardMultiFilterController extends DashboardMainFrameController {

    private ExecutorService helpers = Executors.newFixedThreadPool(4);
    private GraphCache multiCache;

    public DashboardMultiFilterController(DashboardMainFrame frame, DatabaseManager model) {
        super(frame, model);
        multiCache = new GraphCache(model);
    }

    public void requestMultiTimeChart(TimeDataQuery query1, TimeDataQuery query2) {
        helpers.submit(() -> {
            TimeQueryResult firstResult;
            TimeQueryResult secondResult;

            if (this.getCache().isInCache(query1)) {
                firstResult = (TimeQueryResult) this.getCache().hitCache(query1);
            } else {
                firstResult = (TimeQueryResult) this.getModel().resolveQuery(query1);
                this.getCache().addToCache(query1, firstResult);
            }

            if (multiCache.isInCache(query2)) {
                secondResult = (TimeQueryResult) multiCache.hitCache(query2);
            } else {
                secondResult = (TimeQueryResult) this.getModel().resolveQuery(query2);
                multiCache.addToCache(query2, secondResult);
            }

            this.getFrame().displayDoubleChart(query1.getMetric(), query1.getGranularity(), firstResult.getData(), secondResult.getData());
        });
    }

    public void requestMultiAttributeChart(AttributeDataQuery query1, AttributeDataQuery query2) {
        helpers.submit(() -> {
            AttributeQueryResult firstResult;
            AttributeQueryResult secondResult;

            if (this.getCache().isInCache(query1)) {
                firstResult = (AttributeQueryResult) this.getCache().hitCache(query1);
            } else {
                firstResult = (AttributeQueryResult) this.getModel().resolveQuery(query1);
                this.getCache().addToCache(query1, firstResult);
            }

            if (multiCache.isInCache(query2)) {
                secondResult = (AttributeQueryResult) this.multiCache.hitCache(query2);
            } else {
                secondResult = (AttributeQueryResult) this.getModel().resolveQuery(query2);
                multiCache.addToCache(query2, secondResult);
            }

            this.getFrame().displayDoubleChart(query1.getMetric(), query2.getAttribute(), firstResult.getData(), secondResult.getData());
        });
    }
}
