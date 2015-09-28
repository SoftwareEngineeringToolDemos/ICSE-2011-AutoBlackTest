package autoblacktest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;

import com.rational.test.ft.script.RationalTestScript;


import abt.conf.ExperimentDirectories;
import autoblacktest.util.ExperimentFileManager;
/**
 * Classe che gestigsce l'esecuzione della random strategy utilizzando le tracce di esecuzione salvate su file 
 * 
 */
public class RandomStrategyFromEpisodes {

	private Logger logger = Logger.getLogger(RandomStrategyFromEpisodes.class);
	private TestEnvironment env;

	public RandomStrategyFromEpisodes(TestEnvironment env){
		this.env = env;
	}
	public void executeReplay() {

		logger.info("Random Replay is starting");
		TestExecutionTraces.getInstance().resetEpisodeTrace();
		
		//Selezione random di uno stato  
		TestExecutionTraces testExecTrace = TestExecutionTraces.getInstance(); 
		int numberOfStates = testExecTrace.getNumberOfStates();
		//Selezione random di uno stato da stati della test suite
		//int numberOfStates = testExecTrace.getNumberOfStatesFromTestSuite();
		int state = -1;
		String trace = "";
		if (numberOfStates > 0){
			state = new Random(System.nanoTime()).nextInt(numberOfStates);
			trace = testExecTrace.getPathToState(state);
			if (trace==null){
				state = new Random(System.nanoTime()).nextInt(numberOfStates);
				trace = testExecTrace.getPathToState(state);
			}
			
		}
		if(state == 0 || state == -1) {
			logger.info("Random Replay is finished reaching the selected random state: " + state);
			return;
		}
		if (trace==null){
			logger.info("Random Replay is finished no trace to reach state: " + state);
			return;
		}
		
	

		//Esecuzione della Random Strategy
		logger.info("Random Replay is starting: Trace " + trace + " to reach state " + state);
		String [] path = trace.split(";");
		String[] trans = null;
//per controllo stato		State sn = env.getState();
		
		for (int i=0; i<path.length && !(path[0].equals("")); i++){
//per controllo stato			double si = sn.get(0);
//per controllo stato			if( env.isTerminalState() )
//per controllo stato			{
//per controllo stato				logger.info("Random replay strategy failed, starting new episode");
//per controllo stato				break;
//per controllo stato			}
			trans = path[i].split(",");

			State init = getInitState(trans[0]);
//per controllo stato			if( si!=init.get(0))
//per controllo stato			{
//per controllo stato				logger.debug("Random Replay is finished after " + i + " steps. Action "+trans[1]);
//per controllo stato				testExecTrace.addPathToState((int)si, false);
//per controllo stato				break;
//per controllo stato			}
			Action a = getAction(trans[1]);
			logger.info("Random Replay: Performing action "+a.get(0)+" "+a.get(1));
			double r = env.doActionForRandomStrategy(a);
			logger.info("Random Replay: Performed action "+a.get(0)+" "+a.get(1));
			if(r<0){
				logger.info("Random Replay is finished after " + i + " steps. Action "+trans[1]);
				break;
			}
			if(env.getExceptionStop()){
				break;
			}
//per controllo stato			sn = env.getState();
//per controllo stato			testExecTrace.setEpisodeTrace(si+","+a.get(0)+":"+a.get(1)+","+sn.get(0)+";");
			testExecTrace.setEpisodeTrace(init.get(0) + "," + a.get(0)+":" + a.get(1)+ "," + getFinalState(trans[2]).get(0)+";");
		}
		//BEGIN codice in fase di valutazioine
		RationalTestScript.unregisterAll();
		System.gc();
		//END codice in fase di valutazioine
		
		//controllo che lo stato di arrivo corrrisponde allo stato selezionato randomicamente
		if(env.getState().get(0) == getFinalState(trans[2]).get(0))
			logger.info("Random Replay is finished reaching the selected random state: " + getFinalState(trans[2]).get(0));
		else{
		    logger.info("Random Replay stopped, reached state is " + env.getState().get(0) + " instead of " + getFinalState(trans[2]).get(0));
		    
		}
	}
	
	private Action getAction(String action) {
		String[] actions = action.split(":");
		double actionV1 = Double.parseDouble(actions[0]);
		double actionV2 = Double.parseDouble(actions[1]);
		double actionValues[] = {actionV1, actionV2};
		return new Action(actionValues);
	}

	private State getFinalState(String finalState) {
		double state = Double.parseDouble(finalState);
		State finalS = new State(1);
		finalS.assign(state);
		return finalS;
	}

	private State getInitState(String initState) {
		double state = Double.parseDouble(initState);
		State initS = new State(1);
		initS.assign(state);
		return initS;
	}

}
