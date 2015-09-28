package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import autoblacktest.actions.parameters.ParameterNumberFormat;
import autoblacktest.actions.parameters.text.DataManager;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.TopLevelTestObject;


public class TextFieldUI extends TesterHelper {
	
	public static void setText(TestObject testObject, long index) throws IOException {

		//String text = testObject.getProperty("text").toString();
		//if (text.matches(".\\d*.\\d*")) {
		//	text = text.replace(',', '.');
		//	try {
		//		Double.parseDouble(text);
		//		text = 100.0 + "";
		//	} catch (NumberFormatException e) {
		//		text = "generic string";
		//	}
		//} else {
		//	text = "generic string";
		//}

		//recupero gli indici della hashmap dei dati
		long[] parameters=ParameterNumberFormat.getDoubleParameter(index);
		//recupero il valore da insierire dalla hashmap dei dati 
		String text=DataManager.getInstance().getData((int)parameters[0],(int)parameters[1]);
		new TextGuiSubitemTestObject(testObject).setProperty("text", text);
		// TopLevelTestObject top = (TopLevelTestObject)
		// testObject.getTopParent();
		// top.inputKeys("{TAB}");
	}
	
// METODO ORIGINALE
  /*
	public static void setText(TestObject testObject) throws IOException {
		if (testObject.getProperty("editable").toString().equals("true")) {
			String text = testObject.getProperty("text").toString();
			if (text.matches(".\\d*.\\d*")) {
				text = text.replace(',', '.');
				try {
					Double.parseDouble(text);
					text = 100.0 + "";
				} catch (NumberFormatException e) {
					text = "generic string";
				}
			} else {
				text = "generic string";
			}

			new TextGuiSubitemTestObject(testObject).setText(text);
			// TopLevelTestObject top = (TopLevelTestObject)
			// testObject.getTopParent();
			// top.inputKeys("{TAB}");

		}
	}
*/
}