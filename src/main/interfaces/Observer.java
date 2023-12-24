package main.interfaces;

import java.util.ArrayList;

import main.Metric;

public interface Observer {
    void updateMetric(ArrayList<Metric> metrics);
}
