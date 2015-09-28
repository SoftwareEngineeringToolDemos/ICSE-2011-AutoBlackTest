package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.ToggleGUITestObject;

public class CheckBoxUI extends TesterHelper {

	public static void clickSelect(TestObject testObject) throws IOException {
		
		ToggleGUITestObject toggle = new ToggleGUITestObject(testObject);

		Object ob;
		try {
			ob = testObject.getParent().getParent().getProperty("uIClassID");

			if (ob != null) {
				String proper = ob.toString();
				if (proper.equals("ViewportUI")) {
					if (testObject.getProperty("selected").toString().equals(
							"true")) {
						// toggle.setState(NOT_SELECTED);

					} else {
						toggle.setState(SELECTED);
					}
				} else {
					if (testObject.getProperty("selected").toString().equals(
							"true")) {
						// toggle.clickToState(NOT_SELECTED);

					} else {
						toggle.clickToState(SELECTED);
					}
				}
			}
		} catch (Exception e) {
			if (testObject.getProperty("selected").toString().equals("true")) {
				// toggle.clickToState(NOT_SELECTED);

			} else {
				toggle.clickToState(SELECTED);
			}
		}
	}

	public static void clickNotSelect(TestObject testObject) throws IOException {

		ToggleGUITestObject toggle = new ToggleGUITestObject(testObject);

		Object ob;
		try {
			ob = testObject.getParent().getParent().getProperty("uIClassID");

			if (ob != null) {
				String proper = ob.toString();
				if (proper.equals("ViewportUI")) {
					if (testObject.getProperty("selected").toString().equals(
							"true")) {
						toggle.setState(NOT_SELECTED);

					} else {
						// toggle.setState(SELECTED);
					}
				} else {
					if (testObject.getProperty("selected").toString().equals(
							"true")) {
						toggle.clickToState(NOT_SELECTED);

					} else {
						// toggle.clickToState(SELECTED);
					}
				}
			}
		} catch (Exception e) {
			if (testObject.getProperty("selected").toString().equals("true")) {
				toggle.clickToState(NOT_SELECTED);

			} else {
				// toggle.clickToState(SELECTED);
			}
		}

	}
}
