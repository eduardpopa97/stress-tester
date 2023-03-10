package com.app.backend.auxiliaryClasses;

import org.loadtest4j.LoadTesterException;
import org.loadtest4j.driver.Driver;
import org.loadtest4j.driver.DriverRequest;
import org.loadtest4j.driver.DriverResult;
import org.loadtest4j.drivers.jmeter.JMeterResult;
import org.loadtest4j.drivers.jmeter.engine.Engine;
import org.loadtest4j.drivers.jmeter.engine.NativeEngine;
import org.loadtest4j.drivers.jmeter.parser.Parser;
import org.loadtest4j.drivers.jmeter.plan.BlackBoxTestPlanFactory;
import org.loadtest4j.drivers.jmeter.plan.TestPlanFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class JMeter implements Driver {

	final String domain;

    final int numThreads;

    final int port;

    public String protocol;

    final int rampUp;

    public JMeter(String domain, int numThreads, int port, String protocol, int rampUp) {
        this.domain = domain;
        this.numThreads = numThreads;
        this.port = port;
        this.protocol = protocol;
        this.rampUp = rampUp;
    }

    public DriverResult run(List<DriverRequest> requests) {
        validateNotEmpty(requests);

        final File testPlan = createTestPlan(requests);

        final File resultFile = runJmeter(testPlan);

        return readResult(resultFile);
    }

    private static <T> void validateNotEmpty(Collection<T> requests) {
        if (requests.size() < 1) {
            throw new LoadTesterException("No requests were specified for the load test.");
        }
    }

    private File createTestPlan(List<DriverRequest> driverRequests) {
        final TestPlanFactory testPlan = new BlackBoxTestPlanFactory(domain, numThreads, port, protocol, rampUp);
        return testPlan.create(driverRequests);
    }

    private File runJmeter(File testPlan) {
        final Path standardResultsDirectory = new File(System.getProperty("user.dir")).toPath().resolve("results");
        final Engine engine = new NativeEngine(standardResultsDirectory);
        return engine.runJmeter(testPlan);
    }

    private static JMeterResult readResult(File resultFile) {
        final Parser parser = new Parser();
        return parser.parse(toUrl(resultFile));
    }

    private static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new LoadTesterException(e);
        }
    }
}
