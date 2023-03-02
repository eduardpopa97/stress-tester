package com.app.backend.protocolController;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.IfController;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.SetupThreadGroup;

public class TestHelper {

	public static HTTPSampler createHttpSampler(String method, String domain, String port, String path) {
        HTTPSampler serverGetSampler = new HTTPSampler();
        serverGetSampler.setDomain(domain);
        serverGetSampler.setPort(Integer.parseInt(port));
        serverGetSampler.setPath(path);
        serverGetSampler.setMethod(method);
        serverGetSampler.setName(String.format("%s %s %s", domain, method, path));
        return enhanceWithGuiClass(serverGetSampler);
    }

    public static LoopController createLoopController(int loops) {
        LoopController loopCtrl = new LoopController();
        loopCtrl.setLoops(loops);
        loopCtrl.setFirst(true);
        return enhanceWithGuiClass(loopCtrl);
    }

  
    public static IfController createIfController(String condition) {
        IfController ifController = new IfController(condition);
        return ifController;
    }

    public static SetupThreadGroup createSetupThreadGroup(LoopController loopCtrl, int numThreads, int rampUp) {
        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(numThreads);
        threadGroup.setRampUp(rampUp);
        threadGroup.setSamplerController(loopCtrl);
        return enhanceWithGuiClass(threadGroup);
    }

    public static TestPlan createTestPlan(String name) {
        TestPlan testPlan = new TestPlan(name);
        testPlan.setEnabled(true);
        testPlan.setComment("");
        testPlan.setFunctionalMode(false);
        testPlan.setSerialized(false);
        testPlan.setUserDefinedVariables(new Arguments());
        testPlan.setTestPlanClasspath("");
        return enhanceWithGuiClass(testPlan);
    }

    public static <T extends TestElement> T enhanceWithGuiClass(T testElement) {
        testElement.setProperty(TestElement.GUI_CLASS, " ");
        return testElement;
    }
    
}
