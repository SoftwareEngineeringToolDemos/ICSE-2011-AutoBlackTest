package autoblacktest.observers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;
import org.hswgt.teachingbox.core.rl.experiment.ExperimentObserver;
import org.hswgt.teachingbox.core.rl.valuefunctions.QFunction;

import abt.conf.ExperimentDirectories;
import autoblacktest.TestExecutionTraces;
import autoblacktest.TestState;
import autoblacktest.util.ExperimentFileManager;
import autoblacktest.util.SerializeObject;

import results.analysis.TestCasesFromCoverage;

public class TestExperimentObserver implements ExperimentObserver {

	private static final long serialVersionUID = 1L;
	
	private TestState testState;
	
	private transient Logger logger = Logger.getLogger(TestExperimentObserver.class);
	
	public TestExperimentObserver() {
		this.testState = TestState.getInstance();
	}

	@Override
	public void update(State s, Action a, State sn, Action an, double r, boolean terminalState) {
		logger.info("Updating observer");
		TestExecutionTraces.getInstance().setEpisodeTrace(s.get(0)+","+a.get(0)+":"+a.get(1)+","+sn.get(0)+";");
		TestExecutionTraces.getInstance().addPathToState((int)sn.get(0),terminalState);
		logger.debug("Updating observer done");		
	}

	@Override
	public void updateExperimentStart() {
		ExperimentDirectories.getInstance(); // using default base directory
	}
	
	@Override
	public void updateExperimentStop() {

		testState.closeApplication();
		ExperimentFileManager.closeExperiment(testState.getEpisodeNumber());
	}

	@Override
	public void updateNewEpisode(State initialState) {
	}
}
