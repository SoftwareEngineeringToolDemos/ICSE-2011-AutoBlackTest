package autoblacktest.actions;

import java.util.HashMap;
import java.util.List;

import resources.TesterHelper;

import autoblacktest.TestState;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.rational.test.ft.object.interfaces.TestObject;

public class ActionFilter extends TesterHelper {
	
	private static ImmutableMap<String, Object> simpleActions;

	/**
	 * Returns an {@link ImmutableMap} containing all simple actions that can be
	 * executed by the agent
	 * 
	 * @return a map containing all executable simple actions
	 */
	public ImmutableMap<String, Object> getSimpleActionMap() {
		
		if (simpleActions == null) {
			HashMap<String, Object> tmpSimpleActions = new HashMap<String, Object>();
			tmpSimpleActions.put("ButtonUI", 1);
			tmpSimpleActions.put("CheckBoxUI", 1);
			tmpSimpleActions.put("ComboBoxUI", 1);
			tmpSimpleActions.put("FormattedTextFieldUI", 1);
			tmpSimpleActions.put("ListUI", 1);
			tmpSimpleActions.put("MenuItemUI", 1);
			tmpSimpleActions.put("MenuUI", 1);
			tmpSimpleActions.put("RadioButtonUI", 1);
			tmpSimpleActions.put("SliderUI", 1);
			tmpSimpleActions.put("TabbedPaneUI", 1);
			tmpSimpleActions.put("TableUI", 1);
			tmpSimpleActions.put("TextAreaUI", 1);
			tmpSimpleActions.put("TextFieldUI", 1);
			tmpSimpleActions.put("TextPaneUI", 1);
			tmpSimpleActions.put("ToggleButtonUI", 1);
			tmpSimpleActions.put("TreeUI", 1);
			tmpSimpleActions.put("PasswordFieldUI", 1);
			tmpSimpleActions.put("MonthViewUI", 1);
			tmpSimpleActions.put("EditorPaneUI", 1);

			// actually not enabled:
			// tmpSimpleActions.put("PopUpMenuUI", 1);
			// tmpSimpleActions.put("MenuBarUI", 1);
			// tmpSimpleActions.put("LabelUI", 1);
			//tmpSimpleActions.put("ScrollBarUI", 1);

			simpleActions = ImmutableMap.copyOf(tmpSimpleActions);
		}
		return simpleActions;
	}

	public boolean filterByTo(TestObject to) {
		
		String uIclassID = to.getProperty("uIClassID").toString();
		
		
//////////////////////////////////////////////////TEXT WIDGETS BEGIN ////////////////////////////////////////	
		if((uIclassID.equals("TextFieldUI") ||
			uIclassID.equals("TextPaneUI") ||	
			uIclassID.equals("TextAreaUI") ||
			uIclassID.equals("PasswordFieldUI") ||
			uIclassID.equals("EditorPaneUI") ||
			uIclassID.equals("FormattedTextFieldUI")) && to.getProperty("editable").toString().equals("false")) {
			
			return true;
		}
//////////////////////////////////////////////////TEXT WIDGETS END ////////////////////////////////////////
		

//////////////////////////////////////////////////FILE CHOOSER BEGIN ////////////////////////////////////////
		TestObject mp = to.getMappableParent();
		TestObject tmp= to.getTopMappableParent();
		TestObject mpmp = null;
		if(mp!=null) mpmp = mp.getMappableParent();
		if (mp!=null && tmp !=null && mpmp!=null){
			//Filtra gli oggetti di FileChooser
			if (uIclassID.equals("ToolBarUI")) {
				if((!(mp.isSameObject(tmp)))&& mp.getProperty("uIClassID").toString().equals("FileChooserUI")){
					return true;
				}
			}
			if(mp != null && mp.getProperty("uIClassID").toString().equals("ToolBarUI")) {
				if ((!(mp.isSameObject(tmp)))){
					if((!(mp.getMappableParent().isSameObject(tmp)))&& mp.getMappableParent().getProperty("uIClassID").toString().equals("FileChooserUI")){
						return true;
					}
				}
			}
		}
//////////////////////////////////////////////////FILE CHOOSER END ////////////////////////////////////////
		
		
		return false;
	}
}