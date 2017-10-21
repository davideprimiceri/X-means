package data;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Set;

/**
 * La classe DiscreteAttribute estende la classe astratta Attribute e rappresenta un attributo discreto.
 * 
 * @author Davide Primiceri
 *
 * @see Attribute
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {
	/**
	 * Contenitore TreeSet di oggetti String,uno per ciascun valore discreto. 
	 * I valori si intendono pre-ordinati lessicograficamente.
	 */
	private TreeSet<String> values;
	
	/**
	 * Il costruttore invoca il costruttore della classe madre e avvalore il TreeSet di oggetti String
	 * con i valori discreti in input.
	 * @param name Nome dell'attributo
	 * @param index Indice dell'attributo
	 * @param values TreeSet dei valori discreti che l'attributo può assumere
	 */
	public DiscreteAttribute (String name, int index, TreeSet<String> values) {
		super(name, index);
		this.values = values;
	}
	
	/**
	 * Restituisce il numero di elementi divesi che un attributo può assumere.
	 * @return Cardinalità TreeSet {@link #values}
	 */
	int getNumberOfDistinctValues() { 
		return values.size(); 
	}
	
	/**
	 * Restituisce l'iteratore del TreeSet {@link #values}
	 */
	public Iterator<String> iterator() {
		return values.iterator();
	}
	
	/**
	 * Determina il numero volte che compare il valore discreto v nelle tuple i cui indici 
	 * sono indicati da IdList.
	 * @param data Riferimento alla classe Data che rapprensenta la tabella contenente tutte le tuple
	 * @param idList Contenitore di alcuni indici di tuple per cui va determinata la frequenza 
	 * @param v Valore discreto per cui si vuole determinare la frequenza.
	 * @return Frequenza di v
	 */
	int frequency(Data data,  Set <Integer> idList, String v) {
		int cont = 0, attrInd = this.getIndex();
		for(Object o: idList) {
			String dataValue = (String)(data.getAttributeValue((Integer)o, attrInd));
			if(v.equals(dataValue)) {
					cont++;
			}
		}
		return cont;
	}
}
