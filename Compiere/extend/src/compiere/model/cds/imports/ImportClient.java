package compiere.model.cds.imports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.StringTokenizer;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_I_BPartner;


public class ImportClient extends SvrProcess{

	
	static String Description="Venta en caja registradora";
	static Integer FreightAmt = null;
	static String I_IsImported="N";
	static String IsActive="Y";
	static String IsSOtrx="Y";
	static String Processed="N";
	static String Processing="N";	
	
	
	
	
	/**
	 * Ésta función permite extraer la información con respecto a los clientes que se encuentran
	 * en el sistema actual CENTROBECO98.
	 * 
	 * @param date		Fecha en que se va a exportar la data de las ventas.
	 * @param sentencia	Statement con el cual se va a realizar la consulta de los clientes en el AS/400
	 * @return			ResultSet que contiene el conjunto de clientes que se hicieron en la fecha date.
	 */
	public ResultSet ExportClientDB2(String date, Statement sentencia, As400DbManager As)
	{
		String sql;
        DateFormat formater ; 
        Date dateS ; 
        Date dateU;
        ResultSet r = null;
        
        As.conectar();
        	
        System.out.println("Today is " +date );
        try{
        	//se pasa la fecha date al formato necesario para poder hacer la consulta en el AS/400
            formater = new SimpleDateFormat("yyyy-MM-dd");
            dateU = formater.parse(date); 
            dateS = new java.sql.Date(dateU.getTime());   
            //System.out.println("Today is " +dateS );
            //query que permite hacer la consulta sobre las ventas en el AS/400
                       
            sql = "select * from cr.afiliado1 where estadoafiliado = 'A' and regactualizado = 'N' and fechaafiliacion >= '"+dateS+"' ";
            
            System.out.println("QUERY:::"+sql);
            
		
            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarConsulta(sql, sentencia);
           
           
		}catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}
	
	
	
	public int getcountry()
	{
		int countryid = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "SELECT C_COUNTRY_ID FROM C_COUNTRY WHERE NAME LIKE '%VENEZUELA%'";
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				countryid = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return countryid;
	}
	
