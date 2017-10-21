package data;
import java.io.Serializable;

/**
 * La classe OutOfRangeSampleSize estende Exception e definisce un'eccezione per gestire il
 * caso di immissione da tastiera di un numero di cluster maggiore del numero di centroidi 
 * generabili dall'insieme di transazioni.
 * 
 * @author Davide Primiceri
 *
 */
public class OutOfRangeSampleSize extends Exception implements Serializable {
	
	/**
	 * Il costruttore invoca il costruttore della classe madre per creare l'oggetto eccezione con il
	 * messaggio specificato.
	 * @param msg Messaggio che verrà visualizzato se l'eccezione è sollevata.
	 */
	public OutOfRangeSampleSize (String msg) {
		super (msg);
	}

}
