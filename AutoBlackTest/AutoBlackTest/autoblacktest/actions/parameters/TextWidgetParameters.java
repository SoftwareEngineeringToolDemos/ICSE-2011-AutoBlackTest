package autoblacktest.actions.parameters;

import java.util.Vector;

import org.apache.log4j.Logger;


import autoblacktest.actions.parameters.text.DataAction;
import autoblacktest.actions.parameters.text.DataActionFactory;
import autoblacktest.actions.parameters.text.DataManager;
import autoblacktest.actions.parameters.text.TextFieldUIData;

import com.rational.test.ft.object.interfaces.TestObject;


public class TextWidgetParameters implements ParametrizedAction {
	
	private Logger logger = Logger.getLogger(TextWidgetParameters.class);
	
	@Override
	public String[] getValues(TestObject to){
		// recupero gli indici delle entry "valide" della mappa dei dati che voglio siano tra le possibili azioni
		DataAction dataAction = DataActionFactory.getDataAction(to.getProperty("uIClassID").toString());
		Vector<Long>  validElements = dataAction.getValidElements(to);
			
			
		String toReturn[] = new String[validElements.size()];
		for (int i = 0; i < toReturn.length; i++) {
			// il valore viene trasformato in modo che un 1 termini la sequenza di cifre (1234 -> 12341)
			
			//modifica di test
			//recupero gli indici della hashmap dei dati
			long[] parameters=ParameterNumberFormat.getDoubleParameter(validElements.get(i));
			//recupero il valore da insierire dalla hashmap dei dati 
			String text=DataManager.getInstance().getData((int)parameters[0],(int)parameters[1]);
			System.out.println("paramentro "+i+" : Numero :"+ validElements.get(i) + "Valore:" +text);
			//FINE MODIFICA
			
			toReturn[i] = ParameterNumberFormat.getFormattedParameter(validElements.get(i));
		}
			
		return toReturn;
		
	}


}
