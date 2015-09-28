package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import autoblacktest.actions.parameters.ParameterNumberFormat;
import autoblacktest.actions.parameters.text.DataManager;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiSubitemTestObject;


public class TextPaneUI extends TesterHelper {


	public static void setText(TestObject testObject, long index) throws IOException {
		//recupero gli indici della hashmap dei dati
		long[] parameters=ParameterNumberFormat.getDoubleParameter(index);
		//recupero il valore da insierire dalla hashmap dei dati 
		String text=DataManager.getInstance().getData((int)parameters[0],(int)parameters[1]);
		new TextGuiSubitemTestObject(testObject).setProperty("text", text);
	}
	//old version
	//public static void setText(TestObject testObject) throws IOException {
	//	if (testObject.getProperty("editable").toString().equals("true")) {
	//		new TextGuiSubitemTestObject(testObject).setText("general jtextpane string");
	//	}
	// }

}

