package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.common.CompiereSQLException;
import org.compiere.common.CompiereStateException;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProductPrice;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MUser;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_I_Order;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.IOrder;

/**
 * Esta clase contiene todas las funciones necesarias para poder realizar
 * la importación de las ventas que se encuentran actualmente en el AS/400 de BECO
 * a Compiere.
 * 
 * @author Alejandro Prieto	
 * @version 1.2
 */

public class ImportSales extends SvrProcess {
	
	/*manejadora de la base de datos, es decir, es la que va a permitir accesar y modificar
	la información que se ubica tanto en la base de datos del AS/400 como en la de Compiere*/
	//ManejadoraBD m = new ManejadoraBD();
	//static int Cliente = Env.getCtx().getAD_Client_ID();
	//static int Org = Env.getCtx().getAD_Org_ID();
	//static int CreatedBy = Env.getCtx().getAD_Client_ID();
	//static int UpdatedBy = Env.getCtx().getAD_Client_ID();
	static String Description="Venta en caja registradora";
	static Integer FreightAmt = null;
	static String I_IsImported="N";
	static String IsActive="Y";
	static String IsSOtrx="Y";
	private int priceListGlobal=0;
	private Timestamp salesDate = null;
	
	Vector<String> storeCodes = new Vector<String>();
	Vector<Integer> storeIDs = new Vector<Integer>();
	Vector<String> orgCodes = new Vector<String>();
	Vector<Integer> orgIDs = new Vector<Integer>();
	HashMap<String, SaleCustomer> allCustomers = new HashMap<String, SaleCustomer>();
	//static String PaymentRule="S";
	
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 1000012;
	/**	Organization to be imported to		*/
	private int				m_AD_Org_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = true;
	/**	Document Action					*/
	private String			m_docAction = X_C_Order.DOCACTION_Complete;
	
	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=? " ;	
	
	private static final boolean TESTMODE = false;
	
