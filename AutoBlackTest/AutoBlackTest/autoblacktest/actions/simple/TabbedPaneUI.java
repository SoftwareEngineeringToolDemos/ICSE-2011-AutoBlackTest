package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.GuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.TestObject;

public class TabbedPaneUI extends TesterHelper {

	public static void click(TestObject testObject, long tabCount) throws IOException {
		new GuiSubitemTestObject(testObject).click(atIndex((int)tabCount));
	}

}
