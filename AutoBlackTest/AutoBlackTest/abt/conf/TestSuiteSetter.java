package abt.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class TestSuiteSetter {

	/*
	 * AUT configuration paramenters
	 */
	private static String autClasspath;
	private static String autBinDirectory;
	private static String autMainCLass;
	private static String autConfigurationFilePath;
	private static String autWorkingFilePath;
	
	private static String testSuiteFolder;
	private static String propertiesSetFile;

	private static Logger logger = Logger.getLogger(TestSuiteSetter.class);

	private static void setConfigurationProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("abt-config\\testsuite.properties"));
			
			setTestSuiteFolder(properties.getProperty("testSuite_folder"));
			setPropertiesSetFile(properties.getProperty("propertiesSet_file"));
			
			setAutClasspath(properties.getProperty("aut_classpath"));
			setAutBinDirectory(properties.getProperty("aut_bin_directory"));
			setAutMainCLass(properties.getProperty("aut_main_class"));
			setAutConfigurationFilePath(properties.getProperty("aut_configuation_file_path"));
			setAutWorkingFilePath(properties.getProperty("aut_working_file_path"));

		} catch (FileNotFoundException e) {
			logger.error("Properties file has been not found.");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			logger.error("Properties file has not been opened.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String getTestSuiteFolder() {
		if(testSuiteFolder == null)
			setConfigurationProperties();
		return testSuiteFolder;
	}

	public static void setTestSuiteFolder(String testSuiteFolder) {
		TestSuiteSetter.testSuiteFolder = testSuiteFolder;
	}

	public static String getPropertiesSetFile() {
		if(propertiesSetFile == null)
			setConfigurationProperties();
		return propertiesSetFile;
	}

	public static void setPropertiesSetFile(String propertiesSetFile) {
		TestSuiteSetter.propertiesSetFile = propertiesSetFile;
	}

	public static String getAutClasspath() {
		if (autClasspath == null)
			setConfigurationProperties();
		return autClasspath;
	}

	public static void setAutClasspath(String autClasspath) {
		TestSuiteSetter.autClasspath = autClasspath;
	}

	public static String getAutBinDirectory() {
		if (autBinDirectory == null)
			setConfigurationProperties();
		return autBinDirectory;
	}

	public static void setAutBinDirectory(String autBinDirectory) {
		TestSuiteSetter.autBinDirectory = autBinDirectory;
	}

	public static String getAutMainCLass() {
		if (autMainCLass == null)
			setConfigurationProperties();
		return autMainCLass;
	}

	public static void setAutMainCLass(String autMainCLass) {
		TestSuiteSetter.autMainCLass = autMainCLass;
	}

	public static String getAutConfigurationFilePath() {
		if (autConfigurationFilePath == null)
			setConfigurationProperties();
		return autConfigurationFilePath;
	}

	public static void setAutConfigurationFilePath(String autConfigurationFilePath) {
		TestSuiteSetter.autConfigurationFilePath = autConfigurationFilePath;
	}

	public static String getAutWorkingFilePath() {
		if (autWorkingFilePath == null)
			setConfigurationProperties();
		return autWorkingFilePath;
	}

	public static void setAutWorkingFilePath(String autWorkingFilePath) {
		TestSuiteSetter.autWorkingFilePath = autWorkingFilePath;
	}

}
