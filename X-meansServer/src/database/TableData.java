package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import database.TableSchema.Column;

/**
 * La classe TableData modella l'insieme delle tuple contenute in una tabella.
 * Ogni tupla è un oggetto Example
 * @author Davide Primiceri
 */
public class TableData {
	
	/**
	 * Riferimento alla base di dati
	 */
	DbAccess db;

	/**
	 * Inizializza il membro db
	 * @param db Riferimento alla base di dati
	 */
	public TableData (DbAccess db) {
		this.db=db;
	}

	/**
	 * Interroga la tabella passata in input considerando le tuple distinte.  
	 * Per ogni tupla del ResultSet, istanza un Example e controlla se un elemento-colonna 
	 * è String oppure Number e lo aggiunge alla istanza corrente di Example. 
	 * Infine aggiunge la tupla Example alla lista di Example da restituire in output. 
	 * Il metodo solleva e propaga una eccezione SQLException se la tabella non ha colonne ed 
	 * una eccezione EmptySetException se l'insieme delle tuple distinte è vuoto
	 * 
	 * @param table Tabella contenente le tuple
	 * @return Lista di transazioni distinte contenute nella tabella
	 * @throws SQLException
	 * @throws EmptySetException
	 */
	public List<Example> getDistinctTransazioni (String table) throws SQLException, EmptySetException {
		List<Example> distinct = new ArrayList<Example> ();
		String query = "SELECT DISTINCT * FROM " +table +";";
		Statement s = DbAccess.getConnection().createStatement();
		ResultSet rs = s.executeQuery(query);
		TableSchema ts = new TableSchema(db, table);
		if (!rs.next())
			throw new EmptySetException ("Nessuna tupla nella tabella");
		else {
			do {
				Example e = new Example();
				for (int i=0; i<ts.getNumberOfAttributes(); i++) {
					if (ts.getColumn(i).isNumber())
						e.add(rs.getDouble(ts.getColumn(i).getColumnName()));
					else
						e.add(rs.getString(ts.getColumn(i).getColumnName()));
				}
				distinct.add(e);
			} while(rs.next());
				
		}
		rs.close();
		return distinct;
	}
	
	/**
	 *  Interroga la tabella passata in input. Seleziona dalla tabella solo la colonna 
	 *  passata in input restituendo i valori ordinati e distinti della colonna column sia 
	 *  che si tratti di una colonna a valori discreti sia che si tratti di una colonna
	 *  a valori numerici
	 *  
	 * @param table Tabella contenente le tuple
	 * @param column Colonna dalla quale collezionare valori
	 * @return Lista dei valori distinti della colonna column
	 * @throws SQLException
	 */
	public Set<Object> getDistinctColumnValues (String table, Column column) throws SQLException {
		Set<Object> distinct = new TreeSet<Object> ();
		String query = "SELECT DISTINCT " +column.getColumnName() +" FROM " +table +" ORDER BY " +column.getColumnName() +" ASC;";
		Statement s = DbAccess.getConnection().createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
			distinct.add(rs.getObject(column.getColumnName()));
		rs.close();
		return distinct;
	}

	/**
	 * Interroga la tabella passata in input. Seleziona dalla tabella solo la colonna
	 * passata in input e restituisce il valore aggregato della colonna column corrispondente
	 * all'argomento aggregate. 
	 * Il valore aggregato va calcolato solo per attributi  continui. 
	 * Il metodo solleva un'eccezione NoValueException se l'aggregato ha valore null
	 * 
	 * @param table Tabella contenente le tuple
	 * @param column Colonna da cui selezionare l'aggregato
	 * @param aggregate MIN o MAX
	 * @return Lista del valore aggregato della colonna column
	 * @throws SQLException
	 * @throws NoValueException
	 */
	public Object getAggregateColumnValue (String table, Column column, QUERY_TYPE aggregate) 
			throws SQLException, NoValueException {
		Object aggr = new Object();
		String query = "SELECT " +aggregate +"(" +column.getColumnName() +") FROM " +table +";";
		Statement s = DbAccess.getConnection().createStatement();
		ResultSet rs = s.executeQuery(query);
			if (!rs.next())
				throw new NoValueException ("Nessun risultato dalla query");
			else
				aggr = rs.getObject(1);
		if (aggr == null)
			throw new NoValueException ("L'aggregato ha valore null");
		rs.close();
		return aggr;
	}

}