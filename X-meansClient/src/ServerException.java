
/**
 * La classe ServerException estende Exception e definisce un'eccezione che verrà sollevata 
 * se una delle operazioni richieste al server non è andata a completamento (sul server)
 * @author Davide Primiceri
 */
public class ServerException extends Exception {
	
	/**
	 * Il costruttore invoca il costruttore della classe madre per creare l'oggetto eccezione con il
	 * messaggio specificato.
	 * @param msg Messaggio che verrà visualizzato se l'eccezione è sollevata
	 */
	public ServerException (String msg) {
			super(msg);
	}

}
