package data;

import java.io.Serializable;
import java.util.*;

/**
 * La classe Tuple rappresenta una tupla dell’insieme di transazioni
 * @author Davide Primiceri
 *
 */
public class Tuple implements Serializable {
	
	/**
	 * Array di item che rappresenta la tupla
	 */
	Item [] tuple;
	
	/**
	 * Inizializza l'array tuple con l'argomento size
	 * @param size Dimensione da assegnare all'array
	 */
	public Tuple (int size) {
		tuple = new Item[size];
	}
	
	/**
	 * Restituisce la dimensione dell'array {@link #tuple}
	 * @return Dimensione dell'array
	 */
	public int getLength() {
		return tuple.length;
	}
	
	/**
	 * Restituisce l'item in posizione i
	 * @param i Posizione dell'item
	 * @return  Item di posizione i
	 */
	public Item get(int i) {
		return tuple[i];
	}
	
	/**
	 * Aggiorna il membro tuple col nuovo Item in posizione i
	 * @param c Item da aggiungere all'array {@link #tuple}
	 * @param i Indice in cui aggiungere l'item
	 */
	void add(Item c,int i) {
		tuple[i] = c;
	}
	
	/*Determina la distanza tra la Tupla passata per argomento e la Tupla corrente.
	 * La distanza è la somma delle distanze tra gli item con stessa posizione nelle due tuple.
	public double getDistance(Tuple obj) {
		double dist = 0;
		for (int i=0; i<obj.getLength(); i++)
			dist += tuple[i].distance(obj.get(i));
		return dist;
	}*/
	
	/**
	 * Determina la distanza tra la Tupla passata per argomento e la Tupla corrente.
	 * Implementa la distanza euclidea impiegando le distanze di ogni singolo item.
	 * @param obj Tupla per cui si calcola la distanza rispetto alla tupla corrente
	 * @return Distanza tra le due tuple
	 */
	public double getDistance(Tuple obj) {
		double dist = 0;
		for (int i=0; i<obj.getLength(); i++)
			dist += Math.pow(tuple[i].distance(obj.get(i)), 2);
		return Math.sqrt(dist);
	}
	
	/**
	 * Determina la distanza media tra le tuple clusteredData e la Tupla corrente
	 * @param data Oggetto data
	 * @param clusteredData Indici di alcune tuple appartenenti ad un cluster
	 * @return Distanza media
	 */
	public double avgDistance(Data data, Set<Integer> clusteredData) {
		double sum = 0;
		for (Integer i: clusteredData) 
			sum += getDistance(data.getItemSet(i));
		return sum/clusteredData.size();
	}
}

