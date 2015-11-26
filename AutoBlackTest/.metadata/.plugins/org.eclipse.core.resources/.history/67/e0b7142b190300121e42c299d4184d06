package autoblacktest.actions.parameters.text;

import java.util.List;
import java.util.Set;
import java.util.Vector;



import autoblacktest.TestState;
import autoblacktest.actions.parameters.ParameterNumberFormat;

import com.rational.test.ft.object.interfaces.TestObject;



public class TextFieldUIData implements DataAction{
	
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
		
		if(dataContext!=null){
			if(!(dataContext[0].equals(""))){
				System.out.println("CCCCCCCCCCCCCCCCCCCCCCiso " +dataContext[0]);
				List<String> listKeywords = ontology.findRelatedKeywords(dataContext[0]);
				for(String keyword : listKeywords){
					System.out.println("0: "+keyword);
					int key=DataManager.getInstance().getKey(keyword);
					if(key!=-1){
						Set<Integer> keySet=DataManager.getInstance().getKeysWithKey(key);
						for(Integer secondIndex : keySet){
							long parameter=ParameterNumberFormat.computeValueForDoubleParameter(key,secondIndex);
//30/07/2012 questo controllo è stato commentato per evitare il filtraggio dei dati errati se già "utilizzati" un volta durante l'episodio.
//con "utilizzati" intendiamo che il dato è stato caricato tra le potenziali azioni per lo stato ma non che l'azione sia stata effetivamente eseguita 
//							if(dataManager.isErroneous(parameter)){
//								if(!(dataManager.isErroneousDataUsed(parameter))){
//									validElements.add(parameter);
//									dataManager.setErroneousDataUsed(parameter);
//								}	
//							}else
								validElements.add(parameter);			
							
						}
					}
				}
			}
			
			if(!(dataContext[1].equals(""))){
				List<String> listKeywords = ontology.findRelatedKeywords(dataContext[1]);
				for(String keyword : listKeywords){
					System.out.println("1: "+keyword);
					int key=DataManager.getInstance().getKey(keyword);
					if(key!=-1){
						Set<Integer> keySet=DataManager.getInstance().getKeysWithKey(key);
						for(Integer secondIndex : keySet){
							long parameter=ParameterNumberFormat.computeValueForDoubleParameter(key,secondIndex);
//30/07/2012 questo controllo è stato commentato per evitare il filtraggio dei dati errati se già "utilizzati" un volta durante l'episodio.
//con "utilizzati" intendiamo che il dato è stato caricato tra le potenziali azioni per lo stato ma non che l'azione sia stata effetivamente eseguita 
//							if(dataManager.isErroneous(parameter)){
//								if(!(dataManager.isErroneousDataUsed(parameter))){
//									validElements.add(parameter);
//									dataManager.setErroneousDataUsed(parameter);
//								}	
//							}else
								validElements.add(parameter);	
						}
					}
				}
			}
			if(!(dataContext[2].equals(""))){
				List<String> listKeywords = ontology.findRelatedKeywords(dataContext[2]);
				for(String keyword : listKeywords){
					System.out.println("2: "+keyword);
					int key=DataManager.getInstance().getKey(keyword);
					if(key!=-1){
						Set<Integer> keySet=DataManager.getInstance().getKeysWithKey(key);
						for(Integer secondIndex : keySet){
							long parameter=ParameterNumberFormat.computeValueForDoubleParameter(key,secondIndex);
//30/07/2012 questo controllo è stato commentato per evitare il filtraggio dei dati errati se già "utilizzati" un volta durante l'episodio.
//con "utilizzati" intendiamo che il dato è stato caricato tra le potenziali azioni per lo stato ma non che l'azione sia stata effetivamente eseguita 
//							if(dataManager.isErroneous(parameter)){
//								if(!(dataManager.isErroneousDataUsed(parameter))){
//									validElements.add(parameter);
//									dataManager.setErroneousDataUsed(parameter);
//								}	
//							}else
								validElements.add(parameter);
						}
					}
				}
			}
			
		}
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