	public int getcity()
	{
		int cityid = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		String sql = "SELECT C_City_ID FROM C_City WHERE C_COUNTRY_ID = 339 AND NAME LIKE '%Caracas%'";
		try{
			//sentencia = m.conexionOXE.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//rs = m.realizarConsultaOXE(sql,sentencia);
			pstmt = DB.prepareStatement(sql, null);
   		    rs = pstmt.executeQuery();
			if(rs.next()){
				cityid = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return cityid;
	}
	
	
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
	 * Trae la información del Almacén donde se produjo la venta.
	 * 
	 * @param tienda	número o valor de la tienda donde se produjo la venta.
	 * @return			identificador del almacén.
	 */
	public int getWHId (int tienda, int organization){
		
		int WHId = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		
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
	
	
	public ResultSet getEstado(Statement sentencia, As400DbManager As, String codedo)
	{
		String sql;
        ResultSet r = null;
        
        As.conectar();
        	
       try{        	         
            sql = "select desedo from cr.atcm23 where codedo = '"+codedo+"' ";
            
            //System.out.println("QUERY Estado:::"+sql);
            
            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarConsulta(sql, sentencia);
           
           
		}catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}
	
	public ResultSet getCiudad(Statement sentencia, As400DbManager As, String codedo, String codciu)
	{
		String sql;
        ResultSet r = null;
        
        As.conectar();
        	
       try{        	         
            sql = "select desciu from cr.atcm24 where codedo = '"+codedo+"' and codciu = '"+codciu+"' ";
            
            //System.out.println("QUERY Ciudad:::"+sql);
            
            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarConsulta(sql, sentencia);
           
           
		}catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}
	
	
	public ResultSet getUrb(Statement sentencia, As400DbManager As, String codedo, String codciu, String codurb)
	{
		String sql;
        ResultSet r = null;
        
        As.conectar();
        	
       try{        	         
            sql = "select desurb from cr.atcm25 where codedo = '"+codedo+"' and codciu = '"+codciu+"' and codurb = '"+codurb+"' ";
            
           // System.out.println("QUERY Urb:::"+sql);
            
            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarConsulta(sql, sentencia);
           
           
		}catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	
	public int getcargo (String cargo){
		
		int cargocr = 0;
		ResultSet rs = null;
		PreparedStatement pstmt=null;
		
		//String sql = "select name from c_job where value = '"+cargo+"' and ISACTIVE = 'Y' ";
		String sql = "select C_Job_ID from c_job where value = 'VIG' and ISACTIVE = 'Y' ";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if( rs.next() ){
				cargocr = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return cargocr;
	}
	
	
	public void bPartnerIsImported (String codempleado, As400DbManager As)
	{
		
		Integer r = 0;
        /*As400DbManager As = new As400DbManager();
        As.conectar();
        */
                
        try
        {
        	//String SQL = ("UPDATE cr.transaccion SET regactualizado= 'Y' WHERE numtienda = '"+warehouse+"' and  fecha = '"+fecha+"' and numcajafinaliza = '"+cajaFin+"' and numtransaccion = '"+tranc+"' ");
        	String SQL = ("Update cr.afiliado1 set regactualizado = 'S' where codafiliado = '"+codempleado+"' ");
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	r = sentencia.executeUpdate(SQL);
        	sentencia.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
       
		}
       
	}
	

	@SuppressWarnings("null")
	@Override
	protected String doIt() throws Exception {
		
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement sentencia = null;
		As400DbManager As = new As400DbManager();
		Trx Transaccion = Trx.get("Transaccion");
		Boolean aux1 = false;
		X_I_BPartner IBPartner = null;
		
		String codafiliado, tipoafiliado, nombre, apellido, coddepartamento, codcargo, nitcliente, 
			   direccion, direccionfiscal, email, exentoimpuesto, registrado, contactar, codregion, 
			   estadoafiliado, estadocola, genero, estadocivil, codafiliado1, nomCompleto, tlfDef1, tlfDef2;
		Integer numtienda, numficha, codarea, numtelefono, codarea1, numtelefono1, organization, warehouse; 
		Timestamp fechaafiliacion, actualiza, fechanac; 
		
		try
		{			
			rs=ExportClientDB2("2010-04-10",sentencia, As);
			
			while(rs.next())
			{
				codafiliado =rs.getString(1);
				
				codafiliado1 = codafiliado.substring(2); // Codigo de afiliado sin V- o E-
				tipoafiliado = rs.getString(2); //Tipo afiliado	
				nombre = rs.getString(3); // Nombre
				apellido = rs.getString(4); // Apellido
				numtienda = rs.getInt(5); // Numero de tienda de afiliacion
				numficha = rs.getInt(6); // Numero de ficha -> Codigo del empleado BECO
				//coddepartamento = rs.getString(7); // Codigo departamento
				codcargo = rs.getString(8); // Codigo de Cargo
				nitcliente = rs.getString(9); // NIT cliente
				direccion = rs.getString(10); // Direccion
				direccionfiscal = rs.getString(11); // Direccion Fiscal
				email = rs.getString(12); // email
				codarea = rs.getInt(13); // Codigo de Area
				numtelefono = rs.getInt(14); // Numero de TLF
				codarea1 = rs.getInt(15); // Codigo de Area 1
				numtelefono1 = rs.getInt(16); // Numero TLF 1
				fechaafiliacion = rs.getTimestamp(17); // Fecha Afiliacion
				exentoimpuesto = rs.getString(18); // Exento impuesto
				registrado = rs.getString(19); // Registrado
				contactar = rs.getString(20); // Contactar
				//codregion = rs.getString(21); // Codigo Region NO SE USA
				estadoafiliado = rs.getString(22); // Estado Afiliado
				estadocola = rs.getString(23); // Estado Colaborador
				actualiza = rs.getTimestamp(24); // Actualizacion
				genero = rs.getString(25); // Genero
				estadocivil = rs.getString(26); // Estado Civil
				fechanac = rs.getTimestamp(27); // Fecha Nacimiento
				
				//Organizacion
				organization = getOrganization(numtienda);
				// Tienda
				warehouse = getWHId(numtienda, organization);
				
				// Direccion
				Vector <String> dir = new Vector <String>();
					 
				
				//========================================
				//        Insertar I_BP
				//========================================
				
				IBPartner = new X_I_BPartner(getCtx(),0,Transaccion);
				
				IBPartner.setAD_Org_ID(0); // //siempre cero para que sea la organizacion *  AD_ORG_ID
				
				
				IBPartner.setXX_CI_RIF(codafiliado1);
				IBPartner.setValue(codafiliado1);
				
				IBPartner.setDescription("Afiliado BECO");
				if(tipoafiliado.equalsIgnoreCase("V"))
					IBPartner.setXX_TypePerson("V");
				else
					IBPartner.setXX_TypePerson("E");
				
				nomCompleto = nombre+" "+apellido;
				IBPartner.setName(nomCompleto);
				//IBPartner.setContactName(nomCompleto);
				
				// NUMERO DE TIENDA (CAMPO QUE NO ESTA EN LA TABLA I_BP NO EN BP)
				IBPartner.setM_Warehouse_ID(warehouse);
				
				// NUMERO DE FICHA (CAMPO QUE NO ESTA EN LA TABLA I_BP NO EN BP)
				//System.out.println("Numero de ficha "+numficha);
			
				if(numficha.toString().isEmpty() || numficha == 0 ||  estadoafiliado.equalsIgnoreCase("I"))
				{
					IBPartner.setXX_IsCostumer(true);
					IBPartner.setXX_IsEmployee(false);
				}
				else
				{
					IBPartner.setXX_NumberTap(numficha.toString());
					// CODIGO DE CARGO (CAMPO QUE NO ESTA EN LA TABLA I_BP NO EN BP)
					IBPartner.setC_Job_ID(getcargo(codcargo));
					IBPartner.setXX_IsCostumer(true);
					IBPartner.setXX_IsEmployee(true);
				}

				IBPartner.setXX_NIT(nitcliente);
				IBPartner.setC_Country_ID(getcountry());
				IBPartner.setCity_ID(getcity());
				
				String estado, ciudad, urb;
				
				if(direccion.compareToIgnoreCase( ">>>>>" ) == 0)
					IBPartner.setAddress1("Dirección no definida");
				else
				{
					
					StringTokenizer tokens = new StringTokenizer(direccion,">");
					String aux = null;
					while(tokens.hasMoreTokens()){
			            aux = tokens.nextToken().toString();
			            dir.add(aux);
			        }
					
					String dir0, dir1, dir2, dir3 =  "";
					
					 dir0 = dir.get(0)+","+dir.get(1)+","+dir.get(2);	
					 dir1 = dir.get(3);
					 dir2 = dir.get(4);
					 dir3 = dir.get(5);
					 
					 /*System.out.println(dir0);
					 System.out.println(dir1);
					 System.out.println(dir2);
					 System.out.println(dir3);*/
					
					
					rs1 = getEstado(sentencia, As, dir3);
					if(rs1.next())
					{
						estado = rs1.getString(1);
						//System.out.println("estado "+estado);
						IBPartner.setAddress4(estado);
					}
					rs1.close();
					sentencia.close();
					
					rs1 = getCiudad(sentencia, As, dir3, dir2);
					if(rs1.next())
					{
						ciudad = rs1.getString(1);
						//System.out.println("ciudad "+ciudad);
						IBPartner.setAddress3(ciudad);
					}
					rs1.close();
					sentencia.close();
					rs1 = getUrb(sentencia, As, dir3, dir2, dir1);
					if(rs1.next())
					{
						urb = rs1.getString(1);
						//System.out.println("urb "+urb);
						IBPartner.setAddress2(urb);
					}
					rs1.close();
					sentencia.close();
					IBPartner.setAddress1(dir0);
					
				}
				
				IBPartner.setAddress2(direccionfiscal);
				//IBPartner.setEMail(email);
				IBPartner.setXX_Email(email);
				
				tlfDef1 = codarea.toString()+numtelefono.toString();
				//IBPartner.setPhone(tlfDef1);
				IBPartner.setXX_Phone(tlfDef1);
				
				tlfDef2 = codarea1.toString()+numtelefono1.toString();
				//IBPartner.setPhone2(tlfDef2);
				IBPartner.setXX_Phone2(tlfDef2);
				
				IBPartner.setXX_EntryDate(fechaafiliacion);
				
				// PREGUNTAR QUE SE HACE CON TAX.........
				if(exentoimpuesto.equalsIgnoreCase("N"))
					IBPartner.setXX_TypeTax_ID("1000001");
				else
					IBPartner.setXX_TypeTax_ID("1000000");
					
				// REGISTRADO (CAMPO QUE NO ESTA EN LA TABLA I_BP NO EN BP)
				IBPartner.setXX_Registrado(registrado);
				
				// CONTACTAR (CAMPO QUE NO ESTA EN LA TABLA I_BP NO EN BP)
				if(contactar.equalsIgnoreCase("N"))
					IBPartner.setXX_Iscontac(false);
				else
					IBPartner.setXX_Iscontac(true);
				
				// GENERO, ESTADO CIVIL, FECHA NAC (CAMPO QUE NO ESTA EN LA TABLA I_BP NO EN BP)
				IBPartner.setXX_Genero(genero);
				IBPartner.setXX_EstadoCivil(estadocivil);
				IBPartner.setXX_FechaNac(fechanac);
				
				IBPartner.setI_IsImported(I_IsImported);
				IBPartner.setIsActive(true);
				IBPartner.setProcessed(false);
				IBPartner.setProcessing(false);
				
				if(!IBPartner.save())
				{
					aux1 = true;
					Transaccion.rollback();
				}
				
			}// end while
			Transaccion.commit();
			
			rs.first();
			rs.beforeFirst();

			// Despues si de guardadas las ordenes de Ventas en la I_Order se llama al proceso de update 
			
			if(aux1 == false)
			{
				while(rs.next())
				{
					// Codigo de afiliado
					codafiliado =rs.getString(1);
					bPartnerIsImported(codafiliado, As);
					
				}
				rs.close();
				sentencia.close();
			}
			rs.close();
			sentencia.close();
			
		} // end try
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Import Completed";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
