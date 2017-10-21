
/**
 * La classe ServerException estende Exception e definisce un'eccezione che verr� sollevata 
 * se una delle operazioni richieste al server non � andata a completamento (sul server)
 * @author Davide Primiceri
 */
public class ServerException extends Exception {
	
	/**
	 * Il costruttore invoca il costruttore della classe madre per creare l'oggetto eccezione con il
	 * messaggio specificato.
	 * @param msg Messaggio che verr� visualizzato se l'eccezione � sollevata
	 */
	public ServerException (String msg) {
			super(msg);
	}

}
