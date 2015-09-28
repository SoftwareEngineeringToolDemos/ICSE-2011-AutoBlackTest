package autoblacktest.actions.simple;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.ToggleGUITestObject;

public class ToggleButtonUI extends TesterHelper {
	
	public static void click (TestObject testObject) {
		new ToggleGUITestObject(testObject).click();
	}

}
