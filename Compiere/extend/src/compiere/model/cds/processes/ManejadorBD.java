package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JTextArea;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

import com.csvreader.CsvReader;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import compiere.model.cds.X_XX_VMR_Prld01;

/**
 * =============================================================================
 * Proyecto   : Presupuesto
 * Paquete    : com.beco.merchandising.presupuesto
 * Programa   : ManejadorBD.java
 * Creado por : mmiyazono
 * Creado el  : 29/11/2008 - 02:18:51 AM
 *
 * (c) CENTROBECO, C.A.
 * -----------------------------------------------------------------------------
 * 				Actualizaciones
 * -----------------------------------------------------------------------------
 * Versión     : 
 * Fecha       : 
 * Analista    : 
 * Descripción : 
 * =============================================================================
 */


public class ManejadorBD {

	private Connection conexion;
	
	private int ad_client;
	private Ctx ctx;
	private Trx trx;
	
	Vector<String> categoryCodes = new Vector<String>();
	Vector<Integer> categoryIDs = new Vector<Integer>();
	Vector<Integer> departmentIDs = new Vector<Integer>();
	Vector<String> departmentCodes = new Vector<String>();
	Vector<Integer> lineIDs = new Vector<Integer>();
	Vector<Integer> lineDepIDs = new Vector<Integer>();
	Vector<String> lineCodes = new Vector<String>();
	Vector<Integer> sectionIDs = new Vector<Integer>();
	Vector<Integer> sectionLinIDs = new Vector<Integer>();
	Vector<String> sectionCodes = new Vector<String>();
	Vector<String> storeCodes = new Vector<String>();
	Vector<Integer> storeIDs = new Vector<Integer>();
	
 //Desarrollo
	public final String USER = "irojas";
	public final String PASSWORD = "irojas2";
	public final String IP = "192.168.1.10";
	
	 //Produccion
	/*	public final String USER = "icttfr";
		public final String PASSWORD = "icttfr";
		public final String IP = "192.168.1.2";*/

	public final String URL = "jdbc:as400://".concat(IP);
	Utilitarios utilitarios = new Utilitarios();

	/**
	 * Método conectar. Realiza la conexion a la base de Datos.
	 * 
	 */
	public void conectar() {
		try {
			// Creamos la nueva conexion
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			Properties p = new Properties();
			p.put("user", USER);
			p.put("password", PASSWORD);
			p.put("translate binary", "true");
			conexion = DriverManager.getConnection(URL, p);
		} catch (Exception e) {
			e.printStackTrace();
			conexion = null;
		}
	}

	/**
	 * Método desconectar. Realiza la desconexion de la base de Datos.
	 * 
	 */
	private void desconectar() {
		try {
			conexion.close();
		} catch (Exception e) {
			e.printStackTrace();
			// No existen conexiones abiertas
		}
	}
 
