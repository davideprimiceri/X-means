package data;

/**
 * La classe ContinuousItem estende la classe Item e rappresenta una coppia 
 * <Attributo continuo - valore numerico>
 * 
 * @author Davide Primiceri
 *
 * @see Item
 */
public class ContinuousItem extends Item {
	
	/**
	 * Invoca il costruttore della classe madre e avvalora i membri
	 * @param attribute Attributo continuo
	 * @param value Valore dell'attributo continuo
	 */
	ContinuousItem (Attribute attribute, Double value) {
		super (attribute, value);
	}
	
	/**
	 * Determina la distanza (in valore assoluto) tra il valore normalizzato dall'item corrente
	 * {@link Item#getValue()} e quello normalizzato dell'item passato per argomento.
	 * @param a Item per il calcolo della distanza 
	 */
	double distance (Object a) {
		Attribute attribute = ((ContinuousItem)a).getAttribute();
		double currentVal = ((ContinuousAttribute)this.getAttribute()).getScaledValue((Double)this.getValue());
		double tupVal = ((ContinuousAttribute)attribute).getScaledValue((Double)((ContinuousItem)a).getValue());
		return Math.abs(currentVal - tupVal);
	}
	
}
