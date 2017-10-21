package data;

/**
 * La classe DiscreteItem estende la classe astratta Item e rappresenta una coppia 
 * Attributo discreto - valore discreto
 * 
 * @author Davide Primiceri
 *
 * @see Item
 */
class DiscreteItem extends Item {
	
	/**
	 * Il costruttore invoca il costruttore della classe madre e avvalora i membri
	 * @param attribute Attributo discreto
	 * @param value Valore dell'attributo discreto
	 */
	DiscreteItem(Attribute attribute, String value) {
		super (attribute, value);
	}
	
	/**
	 * Implementa il metodo astratto {@link Item#distance(Object)} definito nella classe madre.
	 * Restituisce 0 se il valore dell'attributo corrente è uguale al valore dato come parametro,
	 * altrimenti restituisce 1.
	 */
	double distance(Object a) {
		if(getValue().equals(a.toString()))
			return 0;
		else
			return 1;
		}

}
