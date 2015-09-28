package autoblacktest.actions.parameters.text;

import java.util.Vector;

import com.rational.test.ft.object.interfaces.TestObject;

/**
 * Interfaccia dotata di un metodo per il recupero dei dati validi
 * durante la fase di test dell'applicazione.
 * 
 * @author Giovanni Becce
 *
 */
public interface DataAction {
/**
 * Ritorna un Vector di indici selezionati corrispondenti alle entry mappa dei dati
 * @param to
 * @return un Vector di indici corrispondenti ai dati validi per il test
 */
	public Vector<Long> getValidElements(TestObject to);
}
