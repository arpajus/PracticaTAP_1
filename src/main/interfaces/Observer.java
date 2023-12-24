package main.interfaces;

import main.Metric;

public interface Observer {
    void updateMetric(Metric metric);
}
