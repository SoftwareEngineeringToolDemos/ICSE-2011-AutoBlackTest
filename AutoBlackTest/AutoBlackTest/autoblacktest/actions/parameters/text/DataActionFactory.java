package autoblacktest.actions.parameters.text;


//import org.apache.log4j.Logger;


public class DataActionFactory {
	//private static final Logger logger = Logger.getLogger(DataActionFactory.class);
	
	/**
	 * Ritorna un'istanza di una classe usata per recuperare 
	 * gli indici dei dati "validi" nella mappa dei dati.
	 * 
	 * 
	 * @param uIClassID.
	 *          
	 * @return Ritorna un'istanza di una classe usata per recuperare 
	 * 			gli indici dei dati "validi" nella mappa dei dati.
	 * 
	 */
	public static DataAction getDataAction(String uIClassID){
		DataAction instance = null;
		
		if(uIClassID.equals("TextAreaUI")) {
			
			instance = new TextAreaUIData();
		} else if(uIClassID.equals("TextPaneUI")) {
			
			instance = new TextPaneUIData();
		} else if(uIClassID.equals("PasswordFieldUI")) {
	
			instance = new PasswordFieldUIData();
		} else if(uIClassID.equals("FormattedTextFieldUI")) {
				
			instance = new FormattedTextFieldUIData();
		}else if(uIClassID.equals("TextFieldUI")) {

			instance = new TextFieldUIData();
		}else if(uIClassID.equals("EditorPaneUI")) {

			instance = new EditorPaneUIData();
		}
		
		return instance;
	}
}
