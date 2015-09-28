package abt.conf;

import java.io.File;

import org.apache.log4j.Logger;

import autoblacktest.util.DateUtility;


public class TestSuiteDirectories {

	private static TestSuiteDirectories instance;	// class instance
	
	private String baseDirectory;		// project directory
	private String testDirectory;				// experiment directory
	private String stdOutExperimentDirectory;		// stdOut log files directory
	private String stdErrExperimentDirectory;		// stdErr log files directory

	private Logger logger = Logger.getLogger(ExperimentDirectories.class);	// class logger
	
	public static TestSuiteDirectories getInstance() {
		if(instance == null) {
			instance = new TestSuiteDirectories();
		}
		
		return instance;
	}
	
	private TestSuiteDirectories() {
		setBaseDirectory();//TestSuiteSetter.getTestSuiteFolder();
		
		this.testDirectory = baseDirectory  + File.separator + "results";
		this.stdOutExperimentDirectory = testDirectory + File.separator + "stdOut";
		this.stdErrExperimentDirectory = testDirectory + File.separator + "stdErr";
		checkDirectoriesExistance();
	}
	
	private void setBaseDirectory() {
		this.baseDirectory = TestSuiteSetter.getTestSuiteFolder();
		if(!new File(this.baseDirectory).isDirectory())
			this.baseDirectory = new File(this.baseDirectory).getParent();
		
	}

	private void checkDirectoriesExistance() {
		logger.info("Checking test directories existance");
		
		File baseDirectoryFile = new File(baseDirectory);
		
		if(!baseDirectoryFile.exists()) {
			logger.debug("Base directory doesn't exists. Invoking mkdirs");
			
			baseDirectoryFile.mkdirs();
		}
		
		File directories[] = new File[] {
				new File(testDirectory),
				new File(stdErrExperimentDirectory),
				new File(stdOutExperimentDirectory)};
		
		for (File directory : directories) {
			if(!directory.exists()) {
				logger.debug("Directory " + directory.getName() + " doesn't exists. Invoking mkdir");
				
				directory.mkdir();
			}
		}
		
		logger.debug("Checking experiment directories existance done");
	}
	
	public String getTestSuiteDirectory() {
		return testDirectory;
	}
	
	public String getStdErrTestSuiteDirectory() {
		return stdErrExperimentDirectory;
	}
	
	public String getStdOutTestSuiteDirectory() {
		return stdOutExperimentDirectory;
	}
}
