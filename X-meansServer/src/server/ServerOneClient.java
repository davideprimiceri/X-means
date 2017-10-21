package server;

import java.io.*;
import java.net.*;
import data.Data;
import mining.XmeansMiner;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.NoValueException;
import database.TableInDb;
import data.OutOfRangeSampleSize;
import java.sql.SQLException;

/**
 * La classe ServerOneClient estende la classe Thread. ServerOneClient comunica con un 
 * solo Client procedendo nell’uso della applicazione dipendentemente dalla selezione 
 * di input dal Client
 * @author Davide Primiceri
 */
public class ServerOneClient extends Thread {
	
	/**
	 * Porta per il canale di connessione con il client
	 */
	private Socket socket;
	
	/**
	 * Oggetto istanza della classe XmeansMiner
	 */
	private XmeansMiner xmeans;
	
	/**
	 * Stream di input
	 */
	private ObjectInputStream in;
	
	/**
	 * Stream di output
	 */
	private ObjectOutputStream out;
	
	/**
	 * Costruttore di classe. Collega la socket passata, crea gli stream di input e output 
	 * sulla socket, quindi avvia il thread (start()).
	 * @param s Oggetto istanza della classe socket
	 * @throws IOException
	 */
	public ServerOneClient (Socket s) throws IOException {
		socket = s;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		start(); 
	}	
	
	/**
	 * Gestisce le richieste del client eseguendo una delle possibili selezioni dell'utente
	 */
	public void run() {
		try {
			while (true) {
				DbAccess db = new DbAccess();
				try {
					DbAccess.initConnection();
				} catch (DatabaseConnectionException e1) {
					e1.printStackTrace();
				}
				int scelta = (Integer)in.readObject();
				String fileName;
				switch (scelta) {
					case 0:
						TableInDb tid = new TableInDb(db);
						String [] table = null;
						try {
							table = tid.getTableDB();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						out.writeObject(table.length);
						for (int i=0; i<table.length; i++)
							out.writeObject(table[i]);
						break;
					case 1:
						Data data;
						String tableName = (String)in.readObject();
						String iterMin = (String)in.readObject();
						String iterMax = (String)in.readObject();
						int kMin = Integer.parseInt(iterMin);
						int kMax = Integer.parseInt(iterMax);
						try {
							data = new Data (tableName, db);
						} catch (SQLException e) {
							out.writeObject("ERRORE: "+e.getMessage());
							break;
						} catch (DatabaseConnectionException e) {
							out.writeObject("ERRORE: "+e.getMessage());
							break;
						} catch (EmptySetException e) {
							out.writeObject("ERRORE: "+e.getMessage());
							break;
						} catch (NoValueException e) {
							out.writeObject("ERRORE: "+e.getMessage());
							break;
						}
						XmeansMiner Xmeans [] = new XmeansMiner [kMax-kMin+1];
						double DBindex [] = new double [kMax-kMin+1];
						int numIter [] = new int [kMax-kMin+1];
						int k = 0;
						boolean flag = false;
						for (int i=kMin; i<=kMax; i++) {
							Xmeans [k] = new XmeansMiner(i);
							try {
								numIter [k] = Xmeans[k].Xmeans(data);
								DBindex [k] = Xmeans[k].computeDBIndex();
								k++;
							} catch(OutOfRangeSampleSize e) {
								out.writeObject("ERRORE: "+e.getMessage());
								flag = true;
								break;
							}
						}
						if (flag)
							break;
						else {
							double minIndex = DBindex[0];
							int posIndex = 0;
							for (int i=1; i<DBindex.length; i++) {
								if (minIndex > DBindex[i]) {
									minIndex = DBindex[i];
									posIndex = i;
								}
							}
							out.writeObject(numIter[posIndex]);
							out.writeObject(Xmeans[posIndex].getC().toString(data));
							fileName = (String)in.readObject() + ".dmp";
							try {
								Xmeans[posIndex].salva(fileName);
							} catch (IOException e) {
								e.printStackTrace();
								break;
							}
						}
						break;
					case 2:
						try {
							fileName = (String)in.readObject() + ".dmp";
							xmeans = new XmeansMiner (fileName);
						} catch (FileNotFoundException e) {
							out.writeObject("ERRORE: "+e.getMessage());
							break;
						}
						out.writeObject(xmeans.getC().toString());
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch(IOException e) {
				System.err.println("Socket not closed");
			}
		}
	}

}
	

