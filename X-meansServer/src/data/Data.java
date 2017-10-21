package data;

import java.io.Serializable;
import java.util.*;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.TableData;
import database.TableSchema;
import database.QUERY_TYPE;
import java.sql.SQLException;

/**
 * La classe Data modella l'insieme di tuple prelevate dalla base di dati.
 * @author Davide Primiceri
 */
public class Data implements Serializable {
	
	/**
	 * Insieme di tuple
	 */
	private List<Example> data;
	
	/**
	 * Numero di tuple presenti in data
	 */
	private int numberOfExamples;
	
	/**
	 * Insieme di attributi presenti e valori che possono assumere
	 */
	private List<Attribute> explanatorySet;
	
	DbAccess db;
	
	/**
	 * Il costruttore avvalora i membri {@link data} e {@link explanatorySet} facendo uso di 
	 * metodi presenti in {@link database.TableData} e recuperando la struttura della tabella da
	 * {@link database.TableSchema}
	 * 
	 * @param table Nome della tabella da cui prelevare le tuple
	 * @throws SQLException
	 * @throws EmptySetException
	 * @throws DatabaseConnectionException
	 * @throws NoValueException
	 */
	public Data (String table ,DbAccess db) throws SQLException, EmptySetException, DatabaseConnectionException, NoValueException {
		data = new ArrayList<Example>();
		this.db = db;
		TableData td = new TableData (db);
 		TableSchema ts = new TableSchema (db, table);
 		data = td.getDistinctTransazioni (table);
 		numberOfExamples = data.size ();		
 		explanatorySet = new LinkedList<Attribute>();
 		for (int i=0; i<ts.getNumberOfAttributes(); i++) {
 			String attribute = ts.getColumn(i).getColumnName();
 			if (ts.getColumn(i).isNumber()) {
 				double min = ((Float)td.getAggregateColumnValue(table, ts.getColumn(i), QUERY_TYPE.MIN)).doubleValue();
 				double max = ((Float)td.getAggregateColumnValue(table, ts.getColumn(i), QUERY_TYPE.MAX)).doubleValue();
 				explanatorySet.add(new ContinuousAttribute(attribute, i, min, max));
 			} else {
 				Set<Object> distinct = td.getDistinctColumnValues(table, ts.getColumn(i));
 				TreeSet<String> valueAttribute = new TreeSet<String>();
 				for(Object o : distinct)
 					valueAttribute.add((String)o);
 				explanatorySet.add ( new DiscreteAttribute(attribute, i, valueAttribute ) );
 		 	}
 		}
 	}
 	
	/**
	 * Restituisce il membro {@link numberOfExamples}
	 * @return Cardinalità dell'insieme di tuple 
	 */
	public int getNumberOfExamples() { 
		return numberOfExamples;
	}
	
	/**
	 * Restituisce la cardinalità del membro {@link explanatorySet}
	 * @return Cardinalità explanatorySet
	 */
	public int getNumberOfExplanatoryAttributes() { 
		return explanatorySet.size(); 
	}
	
	/**
	 * Restituisce l'array membro @link explanatorySet}
	 * @return Array degli attributi
	 */
	public List<Attribute> getAttributeSchema() { 
		return explanatorySet;
	}
	
