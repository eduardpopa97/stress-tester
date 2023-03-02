package com.app.backend.auxiliaryClasses;

import java.util.List;

/**
 * The load test runner.
 */
public interface LoadTester {
    Result run(List<Request> requests);
}