	/**
	 * Método crearSentencia
	 * @author mmiyazono
	 * @return Statement
	 */
	private Statement crearSentencia ()  {
		Statement s = null;
		try {
			s = conexion.createStatement();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/* ES NECESARIO Activar el diario PARA QUE SE PUEDA UTILIZAR EL BATCH
CRTJRNRCV JRNRCV(LIBRAMMS/DIAINTM07) TEXT('diario int07') 
CRTJRN JRN(LIBRAMMS/DIARIO7) JRNRCV(LIBRAMMS/DIAINTM07)
STRJRNPF FILE(LIBRAMMS/INTM07) JRN(LIBRAMMS/DIARIO7)
	 */
	/**
	 * Método ejecutarLoteSentencias
	 * @author mmiyazono
	 * @param loteSentencias
	 * @return int[]
	 */
	private int[] realizarLoteSentencias(Statement loteSentencias)  {
		int[] filasAfectadas = null;
		try {
			conexion.setAutoCommit(false);
			conexion.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			filasAfectadas = loteSentencias.executeBatch();
			conexion.commit();
			conexion.setAutoCommit(true);
		}catch (SQLException exSQL) {
			exSQL.printStackTrace();
			try {
					conexion.rollback();
					conexion.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
 		}catch (Exception e) {
			e.printStackTrace();
		}

 		return filasAfectadas;
	}

	/**
	 * Método realizarSentencia. Realiza las sentencias de insersion y
	 * actualizacion en la base de datos
	 * 
	 * @param String
	 *            sentenciaSql de update, insert o delete
	 * @return int negativo => error, positivo o cero => numero de lineas afectadas 
	 */
	private int realizarSentencia(String sentenciaSQL) throws SQLException {
		// Realizamos la operacion
		Statement sentencia = null;
		int estado = -1;
		//System.out.println(sentenciaSQL);
		try {
			sentencia = conexion.createStatement();
			estado = sentencia.executeUpdate(sentenciaSQL);
			sentencia.close();
		} catch (Exception e) {
			e.printStackTrace();
			desconectar();
		}

		return estado;
	}


	/**
	 * Método realizarConsulta. Ejecuta una sentencia SQL de consulta de
	 * registros (Selects) con la capacidad de desplazamiento y actualización o
	 * no de los datos resultantes.
	 * 
	 * @param sentenciaSQL
	 *            String sentencia sql de consulta
	 * @return ResultSet
	 */
	private ResultSet realizarConsulta(String sentenciaSQL) {
		ResultSet resultado = null;
		Statement sentencia = null;
		//System.out.println(sentenciaSQL);
		try {
			sentencia = conexion.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			resultado = sentencia.executeQuery(sentenciaSQL);
		} catch (SQLException e) {
			e.printStackTrace();
			desconectar();
		} catch (Exception e1) {
			e1.printStackTrace();
			desconectar();
		}

		return resultado;
	}

	public void realizarComandoAS400( String comando) {

		AS400 as = new AS400(IP, USER, PASSWORD);
		CommandCall command = new CommandCall(as);
		//comando = "CPYF FROMFILE(BECOFILE/INTM02) TOFILE(BECOFILE/INTM02BK) MBROPT(*ADD) CRTFILE(*YES) FMTOPT(*NOCHK)";
		/**
		 * ** obtenido de
		 * http://www.itjungle.com/mpo/mpo060602-KevinB-CommandCallExample.txt
		 * ****************
		 */
		try {
			// Run the command.
			if (command.run(comando))
				System.out.println("Command successful");
			else
				System.out.println("Command failed");

			// If messages were produced from the command, print them
			AS400Message[] messagelist = command.getMessageList();

			if (messagelist.length > 0) {
				System.out.println(", messages from the command:");
				System.out.println(" ");
			}

			for (int i = 0; i < messagelist.length; i++) {
				System.out.println(messagelist[i].getID());
				System.out.println(": ");
				System.out.println(messagelist[i].getText());
			}
		} catch (Exception e) {
			System.out.println("Command " + command.getCommand()
					+ " did not run");
		}
	}
	
	/**
	 * Método montarPresupuestoDeArchivo
	 *  TITULO DE LAS COLUMNAS
	 *  CODTIE (codigo de tienda)
	 *  CODDEP (codigo de departamento)
	 *  CODLIN (codigo de linea) si no se distribuye por linea que el valor se encuentre en 99
	 *  CODSEC (codigo de seccion) si no se distribuye por seccion que el valor se encuentre en 99
	 *  AÑO (año)
	 *  MES (mes)
	 *  MONVENPRE (monto ventas presupuestadas)
	 *  MOINVINPR (monto inventario inicial presupuestadas)
	 *  MOINVFIPR (monto inventario final presupuestadas)
	 *  MONCOMPRE (monto compras presupuestadas)
	 *  MONREBAJAS (monto rebajas presupuestadas)
	 *  PORCOBPRE (porcentaje cobertura)
	 *  PORROTPRE (porcentaje rotacion)
	 *  POMARBGPR (porcentaje margen bruto ganado presupuestado)
	 *  POMARSCPR (porcentaje margen segun compras presupuestado)
	 *  CANVENPRE (cantidad ventas presupuestadas)
	 *  CAINVINPR (cantidad inventario inicial presupuestadas)
	 *  CAINVFIPR (cantidad inventario final presupuestadas)
	 *  CANCOMPRE (cantidad compras presupuestadas)
	 *  
	 * @param archivo
	 *            String archivo en formato CSV a cargar
	 * @return int lineas leidas
	 */
	public int montarPresupuestoDeArchivo(int fechaInicio, int fechaFin, int cliente, Ctx contexto, Trx transaccion) {
		ctx = contexto;
		trx = transaccion;
		ad_client = cliente;
		int i = 1;
		int j = 1;
		BigDecimal POREBPRPRTEMP = new BigDecimal(0);
		BigDecimal POREBPRPR = new BigDecimal(0);
		BigDecimal POREBFRPRTEMP = new BigDecimal(0);
		BigDecimal POREBFRPR = new BigDecimal(0);
		BigDecimal POREBDFPRTEMP = new BigDecimal(0);
		BigDecimal POREBDFPR = new BigDecimal(0);
		BigDecimal PORCOBPRE = new BigDecimal(0);
		/*double PORCOBPRETEMP = 0;
		double INVPROM = 0;
		double PORROTPRETEMP = 0;*/
		BigDecimal PORROTPRE = new BigDecimal(0);
		BigDecimal POMARNGPR = new BigDecimal(0);
		BigDecimal POMARBGPR = new BigDecimal(0);
		BigDecimal POMARSCPR = new BigDecimal(0);
		BigDecimal POMARPGPR = new BigDecimal(0);
		String coddepTemp = "0";
		int codcat = 0;
		int linea = 1;
		String sentenciaUpdateInsert ="";
		
		try {
			conectar();
			
			//*** OJO SE DEBE COMENTAR CUANDO SE QUIERE CORRER PRESUPUESTO DE UN SOLO DPTO
			//** xcomnetado por irojas para modificar el presupuesto de un solo dpto, caso dpto30
			//realizarSentencia("DELETE FROM BECOFILE.PRLD01 WHERE AÑOMESPRE BETWEEN " + fechaInicio + " AND " + fechaFin);

			Statement loteSentencias = crearSentencia();

			/* Archivo de Entrada */
			CsvReader reader = new CsvReader("Presupuesto.csv", ';');
			reader.readHeaders();
			System.out.println("Leyendo el archivo... \n");
			while (reader.readRecord()) {
				linea++;
				String codtie = reader.get("CODTIE");
				String coddep = reader.get("CODDEP");
				String codlin = reader.get("CODLIN");
				String codsec = reader.get("CODSEC");
				String año = reader.get("AÑO");
				String mes = reader.get("MES");
				int añomespre = Integer.parseInt(año) * 100 + Integer.parseInt(mes);
				// Ventas Presupuestadas
				BigDecimal monvenpre = new BigDecimal(reader.get("MONVENPRE"));
				BigDecimal moinvinpr = new BigDecimal(reader.get("MOINVINPR"));
				BigDecimal moinvfipr = new BigDecimal(reader.get("MOINVFIPR"));
				// ***  double porrebpr = (new Double(reader.get("PORREBAJAS"))).doubleValue();
				BigDecimal morebpr = new BigDecimal(reader.get("MONREBAJAS"));
				BigDecimal morebprpr = morebpr.multiply(new BigDecimal(0.5));
				BigDecimal morebfrpr = morebpr.multiply(new BigDecimal(0.5));
				BigDecimal morebdfpr = new BigDecimal(0);
				BigDecimal momermpre = monvenpre.multiply(new BigDecimal(0.2));//(new Double(reader.get("MOMERMPRE"))).doubleValue();
				PORCOBPRE = new BigDecimal(reader.get("PORCOBPRE"));
				PORROTPRE = new BigDecimal(reader.get("PORROTPRE"));
				POMARBGPR = new BigDecimal(reader.get("POMARBGPR")).multiply(new BigDecimal(100));
				POMARSCPR = new BigDecimal(reader.get("PORMARSCPR")).multiply(new BigDecimal(100));
				String moncompre = reader.get("MONCOMPRE");
				// Piezas Presupuestadas
				String canvenpre = reader.get("CANVENPRE");
				String cancompre = reader.get("CANCOMPRE");
				String cainvinpr = reader.get("CAINVINPR");
				String cainvfipr = reader.get("CAINVFIPR");
				
				//Codigo de Tienda
				int store = getStoreID(codtie);
				if(store==0)
					continue;
				
				//Codigo de Departamento
				int dep = getDepartmentID(coddep);
				if(dep==0)
					continue;
					
				//Codigo de Categoria
				int category = getCategoryID(dep);	
				if(category==0)
					continue;
				
				//Codigo de Linea
				int line = getLineID(codlin,dep);
				if(line==0)
					continue;
				
				//Codigo de Seccion
				int section = getSectionID(codsec, line);
				if(section==0)
					continue;
				
			    //Se genera el registro
			    X_XX_VMR_Prld01 prld01 = new X_XX_VMR_Prld01(ctx,0,null);
			    	
			    prld01.setXX_INVEFECBUDGETEDAMOUNT(moinvinpr);
			   	prld01.setXX_INVAMOUNTORIGBUDGETED(new BigDecimal(cainvinpr)); 		   	
				prld01.setXX_PURCHAMOUNTBUDGETED(new BigDecimal(moncompre));  
				prld01.setXX_QUANTBUDGETEDSHOPPING(new BigDecimal(cancompre));  
				prld01.setXX_SALESAMOUNTBUD2(momermpre); 
				prld01.setXX_SALESAMOUNTBUD(new BigDecimal(canvenpre)); 
				prld01.setXX_PROMSALEAMOUNTBUD(new BigDecimal(canvenpre)); 					
				//prld01.setXX_PERTSALEFRINTERESTS(rs.getBigDecimal("POREBPRRE"));  //OJO
				prld01.setXX_AMOUNTSALEFRBUD(morebfrpr); 
				prld01.setXX_FINALINVAMOUNTBUD2(morebfrpr); 
				prld01.setXX_FINALINVAMOUNTBUD(new BigDecimal(cainvfipr)); 
				prld01.setXX_ROTATIONBUD(PORROTPRE); 
				//prld01.setXX_PECTSALEPROMINTERESTS "+rs.getBigDecimal("PORCOBPRE"));  //OJO	
				prld01.setXX_PERCNBUDCOVERAGE(PORCOBPRE); 		//OJO
				prld01.setXX_MARGACCORDINGBUDPURCH(POMARSCPR); 
				prld01.setXX_LISCKGROSSMARGPERCTBUD(POMARBGPR); 
				prld01.setXX_BUDDDECLINE(momermpre); 
				
				String canrebajas = "0"; //reader.get("CANREBAJAS");
				BigDecimal carebpr = new BigDecimal(canrebajas);
				BigDecimal carebprpr = carebpr.multiply(new BigDecimal(0.5));
				BigDecimal carebfrpr = carebpr.multiply(new BigDecimal(0.5));;
				BigDecimal carebdfpr = new BigDecimal(0);
				prld01.setXX_PROMSALENUMBUD(carebprpr); 
				
				prld01.setXX_PERCENTSQALEFINALBUD(new BigDecimal(0));
				prld01.setXX_PORTSALEFRBUD(new BigDecimal(50)); 
				prld01.setXX_PECTSALEPROMBUD(new BigDecimal(50)); 
				
				prld01.setXX_BUDAMOUNTFRSALE(new BigDecimal(0)); 
				prld01.setXX_FINALBUDAMOUNTSALE(new BigDecimal(0)); 
				
				prld01.setXX_BYWINMARGPERTBUD(POMARBGPR);
				prld01.setXX_NETMARGPERTCATTLEBUD(POMARBGPR.min(POMARBGPR)); 
				
/**				prld01.setXX_INIAMOUNTINVECOST(rs.getBigDecimal("MOINVINCO")); 
				prld01.setXX_AMOUNTINIINVEREAL(rs.getBigDecimal("MOINVINRE"));  
				prld01.setXX_NUMINIINVEREAL(rs.getBigDecimal("CAINVINRE")); 
				prld01.setXX_AMOUNTPLACEDNACPURCHCOST(rs.getBigDecimal("MOCOMNCCO"));  
				prld01.setXX_AMOUNTPLACEDNACCAMPRA(rs.getBigDecimal("MOCOMNCOL"));  
				prld01.setXX_NUMNACSHOPPINGPLACED(rs.getBigDecimal("CACOMNCOL")); 
				prld01.setXX_NACPURCHAMOUNTRECEIVED(rs.getBigDecimal("MOCOMNREC"));  
				prld01.setXX_QUANTPURCHNAC(rs.getBigDecimal("CACOMNREC")); 
				prld01.setXX_PURCHAMOUNTPLACEDCOSTIMP(rs.getBigDecimal("MOCOMICCO")); 
				prld01.setXX_PURCHAMOUNTPLACEDIMPD(rs.getBigDecimal("MOCOMICOL")); 
				prld01.setXX_PURCHQUANTIMPDPLACED(rs.getBigDecimal("CACOMICOL")); 
				prld01.setXX_PURCHAMOUNTREVIMPD(rs.getBigDecimal("MOCOMIREC")); 
				prld01.setXX_QUANTPURCHAMOUNTSREV(rs.getBigDecimal("CACOMIREC")); 
				prld01.setXX_PURCHAMOUNTPLADPASTMONTHS(rs.getBigDecimal("MOCOMCOMA"));
				prld01.setXX_PURCHQUANTPLACEDMONTHS(rs.getBigDecimal("CACOMCOMA")); 
				prld01.setXX_PURCHAMOUNTREDPASTMONTHS(rs.getBigDecimal("MOCOMREMA")); 
				prld01.setXX_NUMMONTHSREDSHOP(rs.getBigDecimal("CACOMREMA")); 
				prld01.setXX_AMOUNTSALECOST(rs.getBigDecimal("MONVENCOS")); 
				prld01.setXX_AMOUNTACTUALSALE(rs.getBigDecimal("MONVENREA")); 
				prld01.setXX_QUANTACTUALSALE(rs.getBigDecimal("CANVENREA"));  		
				prld01.setXX_ACTAMOUNTSALEPROM(rs.getBigDecimal("MOREBPRRE")); 
				prld01.setXX_AMOUNTSALEPROMINTERESTS(rs.getBigDecimal("CAREBPRRE"));  
				prld01.setXX_PECTSALEPROMINTERESTS(rs.getBigDecimal("POREBPRRE"));  //OJO
				prld01.setXX_PERTSALEFRINTERESTS(rs.getBigDecimal("POREBFRRE"));  //OJO
				prld01.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("MOREBFRRE")); 
				prld01.setXX_AMOUNTSALEFRINTERESTS(rs.getBigDecimal("CAREBFRRE")); 
				prld01.setXX_FINALSALEAMOUNTBUD(rs.getBigDecimal("MOREBDFPR")); 
				prld01.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("MOREBDFRE")); 
				prld01.setXX_FINALSALEAMOUNTINTERESTS(rs.getBigDecimal("CAREBDFRE")); 
				prld01.setXX_PERCENTACTFINALSALE(rs.getBigDecimal("POREBDFRE")); 
				prld01.setXX_TRANSFAMOUNTSENT(rs.getBigDecimal("MONTRAENV")); 				
				prld01.setXX_NUMTRANSFSENT(rs.getBigDecimal("CANTRAENV")); 
				prld01.setXX_TRANSFAMOUNTRECEIVED(rs.getBigDecimal("MONTRAREC")); 
				prld01.setXX_NUMBTRANSFREV(rs.getBigDecimal("CANTRAREC")); 
				prld01.setXX_INVAMOUNTFINALREAL(rs.getBigDecimal("MOINVFIRE")); 
				prld01.setXX_NUMREALFINALINV(rs.getBigDecimal("CAINVFIRE")); 
				prld01.setXX_FINALINVAMOUNTPROJD(rs.getBigDecimal("MOINVFIPY")); 
				prld01.setXX_NUMPROJDFINALINV(rs.getBigDecimal("CAINVFIPY")); 
				prld01.setXX_AMOUNTPURCHASELIMIT(rs.getBigDecimal("MONLIMCOM")); 
				prld01.setXX_QUANTITYPURCHLIMIT(rs.getBigDecimal("CANLIMCOM")); 
				prld01.setXX_ROTATIONREAL(rs.getBigDecimal("PORROTREA")); 
				prld01.setXX_REALPERCCOVERAGE(rs.getBigDecimal("PORCOBREA")); 
				prld01.setXX_MARGACCORDINGBUYREAL(rs.getBigDecimal("POMARSCRE")); 
				prld01.setXX_LISCKGROSSMARGPERTREAL(rs.getBigDecimal("POMARBGRE")); 
				prld01.setXX_NETMARGPERTROYALLIVESTOCK(rs.getBigDecimal("POMARNGRE"));				 
				prld01.setXX_PERTWINGMARGREAL(rs.getBigDecimal("POMARPGRE")); 
				prld01.setXX_REALDECLINE(rs.getBigDecimal("MOMERMREA")); 	
	*/			
				prld01.setM_Warehouse_ID(store);
				prld01.setXX_VMR_Category_ID(category);
				prld01.setXX_VMR_Department_ID(dep);		
				prld01.setXX_VMR_Line_ID(line);
				prld01.setXX_VMR_Section_ID(section);
				prld01.setXX_BUDGETYEARMONTH(añomespre);
					
			    prld01.save();	
			}
				
				
				String canrebajas = "0"; //reader.get("CANREBAJAS");
				double carebpr = (new Double(canrebajas)).doubleValue();
				double carebprpr = carebpr * 0.5;
				double carebfrpr = carebpr * 0.5;
				double carebdfpr = 0;
				

				
				// COBERTURA
				/*if (monvenpre != 0)
					PORCOBPRE = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;*/
				
				// ROTACION
			/*	INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;*/
				
				// MARGEN BRUTO GANADO
				/*if (monvenpre != 0)
					POMARBGPR = (MARBGPR / monvenpre) * 100;*/
				
				// MARGEN NETO
//				POMARNGPR = POMARBGPR - 2;
				
				// MARGEN SEGUN COMPRAS no tenemos MBGanado por linea
				/*if (monvenpre != 0)
					POMARSCPR = ((MARBGPR + morebpr) / monvenpre) * 100;
				*/
				
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
//				POMARPGPR = POMARSCPR;
				 
				
/*
				String sentenciaUpdateInsert =  "UPDATE BECOFILE.PRLD01 SET (MONVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBFRPR," +
							" MOREBDFPR, MONCOMPRE, CANVENPRE, CAINVINPR, CAINVFIPR, CAREBPRPR, CAREBFRPR, CAREBDFPR, CANCOMPRE, " +
							" POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR, POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE) = ("
							+ monvenpre + "," + moinvinpr + "," + moinvfipr + "," + morebprpr + "," + morebfrpr + "," + morebdfpr
							+ "," + moncompre + "," + canvenpre + "," + cainvinpr + "," + cainvfipr + "," + carebprpr + ","
							+ carebfrpr + "," + carebdfpr + "," + cancompre + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", "
							+ PORROTPRE + ", " + PORCOBPRE + " , " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", "
							+ POMARPGPR  + ", "	+ momermpre + ") "
							+ " WHERE CODTIE = " + codtie + " AND CODCAT <> 99 AND CODDEP = " + coddep + " AND CODLIN = " + codlin
							+ " AND CODSEC = " + codsec + " AND AÑOMESPRE = " + añomespre;
			//	System.out.println("Se está actualizando la linea  " + i + " codtie: " + codtie +  " coddep: " + coddep + " codlin: " + codlin + " codsec: " + codsec + " añomespre:  " + añomespre );

				if (estado.) {*/
/**						if(!coddep.equalsIgnoreCase(coddepTemp)){
							ResultSet resultado = realizarConsulta("SELECT CODCAT FROM BECOFILE.TABM12 WHERE CODDEP = " + coddep);
								if (resultado.first()) {
									codcat = resultado.getInt("CODCAT");
								}
								coddepTemp = coddep;
						}
							 sentenciaUpdateInsert = "INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, " +
										"AÑOMESPRE, MONVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBFRPR, MOREBDFPR, MONCOMPRE, "
										+ "CANVENPRE, CAINVINPR, CAINVFIPR, CAREBPRPR, CAREBFRPR, CAREBDFPR, CANCOMPRE,"  
										+ " POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR, POMARNGPR, "
										+ "POMARSCPR, POMARPGPR, MOMERMPRE) VALUES ("
										+ codtie + "," + codcat + "," + coddep + "," + codlin + ", " + codsec + " , " + añomespre + ","
										+ utilitarios.redondear(monvenpre,10,2) + "," + utilitarios.redondear(moinvinpr,10,2) + "," + utilitarios.redondear(moinvfipr,10,2) + "," + utilitarios.redondear(morebprpr,10,2) + "," + utilitarios.redondear(morebfrpr,10,2)
										+ "," + utilitarios.redondear(morebdfpr,10,2) + "," + utilitarios.redondear(new Double(moncompre).doubleValue(),10,2) + "," + canvenpre + "," + cainvinpr + "," + cainvfipr
										+ "," + carebprpr + "," + carebfrpr + "," + carebdfpr + "," + cancompre + ", " 
										+ utilitarios.redondear(POREBPRPR,10,2) + ", " + utilitarios.redondear(POREBDFPR,10,2) + ", " + utilitarios.redondear(POREBFRPR,10,2) + ", "
										+ utilitarios.redondear(PORROTPRE,10,2) + ", " + utilitarios.redondear(PORCOBPRE,10,2) + " , " + utilitarios.redondear(POMARBGPR,10,2) + ", " + utilitarios.redondear(POMARNGPR,10,2) + ", " + utilitarios.redondear(POMARSCPR,10,2) + ", "
										+ utilitarios.redondear(POMARPGPR,10,2) + ", "	+ utilitarios.redondear(momermpre,10,2) + ")";
								//System.out.println("Se está insertando la linea  " + i + " codtie: " + codtie + " codcat: " + codcat + " coddep: " + coddep + " codlin: " + codlin + " codsec: " + codsec + " añomespre:  " + añomespre );
							//System.out.println("Sentencia: " + sentenciaUpdateInsert);
							j++;
							loteSentencias.addBatch(sentenciaUpdateInsert);	
				}
				i = realizarLoteSentencias(loteSentencias).length;
				desconectar();
				//i++;			
			//}
			reader.close();
*/		} catch (Exception e) {
			System.out.println("Error en linea " + linea);
			e.printStackTrace();
			desconectar();
			return -1;
		} catch (Throwable e1) {
			System.out.println("ERROR EN SENTENCIA: " + sentenciaUpdateInsert);
			e1.printStackTrace();
			desconectar();
			return -1;
		}
		System.out.println("Se realizaron " + j + " lecturas ");
		return i;

	}
	
	private void getAllStores(){
		
		String sql = "SELECT VALUE, M_WAREHOUSE_ID " +
				     "FROM M_WAREHOUSE " +
				     "WHERE UPPER(VALUE) = LOWER(VALUE) " +
				     "AND AD_CLIENT_ID = " + ad_client;
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				storeCodes.add(rs.getString("VALUE"));
				storeIDs.add(rs.getInt("M_WAREHOUSE_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}	
	}

	private void getAllCategorys(){
		
		String sql = "SELECT VALUE, XX_VMR_CATEGORY_ID " +
				     "FROM XX_VMR_CATEGORY WHERE ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID = " + ad_client;

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				categoryCodes.add(rs.getString("VALUE"));
				categoryIDs.add(rs.getInt("XX_VMR_CATEGORY_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}	
	}
	
	private void getAllDeparments(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID " +
				     "FROM XX_VMR_DEPARTMENT WHERE ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID = " + ad_client;

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				departmentCodes.add(rs.getString("VALUE"));
				departmentIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}	
	}
	
	private void getAllLines(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
				     "FROM XX_VMR_LINE WHERE " +
				     //"ISACTIVE='Y' AND " + 
				     "AD_CLIENT_ID = " + ad_client;

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				lineCodes.add(rs.getString("VALUE"));
				lineIDs.add(rs.getInt("XX_VMR_LINE_ID"));
				lineDepIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}	
	}
	
	private void getAllSections(){
		
		String sql = "SELECT XX_VMR_LINE_ID, VALUE, XX_VMR_SECTION_ID " +
				     "FROM XX_VMR_SECTION WHERE ISACTIVE='Y' " + 
				     "AND AD_CLIENT_ID = " + ad_client;

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				sectionCodes.add(rs.getString("VALUE"));
				sectionIDs.add(rs.getInt("XX_VMR_SECTION_ID"));
				sectionLinIDs.add(rs.getInt("XX_VMR_LINE_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}	
	}
	
	private int getStoreID(String cellValue){
		
		int index=-1;  // OJO CON ESTO (ACTUALMENTE LAS TIENDAS TIENEN 3 DIGITOS Ejm: 002)
		if(cellValue.length()==1){
			cellValue = "00" + cellValue;
		}else if (cellValue.length()==2) {
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<storeCodes.size(); i++){
			
			if(storeCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return storeIDs.get(index);
		else
			return 0;
	}
	
	private int getCategoryID(int departamento_id){
		
		String sql = "Select value from XX_VMR_Category where XX_VMR_Category_ID = (Select XX_VMR_Category_ID from XX_VMR_Department where XX_VMR_Department_ID=" + departamento_id + ")";
	
		PreparedStatement prst = DB.prepareStatement(sql,null);
		String cellValue = "";
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				cellValue = rs.getString("value");
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			e.printStackTrace();
		}	
			
			int index=-1;
			if(cellValue.length()==1){
				cellValue = "0" + cellValue;
			}
			
			for(int i=0; i<categoryCodes.size(); i++){
				
				if(categoryCodes.get(i).equals(cellValue)){
					index=i;
					break;
				}
			}
			
			if(index!=-1)
				return categoryIDs.get(index);
			else
				return 0;
		}
	
	private int getDepartmentID(String cellValue){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<departmentCodes.size(); i++){
			
			if(departmentCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return departmentIDs.get(index);
		else
			return 0;
	}
	
	private int getLineID(String cellValue, int depID){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<lineCodes.size(); i++){
			
			if(lineCodes.get(i).equals(cellValue) && lineDepIDs.get(i)==depID){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return lineIDs.get(index);
		else
			return 0;
	}
	
	private int getSectionID(String cellValue, int lineID){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}

		for(int i=0; i<sectionCodes.size(); i++){
			
			if(sectionCodes.get(i).equals(cellValue) && sectionLinIDs.get(i)==lineID){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return sectionIDs.get(index);
		else
			return 0;
	}

	/**
	 * Método consolidadosXDepartamento 
	 * Se registran los consolidados mensuales por departamento en un rango de fecha. (añomes inicio y añomes fin)
	 * @param añoMesIni
	 * @param añoMesFin
	 * @return int estado de resultante, > 0 indica bueno
	 */
	public int consolidadosXDepartamento(int añoMesIni, int añoMesFin) {
		int estado = -2;
		ResultSet resultado = null;
		double REBTOT = 0;
		double POREBPRPRTEMP = 0;
		double POREBPRPR = 0;
		double POREBFRPRTEMP = 0;
		double POREBFRPR = 0;
		double POREBDFPRTEMP = 0;
		double POREBDFPR = 0;
		double PORCOBPRE = 0;
		double PORCOBPRETEMP = 0;
		double INVPROM = 0;
		double PORROTPRETEMP = 0;
		double PORROTPRE = 0;
		double POMARNGPR = 0;
		double POMARBGPR = 0;
		double POMARSCPR = 0;
		double POMARPGPR = 0;
		double monvenpre = 0;
		double moncompre = 0;
		double canvenpre = 0;
		double moinvinpr = 0;
		double moinvfipr = 0;
		double morebprpr = 0;
		double morebdfpr = 0;
		double morebfrpr = 0;
		double mbganado = 0;
		double momermpre = 0;
		double cancompre = 0;
		double cainvinpr = 0;
		double cainvfipr = 0;
		double carebfrpr = 0;
		double carebprpr = 0;
		double carebdfpr = 0;
		int codCat = 0;
		int codDep = 0;
		int añoMesPre = 0;
		int inserts = 0;
		int updates = 0;
		
		// Nos conectamos a la BD
		conectar();

		try {			
 			System.out.println("// POR DEPARTAMENTO 999,CAT,DPTO,99,99");
			// POR DEPARTAMENTO 999,CAT,DPTO,99,99
			resultado = realizarConsulta("SELECT CODCAT, CODDEP, AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR,"
					+ " SUM(MOINVFIPR) MOINVFIPR, SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO, "
					+ "SUM(MOMERMPRE) MOMERMPRE, SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR FROM BECOFILE.PRLD01 "
					+ "WHERE CODTIE <> 999 AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN = 99 AND CODSEC = 99 "
					+ "AND AÑOMESPRE BETWEEN " + añoMesIni + " AND " + añoMesFin
					+ " GROUP BY CODCAT, CODDEP, AÑOMESPRE ORDER BY AÑOMESPRE, CODCAT, CODDEP");
			resultado.beforeFirst();
			while (resultado.next()) {
				codCat = resultado.getInt("CODCAT");
				codDep = resultado.getInt("CODDEP");
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				momermpre = resultado.getDouble("MOMERMPRE");
				canvenpre = resultado.getDouble("CANVENPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;
				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr
						+ ", MOREBPRPR = " + morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr 
						+ ", POREBPRPR = " + POREBPRPR + ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR 
						+ ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = " + PORROTPRE + ", " + "POMARBGPR = " + POMARBGPR 
						+ ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR + ", POMARPGPR = " + POMARPGPR 
						+ ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = " + cainvinpr + ", CAINVFIPR = "
						+ cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr + ", CAREBDFPR = " + carebdfpr
						+ " WHERE CODTIE = 999 AND CODCAT = " + codCat + " AND CODDEP = " + codDep + " AND CODLIN = 99 AND "
						+ "CODSEC = 99 AND AÑOMESPRE = " + añoMesPre);
					updates = updates + estado;
				if (estado < 1) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE,"
							+ " CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, "
							+ "POMARBGPR, POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) "
							+ "VALUES(999, " + codCat + ", " + codDep + ", 99, 99, " + añoMesPre + ", " + monvenpre + ", "
							+ moncompre + ", " + canvenpre + ", " + moinvinpr + ", " + moinvfipr + ", " + morebprpr + ", "
							+ morebdfpr + ", " + morebfrpr + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", "
							+ PORROTPRE + ", " + PORCOBPRE + " , " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", "
							+ POMARPGPR + ", " + momermpre + ", " + cancompre + ", " + cainvinpr + ", " + cainvfipr + ", "
							+ carebfrpr + ", " + carebprpr + ", " + carebdfpr + ")");
				}
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE =  " + moncompre
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
						+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBPRPR = " + POREBPRPR
						+ ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
						+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
						+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
						+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
						+ ", CAREBDFPR = " + carebdfpr
						+ " WHERE P1.CODTIE = 999 AND P1.CODCAT = 99 AND P1.CODDEP = " + codDep 
						+ " AND P1.CODLIN = 99 AND P1.CODSEC = 99 AND P1.AÑOMESPRE = " + añoMesPre);
				if (estado < 1) {
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, "
							+ "CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR,"
							+ " POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) "
							+ "VALUES(999, 99, " + codDep + ", 99, 99, " + añoMesPre + ", " + monvenpre + ", " + moncompre + ", "
							+ canvenpre + ", " + moinvinpr + ", " + moinvfipr + ", " + morebprpr + ", " + morebdfpr + ", "
							+ morebfrpr + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", " + PORROTPRE + ", "
							+ PORCOBPRE + ", " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + ", "
							+ momermpre + ", " + cancompre + ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr
							+ ", " + carebprpr + ", " + carebdfpr + ")");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			desconectar();
			return -1;
		}
		System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
		// Cerramos la conexion de la base de datos
		desconectar();

		return estado;

		}

	/**
	 * 
	 * @param añoMesIni
	 * @param añoMesFin
	 * @return
	 */
		public int consolidadosXLinea(int añoMesIni, int añoMesFin) {
			int estado = -2;
			ResultSet resultado = null;
			double REBTOT = 0;
			double POREBPRPRTEMP = 0;
			double POREBPRPR = 0;
			double POREBFRPRTEMP = 0;
			double POREBFRPR = 0;
			double POREBDFPRTEMP = 0;
			double POREBDFPR = 0;
			double PORCOBPRE = 0;
			double PORCOBPRETEMP = 0;
			double INVPROM = 0;
			double PORROTPRETEMP = 0;
			double PORROTPRE = 0;
			double POMARNGPR = 0;
			double POMARBGPR = 0;
			double POMARSCPR = 0;
			double POMARPGPR = 0;
			double monvenpre = 0;
			double moncompre = 0;
			double canvenpre = 0;
			double moinvinpr = 0;
			double moinvfipr = 0;
			double morebprpr = 0;
			double morebdfpr = 0;
			double morebfrpr = 0;
			double mbganado = 0;
			double momermpre = 0;
			double cancompre = 0;
			double cainvinpr = 0;
			double cainvfipr = 0;
			double carebfrpr = 0;
			double carebprpr = 0;
			double carebdfpr = 0;
			int codCat = 0;
			int codDep = 0;
			int codLin = 0;
			int añoMesPre = 0;
			int inserts = 0;
			int updates = 0;
			// Nos conectamos a la BD
			conectar();

			try {			
				//**************************************************
			//**************************************************
 			System.out.println("// POR LINEA  999,CAT,DPTO,LIN,99");
			// POR LINEA  999,CAT,DPTO,LIN,99
			resultado = realizarConsulta("SELECT CODCAT, CODDEP, CODLIN, AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR,"
					+ " SUM(MOINVFIPR) MOINVFIPR, SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO, "
					+ "SUM(MOMERMPRE) MOMERMPRE, SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR FROM BECOFILE.PRLD01 "
					+ "WHERE CODTIE <> 999 AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN <> 99 AND CODSEC <> 99 "
					+ "AND AÑOMESPRE BETWEEN " + añoMesIni + " AND " + añoMesFin
					+ " GROUP BY CODCAT, CODDEP, CODLIN, AÑOMESPRE ORDER BY AÑOMESPRE, CODCAT, CODDEP, CODLIN");
			resultado.beforeFirst();
			while (resultado.next()) {
				codCat = resultado.getInt("CODCAT");
				codDep = resultado.getInt("CODDEP");
				codLin = resultado.getInt("CODLIN");
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				momermpre = resultado.getDouble("MOMERMPRE");
				canvenpre = resultado.getDouble("CANVENPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;
				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr
						+ ", MOREBPRPR = " + morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr 
						+ ", POREBPRPR = " + POREBPRPR + ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR 
						+ ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = " + PORROTPRE + ", " + "POMARBGPR = " + POMARBGPR 
						+ ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR + ", POMARPGPR = " + POMARPGPR 
						+ ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = " + cainvinpr + ", CAINVFIPR = "
						+ cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr + ", CAREBDFPR = " + carebdfpr
						+ " WHERE CODTIE = 999 AND CODCAT = " + codCat + " AND CODDEP = " + codDep + " AND CODLIN = " + codLin + " AND "
						+ "CODSEC = 99 AND AÑOMESPRE = " + añoMesPre);
					updates = updates + estado;
				if (estado < 1) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE,"
							+ " CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, "
							+ "POMARBGPR, POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) "
							+ "VALUES(999, " + codCat + ", " + codDep + ", " + codLin + ", 99, " + añoMesPre + ", " + monvenpre + ", "
							+ moncompre + ", " + canvenpre + ", " + moinvinpr + ", " + moinvfipr + ", " + morebprpr + ", "
							+ morebdfpr + ", " + morebfrpr + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", "
							+ PORROTPRE + ", " + PORCOBPRE + " , " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", "
							+ POMARPGPR + ", " + momermpre + ", " + cancompre + ", " + cainvinpr + ", " + cainvfipr + ", "
							+ carebfrpr + ", " + carebprpr + ", " + carebdfpr + ")");
				}
			}
			} catch (SQLException e) {
				e.printStackTrace();
				desconectar();
				return -1;
			}
			System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
			// Cerramos la conexion de la base de datos
			desconectar();

			return estado;
			
			}
		
		/**
		 * 
		 * @param añoMesIni
		 * @param añoMesFin
		 * @return
		 */
			public int consolidadosXTiendaCategoria(int añoMesIni, int añoMesFin) {
				int estado = -2;
				ResultSet resultado = null;
				double REBTOT = 0;
				double POREBPRPRTEMP = 0;
				double POREBPRPR = 0;
				double POREBFRPRTEMP = 0;
				double POREBFRPR = 0;
				double POREBDFPRTEMP = 0;
				double POREBDFPR = 0;
				double PORCOBPRE = 0;
				double PORCOBPRETEMP = 0;
				double INVPROM = 0;
				double PORROTPRETEMP = 0;
				double PORROTPRE = 0;
				double POMARNGPR = 0;
				double POMARBGPR = 0;
				double POMARSCPR = 0;
				double POMARPGPR = 0;
				double monvenpre = 0;
				double moncompre = 0;
				double canvenpre = 0;
				double moinvinpr = 0;
				double moinvfipr = 0;
				double morebprpr = 0;
				double morebdfpr = 0;
				double morebfrpr = 0;
				double mbganado = 0;
				double momermpre = 0;
				double cancompre = 0;
				double cainvinpr = 0;
				double cainvfipr = 0;
				double carebfrpr = 0;
				double carebprpr = 0;
				double carebdfpr = 0;
				int codTie = 0;
				int codCat = 0;
				int añoMesPre = 0;
				int inserts = 0;
				int updates = 0;
				// Nos conectamos a la BD
				conectar();

				try {			

			//**************************************************
			//**************************************************
			System.out.println("// POR TIENDA/CATEGORIA TIE,CAT,99,99,99");
			// POR TIENDA/CATEGORIA TIE,CAT,99,99,99
			resultado = realizarConsulta("SELECT CODTIE, CODCAT, AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR, "
					+ "SUM(MOINVFIPR) MOINVFIPR, SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO,"
					+ " SUM(MOMERMPRE) MOMERMPRE, SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR FROM BECOFILE.PRLD01 WHERE "
					+ " CODTIE <> 999 AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN = 99 AND CODSEC = 99 "
					+ "AND AÑOMESPRE BETWEEN " + añoMesIni + " AND " + añoMesFin
					+ " GROUP BY CODTIE, CODCAT, AÑOMESPRE ORDER BY AÑOMESPRE, CODTIE, CODCAT");
			resultado.beforeFirst();
			while (resultado.next()) {
				codCat = resultado.getInt("CODCAT");
				codTie = resultado.getInt("CODTIE");
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				canvenpre = resultado.getDouble("CANVENPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				momermpre = resultado.getDouble("MOMERMPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;
				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
						+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBPRPR = " + POREBPRPR
						+ ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
						+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
						+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
						+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
						+ ", CAREBDFPR = " + carebdfpr + " WHERE P1.CODTIE = " + codTie + " AND P1.CODCAT = " + codCat
						+ " AND P1.CODDEP = 99 AND P1.CODLIN = 99 AND P1.CODSEC = 99 AND P1.AÑOMESPRE = " + añoMesPre);
				updates = updates + estado;
				if (estado < 1) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN,CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, CANVENPRE, "
							+ " MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR, POMARNGPR, "
							+ "POMARSCPR, POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) VALUES("
							+ codTie + ", " + codCat + ", 99, 99, 99," + añoMesPre + ", " + monvenpre + ", " + moncompre + ", "
							+ canvenpre + ", " + moinvinpr + ", " + moinvfipr + ", " + morebprpr + ", " + morebdfpr + ", " + morebfrpr
							+ ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", " + PORROTPRE + ", " + PORCOBPRE + ", "
							+ POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + ", " + momermpre + ", " + cancompre
							+ ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr + ", " + carebprpr + ", " + carebdfpr + ")");
				}
			}
			} catch (SQLException e) {
				e.printStackTrace();
				desconectar();
				return -1;
			}
			System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
			// Cerramos la conexion de la base de datos
			desconectar();

			return estado;
			}
			
			/**
			 * 
			 * @param añoMesIni
			 * @param añoMesFin
			 * @return
			 */
			public int consolidadosXTienda(int añoMesIni, int añoMesFin) {
				int estado = -2;
				ResultSet resultado = null;
				double REBTOT = 0;
				double POREBPRPRTEMP = 0;
				double POREBPRPR = 0;
				double POREBFRPRTEMP = 0;
				double POREBFRPR = 0;
				double POREBDFPRTEMP = 0;
				double POREBDFPR = 0;
				double PORCOBPRE = 0;
				double PORCOBPRETEMP = 0;
				double INVPROM = 0;
				double PORROTPRETEMP = 0;
				double PORROTPRE = 0;
				double POMARNGPR = 0;
				double POMARBGPR = 0;
				double POMARSCPR = 0;
				double POMARPGPR = 0;
				double monvenpre = 0;
				double moncompre = 0;
				double canvenpre = 0;
				double moinvinpr = 0;
				double moinvfipr = 0;
				double morebprpr = 0;
				double morebdfpr = 0;
				double morebfrpr = 0;
				double mbganado = 0;
				double momermpre = 0;
				double cancompre = 0;
				double cainvinpr = 0;
				double cainvfipr = 0;
				double carebfrpr = 0;
				double carebprpr = 0;
				double carebdfpr = 0;
				int codTie = 0;
				int añoMesPre = 0;
				int inserts = 0;
				int updates = 0;
				// Nos conectamos a la BD
				conectar();

				try {			
			//**************************************************
			//**************************************************
			System.out.println("// POR TIENDA TIE,99,99,99,99");
			// POR TIENDA TIE,99,99,99,99
			resultado = realizarConsulta("SELECT CODTIE, AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR, SUM(MOINVFIPR) MOINVFIPR,"
					+ " SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO, SUM(MOMERMPRE) MOMERMPRE, "
					+ "SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR FROM BECOFILE.PRLD01 " +
							"WHERE CODTIE <> 999 "
					+ "AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN = 99 AND CODSEC = 99 AND AÑOMESPRE "
					+ "BETWEEN " + añoMesIni + " AND " + añoMesFin + " GROUP BY CODTIE, AÑOMESPRE ORDER BY AÑOMESPRE, CODTIE");
			resultado.beforeFirst();
			while (resultado.next()) {
				codTie = resultado.getInt("CODTIE");
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				canvenpre = resultado.getDouble("CANVENPRE");
				momermpre = resultado.getDouble("MOMERMPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;
				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
						+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBDFPR = " + POREBDFPR
						+ ", POREBFRPR = " + POREBFRPR + ", POREBPRPR = " + POREBPRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
						+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
						+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
						+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
						+ ", CAREBDFPR = " + carebdfpr + " WHERE P1.CODTIE = " + codTie
						+ " AND P1.CODCAT = 99 AND P1.CODDEP = 99 AND P1.CODLIN = 99 AND P1.CODSEC = 99"
						+ " AND P1.AÑOMESPRE = " + añoMesPre);
				updates = updates + estado;
				if (estado <= 0) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, "
							+ "CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, "
							+ "POMARBGPR, POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR)"
							+ " VALUES(" + codTie + ", 99, 99, 99, 99, " + añoMesPre + ", " + monvenpre + ", " + moncompre + ", "
							+ canvenpre + ", " + moinvinpr + ", " + moinvfipr + ", " + morebprpr + ", " + morebdfpr + ", "
							+ morebfrpr + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", " + PORROTPRE + ", "
							+ PORCOBPRE + ", " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + "," 
							+ momermpre + "," + cancompre + ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr + ", " 
							+ carebprpr + ", " + carebdfpr + ")");
				}
			}
			} catch (SQLException e) {
				e.printStackTrace();
				desconectar();
				return -1;
			}
			System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
			// Cerramos la conexion de la base de datos
			desconectar();

			return estado;

			}
			
			/**
			 * 
			 * @param añoMesIni
			 * @param añoMesFin
			 * @return
			 */
			public int consolidadosXTiendaCategoriaDepartamento(int añoMesIni, int añoMesFin) {
				int estado = -2;
				ResultSet resultado = null;
				double REBTOT = 0;
				double POREBPRPRTEMP = 0;
				double POREBPRPR = 0;
				double POREBFRPRTEMP = 0;
				double POREBFRPR = 0;
				double POREBDFPRTEMP = 0;
				double POREBDFPR = 0;
				double PORCOBPRE = 0;
				double PORCOBPRETEMP = 0;
				double INVPROM = 0;
				double PORROTPRETEMP = 0;
				double PORROTPRE = 0;
				double POMARNGPR = 0;
				double POMARBGPR = 0;
				double POMARSCPR = 0;
				double POMARPGPR = 0;
				double monvenpre = 0;
				double moncompre = 0;
				double canvenpre = 0;
				double moinvinpr = 0;
				double moinvfipr = 0;
				double morebprpr = 0;
				double morebdfpr = 0;
				double morebfrpr = 0;
				double mbganado = 0;
				double momermpre = 0;
				double cancompre = 0;
				double cainvinpr = 0;
				double cainvfipr = 0;
				double carebfrpr = 0;
				double carebprpr = 0;
				double carebdfpr = 0;
				int codTie = 0;
				int codCat = 0;
				int codDep = 0;
				int añoMesPre = 0;
				int inserts = 0;
				int updates = 0;
				// Nos conectamos a la BD
				conectar();

				try {			
			//**************************IROJAS***************************************
			//**************************IROJAS***************************************
			System.out.println("// POR TIENDA/DEPARTAMENTO TIE,CAT,DPTO,99,99");
			// POR TIENDA/DEPARTAMENTO  TIE,CAT,DPTO,99,99
			resultado = realizarConsulta("SELECT CODTIE, CODDEP, CODCAT, AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR, "
					+ "SUM(MOINVFIPR) MOINVFIPR, SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO,"
					+ " SUM(MOMERMPRE) MOMERMPRE, SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR FROM BECOFILE.PRLD01 WHERE "
					+ " CODTIE <> 999 AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN <> 99 AND CODSEC <> 99 "
					+ "AND AÑOMESPRE BETWEEN " + añoMesIni + " AND " + añoMesFin
					+ " GROUP BY CODTIE, CODCAT, CODDEP, AÑOMESPRE ORDER BY AÑOMESPRE, CODTIE, CODCAT, CODDEP");
			resultado.beforeFirst();
			while (resultado.next()) {
				codTie = resultado.getInt("CODTIE");
				codDep = resultado.getInt("CODDEP");
				codCat = resultado.getInt("CODCAT");
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				canvenpre = resultado.getDouble("CANVENPRE");
				momermpre = resultado.getDouble("MOMERMPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;
				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
						+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBDFPR = " + POREBDFPR
						+ ", POREBFRPR = " + POREBFRPR + ", POREBPRPR = " + POREBPRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
						+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
						+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
						+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
						+ ", CAREBDFPR = " + carebdfpr + " WHERE P1.CODTIE = " + codTie
						+ " AND P1.CODCAT = " + codCat + " AND P1.CODDEP = " + codDep + " AND P1.CODLIN = 99 AND P1.CODSEC = 99"
						+ " AND P1.AÑOMESPRE = " + añoMesPre);
				updates = updates + estado;
				if (estado <= 0) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, "
							+ "CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, "
							+ "POMARBGPR, POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR)"
							+ " VALUES(" + codTie + ", " + codCat + ", " + codDep + ", 99, 99, " + añoMesPre + ", " + monvenpre + ", " + moncompre + ", "
							+ canvenpre + ", " + moinvinpr + ", " + moinvfipr + ", " + morebprpr + ", " + morebdfpr + ", "
							+ morebfrpr + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", " + PORROTPRE + ", "
							+ PORCOBPRE + ", " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + "," 
							+ momermpre + "," + cancompre + ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr + ", " 
							+ carebprpr + ", " + carebdfpr + ")");
				}
			}
				} catch (SQLException e) {
					e.printStackTrace();
					desconectar();
					return -1;
				}
				System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
				// Cerramos la conexion de la base de datos
				desconectar();

				return estado;
			}
			
			/**
			 * 
			 * @param añoMesIni
			 * @param añoMesFin
			 * @return
			 */
			public int consolidadosXCategoria(int añoMesIni, int añoMesFin) {
				int estado = -2;
				ResultSet resultado = null;
				double REBTOT = 0;
				double POREBPRPRTEMP = 0;
				double POREBPRPR = 0;
				double POREBFRPRTEMP = 0;
				double POREBFRPR = 0;
				double POREBDFPRTEMP = 0;
				double POREBDFPR = 0;
				double PORCOBPRE = 0;
				double PORCOBPRETEMP = 0;
				double INVPROM = 0;
				double PORROTPRETEMP = 0;
				double PORROTPRE = 0;
				double POMARNGPR = 0;
				double POMARBGPR = 0;
				double POMARSCPR = 0;
				double POMARPGPR = 0;
				double monvenpre = 0;
				double moncompre = 0;
				double canvenpre = 0;
				double moinvinpr = 0;
				double moinvfipr = 0;
				double morebprpr = 0;
				double morebdfpr = 0;
				double morebfrpr = 0;
				double mbganado = 0;
				double momermpre = 0;
				double cancompre = 0;
				double cainvinpr = 0;
				double cainvfipr = 0;
				double carebfrpr = 0;
				double carebprpr = 0;
				double carebdfpr = 0;
				int codCat = 0;
				int añoMesPre = 0;
				int inserts = 0;
				int updates = 0;
				// Nos conectamos a la BD
				conectar();

				try {			
			//*************************IROJAS****************************************
			System.out.println("// POR CATEGORIA 999,CAT,99,99,99");
			// POR CATEGORIA 999,CAT,99,99,99
			resultado = realizarConsulta("SELECT CODCAT, AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR, "
					+ "SUM(MOINVFIPR) MOINVFIPR, SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO, "
					+ "SUM(MOMERMPRE) MOMERMPRE, SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR FROM BECOFILE.PRLD01"
					+ " WHERE CODTIE <> 999 AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN = 99 AND "
					+ "CODSEC = 99 AND AÑOMESPRE BETWEEN " + añoMesIni + " AND " + añoMesFin
					+ " GROUP BY CODCAT, AÑOMESPRE ORDER BY AÑOMESPRE, CODCAT");
			resultado.beforeFirst();
			while (resultado.next()) {
				codCat = resultado.getInt("CODCAT");
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				canvenpre = resultado.getDouble("CANVENPRE");
				momermpre = resultado.getDouble("MOMERMPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;
				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
						+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBPRPR = " + POREBPRPR
						+ ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
						+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
						+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
						+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
						+ ", CAREBDFPR = " + carebdfpr + " WHERE P1.CODTIE = 999 AND P1.CODCAT = " + codCat
						+ " AND P1.CODDEP = 99 AND P1.CODLIN = 99 AND P1.CODSEC = 99 AND P1.AÑOMESPRE = " + añoMesPre);
				updates = updates + estado;
				if (estado < 1) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, "
							+ "CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, POREBPRPR, POREBDFPR, POREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR, "
							+ "POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) "
							+ "VALUES(999, " + codCat + ", 99, 99, 99, " + añoMesPre + ", " + monvenpre + ", " + moncompre + ", "
							+ canvenpre + ", " + moinvinpr + ", " + moinvfipr + ", " + morebprpr + ", " + morebdfpr + ", "
							+ morebfrpr + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", " + PORROTPRE + ", "
							+ PORCOBPRE + ", " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + ", "
							+ momermpre + ", " + cancompre + ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr
							+ ", " + carebprpr + ", " + carebdfpr + ")");
				}
			}
				} catch (SQLException e) {
					e.printStackTrace();
					desconectar();
					return -1;
				}
				System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
				// Cerramos la conexion de la base de datos
				desconectar();

