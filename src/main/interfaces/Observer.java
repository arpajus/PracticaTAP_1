package main.interfaces;

import java.util.ArrayList;
import java.util.concurrent.Future;

import main.Metric;

public interface Observer {
    void updateMetric(ArrayList<Metric> metrics);
    void updateMetricAsync(ArrayList<Future<Metric>> metrics);

}
