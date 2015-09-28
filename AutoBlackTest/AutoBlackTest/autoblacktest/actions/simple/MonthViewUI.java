package autoblacktest.actions.simple;

import java.io.IOException;

import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.TestObject;

public class MonthViewUI {
	
	public static void click(TestObject testObject) throws IOException {

		try {
			new GuiTestObject(testObject).click();
		} catch (WindowActivateFailedException e) {
			throw new WindowActivateFailedException();
		}

	}

}
