package compiere.model.payments.processes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import sun.net.TelnetInputStream;
import sun.net.ftp.FtpClient;

public class XX_Test_FTP {

	public static int BUFFER_SIZE = 10240;

	private FtpClient m_client;
	
	// set the values for your server
	//private String host = "192.168.1.31";//6
	
	//private String user = "USRBAN";
	
	//private String password = "USRBAN1";
	
	//private String sDir = "C:";
	
	private String m_sLocalFile = "";
	
	private String m_sHostFile = "/Interfaces/";
	
	public XX_Test_FTP(String ruta, String nombreArchivo, String host, String user, String password) {
	    try {
	    	m_sLocalFile = ruta+nombreArchivo;
	    	m_sHostFile = m_sHostFile + nombreArchivo;
			m_client = new FtpClient(host);
			m_client.login(user, password);
			m_client.binary();
			putFile();
	    }catch (Exception ex) {
	    	System.out.println("Error: " + ex.toString());
	    }
	  }

	  protected void disconnect() {
		  if (m_client != null) {
			  try {
		    	  m_client.closeServer();
		      }catch (IOException ex) {
		      }
		      m_client = null;
		  }
	  }

	  public static int getFileSize(FtpClient client, String fileName)throws IOException {
		  TelnetInputStream lst = client.list();
		  String str = "";
		  fileName = fileName.toLowerCase();
		  while (true) {
			  int c = lst.read();
			  char ch = (char) c;
			  if (c < 0 || ch == '\n') {
				  str = str.toLowerCase();
				  if (str.indexOf(fileName) >= 0) {
					  StringTokenizer tk = new StringTokenizer(str);
					  int index = 0;
					  while (tk.hasMoreTokens()) {
						  String token = tk.nextToken();
						  if (index == 4)
							  try {
								  return Integer.parseInt(token);
							  }catch (NumberFormatException ex) {
							   	  return -1;
							  }
						  index++;
					  }
				  }
				  str = "";
			  }
			  if (c <= 0)
				  break;
			  str += ch;	
		 }
		 return -1;
	  }

	  protected void getFile() {
		  if (m_sLocalFile.length() == 0) {
			  m_sLocalFile = m_sHostFile;
		  }
		  byte[] buffer = new byte[BUFFER_SIZE];
		  try {
			  int size = getFileSize(m_client, m_sHostFile);
			  if (size > 0) {
				  System.out.println("File " + m_sHostFile + ": " + size
						  + " bytes");
				  System.out.println(size);
			  }else
				  System.out.println("File " + m_sHostFile + ": size unknown");
			  FileOutputStream out = new FileOutputStream(m_sLocalFile);
			  InputStream in = m_client.get(m_sHostFile);
			  int counter = 0;
			  while (true) {
				  int bytes = in.read(buffer);
				  if (bytes < 0)
					  break;

				  out.write(buffer, 0, bytes);
				  counter += bytes;
			  }
			  out.close();
			  in.close();
		  }catch (Exception ex) {
			  System.out.println("Error: " + ex.toString());
		  }
	  }

	  private void putFile() {
		  if (m_sLocalFile.length() == 0) {
			  System.out.println("Please enter file name");
		  }
		  byte[] buffer = new byte[BUFFER_SIZE];
		  try {
			  File f = new File(m_sLocalFile);
			  int size = (int) f.length();
			  System.out.println("File " + m_sLocalFile + ": " + size + " bytes");
			  System.out.println(size);
			  FileInputStream in = new FileInputStream(m_sLocalFile);
	      	  OutputStream out = m_client.put(m_sHostFile);

	      	  int counter = 0;
	      	  while (true) {
	      		  int bytes = in.read(buffer);
	      		  if (bytes < 0)
	      			  break;
	      		  out.write(buffer, 0, bytes);
	      		  counter += bytes;
	      		  System.out.println(counter);
	      	  }

	      	  out.close();
	      	  in.close();
		  }catch (Exception ex) {
			  System.out.println("Error: " + ex.toString());
		  }
	  }
}
