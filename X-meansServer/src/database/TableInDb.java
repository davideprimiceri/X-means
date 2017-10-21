package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * La classe TableInDb si occupa di prelevare dal database i nomi delle tabelle presenti
 * @author Davide Primiceri
 */
public class TableInDb {
	
	/**
	 * Riferimento alla base di dati
	 */
	DbAccess db;
	
	/**
	 * Inizializza il membro db
	 * @param db Riferimento alla base di dati
	 */
	public TableInDb(DbAccess db) {
		this.db = db;
	}
	
	/**
	 * Il metodo restituisce tutte le tabelle presenti nel database.
	 * @return Tabelle presenti
	 * @throws SQLException
	 */
	public String[] getTableDB () throws SQLException{
		int nTab = 0, i = 0;
		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet r = meta.getTables(null, null, null, null);
		if (r.last()) {
			nTab = r.getRow();
			r.beforeFirst();
		}
		String [] table = new String [nTab];
		while (r.next()) {
			table[i] = r.getString(3);
			i++;
		}
		r.close();
		return table;
	}

}