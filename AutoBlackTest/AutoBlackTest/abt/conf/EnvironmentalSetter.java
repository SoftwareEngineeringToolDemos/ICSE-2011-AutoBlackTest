package abt.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class EnvironmentalSetter {
	
	/*
	 * abt configuration paramenters
	 */
	private static String numberOfEpisodes;
	private static String numberOfSteps;
	private static String sleepTime;
	private static String widgetLabelsToFilter;
	private static String execTime;
	
	/*
	 * AUT configuration paramenters
	 */
	private static String autClasspath;
	private static String autBinDirectory;
	private static String autMainCLass;
	private static String autConfigurationFilePath;
	private static String autWorkingFilePath;
	private static String serilizedObjectDir;
	
	private static Logger logger = Logger.getLogger(EnvironmentalSetter.class);
	
	private static void setConfigurationPropertiesTestSuiteExecutor() {
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream("abt-config\\testsuite.properties"));
			
			setNumberOfEpisodes(properties.getProperty("number_of_episodes"));
			setNumberOfSteps(properties.getProperty("number_of_steps"));
			setSleepTime(properties.getProperty("sleep_time"));
			
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
	
	private static void setConfigurationPropertiesABT() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("abt-config\\abt.properties"));
			
			setNumberOfEpisodes(properties.getProperty("number_of_episodes"));
			setNumberOfSteps(properties.getProperty("number_of_steps"));
			setSleepTime(properties.getProperty("sleep_time"));
			setExecutionTime(properties.getProperty("hours"), properties.getProperty("minutes"));
			
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

	private static void setConfigurationPropertiesAUT() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("abt-config\\aut.properties"));
			
			setAutClasspath(properties.getProperty("aut_classpath"));
			setAutBinDirectory(properties.getProperty("aut_bin_directory"));
			setAutMainCLass(properties.getProperty("aut_main_class"));
			setAutConfigurationFilePath(properties.getProperty("aut_configuation_file_path"));
			setAutWorkingFilePath(properties.getProperty("aut_working_file_path"));
			
			setSerializedObjectDirectory(properties.getProperty("serializedObjectDir"));
			
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
	
	public static int getNumberOfEpisodes() {
		if (numberOfEpisodes == null )
			setConfigurationPropertiesABT();
		return Integer.parseInt(numberOfEpisodes);
	}

	public static void setNumberOfEpisodes(String numberOfEpisodes) {
		EnvironmentalSetter.numberOfEpisodes = numberOfEpisodes;
	}

	public static int getNumberOfSteps() {
		if (numberOfSteps == null)
			setConfigurationPropertiesABT();
		return Integer.parseInt(numberOfSteps);
	}

	public static void setNumberOfSteps(String numberOfSteps) {
		EnvironmentalSetter.numberOfSteps = numberOfSteps;
	}

	public static double getSleepTime() {
		if (sleepTime == null)
			setConfigurationPropertiesABT();
		return Double.parseDouble(sleepTime);
	}

	public static void setSleepTime(String sleepTime) {
		EnvironmentalSetter.sleepTime = sleepTime;
	}

	public static String getWidgetLabelsToFilter() {
		return widgetLabelsToFilter;
	}

	public static void setWidgetLabelsToFilter(String widgetLabelsToFilter) {
		EnvironmentalSetter.widgetLabelsToFilter = widgetLabelsToFilter;
	}
	
	public static long getExecutionTime() {
		if (execTime == null)
			setConfigurationPropertiesABT();
		return Long.parseLong(execTime);
	}
	
	private static void setExecutionTime(String h, String m) {
		long hMillisec = (((Long.parseLong(h)*60)*60)*1000);
		long mMillisec = ((Long.parseLong(m)*60)*1000);
		
		EnvironmentalSetter.execTime = Long.toString(hMillisec + mMillisec);
	}

	public static String getAutClasspath() {
		if (autClasspath == null)
			setConfigurationPropertiesAUT();
		return autClasspath;
	}

	public static void setAutClasspath(String autClasspath) {
		EnvironmentalSetter.autClasspath = autClasspath;
	}

	public static String getAutBinDirectory() {
		if (autBinDirectory == null)
			setConfigurationPropertiesAUT();
		return autBinDirectory;
	}

	public static void setAutBinDirectory(String autBinDirectory) {
		EnvironmentalSetter.autBinDirectory = autBinDirectory;
	}

	public static String getAutMainCLass() {
		if (autMainCLass == null)
			setConfigurationPropertiesAUT();
		return autMainCLass;
	}

	public static void setAutMainCLass(String autMainCLass) {
		EnvironmentalSetter.autMainCLass = autMainCLass;
	}

	public static String getAutConfigurationFilePath() {
		if (autConfigurationFilePath == null)
			setConfigurationPropertiesAUT();
		return autConfigurationFilePath;
	}

	public static void setAutConfigurationFilePath(String autConfigurationFilePath) {
		EnvironmentalSetter.autConfigurationFilePath = autConfigurationFilePath;
	}

	public static String getAutWorkingFilePath() {
		if (autWorkingFilePath == null)
			setConfigurationPropertiesAUT();
		return autWorkingFilePath;
	}

	public static void setAutWorkingFilePath(String autWorkingFilePath) {
		EnvironmentalSetter.autWorkingFilePath = autWorkingFilePath;
	}
	
	public static String getSerializedObjectDirectory() {
		if (serilizedObjectDir == null)
			setConfigurationPropertiesAUT();
		return serilizedObjectDir;
	}
	
	public static void setSerializedObjectDirectory(String serilizedObjectDir) {
		EnvironmentalSetter.serilizedObjectDir = serilizedObjectDir;
	}
}


