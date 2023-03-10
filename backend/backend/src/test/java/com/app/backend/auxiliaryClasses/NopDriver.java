package com.app.backend.auxiliaryClasses;

import org.loadtest4j.driver.Driver;
import org.loadtest4j.driver.DriverRequest;
import org.loadtest4j.driver.DriverResponseTime;
import org.loadtest4j.driver.DriverResult;

import java.time.Duration;
import java.util.List;

public class NopDriver implements Driver {
    @Override
    public DriverResult run(List<DriverRequest> requests) {
        return new NopDriverResult();
    }

    private static class NopDriverResult implements DriverResult {

        @Override
        public long getOk() {
            return 0;
        }

        @Override
        public long getKo() {
            return 0;
        }

        @Override
        public Duration getActualDuration() {
            return Duration.ZERO;
        }

        @Override
        public DriverResponseTime getResponseTime() {
            return TestResponseTime.ZERO;
        }
    }
}