				return estado;
			}
			
			/**
			 * Método consolidadosXCompania
			 * @param añoMesIni
			 * @param añoMesFin
			 * @return
			 */
			public int consolidadosXCompania(int añoMesIni, int añoMesFin) {
				int estado = -2;
				ResultSet resultado = null;
				double REBTOT = 0;
				double POREBPRPRTEMP = 0;
				double POREBPRPR = 0;
				double POREBFRPRTEMP = 0;
				double POREBFRPR = 0;
				double POREBDFPRTEMP = 0;
				double POREBDFPR = 0;
				double PORCOBPRE = 0;
				double PORCOBPRETEMP = 0;
				double INVPROM = 0;
				double PORROTPRETEMP = 0;
				double PORROTPRE = 0;
				double POMARNGPR = 0;
				double POMARBGPR = 0;
				double POMARSCPR = 0;
				double POMARPGPR = 0;
				double monvenpre = 0;
				double moncompre = 0;
				double canvenpre = 0;
				double moinvinpr = 0;
				double moinvfipr = 0;
				double morebprpr = 0;
				double morebdfpr = 0;
				double morebfrpr = 0;
				double mbganado = 0;
				double momermpre = 0;
				double cancompre = 0;
				double cainvinpr = 0;
				double cainvfipr = 0;
				double carebfrpr = 0;
				double carebprpr = 0;
				double carebdfpr = 0;
				int añoMesPre = 0;
				int inserts = 0;
				int updates = 0;
				// Nos conectamos a la BD
				conectar();

				try {			
	
			//**************************************************
			//**************************************************
			System.out.println("// POR CONSOLIDADO 999,99,99,99,99");
			// POR CONSOLIDADO 999,99,99,99,99
			resultado = realizarConsulta("SELECT AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR, SUM(MOINVFIPR) MOINVFIPR,"
					+ " SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO, SUM(MOMERMPRE) MOMERMPRE, "
					+ "SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR " +
							"FROM BECOFILE.PRLD01 WHERE CODTIE <> 999"
					+ " AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN = 99 AND CODSEC = 99 AND AÑOMESPRE "
					+ "BETWEEN " + añoMesIni + " AND " + añoMesFin + " GROUP BY AÑOMESPRE ORDER BY AÑOMESPRE");
			resultado.beforeFirst();
			while (resultado.next()) {
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				momermpre = resultado.getDouble("MOMERMPRE");
				canvenpre = resultado.getDouble("CANVENPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;

				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre 
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
						+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBPRPR = " + POREBPRPR
						+ ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
						+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
						+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
						+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
						+ ", CAREBDFPR = " + carebdfpr
						+ " WHERE P1.CODTIE = 999 AND P1.CODCAT = 99 AND P1.CODDEP = 99 AND P1.CODLIN = 99 AND P1.CODSEC = 99 "
						+ "AND P1.AÑOMESPRE = " + añoMesPre);
				updates = updates + estado;
				if (estado < 1) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, "
							+ "CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR, POMARNGPR, POMARSCPR, "
							+ "POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) VALUES(999, 99, 99, 99, 99, "
							+ añoMesPre + "," + monvenpre+ ", " + moncompre+ ", " + canvenpre + ", " + moinvinpr + ", " + moinvfipr
							+ ", " + morebprpr + ", " + morebdfpr + ", " + morebfrpr + ", " + PORROTPRE + ", " + PORCOBPRE + ", "
							+ POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + ", " + momermpre + ", "
							+ cancompre + ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr + ", "
							+ carebprpr + ", " + carebdfpr + ")");
				}
			}
				} catch (SQLException e) {
					e.printStackTrace();
					desconectar();
					return -1;
				}
				System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
				// Cerramos la conexion de la base de datos
				desconectar();

				return estado;
			}
			
			/**
			 * 
			 * @param añoMesIni
			 * @param añoMesFin
			 * @return
			 */
			public int consolidadosXSeccion(int añoMesIni, int añoMesFin) {
				int estado = -2;
				ResultSet resultado = null;
				double REBTOT = 0;
				double POREBPRPRTEMP = 0;
				double POREBPRPR = 0;
				double POREBFRPRTEMP = 0;
				double POREBFRPR = 0;
				double POREBDFPRTEMP = 0;
				double POREBDFPR = 0;
				double PORCOBPRE = 0;
				double PORCOBPRETEMP = 0;
				double INVPROM = 0;
				double PORROTPRETEMP = 0;
				double PORROTPRE = 0;
				double POMARNGPR = 0;
				double POMARBGPR = 0;
				double POMARSCPR = 0;
				double POMARPGPR = 0;
				double monvenpre = 0;
				double moncompre = 0;
				double canvenpre = 0;
				double moinvinpr = 0;
				double moinvfipr = 0;
				double morebprpr = 0;
				double morebdfpr = 0;
				double morebfrpr = 0;
				double mbganado = 0;
				double momermpre = 0;
				double cancompre = 0;
				double cainvinpr = 0;
				double cainvfipr = 0;
				double carebfrpr = 0;
				double carebprpr = 0;
				double carebdfpr = 0;
				int codCat = 0;
				int codDep = 0;
				int codLin = 0;
				int codSec = 0;
				int añoMesPre = 0;
				int inserts = 0;
				int updates = 0;
				// Nos conectamos a la BD
				conectar();

				try {			
			//**************************************************
			//**************************************************
			System.out.println("// POR SECCION 999,CAT,DEP,LIN,SEC");
			// POR SECCION  999,CAT,DEP,LIN,SEC
			resultado = realizarConsulta("SELECT CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR, SUM(MOINVFIPR) MOINVFIPR,"
					+ " SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO, SUM(MOMERMPRE) MOMERMPRE, "
					+ "SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR " +
							"FROM BECOFILE.PRLD01 WHERE CODTIE <> 999"
					+ " AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN <> 99 AND CODSEC <> 99 AND AÑOMESPRE "
					+ "BETWEEN " + añoMesIni + " AND " + añoMesFin + " GROUP BY CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE ORDER BY AÑOMESPRE, CODCAT, CODDEP, CODLIN, CODSEC");
			resultado.beforeFirst();
			while (resultado.next()) {
				codCat = resultado.getInt("CODCAT");
				codDep = resultado.getInt("CODDEP");
				codLin = resultado.getInt("CODLIN");
				codSec = resultado.getInt("CODSEC");
				añoMesPre = resultado.getInt("AÑOMESPRE");
				monvenpre = resultado.getDouble("MONVENPRE");
				moncompre = resultado.getDouble("MONCOMPRE");
				moinvinpr = resultado.getDouble("MOINVINPR");
				moinvfipr = resultado.getDouble("MOINVFIPR");
				morebprpr = resultado.getDouble("MOREBPRPR");
				morebdfpr = resultado.getDouble("MOREBDFPR");
				morebfrpr = resultado.getDouble("MOREBFRPR");
				mbganado = resultado.getDouble("MBGANADO");
				momermpre = resultado.getDouble("MOMERMPRE");
				canvenpre = resultado.getDouble("CANVENPRE");
				cancompre = resultado.getDouble("CANCOMPRE");
				cainvinpr = resultado.getDouble("CAINVINPR");
				cainvfipr = resultado.getDouble("CAINVFIPR");
				carebfrpr = resultado.getDouble("CAREBFRPR");
				carebprpr = resultado.getDouble("CAREBPRPR");
				carebdfpr = resultado.getDouble("CAREBDFPR");
				POREBDFPRTEMP = 0;
				REBTOT = morebprpr + morebdfpr + morebfrpr;
				// REBAJAS
				if (REBTOT != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;

				POREBDFPR = POREBDFPRTEMP;
				// COBERTURA
				if (monvenpre != 0)
					PORCOBPRETEMP = moinvinpr / monvenpre;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORCOBPRETEMP > 999)
					PORCOBPRETEMP = 999;
				else if (PORCOBPRETEMP < 0)
					PORCOBPRETEMP = 0;
				PORCOBPRE = PORCOBPRETEMP;
				// ROTACION
				INVPROM = (moinvinpr + moinvfipr) / 2;
				if (INVPROM != 0)
					PORROTPRETEMP = (monvenpre * 12) / INVPROM;
				// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
				// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
				if (PORROTPRETEMP > 999)
					PORROTPRETEMP = 999;
				else if (PORROTPRETEMP < 0)
					PORROTPRETEMP = 0;
				PORROTPRE = PORROTPRETEMP;
				// MARGEN BRUTO GANADO
				if (monvenpre != 0)
					POMARBGPR = (mbganado / monvenpre) * 100;
				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				// MARGEN SEGUN COMPRAS
				if (monvenpre != 0)
					POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
				// Margen Por Ganar = MB del inventario final o igual a MBSC
				// Margen Por Ganar %
				POMARPGPR = POMARSCPR;
				estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre 
						+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
						+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBPRPR = " + POREBPRPR
						+ ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
						+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
						+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
						+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
						+ ", CAREBDFPR = " + carebdfpr
						+ " WHERE P1.CODTIE = 999 AND P1.CODCAT = " + codCat + " AND P1.CODDEP = " + codDep + " AND P1.CODLIN = " + codLin + " AND P1.CODSEC = " + codSec 
						+ " AND P1.AÑOMESPRE = " + añoMesPre);
				updates = updates + estado;
				if (estado < 1) {
					inserts ++;
					estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, "
							+ "CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR, POMARNGPR, POMARSCPR, "
							+ "POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) VALUES(999, " 
							+ codCat + ", " + codDep + ", " + codLin + ", " + codSec + ", "
							+ añoMesPre + "," + monvenpre+ ", " + moncompre+ ", " + canvenpre + ", " + moinvinpr + ", " + moinvfipr
							+ ", " + morebprpr + ", " + morebdfpr + ", " + morebfrpr + ", " + PORROTPRE + ", " + PORCOBPRE + ", "
							+ POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + ", " + momermpre + ", "
							+ cancompre + ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr + ", "
							+ carebprpr + ", " + carebdfpr + ")");
				}
			}
				} catch (SQLException e) {
					e.printStackTrace();
					desconectar();
					return -1;
				}
				System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
				// Cerramos la conexion de la base de datos
				desconectar();

				return estado;
			}

			/**
			 * 
			 * @param añoMesIni
			 * @param añoMesFin
			 * @return
			 */
			public int consolidadosXTiendaLinea(int añoMesIni, int añoMesFin) {
				int estado = -2;
				ResultSet resultado = null;
				double REBTOT = 0;
				double POREBPRPRTEMP = 0;
				double POREBPRPR = 0;
				double POREBFRPRTEMP = 0;
				double POREBFRPR = 0;
				double POREBDFPRTEMP = 0;
				double POREBDFPR = 0;
				double PORCOBPRE = 0;
				double PORCOBPRETEMP = 0;
				double INVPROM = 0;
				double PORROTPRETEMP = 0;
				double PORROTPRE = 0;
				double POMARNGPR = 0;
				double POMARBGPR = 0;
				double POMARSCPR = 0;
				double POMARPGPR = 0;
				double monvenpre = 0;
				double moncompre = 0;
				double canvenpre = 0;
				double moinvinpr = 0;
				double moinvfipr = 0;
				double morebprpr = 0;
				double morebdfpr = 0;
				double morebfrpr = 0;
				double mbganado = 0;
				double momermpre = 0;
				double cancompre = 0;
				double cainvinpr = 0;
				double cainvfipr = 0;
				double carebfrpr = 0;
				double carebprpr = 0;
				double carebdfpr = 0;
				int codTie = 0;
				int codCat = 0;
				int codDep = 0;
				int codLin = 0;
				int añoMesPre = 0;
				int inserts = 0;
				int updates = 0;
				// Nos conectamos a la BD
				conectar();

				try {			
			//**************************************************
			//**************************************************
				System.out.println("// POR TIE/LIN TIE,CAT,DEP,LIN,99");
				// POR TIENDA/LINEA  TIE,CAT,DEP,LIN,99
				resultado = realizarConsulta("SELECT CODTIE, CODCAT, CODDEP, CODLIN,  AÑOMESPRE, SUM(MONVENPRE) MONVENPRE, SUM(MONCOMPRE) MONCOMPRE, SUM(CANVENPRE) CANVENPRE, SUM(MOINVINPR) MOINVINPR, SUM(MOINVFIPR) MOINVFIPR,"
						+ " SUM(MOREBPRPR) MOREBPRPR, SUM(MOREBDFPR) MOREBDFPR, SUM(MOREBFRPR) MOREBFRPR, SUM(ZONED(((POMARBGPR * MONVENPRE) / 100), 15 , 2)) MBGANADO, SUM(MOMERMPRE) MOMERMPRE, "
						+ "SUM(CANCOMPRE) CANCOMPRE, SUM(CAINVINPR) CAINVINPR, SUM(CAINVFIPR) CAINVFIPR, SUM(CAREBFRPR) CAREBFRPR, SUM(CAREBPRPR) CAREBPRPR, SUM(CAREBDFPR) CAREBDFPR " +
								"FROM BECOFILE.PRLD01 WHERE CODTIE <> 999"
						+ " AND CODCAT <> 99 AND CODDEP <> 99 AND CODLIN <> 99 AND CODSEC <> 99 AND AÑOMESPRE "
						+ "BETWEEN " + añoMesIni + " AND " + añoMesFin + " GROUP BY CODTIE, CODCAT, CODDEP, CODLIN, AÑOMESPRE ORDER BY AÑOMESPRE, CODTIE, CODCAT, CODDEP, CODLIN");
				resultado.beforeFirst();
				while (resultado.next()) {
					codCat = resultado.getInt("CODCAT");
					codDep = resultado.getInt("CODDEP");
					codLin = resultado.getInt("CODLIN");
					codTie = resultado.getInt("CODTIE");
					añoMesPre = resultado.getInt("AÑOMESPRE");
					monvenpre = resultado.getDouble("MONVENPRE");
					moncompre = resultado.getDouble("MONCOMPRE");
					moinvinpr = resultado.getDouble("MOINVINPR");
					moinvfipr = resultado.getDouble("MOINVFIPR");
					morebprpr = resultado.getDouble("MOREBPRPR");
					morebdfpr = resultado.getDouble("MOREBDFPR");
					morebfrpr = resultado.getDouble("MOREBFRPR");
					mbganado = resultado.getDouble("MBGANADO");
					momermpre = resultado.getDouble("MOMERMPRE");
					canvenpre = resultado.getDouble("CANVENPRE");
					cancompre = resultado.getDouble("CANCOMPRE");
					cainvinpr = resultado.getDouble("CAINVINPR");
					cainvfipr = resultado.getDouble("CAINVFIPR");
					carebfrpr = resultado.getDouble("CAREBFRPR");
					carebprpr = resultado.getDouble("CAREBPRPR");
					carebdfpr = resultado.getDouble("CAREBDFPR");
					POREBDFPRTEMP = 0;
					REBTOT = morebprpr + morebdfpr + morebfrpr;
					// REBAJAS
					if (REBTOT != 0) {
						POREBPRPRTEMP = (morebprpr * 100) / REBTOT;
						if (POREBPRPRTEMP > 999)
							POREBPRPRTEMP = 999;
						else if (POREBPRPRTEMP < 0)
							POREBPRPRTEMP = 0;
						POREBPRPR = POREBPRPRTEMP;
						POREBFRPRTEMP = (morebfrpr * 100) / REBTOT;
						if (POREBFRPRTEMP > 999)
							POREBFRPRTEMP = 999;
						else if (POREBFRPRTEMP < 0)
							POREBFRPRTEMP = 0;
						POREBFRPR = POREBFRPRTEMP;
						POREBDFPRTEMP = (morebdfpr * 100) / REBTOT;
					}
					if (POREBDFPRTEMP > 999)
						POREBDFPRTEMP = 999;
					else if (POREBDFPRTEMP < 0)
						POREBDFPRTEMP = 0;

					POREBDFPR = POREBDFPRTEMP;
					// COBERTURA
					if (monvenpre != 0)
						PORCOBPRETEMP = moinvinpr / monvenpre;
					// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
					// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
					if (PORCOBPRETEMP > 999)
						PORCOBPRETEMP = 999;
					else if (PORCOBPRETEMP < 0)
						PORCOBPRETEMP = 0;
					PORCOBPRE = PORCOBPRETEMP;
					// ROTACION
					INVPROM = (moinvinpr + moinvfipr) / 2;
					if (INVPROM != 0)
						PORROTPRETEMP = (monvenpre * 12) / INVPROM;
					// *MVM 28/12/2007 CAMBIO PORQUE LOS VALORES SUPERAN 5 2
					// * PORQUE HAY VALORES NEGATIVOS O SUPERIOR A 1000)
					if (PORROTPRETEMP > 999)
						PORROTPRETEMP = 999;
					else if (PORROTPRETEMP < 0)
						PORROTPRETEMP = 0;
					PORROTPRE = PORROTPRETEMP;
					// MARGEN BRUTO GANADO
					if (monvenpre != 0)
						POMARBGPR = (mbganado / monvenpre) * 100;
					// MARGEN NETO
					POMARNGPR = POMARBGPR - 2;
					// MARGEN SEGUN COMPRAS
					if (monvenpre != 0)
						POMARSCPR = ((mbganado + REBTOT) / monvenpre) * 100;
					// Margen Por Ganar = MB del inventario final o igual a MBSC
					// Margen Por Ganar %
					POMARPGPR = POMARSCPR;
					estado = realizarSentencia("UPDATE BECOFILE.PRLD01 P1 SET MONVENPRE = " + monvenpre + ", MONCOMPRE = " + moncompre 
							+ ", CANVENPRE = " + canvenpre + ", MOINVINPR = " + moinvinpr + ", MOINVFIPR = " + moinvfipr + ", MOREBPRPR = "
							+ morebprpr + ", MOREBDFPR = " + morebdfpr + ", MOREBFRPR = " + morebfrpr + ", POREBPRPR = " + POREBPRPR
							+ ", POREBDFPR = " + POREBDFPR + ", POREBFRPR = " + POREBFRPR + ", PORCOBPRE = " + PORCOBPRE + ", PORROTPRE = "
							+ PORROTPRE + ", POMARBGPR = " + POMARBGPR + ", POMARNGPR = " + POMARNGPR + ", POMARSCPR = " + POMARSCPR
							+ ", POMARPGPR = " + POMARPGPR + ", MOMERMPRE = " + momermpre + ", CANCOMPRE = " + cancompre + ", CAINVINPR = "
							+ cainvinpr + ", CAINVFIPR = " + cainvfipr + ", CAREBFRPR = " + carebfrpr + ", CAREBPRPR = " + carebprpr
							+ ", CAREBDFPR = " + carebdfpr
							+ " WHERE P1.CODTIE = " + codTie + " AND P1.CODCAT = " + codCat + " AND P1.CODDEP = " + codDep + " AND P1.CODLIN = " + codLin + " AND P1.CODSEC = 99 "   
							+ " AND P1.AÑOMESPRE = " + añoMesPre);
					updates = updates + estado;
					if (estado < 1) {
						inserts ++;
						estado = realizarSentencia("INSERT INTO BECOFILE.PRLD01 (CODTIE, CODCAT, CODDEP, CODLIN, CODSEC, AÑOMESPRE, MONVENPRE, MONCOMPRE, "
								+ "CANVENPRE, MOINVINPR, MOINVFIPR, MOREBPRPR, MOREBDFPR, MOREBFRPR, PORROTPRE, PORCOBPRE, POMARBGPR, POMARNGPR, POMARSCPR, "
								+ "POMARPGPR, MOMERMPRE, CANCOMPRE, CAINVINPR, CAINVFIPR, CAREBFRPR, CAREBPRPR, CAREBDFPR) VALUES(" + codTie + ", " 
								+ codCat + ", " + codDep + ", " + codLin + ", 99, "
								+ añoMesPre + "," + monvenpre+ ", " + moncompre+ ", " + canvenpre + ", " + moinvinpr + ", " + moinvfipr
								+ ", " + morebprpr + ", " + morebdfpr + ", " + morebfrpr + ", " + PORROTPRE + ", " + PORCOBPRE + ", "
								+ POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", " + POMARPGPR + ", " + momermpre + ", "
								+ cancompre + ", " + cainvinpr + ", " + cainvfipr + ", " + carebfrpr + ", "
								+ carebprpr + ", " + carebdfpr + ")");
					}
				}
		} catch (SQLException e) {
			e.printStackTrace();
			desconectar();
			return -1;
		}
		System.out.println("en Consolidados se realizaron " + updates + " updates y " + inserts + " inserts ");
		// Cerramos la conexion de la base de datos
		desconectar();

		return estado;
	}

	
	
	JTextArea log;
	public int leerArchivo(String archivo) {
		int i = 1;
		try {
		CsvReader reader = new CsvReader(archivo, ';');
		reader.readHeaders();
		System.out.println("Leyendo el archivo " + archivo + "...\n" );
		while (reader.readRecord()) {
			String codtie = reader.get("CODTIE");
			String coddep = reader.get("CODDEP");
			log.append("CODTIE: "+ codtie + " CODDEP: "+ coddep + "\n");
			i++;
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public int actualizarPiezasDeArchivo(String archivo) {
		int j=0;
		int i=0;
		try {
			conectar();
			Statement loteSentencias = crearSentencia();
			/* Archivo de Entrada */
			CsvReader reader = new CsvReader(archivo, ';');
			reader.readHeaders();
			System.out.println("Leyendo el archivo " + archivo + " ... \n");
			while (reader.readRecord()) {
				String codtie = reader.get("CODTIE");
				String coddep = reader.get("CODDEP");
				String codlin = reader.get("CODLIN");
				String codsec = reader.get("CODSEC");
				String año = reader.get("AÑO");
				String mes = reader.get("MES");
				int añomespre = Integer.parseInt(año) * 100 + Integer.parseInt(mes);
				
				// piezas Presupuestadas
				double canvenpre = (new Double(reader.get("CANVENPRE"))).doubleValue();
				double cainvinpr = (new Double(reader.get("CAINVINPR"))).doubleValue();
				double cainvfipr = (new Double(reader.get("CAINVFIPR"))).doubleValue();
				double cancompre = (new Double(reader.get("CANCOMPRE"))).doubleValue();
				/*				
 				double canrebajas = 0; //reader.get("CANREBAJAS");
				double carebpr = (new Double(canrebajas)).doubleValue();
				double carebprpr = carebpr * 0.5;
				double carebfrpr = carebpr * 0.5;
				double carebdfpr = 0;
				*/
				String sentenciaUpdate=  "UPDATE BECOFILE.PRLD01 SET (CANVENPRE, CAINVINPR, CAINVFIPR, CANCOMPRE) = (" + canvenpre 
					+ ", " + cainvinpr + "," + cainvfipr + " ," + cancompre + ") WHERE CODTIE = " + codtie + " AND CODCAT <> 99 AND CODDEP =  "
					+ coddep + " AND CODLIN = " + codlin + " AND CODSEC = " + codsec + " AND AÑOMESPRE = " + añomespre;
				j++;
				loteSentencias.addBatch(sentenciaUpdate); 	
			}
			i = realizarLoteSentencias(loteSentencias).length;
			desconectar();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			desconectar();
			return -1;
		}
	System.out.println("Se realizaron " + j + " lecturas ");
	return i;		
	}

	public int actualizarNoviembreDeArchivo(String archivo) {
		int j=0;
		int i=0;
		double PORROTPRE = 0;
		double POMARNGPR = 0;
		double POMARBGPR = 0;
		double POMARSCPR = 0;
		double POMARPGPR = 0;
		double POREBPRPRTEMP = 0;
		double POREBPRPR = 0;
		double POREBFRPRTEMP = 0;
		double POREBFRPR = 0;
		double POREBDFPRTEMP = 0;
		double POREBDFPR = 0;

		try {
			conectar();
			Statement loteSentencias = crearSentencia();
			/* Archivo de Entrada */
			CsvReader reader = new CsvReader(archivo, ';');
			reader.readHeaders();
			System.out.println("Leyendo el archivo " + archivo + " ... \n");
			while (reader.readRecord()) {
				String codtie = reader.get("CODTIE");
				String coddep = reader.get("CODDEP");
				String codlin = reader.get("CODLIN");
				String codsec = reader.get("CODSEC");
				String año = reader.get("AÑO");
				String mes = reader.get("MES");
				int añomespre = Integer.parseInt(año) * 100 + Integer.parseInt(mes);
				
				double monvenpre = (new Double(reader.get("MONVENPRE"))).doubleValue();
				double moinvinpr = (new Double(reader.get("MOINVINPR"))).doubleValue();
				double moinvfipr = (new Double(reader.get("MOINVFIPR"))).doubleValue();
				double moncompre = (new Double(reader.get("MONCOMPRE"))).doubleValue();
				double morebpr = (new Double(reader.get("MONREBAJAS"))).doubleValue();
				double morebprpr = morebpr * 0.5;
				double morebfrpr = morebpr * 0.5;
				double morebdfpr = 0;
				double momermpre = (new Double(reader.get("MOMERMPRE"))).doubleValue();

				// REBAJAS
				if (morebpr != 0) {
					POREBPRPRTEMP = (morebprpr * 100) / morebpr;
					if (POREBPRPRTEMP > 999)
						POREBPRPRTEMP = 999;
					else if (POREBPRPRTEMP < 0)
						POREBPRPRTEMP = 0;
					POREBPRPR = POREBPRPRTEMP;
					POREBFRPRTEMP = (morebfrpr * 100) / morebpr;
					if (POREBFRPRTEMP > 999)
						POREBFRPRTEMP = 999;
					else if (POREBFRPRTEMP < 0)
						POREBFRPRTEMP = 0;
					POREBFRPR = POREBFRPRTEMP;
					POREBDFPRTEMP = (morebdfpr * 100) / morebpr;
				}
				if (POREBDFPRTEMP > 999)
					POREBDFPRTEMP = 999;
				else if (POREBDFPRTEMP < 0)
					POREBDFPRTEMP = 0;
				POREBDFPR = POREBDFPRTEMP;
				
 				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				
 				POMARPGPR = POMARSCPR;

				String sentenciaUpdate = "UPDATE BECOFILE.PRLD01 SET (MONVENPRE, MOINVINPR, MOINVFIPR, MONCOMPRE, POREBPRPR, POREBDFPR, POREBFRPR," +
						" PORROTPRE, POMARBGPR, POMARNGPR, POMARSCPR, POMARPGPR, MOMERMPRE) = (" + monvenpre 
					+ ", " + moinvinpr + "," + moinvfipr + " ," + moncompre + ", " + POREBPRPR + ", " + POREBDFPR + ", " + POREBFRPR + ", "
					+ PORROTPRE + ", " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", "
					+ POMARPGPR  + ", "	+ momermpre + ") WHERE CODTIE = " + codtie + " AND CODCAT <> 99 AND CODDEP =  "
					+ coddep + " AND CODLIN = " + codlin + " AND CODSEC = " + codsec + " AND AÑOMESPRE = " + añomespre;
				j++;
				loteSentencias.addBatch(sentenciaUpdate); 	
			}
			i = realizarLoteSentencias(loteSentencias).length;
			desconectar();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			desconectar();
			return -1;
		}
	System.out.println("Se realizaron " + j + " lecturas ");
	return i;		
	}

	public int actualizarNoviembreARREGLO(String archivo) {
		int j=0;
		int i=0;
		double PORROTPRE = 0;
		double POMARNGPR = 0;
		double POMARBGPR = 0;
		double POMARSCPR = 0;
		double POMARPGPR = 0;
		double PORCOBPRE = 0;

		try {
			conectar();
			Statement loteSentencias = crearSentencia();
			/* Archivo de Entrada */
			CsvReader reader = new CsvReader(archivo, ';');
			reader.readHeaders();
			System.out.println("Leyendo el archivo " + archivo + " ... \n");
			while (reader.readRecord()) {
				String codtie = reader.get("CODTIE");
				String coddep = reader.get("CODDEP");
				String codlin = reader.get("CODLIN");
				String codsec = reader.get("CODSEC");
				String año = reader.get("AÑO");
				String mes = reader.get("MES");
				int añomespre = Integer.parseInt(año) * 100 + Integer.parseInt(mes);
				
				  PORROTPRE = (new Double(reader.get("PORROTPRE"))).doubleValue();
				  POMARSCPR = (new Double(reader.get("POMARSCPR"))).doubleValue();
				  POMARBGPR = (new Double(reader.get("POMARBGPR"))).doubleValue();
				  PORCOBPRE = (new Double(reader.get("PORCOBPRE"))).doubleValue();
				
 				// MARGEN NETO
				POMARNGPR = POMARBGPR - 2;
				
 				POMARPGPR = POMARSCPR;

				String sentenciaUpdate = "UPDATE BECOFILE.PRLD01 SET ( PORCOBPRE, " +
						" PORROTPRE, POMARBGPR, POMARNGPR, POMARSCPR, POMARPGPR ) = (" + PORCOBPRE + ","
					+ PORROTPRE + ", " + POMARBGPR + ", " + POMARNGPR + ", " + POMARSCPR + ", "
					+ POMARPGPR  + ") WHERE CODTIE = " + codtie + " AND CODCAT <> 99 AND CODDEP =  "
					+ coddep + " AND CODLIN = " + codlin + " AND CODSEC = " + codsec + " AND AÑOMESPRE = " + añomespre;
				j++;
				loteSentencias.addBatch(sentenciaUpdate); 	
			}
			i = realizarLoteSentencias(loteSentencias).length;
			desconectar();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			desconectar();
			return -1;
		}
	System.out.println("Se realizaron " + j + " lecturas ");
	return i;		
	}
	
	
}
