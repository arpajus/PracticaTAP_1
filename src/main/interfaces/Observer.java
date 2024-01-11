package main.interfaces;

import java.util.ArrayList;
import java.util.concurrent.Future;

import main.Metric;

/**
 * Interface representing an observer that receives updates on metrics.
 */
public interface Observer {

    /**
     * Updates the observer with a list of metrics.
     *
     * @param metrics The list of metrics to be updated.
     */
    void updateMetric(ArrayList<Metric> metrics);

    /**
     * Updates the observer asynchronously with a list of future metrics.
     *
     * @param metrics The list of future metrics to be updated.
     */
    void updateMetricAsync(ArrayList<Future<Metric>> metrics);
}
