package database;

/**
 * La classe NoValueException estende Exception e definisce un'eccezione
 * per gestire il caso di valore nullo riscontrato (in Table_Data).
 * @author Davide Primiceri
 */
public class NoValueException extends Exception {
	
	/**
	 * Il costruttore invoca il costruttore della classe madre per creare l'oggetto eccezione con il
	 * messaggio specificato.
	 * @param msg Messaggio che verr� visualizzato se l'eccezione � sollevata
	 */
	NoValueException (String msg) {
		super(msg);
	}

}