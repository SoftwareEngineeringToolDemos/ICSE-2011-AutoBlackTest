package autoblacktest.actions.parameters.text;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


import com.google.common.collect.Lists;


/**
 * Classe utilizzata per l'analisi delle informazioni
 * Nello stato attuale viene utilizzato un "dizionario" per il riconoscimento
 * delle parole chiave associabili a metadati.
 * @author Giovanni Becce
 *
 */
public class LabelAnalysis {
	
	private String fileXMLPath="data\\dataset.xml";
	
	private static LabelAnalysis instance = null;

	private List<String> discardedWords=Lists.newArrayList(
							"the","in","these","this","that","of","an");
	List<String> specialCharacters= Lists.newArrayList(":",";",".");

	/**
	 * Mappa tra il generics del metadato e il metadato stesso
	 */
	private HashMap<String,String> genericsMetadata=new HashMap<String,String>();
	
	//tag file xml
	private String tagNode="DATA";
	private String tagMetadata="metadata";
	private String tagGenerics="generics";
	private String tagGeneric ="generic";
	
	public static synchronized LabelAnalysis getInstance() {
		if (instance == null)
			instance = new LabelAnalysis();
		return instance;
	}
	
	
	public LabelAnalysis(){
		loadKeywordsFromXMLFile();	
	}
	
	/**
	 * Metodo per la ricerca di parole chiave significative (di cui si ha conoscenza) 
	 * all'interno di una frase in linguaggio naturale
	 * @param phrase
	 * @return una lista di parole chiave selezionate
	 */
	public List<String> findRelatedKeywords(String phrase){
		
		List<String> relatedKeywords=Lists.newArrayList();
		relatedKeywords.clear();
		//filtro le parole da non considerare
		List<String> words= wordsFilter(phrase);
		for(String s : words){
			if(genericsMetadata.containsKey(s)){
				if(!(isKeywordAlreadyAdded(relatedKeywords,s)))
					relatedKeywords.add(genericsMetadata.get(s));
			}
		}			
		return relatedKeywords;
	}
	
	private boolean isKeywordAlreadyAdded(List<String> relatedKeywords,String key){
		boolean added=false;
		for(String s : relatedKeywords){
			if(s.equals(key))
				added=true;
		}	
		return added;
	}
	/**
	 * Metodo utilizzato per filtrare le parole chiave
	 * all'interno di una frase in linguaggio naturale 
	 * eliminando tutte le parole da non considerare
	 * @param phrase
	 * @return
	 */
	private List<String> wordsFilter(String phrase){
		String[] words=phrase.split(" ");
		List<String> rightWords=Lists.newArrayList();
		for(int i=0;i<words.length;i++){
			String word=deleteSpecialCharacter(words[i].trim().toLowerCase());
			if(!(discardedWords.contains(word)) ){
				rightWords.add(word);
			}
		}		
		return rightWords;
	}
	/**
	 * Metodo utilizzato per eliminare caratteri speciali
	 * @param s
	 * @return
	 */
	private String deleteSpecialCharacter(String s){
		String cleaned="";
		for(int i=0;i<s.length();i++){
			String temp=""+s.charAt(i);
			if(!(specialCharacters.contains(temp))){
				cleaned=cleaned+s.charAt(i);
			}
		}
		return cleaned;
	}
	
	/**
	 * Metodo per il caricamento delle keyword conosciute (e relative generalizzazioni)
	 * dal file xml dataSetXMLPath
	 */
	private void loadKeywordsFromXMLFile(){
		
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new File(fileXMLPath));
			
			Element rootElement = document.getRootElement();
			
			List children = rootElement.getChildren(); 
			Iterator iterator = children.iterator(); 
			while (iterator.hasNext()){ 
				 String metadata="";
				 String generic="";
			
				 Element nodo = (Element)iterator.next(); 
			   
				 if(nodo.getName().equals(tagNode)){
					 //recupero il metadato
					 metadata=nodo.getChildText(tagMetadata);
					 genericsMetadata.put(metadata,metadata);
					 
					 //System.out.println("");
					// System.out.println("metadata: "+metadata);
					 //recupero i generici
					if((nodo.getChild(tagGenerics)!=null)&&(nodo.getChild(tagGenerics).getChildren()!=null)){
						List generics = nodo.getChild(tagGenerics).getChildren();
						Iterator iterator2 = generics.iterator(); 
						while (iterator2.hasNext()){ 
							Element genericNode = (Element)iterator2.next(); 
							if(genericNode.getName().equals(tagGeneric)){
								generic=genericNode.getText();
								genericsMetadata.put(generic,metadata);
								//System.out.println("generics: "+generic);

							}
						}
					}
				 }
			}
					
		} catch (JDOMException e) {
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}

	}

}
