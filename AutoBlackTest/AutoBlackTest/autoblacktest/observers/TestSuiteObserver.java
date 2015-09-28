package autoblacktest.observers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;
import org.hswgt.teachingbox.core.rl.experiment.ExperimentObserver;
import org.hswgt.teachingbox.core.rl.learner.TabularQLearner;
import org.hswgt.teachingbox.core.rl.tabular.HashQFunction;
import org.hswgt.teachingbox.core.rl.tabular.TabularQFunction;
import org.hswgt.teachingbox.core.rl.valuefunctions.QFunction;

import abt.conf.ExperimentDirectories;
import abt.conf.TestSuiteSetter;
import abtRun.Tester;
import autoblacktest.TestExecutionTraces;
import autoblacktest.TestState;
import autoblacktest.util.ExperimentFileManager;
import autoblacktest.util.SerializeObject;

import results.analysis.TestCasesFromCoverage;

public class TestSuiteObserver implements ExperimentObserver {

	private static final long serialVersionUID = 1L;
	
	private TestState testState;
	
	private transient Logger logger = Logger.getLogger(TestSuiteObserver.class);
	
	private HashMap<Integer, StepHashQFunction> stepMap;
	private int step;
	private TabularQFunction Q;
	private TabularQFunction mergedQ;
	private TabularQLearner learner;
	
	public TestSuiteObserver(TabularQLearner learner, TabularQFunction Q) {
		step = 0;
		stepMap = new HashMap<Integer, StepHashQFunction>();
		this.Q = Q;
		this.mergedQ = new HashQFunction(0);
		this.learner = learner;
		
		this.testState = TestState.getInstance();
	}

	@Override
	public void update(State s, Action a, State sn, Action an, double r, boolean terminalState) {
		stepMap.put(step, new StepHashQFunction(s, a, sn, an, r, terminalState));
		step++;	
	}

	@Override
	public void updateExperimentStart() {

	}
	
	@Override
	public void updateExperimentStop() {
		
		//retropropagazione
		StepHashQFunction map;
		for(int step = this.step-2; step>=0; step--) {
			map = stepMap.get(step);
			learner.update(map.s, map.a, map.sn, map.an, map.r, map.terminalState);
		}
		
		mergeHashQFunction();	
		SerializeObject.serialize(ExperimentDirectories.getInstance().getStateExperimentDirectory() + "\\Q.ser", mergedQ);
		
		SerializeObject.serialize(ExperimentDirectories.getInstance().getStateExperimentDirectory() + "\\propertiesSet.ser", testState.getPropertiesSet());
		SerializeObject.serialize(ExperimentDirectories.getInstance().getStateExperimentDirectory() + "\\mapDataContext.ser", testState.getMapDataContext());
		SerializeObject.serialize(ExperimentDirectories.getInstance().getStateExperimentDirectory() + "\\rewardSet.ser", testState.getRewardSet());
		SerializeObject.serialize(ExperimentDirectories.getInstance().getStateExperimentDirectory() + "\\pathToStateMap.ser", TestExecutionTraces.getInstance().getPathToStateMap());
		
		stepMap.clear();
		stepMap = null;	
	}

	@Override
	public void updateNewEpisode(State initialState) {
		
		//retropropagazione
		StepHashQFunction map;
		for(int step = this.step-2; step>=0; step--) {
			map = stepMap.get(step);
			learner.update(map.s, map.a, map.sn, map.an, map.r, map.terminalState);
		}
		
	    mergeHashQFunction();
		
		step = 0;
		stepMap.clear();
	}
	
	private class StepHashQFunction {
		
		private State s, sn;
		private Action a, an;
		private double r;
		private boolean terminalState;
		
		private StepHashQFunction(State s, Action a, State sn, Action an, double r, boolean terminalState) {
			this.s = s;
			this.sn = sn;
			this.a = a;
			this.an = an;
			this.r = r;
			this.terminalState = terminalState;
		}
		
	}
	
	private void mergeHashQFunction() {
		
		Action a = null;
		State s = null;
		for(int x = 0; x<stepMap.size(); x++) {
			a = stepMap.get(x).a;
			s = stepMap.get(x).s;
			
			if(mergedQ.getValue(s, a) < Q.getValue(s, a))
				mergedQ.setValue(s, a, Q.getValue(s, a));
			
			Q.setValue(s, a, 0);
		}
	}
	
//	public static void main(String[] args) {
//
//		TabularQFunction propertiesSet = null;
//		
//
//		File fileSer = new File("C:\\workspaceDist\\AutoBlackTest\\outputs\\experiment_20121016_1015\\state\\Q.ser");
//
//
//		FileInputStream fis;
//		try {
//			fis = new FileInputStream(fileSer);
//			ObjectInputStream ois = new ObjectInputStream(fis);
//			propertiesSet = (TabularQFunction) ois.readObject();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int x = 0; x < 10; x++) {
//			State state = new State(1);
//			state.assign((double)x);
//			System.out.println("CCCCCCC " + propertiesSet.getMaxValue(state));
//		}
//	}
}
