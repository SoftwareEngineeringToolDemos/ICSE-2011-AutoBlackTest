package autoblacktest.actions.parameters.text;

import java.util.List;
import java.util.Set;
import java.util.Vector;



import autoblacktest.TestState;
import autoblacktest.actions.parameters.ParameterNumberFormat;

import com.rational.test.ft.object.interfaces.TestObject;



public class FormattedTextFieldUIData implements DataAction{
	
	private DataManager dataManager;
	private LabelAnalysis ontology;
	@Override
	public Vector<Long> getValidElements(TestObject to) {

		dataManager=DataManager.getInstance();
		ontology = LabelAnalysis.getInstance();
		Vector<Long> validElements=new Vector<Long>();
		validElements.clear();
		System.out.println("TO:"+to.getProperty("uIClassID"));
		String[] dataContext=TestState.getInstance().getDataContextFromCurrentState(to);
		// se esistono delle informazioni associate al to
		if(dataContext!=null){
			if(!(dataContext[0].equals(""))){
				System.out.println("CCCCCCCCCCCCCCCCCCCCCCiso " +dataContext[0]);
				// recupero le keyword associate all'informazione contestuale
				List<String> listKeywords = ontology.findRelatedKeywords(dataContext[0]);
				for(String keyword : listKeywords){
					System.out.println("0: "+keyword);
					//recupero la key associata al metadato
					int key=DataManager.getInstance().getKey(keyword);
					// se esiste la key
					if(key!=-1){
						//recupero tutte le key dei valori associati al metadato
						Set<Integer> keySet=DataManager.getInstance().getKeysWithKey(key);
						for(Integer secondIndex : keySet){
							//rielaboro le key per trasformale in formato compatibile con TeachingBox
							long parameter=ParameterNumberFormat.computeValueForDoubleParameter(key,secondIndex);
							//controllo se � un dato segnalato come errato
//30/07/2012 questo controllo � stato commentato per evitare il filtraggio dei dati errati se gi� "utilizzati" un volta durante l'episodio.
//con "utilizzati" intendiamo che il dato � stato caricato tra le potenziali azioni per lo stato ma non che l'azione sia stata effetivamente eseguita 
//							if(dataManager.isErroneous(parameter)){
//								//controllo se � gi� stato utilizzato
//								if(!(dataManager.isErroneousDataUsed(parameter))){
//									//aggiungo alla lista dei possibili parametri
//									validElements.add(parameter);
//									// setto il dato errato come utilizzato
//									dataManager.setErroneousDataUsed(parameter);
//								}	
//							}else
								//aggiungo alla lista dei possibili parametri
								validElements.add(parameter);
						}
					}
				}
			}
			/*recupera i valori del testo inserito nei widget di testo
			 * esempio: textfield con label citt� contiene "milano"
			 * questa funzionalit� estrae il testo "milano"
			 * Questa opzione � stata commentata perch� se il testo non � semanticamente corretto 
			 * potrebbe portare ad errori (caso che si verificherebbe se la posto di 
			 * "milano" ci fosse scritto "pippo" 
			 */
			/*
			if(!(dataContext[1].equals(""))){
				List<String> listKeywords = ontology.findRelatedKeywords(dataContext[1]);
				for(String keyword : listKeywords){
					System.out.println("1: "+keyword);
					int key=DataManager.getInstance().getKey(keyword);
					if(key!=-1){
						Set<Integer> keySet=DataManager.getInstance().getKeysWithKey(key);
						for(Integer secondIndex : keySet){
							long parameter=ParameterNumberFormat.computeValueForDoubleParameter(key,secondIndex);
							if(dataManager.isErroneous(parameter)){
								if(!(dataManager.isErroneousDataUsed(parameter))){
									validElements.add(parameter);
									dataManager.setErroneousDataUsed(parameter);
								}	
							}else
								validElements.add(parameter);
						}
					}
				}
			}
			*/
			/*queta opzione estra le informazioni dalla propriet� tooltips, 
			 * che contiene l'eventuale help
			 */
			/*
			if(!(dataContext[2].equals(""))){
				List<String> listKeywords = ontology.findRelatedKeywords(dataContext[2]);
				for(String keyword : listKeywords){
					System.out.println("2: " +keyword);
					int key=DataManager.getInstance().getKey(keyword);
					if(key!=-1){
						Set<Integer> keySet=DataManager.getInstance().getKeysWithKey(key);
						for(Integer secondIndex : keySet){
							long parameter=ParameterNumberFormat.computeValueForDoubleParameter(key,secondIndex);
							if(dataManager.isErroneous(parameter)){
								if(!(dataManager.isErroneousDataUsed(parameter))){
									validElements.add(parameter);
									dataManager.setErroneousDataUsed(parameter);
								}	
							}else
								validElements.add(parameter);
						}
					}
				}
			}
			*/
		}
		//se non � stato possibile associare nessuna informazione
		//inserisco i valori generici come possibili parametri
		System.out.println("numero parametri: "+ validElements.size());
		if(validElements.size()<=0){
			int key=DataManager.getInstance().getKey("generic");
			if(key!=-1){
				Set<Integer> keySet=DataManager.getInstance().getKeysWithKey(key);
				for(Integer secondIndex : keySet){
					long parameter=ParameterNumberFormat.computeValueForDoubleParameter(key,secondIndex);
					validElements.add(parameter);
				}
			}
		}
		return validElements;	
	}
	

}