	/**
	 * Restituisce il valore dell'attributo attributeIndex per la transazione exampleIndex 
	 * @param exampleIndex Indice di riga indicante il valore di un attributo
	 * @param attributeIndex Indice di colonna indicante il nome di un attributo
	 * @return Object associato all'attributo per la transazione indicizzata in input 
	 */
	public Object getAttributeValue (int exampleIndex, int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);
	}
	
	/**
	 * Per ogni transazione, legge i valori di tutti gli attributi in {@link data} e li concatena 
	 * in un oggetto String che restituisce come risultato finale in forma di sequenze di testi.
	 */
	public String toString () {
		String s = new String();
		for (int i=0; i<getNumberOfExamples(); i++) { 
			s += (i+1)+":";
			for(int j=0; j<getNumberOfExplanatoryAttributes(); j++) 
				s += getAttributeValue(i, j)+",";
			s += "\n";
		}
		return s;
	}
	
	/**
	 * Crea e restituisce un oggetto Tuple costituito con le coppie Attributo-valore della i-esima transazione
	 * @param index Indice sull'insieme di tuple
	 * @return Oggetto Tuple con gli Item (coppie  Attributo-Valore) che lo compongono
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(getNumberOfExplanatoryAttributes());
		for(int i=0; i<getNumberOfExplanatoryAttributes(); i++)
			if (getAttributeValue(index, i) instanceof Double)
				tuple.add(new ContinuousItem((ContinuousAttribute)explanatorySet.get(i), (Double)getAttributeValue(index, i)), i);
			else
				tuple.add(new DiscreteItem((DiscreteAttribute)explanatorySet.get(i), (String)getAttributeValue(index, i)), i);
		return tuple;
	}
	
	/**
	 * Inizializza i k-cluster assegnando in modo random a ciascuno di essi una Tupla dall’insieme in Data.
	 * @param k Numero di cluster da generare
	 * @return Array con gli indici delle Tuple-centroidi selezionati rispetto all’insiemem iniziale
	 * @throws OutOfRangeSampleSize
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize {
		int centrInd[] = new int[k];
		Random random = new Random();
		random.setSeed(System.nanoTime());
		if (k > getNumberOfExamples() || k<=0)
			throw new OutOfRangeSampleSize("Il numero di cluster da generare non è valido");
		else {
			for (int i=0; i<k; i++) {
				int j = 0;
				boolean same = false;
				do {
					same = false;
					j = random.nextInt(getNumberOfExamples());
					for(int z=0; z<i; z++) {
						if(compare(centrInd[z],j)) {
							same = true;
							break;
						}
					}
				}while(same);
				centrInd[i]=j;
			}
		}
		return centrInd;
	}
	
	/**
	 * Verifica se due Tuple sono coincidenti
	 * @param i Indice di una tupla
	 * @param j Indice di una tupla
	 * @return Restituisce true se le tuple sono coincidenti, altrimenti false
	 */
	private boolean compare(int i,int j) {
		Object o1 = new Object(),
			   o2 = new Object();
		boolean same=true;
		for (int k=0;k<getNumberOfExplanatoryAttributes();k++){
			o1 = getAttributeValue(i, k);
			o2 = getAttributeValue(j, k);
			if(o1 != o2)
				same = false;
		}
		return same;
	}
	
	/**
	 * Invoca i metodi per i casi discreto e continuo per la determinazione del centroide rispetto ad attribute
	 * @param idList Tuple appartenenti ad un cluster
	 * @param attribute Attributo rispetto al quale calcolare il prototipo
	 * @return Valore centroide rispetto ad attribute
	 */
	Object computePrototype(Set<Integer> idList,  Attribute attribute) {
		if (attribute instanceof DiscreteAttribute)
			return computePrototype (idList, (DiscreteAttribute)attribute);
		else
			return computePrototype (idList, (ContinuousAttribute)attribute);
	}
	
	/**
	 * Determina il prototipo, come valore più frequente, rispetto ad attribute (caso discreto)
	 * @param idList Tuple appartenenti ad un cluster
	 * @param attribute Attributo discreto rispetto al quale calcolare il prototipo
	 * @return Valore discreto centroide rispetto ad attribute
	 */
	String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		int freq = 0, maxFreq = 0;
		String centr = new String();
		for(String val : attribute) {
			freq = attribute.frequency(this, idList, val);
			if(freq > maxFreq){
				maxFreq = freq;
				centr = val;
			}
		}
		return centr;
	}
	
	/**
	 * Determina il prototipo, come valore medio, sui valori di attribute nelle tuple idList (caso continuo)
	 * @param idList Tuple appartenenti ad un cluster
	 * @param attribute Attributo continuo rispetto al quale calcolare il prototipo
	 * @return Valore continuo centroide rispetto ad attribute
	 */
	Double computePrototype(Set<Integer>  idList,  ContinuousAttribute attribute) {
		double sum = 0.0;
		int indAttr = attribute.getIndex();
		for (Integer val : idList) 
			sum += (Double)getAttributeValue(val, indAttr);
		return sum/idList.size();
	}
			
}