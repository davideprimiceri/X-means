package mining;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import data.Data;
import data.OutOfRangeSampleSize;


/**
 * La classe XmeansMiner modella l'algoritmo XMeans e inoltre si occupa della lettura
 * dei cluster da file e del salvataggio.
 * @author Davide Primiceri
 */
public class XmeansMiner implements Serializable {
	/**
	 * Risultato dell’algoritmo Xmeans	
	 */
	private ClusterSet C;
	
	/**
	 * Inizializza il ClusterSet con il numero di cluster che si vogliono generare
	 * @param k Numero di cluster da generare
	 */
	public XmeansMiner(int k) {
		C = new ClusterSet (k);
	}
	
	/**
	 * Inizializza il ClusterSet con l'insieme di cluster letti da file
	 * @param fileName Nome file da leggere
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public XmeansMiner (String fileName) throws IOException, ClassNotFoundException {
		FileInputStream inFile = new FileInputStream (fileName);
		ObjectInputStream inStream = new ObjectInputStream (inFile);
		C = (ClusterSet)inStream.readObject();
		inFile.close();
	}
	
	/**
	 * Restituisce l’insieme di clusters generati
	 * @return Membro {@link C}
	 */
	public ClusterSet getC() {
		return C;
	}
	
	/**
	 * Serializza l'oggetto ClusterSet C nel file fileName
	 * @param fileName Nome del file in cui si vuole salvare
	 * @throws IOException
	 */
	public void salva (String fileName) throws IOException {
		FileOutputStream outFile = new FileOutputStream (fileName);
		ObjectOutputStream outStream = new ObjectOutputStream (outFile);
		outStream.writeObject (C);
		outFile.close();
	}
	
	/**
	 * Esegue l’algoritmo di clustering eseguendo i seguenti passi:
	 * 1. Selezione di k tuple come iniziali centroidi per k clusters
	 * 2. Assegnazione di ciascuna tupla al cluster il cui centroide ha distanza minima 
	 *    dalla tupla.
	 * 3. Ri-determinazione dei centroidi per ciascun cluster
	 * 4. Ri-esecuzione iterativa dei punti 2 e 3 finché non coincidono i centroidi 
	 *    tra due iterazioni consecutive
	 *    
	 * @param data Tabella contenente le tuple
	 * @return Numero di iterazioni eseguite
	 * @throws OutOfRangeSampleSize
	 */
	public int Xmeans(Data data) throws OutOfRangeSampleSize {
		int nIterations = 0;
		boolean changed = false;
		C.initializeCentroids(data);
		do {
			nIterations++;
			changed = false;
			for(int i=0; i<data.getNumberOfExamples(); i++){
				Cluster nearest = C.nearestCluster(data.getItemSet(i));
				Cluster current = C.currentCluster(i);
				boolean currentChange = nearest.addData(i);
				if(currentChange)
					changed = true;
				if(currentChange && current!=null)
					current.removeTuple(i);
			}
			C.updateCentroids(data);
		}while(changed);
		return nIterations;
	}
	
	/**
	 * Calcola l'indice di valutazione Davies-Bouldin per la qualità dei cluster generati 
	 * in una iterazione
	 * @return Indice di Davies-Bouldin
	 */
	public double computeDBIndex() {
		double temp = 0;
		double max = 0;
		double result = 0;
		for (int i = 0; i < C.size(); i++) {
			for (int j = i+1; j < C.size(); j++) {
					double sumAvg = (C.get(i).getIntraDistance()+ C.get(j).getIntraDistance());
					temp = sumAvg / C.get(i).getCentroid().getDistance(C.get(j).getCentroid());
					if (temp > max)
						max = temp;
				}
			result += max;
		}
		return result / C.size();
	}
}