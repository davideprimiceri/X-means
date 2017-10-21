package data;

/**
 * La classe ContinuousAttribute estende la classe astratta Attribute e rappresenta un attributo
 * continuo (numerico). Inoltre la classe si occupa di normalizzare nell'intervallo [0,1] i valori 
 * degli attributi continui per evitare che la differenza di valori assumibili dagli attributi 
 * influisca sul calcolo della distanza.
 * 
 * @author Davide Primiceri
 * 
 * @see Attribute
 */
class ContinuousAttribute extends Attribute {
	
	/**
	 * Estremo massimo dell'intervallo di valori che l'attributo può assumere
	 */
	private double max;
	
	/**
	 * Estremo minimo dell'intervallo di valori che l'attributo può assumere
	 */
	private double min;
	
	/**
	 * Il costruttore invoca il costruttore della classe madre e avvalora i membri 
	 * @param name Nome dell'attributo
	 * @param index Indice dell'attributo
	 * @param min Estremo minimo dell'intervallo
	 * @param max Estremo massimo dell'intervallo
	 */
	public ContinuousAttribute(String name, int index, double min, double max){
		super(name, index);
		this.min = min;
		this.max = max; 
	}
	
	/**
	 * Esegue la normalizzazione usando il valore di input e quello dei membri min,max
	 * @param v Valore dell'attributo da normalizzare
	 * @return Valore normalizzato
	 */
	double getScaledValue (double v) {
		Double new_minA = 0.0;
		Double new_maxA = 1.0;
		return (((v-min)/(max-min))*(new_maxA-new_minA)+new_minA);
	}
	
}