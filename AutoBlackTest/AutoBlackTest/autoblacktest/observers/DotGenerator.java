//DOT: The class manages the creation of Dot files
package autoblacktest.observers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;
import org.hswgt.teachingbox.core.rl.experiment.ExperimentObserver;
import org.hswgt.teachingbox.core.rl.tabular.TabularQFunction;
import org.hswgt.teachingbox.core.rl.valuefunctions.QFunction;

import abt.conf.ExperimentDirectories;
import autoblacktest.TestState;
import autoblacktest.actions.ActionManager;
import autoblacktest.actions.parameters.ParameterNumberFormat;

public class DotGenerator implements ExperimentObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QFunction qFunction;
	private int episodeNumber=-1;
	private int sequenceNumber=0;
	private HashMap <Integer, String[]> episodeTrace;
	
	private Logger logger = Logger.getLogger(DotGenerator.class);

	
	
	public DotGenerator(QFunction q) {
		// TODO Auto-generated constructor stub
		this.qFunction = q;
		
	}

	@Override
	public void update(State s, Action a, State sn, Action an, double r, boolean terminalState) {
		// TODO Auto-generated method stub
		
		String[] transitionInfo = new String[5];
		int initialState = (int)s.get(0);
		transitionInfo[0]= Integer.toString(initialState);
		int finalState = (int)sn.get(0);
		transitionInfo[1]= Integer.toString(finalState);
		String action= TestState.getInstance().getLastInvokedMethod();
		transitionInfo[2]= action;
		String reward = Double.toString(r);
		transitionInfo[3]= reward;
		System.out.println(qFunction);
		String qValue = Double.toString(qFunction.getValue(s, a));
		transitionInfo[4]= qValue;
		
		episodeTrace.put(sequenceNumber, transitionInfo);
		sequenceNumber++;
	}

	@Override
	public void updateExperimentStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateExperimentStop() {
		// TODO Auto-generated method stub
		episodeToDot(episodeNumber);

	}

	@Override
	public void updateNewEpisode(State initialState) {
		// TODO Auto-generated method stub
		episodeNumber++;
		if (episodeNumber >0){
			episodeToDot(episodeNumber-1);
		}
		episodeTrace=new HashMap<Integer, String[]>();
		sequenceNumber=0;

	}

	private void episodeToDot(int epNum) {
		// TODO Auto-generated method stub
		String dotDirectoryPath = ExperimentDirectories.getInstance().getExperimentDirectory()+File.separator+"DotFiles";
		File dotDirectory = new File(dotDirectoryPath);
		if (!dotDirectory.exists()){
			dotDirectory.mkdir();
		}
		String dotFilePath= dotDirectoryPath+File.separator+"episode_"+epNum+".dot";
		
		FileWriter fw;
		BufferedWriter bw;
		
		try {
			fw = new FileWriter(dotFilePath, false);
			bw = new BufferedWriter(fw);
			//init DOT file
			bw.write("digraph stategraph {");
			bw.newLine();
			Set<Entry<Integer, String[]>> entries = episodeTrace.entrySet();
			DecimalFormat integerFormatter = new DecimalFormat("00");
			
			for (Entry<Integer, String[]> entry : entries) {
				// "00 window1" -> "01 window2" [label="action"]
				bw.write("\"" + 
						integerFormatter.format(Integer.parseInt(entry.getValue()[0])) + 
						"\" -> \"" +
						integerFormatter.format(Integer.parseInt(entry.getValue()[1])) +
						"\"" +
						"[label= \"("+entry.getKey()+")" + 
						entry.getValue()[2] +
						" [R:" + entry.getValue()[3]  + "]"+
						" [Q:" + entry.getValue()[4]  + "]"+
						"\"];");
				bw.newLine();
			}
			//ending DOT file
			bw.write("}");
			bw.newLine();
			
			bw.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
		logger.debug("Saving model to DOT file done");

		
	    	
		
	}

}
