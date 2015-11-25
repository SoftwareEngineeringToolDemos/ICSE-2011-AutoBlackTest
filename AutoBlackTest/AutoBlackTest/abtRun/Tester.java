package abtRun;

import autoblacktest.observers.DotGenerator;
import autoblacktest.observers.TestExperimentObserver;
import autoblacktest.observers.TestSuiteObserver;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.agent.Agent;
import org.hswgt.teachingbox.core.rl.learner.TabularQLearner;
import org.hswgt.teachingbox.core.rl.policy.EpsilonGreedyPolicy;
import org.hswgt.teachingbox.core.rl.tabular.HashQFunction;
import org.hswgt.teachingbox.core.rl.tabular.TabularQFunction;

import resources.abtRun.TesterHelper;
import abt.conf.EnvironmentalSetter;
import autoblacktest.TestEnvironment;
import autoblacktest.TestExperimentByEpisode;
import autoblacktest.TestExperimentByTime;
import autoblacktest.state.PropertyManager;

/**
 * Description   : Functional Test Script
 * @author Administrator
 */
public class Tester extends TesterHelper {
	/**
	 * Script Name   : <b>ABT</b>
	 * Generated     : <b>08/giu/2010 14:21:24</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2010/06/08
	 * @author Administrator
	 */
	
	public void testMain(Object[] args) {
		
		Logger.getRootLogger().setLevel(Level.DEBUG);
		
		//set property to use to identify states
		PropertyManager.addStandardProperties();
		
		TabularQFunction Q = new HashQFunction(0);
		
		// setup environment
		TestEnvironment env = new TestEnvironment();

		// setup policy
        // we use a greedy e-policy with epsilon 0.9 here
        // in order to make decisions we have to provide the
        // QFunction and the set of all possible action
		
		EpsilonGreedyPolicy pi = new EpsilonGreedyPolicy(Q, TestEnvironment.ACTION_SET, 0.8);
		
		// create agent
		Agent agent = new Agent(pi);

		final double alpha     = 1.0;
		final double gamma     = 0.9;
		final double lambda    = 0.9; 
		
		
		// setup experiment per episodes
		//final int MAX_EPISODES = EnvironmentalSetter.getNumberOfEpisodes();
		//final int MAX_STEPS    = EnvironmentalSetter.getNumberOfSteps();
		//TestExperimentByEpisode experiment = new TestExperimentByEpisode(agent, env, MAX_EPISODES, MAX_STEPS);
		
		//setup experiment per time
		final long execTime = EnvironmentalSetter.getExecutionTime();
		TestExperimentByTime experiment = new TestExperimentByTime(agent, env, execTime);
		
		// setup learner
		TabularQLearner learner = new TabularQLearner(Q);
		learner.setAlpha(alpha);
		learner.setGamma(gamma);
		learner.setLambda(lambda);
		
		// attach learner to agent
		agent.addObserver(learner);
		
		// Model creation of the application under test.
		experiment.addObserver(new TestExperimentObserver());
		
		// Test Suite Observer
		//experiment.addObserver(new TestSuiteObserver(learner, Q));
		
		//DOT: Dot file creation
		//experiment.addObserver(new DotGenerator(Q));
		experiment.addObserver(new DotGenerator(Q));
		experiment.run();
		
	}	
}
