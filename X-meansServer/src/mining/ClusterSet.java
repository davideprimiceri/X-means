package mining;

import java.io.Serializable;
import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

/**
 * La classe ClusterSet rappresenta un insieme di cluster.
 * 
 * @author Davide Primiceri
 * 
 * @see Cluster
 * @see data.Data
 */
public class ClusterSet implements Serializable {
	/**
	 * Rappresenta un array di oggetti Cluster
	 */
	private Cluster C[];
	/**
	 * Rappresenta l'indice dell'ultimo Cluster aggiunto all'array C
	 */
	private int i = 0;
	
	/**
	 * Definisce la dimensione dell'array C
	 * @param k Cardinalità dei cluster da generare
	 */
	ClusterSet(int k) {
		C = new Cluster[k];
	}
	
	/**
	 * Aggiunge il cluster passato come parametro all'array C e incrementa l'indice
	 * @param c Oggetto Cluster da aggiungere all'array {@link #C}
	 */
	void add(Cluster c) {
		C[i] = c;
		i++;
	}
	
	/**
	 * Restituisce un Cluster all'interno dell'array {@link #C}
	 * @param i Posizione del Cluster da restituire
	 * @return Oggetto Cluster in posizione i nell'array {@link #C}
	 */
	public Cluster get(int i) {
		return C[i];
	}
	
	/**
	 * Esegue l’inizializzazione dei cluster individuando i centroidi dall’insieme di tuple da Data
	 * @param data Oggetto Data contenente le tuple
	 * @throws OutOfRangeSampleSize
	 */
	void initializeCentroids(Data data) throws OutOfRangeSampleSize {
		int indCentroid [] = data.sampling(C.length);
		for(int i=0; i<indCentroid.length; i++) {
			Tuple centroid = data.getItemSet(indCentroid[i]);
			add(new Cluster(centroid));
			}
	}
	
	/**
	 * Calcola la distanza tra la tupla ed il centroide di ciascun cluster determinando quello a distanza minore
	 * @param tuple Tupla da considerare per il calcolo
	 * @return Cluster con minor distanza dalla tupla passata come argomento
	 */
	Cluster nearestCluster(Tuple tuple) {
		double distMin = tuple.getDistance(C[0].getCentroid());
		int j = 0;
		for (int i=1; i<C.length; i++){
			double distCurrent = tuple.getDistance(C[i].getCentroid());
			if (distMin > distCurrent) {
				distMin = distCurrent;
				j = i;
			}
		}
		return get(j);
	}
	
	/**
	 *Identifica e restituisce il cluster cui la tupla appartiene se la tupla appartiene ad un cluster,
	 *altrimenti restituisce null
	 * @param id Indice di una tupla in Data
	 * @return Cluster che contiene la tupla il cui indice è passato come argomento
	 */
	Cluster currentCluster(int id) {
		Cluster c = null;
		for (int i=0; i<C.length; i++){
			if (get(i).contain(id) == true) 
				c = get(i);
		}
		return c;
	}
	
	/**
	 * Ri-determina il centroide per ciascun cluster nell'array C
	 * @param data Insieme di tuple
	 */
	void updateCentroids(Data data) {
		for (int i=0; i<C.length; i++)
			C[i].computeCentroid(data);
	}
	
	/**
	 * Restituisce una stringa fatta da ciascun centroide dell’insieme dei cluster.
	 */
	public String toString() {
		String s = new String();
		for (int i=0; i<C.length; i++) 
			s += C[i].toString() + "\n";
		return s;
	}
	
	/**
	 * Restituisce una stringa fatta da ciascun centroide dell’insieme dei cluster e 
	 * dalle tuple appartenenti a ciascun cluster.
	 * @param data Insieme di tuple {@link data.Data}
	 * @return
	 */
	public String toString(Data data) {
		String s = new String();
		for (int i=0; i<C.length; i++) {
			if (C[i] != null)
				s += "\n"+i+":"+ C[i].toString(data);
		}
		return s;
	}

	/**
	 * Restituisce la lunghezza del membro {@link C}
	 * @return Cardinalità C
	 */
	public int size() {
		return C.length;
	}
}
