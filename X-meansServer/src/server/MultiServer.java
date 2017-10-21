package server;

import java.io.*;
import java.net.*;

/**
 * La classe MultiServer accetta la richiesta di ciascun Client e la evade avviando un thread 
 * ServerOneClient. Il Server sarà  lanciato separatamente dal Client e sarà disponibile su 
 * una porta predefinita (al di fuori del range 1-1024). 
 * Il Server gestirà richieste da più client contemporaneamente.
 * @author Davide Primiceri
 */
public class MultiServer extends Thread{
	
	/**
	 * Porta su cui il server è in ascolto
	 */
	static final int PORT = 8080;
	
	/**
	 * Istanzia un oggetto di tipo MultiServer
	 */
	public static void main (String [] args) {
		MultiServer m = new MultiServer();
	}
	
	/**
	 * Avvia il thread (start()) e stampa sulla console il messaggio “Server Avviato”.
	 */
	public MultiServer () {
		start();
		System.out.println ("Server avviato");
	}
	
	/**
	 * Avvia una socket che si mette in attesa di nuove connessioni. Ad ogni nuova 
	 * connessione crea un oggetto ServerOneClient e lo associa alla nuova connessione
	 */
	public void run () {
		ServerSocket s = null;
		try {
			s = new ServerSocket (PORT);
			try {
				while(true) {
					// Si blocca finchè non si verifica una connessione:
					Socket socket = s.accept();
					System.out.println("Nuovo client connesso");
					try {
						new ServerOneClient(socket);
					} catch(IOException e) {
						socket.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
					}
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
