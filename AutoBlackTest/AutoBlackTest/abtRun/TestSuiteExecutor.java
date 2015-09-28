package abtRun;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;

import resources.TesterHelper;
import resources.abtRun.TestSuiteExecutorHelper;
import abt.conf.EnvironmentalSetter;
import abt.conf.TestSuiteDirectories;
import abt.conf.TestSuiteSetter;
import autoblacktest.TestEnvironment;
import autoblacktest.TestState;
import autoblacktest.actions.parameters.text.DataManager;
import autoblacktest.util.ExperimentFileManager;

import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.TestObject;

/**
 * Description   : Functional Test Script
 * @author lta
 */
public class TestSuiteExecutor extends TestSuiteExecutorHelper
{
	/**
	 * Script Name   : <b>TestSuiteExecutor</b>
	 * Generated     : <b>25/lug/2012 06:36:46</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2012/07/25
	 * @author lta
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	public void testMain(Object[] args) 
	{
		executeTestCase();

	}

	private void executeTestCase() {

		TestState testState = TestState.getInstance();
		testState.setPropertiesSet(getPropertiesSet());
		TestEnvironment env = new TestEnvironment();

		String[][] testCases = getTestCases();

		initApplicationToTest();

		for(int tc = 0; tc < testCases.length; tc++) {

			startApplication(testState, env);

			logger.info("Executing Test Case " + testCases[tc][0]);
			String [] path = testCases[tc][1].split(";");

			State sn = getState(testState, env);

			for (int i=0; i<path.length && !(path[0].equals("")); i++){
				double si = sn.get(0);
				if( env.isTerminalState() )
				{
					logger.info("test case failed: crash");
					break;
				}
				String[] trans = path[i].split(",");

				State init = getInitState(trans[0]);
				//if( si!=init.get(0))
			//	{
				//	logger.debug("test case failed: inconsistent application state");
					//break;
			//	}
				Action a = getAction(trans[1]);
				logger.info("State:"+init.get(0) +"performing action "+a.get(0)+" "+a.get(1));
				doAction(a, testState, env);
				logger.info("performed action "+a.get(0)+" "+a.get(1));
				if(env.getExceptionStop()){
					break;
				}
				sn = getState(testState, env);
			}
			closeApplication(testState, testCases[tc][0]);
			logger.info("Finisched Test Case " + testCases[tc][0]);
		}
	}

	private State getState(TestState testState, TestEnvironment env) {

		if(testState.getCurrentTos().length == 0) {
			TestObject[] newTos = testState.getStateTestObjects();
			testState.setCurrentState(newTos);
		}

		return testState.getCurrentState().copy();
	}

	private void doAction(Action a, TestState testState, TestEnvironment env) {

		testState.setMessage_err(testState.getStdErr());
		testState.setCheck_std_err(true);

		logger.info("INVOKING ACTION ON STATE "	+ (int) testState.getCurrentState().get(0));

		try {
			
			env.doSimpleAction(a, testState.getActionableTestObjects(testState.getLastExecutedTestObject()));

		} catch (WindowActivateFailedException e) {
			logger.debug("unregister in doaction");
			TesterHelper.unregisterAll();
			System.gc();

			try {
				env.doSimpleAction(a, testState.getActionableTestObjects(testState.getLastExecutedTestObject()));
			} catch (Exception e1) {
				logger.fatal("1 ERRORE FATAL IN DOACTION");
				env.setExceptionStop(true);
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
					env.doSimpleAction(a, testState.getActionableTestObjects(testState.getLastExecutedTestObject()));
				} catch (Exception e1) {
					logger.fatal("2 ERRORE FATAL IN DOACTION");
					env.setExceptionStop(true);
					e1.printStackTrace();
				}
			}
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (StackOverflowError e) {
			logger.fatal("3 ERRORE FATAL IN DOACTION");
			env.setExceptionStop(true);
			e.printStackTrace();

		}
		sleep(2);
		TestObject[] newTos = testState.getStateTestObjects();
		testState.setCurrentState(newTos);		
	}

	private void initApplicationToTest() {
		//generate the file application.bat to run the AUT
		FileWriter autBatFile;
		try {
			autBatFile = new FileWriter(System.getProperty("user.dir") + File.separator + "AppScript\\AUT.bat");
			BufferedWriter out = new BufferedWriter(autBatFile);
			out.write("call AppScript\\userAdditionalReset.bat");
			out.newLine();
			out.write("java -Xbootclasspath/p:" +
					System.getProperty("user.dir") + File.separator + "AppScript\\lib\\abtJFileChooser.jar;" + 
					" -cp %CLASSPATH%;" +
					System.getProperty("user.dir") + ";" +
					TestSuiteSetter.getAutClasspath() + ";" +
					TestSuiteSetter.getAutBinDirectory() + ";" +
					System.getProperty("user.dir") + File.separator + "lib\\jmockit\\jmockit.jar" + ";" + 
					" -javaagent:" + System.getProperty("user.dir") + File.separator + "\\lib\\jmockit\\jmockit.jar" +
					" " + TestSuiteSetter.getAutMainCLass() +
					" 1> \"" + System.getProperty("user.dir") + File.separator + "stdOut_Err\\output.log\"" + 
					" 2> \"" + System.getProperty("user.dir") + File.separator + "stdOut_Err\\error.log\""
			);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//manage the files to be used by the AUT during the execution:
		//1 backup of the files listed in "aut_configuation_file_path" and "aut_working_file_path" 
		//the backup files will be used at the beginning of each experiment
		String [] autConfFiles = TestSuiteSetter.getAutConfigurationFilePath().split(";");
		for(String autConfFile : autConfFiles) {
			String fileName = autConfFile.substring(autConfFile.lastIndexOf("\\")+1);
			ExperimentFileManager.copyFile(autConfFile, System.getProperty("user.dir") + File.separator + "autResources\\confFiles" + File.separator + fileName);
		}
		File abtMyDocumentsDir = new File(System.getProperty("user.home") + System.getProperty("file.separator")+ "abtMyDocuments");
		File[] oldWorkFiles = abtMyDocumentsDir.listFiles();
		for (File oldWorkFile : oldWorkFiles)
			ExperimentFileManager.delDir(oldWorkFile);
		String [] autWorkFiles = TestSuiteSetter.getAutWorkingFilePath().split(";");
		for(String autWorkFile : autWorkFiles) {
			String fileName = autWorkFile.substring(autWorkFile.lastIndexOf("\\")+1);
			ExperimentFileManager.copyFile(autWorkFile, System.getProperty("user.home") + System.getProperty("file.separator")+ "abtMyDocuments" + File.separator + fileName);
			ExperimentFileManager.copyFile(autWorkFile, System.getProperty("user.dir") + File.separator + "autResources\\workingFiles" + File.separator + fileName);
		}	

	}

	private void closeApplication(TestState testState, String testCase) {
		String stdOutDirectory = TestSuiteDirectories.getInstance().getStdOutTestSuiteDirectory();
		String stdErrDirectory = TestSuiteDirectories.getInstance().getStdErrTestSuiteDirectory();
		String testDirectory = TestSuiteDirectories.getInstance().getTestSuiteDirectory();

		//per copiare i file di output e di error
		ExperimentFileManager.copyFile(System.getProperty("user.dir")+"\\stdOut_Err\\output.log", stdOutDirectory+ "\\output" + testCase +".log");
		ExperimentFileManager.copyFile(System.getProperty("user.dir")+"\\stdOut_Err\\error.log", stdErrDirectory+ "\\error" + testCase +".log");
		ExperimentFileManager.copyFile(System.getProperty("user.dir")+"\\log\\exploration.log", testDirectory +"\\exploration.log");
		//Giovanni
		//reset dei dati errati usati durante l'episodio
		System.out.println("reset dati errati usati");
		DataManager.getInstance().resetAllErroneousDataUsed();
		
		endTestCase();

		testState.killApplication();

	}

	private void startApplication(TestState testState, TestEnvironment env) {
		testState.setCurrentTos(new TestObject[0]);
		testState.setCheck_std_err(false);
		testState.setMessage_err("");
		testState.setTerm(false);
		env.setExceptionStop(false);
		
		testState.startApplication();
	}

	private void endTestCase() {
		
		//manage the file used by the AUT during the execution:
		//1 delete the file in "aut_configuation_file_path" and "aut_working_file_path" used in the previous episode
		//2 restore the files in "aut_configuation_file_path" and "aut_working_file_path" by using the backup files
		String [] autConfFiles = EnvironmentalSetter.getAutConfigurationFilePath().split(";");
		for(String autConfFile : autConfFiles) {
			ExperimentFileManager.delDir(new File(autConfFile));
		}
		File autResourcesDir = new File(System.getProperty("user.dir") + File.separator + "autResources\\confFiles");
		File[] autBackupConfFiles = autResourcesDir.listFiles();
		for (File autBackupConfFile : autBackupConfFiles) {
			String fileName = autBackupConfFile.getAbsolutePath().substring(autBackupConfFile.getAbsolutePath().lastIndexOf("\\")+1);
			for(String autConfFile : autConfFiles) {
				if (fileName.equals(autConfFile.substring(autConfFile.lastIndexOf("\\")+1)))
					ExperimentFileManager.copyFile(autBackupConfFile.getAbsolutePath(), autConfFile);
			}
		}
		File abtMyDocumentsDir = new File(System.getProperty("user.home") + System.getProperty("file.separator")+ "abtMyDocuments");
		File[] oldWorkFiles = abtMyDocumentsDir.listFiles();
		for (File oldWorkFile : oldWorkFiles)
			ExperimentFileManager.delDir(oldWorkFile);
		autResourcesDir = new File(System.getProperty("user.dir") + File.separator + "autResources\\workingFiles");
		File[] autBackupWorkFiles = autResourcesDir.listFiles();
		for (File autBackupWorkFile : autBackupWorkFiles) {
			String fileName = autBackupWorkFile.getAbsolutePath().substring(autBackupWorkFile.getAbsolutePath().lastIndexOf("\\")+1);
			ExperimentFileManager.copyFile(autBackupWorkFile.getAbsolutePath(), System.getProperty("user.home") + System.getProperty("file.separator")+ "abtMyDocuments" + File.separator + fileName);
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
		//finalState = finalState.substring(1, finalState.length());
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

	private HashMap<String, Integer> getPropertiesSet() {

		HashMap<String, Integer> propertiesSet = null;
		String propertiesSetFileName = TestSuiteSetter.getPropertiesSetFile();

		File fileSer = new File(propertiesSetFileName);
		if(!fileSer.exists()) {
			logger.error(fileSer.getAbsolutePath() + " does not exist");
			return null;
		}

		FileInputStream fis;
		try {
			fis = new FileInputStream(fileSer);
			ObjectInputStream ois = new ObjectInputStream(fis);
			propertiesSet = (HashMap<String, Integer>) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propertiesSet;
	}

	private String[][] getTestCases() {
		String[][] testCases = null;

		String testSuiteFileName = TestSuiteSetter.getTestSuiteFolder();
		BufferedReader bf = null;

		File fileSer = new File(testSuiteFileName);
		if(!fileSer.exists()) {
			logger.error(fileSer.getAbsolutePath() + " does not exist");
			return null;
		}

		if(fileSer.isFile()) {
			try {
				bf = new BufferedReader(new FileReader(testSuiteFileName));

				testCases = new String[1][2];

				testCases[0][0] = fileSer.getName();
				testCases[0][1] = bf.readLine();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			GenericExtFilter filter = new GenericExtFilter(".tc");
			String[] files = fileSer.list(filter);
			testCases = new String[files.length][2];
			for (int i=0; i < files.length; i++) {
				try {
					bf = new BufferedReader(new FileReader(fileSer.getAbsolutePath() + File.separator + files[i]));

					testCases[i][0] = new File(files[i]).getName();
					testCases[i][1] = bf.readLine();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return testCases;
	}

	// inner class, generic extension filter
	public class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
}

