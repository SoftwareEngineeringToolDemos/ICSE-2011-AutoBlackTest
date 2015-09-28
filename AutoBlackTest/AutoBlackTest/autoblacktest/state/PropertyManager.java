package autoblacktest.state;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.rational.test.ft.object.interfaces.TestObject;

public class PropertyManager {
	
	private static Logger logger = Logger.getLogger(PropertyManager.class);

	public static HashMap<String, List<String>> properties = new HashMap<String, List<String>>();
	

	public static void addStandardProperties() {
		List<String> generalProp = Lists.newArrayList("uIClassID"/*, "class",*/
				/*"opaque",*/ /*"x", "y", "height", "width",*/ /*"font",*/ /*"background",*/
				/*"enabled",*/ /*"focusable"*/ /*"visible",*/ /*"foreground"*/);
		List<String> generalNameProp = Lists.newArrayList("uIClassID", /*"class",*/
				/*"opaque",*/ "name"/*,*/ /*"x", "y", "height", "width",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable"*/ /*"visible",*/ /*"foreground"*/);
		List<String> popProp = Lists.newArrayList("uIClassID", /*"class",*/
				/*"opaque",*/ "label"/*,*/ /*"x", "y", "height", "width",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable"*/ /*"visible",*/ /*"foreground"*/);
		List<String> buttonsProp = Lists.newArrayList("uIClassID", "label",
				"text"/*,*/ /*"height", "width",*/ /*"class",*/ /*"opaque",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable"*/ /*"visible",*/ /*"foreground"*/);
		List<String> checkboxProp = Lists.newArrayList("uIClassID", /*"class",*/
				"label", "text", "name", "selected"/*,*/ /*"x", "y", "height",
				"width",*/ /*"opaque",*/ /*"font",*/ /*"background",*/ /*"enabled",*/
				/*"focusable"*/ /*"visible",*/ /*"foreground"*/);
		List<String> radioProp = Lists.newArrayList("uIClassID", /*"class",*/
				"label", "text", "selected"/*,*/ /*"x", "y", "height", "width",*/
				/*"opaque",*/ /*"font",*/ /*"background",*/ /*"enabled",*/ /*"focusable"*/
				/*"visible",*/ /*"foreground"*/);
		List<String> comboProp = Lists.newArrayList("uIClassID", /*"class",*/
				"name", "selectedIndex", /*"x", "y", "height", "width",*/ /*"opaque",*/
				/*"font",*/ /*"background",*/ /*"enabled",*/ /*"focusable",*/ /*"visible",*/
				/*"foreground",*/ "itemCount");
		List<String> tabbProp = Lists.newArrayList("uIClassID", /*"class",*/
				"selectedIndex", /*"x", "y", "height", "width",*/ /*"opaque",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable",*/ /*"visible",*/ /*"foreground",*/
				"tabCount");
		List<String> textProp = Lists.newArrayList("uIClassID", /*"class",*/
				"text", /*"x", "y", "height", "width",*/ /*"opaque",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable",*/ /*"visible",*/ /*"foreground",*/ "editable");
		List<String> linkProp = Lists.newArrayList("uIClassID", /*"class",*/
				"text", "label"/*,*/ /*"x", "y", "height", "width",*/ /*"opaque",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable"*/ /*"visible",*/ /*"foreground"*/);
		List<String> listProp = Lists.newArrayList("uIClassID", /*"class",*/
				/*"opaque",*/ /*"x", "y", "height", "width",*/ /*"font",*/ /*"background",*/
				/*"enabled",*/ /*"focusable",*/ /*"visible",*/ /*"foreground",*/
				"minSelectionIndex", "maxSelectionIndex", ".itemCount");
		List<String> sliderProp = Lists.newArrayList("uIClassID", /*"class",*/
				/*"opaque",*/ /*"x", "y", "height", "width",*/ /*"font",*/ /*"background",*/
				/*"enabled",*/ /*"focusable",*/ /*"visible",*/ /*"foreground",*/ "minimum",
				"maximum", "value");
		List<String> tableProp = Lists.newArrayList("uIClassID", /*"class",*/
				"rowCount", "columnCount", /*"opaque",*/ /*"x", "y", "height", "width",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable",*/ /*"visible",*/ /*"foreground",*/
				"selectedRow", "selectedColumn", "selectedColumnCount", "selectedRowCount", "editable");
		List<String> passwordProp = Lists.newArrayList("uIClassID", /*"class",*/
				"text", /*"x", "y", "height", "width",*/ /*"opaque",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable",*/ "editable" /*"visible",*/ /*"foreground"*/);
		List<String> treeProp = Lists.newArrayList("uIClassID", /*"class",*/
				/*"opaque",*/ "name", /*"x", "y", "height", "width",*/ /*"font",*/
				/*"background",*/ /*"enabled",*/ /*"focusable",*/ /*"visible",*/ /*"foreground",*/
				"rowCount", "maxSelectionRow", "minSelectionRow", "editable");
		List<String> dateProp = Lists.newArrayList("uIClassID", /*"opaque",*/ "name"/*, "focusable"*/);
		
		properties.put("BusyLabelUI", generalProp);
		properties.put("ButtonUI", buttonsProp);
		properties.put("CheckBoxMenuItemUI", checkboxProp);
		properties.put("CheckBoxUI", checkboxProp);
		properties.put("ColorChooserUI", generalProp);
		properties.put("ComboBoxUI", comboProp);
		properties.put("ComponentUI", generalProp);
		properties.put("EditorPaneUI", textProp);
		properties.put("FileChooserUI", generalProp);
		properties.put("FormattedTextFieldUI", textProp);
		properties.put("HyperlinkUI", linkProp);
		properties.put("LabelUI", generalProp);
		properties.put("ListUI", listProp);
		properties.put("MenuBarUI", generalProp);
		properties.put("MenuItemUI", linkProp);
		properties.put("MenuUI", linkProp);
		properties.put("OptionPaneUI", generalProp);
		properties.put("PanelUI", generalNameProp);
		properties.put("PasswordFieldUI", passwordProp);
		properties.put("PopupMenuSeparatorUI", generalProp);
		properties.put("PopupMenuUI", popProp);
		properties.put("ProgressBarUI", generalProp);
		properties.put("RadioButtonUI", radioProp);
		properties.put("ScrollBarUI", generalProp);
		properties.put("ScrollPaneUI", generalProp);
		properties.put("SeparatorUI", generalProp);
		properties.put("SeparatorUI", generalProp);
		properties.put("SliderUI", sliderProp);
		properties.put("SpinnerUI", generalProp);
		properties.put("SplitPaneUI", generalProp);
		properties.put("TabbedPaneUI", tabbProp);
		properties.put("TableHeaderUI", generalProp);
		properties.put("TableUI", tableProp);
		properties.put("TextAreaUI", textProp);
		properties.put("TextFieldUI", textProp);
		properties.put("TextPaneUI", textProp);
		properties.put("ToggleButtonUI", buttonsProp);
		properties.put("ToolBarUI", generalNameProp);
		properties.put("TreeUI", treeProp);
		properties.put("MonthViewUI", dateProp);

	}
	
	public static String manageProperties(TestObject to) {
		if (properties.isEmpty()) {
			addStandardProperties();
		}

		String str = "{";
		try {
			List<String> wantedProp = properties.get(to.getProperty("uIClassID").toString());
			if (wantedProp.isEmpty()) {
				wantedProp.add("uIClassID");
				logger.warn("The uIClassID " + to.getProperty("uIClassID").toString()+ " is not supported");
			}

			for (String s : wantedProp) {
				String prope = "";
				if (to.getProperty(s) != null) {
					prope = to.getProperty(s).toString();
				}
				str = str + s + "=" + prope + "#AbT#";
			}
		} catch (Exception e) {
			str = to.getProperty("class").toString();
			logger.error("The current Test Object " + to.getProperty("class").toString()+ " has to be verified");
		}

		str = str + "}";

		return str;
	}
}