	private static final int	COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	/** Effective						*/
	//private Timestamp		m_DateValue = null;

	
	/*
	 * SELECT RL.AD_REF_LIST_ID, RL.NAME, RL.VALUE, RL.ISACTIVE, 
TO_CHAR(RL.UPDATED,'YYYY-MM-DD') AS UPDATED FROM AD_REF_LIST RL WHERE RL.AD_REFERENCE_ID = 214
AND RL.AD_REF_LIST_ID = 401
	*/
	static String Processed="N";
	Date dateS ; 
	static String Processing="N";
	static String query_I_Order_ID="SELECT CASE WHEN MAX( I_ORDER_ID ) IS NULL THEN " +
	"1000000 ELSE MAX( I_ORDER_ID ) + 1 END As Maximo FROM I_ORDER";
	/**
	 * Ésta función permite extraer la información con respecto a las ventas que se encuentran
	 * en el sistema actual CENTROBECO98.
	 * 
	 * @param date		Fecha en que se va a exportar la data de las ventas.
	 * @param sentencia	Statement con el cual se va a realizar la consulta de las ventas en el AS/400
	 * @return			ResultSet que contiene el conjunto de ventas que se hicieron en la fecha date.
	 */
	public ResultSet ExportOrderDB2(Statement sentencia, As400DbManager As, Timestamp salesDate)
	{
		String sql;
        ResultSet r = null;
        
        As.conectar();
        	
        //System.out.println("From " + dateFrom + " To " + dateTo); 	
        try{
           
            //query que permite hacer la consulta sobre las ventas en el AS/400
            sql = "select  concat(t.numtienda, concat('_',concat(CAST(t.fecha AS varchar(10)), " +
				"concat('_',concat(t.numcajainicia,concat('_',t.numtransaccion)))))) as documentno, " +
				"t.numtienda, t.numcomprobantefiscal, t.codcliente,t.codcajero, t.lineasfacturacion, " +
				"t.serialcaja, t.estadotransaccion, t.codautorizante, t.fecha, " +
				"CAST(SUBSTRING(d.codproducto, 1, LENGTH(d.codproducto)-3) as Integer) AS codproducto, " +
				"SUBSTRING(d.codproducto, LENGTH(d.codproducto)-2, LENGTH(d.codproducto)) AS consecutivo, d.cantidad, " +
				"d.precioregular, d.preciofinal, d.montoimpuesto, d.desctoempleado, d.numcajafinaliza, " +
				"d.numtransaccion, t.tipotransaccion, t.horainicia, t.horafinaliza, tdev.numcomprobantefiscal," +
				"concat(afil.NOMBRE, concat(' ',afil.APELLIDO)), afil.tipoAFILIADO, afil.email, afil.genero, " +
				"afil.estadocivil, afil.registrado, afil.fechanacimiento, afil.numtienda, " +
				"afil.direccion, afil.direccionfiscal, concat(codarea, afil.numtelefono), concat(codarea1, afil.numtelefono1) " +
				"from cr.detalletransaccion d " +
				"join cr.transaccion t ON (t.numtienda = d.numtienda and t.fecha = d.fecha and t.numcajafinaliza = d.numcajafinaliza and t.numtransaccion = d.numtransaccion) " +
				"left join cr.devolucionventa dev ON (dev.NumTiendaDevolucion = t.numtienda and dev.fechaDevolucion = t.fecha and dev.NumCajaDevolucion = t.numcajafinaliza and dev.NumTRANSACCIONDev =  t.numtransaccion) " +
				"left join cr.transaccion tdev ON (tdev.numtienda = dev.numtiendaventa AND tdev.fecha = dev.fechaVenta AND tdev.numcajafinaliza = dev.numcajaVenta AND tdev.numtransaccion =  dev.numtransaccionvta) " +
				"left join cr.afiliado1 afil ON (afil.codafiliado = t.codcliente) " +
				"where " +
				"t.estadotransaccion = 'F' ";
		
				if(salesDate==null)
					sql += "and t.regactualizado= 'N' and t.fecha >= (select current date -1 DAYS FROM SYSIBM.SYSDUMMY1)";
				else
					sql += "and t.fecha = '" + new SimpleDateFormat("yyyy-MM-dd").format(salesDate.getTime()) + "' "; 	
										
            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarConsulta(sql, sentencia);

		}catch (Exception e) {
			e.printStackTrace();
		}
		//As.desconectar();
		return r;
	}
	
	
	public void salesIsImported (Integer warehouse,String fecha, Integer cajaFin, Integer tranc, Integer product, int count, As400DbManager As)
	{
        try
        {
        	String SQL = ("UPDATE cr.transaccion SET regactualizado= 'S' WHERE numtienda = '"+warehouse+"' and  fecha = '"+fecha+"' and numcajafinaliza = '"+cajaFin+"' and numtransaccion = '"+tranc+"' ");
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	sentencia.executeUpdate(SQL);
        	sentencia.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
	}

	/**
	 * Esta función se encarga de buscar el identificador del cliente involucrado en la venta.
	 * 
	 * @param	cliente	nombre del cliente que se está buscando
	 * @return 	identificador del business partner que representa un cliente en el sistema.
	 */
	public int getBPartner(String cliente){
		
		int bpartner = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select c_bpartner_id from c_bpartner where XX_CI_RIF='"+cliente+"'";
		
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			 pstmt = DB.prepareStatement(sql, null);
    		 rs = pstmt.executeQuery();
			if(rs.next()){
				bpartner=rs.getInt(1);
			}
			else{
					rs.close();
					pstmt.close();
					cliente="Standard";
					sql = "select c_bpartner_id from c_bpartner where XX_CI_RIF='"+cliente+"'";
					//rs = m.realizarConsultaOXE(sql,sentencia);
					 pstmt = DB.prepareStatement(sql, null);
		    		 rs = pstmt.executeQuery();
					if(rs.next()){
						bpartner=rs.getInt(1);
					}
					rs.close();
					pstmt.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bpartner;
	}
	
	
	public String getBPartnerV(String cliente){
		
		String bpartner = null;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select value from c_bpartner where XX_CI_RIF='"+cliente+"'";
		
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			 pstmt = DB.prepareStatement(sql, null);
    		 rs = pstmt.executeQuery();
			if(rs.next()){
				bpartner=rs.getString(1);
			}
			else{
				rs.close();
				pstmt.close();
				cliente="Standard";
				sql = "select c_bpartner_id from c_bpartner where XX_CI_RIF='"+cliente+"'";
				//rs = m.realizarConsultaOXE(sql,sentencia);
				pstmt = DB.prepareStatement(sql, null);
		        rs = pstmt.executeQuery();
				if(rs.next()){
					bpartner=rs.getString(1);
				}
			}
			
			rs.close();
			pstmt.close();
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bpartner;
	}
	
	public int getBPLocation(String cliente){
		
		int bplocation = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select B.C_BPartner_Location_ID " +
				     "from c_bpartner A, C_BPartner_Location B " +
				     "where A.XX_CI_RIF='"+cliente+"' AND A.C_BPartner_ID = B.C_BPartner_ID ";
		
		try{
			
			pstmt = DB.prepareStatement(sql, null);
    		rs = pstmt.executeQuery();
			
    		if(rs.next()){
				bplocation=rs.getInt(1);
			}
			
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bplocation;
	}

	
	public String getBPName(String cliente){
		
		String bpartner = null;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select name from c_bpartner where XX_CI_RIF='"+cliente+"'";
		
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			 pstmt = DB.prepareStatement(sql, null);
    		 rs = pstmt.executeQuery();
			if(rs.next()){
				bpartner=rs.getString(1);
			}
			else{
				rs.close();
				pstmt.close();
					cliente="Standard";
					sql = "select name from c_bpartner where XX_CI_RIF='"+cliente+"'";
					//rs = m.realizarConsultaOXE(sql,sentencia);
					 pstmt = DB.prepareStatement(sql, null);
		    		 rs = pstmt.executeQuery();
					if(rs.next()){
						bpartner=rs.getString(1);
					}
			    }
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bpartner;
	}
	
	private int getAgent(){
		
		int agent = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select ad_user_id from ad_user where name='BECO_User'";
		
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			 pstmt = DB.prepareStatement(sql, null);
    		 rs = pstmt.executeQuery();
			if(rs.next()){
				agent=rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return agent;
	}
	
	/**
	 * Busca el país del cliente involucrado en la venta.
	 * 
	 * @param	bpartner	identificador del socio del negocio o cliente.
	 * @return				identificador del país al cual pertenece el cliente.
	 */
	public int getCountry(int bpartner){
		
		int country = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		//query para poder extraer el país del que proviene el cliente, primero se pasa por bpartner
		//luego se relaciona con una ubicación y por último se busca en la tabla de ubicaciones
		String sql = "SELECT l.c_country_id FROM c_bpartner b," +
				"c_bpartner_location bl ,c_location l WHERE b.c_bpartner_id="+bpartner+" " +
				"and b.c_bpartner_id = bl.c_bpartner_id and bl.c_location_id = l.c_location_id";
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				country=rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return country;
	}
	
	/**
	 * Esta función busca el identificador de la región donde está ubicada cada una
	 * de las tiendas de BECO.
	 * 	
	 * @param numtienda	identificador de la tienda
	 * @return			identificador de la región a la cuál pertenece la tienda
	 */
	public int getRegion(int numtienda){
		
		int region = 0;
		//dependiendo del número de tienda se selecciona una u otra región.
		switch (numtienda){
			case 2: case 3: case 7: case 10: case 17: 
				region = 1000000;
				break; 
			case 15:
				region=1000002;
				break;
			case 16:
				region=1000004;
				break;
			case 9:
				region=1000003;
				break;
		}
		return region;
	}
	
	/**
	 * Está función se encarga de buscar la lista de precios que está activa y que es utilizada
	 * para los precios de venta de los productos en Compiere.
	 * 
	 * @return	identificador de la lista de precios de venta.
	 * 
	 * Modificado 22/04/2010 -> para O/V no hace falta Price List por lo que se busca la Standard
	 */
	public int getPriceList(){
		
		int pricelist = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select m_pricelist_id from m_pricelist where IsSOPriceList='Y' and NAME = 'StandardSO'";
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				pricelist = rs.getInt(1);
				priceListGlobal=pricelist;
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pricelist;
	}
	
	/**
	 * Esta función se encarga de traer el identificador del tipo de moneda utilizada para
	 * la lista de precios de las ventas.
	 * 
	 * @param pricelist	identificador de la lista de precios de venta.
	 * @return			identificador del tipo de moneda.
	 */
	public int getCurrency (int pricelist){
		
		int currency = 0;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select c_currency_id from m_pricelist where m_pricelist_id="+pricelist;
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				currency = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return currency;
	}
	
	/**
	 * Esta función busca el tipo de documento que representa una venta en BECO, es decir,
	 * es un tipo de venta realizado en un point of service o punto de servicio.
	 * 
	 * @return	identificador del tipo de documento POS Order.
	 */
	public int getDocType (){
		
		int doctype = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "SELECT DT.C_DOCTYPE_ID, DT.NAME FROM C_DOCTYPE DT WHERE DT.DOCBASETYPE = 'SOO' AND DT.DOCSUBTYPESO = 'WR' AND DT.NAME LIKE 'POS Order' ORDER BY DT.C_DOCTYPE_ID ";
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			
			if(rs.next()){
				doctype = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return doctype;
	}
	
	/**
	 * Esta función busca el tipo de término de pago utilizado para las ventas de BECO, es decir, los 
	 * términos de pago utilizados en las tiendas es inmediato. No hay ningún tipo de crédito.
	 * 
	 * @return	identificador del tipo de término de pago, que en nuestro caso es Immediate.
	 */
	public int getPaymentTerm(){
		
		int doctype = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select c_paymentterm_id from c_paymentterm where Name='Immediate'";
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				doctype = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return doctype;
	}

	
	public String getPaymentRule(){
		
		String paymentRule = null;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		
		String sql = "SELECT RL.VALUE " +
				"FROM AD_REF_LIST RL WHERE RL.AD_REFERENCE_ID = 195 AND NAME = 'EFECTIVO'";

		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				paymentRule = rs.getString(1);
				
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return paymentRule;
	}
	
	/**
	 * Esta función se encarga de buscar el tipo de impuesto utilizado para las ventas de BECO, en nuestro
	 * caso es el IVA, y se obtiene a partir del producto.
	 * 
	 * @param producto	identifiador del producto al cual se le quiere conocer el impuesto
	 * @return			identificador de la tasa tributaria.
	 */
	public int getTax(int producto){
		
		int tax = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select t.c_tax_id from m_product p, c_taxcategory tc, c_tax t where" +
		" p.M_Product_ID='"+producto+"' and p.c_taxcategory_id=tc.c_taxcategory_id and" +
		" tc.c_taxcategory_id=t.c_taxcategory_id";
		//String sql = "select C_TaxCategory_ID from M_Product where M_Product_ID = "+producto;
		//String sql = 
		try{
			pstmt = DB.prepareStatement(sql, null);
			 rs = pstmt.executeQuery();
			if(rs.next()){
				tax = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return tax;
	}
	
	/**
	 * Esta función se encarga de generar un nuevo ID o identificador a cada una de las ventas que se importan
	 * del AS/400 a Compiere.
	 * 
	 * @return	identificador de la orden de venta Importada a la tabla I_Order
	 */
	public int getId_IOrder(){

		int OrderId = 0;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(query_I_Order_ID,sentencia);
			pstmt = DB.prepareStatement(query_I_Order_ID, null);
   		    rs = pstmt.executeQuery();
			if( rs.next() ){
				OrderId = rs.getInt( 1 );
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return OrderId;
	}
	
	/**
	 * Esta función trae el identificador de la tienda donde se produjo la venta, es decir, 
	 * la organización donde se produjo la venta.
	 * 
	 * @param tienda	tienda donde fue hecha la venta
	 * @return			identificador de la organización donde se produjo la venta
	 */
	public int getOrgId (int tienda){
		
		int OrgId = 0;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql="select ad_org_id from ad_org where value='"+tienda+"'";
		try {
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if( rs.next() ){
				OrgId = rs.getInt( 1 );
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return OrgId;
	}
	
	/**
	 * Trae la información del Almacén donde se produjo la venta.
	 * 
	 * @param tienda	número o valor de la tienda donde se produjo la venta.
	 * @return			identificador del almacén.
	 */
	public int getWHId (int tienda, int organization){
		
		int WHId = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		//query que trae el identificador del almacén a partir de la organización que representa la tienda.
		//String tienda2 = "00"+tienda;
		//String sql="select m_warehouse_id from m_warehouse w where value = '"+ tienda2 +"' and IsActive = 'Y'";
		String sql = "SELECT W.M_WAREHOUSE_ID, W.NAME FROM M_WAREHOUSE W WHERE W.AD_ORG_ID = '"+organization+"' AND W.ISACTIVE = 'Y' ";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if( rs.next() ){
				WHId = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return WHId;
	}

	
	private void getAllStores(){
		
		String sql = "SELECT VALUE, M_WAREHOUSE_ID " +
				     "FROM M_WAREHOUSE " +
				     "WHERE UPPER(VALUE) = LOWER(VALUE) " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
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
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllCustomers(){
		
		String sql = "SELECT a.XX_TypePerson||'-'||a.XX_CI_RIF, a.C_BPARTNER_ID, a.name, VALUE, b.c_bpartner_location_ID " +
					 "FROM C_BPartner a join C_BPartner_location b ON (a.C_BPARTNER_ID = b.C_BPARTNER_ID) " +
					 "WHERE a.AD_CLIENT_ID = " + getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
					
				SaleCustomer customer = new SaleCustomer();
				customer.setCustomerId(rs.getInt(2));
				customer.setCustomerName(rs.getString(3));
				customer.setCustomerValue(rs.getString(4));
				customer.setCustomerLocationId(rs.getInt(5));
				allCustomers.put(rs.getString(1), customer);
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private int getStoreID(String cellValue){
		
		if(cellValue==null)
			return 0;
		
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
	
	private void getAllOrgs(){
		
		String sql = "SELECT VALUE, AD_ORG_ID " +
				     "FROM AD_ORG " +
				     "WHERE UPPER(VALUE) = LOWER(VALUE) " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				orgCodes.add(rs.getString("VALUE"));
				orgIDs.add(rs.getInt("AD_ORG_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private int getOrgID(String cellValue){
		
		int index=-1;  // OJO CON ESTO (ACTUALMENTE LAS TIENDAS TIENEN 3 DIGITOS Ejm: 002)
		if(cellValue.length()==1){
			cellValue = "00" + cellValue;
		}else if (cellValue.length()==2) {
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<orgCodes.size(); i++){
			
			if(orgCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return orgIDs.get(index);
		else
			return 0;
	}
	
	public int getTaxid (Timestamp fechaTrans, String productValue )
	{
		Integer taxid = 0;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String year = null;
		String mes = null;
		String dia = null;
		String fecha = null;
		
		fechaTrans.toString();
		
		year = fechaTrans.toString().substring(0, 4);
	    mes = fechaTrans.toString().substring(5, 7);
	    dia = fechaTrans.toString().substring(8, 10);
	    /*System.out.println(year);
	    System.out.println(mes);
	    System.out.println(dia);*/
	    
	    fecha = year+mes+dia;
	    
	    String SQL = ("SELECT  T.C_TAX_ID, T.RATE, VALIDFROM " +
	    		"FROM C_TAX T INNER JOIN C_TAXCATEGORY TC ON " +
	    		"T.C_TAXCATEGORY_ID = TC.C_TAXCATEGORY_ID INNER JOIN M_PRODUCT P ON P.C_TAXCATEGORY_ID = TC.C_TAXCATEGORY_ID " +
	    		"WHERE P.VALUE = '"+productValue+"' AND T.ISACTIVE = 'Y' AND " + 
	    		"T.VALIDFROM  <= (SELECT MAX(VALIDFROM) FROM C_TAX WHERE TO_CHAR(VALIDFROM, 'YYYYMMDD') <= '"+fecha+"'  AND ISACTIVE = 'Y') " +
	    		"ORDER BY T.VALIDFROM DESC ");
	    
	    try
	    {
	    	pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				taxid = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
	    }
	    catch (Exception e) {
	    	System.out.println( e.getMessage() );
			e.printStackTrace();
		} 

		return taxid;
	}
	
	/**
	 * @author 
	 * Trae la informacion de la organizacion de la tienda donde se produjo la venta.
	 * @param tienda
	 * @return Identificador de la organizacion
	 */
	public int getOrganization(Integer tienda)
	{
		Integer Org = 0;
		String tienda2 = null;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		
		if(tienda.toString().length() == 1)
			tienda2 = "00"+tienda;
		else
			tienda2 = "0"+tienda;
		
		String SQL = ("SELECT O.AD_ORG_ID, O.NAME FROM AD_ORG O WHERE O.VALUE = '"+tienda2+"' and IsActive = 'Y'");
		
		try
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				Org = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return Org;
	}
	

	/**
	 * Esta función busca el identificador de la persona o cajero que realizó la venta, que en 
	 * el caso de compiere se denomina representante de ventas.
	 * 
	 * @param salesValue	valor o nombre del usuario que realizó la venta
	 * @return				identificador del representante de ventas o cajero en la tabla AD_User
	 */
	public int getSalesRepId(String salesValue){
		
		int salesRepID = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select ad_user_id from ad_user u,c_bpartner b where " +
				"u.c_bpartner_id=b.c_bpartner_id and u.value='"+salesValue+"'";
		try {
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if( rs.next() ){
				salesRepID = rs.getInt( 1 );
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return salesRepID;
	}
	
	/**
	 * Función utilizada para saber si un año es bisiesto o no.
	 * 
	 * @param year 	año
	 * @return		true si es bisiesto, false si no.
	 */
	public boolean isLeapYear(int year){
		
		if (year % 400 == 0){
			return true;
		}else if (year % 100 == 0){
			return false;
		}else if (year % 4 == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Esta función localiza la versión de la lista de precios utilizada para la venta, ya que
	 * las listas de precios tienen una fecha a partir de la cual empiezan a ser válidas. Y ésta es la que se va
	 * a utilizar a la hora de importar las ventas. 
	 * 
	 * @return	identificador de la versión de la lista de precios para las ventas.
	 */
	public int getPriceListVersion (){
		
		int priceListVersion=0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		int numDays=1000000,difDays;
		String sql = "select plv.m_pricelist_version_id, trunc (sysdate - plv.ValidFrom) " +
				"from m_pricelist pl,m_pricelist_version plv " +
				"where pl.m_pricelist_id=plv.m_pricelist_id and pl.IsSOPriceList='Y'";
		try {
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			while( rs.next() ){
				difDays=rs.getInt(2);
				if(numDays>difDays){
					priceListVersion= rs.getInt(1);
					numDays=difDays;
				} 
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return priceListVersion;
	}
	
	/**
	 * Esta función se encarga de traer la información de los productos relacionados con las ventas, y
	 * que además estos productos deberían estar activos para que la venta se importe correctamente.
	 * La información de los productos es traída de la tabla M_Product.
	 * 
	 * @param product	Código del producto en el AS/400, que es el value que se almacena en la tabla M_Product
	 * 					para cada uno de los productos de Compiere.
	 * @return			identificador del producto en Compiere.
	 */
	public int getProduct(String product){
		
		int productID=0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select m_product_id from m_product where value='"+product+"'";
		try {
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if( rs.next() ){
				productID=rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return productID;
	}
	
	/*
	public double getProductListPrice(String product){
		
		double listPrice=0;
		ResultSet rs = null;
		Statement sentencia=null;
		String sql = "select po.PriceList from m_product p, m_product_po po where " +
				"p.m_product_id=po.m_product_id and p.value='"+product+"'";
		try {
			sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = m.realizarConsultaOXE(sql,sentencia);
			if( rs.next() ){
				listPrice=rs.getDouble(1);
			}
			rs.close();
			sentencia.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}		
		return listPrice;
	}
	*/
	
	/**
	 * Esta función chequea si los precios de los productos involucrados en las ventas pertenecen
	 * a la versión actual de la lista de precios utilizada para las ventas.
	 * 
	 * @param product		identificador del producto
	 * @param priceVersion	identificador de la versión de la lista de precios para las ventas
	 * @return				true si pertenece a la versión, false si no.
	 */
	public boolean checkProductInPP(int product, int priceVersion){
		
		boolean productInPP=true;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select * from m_productprice where m_product_id="+product+" and m_pricelist_version_id="+priceVersion;
		try {
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if( rs.next() ){
				productInPP=false;
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return productInPP;
	}
	
	/**
	 * Como los precios de venta no son insertados en la lista de precios de venta al importar los productos, esto
	 * se tiene que hacer antes de importar las ventas. Así aseguramos que al momento de importar un detalle de la venta
	 * su precio esté dentro de la lista de precios de venta.
	 * 
	 * @param product	identificador del producto o detalle de la venta
	 * @param n			número que lleva el control de los registros insertados en m_productprice con nuevos precios.
	 * @param price		precio de venta regular del Producto producto.
	 * @param lprice	precio de venta final del Producto producto.
	 * @return			número de registros insertados actualizado.
	 */
	/*public int setSalesPrice(int product,int n, double price, double lprice){
		
		int priceVersion=getPriceListVersion();
		//int productID=getProduct(product);
		String insertSalesPrice ="INSERT INTO COMPDES.M_PRODUCTPRICE (AD_CLIENT_ID, AD_ORG_ID," +
				"CREATEDBY, M_PRICELIST_VERSION_ID,M_PRODUCT_ID,PRICELIMIT,PRICELIST,UPDATEDBY) " +
				"VALUES ("+Cliente+","+Org+","+CreatedBy+"" +
				","+priceVersion+","+product+","+lprice+","+price+"" +
				","+UpdatedBy+")";
		int num=n;
		Statement sentenciaOXE = null;
		try {
			sentenciaOXE = m.conexionOXE.createStatement();
			if(checkProductInPP(product,priceVersion)){
				System.out.println("este el PRICE a agregar ...."+insertSalesPrice);
				num++;
				System.out.println("numero de fila "+num);
				m.realizarSentenciaOXE(insertSalesPrice,sentenciaOXE);
			}
			sentenciaOXE.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}	
		return num;
	}*/
	
	public BigDecimal comparePrice (BigDecimal actualp, BigDecimal finalp){
		
		if(finalp.compareTo(actualp)== -1){
			return finalp;
		}
		return actualp;
	}
	
	private SaleCustomer getCustomer(String code){
		
		SaleCustomer customer = null;
		
		if(allCustomers.containsKey(code))
			customer = allCustomers.get(code);
		
		return customer;
	}
	
	private int findCustomerByCI(String ci){
		
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select c_bpartner_id " +
					 "from c_bpartner " +
					 "where XX_TYPEPERSON||'-'||XX_CI_RIF = '" + ci + "'";
		
		try {
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if( rs.next() ){
				return rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return 0;
	}
	
	@Override
	protected String doIt() throws Exception {
		
		//Borrado de los registros del mes
		Date actualDate= new Date();
		System.out.println("Comenzando a las: " + actualDate.toString());
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;
		
		//Delete I_Order
		if (salesDate!=null)
		{
			sql = new StringBuffer ("DELETE FROM I_Order "
				  + "WHERE isActive='Y'").append (clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.fine("Delete I_Order =" + no);
		}
		
		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_Order "
				  + "WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.fine("Delete Old Impored =" + no);
		}
		this.commit();
		
		Integer bPartnerId, priceList, WHID, taxReceipt, originalTaxReceipt, organization, currency, taxRate, consecutivo, 
				cajaFinaliza, transaccion, bPLocationId;
		//double descEmpleado;
		BigDecimal priceActual, precioFinal, qtyOrdered, taxAmount, EmployeeDiscount;
		String tipoTransaccion, documentNo, productValue, taxSerial, bPartnerValue, bPName;
		int currencyAux = 0;
		Timestamp fecha;
		String horaInicia;
		String horaFinaliza;
		ResultSet rs=null;
		PreparedStatement sentencia = null;
		IOrder IOrder = null;
		As400DbManager As = new As400DbManager();
		Trx Transaccion = Trx.get("Transaccion");
		Boolean aux = false;
		int paymentTermAux;
		
		try{
			
			rs=ExportOrderDB2(sentencia, As, salesDate);
			
			int count = 1;
			int counter=0;
			
			//Inmediate
			paymentTermAux = getPaymentTerm();
			
			//Variables fijas, CbPartner Standard
			int bPartnerAux = getBPartner("0");
			String bPartnerValueAux = getBPartnerV("0");
			int bPLocationAux = getBPLocation("Standard");
			String bPNameAux = getBPName("0");
			
			// Lista de Precios
			priceList = getPriceList();
			
			//Moneda
			currencyAux =  getCurrency(priceList);
			
			//se cargan todas la tiendas y organizaciones
			getAllStores();
			getAllOrgs();
			getAllCustomers();
			
			// Agente
			int agent = getAgent();
			int createdLocation = 0;
			
			while(rs.next()){
				
				createdLocation = 0;
				counter++;
				if(counter%200==0){
					System.out.println("Cantidad de registros:" + counter);
				}
				
				//Bpartner
				try{
					
					//Colocar en este if el campo de contribuyente TODO
					if(rs.getString(4)!=null && !rs.getString(4).startsWith("N") && rs.getString(24)!=null && !rs.getString(24).trim().equals("")){
						
						SaleCustomer customer =  getCustomer(rs.getString(4));
						
						if(customer!=null)
						{
							bPartnerId = customer.getCustomerId();
							bPartnerValue = customer.getCustomerValue();
							bPLocationId = customer.getCustomerLocationId();
							bPName = customer.getCustomerName();
						}
						else{
								
							//New Customer
							compiere.model.cds.MBPartner partner = new compiere.model.cds.MBPartner(Env.getCtx(), 0, null);
							partner.setName(rs.getString(24));
							partner.setXX_TypePerson(rs.getString(25));
							if(rs.getString(26)!=null)
								partner.setXX_Email(rs.getString(26));
							if(rs.getString(27)!=null)
								partner.setXX_Genero(rs.getString(27));
							if(rs.getString(28)!=null)
								partner.setXX_EstadoCivil(rs.getString(28));
							if(rs.getString(29)!=null)
								partner.setXX_Registrado(rs.getString(29));
							if(rs.getString(30)!=null)
								partner.setXX_FechaNac(rs.getTimestamp(30));
							if(rs.getString(31)!=null && getStoreID(rs.getString(31))>0)
								partner.setM_Warehouse_ID(getStoreID(rs.getString(31)));
							
							String rif = "";
							rif = rs.getString(4).substring(2);
							partner.setXX_CI_RIF(rif);
							partner.setIsCustomer(true);
							
							int auxPartner = -1;
							if(!partner.save())
								auxPartner = findCustomerByCI(rs.getString(4));
							
							if(auxPartner == 0)
								continue;
							else if(auxPartner > 0){
								
								partner = new compiere.model.cds.MBPartner(Env.getCtx(), auxPartner, null);
								createdLocation = getBPLocation(partner.getXX_CI_RIF());
							}
							
							//Customer Location
							if(createdLocation == 0){
								MLocation location = new MLocation(Env.getCtx(), 0, null);
								
								if(rs.getString(32)!=null){
									String[] address = rs.getString(32).split(">");
									
									for(int i=0; i<address.length; i++){
										
										if(i==0)
											location.setAddress1(address[i]);
										if(i==1)
											location.setAddress2(address[i]);
										if(i==2)
											location.setAddress2(location.getAddress2() + " " + address[i]);
										if(i==3)
											location.setAddress3(address[i]);
										if(i==4)
											location.setAddress3(location.getAddress3() + " " + address[i]);
										if(i==5)
											location.setAddress4(address[i]);
										
										if(i==(address.length-1))
											location.setAddress4((location.getAddress4() + " " + rs.getString(33)).trim());
									}
									
								}
								
								location.setC_Country_ID(339); //Venezuela
								location.save();
								
								MBPartnerLocation bpLocation = new MBPartnerLocation(Env.getCtx(), 0, null);
								
								if(rs.getString(34)!=null)
									bpLocation.setPhone(rs.getString(34));
								if(rs.getString(35)!=null)
									bpLocation.setPhone2(rs.getString(35));
								
								bpLocation.setC_Location_ID(location.get_ID());
								bpLocation.setC_BPartner_ID(partner.get_ID());
								bpLocation.save();
								
								bPLocationId = bpLocation.get_ID();
							}
							else
								bPLocationId = createdLocation;
							
							bPartnerId = partner.get_ID();
							bPartnerValue = partner.getValue();
							bPName = partner.getName();
						}
					}
					else
					{	
						bPartnerId = bPartnerAux;
						bPartnerValue = bPartnerValueAux;
						bPLocationId = bPLocationAux;
						bPName = bPNameAux;
					}
				}
				catch(Exception e){
					
					System.out.println("Error con afiliado: " + e.getMessage());
					continue;
				}
	
				
				// Moneda
				currency =  currencyAux;
				
				// Document Number
				documentNo = rs.getString(1);
				
				//Organizacion
				organization = getOrgID(rs.getString(2));
				
				// Tienda
				WHID = getStoreID(rs.getString(2));
				
				taxReceipt=rs.getInt(3);
				taxSerial=rs.getString(7);
				
				// Fecha Trans
				fecha= new Timestamp(rs.getDate(10).getTime());
									    
				// Product Value
				productValue=rs.getString(11);
				
				// Consecutivo de Precio
				consecutivo = rs.getInt(12);
				
				//Tipo de transaccion (V,D,A)
				tipoTransaccion = rs.getString(20);
				
				// Cantidad Ordenada
				if(tipoTransaccion.equals("V"))
					qtyOrdered= new BigDecimal(rs.getDouble(13));
				else
					qtyOrdered= new BigDecimal(rs.getDouble(13)).multiply(new BigDecimal(-1));
				
				// AS Precio Regular.
				priceActual= new BigDecimal(rs.getDouble(14));
				
				// AS Precio Final (Con la suma de descuento empleado lo trae desde el query)
				precioFinal= new BigDecimal(rs.getDouble(15));
				
				// TAX
				// CAMBIAR EL QUERY (HAY QUE COLOCAR EL VALUE DEL PRODUCTO)
				taxRate = getTaxid(fecha, productValue); 
				
				taxAmount= new BigDecimal(rs.getDouble(16));
				
				// AS Descuento Empleado
				EmployeeDiscount = new BigDecimal(rs.getDouble(17));
				
				//Caja final
				cajaFinaliza = rs.getInt(18); 
				
				//Transaccion
				transaccion = rs.getInt(19);
				
				horaInicia = rs.getString(21);
				horaFinaliza = rs.getString(22);
				
				//Numero Fiscal
				originalTaxReceipt = null;
				if(rs.getInt(23)>0)
					originalTaxReceipt = rs.getInt(23);
				
				//se agregan los precios de venta a la tabla que mantiene la relación producto-precio de venta,
				//para que a la hora de importar la venta, se consiga el precio de venta a asociar.
				
				IOrder = new IOrder(getCtx(),0,Transaccion);
				
				//========================================
				//        Insertar orden de venta
				//========================================
				
				// Organizacion segun tienda
				IOrder.setAD_Org_ID(organization);
				
				// Document type (Tipo de Documento) 
				IOrder.setC_DocType_ID(getDocType());
				
				// Document No
				IOrder.setDocumentNo(documentNo);
				
				// Descripcion
				IOrder.setDescription(Description);
				
				//agente
				IOrder.setSalesRep_ID(agent);
				
				// Representative (Cajero) -> Proximamente
				//IOrder.setSalesRep_ID(getSalesRepId(codCajero));
				
				//Currency
				IOrder.setC_Currency_ID(currency);
				
				// Price List
				IOrder.setM_PriceList_ID(priceList);
				
				// Payment Method
				//IOrder.set_ValueNoCheck("PaymentRule",paymentRuleAux);
				
				//Payment Term (Condicion de pago) -> no se encuentra importado 
				
				IOrder.setC_PaymentTerm_ID(paymentTermAux);
			
				// WAREHOUSE
				IOrder.setM_Warehouse_ID(WHID);
				
				// BPartner
				IOrder.setC_BPartner_ID(bPartnerId);
				IOrder.setBPartnerValue(bPartnerValue);
				IOrder.setC_BPartner_Location_ID(bPLocationId);
				IOrder.setName(bPName);
				
				//Country (Venezuela)
				IOrder.setC_Country_ID(339);
				
				// Date Ordered
				IOrder.setDateOrdered(fecha);
				
				//Account Date
				IOrder.setDateAcct(fecha);
				
				// Product Key
				IOrder.setProductValue(productValue); // hay que pasarle productValue
				
				// Base Price -> Precio Regular AS
				IOrder.setXX_BasePrice(priceActual.setScale(2, BigDecimal.ROUND_HALF_UP));
				
				// Unit Price -> Con descuento empleado
				IOrder.setPriceActual(precioFinal.setScale(2, BigDecimal.ROUND_HALF_UP));
				
				IOrder.setXX_EmployeeDiscount(EmployeeDiscount.setScale(2,BigDecimal.ROUND_HALF_UP));
				
				// Tax Rate taxRate
				IOrder.setC_Tax_ID(taxRate);
				
				// Tax Amount
				IOrder.setTaxAmt(taxAmount);
				
				// tax receipt
				IOrder.setXX_TaxReceipt(taxReceipt);
				
				// Tax Serial
				IOrder.setXX_TaxSerial(taxSerial);
				
				// Consecutivo
				IOrder.setXX_PriceConsecutive(consecutivo);
				
				// ID del Producto
				//IOrder.setM_Product_ID(productID);
				
				// Lote
				//IOrder.setM_AttributeSetInstance_ID(lot);
				
				//IOrder.setC_Tax_ID(getTax(productID));
				
				IOrder.setXX_SaleType(tipoTransaccion);
				
				//Hora inicio y fin de la transaccion
				IOrder.setXX_StartTime(horaInicia);
				IOrder.setXX_EndTime(horaFinaliza);
				
				//Original tax receipt (dev)
				if(originalTaxReceipt!=null)
					IOrder.setXX_OrigTaxReceipt(originalTaxReceipt);
				
				//Machine No
				if(cajaFinaliza!=null)
					IOrder.setXX_MachineNo(cajaFinaliza);
				
				//Transaction No
				if(transaccion!=null)
					IOrder.setXX_TransactionNo(transaccion);
		
				IOrder.setQtyOrdered(qtyOrdered);
				
				IOrder.setI_IsImported(I_IsImported);
				IOrder.setIsActive(true);
				IOrder.setIsSOTrx(true);
				IOrder.setProcessed(false);
				IOrder.setProcessing(false);

				if(!IOrder.save()){
					aux = true;
					Transaccion.rollback();
				}
			
			}// end while
		
		Transaccion.commit();
		
		rs.first();
		rs.beforeFirst();
	
		// Despues si de guardadas las ordenes de Ventas en la I_Order se llama al proceso de update 
		
		if(aux == false)
		{
			System.out.println("Ahora se sincroniza las ventas en 'S' en el AS");
			while(rs.next())
			{				
				//fecha= new Timestamp(rs.getDate(10).getTime());
				//Caja final
				cajaFinaliza = rs.getInt(18);
				
				//Fecha
				dateS = new Timestamp(rs.getDate(10).getTime());
				
				//Transaccion
				transaccion = rs.getInt(19);
				salesIsImported(rs.getInt(2), dateS.toString().substring(0, 10), cajaFinaliza, transaccion, rs.getInt(11), count, As);
				
			}
		}
		rs.close();
		//salesImportedDate();
		//sentencia.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		//m.desconectar();
		//m.desconectarOXE();
		
		System.out.println("Lista la colocacion en la i");
		
		System.out.println("Lista la colocacion en la i, ahora se va a pasar de la I_ORDER a la C_ORDER");
		
		String auxImportOrder = importOrder();
		actualDate= new Date();
		System.out.println("Listo el pase a C_Order " + actualDate.toString());
		
		System.out.println("Finalizando a las: " + actualDate.toString());
		
		return "Import Completed \n" + auxImportOrder;
	}


	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Date"))
				salesDate = (Timestamp) element.getParameter();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	/*
	private Integer getLot(int productID, int consec){
		
		Integer aux = 0;
		
		String SQL=  "SELECT " +
					 "CASE " +
					 "WHEN M_ATTRIBUTESETINSTANCE_ID IS NULL THEN 0 " +
					 "ELSE M_ATTRIBUTESETINSTANCE_ID " +
				 	 "END M_ATTRIBUTESETINSTANCE_ID " +
					 "FROM XX_VMR_PRICECONSECUTIVE " +
					 "WHERE M_PRODUCT_ID = " + productID + " and XX_PRICECONSECUTIVE = " + consec;
	
		try{
	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
	
			while(rs.next()){
				aux = rs.getInt(1);			
			}
			
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			log.log(Level.SEVERE,SQL + " " + e.getMessage());
		}
		
		return aux; 
	}
	*/
	
	private void addToPriceList(){
		
		String SQL=  "SELECT M_PRODUCT_ID FROM M_PRODUCT " 
				   + "WHERE " 
				   + "M_PRODUCT_ID NOT IN (SELECT M_PRODUCT_ID FROM M_PRODUCTPRICE WHERE M_PRICELIST_VERSION_ID="
				   					    +"(SELECT M_PRICELIST_VERSION_ID FROM M_PRICELIST_VERSION WHERE M_PRICELIST_ID="+priceListGlobal+")) "
				   + "AND "
				   + "M_PRODUCT_ID IN (SELECT M_PRODUCT_ID FROM I_ORDER)";
		
		try{

			PreparedStatement pstmt = DB.prepareStatement(SQL, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			MProductPrice pp = null;

			int plv = getPriceListVersionStandard();
			while(rs.next()){
				
				pp = new MProductPrice(getCtx(), 0, null);

				pp.setM_PriceList_Version_ID(plv);
				pp.setM_Product_ID(rs.getInt(1));
				
				if(!pp.save())
					System.out.println("Product: "+ rs.getInt(1));				
			
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			log.log(Level.SEVERE,SQL + " " + e.getMessage());
		}
	}
	
	public int getPriceListVersionStandard(){
		
		int priceListVersion = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "select M_PRICELIST_VERSION_ID " +
					 "from M_PRICELIST_VERSION " +
					 "where M_PRICELIST_ID=" + priceListGlobal;
		try{
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				priceListVersion = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return priceListVersion;
	}
	
	/**
	 * Pasa de la I_Order a la C_Order Completando automaticamente
	 */
	private String importOrder() throws java.lang.Exception{
		String sql = null;
		int no = 0;
		Ctx ctx = getCtx();

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = "DELETE FROM I_Order "
				  + "WHERE (I_IsImported='Y' OR I_ERRORMSG = 'ERR=Duplicate Document No, ') "
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}
		
		System.out.println("borro de i_order");

		//	Set Client, Org, IsActive, Created/Updated
		sql = "UPDATE I_Order "
			  + "SET AD_Client_ID = COALESCE (AD_Client_ID,?), "
			  + " AD_Org_ID = COALESCE (AD_Org_ID,?), "
			  + " IsActive = COALESCE (IsActive, 'Y'), "
			  + " Created = COALESCE (Created, SysDate), "
			  + " CreatedBy = COALESCE (CreatedBy, 0), "
			  + " Updated = COALESCE (Updated, SysDate), "
			  + " UpdatedBy = COALESCE (UpdatedBy, 0), "
			  + " I_ErrorMsg = NULL, "
			  + " I_IsImported = 'N' "
			  + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL";
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID, m_AD_Org_ID);
		log.info ("Reset=" + no);

		String ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg";  //java bug, it could not be used directly
		sql = "UPDATE I_Order o "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Org, '"
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Org=" + no);

		//	Document Type - PO - SO
		sql = "UPDATE I_Order o "	//	PO Document Type Name
			  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName"
			  + " AND d.DocBaseType='POO' AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set PO DocType=" + no);
		
		sql = "UPDATE I_Order o "	//	SO Document Type Name
			  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName"
			  + " AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		 no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		 log.fine("Set SO DocType=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=o.DocTypeName"
			  + " AND d.DocBaseType IN ('SOO','POO') AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set DocType=" + no);
		
		sql = "UPDATE I_Order "	//	Error Invalid Doc Type Name
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid DocTypeName, ' "
			  + "WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid DocTypeName=" + no);
		
		//	DocType Default
		sql = "UPDATE I_Order o "	//	Default PO
			  + "SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'"
			  + " AND d.DocBaseType='POO' AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='N' AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set PO Default DocType=" + no);
		
		sql = "UPDATE I_Order o "	//	Default SO
			  + "SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'"
			  + " AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set SO Default DocType=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET C_DocType_ID=(SELECT MAX(C_DocType_ID) FROM C_DocType d WHERE d.IsDefault='Y'"
			  + " AND d.DocBaseType IN('SOO','POO') AND o.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND IsSOTrx IS NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Default DocType=" + no);
		
		sql = "UPDATE I_Order "	// No DocType
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No DocType, ' "
			  + "WHERE C_DocType_ID IS NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("No DocType=" + no);

		//	Set IsSOTrx
		sql = "UPDATE I_Order o SET IsSOTrx='Y' "
			  + "WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID)"
			  + " AND C_DocType_ID IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set IsSOTrx=Y=" + no);
		
		sql = "UPDATE I_Order o SET IsSOTrx='N' "
			  + "WHERE EXISTS (SELECT * FROM C_DocType d WHERE o.C_DocType_ID=d.C_DocType_ID AND d.DocBaseType='POO' AND o.AD_Client_ID=d.AD_Client_ID)"
			  + " AND C_DocType_ID IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set IsSOTrx=N=" + no);

		//	Price List
		sql = "UPDATE I_Order o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'"
			  + " AND p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Default Currency PriceList=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'"
			  + " AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Default PriceList=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p "
			  + " WHERE p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Currency PriceList=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p "
			  + " WHERE p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_PriceList_ID IS NULL AND C_Currency_ID IS NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set PriceList=" + no);
		
		//
		sql = "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No PriceList, ' "
			  + "WHERE M_PriceList_ID IS NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning("No PriceList=" + no);
	
		//	Payment Rule
		//  We support Payment Rule being input in the login language 
		Language language = Language.getLoginLanguage();		//	Base Language
		String AD_Language = language.getAD_Language();
		sql = "UPDATE I_Order O " + 
				"SET PaymentRule= " +
			  	"(SELECT R.value "+
			  	"  FROM AD_Ref_List R " + 
			  	"  left outer join AD_Ref_List_Trl RT " + 
			  	"  on RT.AD_Ref_List_ID = R.AD_Ref_List_ID and RT.AD_Language = ? " +
			  	"  WHERE R.AD_Reference_ID = 195 and coalesce( RT.Name, R.Name ) = O.PaymentRuleName ) " +
			    "WHERE PaymentRule is null AND PaymentRuleName IS NOT NULL AND I_IsImported<>'Y'" +
			  	STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {AD_Language, m_AD_Client_ID});
		log.fine("Set PaymentRule=" + no);
		// do not set a default; if null, the import logic will derive from the business partner
		// do not error in absence of a default

		//	Payment Term
		sql = "UPDATE I_Order o "
			  + "SET C_PaymentTerm_ID=(SELECT C_PaymentTerm_ID FROM C_PaymentTerm p"
			  + " WHERE o.PaymentTermValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE C_PaymentTerm_ID IS NULL AND PaymentTermValue IS NOT NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set PaymentTerm=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET C_PaymentTerm_ID=(SELECT MAX(C_PaymentTerm_ID) FROM C_PaymentTerm p"
			  + " WHERE p.IsDefault='Y' AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE C_PaymentTerm_ID IS NULL AND o.PaymentTermValue IS NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Default PaymentTerm=" + no);
		//
		sql = "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No PaymentTerm, ' "
			  + "WHERE C_PaymentTerm_ID IS NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("No PaymentTerm=" + no);
		
		//	Warehouse
		// if Warehouse key provided, get Warehouse ID			
		sql = "UPDATE I_Order o "
				+ "SET M_Warehouse_ID=(SELECT MAX(M_Warehouse_ID) FROM M_Warehouse w"
				+ " WHERE o.WarehouseValue=w.Value AND o.AD_Client_ID=w.AD_Client_ID AND o.AD_Org_ID = w.AD_Org_ID) "
				+ "WHERE M_Warehouse_ID IS NULL AND WarehouseValue IS NOT NULL"
				+ " AND I_IsImported<>'Y'"
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.fine("Set Warehouse from Value =" + no);

		sql = "UPDATE I_Order o "
			  + "SET M_Warehouse_ID=(SELECT MAX(M_Warehouse_ID) FROM M_Warehouse w"
			  + " WHERE o.AD_Client_ID=w.AD_Client_ID AND o.AD_Org_ID=w.AD_Org_ID) "
			  + "WHERE M_Warehouse_ID IS NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.fine("Set Warehouse=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET M_Warehouse_ID=(SELECT M_Warehouse_ID FROM M_Warehouse w"
			  + " WHERE o.AD_Client_ID=w.AD_Client_ID) "
			  + "WHERE M_Warehouse_ID IS NULL"
			  + " AND EXISTS (SELECT AD_Client_ID FROM M_Warehouse w WHERE w.AD_Client_ID=o.AD_Client_ID GROUP BY AD_Client_ID HAVING COUNT(*)=1)"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.fine("Set Only Client Warehouse=" + no);
		
		//
		sql = "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No Warehouse, ' "
			  + "WHERE M_Warehouse_ID IS NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("No Warehouse=" + no);
		
		sql = "UPDATE I_Order o "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Warehouse, ',M_Warehouse_ID = NULL "
			  + "WHERE o.WarehouseValue IS NOT NULL AND NOT EXISTS (SELECT 1 FROM M_Warehouse w "
			  + "WHERE o.WarehouseValue=w.Value AND o.AD_Client_ID=w.AD_Client_ID AND o.AD_Org_ID = w.AD_Org_ID) "
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Warehouse=" + no);
		
		//	BP from EMail
		sql = "UPDATE I_Order o "
			  + "SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u"
			  + " WHERE o.EMail=u.EMail AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) "
			  + "WHERE C_BPartner_ID IS NULL AND EMail IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set BP from EMail=" + no);
		
		//	BP from ContactName
		sql = "UPDATE I_Order o "
			  + "SET (C_BPartner_ID,AD_User_ID)=(SELECT C_BPartner_ID,AD_User_ID FROM AD_User u"
			  + " WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL) "
			  + "WHERE C_BPartner_ID IS NULL AND ContactName IS NOT NULL"
			  + " AND EXISTS (SELECT Name FROM AD_User u WHERE o.ContactName=u.Name AND o.AD_Client_ID=u.AD_Client_ID AND u.C_BPartner_ID IS NOT NULL GROUP BY Name HAVING COUNT(*)=1)"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set BP from ContactName=" + no);

		//	BP from Value
		sql = "UPDATE I_Order o "
			  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
			  + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set BP from Value=" + no);
		
		//	Default BP
		sql =  "UPDATE I_Order o "
			  + "SET C_BPartner_ID=(SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo c"
			  + " WHERE o.AD_Client_ID=c.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NULL AND Name IS NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Default BP=" + no);
		
		get_Trx().commit();

		//	Existing Location ? Exact Match
		sql =  "UPDATE I_Order o "
			  + "SET (BillTo_ID,C_BPartner_Location_ID)=(SELECT C_BPartner_Location_ID,C_BPartner_Location_ID"
			  + " FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)"
			  + " WHERE o.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=o.AD_Client_ID"
			  + " AND DUMP(o.Address1)=DUMP(l.Address1) AND DUMP(o.Address2)=DUMP(l.Address2)"
			  + " AND DUMP(o.City)=DUMP(l.City) AND DUMP(o.Postal)=DUMP(l.Postal)"
			  + " AND DUMP(o.C_Region_ID)=DUMP(l.C_Region_ID) AND DUMP(o.C_Country_ID)=DUMP(l.C_Country_ID)) "
			  + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			  + " AND I_IsImported='N'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Found Location=" + no);
		//	Set Bill Location from BPartner
		sql =  "UPDATE I_Order o "
			  + "SET BillTo_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l"
			  + " WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID"
			  + " AND ((l.IsBillTo='Y' AND o.IsSOTrx='Y') OR (l.IsPayFrom='Y' AND o.IsSOTrx='N'))"
			  + ") "
			  + "WHERE C_BPartner_ID IS NOT NULL AND BillTo_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set BP BillTo from BP=" + no);
		
		//	Set Location from BPartner
		sql =  "UPDATE I_Order o "
			  + "SET C_BPartner_Location_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l"
			  + " WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID"
			  + " AND ((l.IsShipTo='Y' AND o.IsSOTrx='Y') OR o.IsSOTrx='N')"
			  + ") "
			  + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set BP Location from BP=" + no);
		//
		sql =  "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No BP Location, ' "
			  + "WHERE C_BPartner_ID IS NOT NULL AND (BillTo_ID IS NULL OR C_BPartner_Location_ID IS NULL)"
			  + " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning ("No BP Location=" + no);

		get_Trx().commit();
		
		// Check for Duplicate Document Number/BP/Doc Type Combinations
		sql =  "UPDATE I_Order i "
				  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Duplicate Document No, ' "
				  + "WHERE EXISTS (SELECT 1 FROM C_Order o WHERE "
				  + " o.DocumentNo = i.DocumentNo)"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Duplicate Document Number=" + no);
			
		sql =  "UPDATE I_Order o "
			  + "SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c"
			  + " WHERE o.CountryCode=c.CountryCode AND c.IsSummary='N' AND c.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL AND CountryCode IS NOT NULL"
			  + " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Country=" + no);
		
		//
		sql =  "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Country, ' "
			  + "WHERE C_BPartner_ID IS NULL AND C_Country_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Country=" + no);
	
		//	Set Region
		sql =  "UPDATE I_Order o "
			  + "Set RegionName=(SELECT MAX(Name) FROM C_Region r"
			  + " WHERE r.IsDefault='Y' AND r.C_Country_ID=o.C_Country_ID"
			  + " AND r.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NULL"
			  + " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Region Default=" + no);
		//
		sql =  "UPDATE I_Order o "
			  + "Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r"
			  + " WHERE r.Name=o.RegionName AND r.C_Country_ID=o.C_Country_ID"
			  + " AND r.AD_Client_ID IN (0, o.AD_Client_ID)) "
			  + "WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL AND RegionName IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Region=" + no);
		//
		sql =  "UPDATE I_Order o "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Region, ' "
			  + "WHERE C_BPartner_ID IS NULL AND C_Region_ID IS NULL "
			  + " AND EXISTS (SELECT * FROM C_Country c"
			  + " WHERE c.C_Country_ID=o.C_Country_ID AND c.HasRegion='Y')"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Region=" + no);

		//	Product
		sql =  "UPDATE I_Order o "
			  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
			  + " WHERE o.ProductValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Product from Value=" + no);
		
		sql =  "UPDATE I_Order o "
			  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
			  + " WHERE o.UPC=p.UPC AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_Product_ID IS NULL AND UPC IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Product from UPC=" + no);
		
		sql =  "UPDATE I_Order o "
			  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
			  + " WHERE o.SKU=p.SKU AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_Product_ID IS NULL AND SKU IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Product fom SKU=" + no);
		
		sql =  "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Product, ' "
			  + "WHERE M_Product_ID IS NULL AND (ProductValue IS NOT NULL OR UPC IS NOT NULL OR SKU IS NOT NULL)"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Product=" + no);
		
		//Las ordenes de los productos no encontrados las colocamos en E para no importar la venta completa
		Date actualDate = new Date();
		System.out.println("Ahora las ordenes de los productos no encontrados las colocamos en E para no importar la venta completa " + actualDate.toString());
		sql =  "UPDATE I_Order "
				  + "SET I_IsImported='E' "
				  + "WHERE DOCUMENTNO IN (select DOCUMENTNO from i_order where I_IsImported='E')"
				  + "" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set I_IsImported='E' for all de OS =" + no);
		
		actualDate = new Date();
		System.out.println("Ahora creamos los productos en la lista de precios " + actualDate.toString());
		addToPriceList();
		
		actualDate = new Date();
		System.out.println("Ahora se indica el lote correspondiente " + actualDate.toString());
		
		get_Trx().commit();
		//Lote BECO
		sql =  "UPDATE I_Order o "
				  + "SET M_ATTRIBUTESETINSTANCE_ID="
				  + "(SELECT MIN(CASE "
				  + "WHEN M_ATTRIBUTESETINSTANCE_ID IS NULL THEN 0 "
				  +	"ELSE M_ATTRIBUTESETINSTANCE_ID "
				  + "END) M_ATTRIBUTESETINSTANCE_ID "
				  + "FROM XX_VMR_PRICECONSECUTIVE pc "
				  + "WHERE o.M_PRODUCT_ID = pc.M_PRODUCT_ID AND o.XX_PRICECONSECUTIVE = pc.XX_PRICECONSECUTIVE "
				  + "AND o.AD_Client_ID=pc.AD_Client_ID) "
				  + "WHERE o.M_Product_ID IS NOT NULL AND o.XX_PRICECONSECUTIVE IS NOT NULL "
				  + "AND I_IsImported<>'Y' " + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set LOT from Product=" + no);
		
		/*sql = new StringBuffer ("UPDATE I_Order o "
				  + "SET M_ATTRIBUTESETINSTANCE_ID = NULL "
				  + "WHERE o.M_ATTRIBUTESETINSTANCE_ID = 0 "
				  + "AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set LOT to 0 from Product=" + no);
		*/
		
		actualDate = new Date();
		System.out.println("Listo seteo de lotes " + actualDate.toString());
		//Fin BECO

		
		
		// Charge
		sql =  "UPDATE I_Order o "
				  + "SET C_Charge_ID=(SELECT MAX(C_Charge_ID) FROM C_Charge c"
				  + " WHERE o.ChargeName=c.Name AND o.AD_Client_ID=c.AD_Client_ID) "
				  + "WHERE C_Charge_ID IS NULL AND ChargeName IS NOT NULL"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Charge from Name=" + no);

		sql =  "UPDATE I_Order "
				  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Charge, ' "
				  + "WHERE C_Charge_ID IS NULL AND (ChargeName IS NOT NULL)"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Charge=" + no);

		sql =  "UPDATE I_Order "
				  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Both Charge and Product are specified, ' "
				  + "WHERE C_Charge_ID IS NOT NULL AND M_Product_ID IS NOT NULL"
				  + " AND I_IsImported<>'Y'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Charge and Product are specified=" + no);
			
		//	
		sql =  "UPDATE I_Order o "
			  + "SET C_Tax_ID=(SELECT MAX(C_Tax_ID) FROM C_Tax t"
			  + " WHERE o.TaxIndicator=t.TaxIndicator AND o.AD_Client_ID=t.AD_Client_ID) "
			  + "WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set Tax=" + no);
		
		sql =  "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Tax, ' "
			  + "WHERE C_Tax_ID IS NULL AND TaxIndicator IS NOT NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Tax=" + no);

		commit();

		
		//	-- New BPartner ---------------------------------------------------
		sql =  "UPDATE I_Order "
				  + "SET BPartnerValue = COALESCE(EMail,Name) " 
				  + "WHERE C_BPartner_ID IS NULL "
				  + "AND BPartnerValue IS NULL "
				  + "AND (Email IS NOT NULL OR Name IS NOT NULL)"
				  + "AND I_IsImported='N'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.fine ("Update BPartnerValue from Email/Name=" + no);

		sql =  "UPDATE I_Order "
				  + "SET Name = COALESCE(ContactName,BPartnerValue) " 
				  + "WHERE C_BPartner_ID IS NULL "
				  + "AND Name IS NULL "
				  + "AND (ContactName IS NOT NULL OR BPartnerValue IS NOT NULL) "
				  + "AND I_IsImported='N'" 
				  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.fine ("Update Name from ContactName/BPartnerValue=" + no);

		//	BP from Value
		sql =  "UPDATE I_Order o "
			  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
			  + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
			  + " AND I_IsImported='N'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		log.fine("Set BP from updated Value=" + no);

		Map <String, MBPartner> bpartnerMap = new HashMap <String, MBPartner>();		
		Map <String, List<MLocation>> bpLocationMap= new HashMap <String, List<MLocation>>();
		Map <String, List<MUser>> bpUserMap= new HashMap <String, List<MUser>>();
		
		//	Go through remaining Order Records w/o C_BPartner_ID
		sql =  	"SELECT * FROM I_Order "
			  + "WHERE I_IsImported='N' "
			  + "AND C_BPartner_ID IS NULL "
			  + "AND BPartnerValue IS NOT NULL" 
			  + STD_CLIENT_CHECK
			  + " ORDER by BPartnerValue ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery ();

			while (rs.next ())
			{
				X_I_Order imp = new X_I_Order (ctx, rs, get_Trx());
		
				MBPartner bp = bpartnerMap.get(imp.getBPartnerValue());

				if(bp == null) {
					if(bpartnerMap.size() >= COMMITCOUNT) {
						saveBPartners(bpartnerMap, bpLocationMap, bpUserMap);
						bpartnerMap.clear();
						bpLocationMap.clear();
						bpUserMap.clear();
					}
					
					bp = new MBPartner (ctx, -1, get_Trx());
					bp.setClientOrg (imp.getAD_Client_ID (), imp.getAD_Org_ID ());
					bp.setValue (imp.getBPartnerValue());
					bp.setName (imp.getName());
					bpartnerMap.put(imp.getBPartnerValue(), bp);
				}
				
				List<MLocation> bpLocations = bpLocationMap.get(imp.getBPartnerValue());
				if(bpLocations == null) {
					bpLocations = new ArrayList <MLocation>();
					bpLocationMap.put(imp.getBPartnerValue(), bpLocations);
				}
				
				MLocation location = null;
				for(MLocation loc : bpLocations) {
					if(loc.equals(imp.getC_Country_ID(), imp.getC_Region_ID(), 
							imp.getPostal(), "", imp.getCity(), 
							imp.getAddress1(), imp.getAddress2())) {
					location = loc;
					break;
					}
				}
				
				if(location == null) {
					location = new MLocation (ctx, 0, get_Trx());
					location.setAddress1 (imp.getAddress1 ());
					location.setAddress2 (imp.getAddress2 ());
					location.setCity (imp.getCity ());
					location.setPostal (imp.getPostal ());
					if (imp.getC_Region_ID () != 0)
						location.setC_Region_ID (imp.getC_Region_ID ());
					location.setC_Country_ID (imp.getC_Country_ID ());
					bpLocations.add(location);
				}
				
				List<MUser> bpUsers = bpUserMap.get(imp.getBPartnerValue());
				if(bpUsers == null) {
					bpUsers = new ArrayList <MUser>();
					bpUserMap.put(imp.getBPartnerValue(), bpUsers);
				}
				
				MUser contact = null;
				for(MUser user : bpUsers) {
					if(user.getName().equals(imp.getContactName()) ||
							user.getName().equals(imp.getName())) {
					contact = user;
					break;
					}
				}
				
				if(contact == null) {
					contact = new MUser (getCtx(), 0, get_Trx());
					if (imp.getContactName () == null)
						contact.setName (imp.getName ());
					else
						contact.setName (imp.getContactName ());
					contact.setEMail (imp.getEMail ());
					contact.setPhone (imp.getPhone ());
					bpUsers.add(contact);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sql, e);
			throw new CompiereSQLException();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		saveBPartners(bpartnerMap, bpLocationMap, bpUserMap);
		sql =  "UPDATE I_Order "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No BPartner, ' "
			  + "WHERE C_BPartner_ID IS NULL"
			  + " AND I_IsImported<>'Y'" 
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		if (no != 0)
			log.warning ("No BPartner=" + no);
		commit();

		//	-- New Orders -----------------------------------------------------

		int noInsert = 0;
		int noInsertLine = 0;
		
		actualDate = new Date();
		System.out.println("O/C " + actualDate.toString());
		
		Map<Integer, X_I_Order> importOrderMap = new HashMap<Integer, X_I_Order>();
		Map <MOrder, List<MOrderLine>> orderMap = new HashMap <MOrder, List<MOrderLine>> ();
		
		//	Go through Order Records w/o
		sql =  "SELECT * FROM I_Order "
			  + "WHERE I_IsImported='N'" 
			  + STD_CLIENT_CHECK
			  + "ORDER BY DocumentNo, Ad_Org_ID, C_BPartner_ID, BillTo_ID, C_BPartner_Location_ID, I_Order_ID";
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery ();
			//
			int oldC_BPartner_ID = 0;
			int oldBillTo_ID = 0;
			int oldC_BPartner_Location_ID = 0;
			String oldDocumentNo = "";
			int oldAD_Org_ID = 0;
			int oldDocumentType = 0;
			//
			MOrder order = null;
			int lineNo = 0;
			List<MOrderLine> orderLines = null;
			int counter = 0;
			X_I_Order imp = null;
			
			while (rs.next ())
			{
				
				counter++;
				if(counter%100==0){
					System.out.println("O/C draft: " + counter);
				}	
				
				imp = new X_I_Order (ctx, rs, get_Trx());
				String cmpDocumentNo = imp.getDocumentNo();
				if (cmpDocumentNo == null)
					cmpDocumentNo = "";
				
				//	New Order
				if (order==null 
					||oldAD_Org_ID != imp.getAD_Org_ID() 
					||oldC_BPartner_ID != imp.getC_BPartner_ID() 
					|| oldC_BPartner_Location_ID != imp.getC_BPartner_Location_ID()
					|| oldBillTo_ID != imp.getBillTo_ID() 
					|| oldDocumentType !=imp.getC_DocType_ID()
					|| !oldDocumentNo.equals(cmpDocumentNo))
				{
					if(orderMap.size() >= COMMITCOUNT) {
						saveOrders(orderMap, importOrderMap);
						orderMap.clear();
						importOrderMap.clear();
					}
					
					oldAD_Org_ID = imp.getAD_Org_ID();
					oldC_BPartner_ID = imp.getC_BPartner_ID();
					oldC_BPartner_Location_ID = imp.getC_BPartner_Location_ID();
					oldBillTo_ID = imp.getBillTo_ID();
					oldDocumentNo = imp.getDocumentNo();
					oldDocumentType = imp.getC_DocType_ID();
					if (oldDocumentNo == null)
						oldDocumentNo = "";
					//
					order = new MOrder (imp);
					order.setDocAction(m_docAction);
					
					if (order.getSalesRep_ID() == 0)
						order.setSalesRep_ID(getAD_User_ID());
					
					//-----------------------------------------------------------BECO
					
					//Forma de pago cableada a Cash (Cheque)
					order.set_ValueNoCheck("PaymentRule", "S");
					order.set_Value("XX_TaxReceipt", imp.get_Value("XX_TaxReceipt"));
					
					if(imp.get_Value("XX_OrigTaxReceipt")!=null)
						order.set_Value("XX_OrigTaxReceipt", imp.get_Value("XX_OrigTaxReceipt"));
					
					if(imp.get_Value("XX_MachineNo")!=null)
						order.set_Value("XX_MachineNo", imp.get_Value("XX_MachineNo"));
					
					if(imp.get_Value("XX_TransactionNo")!=null)
						order.set_Value("XX_TransactionNo", imp.get_Value("XX_TransactionNo"));
					
					order.set_Value("XX_TaxSerial", imp.get_Value("XX_TaxSerial"));
					order.set_Value("XX_StartTime", imp.get_Value("XX_StartTime"));
					order.set_Value("XX_EndTime", imp.get_Value("XX_EndTime"));
					
					//----------------------------------------------------------FIN BECO
					
					
					//
					noInsert++;
					//
					orderLines = new ArrayList<MOrderLine>();
					orderMap.put(order, orderLines);
					lineNo = 10;
				}
				
				importOrderMap.put(imp.getI_Order_ID(), imp);

				//	New OrderLine
				MOrderLine line = new MOrderLine (order,imp.getI_Order_ID());
				line.setLine(lineNo);
				lineNo += 10;

				// gwu: 1712639, added support for UOM conversions
				boolean convertUOM = false;
				if ( imp.getM_Product_ID() != 0 && imp.getC_UOM_ID() != 0 )
				{
					line.setM_Product_ID( imp.getM_Product_ID(), imp.getC_UOM_ID() );
					convertUOM = ( line.getProduct().getC_UOM_ID() != imp.getC_UOM_ID() );
				}
				else if (imp.getM_Product_ID() != 0)
				{
					line.setM_Product_ID(imp.getM_Product_ID(), true);
					convertUOM = false; 
				}

				if (imp.getC_Charge_ID() != 0)
				{
					line.setC_Charge_ID(imp.getC_Charge_ID());
					convertUOM = false;
				}
				
				if( convertUOM )
				{
	                BigDecimal rateQty = MUOMConversion.getProductRateFrom(ctx, line.getM_Product_ID(), imp.getC_UOM_ID() );
	                if( rateQty == null )
	                {
	                	String msg = Msg.translate(ctx, "NoProductUOMConversion");
						imp.setI_ErrorMsg(msg);
						imp.save();
						continue;
	                }
					line.setQtyEntered( imp.getQtyOrdered() );
					line.setQtyOrdered( imp.getQtyOrdered().multiply( rateQty ) );
					line.setPrice();
					if (imp.getPriceActual().compareTo(Env.ZERO) != 0)
					{
						line.setPriceActual( imp.getPriceActual() );						
						line.setPriceEntered(imp.getPriceActual().multiply(line.getQtyOrdered()
								.divide(line.getQtyEntered(), 12, BigDecimal.ROUND_HALF_UP)));
					}
				}
				else // no UOM conversion
				{
					line.setQty(imp.getQtyOrdered());
					line.setPrice();
					if (imp.getPriceActual().compareTo(Env.ZERO) != 0)
						line.setPrice(imp.getPriceActual());
				}
				
				if (imp.getC_Tax_ID() != 0)
					line.setC_Tax_ID(imp.getC_Tax_ID());
				else
				{
					line.setTax();
					imp.setC_Tax_ID(line.getC_Tax_ID());
				}
				if (imp.getFreightAmt() != null)
					line.setFreightAmt(imp.getFreightAmt());
				if (imp.getLineDescription() != null)
					line.setDescription(imp.getLineDescription());
				
				//--------------------------------------------BECO
				
				line.setPriceList(imp.get_ValueAsBigDecimal("XX_BasePrice"));
				line.set_Value("XX_PriceConsecutive", imp.get_Value("XX_PriceConsecutive"));
				line.setM_AttributeSetInstance_ID( imp.get_ValueAsInt("M_AttributeSetInstance_ID"));
				line.setXX_EmployeeDiscount(imp.get_ValueAsBigDecimal("XX_EmployeeDiscount"));
				line.set_Value("TaxAmt", imp.getTaxAmt());
				
				//--------------------------------------------FIN BECO
				
				noInsertLine ++;
				orderLines.add(line);
				
				//	Update Import
				imp.setI_IsImported(X_I_Order.I_ISIMPORTED_Yes);
				imp.setProcessed(true);
			}
		}
		catch (SQLException e)	{
			log.log(Level.SEVERE, "Order - " + sql, e);
			throw new CompiereSQLException();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		
		saveOrders(orderMap, importOrderMap);
		
		//	Set Error to indicator to not imported
		sql =  "UPDATE I_Order "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		//
		addLog (0, null, new BigDecimal (noInsert), "@C_Order_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertLine), "@C_OrderLine_ID@: @Inserted@");
		return "#" + noInsert + "/" + noInsertLine;

	}
	
	public void saveBPartners (Map<String, MBPartner> bpartnerMap, 
			Map<String, List<MLocation>> bpLocationMap,
			Map<String, List<MUser>> bpUserMap) throws Exception {

			if(bpartnerMap.isEmpty()) 
			return;
			
			
			List<MBPartner> bpartnersToSave = new ArrayList<MBPartner>(bpartnerMap.values());
			if(!PO.saveAll(get_Trx(), bpartnersToSave)) 
			throw new CompiereStateException("Could not save Business Partners");
			
			List<MLocation> locationsToSave = new ArrayList<MLocation>();
			for(List<MLocation> locations : bpLocationMap.values())
			locationsToSave.addAll(locations);
			
			if(!PO.saveAll(get_Trx(), locationsToSave)) 
			throw new CompiereStateException("Could not save Locations");
			
			List <MBPartnerLocation> bpLocationsToSave = new ArrayList<MBPartnerLocation> ();
			for(Map.Entry<String, List<MLocation>> entry : bpLocationMap.entrySet()) {
			MBPartner bp = bpartnerMap.get(entry.getKey());
			for(MLocation loc : entry.getValue()) {
			MBPartnerLocation bpl = new MBPartnerLocation (bp);
			bpl.setC_Location_ID (loc.getC_Location_ID ());
			bpLocationsToSave.add(bpl);
			}
			}
			
			if(!PO.saveAll(get_Trx(), bpLocationsToSave)) 
			throw new CompiereStateException("Could not save BP Locations");
			
			List <MUser> usersToSave = new ArrayList<MUser>();
			for(Map.Entry<String, List<MUser>> entry : bpUserMap.entrySet()) {
			MBPartner bp = bpartnerMap.get(entry.getKey());
			for(MUser user : entry.getValue()) {
			user.setC_BPartner_ID(bp.getC_BPartner_ID());
			usersToSave.add(user);
			}
			}
			
			if(!PO.saveAll(get_Trx(), usersToSave)) 
			throw new CompiereStateException("Could not save Users");
			
			// Update BPs for new Business Partners
			String sql =  "UPDATE I_Order o "
			+ "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp "
			+ "WHERE o.BPartnerValue=bp.Value "
			+ "AND o.AD_Client_ID=bp.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NULL "
			+ "AND BPartnerValue IS NOT NULL "
			+ "AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
			int no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
			log.fine("Set BP for newly created BPs =" + no);
			
			//	Update BP Location for new Business Partners
			sql =  "UPDATE I_Order o "
			+ "SET (BillTo_ID, C_BPartner_Location_ID)=(SELECT C_BPartner_Location_ID,C_BPartner_Location_ID "
			+ "FROM C_BPartner_Location bpl "
			+ "INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID) "
			+ "WHERE o.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=o.AD_Client_ID "
			+ "AND DUMP(o.Address1)=DUMP(l.Address1) AND DUMP(o.Address2)=DUMP(l.Address2) "
			+ "AND DUMP(o.City)=DUMP(l.City) AND DUMP(o.Postal)=DUMP(l.Postal) "
			+ "AND DUMP(o.C_Region_ID)=DUMP(l.C_Region_ID) AND DUMP(o.C_Country_ID)=DUMP(l.C_Country_ID)) "
			+ "WHERE C_BPartner_ID IS NOT NULL "
			+ "AND C_BPartner_Location_ID IS NULL "
			+ "AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
			log.fine("Set BP Location for newly created BPs=" + no);
			
			//	Set User for new Business Partners
			sql =  "UPDATE I_Order o "
			+ "SET AD_User_ID=(SELECT AD_User_ID FROM AD_User u "
			+ "WHERE u.AD_Client_ID=o.AD_Client_ID "
			+ "AND u.C_BPartner_ID=o.C_BPartner_ID " 
			+ "AND u.Name=COALESCE(o.ContactName, o.Name) )"
			+ "WHERE C_BPartner_ID IS NOT NULL "
			+ "AND AD_User_ID IS NULL "
			+ "AND EXISTS (SELECT 1 FROM AD_User u WHERE o.C_BPartner_ID=u.C_BPartner_ID "
			+ "AND o.AD_Client_ID=u.AD_Client_ID "
			+ "AND u.Name=COALESCE(o.ContactName, o.Name)) "
			+ "AND I_IsImported<>'N'" 
			+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, m_AD_Client_ID);
			log.fine("Set User for newly created BPs=" + no);
			
			commit();
		}
	
		public void saveOrders (	Map<MOrder, List<MOrderLine>> orderMap,
			Map<Integer, X_I_Order> importOrderMap) throws Exception{

			if(orderMap.isEmpty()) 
			return;
			
			List<MOrder> ordersToSave = new ArrayList<MOrder>(orderMap.keySet());
			if(!PO.saveAll(get_Trx(), ordersToSave)) 
			throw new CompiereStateException("Could not save Orders");
			
			List<MOrderLine> orderLinesToSave = new ArrayList<MOrderLine>();
			for(Map.Entry<MOrder, List<MOrderLine>> entry : orderMap.entrySet()) {
			MOrder order = entry.getKey();
			for(MOrderLine orderLine : entry.getValue()) {
			orderLine.setC_Order_ID(order.getC_Order_ID());
			orderLinesToSave.add(orderLine);
			}
			}
			
			if(!PO.saveAll(get_Trx(), orderLinesToSave)) 
			throw new CompiereStateException("Could not save Orders");
			
			List<X_I_Order> importOrdersToSave = new ArrayList<X_I_Order>();
			
			for(MOrderLine line :orderLinesToSave)
			{
			X_I_Order imp = importOrderMap.get(line.getI_Order_ID());
			if(imp != null) {
			imp.setC_OrderLine_ID(line.getC_OrderLine_ID());
			imp.setC_Order_ID(line.getC_Order_ID());
			importOrdersToSave.add(imp);
			}
			}
			
			commit();
			
			for(MOrder order : ordersToSave) {
			if(!DocumentEngine.processIt(order, m_docAction))
			{
			// Ignore errors in processing
			log.warning("Could not process Order : " + order.getDocumentNo());
			}
			
			if(!order.save())
			throw new CompiereStateException("Could not save Order");
			
			// Need to commit after each order to prevent deadlocks
			commit();
			}
			
			if(!PO.saveAll (get_Trx(), importOrdersToSave))
			throw new CompiereStateException("Could not save Import Order records");
			
			commit();
			}
	
}
