The tool runs an automation script for analysis. Hence, on startup, an instance of IBM Rational Function Tester should open up with AutoBlackTest\abtRun\Tester.java opened.


Steps to Run:
--------------------------------------------------------------------------------------------------------
1) Open IBM Rational Functional Tester. Could be done from the 'Tester.java-RFT' shortcut available on desktop. The tool would be opened.

2) Click on 'Run Functional Test Script' in the toolbar, or press Ctrl+Shift+F11

3) When asked 'Log File already exists, overwrite?' - Click on yes

4) ---OPTIONAL (Only when playback exits before application opens)---

Once the playback starts, a command prompt would open. Wait for the command prompt to execute a few statements and then pause the playback, and wait for Buddi app to open. (This step could be optional, but is recommended since different systems might take different times to compile and open the app and the AutoBlackTest tool has a timeout for finding objects, after which it results in shutting the application. Additionally, sometimes the pause is not registered by Rational Functional Tester, you may want to re-try the playback again.)

5) Let tool run, it would take 5-10 minutes for the automation to complete.

6) In the browser, the test results output would be shown.


AutoBlackTest (http://www.lta.disco.unimib.it/tools/AutoBlackTest/AutoBlackTest/Home.html):
----------------------------------------------------------------------------------------------------------

Presented at ICSE, 2011

Automatic test case generation is a key ingredient of an efficient and cost-effective software verification process. AutoBlackTest focuses on testing applications that interact with the users through a GUI, and generates test cases at the system level. AutoBlackTest uses reinforcement learning to learn how to interact with the application under test and stimulate its functionalities.

Authors - 
Riganelli Oliviero - riganelli@disco.unimib.it
Mauro Santoro - santoro@disco.unimib.it
Leonardo Mariani- mariani@disco.unimib.it
Mauro Pezze - pezze@disco.unimib.it

Project Repository(shared) - https://github.com/SoftwareEngineeringToolDemos/ICSE-2011-AutoBlackTest

Paper Link - (http://dl.acm.org/citation.cfm?doid=1985793.1985979)


Note: The tool itself is only responsible for generating test coverage information '.ser' files, and test cases. I've included a few statements to run Cobertura at the end of test to auto generate a report based on the datafile. Please note that report generation isn't AutoBlackTest's responsibility.
