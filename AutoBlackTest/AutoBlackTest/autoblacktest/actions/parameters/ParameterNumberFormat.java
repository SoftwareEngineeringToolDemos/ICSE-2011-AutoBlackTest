package autoblacktest.actions.parameters;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;

/**
 * Class used to format action parameters into a teaching-box compatible format.<br/>
 * A teaching-box {@link Action} is a pair of <tt>double</tt> values. The first
 * one is use as index of test object into the actual global test object vector,
 * whereas the second is used both as index of the method into the action helper
 * class and as parameter value.<br/>
 * This last purpose is achieved using the integer portion of the double number
 * as index of the method and the fractional portion as parameter.<br/>
 * The parameter value is formatted adding '1' to the number. This process is
 * done due to the identical representation of numbers 0.10 and 0.100 that would
 * lead into an identification failure.<br/>
 * <br/>
 * For example, if the method index is 3 and the parameter value is 5 the
 * formatted number will be 3.51. Instead, if the parameter value is 100 the
 * formatted number will be 3.1001.
 * 
 * @author Andrea Mattavelli
 */
public class ParameterNumberFormat {

	private static final Logger logger = Logger.getLogger(ParameterNumberFormat.class);
	
	// valore utilizzato per la conversione di un doppio parametro in un'unico
	// numero attraverso la divisione, secondo lo schema dividendo=quoziente*divisore+resto.
	private static long divisor=10000;
	
	/**
	 * Returns an integer value into a formatted number that can be used as
	 * fractional portion of a double value.
	 * 
	 * @param parameter
	 *            value to be formatted.
	 * @return the parameter into a formatted number that can be used as
	 *         fractional portion of a double value.
	 */
	public static String getFormattedParameter(long parameter) {
//		logger.debug("Formatting number " + parameter + " into " + Integer.toString(parameter) + "1");
		
		// Old version
		//return Integer.toString(parameter) + "1";
		
		//Giovanni - modifica per la gestione del parametro come long
		return parameter + "1";
	}
	
	/**
	 * Returns the method index stored into the double formatted number.
	 * 
	 * @param number
	 *            the formatted number to parse.
	 * @return the method index.
	 */
	public static int getMethodIndex(double number) {
		String numberString = Double.toString(number);
		String methodIndex = numberString.split("\\.")[0];
		
//		logger.debug("Extracting method index from number: " + number + ". Result is: " + methodIndex);
		
		return Integer.parseInt(methodIndex);
	}
	
	/**
	 * Returns the parameter value stored into the double formatted number.
	 * 
	 * @param number
	 *            the formatted number to parse.
	 * @return the parameter value, if any, or -1.
	 */
	public static long getParameter(double number) {
//		logger.debug("Getting parameter from " + number);
		
		String parameter = Double.toString(number).split("\\.")[1];
		if(parameter.equals("0")) {
//			logger.debug("No parameter");
			
			return -1;
		}
		else {
			return getNumber(parameter);
		}
	}

	/**
	 * Returns the parameter value from a string representation of the
	 * fractional portion a formatted number.
	 * 
	 * @param formattedNumber
	 *            the string representation of the fractional portion of a
	 *            formatted number.
	 * @return the parameter value from a string representation of the
	 *         fractional portion a formatted number.
	 */
	private static long getNumber(String formattedNumber) {
		String numberString = formattedNumber.substring(0, formattedNumber.length() - 1);
		
//		logger.debug("Extracting parameter from formatted number: " + formattedNumber + " to: " + numberString);
		
		//old version
		//return Integer.parseInt(numberString);
		
		//Giovanni - modifica per la gestione del parametro come long
		return Long.parseLong(numberString);
	}
	
	/**
	 * Metodo utilizzato per poter usufruire di un doppio parametro per le azioni,
	 * i due parametri vengono trasformati in un'unico numero di tipo long sfruttando
	 * lo schema della divisione (dividendo=quoziente*divisore+resto) in modo che sia
	 * possibile recuperare i due parametri con un'operazione inversa.
	 * @param parameter1
	 * @param parameter2
	 * @return
	 */
	public static long computeValueForDoubleParameter(long parameter1,long parameter2){
		long computedParameter;
		return computedParameter=parameter1*divisor+parameter2;
		
	}
	
	/**
	 * Metodo utilizzato per il recupero del doppio parametro precendentemente
	 * unificato in {@link computeValueForDoubleParameter}
	 * @param parameter
	 * @return il valore dei due parametri
	 */
	public static long[] getDoubleParameter(long parameter){
		long[] parameters=new long[2];
		//calcolo il primo parametro corrispondente al quoziente della divisione
		parameters[0]=parameter/divisor;
		//calcolo il secondo parametro corrispondente al resto della divisione
		parameters[1]=parameter%divisor;
		
		return parameters;		
	}
}
