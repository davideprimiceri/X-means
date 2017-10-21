import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * La classe XMeansClient integra una interfaccia grafica per la interazione con  
 * l'applicazione tramite la selezione di una delle due modalità di esecuzione 
 * (esecuzione di Xmeans, lettura da file).
 * Per la creazione dell'applet la classe XMeansClient estende la classe JApplet
 * implementandone i metodi
 * @author Davide Primiceri
 */
public class XMeansClient extends JApplet {
	
	/**
	 * Stream di output per inviare richieste al server
	 */
	private ObjectOutputStream out;
	
	/**
	 * Stream di input per ricevere risultati dal server
	 */
	private ObjectInputStream in;
	
	/**
	 * Il metodo si occupa di ricevere dal server i nomi delle tabelle presenti nel
	 * database e le memorizza in un'array di stringhe che poi resituisce per essere
	 * passato alla ComboBox
	 */
	String [] getTableDB () {
		int nTab=0;
		String [] tableDB = null;
		try {
			nTab = (Integer)in.readObject();
			tableDB = new String [nTab];
			for (int i=0; i<nTab; i++)
				tableDB [i] = (String)in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return tableDB;
	}
	
	/**
	 * La classe TabbedPane estende JPanel ed è inner in XMeansClient. Si occupa della 
	 * realizzazione di contenitori Tab attraverso  aggregazione  di JTabbedPane.
	 * @author Davide Primiceri
	 */
	private class TabbedPane extends JPanel {
		
		/**
		 * Tab per esecuzione di XMeans
		 */
		private JPanelClusterDB panelDB;
		
		/**
		 * Tab per lettura da file
		 */
		private JPanelClusterFile panelFile;
		
		/**
		 * La classe JPanelClusterDB, estende JPanel ed è inner in TabbedPane, per la 
		 * realizzazione di un pannello GUI per l'esecuzione di XMeans
		 * @author Davide Primiceri
		 */
		private class JPanelClusterDB extends JPanel {
			
			/**
			 * Menù a tendina dove scegliere la tabella
			 */
			JComboBox <String> table = new JComboBox <String> (getTableDB());
			
			/**
			 * Casella di testo per inserire il valore minimo delle iterazioni
			 */
			JTextField kTextMin = new JTextField(10);
			
			/**
			 * Casella di testo per inserire il valore massimo delle iterazioni
			 */
			JTextField kTextMax = new JTextField(10);
			
			/**
			 * Casella di testo per inserire il nome del file in cui si vuole salvare
			 */
			JTextField fileText = new JTextField(20);
			
			/**
			 * Area in cui vengono visualizzati i risultati dell'esecuzione di XMeans
			 */
			JTextArea clusterOutput = new JTextArea(25,0);
			
			/**
			 * Bottone da premere per avviare l'esecuzione di XMeans
			 */
			JButton executeButton;
			/**
			 * Realizza il Panel per l'esecuzione di XMeans e associa al componente JButton
			 * un oggetto Listener
			 * @param buttonName Nome per {@link #executeButton}
			 * @param a Oggetto Listener da associare al bottone
			 */
			JPanelClusterDB (String buttonName, java.awt.event.ActionListener a) {
				setLayout (new BoxLayout (this,BoxLayout.Y_AXIS));
				JPanel upPanel = new JPanel();
				upPanel.setLayout(new FlowLayout());
				upPanel.add(new JLabel("Table:"));
				upPanel.add(table);
				upPanel.add(new JLabel("Kmin:"));
				upPanel.add(kTextMin);
				add(upPanel);
				upPanel.add(new JLabel("Kmax:"));
				upPanel.add(kTextMax);
				add(upPanel);
				upPanel.add(new JLabel("Salva in:"));
				upPanel.add(fileText);
				add(upPanel);
				JPanel centralPanel=new JPanel();
				centralPanel.setLayout(new GridLayout(1,1));
				clusterOutput.setEditable(false);
				JScrollPane scrollingArea= new JScrollPane(clusterOutput);
				centralPanel.add(scrollingArea);
				add(centralPanel);
				JPanel downPanel = new JPanel();
				downPanel.setLayout (new FlowLayout());
				executeButton = new JButton (buttonName);
				executeButton.addActionListener (a);
				downPanel.add (executeButton);
				add(downPanel); 
			}
		}
		
		/**
		 * La classe JPanelClusterFile, estende JPanel ed è inner in TabbedPane, per la 
		 * realizzazione di un pannello GUI per la lettura da file
		 * @author Davide Primiceri
		 */
		private class JPanelClusterFile extends JPanel {
			
			/**
			 * Casella di testo in cui inserire il nome del file da cui si vogliono
			 * leggere i cluster di una precedente esecuzione
			 */
			JTextField fileText = new JTextField(20);
			
			/**
			 * Area in cui vengono visualizzati i cluster contenuti nel file scelto
			 */
			JTextArea clusterOutput = new JTextArea(15,0);
			
			/**
			 * Bottone da premere per avviare la lettura da file
			 */
			JButton executeButton;
			
			/**
			 * Realizza il Panel per la lettura da file e associa al componente JButton
			 * un oggetto Listener
			 * @param buttonName Nome per {@link #executeButton}
			 * @param a Oggetto Listener da associare al bottone
			 */
			JPanelClusterFile (String buttonName, java.awt.event.ActionListener a) {
				setLayout (new BoxLayout (this,BoxLayout.Y_AXIS));
				JPanel upPanel = new JPanel();
				upPanel.setLayout(new FlowLayout());
				upPanel.add(new JLabel("Nome file:"));
				upPanel.add(fileText);
				add(upPanel);
				JPanel centralPanel=new JPanel();
				centralPanel.setLayout(new GridLayout(1,1));
				clusterOutput.setEditable(false);
				JScrollPane scrollingArea= new JScrollPane(clusterOutput);
				centralPanel.add(scrollingArea);
				add(centralPanel);
				JPanel  downPanel = new JPanel();
				downPanel.setLayout (new FlowLayout());
				executeButton = new JButton (buttonName);
				executeButton.addActionListener (a);
				downPanel.add (executeButton);
				add(downPanel);
			}
		}
		
		/**
		 * Istanzia la classe JTabbedPane e istanzia la classe JPanelCluster per panelDB e 
		 * panelFile (ai cui JButton associa due oggetti Listener)
		 */
		TabbedPane() {
			super(new GridLayout(1, 1)); 
			JTabbedPane tabbedPane= new JTabbedPane();
			java.net.URL imgURL = getClass().getResource("img/db.jpg");
			ImageIcon iconDB = new ImageIcon(imgURL);
			
			panelDB = new JPanelClusterDB("MINE", new java.awt.event.ActionListener() {
				
				public void actionPerformed (ActionEvent e) {
					try{
						runXMeans();
					}
					catch(SocketException e1) {
						System.out.println(e1);
					}
					catch(FileNotFoundException e1) {
						System.out.println(e1);
					} catch(IOException e1) {
						System.out.println(e1);
					} catch(ClassNotFoundException e1) {
						System.out.println(e1);
					} catch (ServerException e1) {
						JOptionPane.showMessageDialog(panelDB, e1.getMessage());
					}
				}
			});
			
			tabbedPane.addTab ("DB", iconDB, panelDB, "Does nothing");
			imgURL= getClass().getResource ("img/file.jpg");
			ImageIcon iconFile = new ImageIcon (imgURL);
			
			panelFile = new JPanelClusterFile ("STORE FROM FILE",new java.awt.event.ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					try{
						readFromFile();
					}
					catch(SocketException e1) {
						System.out.println(e1);
					}
					catch(FileNotFoundException e1) {
						System.out.println(e1);
					} catch(IOException e1) {
						System.out.println(e1);
					} catch(ClassNotFoundException e1) {
						System.out.println(e1);
					} catch (ServerException e1) {
						JOptionPane.showMessageDialog(panelFile, e1.getMessage());
					}
				}
			});
			
			tabbedPane.addTab("FILE", iconFile, panelFile, "Does nothing");
			//Add the tabbedpane to this panel.
			add(tabbedPane); 
			//The following line enables to use scrolling tabs.
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}
		
