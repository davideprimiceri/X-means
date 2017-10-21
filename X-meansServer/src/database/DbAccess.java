package database;
import java.sql.*;

/**
 * La classe DbAccess gestisce l'interfacciamento alla base di dati
 * @author Davide Primiceri
 */
public class DbAccess {
	/**
	 * Nome Driver per la connessione
	 */
	private static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	/**
	 * Tipo Database Management System
	 */
	private static final String DBMS = "jdbc:mysql";
	/**
	 * Identificativo del server su cui risiede la base di dati
	 */
	private static final String SERVER = "localhost";
	/**
	 * Nome base di dati
	 */
	private static final String DATABASE = "xmeans2016";
	/**
	 * Porta su cui il db accetta le connessioni
	 */
	private static final int PORT = 3306;
	/**
	 * Nome utente per accedere alla base di dati
	 */
	private static final String USER_ID = "xmuser";
	/**
	 * Password per accedere alla base di dati (per l'utente USER_ID)
	 */
	private static final String PASSWORD = "xmpassword";
	/**
	 * Gestore connessione
	 */
	private static Connection conn;
	
	/**
	 * Provvede a caricare il driver per stabilire la connessione con la base di dati e  
	 * inizializza tale connessione
	 * @throws DatabaseConnectionException
	 */
	public static void initConnection() throws DatabaseConnectionException {
		String connectionString = DBMS+"://" +SERVER +":" +PORT +"/" +DATABASE;
		try{
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch(Exception ex) {
			System.out.println("Impossibile trovare il Driver: " + DRIVER_CLASS_NAME);
			}
		try{
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
		} catch(SQLException e) {
			System.out.println("Impossibile connettersi al DB");
			e.printStackTrace();
			}
	}
	
	/**
	 * Restituisce la connessione corrente
	 * @return Restituisce il membro {@link conn}
	 */
	public static Connection getConnection() { 
		return conn;
	}
	
	/**
	 * Chiude la connessione
	 * @throws SQLException
	 */
	public static void closeConnection () throws SQLException {
		conn.close();
	}
}
