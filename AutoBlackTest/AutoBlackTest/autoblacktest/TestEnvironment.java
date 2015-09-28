package autoblacktest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.datastructures.ActionSet;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.Environment;
import org.hswgt.teachingbox.core.rl.env.State;

import resources.TesterHelper;

import abt.conf.EnvironmentalSetter;
import autoblacktest.actions.ActionFilter;
import autoblacktest.actions.ActionManager;
import autoblacktest.actions.parameters.ParameterNumberFormat;
import autoblacktest.actions.parameters.text.DataContextFinder;
import autoblacktest.actions.parameters.text.DataManager;
import autoblacktest.state.PropertyManager;
import autoblacktest.util.ExperimentFileManager;
import autoblacktest.util.NewClassRecorder;

import com.google.common.collect.ImmutableMap;
import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.TestObject;

/**
 * class that contains methods for actions
 * 
 */
public class TestEnvironment extends TesterHelper implements Environment {
	
	private int stepCounter = 0;
	private int experimentStepCounter = 0;
	
	//da utilizzar per gestire eccezione finestra non attiva sul doaction, 
	//a true ferma episodio senza aggiornare lo stato
	private boolean exceptionStop = false;
	
	private static final long serialVersionUID = 6150832709415978972L;

	protected double sleeptime = EnvironmentalSetter.getSleepTime();
	
	private ImmutableMap<String, Object> simpleActions;
	
	protected TestState testState;
	
	/*insieme degli actionable Tos (insieme degli UIClassID) Lo utilizziamo per controllare che l'insieme degli
	 * actionable Tos calcolati in "updateAction" sono gli stessi calcolati di "doAction"
	 * questo ci permette di evitare "ArraysOutOfBound.
	 */
	private String[] currentActionableTosID;
	
	/**
	 * to filter actions to be executed on the application
	 */
	private ActionFilter filter = new ActionFilter();
	
	public static ActionSet ACTION_SET = new ActionSet();
	
	//Giovanni
	//gestore dei dati per le azioni
	private DataManager dataManager;
	
	private Logger logger = Logger.getLogger(TestEnvironment.class);

	public TestEnvironment() {
		simpleActions = filter.getSimpleActionMap();

		testState = TestState.getInstance();
		
		//Giovanni
		//Inizializzazione dei dati, attualmente vengono inizializzati solo i dati relativi
		//a un dataSet generale.Si può pensare di utilizzare dataSet differenti per ogni applicazione
		//in tal caso il caricamento può essere gestito durante la fase di "start" delle apllicazioni.
		dataManager=DataManager.getInstance();
		dataManager.loadData(dataManager.LOAD_FROM_XML_FILE);
		//fine
	}
	
	public boolean getExceptionStop(){
		return exceptionStop;
	}
	

	public void setExceptionStop(boolean value){
		exceptionStop = value;
	}

