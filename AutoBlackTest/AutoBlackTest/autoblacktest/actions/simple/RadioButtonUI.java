package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.ToggleGUITestObject;

public class RadioButtonUI extends TesterHelper {

	public static void clickSelect(TestObject testObject) {
		// if (testObject.getProperty("selected").toString().equals("true")) {
		new ToggleGUITestObject(testObject).clickToState(SELECTED);
		// }
	}

	public static void clickNotSelect(TestObject testObject) {
		if (testObject.getProperty("selected").toString().equals("true")) {
			new ToggleGUITestObject(testObject).clickToState(NOT_SELECTED);
		}
	}

}
