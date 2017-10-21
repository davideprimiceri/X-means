package data;
import java.io.Serializable;

/**
 * La classe astratta Attribute modella un generico attributo discreto o continuo.
 * 
 * @author Davide Primiceri
 *
 */
public abstract class Attribute implements Serializable {
	
	
	/**
	 * Nome simbolico dell'attributo
	 */
	protected String name;
	
	/**
	 * Identificativo numerico dell'attributo
	 */
	protected int index;
	
	/**
	 * Inizializza i valori dei membri name e index
	 * @param name Nome dell'attributo
	 * @param index Identificativo numerico dell'attributo
	 */
	Attribute(String name, int index) { 
		this.name = name; 
		this.index = index; 
	}
	
	/**
	 * Restituisce il nome di un attributo
	 * @return Valore del membro {@link #name}
	 */
	String getName() { 
		return name; 
	}
	
	/**
	 * Restituisce l'indice di un attributo
	 * @return Valore del membro {@link #index}
	 */
	int getIndex() { 
		return index; 
	}
	
	/**
	 * Metodo toString che restituisce il valore il nome dell'attributo
	 */
	public String toString() { 
		return name;
	}
}