		/**
		 * Metodo che invia stream al server per la lettura da file e acquisisce i risultati
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 * @throws ServerException
		 */
		private void readFromFile () throws SocketException, IOException, ClassNotFoundException, ServerException {
			String fileName = panelFile.fileText.getText();
			if(fileName.equals("")) {
				JOptionPane.showMessageDialog(this, "Inserire il nome del file");
				return;
			}
			out.writeObject(2);
			out.writeObject(fileName);
			String str = (String)in.readObject();
			if ( str.startsWith("ERRORE") ) {
				throw new ServerException(str);
			}
			else
				panelFile.clusterOutput.setText(str);		
		}
		
		/**
		 * Metodo che invia stream al server per l'esecuzione di XMeans e acquisisce i 
		 * risultati
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 * @throws ServerException
		 */
		private void runXMeans () throws SocketException, IOException, ClassNotFoundException, ServerException {
			String tableName = (String)panelDB.table.getSelectedItem();
			String fileName = panelDB.fileText.getText();
			String kMin = panelDB.kTextMin.getText();
			String kMax = panelDB.kTextMax.getText();
			if(kMin.equals("")) {
				JOptionPane.showMessageDialog(this, "Inserire il valore di kMin");
				return;
			}
			if(kMax.equals("")) {
				JOptionPane.showMessageDialog(this, "Inserire il valore di kMax");
				return;
			}
			if(fileName.equals("")) {
				JOptionPane.showMessageDialog(this, "Inserire il nome del file in cui salvare");
				return;
			}
			if (Integer.parseInt(kMin) > Integer.parseInt(kMax)) {
				JOptionPane.showMessageDialog(this, "Attenzione! kMin > kMax");
				return;
			}
			out.writeObject(1);
			out.writeObject(tableName);
			out.writeObject(kMin);
			out.writeObject(kMax);
			Object o = in.readObject();
			if ( o instanceof String )
				throw new ServerException((String)o);
			else {
				int nIter = (Integer)o;
				panelDB.clusterOutput.setText("Numero iterate: " +nIter +"\n" +(String)in.readObject());
				out.writeObject(fileName);
			}
		}
	}
	
	/**
	 * Prepara  l'applet e ne realizza la GUI istanziando la classe JTabbedPane. 
	 * Inoltre avvia la richiesta al Server ed istanzi  gli Stream di comunicazione.
	 */
	public void init(){
		getContentPane().setLayout (new GridLayout(1,1));
		String ip = "127.0.0.1";
		int port = 8080;
		try
		{
			InetAddress addr= InetAddress.getByName(ip); //ip
			System.out.println("addr = "+ addr);
			Socket socket= new Socket(addr, port); 
			System.out.println(socket);
			out = new ObjectOutputStream (socket.getOutputStream());
			in = new ObjectInputStream (socket.getInputStream());
			out.writeObject(0);
			TabbedPane tb = new TabbedPane();
			getContentPane().add(tb);
		} catch(IOException e){
			JOptionPane.showMessageDialog(this, "Impossibile Connettersi al Server!");
			System.exit(0);
		}
	}
	
}

