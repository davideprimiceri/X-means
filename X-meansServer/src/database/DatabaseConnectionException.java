package database;

/**
 * La classe DatabaseConnectionException estende Exception e definisce un'eccezione
 * per gestire il caso di fallito caricamento del driver del DBMS.
 * @author Davide Primiceri
 */
public class DatabaseConnectionException extends Exception {
	
	/**
	 * Il costruttore invoca il costruttore della classe madre per creare l'oggetto eccezione con il
	 * messaggio specificato.
	 * @param msg Messaggio che verrà visualizzato se l'eccezione è sollevata
	 */
	public DatabaseConnectionException(String msg) {
		super(msg);
	}

}