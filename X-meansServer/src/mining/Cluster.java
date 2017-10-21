package mining;

import java.io.Serializable;
import java.util.*;
import data.Data;
import data.Tuple;

/**
 * La classe Cluster rappresenta un cluster, cioè collezione di tuple dall’insieme in Data.
 * @author Davide Primiceri
 */
public class Cluster implements Serializable {
	
	/**
	 * Tupla rappresentante il centroide del cluster
	 */
	private Tuple centroid;
	/**
	 * Insieme di righe della tabella Data che appartengono al Cluster
	 */
	private Set<Integer> clusteredData;
	/**
	 * Distanza media interna ad un cluster
	 */
	private Double intraDistance;
	
	/**
	 * Inizializza il cluster avvalorando il membro centroid con l’argomento e 
	 * istanzia clusteredData
	 * @param centroid Oggetto tuple che definisce il primo centroide
	 */
	Cluster(Tuple centroid){
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();
	}
	
	/**
	 * Restituisce il membro {@link centroid}
	 * @return Valore centroide
	 */
	public Tuple getCentroid(){
		return centroid;
	}
	
	/**
	 * Restituisce il valore di {@link intraDistance}
	 * @return
	 */
	public double getIntraDistance () {
		return intraDistance;
	}
	
	/**
	 * Aggiorna il centroide del cluster (inizialmente è scelto randomicamente),  
	 * aggiornando il valore per ciascun Item della tupla centroide.
	 * Avvalora intraDistance
	 * @param data Tabella contenente le tuple su cui fare il calcolo
	 */
	void computeCentroid(Data data){
		for(int i=0;i<centroid.getLength();i++)
			centroid.get(i).update(data,clusteredData);
		intraDistance = centroid.avgDistance(data, clusteredData);
	}
	
	/**
	 * Aggiorna il membro clusteredData con la tupla id: se la tupla già esiste 
	 * restituisce false,altrimenti true
	 * @param id Indice di tupla da inserire nel cluster
	 * @return True se la tupla è stata aggiunta, false altrimenti
	 */
	boolean addData(int id){
		return clusteredData.add(id);
		
	}
	
	/**
	 * Verifica se il membro clusteredData contiene la tupla id
	 * @param id Indice di tupla da verificare nel cluster
	 * @return True se la tupla è presente, false altrimenti
	 */
	boolean contain(int id){
		return clusteredData.contains(id);
	}
	
	/**
	 * Rimuove dal membro clusteredData la tupla id
	 * @param id Indice di tupla da rimuovere dal cluster
	 */
	void removeTuple(int id){
		clusteredData.remove(id);
	}
	
	/**
	 * Restituisce una stringa fatta da ciascun Item del membro centroid
	 */
	public String toString(){
		String str="Centroid=( ";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i)+" ";
		str+=")";
		return str;
	}
	
	/**
	 * Restituisce una stringa fatta da: ciascun Item del membro centroid, dalle tuple 
	 * in clusteredData appartenenti al cluster, dalla distanza tra centroid e 
	 * ciascuna tupla in clusteredData, e dalla distanza media tra centroid e ciascuna tupla.
	 * @param data Tabella contenente le tuple
	 */
	public String toString(Data data){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i)+ " ";
		str+=")\nExamples:\n";
		for(Integer i: clusteredData){
			str+="[";
			for(int j=0;j<data.getNumberOfExplanatoryAttributes();j++)
				str+=data.getAttributeValue(i, j)+" ";
			str+="] dist="+getCentroid().getDistance(data.getItemSet(i))+"\n";
			}
		str+="AvgDistance="+getCentroid().avgDistance(data, clusteredData)+"\n";
		return str;
	}

}