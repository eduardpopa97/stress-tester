package com.app.backend.protocolController;

import java.io.IOException;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jorphan.collections.HashTree;

public class TestGenerator {
	
	 public static HashTree create(String method, String server, String port, String path,
			String loops, String users, String ramp) throws IOException {

	        HashTree projectTree = new HashTree();

	        HTTPSampler serverGetSampler = TestHelper.createHttpSampler(method, server, port, path);
	        LoopController loopCtrl = TestHelper.createLoopController(Integer.parseInt(loops));
	        loopCtrl.addTestElement(serverGetSampler);
	        SetupThreadGroup setupThreadGroup = TestHelper.
	        		createSetupThreadGroup(loopCtrl, Integer.parseInt(users), Integer.parseInt(ramp));
	        TestPlan testPlan = TestHelper.createTestPlan("Simple Test Plan");
	        testPlan.addThreadGroup(setupThreadGroup);

	        HashTree testPlanTree = projectTree.add(testPlan);
	        HashTree setupThreadGroupTree = testPlanTree.add(setupThreadGroup);
	        HashTree loopCtrlTree = setupThreadGroupTree.add(loopCtrl);
	        loopCtrlTree.add(serverGetSampler);
	        
	        return projectTree;
	    }

}
