package results.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler;
import net.sourceforge.cobertura.coveragedata.ProjectData;

import org.apache.log4j.Logger;

import abt.conf.ExperimentDirectories;
import autoblacktest.util.ExperimentFileManager;

public class TestCasesFromCoverage {

	private static Logger logger = Logger.getLogger(TestCasesFromCoverage.class);

	public static ArrayList<Integer> getTestCases() {
		ArrayList<Integer> testCases = new ArrayList<Integer>();
		SortedMap<Double, String> ht = Collections.synchronizedSortedMap (new TreeMap<Double, String> () );;
		String directory=ExperimentDirectories.getInstance().getCoverageExperimentDirectory()+"\\ser";
		File dir = new File(directory);
		String [] files = dir.list();
		if (files == null) {

		} else {

			for (int i=0; i<(files.length-2); i++) {

				File dataFile = new File(directory+"\\cobertura"+i+".ser");
				ProjectData projectData = CoverageDataFileHandler.loadCoverageData(dataFile);
				Double d = projectData.getLineCoverageRate();

				if (ht.containsKey(d)){
					String episodes = ht.get(d);

					episodes = episodes+","+i;
					ht.put(d, episodes);
				}else{

					ht.put(d, Integer.toString(i));
				}
			}
		}
		ProjectData globalCobertura = CoverageDataFileHandler.loadCoverageData(new File(directory+ File.separator + "totalCobertura.ser"));

		while(!(ht.isEmpty())){
			Double key = ht.lastKey();
			String value = ht.get(key);

			String[] episodes =  value.split(",");
			for(int i =0; i<episodes.length;i++){

				File dataFile = new File(directory+"\\cobertura"+episodes[i]+".ser");
				ProjectData episodeCobertura = CoverageDataFileHandler.loadCoverageData(dataFile);
				double beforeMerge=globalCobertura.getLineCoverageRate();
				globalCobertura.merge(episodeCobertura);
				double afterMerge = globalCobertura.getLineCoverageRate();
				if (afterMerge>beforeMerge){
					testCases.add(Integer.parseInt(episodes[i]));
				}	
			}
			ht.remove(key);
		}

		CoverageDataFileHandler.saveCoverageData(globalCobertura, new File(directory+ File.separator + "totalCobertura.ser"));
		logger.info("Total cobertura from the experiment " + globalCobertura.getLineCoverageRate());

		return testCases;

	}

	public static void generateTestCases() {

		ArrayList<Integer> testCases = TestCasesFromCoverage.getTestCases();
		ExperimentDirectories testCasesDir = ExperimentDirectories.getInstance();
		File testSuiteDir = new File(testCasesDir.getTestCasesDirectory() + File.separator + "testSuite");
		testSuiteDir.mkdir();
		for(int testCase : testCases) {
			ExperimentFileManager.copyFile(testCasesDir.getTestCasesDirectory() + File.separator + "TestCase_" + testCase + ".tc", testSuiteDir.getAbsolutePath() + File.separator + "TestCase_" + testCase + ".tc");
		}
	}
}
