package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * La classe TableSchema modella una tabella di un database relazionale ed il mapping 
 * tra tipi di dato Java e tipi di dato SQL (in MySQL). Inoltre, contiene la classe inner
 * Column
 * @author Davide Primiceri
 */
public class TableSchema {
	
	/**
	 * Riferimento alla base di dati
	 */
	DbAccess db;
	
	/**
	 * L'inner class Column gestisce le colonne della tabella
	 * @author Davide Primiceri
	 */
	public class Column{
		
		/**
		 * Nome colonna
		 */
		private String name;
		/**
		 * Tipo di dati presenti nella colonna
		 */
		private String type;
		
		/**
		 * Avvalora i membri {@link name} e {@link type}
		 * @param name
		 * @param type
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}
		
		/**
		 * Restituisce il membro {@link name}
		 * @return Valore di name
		 */
		public String getColumnName(){
			return name;
		}
		
		/**
		 * Restituisce true se la colonna su cui è invocato è di tipo number,
		 * false altrimenti
		 * @return True se numero, false altrimenti
		 */
		public boolean isNumber(){
			return type.equals("number");
		}
		
		/**
		 * Restituisce una stringa del tipo: Outlook:string
		 */
		public String toString(){
			return name+":"+type;
		}
	}
	
	/**
	 * Lista colonne tabella
	 */
	List<Column> tableSchema=new ArrayList<Column>();
	
	/**
	 * Crea lo schema della tabella tableName creata nella base di dati accessibile col 
	 * riferimento db
	 * @param db Riferimento alla base di dati
	 * @param tableName Tabella presente nel database
	 * @throws SQLException
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException{
		this.db=db;
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		Connection con=db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
	    ResultSet res = meta.getColumns(null, null, tableName, null);
		   
	    while (res.next()) {
	         
	         if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	        		 tableSchema.add(new Column(
	        				 res.getString("COLUMN_NAME"),
	        				 mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
	        				 );
	
	      }
	      res.close();
	    
	}
	  
	/**
	 * Restituisce il numero delle colonne della tabella
	 * @return Numero colonne
	 */
	public int getNumberOfAttributes(){
		return tableSchema.size();
	}
	
	/**
	 * Restituisce la colonna in posizione index della tabella
	 * @param index Indice colonna
	 * @return Colonna in posizione dell'indice
	 */
	public Column getColumn(int index){
		return tableSchema.get(index);
	}

}