	/**
	 * Invoke the action on the current state
	 * @throws IOException 
	 */
	@Override
	public double doAction(Action a) {
		double reward = 0;

		testState.setMessage_err(testState.getStdErr());
		testState.setCheck_std_err(true);

		logger.info("INVOKING ACTION ON STATE "	+ (int) testState.getCurrentState().get(0));
		
		//controllo la consistenza dell'actionalble Tos tra updateAction e doAction
		TestObject[] currentActionalbleTos = testState.getActionableTestObjects(testState.getLastExecutedTestObject());
		if(!(currentActionalbleTos.length == currentActionableTosID.length)) {
			setExceptionStop(true);
			return 0;
		}
		for(int x=0; x<currentActionalbleTos.length; x++) {
			if(!(currentActionalbleTos[x].getProperty("uIClassID").toString().equals(currentActionableTosID[x]))) {
				setExceptionStop(true);
				return 0;
			}
		}
		
		try {
			
			doSimpleAction(a, currentActionalbleTos);

		} catch (WindowActivateFailedException e) {
			logger.debug("unregister in doaction");
			TesterHelper.unregisterAll();
			System.gc();

			try {
				doSimpleAction(a, currentActionalbleTos);
			} catch (Exception e1) {
				logger.fatal("1 ERRORE FATAL IN DOACTION");
				setExceptionStop(true);
				e1.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (InvocationTargetException e) {
			if (e.getTargetException().toString().equals("com.rational.test.ft.WindowActivateFailedException")) {
				logger.debug("unregister in doaction");
				TesterHelper.unregisterAll();
				System.gc();

				try {
					doSimpleAction(a, currentActionalbleTos);
				} catch (Exception e1) {
					logger.fatal("2 ERRORE FATAL IN DOACTION");
					exceptionStop=true;
					e1.printStackTrace();
				}
			}
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (StackOverflowError e) {
			logger.fatal("3 ERRORE FATAL IN DOACTION");
			exceptionStop=true;
			e.printStackTrace();

		}
		sleep(sleeptime);
		TestObject[] newTos = testState.getStateTestObjects();
		testState.setCurrentState(newTos);
		double neg = -1;
		if(testState.getCurrentState().get(0)==neg) return 0;
		
		reward = getReward();
		
		stepCounter++;
		logger.info("number of executed actions without replay strategy " + stepCounter);
		//TesterHelper.unregisterAll();
		return reward;
	}
	/**
	 * Invoke the action on the current state
	 * @throws IOException 
	 */
	public double doActionForRandomStrategy(Action a) {
		double reward = 0;

		//testState.setMessage_err(testState.getStdErr());
		//testState.setCheck_std_err(true);
		
		TestObject[] currentActionalbleTos = testState.getActionableTestObjects(testState.getLastExecutedTestObject());
		try {
			
			doSimpleAction(a, currentActionalbleTos);

		} catch (WindowActivateFailedException e) {
			logger.debug("unregister in doaction");
			TesterHelper.unregisterAll();
			System.gc();

			try {
				doSimpleAction(a, currentActionalbleTos);
			} catch (Exception e1) {
				logger.fatal("1 ERRORE FATAL IN DOACTION");
				setExceptionStop(true);
				e1.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (InvocationTargetException e) {
			if (e.getTargetException().toString().equals("com.rational.test.ft.WindowActivateFailedException")) {
				logger.debug("unregister in doaction");
				TesterHelper.unregisterAll();
				System.gc();

				try {
					doSimpleAction(a, currentActionalbleTos);
				} catch (Exception e1) {
					logger.fatal("2 ERRORE FATAL IN DOACTION");
					exceptionStop=true;
					e1.printStackTrace();
				}
			}
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (StackOverflowError e) {
			logger.fatal("3 ERRORE FATAL IN DOACTION");
			exceptionStop=true;
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("ArrayIndexOutOfBoundsException");
			reward = -1;
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException");
			reward = -1;
		}
		
		
		sleep(sleeptime);
//		TestObject[] newTos = testState.getStateTestObjects();
//		testState.setCurrentState(newTos);
//		double neg = -1;
//		if(testState.getCurrentState().get(0)==neg) return 0;
		
//		reward = getReward();		
		
		return reward;
	}

	public void doSimpleAction(Action a, TestObject[] tos) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, WindowActivateFailedException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		String className = tos[(int) a.get(0)].getProperty("uIClassID").toString();

		Class<?> c = Class.forName("autoblacktest.actions.simple." + className);
		Method m[] = c.getDeclaredMethods();

		logger.debug("Getting " + m.length + " methods of class " + className);

		if (m.length > 0) {
			Object[] arguments;
			int index = 0;
			
			if (ActionManager.isParametrized(a.get(1))) {
				logger.info("Invoking parametrized action "
						+ className
						+ "."
						+ m[ParameterNumberFormat.getMethodIndex(a.get(1))]
								.getName());
				//old version
				//int argument = ParameterNumberFormat.getParameter(a.get(1));
				
				long argument = ParameterNumberFormat.getParameter(a.get(1));
				
				arguments = new Object[] { tos[(int) a.get(0)], argument };
				index = ParameterNumberFormat.getMethodIndex(a.get(1));
				//DOT: Dot file information
				testState.setLastInvokedMethod(className+"."+m[index].getName()+"("+argument+")");

			} else {
				logger.info("Invoking action " + className + "."
						+ m[(int) a.get(1)].getName());

				arguments = new Object[] { tos[(int) a.get(0)] };
				index = (int) a.get(1);
				//DOT: For Dot file information
				testState.setLastInvokedMethod(className+"."+m[index].getName()+"()");
			}
			testState.setLastExecutedTestObject(tos[(int) a.get(0)]);
			
			
			m[index].invoke(c.newInstance(), arguments);
			experimentStepCounter++;
			logger.info("number of executed actions with replay strategy " + experimentStepCounter);
			

		} else {
			logger.error(c.getName() + " has no method implemented");
		}
	}

	/**
	 * return the istant reward based of the difference between the old state
	 * and the current state. A bonus will be given if is the first time in the
	 * episode visiting the new state and if the new state is a new window
	 * 
	 * @param oldState
	 * @param stateNew
	 * @return
	 */
	public double getReward() {
		
		int contextAnalysis = 0;

		double r = 0.0;

		TestObject[] stateOld = testState.getOldTos();
		ArrayList<String> propertiesO = new ArrayList<String>(Arrays.asList(testState.getPropertiesOldState()));
		String[] propertiesN = testState.getPropertiesNewState();
		
		TestObject[] stateNew = testState.getCurrentTos();

		/**
		 * generate the key for the reward map (oldState:newState)
		 */
		String key = Double.toString(testState.getOldState().get(0)) + ":" + Double.toString(testState.getCurrentState().get(0));

		Double mappedReward = testState.getRewardValue(key);

		if( mappedReward == null) {

			int differencesCounter;
			ArrayList<TestObject> stateOldList = new ArrayList<TestObject>(Arrays.asList(stateOld));
			for (int x = 0; x < stateNew.length; x++) {
				differencesCounter = -1;
				for (int y = 0; y < stateOldList.size(); y++) { //togliere le ripetizioni
					if (stateNew[x].isSameObject(stateOldList.get(y))) {
						contextAnalysis++;
						String[] propertiesNew = propertiesN[x].split("#AbT#");
						String[] propertiesOld = propertiesO.get(y).split("#AbT#");

						differencesCounter = 0;
						for (int z = 0; z < propertiesOld.length; z++) {
							if (!propertiesOld[z].equals(propertiesNew[z])) {
								differencesCounter++;
							}
						}
						r = r
						+ (((double) differencesCounter) / (double) propertiesNew.length);
						stateOldList.remove(y);
						propertiesO.remove(y);
						break;
					}
				}
				if (differencesCounter == -1) {
					r = r + 1;
				}
			}

			if(stateNew.length == 0) {
				r = 0.0;
			}
			else {
				r = r / (double) stateNew.length;
			}

			testState.addRewardValue(key, r);

			logger.info("new state length: " + stateNew.length
					+ ", old state length: " + stateOld.length + ", REWARD: " + r);
			
			
			
		} else {
			r = mappedReward;

			logger.info("new state length: " + stateNew.length
					+ ", old state length: " + stateOld.length + ", ALREADY COMPUTED REWARD: " + r);

		}
		
		//check if contextAnalysis must be enabled
		if(mappedReward == null) {
			if(!testState.getMapDataContext().containsKey(testState.getCurrentState().get(0))) {
				if((stateNew.length == stateOld.length) && (stateOld.length == contextAnalysis)) {
					//aggiorno la mappa dei dati contestuali assegnado gli stessi valori al nuovo stato
					testState.putMapDataContext((int) testState.getCurrentState().get(0), testState.getStateMapDataContext((int)testState.getOldState().get(0)));
				}else {
					// richiamo l'analisi per la ricerca dei dati contestuali
					// prima dell'updateAction per poterli eventualmente sfruttare
					DataContextFinder.getInstance().doContextAnalysis(DataContextFinder.LOCAL_ANALYSIS);
				}
			}
		}
			
		return r;
	}

	@Override
	public State getState() {
		/*
		 * to initialize the currentState (tos[]) at the beginning of each episode
		 */
		if(testState.getCurrentTos().length == 0) {
			TestObject[] newTos = testState.getStateTestObjects();
			testState.setCurrentState(newTos);
		}
		
		if(!isTerminalState())
			updateActions();
		
		return testState.getCurrentState().copy();
	}

	@Override
	public void init(State s) {
	}

	@Override
	public void initRandom() {
		testState.setCurrentTos(new TestObject[0]);
		testState.setCheck_std_err(false);
		testState.setMessage_err("");
		testState.setTerm(false);
		setExceptionStop(false);

		// the episode variable is initialized to -1
		testState.incrementEpisodeNumber();

		if (testState.getEpisodeNumber() == 0) {

			//Coverage.initExperiment(testState.getApplication());
			ExperimentFileManager.initExperiment();

		} else {
			testState.closeApplication();

			//Giovanni
			//reset dei dati errati usati durante l'episodio
			System.out.println("reset dati errati usati");
			dataManager.resetAllErroneousDataUsed();
			// fine

			ExperimentFileManager.initEpisode(testState.getEpisodeNumber());
		}

		testState.startApplication();

		if ((testState.getEpisodeNumber()) > 0) {
			RandomStrategyFromEpisodes replayer = new RandomStrategyFromEpisodes(this);
			replayer.executeReplay();
		}

	}
	
	public void updateActions() {
		int i = 0;
		
		ACTION_SET.removeAllElements();
		
		TestObject[] currentActionableTos = testState.getActionableTestObjects(testState.getLastExecutedTestObject());
		
		//Oliviero: non abbiamo test object quindi non dobbiamo aggiungere azioni 
		if (currentActionableTos==null) {
			testState.setTerm(true);
			logger.fatal("tos is null " + (testState.getEpisodeNumber()));
			return;
		}
	
		currentActionableTosID = new String[currentActionableTos.length]; 
		try {
			for (i = 0; i < currentActionableTos.length; i++) {
				String classId = currentActionableTos[i].getProperty("uIClassID").toString();
				
				currentActionableTosID[i] = classId;
				
				// il metodo analyze filtra i testObject su cui non si devono
				// eseguire azioni
				if (!filter.filterByTo(currentActionableTos[i])) {

					if (simpleActions.get(classId) != null) {
						ACTION_SET.addAll(ActionManager.getActions(currentActionableTos[i], i, classId));
					}
				}
			} 
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e.getCause());
			NewClassRecorder.recordClass(currentActionableTos[i]);
		}
		if (ACTION_SET.isEmpty()) {
			testState.setTerm(true);
			logger.fatal("There are not actions at episode " + (testState.getEpisodeNumber()));
		}
		//TesterHelper.unregister(currentActionableTos);
	}

	@Override
	public boolean isTerminalState() {
		return testState.isTerm();
	}
}