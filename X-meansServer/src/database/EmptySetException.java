package database;

/**
 * La classe EmptySetException estende Exception e definisce un'eccezione
 * per gestire il caso di insieme vuoto di tuple distinte (in Table_Data).
 * @author Davide Primiceri
 */
public class EmptySetException extends Exception {
	
	/**
	 * Il costruttore invoca il costruttore della classe madre per creare l'oggetto eccezione con il
	 * messaggio specificato.
	 * @param msg Messaggio che verrà visualizzato se l'eccezione è sollevata
	 */
	EmptySetException (String msg) {
		super(msg);
	}

}