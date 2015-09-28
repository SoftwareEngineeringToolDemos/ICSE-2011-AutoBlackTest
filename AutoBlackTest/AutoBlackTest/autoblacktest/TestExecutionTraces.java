package autoblacktest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.tabular.HashQFunction;

import abt.conf.ExperimentDirectories;

public class TestExecutionTraces {

	private static TestExecutionTraces instance;	// class instance
	private Logger logger = Logger.getLogger(TestExecutionTraces.class);

	private HashMap<Integer, String> pathToState;
	private String episodeTrace;

	private int pathToStateFromTestSuite = 0;

	private TestExecutionTraces() {
		pathToState = new HashMap<Integer, String>();
		//initialize the map with the state zero

			pathToState.put(0, "");
			episodeTrace = "";
	}

	public static TestExecutionTraces getInstance() {
		if(instance == null) {
			instance = new TestExecutionTraces();
		}
		return instance;
	}
	
	public HashMap<Integer, String> getPathToStateMap() {
		return pathToState;
	}
	
	public void setPathToStateMap(HashMap<Integer, String> pathToState) {
		this.pathToState = pathToState;
		//set the number of state visited by executing the test suite
		pathToStateFromTestSuite = this.pathToState.size();
	}

	public String getPathToState(int state) {
		
		String path="";
		path = pathToState.get(state);
		//Integer[] key = pathToState.keySet().toArray(new Integer[pathToState.size()]);
		return path;
	}

	public void addPathToState(int state, boolean terminalState){
		String oldPath = "";
		if (!terminalState) {
				oldPath=pathToState.get(state);
			}
			if(oldPath == null || oldPath.length() > episodeTrace.length()) {
				pathToState.put(state, episodeTrace);
				
				logger.info("updated path to state " + state + " now is " + episodeTrace); 
			}
			
	}

	public String getEpisodeTrace() {
		return episodeTrace;
	}

	public void setEpisodeTrace(String transition) {
		episodeTrace = episodeTrace.concat(transition);
		logger.info("updated the interaction trace of the current episode: " + transition + " now is " + episodeTrace); 
	}

	public int getNumberOfStates() {
		return pathToState.size();
	}
	
	public int getNumberOfStatesFromTestSuite() {	
		return pathToStateFromTestSuite;
	}

	public void resetEpisodeTrace() {	
		episodeTrace = "";
	}
}
