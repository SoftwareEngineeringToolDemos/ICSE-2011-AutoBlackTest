package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.ToggleGUITestObject;

public class CheckBoxMenuItemUI extends TesterHelper {
	
	public static void click (TestObject testObject) throws IOException {


		new ToggleGUITestObject(testObject).click();
	}
	
//	public static void select (TestObject testObject, BufferedWriter responderGeneratorWriter) throws IOException {
//		responderGeneratorWriter.write("testObject = to.find(atDescendant(\".class\", " + "\"" + testObject.getProperty(".class") + "\"" + ", " + "\"location\", " + "\"" + testObject.getProperty("location") + "\"));");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("new ToggleGUITestObject(testObject[0]).select();");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("sleep(1000);");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.flush();
//		
//		new ToggleGUITestObject(testObject).select();
//	}
	
//	public static void deselect (TestObject testObject, BufferedWriter responderGeneratorWriter) throws IOException {
//		responderGeneratorWriter.write("testObject = to.find(atDescendant(\".class\", " + "\"" + testObject.getProperty(".class") + "\"" + ", " + "\"location\", " + "\"" + testObject.getProperty("location") + "\"));");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("new ToggleGUITestObject(testObject[0]).deselect();");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("sleep(1000);");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.flush();
//		
//		new ToggleGUITestObject(testObject).deselect();
//	}
	
//	public static void indeterminate (TestObject testObject, BufferedWriter responderGeneratorWriter) throws IOException {
//		responderGeneratorWriter.write("testObject = to.find(atDescendant(\".class\", " + "\"" + testObject.getProperty(".class") + "\"" + ", " + "\"location\", " + "\"" + testObject.getProperty("location") + "\"));");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("new ToggleGUITestObject(testObject[0]).indeterminate();");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("sleep(1000);");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.flush();
//		
//		new ToggleGUITestObject(testObject).indeterminate();
//	}
}
