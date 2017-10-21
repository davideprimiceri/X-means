package data;
import java.io.Serializable;
import java.util.*;

/**
 * La classe astratta Item modella un generico item (coppia 
 * attributo-valore, Outlook=”Sunny”) di una tupla
 * @author Davide Primiceri
 *
 */
public abstract class Item implements Serializable {
	
	/**
	 * Attributo coinvolto nell'item
	 */
	protected  Attribute attribute;
	
	/**
	 * Valore dell'attributo
	 */
	protected Object value;
	
	/**
	 * Inizializza i valori dei membri attributi
	 * @param attribute {@link #attribute}
	 * @param value {@link #value}
	 */
	public Item(Attribute attribute, Object value){
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * Restituisce il membro Attribute
	 * @return {@link #attribute}
	 */
	Attribute getAttribute() {
		return attribute;
	}
	
	/**
	 * Restituisce il membro value
	 * @return {@link #value}
	 */
	Object getValue() {
		return value;
	}
	
	/**
	 * Restituisce il valore dell'attributo come stringa
	 */
	public String toString(){
		return value.toString();
	}
	
	/**
	 * Metodo astratto quindi senza implementazione: è compito delle 
	 * sottoclassi implementarlo
	 * @param a Oggetto che indica un attributo discreto o continuo
	 * @return Distanza tra item corrente (sul quale il metodo è invocato) e
	 * quello passato come argomento
	 */
	abstract double distance(Object a);
	
	/**
	 * Aggiorna value con il valore restituito da computePrototype
	 * @param data Oggetto data
	 * @param clusteredData Insieme di indici delle tuple appartenenti ad un cluster
	 */
	public void update(Data data,  Set<Integer> clusteredData) {
		value = data.computePrototype(clusteredData, attribute);
	}
	
}