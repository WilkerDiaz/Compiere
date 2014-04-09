package compiere.model.payments.processes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.As400DbManager;

/**
 * @author Trinimar Acevedo
 * @author ghuchet
 * Proceso que se encarga de exportar los registros de las tablas
 * XX_VCN_Inventory, XX_VCN_InventoryMovDetail y E_XX_VCN_INVD53
 * a los archivos INVM14C, INVD52C e INVD53C del AS400 respectivamente
 **/
public class XX_ExportVCNInventory extends SvrProcess{
	private Calendar cal = Calendar.getInstance();
	private int Year = 0;
	private int Month = 0;
	
	protected String doIt() throws Exception {

		cal.add(Calendar.DATE, -1);
		Year = cal.get(Calendar.YEAR);
		Month = cal.get(Calendar.MONTH)+1;
		
		//Year = 2013;
		//Month = 4;
		
		exportXX_VCN_Inventory();
		exportXX_VCN_InventoryMovDetail();
		//Se exporta a la tabla INVD53C del AS los datos de la tabla E_XX_INVD53C de Compiere
		exportToINVD53C();
		return "";
	}
	
	public int exportXX_VCN_Inventory()
	{
		As400DbManager As = new As400DbManager();
		PreparedStatement ps_Compiere = null;
		ResultSet rs = null;
		Statement sentencia = null;
		String SQL_Compiere="";
		String SQL_AS = "";
		int r = 0;
        int r2 = 0;
        /**Conexión con el AS/400**/
        As.conectar();
        try
        {    
        	/**Borra la data de la tabla de Inventario en el AS/400**/
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	SQL_AS = "DELETE FROM BECOFILE.INVM14C";
            r = As.realizarSentencia(SQL_AS, sentencia);

            if(r>=0) /**Si se borró la tabla correctamente**/
            {
        	   
	        	SQL_Compiere = "SELECT XX_SYNCHRONIZED, XX_VCN_Inventory_ID, NVL(war.value,' ') as TIENDA,"
		                		 + "NVL(cat.value,' ') as CODCAT, NVL(dep.value,' ') as CODDEP, NVL(lin.value,' ') as CODLIN, NVL(sec.value,' ') as CODSEC,"
		                		 + "NVL(pro.value,' ') as CODPRO, " 
		                		 + "(CASE WHEN XX_ConsecutivePrice = 0 THEN TO_NUMBER(NVL(att.Lot,0)) ELSE XX_ConsecutivePrice END) as CONPRE,"  
		                		 + "XX_INVENTORYMONTH as MESINV, XX_INVENTORYYEAR as AÑOINV, "  
		                		 + "XX_INITIALINVENTORYQUANTITY as CANTINVINI, trunc(XX_INITIALINVENTORYAMOUNT, 2) as MONTINVINI,"   
		                		 + "XX_SHOPPINGQUANTITY as CANTCOMPRA, trunc(XX_SHOPPINGAMOUNT, 2) as MONTCOMPRA, XX_SALESQUANTITY as CANTVENTAS, "  
		                		 + "trunc(XX_SALESAMOUNT,2) as MONTVENTAS, XX_MOVEMENTQUANTITY as CANTMOVIMI, trunc(XX_MOVEMENTAMOUNT,2) as MONTMOVIMI FROM "
		                  		 + "XX_VCN_Inventory inv JOIN XX_VMR_DEPARTMENT dep ON (inv.XX_VMR_DEPARTMENT_ID = dep.XX_VMR_DEPARTMENT_ID) "
								 + "JOIN M_WAREHOUSE war ON (inv.M_WAREHOUSE_ID = war.M_WAREHOUSE_ID) "
								 + "JOIN XX_VMR_CATEGORY cat ON (inv.XX_VMR_CATEGORY_ID = cat.XX_VMR_CATEGORY_ID) "
								 + "JOIN XX_VMR_LINE lin ON (inv.XX_VMR_LINE_ID = lin.XX_VMR_LINE_ID) "
								 + "JOIN XX_VMR_SECTION sec ON (inv.XX_VMR_SECTION_ID = sec.XX_VMR_SECTION_ID) "
								 + "JOIN M_PRODUCT pro ON (inv.M_PRODUCT_ID = pro.M_PRODUCT_ID) " 
								 + "LEFT JOIN M_ATTRIBUTESETINSTANCE att ON (att.M_ATTRIBUTESETINSTANCE_ID = inv.M_ATTRIBUTESETINSTANCE_ID) "
								 + "WHERE XX_INVENTORYMONTH = "+Month+" AND XX_INVENTORYYEAR = "+Year;
	        	
	        	try{
					ps_Compiere = DB.prepareStatement(SQL_Compiere,null);
					rs = ps_Compiere.executeQuery();
	        	
					int i=0;
					while(rs.next()){
						i++;
		            	/**Query que permite hacer un registro nuevo en el AS/400**/
			            SQL_AS = "INSERT INTO BECOFILE.INVM14C (TIENDA, CODCAT, CODDEP," 
			                + "CODLIN, CODSEC, CODPRO, CONPRE,"    
			           		+ "MESINV, AÑOINV, CANTINVINI, MONTINVINI,"
			           		+ "CANTCOMPRA, MONTCOMPRA, CANTVENTAS, MONTVENTAS,"  
			           		+ "CANTMOVIMI, MONTMOVIMI) " +
			                "VALUES("
			                    + "'"
			                    + rs.getString("TIENDA") + "','"
			                	+ rs.getString("CODCAT") +"','" 
			                	+ rs.getString("CODDEP") +"','"
			                	+ rs.getString("CODLIN") +"','" 
			                	+ rs.getString("CODSEC") +"','" 
			                	+ rs.getString("CODPRO") +"'," 
			                	+ rs.getInt("CONPRE") +"," 
			                	+ rs.getInt("MESINV") +"," 
			               		+ rs.getInt("AÑOINV") +"," 
			               		+ rs.getString("CANTINVINI") +"," 
			               		+ rs.getString("MONTINVINI") +"," 
			               		+ rs.getString("CANTCOMPRA") +"," 
			               		+ rs.getString("MONTCOMPRA") +","
			                	+ rs.getString("CANTVENTAS") +"," 
			                	+ rs.getString("MONTVENTAS") +"," 
			                	+ rs.getString("CANTMOVIMI") +"," 
			                	+ rs.getString("MONTMOVIMI") 
			                	+")";
			                   
						if(i==9001){
							i = 0;
							As.desconectar();
							As.conectar();
		   			    }
			               			
			            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            r2 = As.realizarSentencia(SQL_AS, sentencia);
			            
			            /**Si se inserto correctamente lo actualizo en Y**/	
						if(r2<0){
//							//Si la inserción da error
			    			return r2;
						}
			        }
	        	}
				catch (Exception e) {
		           	System.out.println("SQL CON ERROR" +SQL_AS);
					e.printStackTrace();
					log.log(Level.SEVERE, e.getMessage());
				}finally{
					rs.close();
					ps_Compiere.close();	
				}
			}
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
        	log.log(Level.SEVERE, e.getMessage());
		}finally{
			try {
				sentencia.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        As.desconectar();
		return r;
	}
	
	
	public int exportXX_VCN_InventoryMovDetail()
	{
		As400DbManager As = new As400DbManager();
		PreparedStatement ps_Compiere = null;
		ResultSet rs = null;
		Statement sentencia = null;
		String SQL_Compiere="";
		String SQL_AS = "";
		int r = 0;
	    int r2 = 0;
	    /**Conexión con el AS/400**/
	    As.conectar();
	    try
	    {    
			/**Borra la data de la tabla de Inventario en el AS/400**/
			sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			SQL_AS = "DELETE FROM BECOFILE.INVD52C";
			r = As.realizarSentencia(SQL_AS, sentencia);

			if(r>=0) /**Si se borró la tabla correctamente**/
			{
				SQL_Compiere = "SELECT XX_SYNCHRONIZED, XX_VCN_InventoryMovDetail_ID, NVL(war.value,' ') as CODTIE,"
						+ "NVL(cat.value,' ') as CODCAT, NVL(dep.value,' ') as CODDEP, NVL(lin.value,' ') as CODLIN, NVL(sec.value,' ') as CODSEC,"
						+ "NVL(pro.value,' ') as CODPRO, " 
						+ "(CASE WHEN XX_ConsecutivePrice = 0 THEN TO_NUMBER(NVL(att.Lot,0)) ELSE XX_ConsecutivePrice END) as CONPRE,"     
						+ "XX_INVENTORYMONTH as MESINV, XX_INVENTORYYEAR as AÑOINV, XX_MOVEMENTTYPE as CODIGOMOV, "  
						+ "XX_MOVEMENTQUANTITY as CANTMOVIM, trunc(XX_MOVEMENTAMOUNT, 2) as MONTOSMOV FROM "   
						+ "XX_VCN_InventoryMovDetail imd JOIN XX_VMR_DEPARTMENT dep ON (imd.XX_VMR_DEPARTMENT_ID = dep.XX_VMR_DEPARTMENT_ID) " 
						+ "JOIN M_WAREHOUSE war ON (imd.M_WAREHOUSE_ID = war.M_WAREHOUSE_ID) "
						+ "JOIN XX_VMR_CATEGORY cat ON (imd.XX_VMR_CATEGORY_ID = cat.XX_VMR_CATEGORY_ID) "
						+ "JOIN XX_VMR_LINE lin ON (imd.XX_VMR_LINE_ID = lin.XX_VMR_LINE_ID) "
						+ "JOIN XX_VMR_SECTION sec ON (imd.XX_VMR_SECTION_ID = sec.XX_VMR_SECTION_ID) "
						+ "JOIN M_PRODUCT pro ON (imd.M_PRODUCT_ID = pro.M_PRODUCT_ID) "
						+ "LEFT JOIN M_ATTRIBUTESETINSTANCE att ON (att.M_ATTRIBUTESETINSTANCE_ID = imd.M_ATTRIBUTESETINSTANCE_ID) "
						+ "WHERE XX_INVENTORYMONTH = "+Month+" AND XX_INVENTORYYEAR = "+Year;
				
				try{
					ps_Compiere = DB.prepareStatement(SQL_Compiere,null);
					rs = ps_Compiere.executeQuery();
					
					int i=0;
					while(rs.next()){
						i++;
						/**Query que permite hacer un registro nuevo en el AS/400**/
						SQL_AS = "INSERT INTO BECOFILE.INVD52C (CODTIE, CODCAT, CODDEP," 
							+ "CODLIN, CODSEC, CODPRO, CONPRE, MESINV,"    
							+ "AÑOINV, CODIGOMOV, CANTMOVIM, MONTOSMOV) " +
							"VALUES("
							+ "'"
							+ rs.getString("CODTIE") + "','"
							+ rs.getString("CODCAT") +"','" 
							+ rs.getString("CODDEP") +"','" 
							+ rs.getString("CODLIN") +"','" 
							+ rs.getString("CODSEC") +"','" 
							+ rs.getString("CODPRO") +"'," 
							+ rs.getInt("CONPRE") +"," 
							+ rs.getString("MESINV") +"," 
							+ rs.getString("AÑOINV") +"," 
							+ rs.getString("CODIGOMOV") +"," 
							+ rs.getString("CANTMOVIM") +","
							+ rs.getString("MONTOSMOV") 
							+")";
									   
						if(i==9001){
							i = 0;
							As.desconectar();
							As.conectar();
						}
						sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						r2 = As.realizarSentencia(SQL_AS, sentencia);
								
						/**Si se inserto correctamente lo actualizo en Y**/
						if(r2<0){						
//							//Si la inserción da error
							return r2; 
						}
					}
				}catch (Exception e) {
						System.out.println("SQL CON ERROR" +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
				}finally{
					rs.close();
					ps_Compiere.close();	
				}
			}
	    }catch (Exception e) {
	      	System.out.println("ERROR");
	       	e.printStackTrace();
	       	log.log(Level.SEVERE, e.getMessage());
	    } finally{
	    	try {
				sentencia.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    As.desconectar();
		return r;
	}

	//Se exporta a la tabla INVD53C del AS los datos de la tabla E_XX_INVD53C de Compiere
	public int exportToINVD53C()
	{
		As400DbManager As = new As400DbManager();
		PreparedStatement ps_Compiere = null;
		ResultSet rs = null;
		Statement sentencia = null;
		String SQL_Compiere="";
		String SQL_AS = "";
		int r = 0;
        int r2 = 0;
        /**Conexión con el AS/400**/
        As.conectar();
        try
        {    
        	/**Borra la data de la tabla de Inventario en el AS/400**/
        	sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	SQL_AS = "DELETE FROM BECOFILE.INVD53C";
            r = As.realizarSentencia(SQL_AS, sentencia);

            if(r>=0) /**Si se borró la tabla correctamente**/
            {
        	   
	        	SQL_Compiere = "\nSELECT M_Warehouse_ID as TIENDA,"
		                		 + "\nXX_VMR_Category_ID as CODCAT, XX_VMR_Department_ID as CODDEP, XX_VMR_Line_ID as CODLIN, XX_VMR_Section_ID as CODSEC,"
		                		 + "\nM_Product_ID as CODPRO, " 
		                		 + "\nXX_ConsecutivePrice as CONPRE,"  
		                		 + "\nXX_INVENTORYMONTH as MESINV, XX_INVENTORYYEAR as AÑOINV, "  
		                		 + "\nXX_STATUS as CODIGOUBI, QTY as CANTUBICA, XX_AMOUNT as MONTOUBIC,"
		                		 + "\nXX_ADJUSTMENTSQUANTITY as CANTAJUST, XX_ADJUSTMENTSAMOUNT as MONTAJUST "   
		                		 + "\nFROM E_XX_VCN_INVD53 inv "
								 + "\nWHERE XX_INVENTORYMONTH = "+Month+" AND XX_INVENTORYYEAR = "+Year;

	        	try{
					ps_Compiere = DB.prepareStatement(SQL_Compiere,null);
					rs = ps_Compiere.executeQuery();
	        	
					int i=0;
					while(rs.next()){
						i++;
						String ANOINV = '"'+"AÑOINV"+'"';
		            	/**Query que permite hacer un registro nuevo en el AS/400**/
			            SQL_AS = "INSERT INTO BECOFILE.INVD53C (TIENDA, CODCAT, CODDEP," 
			                + "CODLIN, CODSEC, CODPRO, CONPRE,"    
			           		+ "MESINV,"+ANOINV+", CODIGOUBI, CANTUBICA, MONTOUBIC, CANTAJUST, MONTAJUST) " +
			                "VALUES("
			                    + "'"
			                    + rs.getString("TIENDA") + "','"
			                	+ rs.getString("CODCAT") +"','" 
			                	+ rs.getString("CODDEP") +"','"
			                	+ rs.getString("CODLIN") +"','" 
			                	+ rs.getString("CODSEC") +"','" 
			                	+ rs.getString("CODPRO") +"'," 
			                	+ rs.getInt("CONPRE") +"," 
			                	+ rs.getInt("MESINV") +"," 
			               		+ rs.getInt("AÑOINV") +"," 
			               		+ rs.getString("CODIGOUBI") +"," 
			               		+ rs.getString("CANTUBICA") +"," 
			               		+ rs.getString("MONTOUBIC") +"," 
			               		+ rs.getString("CANTAJUST") +","
			                	+ rs.getString("MONTAJUST")
			                	+")";
			                   
						if(i==9001){
							i = 0;
							As.desconectar();
							As.conectar();
		   			    }
			               			
			            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            r2 = As.realizarSentencia(SQL_AS, sentencia);
			            
			            /**Si se inserto correctamente lo actualizo en Y**/	
						if(r2<0){
//							//Si la inserción da error
			    			return r2;
						}
			        }
	        	}
				catch (Exception e) {
		           	System.out.println("SQL CON ERROR" +SQL_Compiere);
					e.printStackTrace();
					log.log(Level.SEVERE, e.getMessage());
				}finally{
					rs.close();
					ps_Compiere.close();	
				}
			}
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
        	log.log(Level.SEVERE, e.getMessage());
		}finally{
			try {
				sentencia.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        As.desconectar();
		return r;
	}
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
