package abt.conf;

import java.io.File;

import org.apache.log4j.Logger;

import autoblacktest.util.DateUtility;


/**
 * Utility class used to create, store and retrieve all directories used during an experiment
 * 
 * @author Andrea Mattavelli
 */
public class ExperimentDirectories {

	private static ExperimentDirectories instance;	// class instance
	
	public static String baseDirectory = System.getProperty("user.dir") + File.separator + "outputs";		// project directory
	private String experimentDirectory;				// experiment directory
	private String coverageExperimentDirectory;		// cobertura coverage files directory
	private String stateExperimentDirectory;		// recorded properties set
	private String stdOutExperimentDirectory;		// stdOut log files directory
	private String stdErrExperimentDirectory;		// stdErr log files directory
	private String testCasesDirectory;
	public static String sharedDate;
	private Logger logger = Logger.getLogger(ExperimentDirectories.class);	// class logger
	
	public static ExperimentDirectories getInstance() {
		if(instance == null) {
			instance = new ExperimentDirectories();
		}
		
		return instance;
	}
	
	private ExperimentDirectories() {
		this.experimentDirectory = baseDirectory  + File.separator + "experiment_" + DateUtility.now();
		sharedDate = DateUtility.now();
		this.coverageExperimentDirectory = experimentDirectory + File.separator + "coverage";
		this.stateExperimentDirectory = experimentDirectory + File.separator + "state";
		this.stdOutExperimentDirectory = experimentDirectory + File.separator + "stdOut";
		this.stdErrExperimentDirectory = experimentDirectory + File.separator + "stdErr";
		this.testCasesDirectory = experimentDirectory + File.separator + "TestCases";
		checkDirectoriesExistance();
	}
	
	private void checkDirectoriesExistance() {
		logger.info("Checking experiment directories existance");
		
		File baseDirectoryFile = new File(baseDirectory);
		
		if(!baseDirectoryFile.exists()) {
			logger.debug("Base directory doesn't exists. Invoking mkdirs");
			
			baseDirectoryFile.mkdirs();
		}
		
		File directories[] = new File[] {
				new File(experimentDirectory),
				new File(coverageExperimentDirectory),
				new File(coverageExperimentDirectory + File.separator + "ser"),
				new File(stateExperimentDirectory),
				new File(stdErrExperimentDirectory),
				new File(testCasesDirectory),
				new File(stdOutExperimentDirectory)};
		
		for (File directory : directories) {
			if(!directory.exists()) {
				logger.debug("Directory " + directory.getName() + " doesn't exists. Invoking mkdir");
				
				directory.mkdir();
			}
		}
		
		logger.debug("Checking experiment directories existance done");
	}
	
	
	
	
	public String getExperimentDirectory() {
		return experimentDirectory;
	}
	
	public String getStdErrExperimentDirectory() {
		return stdErrExperimentDirectory;
	}
	
	public String getStdOutExperimentDirectory() {
		return stdOutExperimentDirectory;
	}
	
	public String getCoverageExperimentDirectory() {
		return coverageExperimentDirectory;
	}
	
	public String getStateExperimentDirectory() {
		return stateExperimentDirectory;
	}
	
	public String getTestCasesDirectory() {
		return this.testCasesDirectory;
	}
	
}
