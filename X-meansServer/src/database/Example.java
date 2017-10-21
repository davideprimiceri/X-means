package database;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Example modella ciascuna tupla letta dalla base di dati
 * @author Davide Primiceri
 */
public class Example implements Comparable<Example> {
	
	/**
	 * Attributo che contiene i valori degli attributi della tupla. 
	 */
	private List<Object> example = new ArrayList<Object>();
	
	/**
	 * Popola il contenitore {@link #example} con ciascun valore (dell'attributo continuo
	 * o discreto della tupla) passato come argomento 
	 * @param o Oggetto da aggiungere alla transazione
	 */
	public void add (Object o) {
		example.add(o);
	}
	
	/**
	 * Restituisce il valore dell'attributo i-esimo della tupla
	 * @param i Indice che indica il valore dell'attributo da restituire
	 * @return Oggetto appartente alla tupla letta dal db
	 */
	public Object get (int i) {
		return example.get(i);
	}
	
	/**
	 * Metodo (dell'interfaccia Comparable) per il confronto di due tuple.
	 */	
	public int compareTo (Example ex) {
		return this.toString().compareTo(ex.toString());
	}
	
	/**
	 * Restituisce una stringa che concatena tutti i valori inseriti
	 */
	public String toString () {
		String s = new String();
		for (Object o: example) 
			s += o.toString();
		return s;
	}
}




