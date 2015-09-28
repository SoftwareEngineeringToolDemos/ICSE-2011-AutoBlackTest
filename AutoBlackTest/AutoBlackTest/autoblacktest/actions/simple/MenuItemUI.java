package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.ToggleGUITestObject;

public class MenuItemUI extends TesterHelper {	
	
	public static void click (TestObject testObject) throws IOException {
		new ToggleGUITestObject(testObject).click();
	}
	
}
