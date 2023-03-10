package com.app.backend.auxiliaryClasses;

import org.loadtest4j.driver.DriverResponseTime;

import java.time.Duration;

public class TestResponseTime implements DriverResponseTime {

    private final Duration duration;

    public TestResponseTime(Duration duration) {
        this.duration = duration;
    }

    public static final DriverResponseTime ZERO = new TestResponseTime(Duration.ZERO);

    @Override
    public Duration getPercentile(double percentile) {
        return duration;
    }
}