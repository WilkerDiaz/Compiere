package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.apps.ProcessCtl;
import org.compiere.model.MCountry;
import org.compiere.model.MCurrency;
import org.compiere.model.MPInstance;
import org.compiere.model.X_C_Conversion_Rate;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.suppliesservices.X_Ref_XX_DistributionType_LV;

public class MOrder extends org.compiere.model.MOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MOrder.class);

	public MOrder(Ctx ctx, int C_Order_ID, Trx trx) {
		super(ctx, C_Order_ID, trx);
	}

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MOrder (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MOrder

	/**
	 * 	Consulta la tabla C_Order, devuelve el EstimatedDate antes de salvar
	 */
	private Timestamp ConsultaTablaC_OrderEstimatedDate(){
		Timestamp aux = null;
		String sql = "SELECT XX_EstimatedDate "
			+ "FROM C_ORDER "
			+ "WHERE C_ORDER_ID = " + getC_Order_ID();

		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()){
				aux = (Timestamp)rs.getObject("XX_EstimatedDate");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return new Timestamp(1);
		}
	}//ConsultaTablaC_OrderEstimatedDate

	/**
	 * 	Consulta el Jefe de Planificacion
	 */
	public Integer ConsultarJefePlanificacion(){
		Integer aux = null;

		String sql = "select C_BPartner_ID " +
		"from C_BPartner " +
		"where C_JOB_ID = "+getCtx().getContext("#XX_L_JOBPOSITION_PLANMAN_ID")+" and " +
		"IsActive = 'Y' ";

		try{
			//System.out.println(sql);
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()){
				aux = rs.getInt("C_BPartner_ID");

			}
			rs.close();
			pstmt.close();
			return aux;
		}
		catch (SQLException e){
			// TODO: handle exception
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return -1;
		}
	}//ConsultarJefePlanificacion


	/**
	 * Jorge E. Pires G. --> Funcion 089. Envio de Correos por cambio de Estimated Date 
	 * 
	 **/	
	private boolean EnviarCorreosCambiosFechas(Integer BPartner, Timestamp EstimatedViejo, Timestamp EstimatedNuevo, String Attachment) {
		

		String Mensaje = Msg.getMsg( getCtx(), 
				"XX_ChangeEstimatedDate", 
				new String[]{ getDocumentNo(), 
			new String(""+EstimatedNuevo.getDate()+"-"+(EstimatedNuevo.getMonth()+1)+"-"+(EstimatedNuevo.getYear()+1900)), 
			new String(""+EstimatedViejo.getDate()+"-"+(EstimatedViejo.getMonth()+1)+"-"+(EstimatedViejo.getYear()+1900)),
			getCtx().getContext("##AD_User_Name")
		});
		String sql = "SELECT u.AD_User_ID "
			+ "FROM AD_User u "
			+ "where IsActive = 'Y' and "
			+ "C_BPARTNER_ID = " + BPartner;

		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()){
				Integer UserAuxID = rs.getInt("AD_User_ID");
				Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_CAMBFECHLLEG_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") ,-1, UserAuxID, Attachment);
				f.ejecutarMail();  
				f = null;
			}//doctype
			rs.close();
			pstmt.close();
		}//try
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}
	/**
	 * Fin Jorge E. Pires G.
	 * */


	/**
	 * Daniel Pellegrino --> Funcion  
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusCoor(String Attachment, String status , String date) {

		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
		"FROM C_BPARTNER B " +
		"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
		"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Cuentas por Pagar'))";

		MCurrency currency = new MCurrency( Env.getCtx(), getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), getC_Country_ID(), null);

		String Mensaje = getDocumentNo()+" "+status+" \n\n" +
		"Vendor: "+vendor.getName()+" \n"+
		"Amount: "+getTotalLines()+" \n"+
		"Currency: "+currency.getDescription()+"\n"+
		"Payment Term: "+paymentTerm.getName()+"\n"+
		"Country: "+country.getDescription()+"\n"+
		date;

		try{

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");

				String sql2 = "SELECT u.AD_User_ID "
					+ "FROM AD_User u "
					+ "where IsActive = 'Y' and "
					+ "C_BPARTNER_ID = " + coordinador;

				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();


				while(rs2.next())
				{	
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;

				}

				rs2.close();
				pstmt2.close();
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		}
		return true;
	}//Enviar

	private boolean EnviarCorreosCambioStatusJefeImport(String Attachment, String status, String date ) {

		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
		"FROM C_BPARTNER B " +
		"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
		"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Importaciones'))";

		MCurrency currency = new MCurrency( Env.getCtx(), getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), getC_Country_ID(), null);

		String Mensaje = getDocumentNo()+" "+status+" \n\n" +
		"Vendor: "+vendor.getName()+" \n"+
		"Amount: "+getTotalLines()+" \n"+
		"Currency: "+currency.getDescription()+"\n"+
		"Payment Term: "+paymentTerm.getName()+"\n"+
		"Country: "+country.getDescription()+"\n"+
		date;

		try{

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");

				String sql2 = "SELECT u.AD_User_ID "
					+ "FROM AD_User u "
					+ "where IsActive = 'Y' and "
					+ "C_BPARTNER_ID = " + coordinador;

				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();

				while(rs2.next()){

					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;

				}

				rs2.close();
				pstmt2.close();
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		}
		return true;
	}//Enviar


	private boolean EnviarCorreosCambioStatusCoorImport(String Attachment, String status, String date ) {

		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
		"FROM C_BPARTNER B " +
		"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
		"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Importación'))";

		MCurrency currency = new MCurrency( Env.getCtx(), getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), getC_Country_ID(), null);

		String Mensaje = getDocumentNo()+" "+status+" \n\n" +
		"Vendor: "+vendor.getName()+" \n"+
		"Amount: "+getTotalLines()+" \n"+
		"Currency: "+currency.getDescription()+"\n"+
		"Payment Term: "+paymentTerm.getName()+"\n"+
		"Country: "+country.getDescription()+"\n"+
		date;

		try{

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");

				String sql2 = "SELECT u.AD_User_ID "
					+ "FROM AD_User u "
					+ "where IsActive = 'Y' and "
					+ "C_BPARTNER_ID = " + coordinador;

				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();

				while(rs2.next()){

					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;

				}

				rs2.close();
				pstmt2.close();
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		}
		return true;
	}//Enviar


	private boolean EnviarCorreosCambioStatusJefeCat(String Attachment, String status , String date) {

		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
		"FROM C_BPARTNER B " +
		"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
		"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Categoria'))";

		MCurrency currency = new MCurrency( Env.getCtx(), getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), getC_Country_ID(), null);

		String Mensaje = getDocumentNo()+" "+status+" \n\n" +
		"Vendor: "+vendor.getName()+" \n"+
		"Amount: "+getTotalLines()+" \n"+
		"Currency: "+currency.getDescription()+"\n"+
		"Payment Term: "+paymentTerm.getName()+"\n"+
		"Country: "+country.getDescription()+"\n"+
		date;

		try{

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");

				String sql2 = "SELECT u.AD_User_ID "
					+ "FROM AD_User u "
					+ "where IsActive = 'Y' and "
					+ "C_BPARTNER_ID = " + coordinador;

				PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
				ResultSet rs2 = pstmt2.executeQuery();


				while(rs2.next()){

					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;

				}

				rs2.close();
				pstmt2.close();
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		}
		return true;
	}//Enviar



	/*
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		boolean save = super.beforeSave(newRecord);
		String docType = getXX_POType();
		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int nano = cal.get(Calendar.SECOND);

		Timestamp XXExitDate = new Timestamp(year-1900,month,day,hour,min,sec,nano);
		Timestamp arrivalDate;
		Timestamp createdDate;
		String SQL = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		if (!cambioAlmacenValido())
			return false;
		
		if(save){
			if(isSOTrx()==false){ //OC 
				
				if(getXX_PurchaseType().equals("SE") || getXX_PurchaseType().equals("SU")){
					set_ValueNoCheck("XX_VMR_Department_ID", null);
					set_ValueNoCheck("XX_VMR_Category_ID", null);
				}
					
				//Se Iguala el Tipo de Cambio Real a la tasa de cambio
				//Modificado por  GHUCHET. Sólo se puede igualar el campo Tipo de Cambio Real a la tasa de Cambio 
				//si el primero no ha sido ya modificado de forma manual
				//boolean rateFlag = true;
				/*if(getXX_OrderType().equalsIgnoreCase("Importada") && getXX_ConversionRate_ID()!=0 && 
						(((String)get_Value("XX_ChangeRealExchangeRate")) == null || ((String)get_Value("XX_ChangeRealExchangeRate")).compareTo("Y")!=0) ){

					X_C_Conversion_Rate rate = new X_C_Conversion_Rate( Env.getCtx(), getXX_ConversionRate_ID(), null);
					setXX_RealExchangeRate(rate.getMultiplyRate());
					rateFlag = false;
				}*/
				
				if(getXX_OrderType().equalsIgnoreCase("Importada") && getXX_OrderStatus().equalsIgnoreCase("PRO")
						&& getXX_ConversionRate_ID()!=0
						&& (((String)get_Value("XX_ChangeRealExchangeRate")) == null || ((String)get_Value("XX_ChangeRealExchangeRate")).compareTo("Y")!=0)){
					X_C_Conversion_Rate rate = new X_C_Conversion_Rate( Env.getCtx(), getXX_ConversionRate_ID(), null);
					setXX_RealExchangeRate(rate.getMultiplyRate());
				}
				
				//si factor de reposicion = 0 no dejamos guardar                                                              
				if(getXX_OrderType().equalsIgnoreCase("Importada") && getXX_ReplacementFactor().compareTo(new BigDecimal(0))==0){
					log.saveError("Error",Msg.getMsg(getCtx(),"El Factor de Reposicion es 0"));
					return false;
				}	

				//GHUCHET - Si se modifica el folleto en estado RECIBIDA o CHEQUEADA no deja guardar                                                     
				if(get_ValueOld("XX_VMA_Brochure_ID") != get_Value("XX_VMA_Brochure_ID") && (getXX_OrderStatus().equals("CH") || getXX_OrderStatus().equals("RE"))){
					log.saveError("Error",Msg.getMsg(getCtx(),"No puede modificar el campo Folleto ya que la orden ha sido Recibida."));
					return false;
				}	
				
				// Si no hay lead times para este proveedor, no dejamos guardar
				SQL = "Select XX_IMPORTSLOGISTICSTIMETLI" +
				" FROM  XX_VLO_LeadTimes" +
				" WHERE C_BPartner_ID=" + getC_BPartner_ID() +
				" AND C_Country_ID=" +getC_Country_ID() +
				" AND XX_VLO_ArrivalPort_ID=" + getXX_VLO_ArrivalPort_ID() + 
				" AND isactive='Y'";

				boolean conLeadTime = true;

				try 
				{	
					pstmt = DB.prepareStatement(SQL, null);
					rs = pstmt.executeQuery();

					if (!rs.next()) {
						conLeadTime = false;
					}
				} catch (Exception e) {
					log.log(Level.SEVERE, SQL, e);
				} finally 
				{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}

				if(!conLeadTime && (getXX_OrderStatus().equals("PRO") || getXX_OrderStatus().equals("")) && !getXX_ComesFromCopy() && getXX_OrderType().equalsIgnoreCase("Importada")){
					log.saveError("Error",Msg.getMsg(Env.getCtx(), "La combinación seleccionada de proveedor, país y puerto de llegada no tiene un Lead Time asignado. \n" +
							"Por favor contacte a IMPORTACIONES para cargar este lead time ya que sin la carga del mismo, " +
							"usted no podrá guardar la orden de compra. \n" +
					"Recuerde indicar el proveedor, país y puerto de llegada para que el mismo pueda ser cargado"));					
					return false;
				}	

				arrivalDate = getXX_ArrivalDate();
				createdDate = getCreated();

				createdDate.setHours(0);
				createdDate.setMinutes(0);
				createdDate.setSeconds(0);
				createdDate.setNanos(0);

				//Verifico que la fecha de llegada sea mayor a la fecha de la O/C
				if(arrivalDate!=null){
					if(arrivalDate.compareTo(createdDate)==-1){
						log.saveError("Error",Msg.getMsg(getCtx(),"ArrivalDateLaterThanPO"));
						return false;
					}

					//Verifico que el arrival y el estimated sean dias laborables
					Utilities check = new Utilities();
					if(check.isNonBusinessDay(getXX_ArrivalDate())){
						log.saveError("Error",Msg.getMsg(getCtx(),"ArrivalNonBusinessDay"));
						return false;
					}

					if(check.isNonBusinessDay(getXX_EstimatedDate())){
						log.saveError("Error",Msg.getMsg(getCtx(),"EstimatedNonBusinessDay"));
						return false;
					}
				}
				//Definitive Cargo Agent Delivery Read Date
				//**************Fechas****************

				MVLOBoardingGuide boarding = new MVLOBoardingGuide(getCtx(),getXX_VLO_BOARDINGGUIDE_ID(), null);

				boolean aux=true;


				if(boarding.isXX_DefinitiveShippingDate()){
					aux=false;
				}

				if(isXX_DefinitiveCargoAg() && getXX_CARAGENTDELIVREALDATE()!=null && aux){

					BigDecimal TEI_B = new BigDecimal(0);
					BigDecimal TT_B = new BigDecimal(0);
					BigDecimal TRC_B = new BigDecimal(0);
					BigDecimal TNAC_B = new BigDecimal(0);
					BigDecimal TEN_B = new BigDecimal(0);
					BigDecimal TLI_B = new BigDecimal(0);

					String SQL1 = "Select MAX(XX_VLO_LEADTIMES_ID) "+
					"FROM XX_VLO_LEADTIMES A,  C_ORDER C "+
					"WHERE A.ISACTIVE='Y' AND C.DOCUMENTNO = '"+getDocumentNo()+"' " +
					"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID "+
					"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID "+
					"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
					"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
					"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")";

					SQL = ("SELECT XX_TRANSITTIMETT, XX_TIMEREGISTCELLATIONTRC, XX_NATIONALIZATIONTIMETNAC, XX_NACARRIVALTIMETEN, XX_INTERNACARRIVALTIMETEI, XX_IMPORTSLOGISTICSTIMETLI, C.XX_DISPATCHDATE "+
							"FROM XX_VLO_LEADTIMES A, C_ORDER C "+
							"WHERE C.DOCUMENTNO = '"+getDocumentNo()+"'"+
							"AND A.XX_VLO_LEADTIMES_ID ="+
							"("+SQL1+") "+
							"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
							"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")"
					);

					pstmt =null; 
					rs = null;
					try{

						pstmt = DB.prepareStatement(SQL, null); 
						rs = pstmt.executeQuery();

						if(rs.next())
						{
							TEI_B = rs.getBigDecimal("XX_INTERNACARRIVALTIMETEI");
							TT_B = rs.getBigDecimal("XX_TRANSITTIMETT");
							TRC_B = rs.getBigDecimal("XX_TIMEREGISTCELLATIONTRC");
							TNAC_B = rs.getBigDecimal("XX_NATIONALIZATIONTIMETNAC");
							TEN_B = rs.getBigDecimal("XX_NACARRIVALTIMETEN");
							TLI_B = rs.getBigDecimal("XX_IMPORTSLOGISTICSTIMETLI");
						}

					}catch (Exception e) {
						log.log(Level.SEVERE, SQL);
					} finally {
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}

					//Redondeo los bigdecimals
					TEI_B = TEI_B.setScale(0,BigDecimal.ROUND_UP);
					TT_B = TT_B.setScale(0,BigDecimal.ROUND_UP);
					TRC_B= TRC_B.setScale(0,BigDecimal.ROUND_UP);
					TNAC_B = TNAC_B.setScale(0,BigDecimal.ROUND_UP);
					TEN_B = TEN_B.setScale(0,BigDecimal.ROUND_UP);
					TLI_B = TLI_B.setScale(0,BigDecimal.ROUND_UP);

					Calendar date = Calendar.getInstance();
					Timestamp dateSUM = new Timestamp(0);	

					date.setTimeInMillis(getXX_CARAGENTDELIVREALDATE().getTime());

					//Hago las sumas para las fechas restantes

					//XX_SHIPPREALESTEEMEDDATE
					date.add(Calendar.DATE, TEI_B.intValue());
					dateSUM = new Timestamp(date.getTimeInMillis());
					setXX_CustDisEstiDatePO(dateSUM);

					//XX_VZLAARRIVALESTEMEDDATE
					date.add(Calendar.DATE, TT_B.intValue());
					dateSUM = new Timestamp(date.getTimeInMillis());
					setXX_EstArrivalDateToVzla(dateSUM);

					//XX_CustDutEstPayDate
					date.add(Calendar.DATE, TRC_B.intValue());
					dateSUM = new Timestamp(date.getTimeInMillis());
					setXX_CustDutEstPayDatePO(dateSUM);

					//XX_CustDutEstShipDate
					date.add(Calendar.DATE, TNAC_B.intValue());
					dateSUM = new Timestamp(date.getTimeInMillis());
					setXX_CustDutEstShipDatePO(dateSUM);

					//XX_CDARRIVALESTEEMEDDATE
					date.add(Calendar.DATE, TEN_B.intValue());
					dateSUM = new Timestamp(date.getTimeInMillis());

					//Se verifica que la fecha de llegada no sea un dia no laborable
					Utilities check = new Utilities();
					while(check.isNonBusinessDay(dateSUM)){
						date.add(Calendar.DATE, 1);
						dateSUM = new Timestamp(date.getTimeInMillis());
					}

					setXX_CDArrivalEstimatedDatePO(dateSUM);

				}

				//Fin de fechas

				////////Daniel

				if(getXX_VendorInvoiceAmount().compareTo(new BigDecimal(0)) == 1 && getXX_RealExchangeRate().compareTo(new BigDecimal(0)) == 1)
				{
					setXX_TotalVendInv(getXX_VendorInvoiceAmount().multiply(getXX_RealExchangeRate()));
				}

				/** 
				 * dpellegrino -> Valido que la fecha recepción de la factura del proveedor no sea mayor a
				 * la fecha de emición (Importaciones)   
				 * */
				// Se comento por que la Fecha de Emision de Proforma de Proveedor no se va a utilizar
				/*if(getXX_VENDPROFOREMISSIONDATE() != null && getXX_VENDPROFORMARECEPDATE() != null)
			 {
				 if(getXX_VENDPROFOREMISSIONDATE().compareTo(getXX_VENDPROFORMARECEPDATE()) == 1)
				 {
					 log.saveError("ErrorSql", Msg.getMsg(getCtx(), "XX_VendProEmRep"));
					 return false;
				 }
			 }*/

				if(getXX_VENDINVOICEEMISIONDATE() != null && getXX_VENDINVOICERECEPTDATE() != null)
				{
					if(getXX_VENDINVOICEEMISIONDATE().compareTo(getXX_VENDINVOICERECEPTDATE()) == 1)
					{
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), "XX_VendInvEmRep"));
						return false;
					}
				}

				/** Fin dpellegrino */	 



				// Validaciones solo para OC de Mercancía
				if(!docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){

					if (getC_Order_ID()!=0 && arrivalDate!=null)
					{

						Calendar fecha = Calendar.getInstance();

						Timestamp recienteSrv = new Timestamp(fecha.getTime().getTime());
						Long diferencia = arrivalDate.getTime()-recienteSrv.getTime();

						BigDecimal dias = ((new BigDecimal(diferencia)).multiply(new BigDecimal(1157))).multiply(new BigDecimal(0.00000000001));

						String TypeDelivery = getXX_VLO_TypeDelivery();

						if ((dias.compareTo(new BigDecimal(15))<0 && TypeDelivery.equals("PD")) || (TypeDelivery.equals("DD")  ))
						{
							setXX_PriceDefinitiveAllowed(true);			
						}
						else 
						{
							setXX_PriceDefinitiveAllowed(false);
						}
					}


					if(getXX_CARAGENTDELIVREALDATE() != null && isXX_DefinitiveCargoAg() == false)
					{					 
						String yearAux = null;
						String monthAux = null;
						String dayAux = null;
						String date = null;
						String Attachment = null;
						String status = "in now in 'Entregada al Agente de Carga' Status";

						String cargoAgent = getXX_CARAGENTDELIVREALDATE().toString();

						yearAux = cargoAgent.substring(0,4);
						monthAux = cargoAgent.substring(5,7);
						dayAux = cargoAgent.substring(8,10);
						date = "Cargo Agent Delivery Real Date: "+dayAux+"/"+monthAux+"/"+yearAux;

						setXX_DefinitiveCargoAg(true);
						setXX_OrderStatus("EAC");
						setXX_InsertedStatusDate(XXExitDate);
						// TODO SI improtaciones y comprador
						EnviarCorreosCambioStatusCoor(Attachment, status, date);
						EnviarCorreosCambioStatusJefeImport(Attachment, status, date);
						EnviarCorreosCambioStatusJefeCat(Attachment, status, date);
						EnviarCorreosCambioStatusCoorImport(Attachment, status, date);

					}// end if


					if(!newRecord){
						/**
						 * Jorge E. Pires G. --> Funcion 089. Envio de Correos por cambio de Estimated Date 
						 * 
						 **/
						Timestamp EstimatedViejo = ConsultaTablaC_OrderEstimatedDate();
						Timestamp EstimatedNuevo = getXX_EstimatedDate();

						if (EstimatedViejo != null && EstimatedNuevo != null){
							if (!EstimatedViejo.equals(EstimatedNuevo)){
								String Attachment = null;

								X_XX_VMR_Category Categoria =  new X_XX_VMR_Category(getCtx(), getXX_Category_ID(), null);
								X_XX_VMR_Department Departamento = new X_XX_VMR_Department(getCtx(), getXX_Department(), null);

								MBPartner Comprador = new MBPartner(getCtx(), getXX_UserBuyer_ID(), null) ;
								MBPartner JefeCategoria = new MBPartner(getCtx(), Categoria.getXX_CategoryManager_ID(), null) ;
								MBPartner PlanificadorInventario = new MBPartner(getCtx(), Departamento.getXX_InventorySchedule_ID(), null);
								//								MBPartner JefePlanificacion = new MBPartner(getCtx(), ConsultarJefePlanificacion(), null);

								EnviarCorreosCambiosFechas(Comprador.get_ID(), EstimatedViejo, EstimatedNuevo, Attachment);
								EnviarCorreosCambiosFechas(JefeCategoria.get_ID(), EstimatedViejo, EstimatedNuevo, Attachment);
								EnviarCorreosCambiosFechas(PlanificadorInventario.get_ID(), EstimatedViejo, EstimatedNuevo, Attachment);
								//								EnviarCorreosCambiosFechas(JefePlanificacion.get_ID(), EstimatedViejo, EstimatedNuevo, Attachment);
								
								// VLOMONACO 14/09/2012 Se envia correo al proveedor al cambiar la fecha estimada
								// JTRIAS 06/ 
								if (getXX_OrderType().equals("Nacional") && getXX_OrderStatus().equals("AP"))
									EnviarCorreoCambioFechaProv(getC_BPartner_ID(), EstimatedViejo, EstimatedNuevo, Attachment);
								// FIN VLOMONACO
							}					
						}

						/**
						 * Fin Jorge E. Pires G.
						 * */
					}// Fin If newRecord
					// Función 105 O/C de Bienes y servicios
					// La temporada se puede mostrar en O/C de BYS pero no es obligatoria
					// Se elimina el mandatory UI y se coloca una validación para O/C de Mercancía
					if(getXX_Season_ID() == 0){
						log.saveError("Error", Msg.getMsg(getCtx(), "XX_OCTemporada"));
						return false;
					}
				} // OC Mercancia
				// Validaciones solo para OC de BYS
				else {
					// Tomado de OC Mercandia
					if(getXX_CARAGENTDELIVREALDATE() != null && isXX_DefinitiveCargoAg() == false)
					{					 
						String yearAux = null;
						String monthAux = null;
						String dayAux = null;
						String date = null;
						String Attachment = null;
						String status = "in now in 'Entregada al Agente de Carga' Status";

						String cargoAgent = getXX_CARAGENTDELIVREALDATE().toString();

						yearAux = cargoAgent.substring(0,4);
						monthAux = cargoAgent.substring(5,7);
						dayAux = cargoAgent.substring(8,10);
						date = "Cargo Agent Delivery Real Date: "+dayAux+"/"+monthAux+"/"+yearAux;

						setXX_DefinitiveCargoAg(true);
						setXX_OrderStatus("EAC");
						setXX_InsertedStatusDate(XXExitDate);
						
						EnviarCorreosCambioStatusCoor(Attachment, status, date);
						EnviarCorreosCambioStatusJefeImport(Attachment, status, date);
						EnviarCorreosCambioStatusCoorImport(Attachment, status, date);

					}// end if
					Timestamp EstimatedViejo = ConsultaTablaC_OrderEstimatedDate();
					Timestamp EstimatedNuevo = getXX_EstimatedDate();

					if (EstimatedViejo != null && EstimatedNuevo != null){
						if (!EstimatedViejo.equals(EstimatedNuevo)){
							
							String Attachment = null;
							/** Purchase of Supplies and Services 
							 * Maria Vintimilla Funcion 016 **/
							if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){								
								Integer collaborator = getCreatedBy();
								EnviarCorreosCambiosFechas(collaborator, EstimatedViejo, EstimatedNuevo, Attachment);
								
								if (getXX_OrderType().equals("Importada")){	
									String sql = "SELECT B.C_BPARTNER_ID AS IMPORT " +
									" FROM C_BPARTNER B " +
									" WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
									" AND B.C_JOB_ID = " +
									" (SELECT C_JOB_ID " +
									" FROM C_JOB " +
									" WHERE UPPER(NAME) = UPPER('Jefe de Importaciones')" +
									" AND AD_Client_ID = " + Env.getCtx().getAD_Client_ID()+
									")" +
									" AND B.AD_Client_ID = " + Env.getCtx().getAD_Client_ID();
									PreparedStatement pstmt2 = null;
									ResultSet rs2 = null;
									
									try{
										pstmt2 = DB.prepareStatement(sql, null); 
										rs2 = pstmt2.executeQuery();	 
										while(rs2.next()){
											Integer JefeImportaciones = rs2.getInt("IMPORT");
											EnviarCorreosCambiosFechas(JefeImportaciones, EstimatedViejo, EstimatedNuevo, Attachment);
										}
									}
									catch (Exception e) {
										log.log(Level.SEVERE, sql);
									}
									finally{
										DB.closeResultSet(rs2);
										DB.closeStatement(pstmt2);
									}
								}// Fin If Tipo Orden
							}//if doctype
						} //compare estimated
					} // estimated
					
					// Funcion 105: OC de BYS
					if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE")) && getXX_PurchaseType().equals("SE")){
						setXX_VLO_TypeDelivery("CD");
						setM_Warehouse_ID(Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID"));
						setXX_EstimatedDate(XXExitDate);
					}
				}// OC BYS

				return true;
			}// Fin If isSOTrx
			else{
				return save;
			}
		}// Fin If Save

		return save;
	}

	private boolean cambioAlmacenValido() {
		// Inicio VLOMONACO
		// Si cambiaron el almacen destino y la orden ya estaba lista solo lo permito si fue el jefe de cd o el jefe de planificacion (solo si la orden
		// no ha sido recibida ni tiene distribución con estado distinto de anulada)
		
		// primero buscamos el valor viejo en la base de datos del m_warehouse
		String SQL = "Select m_warehouse_id from c_order where c_order_id=" + getC_Order_ID();
		int warehouseViejo = 0;
		CPreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{	
			pstmt = DB.prepareStatement(SQL, null);
			rs  = pstmt.executeQuery();

			if (rs.next()) {
				warehouseViejo = rs.getInt(1);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
		} finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if (warehouseViejo==0)
			return true;
		
		// buscamos si tiene alguna distribucion no anulada
		SQL = "Select xx_vmr_distributionheader_id from xx_vmr_distributionheader where xx_distributionstatus<>'AN' and c_order_id=" + getC_Order_ID();
		int distri = 0;
		try 
		{	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				distri = rs.getInt(1);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
		} finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		
		if (warehouseViejo!=getM_Warehouse_ID() && !getXX_OrderStatus().equals("PRO"))
		{
			if (!getCtx().getContext("#AD_Role_Name").equals("BECO Jefe de Planificacion") && !getCtx().getContext("#AD_Role_Name").equals("BECO Jefe de Centro de Distribución"))
			{
				log.saveError("Error",Msg.getMsg(getCtx(),"Solo el Jefe de Planificación o el Jefe de CD pueden modificar el almacen"));
				return false;
			} else
			{
				if (getXX_OrderStatus().equals("RE") || getXX_OrderStatus().equals("CH"))
				{
					log.saveError("Error",Msg.getMsg(getCtx(),"No puede ser modificado el almacén porque la orden ya ha sido recibida"));
					return false;
				}
				if (distri>0)
				{
					log.saveError("Error",Msg.getMsg(getCtx(),"El almacén no puede ser modificado ya que tiene la distribución No " + distri));
					return false;
				}
			}
			
		}		
		return true;
		// Fin VLOMONACO
	}

	// VLOMONACO envio al proveedor al cambiar las fechas estimadas
	private void EnviarCorreoCambioFechaProv(int prov, Timestamp EstimatedViejo,Timestamp EstimatedNuevo, String attachment) {
		
		String nombreProv = (new MBPartner(getCtx(), prov, null)).getName();
		
		String Mensaje = Msg.getMsg( getCtx(), 
				"XX_ChangeDateVendor", 
				new String[]{ getDocumentNo(), nombreProv,
			new String(""+EstimatedNuevo.getDate()+"-"+(EstimatedNuevo.getMonth()+1)+"-"+(EstimatedNuevo.getYear()+1900)), 
			new String(""+EstimatedViejo.getDate()+"-"+(EstimatedViejo.getMonth()+1)+"-"+(EstimatedViejo.getYear()+1900))
		});
		
		if(getAD_User_ID()!=0){
			
			Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, getAD_User_ID(), attachment);
			try {
				f.ejecutarMail();
			} catch (Exception e) {
				log.log(Level.SEVERE,e.getMessage());
			}
			f = null;
		}

	}

	/**
	 * Jorge E. Pires G. Función 164 
	 * 
	 **/
	private boolean EnvioCorreoStatusRecibida(Integer BPartner, String NumeroOC, String Attachment){
		String sql = "SELECT u.AD_User_ID "
			+ "FROM AD_User u "
			+ "WHERE IsActive = 'Y' " 
			+ "AND C_BPARTNER_ID = " + BPartner;


		MBPartner vendor = new MBPartner(getCtx(), getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( getCtx(), getXX_VMR_DEPARTMENT_ID(), null);
		X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), getXX_VMR_Package_ID(), null);
		X_XX_VMR_Collection collection = 
			new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);

		String Mensaje = Msg.getMsg( getCtx(), 
				"XX_ApprovePO", 
				new String[]{NumeroOC, 
			"Recibida", vendor.getName(), dep.getValue(), collection.getName()								  
		});

		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()){

				Integer UserAuxID = rs.getInt("AD_User_ID");
				Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
				f.ejecutarMail(); 
				f = null;
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}
	/**
	 * Jorge E. Pires G. 
	 **/

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		boolean save = super.afterSave(newRecord, success);

		if(save){		

			if(isSOTrx()==false){ //si es OC (PO)

				if (super.afterSave(newRecord, success)){ // si after save original

					if(getXX_OrderStatus().equals(X_Ref_XX_OrderStatus.RECIBIDA.getValue())){
						if(get_ValueOld("XX_OrderStatus").equals(X_Ref_XX_OrderStatus.APROBADA.getValue())){
							String Attachment = null;
							/** Purchase of Supplies and Services 
							 * Maria Vintimilla Funcion 016 **/
							String docType = getXX_POType();
							if(!docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
								/** Purchase of Supplies and Services 
								 * Maria Vintimilla Funcion 016 **/
								X_XX_VMR_Category Categoria =  new X_XX_VMR_Category(getCtx(), getXX_Category_ID(), null);
								X_XX_VMR_Department Departamento =  new X_XX_VMR_Department(getCtx(), getXX_Department(), null);
								
								MBPartner Comprador = new MBPartner(getCtx(), getXX_UserBuyer_ID(), null) ;
								MBPartner PlanificadorInventario = new MBPartner(getCtx(), Departamento.getXX_InventorySchedule_ID(), null) ;
								MBPartner JefeCategoria = new MBPartner(getCtx(), Categoria.getXX_CategoryManager_ID(), null) ;
								
								EnvioCorreoStatusRecibida(Comprador.get_ID(), getDocumentNo(), Attachment);
								EnvioCorreoStatusRecibida(PlanificadorInventario.get_ID(), getDocumentNo(), Attachment);
								EnvioCorreoStatusRecibida(JefeCategoria.get_ID(), getDocumentNo(), Attachment);
							}//Fin if DocType
						}// Fin if OrderStatus Aprobada
					}// Fin if OrderStatus


					if (!success || newRecord){ // si es un nuevo registro
						setXX_OrderStatus("PRO");
						save();
						return success;
					}else
					{
						if (isXX_Status_Sinc())
						{
							setXX_Status_Sinc(false);
							save();
						}
					}
					//Generamos los PO Limits solo si el registro no es nuevo y si no tiene POLimits pero si tiene Lineas
					//(Para las copias)
					if(!newRecord){
						//						if(hasOLinesWNPOLimits()){
						//							POLimits();
						//						}

						//Realizado por Rosmaira Arvelo
						Integer ct = getCriticalTaskForClose(get_ID());
						Integer cuenta = getAssociatedReferenceCount(get_ID());

						if((ct!=0)&&(cuenta==0))
						{
							MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),ct,null);

							//Llama al proceso cerrar alerta de Codificación de los productos
							if((isXX_Alert9()==true)&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("AS")))
							{
								try
								{
									MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task.get_ID()); 
									mpi.save();

									ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
									pi.setRecord_ID(mpi.getRecord_ID());
									pi.setAD_PInstance_ID(mpi.get_ID());
									pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
									pi.setClassName(""); 
									pi.setTitle(""); 

									ProcessCtl pc = new ProcessCtl(null ,pi,null); 
									pc.start();
								}
								catch(Exception e)
								{
									log.log(Level.SEVERE,e.getMessage());
								}
							}
						}//fin RArvelo
					}

					return true;

				}else{
					return false;	
				}

			}
			else //Si es OV (SO)
			{
				return true;
			}
		}
		return save;
	}

	@Override
	protected boolean beforeDelete(){
		if(isSOTrx()==false){ // si es Orden de Compra

			String SQL = "SELECT XX_OrderStatus"
				+ " FROM C_Order"
				+ " WHERE C_Order_ID=" + getC_Order_ID();
			PreparedStatement prst = DB.prepareStatement(SQL,null);
			String status = "";
			try {
				ResultSet rs = prst.executeQuery();
				if (rs.next()){
					status = rs.getString("XX_OrderStatus");
				}
				rs.close();
				prst.close();
			} catch (SQLException e){
				System.out.println("Error al intentar obtener el status de la orden de compra " + e);
			}

			if (!status.equals("PRO"))
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "La Orden de Compra debe estar en estado Proforma para ser borrada"));
				return false;
			}	

			/** Purchase of Supplies and Services 
			 * Maria Vintimilla Funcion 016 **/
			String docType = getXX_POType();
			if(docType.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
				//System.out.println("DocType es: Assets/Services before delete");
				SQL = " SELECT C_OrderLine_ID " +
						" FROM C_OrderLine " +
						" WHERE C_Order_ID=" + getC_Order_ID();
				
				prst = DB.prepareStatement(SQL,null);
				ResultSet rs = null;
				int result = 0;
				try {
					rs = prst.executeQuery();
					while (rs.next()){
						String sql = "Delete C_OrderLine Where C_OrderLine_ID="+rs.getInt("C_OrderLine_ID");
						try {
							result = DB.executeUpdateEx(sql, get_Trx());
						} 
						catch (SQLException e) { e.printStackTrace(); }

						if(result == -1){
							return false;
						}
					}
				} 
				catch (SQLException e){
					System.out.println("Error Deleting Order Lines " + e);
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(prst);
				}
			}// Fin If DocType
			else{
				// borro los limites
				String sql5 =
					"Delete XX_VMR_PO_APPROVAL " 
					+"where C_ORDER_ID="+getC_Order_ID();
				try {
					PreparedStatement pstmt5 = DB.prepareStatement(sql5, null);
					ResultSet del5 = pstmt5.executeQuery();					
					del5.close();
					pstmt5.close();
				} 
				catch (SQLException e)
				{
					System.out.println("error al intentar borrar los límites de la orden " + e);
				}

				SQL = "SELECT XX_VMR_PO_LineRefProv_ID"
					+ " FROM XX_VMR_PO_LineRefProv"
					+ " WHERE C_Order_ID=" + getC_Order_ID();
				prst = DB.prepareStatement(SQL,null);
				try {
					String sql = "";
					ResultSet rs = prst.executeQuery();
					while (rs.next()){
						sql =
							"Delete XX_VMR_REFERENCEMATRIX " 
							+"where XX_VMR_PO_LINEREFPROV_ID="+rs.getInt("XX_VMR_PO_LineRefProv_ID");

						PreparedStatement pstmt = DB.prepareStatement(sql, null);
						ResultSet del = pstmt.executeQuery();					
						del.close();
						pstmt.close();

					}
					rs.close();
					prst.close();
				} 
				catch (SQLException e){
					System.out.println("error al intentar borrar las matrices asociadas al detalle " + e);
				}

				SQL = "Delete XX_VMR_PO_LineRefProv" 
					+" where C_Order_ID="+getC_Order_ID();

				try {
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();					
					rs.close();
					pstmt.close();

				}
				catch(Exception e)	{	
					System.out.println("Error al borrar los detalles de la orden de compra " + e);
					return false;
				}    		

			}// Fin else DocType

			return true;
		}// Fin If O/C
		else{ // si es Orden de Venta
			return true;			
		}
	}	//	beforeDelete	

	// se puede borrar mas adelante
	//Tiene Lineas pero no tiene PO Limits
	private boolean hasOLinesWNPOLimits(){

		String SQL = "SELECT XX_VMR_PO_LineRefProv_ID " +
		"FROM XX_VMR_PO_LineRefProv " +
		"WHERE C_ORDER_ID = " +get_ID();

		boolean hasOLines = false;
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
			{
				hasOLines = true;
			}

			rs.close();
			pstmt.close();

		}catch (Exception e) {
			log.log(Level.SEVERE, "hasOLinesWNPOLimits() "+SQL);
		}

		if(hasOLines){

			SQL = "SELECT XX_VMR_PO_APPROVAL_ID " +
			"FROM XX_VMR_PO_APPROVAL " +
			"WHERE C_ORDER_ID = " +get_ID();

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(SQL, null); 
				rs = pstmt.executeQuery();

				if (rs.next())
				{
					return false;
				}
				else{
					return true;
				}

			}catch (Exception e) {
				log.log(Level.SEVERE, "hasOLinesWNPOLimits() "+SQL);
			}finally{

				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}

		return false;
	}

	//Realizado por Rosmaira Arvelo
	/*
	 *	Obtengo el ID de la tarea critica segun la Orden
	 */
	private Integer getCriticalTaskForClose(Integer orden){

		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			+ "WHERE XX_ActualRecord_ID="+orden;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();			

			while (rs.next())
			{				
				criticalTask = rs.getInt("XX_VMR_CriticalTaskForClose_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return criticalTask;
	}//fin getCriticalTask 

	/*
	 *	Obtengo la cantidad de detalles sin asociar de la O/C 
	 */
	private Integer getAssociatedReferenceCount(Integer orden){

		Integer count=0;

		String SQL = "SELECT COUNT(*) AS Cuenta "
			+ "FROM C_Order ord, XX_VMR_PO_LineRefProv asp " 
			+ "WHERE ord.C_Order_ID=asp.C_Order_ID " 
			+ "AND ord.C_Order_ID="+orden
			+ " AND asp.XX_ReferenceIsAssociated='N' " 
			+ "AND (ord.XX_OrderStatus='PRO' OR ord.XX_OrderStatus='PEN')";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();			

			while (rs.next())
			{				
				count = rs.getInt("Cuenta");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return count;
	}//fin getAssociatedReferenceCount RArvelo

	/*
	 * Gets y Sets adicionales a X_C_Order original
	 */

	public void setXX_ApprovalDate (Timestamp ApprovalDate) {
		set_Value ("XX_ApprovalDate", ApprovalDate);  
	} 

	public Timestamp getXX_ApprovalDate()  {
		return (Timestamp)get_Value("XX_ApprovalDate");
	}

	public void setXX_EPDate (Timestamp EPDate)
	{
		set_Value ("XX_EPDate", EPDate);

	} 

	public Timestamp getXX_EPDate() 
	{
		return (Timestamp)get_Value("XX_EPDate");

	}


	/** Set Close Guide Check.
	    @param XX_CloseGuideCheck Close Guide Check */
	public void setXX_CloseGuideCheck (boolean XX_CloseGuideCheck) {
		set_Value ("XX_CloseGuideCheck", Boolean.valueOf(XX_CloseGuideCheck));
	}

	/** Get Close Guide Check.
	    @return Close Guide Check */
	public boolean isXX_CloseGuideCheck() {
		return get_ValueAsBoolean("XX_CloseGuideCheck");
	}	

	/** Set Sync Check.
	    @param Sync  Check */
	public void setSync (boolean Sync) {
		set_Value ("Sync", Boolean.valueOf(Sync));
	}

	/** Get Sync Check.
	    @return Sync Check */
	public boolean isSync() {
		return get_ValueAsBoolean("Sync");
	}	

	/*
	 * Reception Date
	 */
	public void setXX_ReceiptOrderDate (Timestamp XX_ReceiptOrderDate) {
		set_Value ("XX_ReceiptOrderDate", XX_ReceiptOrderDate);
	} 

	public Timestamp getXX_ReceiptOrderDate()   {
		return (Timestamp)get_Value("XX_ReceiptOrderDate");
	}

	/*
	 * Rejection Date
	 */
	public void setXX_RejectionDate (Timestamp Date)  {
		set_Value ("XX_RejectionDate", Date);
	} 

	public Timestamp getXX_RejectionDate()  {
		return (Timestamp)get_Value("XX_RejectionDate");
	}

	public void setXX_InsertedStatusDate (Timestamp XX_InsertedStatusDate)  {
		set_Value ("XX_InsertedStatusDate", XX_InsertedStatusDate);
	} 

	public Timestamp getXX_InsertedStatusDate()  {
		return (Timestamp)get_Value("XX_InsertedStatusDate");
	}

	/*
	 * Checkup Date
	 */
	public void setXX_CheckupDate (Timestamp XX_CheckupDate){
		set_Value ("XX_CheckupDate", XX_CheckupDate);
	} 

	public Timestamp getXX_CheckupDate() {
		return (Timestamp)get_Value("XX_CheckupDate");    
	}

	/** Set XX_Evaluated.
    @param XX_Evaluated  */
	public void setXX_Evaluated (boolean XX_Evaluated)   {
		set_Value ("XX_Evaluated", Boolean.valueOf(XX_Evaluated));
	}

	/** Get XX_Evaluated.
    @return XX_Evaluated */
	public boolean isXX_Evaluated()    {
		return get_ValueAsBoolean("XX_Evaluated"); 
	}

	/** Set OrderStatus.
    @param OrderStatus of the Purchase Order */
	public void setXX_OrderStatus (String OrderStatus)  {
		set_Value ("XX_OrderStatus", OrderStatus);
	}

	/** Get OrderStatus.
    @return OrderStatus of the Purchase Order */
	public String getXX_OrderStatus()   {
		return get_ValueAsString("XX_OrderStatus");
	}

	/** Set XX_StatusWhenReceipt.
    @param XX_StatusWhenReceipt of the Purchase Order */
	public void setXX_StatusWhenReceipt (String OrderStatus)
	{
		set_Value ("XX_StatusWhenReceipt", OrderStatus);

	}

	/** Get XX_StatusWhenReceipt.
    @return XX_StatusWhenReceipt of the Purchase Order */
	public String getXX_StatusWhenReceipt() 
	{
		return get_ValueAsString("XX_StatusWhenReceipt");

	}


	/** Set XX_OrderReadyStatus.
    @param XX_OrderReadyStatus  */
	public void setXX_OrderReadyStatus (boolean val)   {
		set_Value ("XX_OrderReadyStatus", Boolean.valueOf(val));
	}

	/** Get XX_OrderReadyStatus.
    @return XX_OrderReadyStatus */
	public boolean isXX_OrderReadyStatus()    {
		return get_ValueAsBoolean("XX_OrderReadyStatus");
	}

	/** Set XX_OrderReady.
    @param XX_OrderReady  */
	public void setXX_OrderReady (String val)
	{
		set_Value ("XX_OrderReady", val);

	}

	/** Get XX_OrderReady.
    @return XX_OrderReady */
	public boolean isXX_OrderReady() 
	{
		return get_ValueAsBoolean("XX_OrderReady");

	}

	// Patricia
	/** Get InvoicingStatus.
    @return InvoicingStatus of the Purchase Order */
	public String getXX_InvoicingStatus()  {
		return get_ValueAsString("XX_InvoicingStatus");  
	}

	/** Set InvoicingStatus.
    @param InvoicingStatus of the Purchase Order */
	public void setXX_InvoicingStatus (String InvoicingStatus)    {
		set_Value ("XX_InvoicingStatus", InvoicingStatus);
	}

	public void setXX_VLO_BOARDINGGUIDE_ID(int id)   {
		if (id <= 0) set_Value ("XX_VLO_BoardingGuide_ID", null);
		else
			set_Value ("XX_VLO_BoardingGuide_ID", Integer.valueOf(id));   	
	}

	public int getXX_VLO_BOARDINGGUIDE_ID()   {
		return get_ValueAsInt("XX_VLO_BoardingGuide_ID");      
	} 

	// Daniel
	public void setXX_TotalVendInv (java.math.BigDecimal XX_TotalVendInv)    {
		set_Value ("XX_TotalVendInv", XX_TotalVendInv); 
	}

	public java.math.BigDecimal getXX_TotalVendInv()  {
		return get_ValueAsBigDecimal("XX_TotalVendInv");   
	}

	/** Get XX_StoreDistribution.
    @return XX_StoreDistribution of the Purchase Order */
	/**
    public String getXX_StoreDistribution() 
    {
        return get_ValueAsString("XX_StoreDistribution");

    }
	 */    

	/** Get XX_ArrivalDate.
    @return XX_ArrivalDate of the Purchase Order */
	public Timestamp getXX_ArrivalDate()  {
		return (Timestamp)get_Value("XX_ArrivalDate");
	}

	/** Get XX_EstimatedDate.
    @return XX_EstimatedDate of the Purchase Order */
	public Timestamp getXX_EstimatedDate()  {
		return (Timestamp)get_Value("XX_EstimatedDate");  
	}

	public void setXX_OrderType (String orderType)
	{
		set_Value ("XX_OrderType", orderType);
	}

	public String getXX_OrderType() 
	{
		return get_ValueAsString("XX_OrderType");      
	}

		/** 
	 * XX_ComesFromCopy Get.
	 */
	public boolean isXX_ComesFromCopy()  {
		return get_ValueAsBoolean("XX_ComesFromCopy");   
	}

	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 006**/

	/** Set XX_PurchaseType.
	@param XX_PurchaseType Purchase Type on MOrder */   
	public void setXX_PurchaseType (String PurchaseType) {
		set_Value ("XX_PurchaseType", PurchaseType);
	}

	/** Get XX_PurchaseType.
	@return XX_PurchaseType Purchase Type on MOrder */
	public String getXX_PurchaseType(){
		return get_ValueAsString("XX_PurchaseType");      
	}

	/** Get Estimated Margin.
    @return Estimated Margin of the Purchase Order */
	public java.math.BigDecimal getXX_EstimatedMargin()   {
		return get_ValueAsBigDecimal("XX_EstimatedMargin");       
	}

	/** Set Estimated Margin.
    @param Estimated Margin of the Purchase Order */
	public void setXX_EstimatedMargin (java.math.BigDecimal XX_EstimatedMargin)   {
		set_Value ("XX_EstimatedMargin", XX_EstimatedMargin);
	}

	/** Get Definitive Factor.
    @return Definitive Factor of the Purchase Order */
	public java.math.BigDecimal getXX_DefinitiveFactor() 
	{
		return get_ValueAsBigDecimal("XX_DefinitiveFactor");
	}

	/** Set Definitive Factor.
    @param Definitive Factor of the Purchase Order */
	public void setXX_DefinitiveFactor (java.math.BigDecimal XX_DefinitiveFactor)
	{
		set_Value ("XX_DefinitiveFactor", XX_DefinitiveFactor);
	}

	/** Get Estimated Factor.
    @return Estimated Margin of the Purchase Order */
	public java.math.BigDecimal getXX_EstimatedFactor() 
	{
		return get_ValueAsBigDecimal("XX_EstimatedFactor");
	}

	/** Set Estimated Factor.
    @param Estimated Margin of the Purchase Order */
	public void setXX_EstimatedFactor (java.math.BigDecimal XX_EstimatedFactor)
	{
		set_Value ("XX_EstimatedFactor", XX_EstimatedFactor);
	}

	/** Get Replacement Factor.
    @return Estimated Margin of the Purchase Order */
	public java.math.BigDecimal getXX_ReplacementFactor() 
	{
		return get_ValueAsBigDecimal("XX_ReplacementFactor");
	}

	/** Set Replacement Factor.
    @param Estimated Margin of the Purchase Order */
	public void setXX_ReplacementFactor (java.math.BigDecimal XX_ReplacementFactor)
	{
		set_Value ("XX_ReplacementFactor", XX_ReplacementFactor);
	}

	/** Get Country.
    @return Country */
	public int getC_Country_ID() 
	{
		return get_ValueAsInt("C_Country_ID");
	}

	/** Set XX_VLO_DispatchRoute_ID.
    @param XX_VLO_DispatchRoute_ID ID de vía de despacho */
	public void setXX_VLO_DispatchRoute_ID (int XX_VLO_DispatchRoute_ID)
	{
		if (XX_VLO_DispatchRoute_ID < 1) throw new IllegalArgumentException ("XX_VLO_DispatchRoute_ID is mandatory.");
		set_Value ("XX_VLO_DispatchRoute_ID", Integer.valueOf(XX_VLO_DispatchRoute_ID));
	}

	/** Get XX_VLO_DispatchRoute_ID.
    @return ID de vía de despacho */
	public int getXX_VLO_DispatchRoute_ID() 
	{
		return get_ValueAsInt("XX_VLO_DispatchRoute_ID");

	}

	/** Set XX_VMR_DEPARTMENT_ID.
    @param XX_VMR_DEPARTMENT_ID XX_VMR_DEPARTMENT_ID */
	public void setXX_VMR_DEPARTMENT_ID (int XX_VMR_DEPARTMENT_ID)
	{
		if (XX_VMR_DEPARTMENT_ID < 1) throw new IllegalArgumentException ("XX_VMR_DEPARTMENT_ID is mandatory.");
		set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_DEPARTMENT_ID));
	}

	/** Get XX_VMR_DEPARTMENT_ID.
    @return XX_VMR_DEPARTMENT_ID */
	public int getXX_VMR_DEPARTMENT_ID() {
		return get_ValueAsInt("XX_VMR_Department_ID");
	}

	/** Set XX_MotivChanEstimDate_ID.
    @param XX_MotivChanEstimDate_ID */
	public void setXX_MotivChanEstimDate_ID (int XX_MotivChanEstimDate_ID)
	{
		set_Value ("XX_MotivChanEstimDate_ID", Integer.valueOf(XX_MotivChanEstimDate_ID));
	}

	/** Get XX_MotivChanEstimDate_ID.
    @param XX_MotivChanEstimDate_ID */
	public int getXX_MotivChanEstimDate_ID()
	{
		return get_ValueAsInt("XX_MotivChanEstimDate_ID"); 
	}

	/** Set     XX_ShowMotiveChange.
    @param     XX_ShowMotiveChange */
	public void setXX_ShowMotiveChange(String motive)
	{
		set_Value ("XX_ShowMotiveChange", motive);
	}   

	public void setXX_Motive_ID(int id)  {
		if (id <= 0) set_Value ("XX_Motive_ID", null);
		else
			set_Value ("XX_Motive_ID", Integer.valueOf(id));   	
	}

	public int getXX_Motive_ID() {
		return get_ValueAsInt("XX_Motive_ID");      
	} 

	public void setXX_Alert(String check) {
		set_Value ("XX_Alert", check);   	
	}

	public String getXX_Alert()  {
		return get_ValueAsString("XX_Alert");      
	} 

	public void setXX_Alert2(String check) {
		set_Value ("XX_Alert2", check);   	
	}

	// GMARQUES
	public void setXX_CT_Responsible_ID(int role) {
		set_Value ("XX_CT_Responsible_ID", Integer.valueOf(role));   	
	}
	
	//GMARQUES
	public int getXX_CT_Responsible_ID()  {
		return get_ValueAsInt("XX_CT_Responsible_ID");      
	} 
	
	public String getXX_Alert2()  {
		return get_ValueAsString("XX_Alert2");      
	} 

	public void setXX_Alert3(String check) {
		set_Value ("XX_Alert3", check);   	
	}

	public void setXX_VMR_CancellationMotive_ID(int check) {
		set_Value ("XX_VMR_CancellationMotive_ID", Integer.valueOf(check));   	
	}

	public int getXX_VMR_CancellationMotive_ID()  {
		return get_ValueAsInt("XX_VMR_CancellationMotive_ID");      
	} 

	/** Set XX_Copy */
	public void setXX_Copy (String XX_Copy)  {
		set_Value ("XX_Copy", XX_Copy);
	}

	/** Get XX_Copy
	 * 
	 * @return
	 */
	public String getXX_Copy()  {
		return (String)get_Value("XX_Copy");
	}

	/** 
	 * Set y Get de XX_OrderNotReady
	 */
	public void setXX_OrderNotReady(String val)  {
		set_Value ("XX_OrderNotReady", val);    
	}

	public String getXX_OrderNotReady()  {
		return (String)get_Value("XX_OrderNotReady");
	}

	public void setXX_Annul(char check) {
		set_Value ("XX_Annul", check);   	
	}

	public char getXX_Annul()  {
		return get_ValueAsString("XX_Annul").charAt(0);      
	} 

	public void setXX_Void (boolean voided) {
		set_ValueNoCheck ("XX_Void", Boolean.valueOf(voided));
	}

	public boolean getXX_Void()    {
		return get_ValueAsBoolean("XX_Void");
	}

	public void setXX_FirstManager (boolean preapproved)   {
		set_ValueNoCheck ("XX_FirstManager", Boolean.valueOf(preapproved));       
	}

	public boolean getXX_FirstManager()  {
		return get_ValueAsBoolean("XX_FirstManager");   
	}

	public void setXX_ProductQuantity(BigDecimal cantidad)  {
		if (cantidad.compareTo(new BigDecimal(0)) == -1 ) set_Value ("XX_ProductQuantity", new BigDecimal(0));
		else
			set_Value ("XX_ProductQuantity", cantidad);   	
	}

	public BigDecimal getXX_ProductQuantity()    {
		return get_ValueAsBigDecimal("XX_ProductQuantity");      
	} 

	public void setTotalPVP(BigDecimal monto)
	{
		set_Value ("TotalPVP", monto);   	
	}

	public BigDecimal getTotalPVP()     {
		return get_ValueAsBigDecimal("TotalPVP");      
	} 

	public void setXX_TotalPVPPlusTax(BigDecimal monto)   {
		set_Value ("XX_TotalPVPPlusTax", monto);   	
	}

	public BigDecimal getXX_TotalPVPPlusTax()    {
		return get_ValueAsBigDecimal("XX_TotalPVPPlusTax");      
	}   


	public void setXX_MontLimit(BigDecimal monto)   {
		set_Value ("XX_MontLimit", monto);   	
	}

	public BigDecimal getXX_MontLimit()    {
		return get_ValueAsBigDecimal("XX_MontLimit");      
	} 

	//Delivery Type
	public String getXX_VLO_TypeDelivery()    {
		return get_ValueAsString("XX_VLO_TypeDelivery");      
	} 

	public void setXX_VLO_TypeDelivery(String valor)   {
		set_Value ("XX_VLO_TypeDelivery", valor);   	
	}

	public void setXX_EstimatedDate(Timestamp fecha)   {
		set_Value ("XX_EstimatedDate", fecha);   	
	}

	public void setXX_ArrivalDate(Timestamp fecha)   {
		set_Value ("XX_ArrivalDate", fecha);   	
	}

	public void setXX_Department (int Department_ID)   {
		if (Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
		else
			set_Value ("XX_VMR_Department_ID", Integer.valueOf(Department_ID));
	}

	public int getXX_Department()    {
		return get_ValueAsInt("XX_VMR_Department_ID");
	}

	public void setXX_PODistribute(String check)    {
		set_Value ("XX_PODistribute", check);   	
	}

	public String getXX_PODistribute()     {
		return get_ValueAsString("XX_PODistribute");      
	} 

	public void setXX_Discount2(BigDecimal discount)    {
		if (discount.intValue() <= 0) set_Value("XX_Discount2", new BigDecimal(0));
		else
			set_Value ("XX_Discount2", discount);   	
	}

	public BigDecimal getXX_Discount2()     {
		return get_ValueAsBigDecimal("XX_Discount2");      
	}

	public void setXX_PurchaseOrderComments(String comentario)    {
		set_Value ("XX_PurchaseOrderComments", comentario);   	
	}

	public String getXX_PurchaseOrderComments()     {
		return get_ValueAsString("XX_PurchaseOrderComments");      
	}

	public void setXX_Brochure_ID(int brochure)    {
		if (brochure <= 0) set_Value ("XX_VMA_Brochure_ID", null);
		else
			set_Value ("XX_VMA_Brochure_ID", Integer.valueOf(brochure));   	
	}

	public int getXX_Brochure_ID()     {
		return get_ValueAsInt("XX_VMA_Brochure_ID");      
	}  

	public void setXX_VLO_ArrivalPort_ID(int puerta)    {
		if (puerta <= 0) set_Value ("XX_VLO_ArrivalPort_ID", null);
		else
			set_Value ("XX_VLO_ArrivalPort_ID", Integer.valueOf(puerta));   	
	}

	public int getXX_VLO_ArrivalPort_ID()     {
		return get_ValueAsInt("XX_VLO_ArrivalPort_ID");      
	} 

	public void setXX_ChangeStatus(char car)    {
		set_Value ("XX_ChangeStatus", car);   	
	}

	public char getXX_ChangeStatus()     {
		return get_ValueAsString("XX_ChangeStatus").charAt(0);      
	}

	public void setXX_ConsigneeName (String name)    {
		set_Value ("XX_ConsigneeName", name);
	}

	public String getXX_ConsigneeName ()    {
		return get_ValueAsString("XX_ConsigneeName");
	}

	public void setXX_Discount3(BigDecimal discount)    {
		if (discount.intValue() <= 0) set_Value ("XX_Discount3", new BigDecimal(0));
		else
			set_Value ("XX_Discount3", discount);   	
	}

	public BigDecimal getXX_Discount3()   {
		return get_ValueAsBigDecimal("XX_Discount3");      
	}

	public void setXX_DispatchDate(Timestamp fecha)    {
		set_Value ("XX_DispatchDate", fecha);   	
	}

	public Timestamp getXX_DispatchDate()     {
		return (Timestamp)get_Value("XX_DispatchDate");      
	} 

	public void setC_Country_ID (int country_ID)    {
		if (country_ID <= 0) set_Value ("C_Country_ID", null);
		else
			set_Value ("C_Country_ID", Integer.valueOf(country_ID));
	}

	public void setXX_Discount4(BigDecimal discount)    {
		if (discount.intValue() <= 0) set_Value ("XX_Discount4", new BigDecimal(0));
		else
			set_Value ("XX_Discount4", discount);   	
	}

	public BigDecimal getXX_Discount4()     {
		return get_ValueAsBigDecimal("XX_Discount4");      
	}

	public void setXX_ConversionRate_ID(int rate)    {
		if (rate <= 0) set_Value ("XX_ConversionRate_ID", null);
		else
			set_Value ("XX_ConversionRate_ID", Integer.valueOf(rate));   	
	}

	public int getXX_ConversionRate_ID()     {
		return get_ValueAsInt("XX_ConversionRate_ID");      
	} 

	public void setXX_ConsignmentDate(Timestamp fecha)    {
		set_Value ("XX_ConsignmentDate", fecha);   	
	}

	public Timestamp getXX_ConsignmentDate()     {
		return (Timestamp)get_Value("XX_ConsignmentDate");      
	} 

	public void setXX_Collection_ID(int coleccion)    {
		if (coleccion <= 0) set_Value ("XX_VMR_Collection_ID", null);
		else
			set_Value ("XX_VMR_Collection_ID", Integer.valueOf(coleccion));   	
	}

	public int getXX_Collection_ID()     {
		return get_ValueAsInt("XX_VMR_Collection_ID");      
	} 

	public void setXX_VMR_Package_ID(int paquete)    {
		if (paquete <= 0) set_Value ("XX_VMR_Package_ID", null);
		else
			set_Value ("XX_VMR_Package_ID", Integer.valueOf(paquete));   	
	}

	public int getXX_VMR_Package_ID()     {
		return get_ValueAsInt("XX_VMR_Package_ID");      
	} 

	/**   
    public void setXX_StoreDistribution (boolean atienda)
    {
        set_ValueNoCheck ("XX_StoreDistribution", Boolean.valueOf(atienda));

    }


    public boolean getXX_StoreDistribution() 
    {
        return get_ValueAsBoolean("XX_StoreDistribution");

    }
	 */   
	public void setXX_StoreDistribution (String atienda)
	{
		set_ValueNoCheck ("XX_StoreDistribution", atienda);
	}


	public String getXX_StoreDistribution() 
	{
		return get_ValueAsString("XX_StoreDistribution");
	}


	public void setXX_VLO_ShippingCondition_ID(int condicion)
	{
		if (condicion <= 0) set_Value ("XX_VLO_ShippingCondition_ID", null);
		else
			set_Value ("XX_VLO_ShippingCondition_ID", Integer.valueOf(condicion));   	
	}

	public int getXX_VLO_ShippingCondition_ID() 
	{
		return get_ValueAsInt("XX_VLO_ShippingCondition_ID");      
	} 

	public void setXX_VMR_Subject_ID(int subject)
	{
		if (subject <= 0) set_Value ("XX_VMR_Subject_ID", null);
		else
			set_Value ("XX_VMR_Subject_ID", Integer.valueOf(subject));   	
	}

	public int getXX_VMR_Subject_ID() 
	{
		return get_ValueAsInt("XX_VMR_Subject_ID");      
	} 

	public void setXX_VLO_DeliveryLocation_ID(int location)
	{
		if (location <= 0) set_Value ("XX_VLO_DeliveryLocation_ID", null);
		else
			set_Value ("XX_VLO_DeliveryLocation_ID", Integer.valueOf(location));   	
	}

	public int getXX_VLO_DeliveryLocation_ID() 
	{
		return get_ValueAsInt("XX_VLO_DeliveryLocation_ID");      
	} 

	public void setXX_Discount1(BigDecimal discount)
	{
		if (discount.intValue() <= 0) set_Value ("XX_Discount1", new BigDecimal(0));
		else
			set_Value ("XX_Discount1", discount);   	
	}

	public BigDecimal getXX_Discount1() 
	{
		return get_ValueAsBigDecimal("XX_Discount1");      
	}

	public void setXX_BuyersComments(String comentario)
	{
		set_Value ("XX_BuyersComments", comentario);   	
	}

	public String getXX_BuyersComments() 
	{
		return get_ValueAsString("XX_BuyersComments");      
	}

	/**
	 * XX_OrderNotReadyReason
	 * @param XX_OrderNotReadyReason
	 */
	public void setXX_OrderNotReadyReason(String comentario)
	{
		set_Value ("XX_OrderNotReadyReason", comentario);   	
	}

	public String getXX_OrderNotReadyReason() 
	{
		return get_ValueAsString("XX_OrderNotReadyReason");      
	}

	public void setXX_Season_ID(int season)
	{
		if (season <= 0) set_Value ("XX_VMA_Season_ID", null);
		else
			set_Value ("XX_VMA_Season_ID", Integer.valueOf(season));   	
	}

	public int getXX_Season_ID() 
	{
		return get_ValueAsInt("XX_VMA_Season_ID");      
	}  

	public void setXX_Category_ID(int categoria)
	{
		if (categoria <= 0) set_Value ("XX_VMR_Category_ID", null);
		else
			set_Value ("XX_VMR_Category_ID", Integer.valueOf(categoria));   	
	}

	public int getXX_Category_ID() 
	{
		return get_ValueAsInt("XX_VMR_Category_ID");      
	} 

	public void setXX_IsAssociated(String asociado)
	{
		set_Value ("XX_IsAssociated", asociado);   	
	}

	public String getXX_IsAssociated() 
	{
		return get_ValueAsString("XX_IsAssociated");      
	}

	public int getAssociation_ID() 
	{
		return get_ValueAsInt("ASSOCIATION_ID");      
	}  

	public void setAssociation_ID(int numero_assoc)
	{
		set_Value ("ASSOCIATION_ID", numero_assoc);

	}

	/** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
	public void setValue (String Value)
	{
		set_Value ("Value", Value);

	}

	/** Get Search Key.
    @return Search key for the record in the format required - must be unique */
	public String getValue() 
	{
		return get_ValueAsString("Value");

	}

	public void setXX_UserBuyer_ID(int XX_UserBuyer_ID)
	{
		if(XX_UserBuyer_ID <= 0)
			set_Value ("XX_UserBuyer_ID", null);
		else
			set_Value ("XX_UserBuyer_ID", Integer.valueOf(XX_UserBuyer_ID));   	
	}

	public int getXX_UserBuyer_ID() 
	{
		return get_ValueAsInt("XX_UserBuyer_ID");      
	} 
	/**
	 * Set
	 * @param XX_ImportingCompany_ID
	 */
	public void setXX_ImportingCompany_ID (int XX_ImportingCompany_ID)
	{
		set_Value ("XX_ImportingCompany_ID", Integer.valueOf(XX_ImportingCompany_ID));   	
	}
	/**
	 * get
	 * @return XX_ImportingCompany_ID
	 */
	public int getXX_ImportingCompany_ID() 
	{
		return get_ValueAsInt("XX_ImportingCompany_ID");      
	} 

	/**
	 * XX_CARAGENTDELIVREALDATE
	 * */
	public void setXX_CARAGENTDELIVREALDATE(Timestamp fecha)
	{
		set_Value ("XX_CARAGENTDELIVREALDATE", fecha);	
	}

	public Timestamp getXX_CARAGENTDELIVREALDATE() 
	{
		return (Timestamp)get_Value("XX_CARAGENTDELIVREALDATE");      
	} 

	/**
	 * XX_SHIPPREALESTEEMEDDATE
	 * */
	public void setXX_SHIPPREALESTEEMEDDATE(Timestamp fecha)
	{
		set_Value ("XX_SHIPPREALESTEEMEDDATE", fecha);	
	}

	public Timestamp getXX_SHIPPREALESTEEMEDDATE() 
	{
		return (Timestamp)get_Value("XX_SHIPPREALESTEEMEDDATE");      
	}

	/**
	 * XX_VZLAARRIVALESTEMEDDATE
	 * */
	public void setXX_VZLAARRIVALESTEMEDDATE(Timestamp fecha)
	{
		set_Value ("XX_VZLAARRIVALESTEMEDDATE", fecha);	
	}

	public Timestamp getXX_VZLAARRIVALESTEMEDDATE() 
	{
		return (Timestamp)get_Value("XX_VZLAARRIVALESTEMEDDATE");      
	} 

	/**
	 * XX_CustDutEstPayDate
	 * */
	public void setXX_CustDutEstPayDate(Timestamp fecha)
	{
		set_Value ("XX_CustDutEstPayDate", fecha);	
	}

	public Timestamp getXX_CustDutEstPayDate() 
	{
		return (Timestamp)get_Value("XX_CustDutEstPayDate");      
	} 

	/**
	 * XX_CustDutEstShipDate
	 * */
	public void setXX_CustDutEstShipDate(Timestamp fecha)
	{
		set_Value ("XX_CustDutEstShipDate", fecha);	
	}

	public Timestamp getXX_CustDutEstShipDate() 
	{
		return (Timestamp)get_Value("XX_CustDutEstShipDate");      
	} 

	/**
	 * XX_CDARRIVALESTEEMEDDATE
	 * */
	public void setXX_CDARRIVALESTEEMEDDATE(Timestamp fecha)
	{
		set_Value ("XX_CDARRIVALESTEEMEDDATE", fecha);	
	}

	public Timestamp getXX_CDARRIVALESTEEMEDDATE() 
	{
		return (Timestamp)get_Value("XX_CDARRIVALESTEEMEDDATE");      
	} 

	/**
	 * XX_CARAGENTDELIVESTEMEDDATE
	 * */
	public void setXX_CARAGENTDELIVESTEMEDDATE(Timestamp fecha)
	{
		set_Value ("XX_CARAGENTDELIVESTEMEDDATE", fecha);	
	}

	public Timestamp getXX_CARAGENTDELIVESTEMEDDATE() 
	{
		return (Timestamp)get_Value("XX_CARAGENTDELIVESTEMEDDATE");      
	} 

	/**
	 * XX_CustDutEstShipDatePO
	 * */
	public void setXX_CustDutEstShipDatePO(Timestamp fecha)
	{
		set_Value ("XX_CustDutShipEstiDatePO", fecha);	
	}

	public Timestamp getXX_CustDutEstShipDatePO() 
	{
		return (Timestamp)get_Value("XX_CustDutShipEstiDatePO");      
	} 

	/**
	 * XX_EstArrivalDateToVzla
	 * */
	public void setXX_EstArrivalDateToVzla(Timestamp fecha)
	{
		set_Value ("XX_EstArrivalDateToVzla", fecha);	
	}

	public Timestamp getXX_EstArrivalDateToVzla() 
	{
		return (Timestamp)get_Value("XX_EstArrivalDateToVzla");      
	} 

	/**
	 * XX_CustDutEstPayDatePO
	 * */
	public void setXX_CustDutEstPayDatePO(Timestamp fecha)
	{
		set_Value ("XX_CustDutEstPayDatePO", fecha);	
	}

	public Timestamp getXX_CustDutEstPayDatePO() 
	{
		return (Timestamp)get_Value("XX_CustDutEstPayDatePO");      
	} 

	/**
	 * XX_CUSTDISESTIDATEPO
	 * */
	public void setXX_CustDisEstiDatePO(Timestamp fecha)
	{
		set_Value ("XX_CustDisEstiDatePO", fecha);	
	}

	public Timestamp getXX_CustDisEstiDatePO() 
	{
		return (Timestamp)get_Value("XX_CustDisEstiDatePO");      
	} 

	/**
	 * XX_CDArrivalEstimatedDatePO
	 * */
	public void setXX_CDArrivalEstimatedDatePO(Timestamp fecha)
	{
		set_Value ("XX_CDArrivalEstimatedDatePO", fecha);	
	}

	public Timestamp getXX_CDArrivalEstimatedDatePO() 
	{
		return (Timestamp)get_Value("XX_CDArrivalEstimatedDatePO");      
	} 

	/**
	 * XX_VENDPROFOREMISSIONDATE
	 * */
	public void setXX_VENDPROFOREMISSIONDATE(Timestamp fecha)
	{
		set_Value ("XX_VENDPROFOREMISSIONDATE", fecha);	
	}

	public Timestamp getXX_VENDPROFOREMISSIONDATE() 
	{
		return (Timestamp)get_Value("XX_VENDPROFOREMISSIONDATE");      
	} 

	/**
	 * XX_VENDPROFORMARECEPDATE
	 * */
	public void setXX_VENDPROFORMARECEPDATE(Timestamp fecha)
	{
		set_Value ("XX_VENDPROFORMARECEPDATE", fecha);	
	}

	public Timestamp getXX_VENDPROFORMARECEPDATE() 
	{
		return (Timestamp)get_Value("XX_VENDPROFORMARECEPDATE");      
	} 

	/**
	 * XX_CADIVIAPPLICATIONDATE
	 * */
	public void setXX_CADIVIAPPLICATIONDATE(Timestamp fecha)
	{
		set_Value ("XX_CADIVIAPPLICATIONDATE", fecha);	
	}

	public Timestamp getXX_CADIVIAPPLICATIONDATE() 
	{
		return (Timestamp)get_Value("XX_CADIVIAPPLICATIONDATE");      
	} 

	/**
	 * XX_ADDDATE
	 * */
	public void setXX_ADDDATE(Timestamp fecha)
	{
		set_Value ("XX_ADDDATE", fecha);	
	}

	public Timestamp getXX_ADDDATE() 
	{
		return (Timestamp)get_Value("XX_ADDDATE");      
	} 

	/**
	 * XX_VENDINVOICEEMISIONDATE
	 * */
	public void setXX_VENDINVOICEEMISIONDATE(Timestamp fecha)
	{
		set_Value ("XX_VENDINVOICEEMISIONDATE", fecha);	
	}

	public Timestamp getXX_VENDINVOICEEMISIONDATE() 
	{
		return (Timestamp)get_Value("XX_VENDINVOICEEMISIONDATE");      
	} 

	/**
	 * XX_VENDINVOICERECEPTDATE
	 * */
	public void setXX_VENDINVOICERECEPTDATE(Timestamp fecha)
	{
		set_Value ("XX_VENDINVOICEEMISIONDATE", fecha);	
	}

	public Timestamp getXX_VENDINVOICERECEPTDATE() 
	{
		return (Timestamp)get_Value("XX_VENDINVOICERECEPTDATE");      
	} 

	/**
	 * XX_INTNACESTMEDAMOUNT
	 * */
	public java.math.BigDecimal getXX_INTNACESTMEDAMOUNT()
	{
		return get_ValueAsBigDecimal("XX_INTNACESTMEDAMOUNT");
	}

	public void setXX_INTNACESTMEDAMOUNT(java.math.BigDecimal XX_INTNACESTMEDAMOUNT)
	{
		set_Value ("XX_INTNACESTMEDAMOUNT", XX_INTNACESTMEDAMOUNT);
	}

	/**
	 * XX_NationalEsteemedAmount
	 * */
	public java.math.BigDecimal getXX_NationalEsteemedAmount()
	{
		return get_ValueAsBigDecimal("XX_NationalEsteemedAmount");
	}

	public void setXX_NationalEsteemedAmount(java.math.BigDecimal XX_NationalEsteemedAmount)
	{
		set_Value ("XX_NationalEsteemedAmount", XX_NationalEsteemedAmount);
	}

	/**
	 * XX_NatTreasuryEstAmount
	 * */
	public java.math.BigDecimal getXX_NatTreasuryEstAmount()
	{
		return get_ValueAsBigDecimal("XX_NatTreasuryEstAmount");
	}

	public void setXX_NatTreasuryEstAmount(java.math.BigDecimal XX_NatTreasuryEstAmount)
	{
		set_Value ("XX_NatTreasuryEstAmount", XX_NatTreasuryEstAmount);
	}

	/**
	 * XX_SENIATESTEEMEDAMUNT
	 * */
	public java.math.BigDecimal getXX_SENIATESTEEMEDAMUNT()
	{
		return get_ValueAsBigDecimal("XX_SENIATESTEEMEDAMUNT");
	}

	public void setXX_SENIATESTEEMEDAMUNT(java.math.BigDecimal XX_SENIATESTEEMEDAMUNT)
	{
		set_Value ("XX_SENIATESTEEMEDAMUNT", XX_SENIATESTEEMEDAMUNT);
	}

	public final void setXX_PriceDefinitiveAllowed (boolean active)
	{
		set_Value("XX_PriceDefinitiveAllowed", Boolean.valueOf(active));
	}	//	setActive

	/**
	 * XX_CustomsAgentEsteemedAmount
	 * */
	public java.math.BigDecimal getXX_CustomsAgentEsteemedAmount()
	{
		return get_ValueAsBigDecimal("XX_CustomsAgentEsteemedAmount");
	}

	public void setXX_CustomsAgentEsteemedAmount(java.math.BigDecimal XX_CustomsAgentEsteemedAmount)
	{
		set_Value ("XX_CustomsAgentEsteemedAmount", XX_CustomsAgentEsteemedAmount);
	}

	/**
	 * XX_EsteemedInsuranceAmount
	 * */
	public java.math.BigDecimal getXX_EsteemedInsuranceAmount()
	{
		return get_ValueAsBigDecimal("XX_ESTEEMEDINSURANCEAMOUNT");
	}

	public void setXX_EsteemedInsuranceAmount(java.math.BigDecimal XX_EsteemedInsuranceAmount)
	{
		set_Value ("XX_ESTEEMEDINSURANCEAMOUNT", XX_EsteemedInsuranceAmount);
	}

	/**
	 * XX_EstimatedExchangeRate
	 * */
	public java.math.BigDecimal getXX_EstimatedExchangeRate()
	{
		return get_ValueAsBigDecimal("XX_EstimatedExchangeRate");
	}

	public void setXX_EstimatedExchangeRate(java.math.BigDecimal XX_EstimatedExchangeRate)
	{
		set_Value ("XX_EstimatedExchangeRate", XX_EstimatedExchangeRate);
	}

	/**
	 * XX_VENDORPROFAMOUNT
	 * */
	public java.math.BigDecimal getXX_VENDORPROFAMOUNT()  {
		return get_ValueAsBigDecimal("XX_VENDORPROFAMOUNT");
	}

	public void setXX_VENDORPROFAMOUNT(java.math.BigDecimal XX_VENDORPROFAMOUNT)  {
		set_Value ("XX_VENDORPROFAMOUNT", XX_VENDORPROFAMOUNT);
	}

	/**
	 * XX_CADIVIAMUUNT
	 * */
	public java.math.BigDecimal getXX_CADIVIAMUUNT()  {
		return get_ValueAsBigDecimal("XX_CADIVIAMUUNT");
	}

	public void setXX_CADIVIAMUUNT(java.math.BigDecimal XX_CADIVIAMUUNT)  {
		set_Value ("XX_CADIVIAMUUNT", XX_CADIVIAMUUNT);
	}

	/**
	 * XX_ADDAMOUNT
	 * */
	public java.math.BigDecimal getXX_ADDAMOUNT()   {
		return get_ValueAsBigDecimal("XX_ADDAMOUNT");
	}

	public void setXX_ADDAMOUNT(java.math.BigDecimal XX_ADDAMOUNT)  {
		set_Value ("XX_ADDAMOUNT", XX_ADDAMOUNT);
	}

	/**
	 * XX_VendorInvoiceAmount
	 * */
	public java.math.BigDecimal getXX_VendorInvoiceAmount()   {
		return get_ValueAsBigDecimal("XX_VendorInvoiceAmount");
	}

	public void setXX_VendorInvoiceAmount (java.math.BigDecimal XX_VendorInvoiceAmount)  {
		set_Value ("XX_VendorInvoiceAmount", XX_VendorInvoiceAmount);
	}

	/**
	 * XX_INVOCECANCEAMOUNT
	 * */
	public java.math.BigDecimal getXX_INVOCECANCEAMOUNT()   {
		return get_ValueAsBigDecimal("XX_INVOCECANCEAMOUNT");
	}

	public void setXX_INVOCECANCEAMOUNT (java.math.BigDecimal XX_INVOCECANCEAMOUNT)   {
		set_Value ("XX_INVOCECANCEAMOUNT", XX_INVOCECANCEAMOUNT);
	}

	/**
	 * XX_RealExchangeRate
	 * */
	public java.math.BigDecimal getXX_RealExchangeRate()  {
		return get_ValueAsBigDecimal("XX_RealExchangeRate");
	}

	public void setXX_RealExchangeRate(java.math.BigDecimal XX_RealExchangeRate)   {
		set_Value ("XX_RealExchangeRate", XX_RealExchangeRate);
	}

	/**
	 * XX_FreightAgentInvoiceAmount
	 * */
	public java.math.BigDecimal getXX_FreightAgentInvoiceAmount()   {
		return get_ValueAsBigDecimal("XX_FreightAgentInvoiceAmount");
	}

	public void setXX_FreightAgentInvoiceAmount(java.math.BigDecimal XX_FreightAgentInvoiceAmount)   {
		set_Value ("XX_FreightAgentInvoiceAmount", XX_FreightAgentInvoiceAmount);
	}

	/**
	 * XX_AntiAmAgentPro
	 * */
	public java.math.BigDecimal getXX_AntiAmAgentPro()   {
		return get_ValueAsBigDecimal("XX_AntiAmAgentPro");
	}

	public void setXX_AntiAmAgentPro (java.math.BigDecimal XX_AntiAmAgentPro)   {
		set_Value ("XX_AntiAmAgentPro", XX_AntiAmAgentPro);
	}

	/**
	 * XX_NatTreasRealAmPro
	 * */
	public java.math.BigDecimal getXX_NatTreasRealAmPro()   {
		return get_ValueAsBigDecimal("XX_NatTreasRealAmPro");
	}

	public void setXX_NatTreasRealAmPro (java.math.BigDecimal XX_NatTreasRealAmPro)   {
		set_Value ("XX_NatTreasRealAmPro", XX_NatTreasRealAmPro);
	}

	/**
	 * XX_SenRealEstAmPro
	 * */
	public java.math.BigDecimal getXX_SenRealEstAmPro()   {
		return get_ValueAsBigDecimal("XX_SenRealEstAmPro");
	}

	public void setXX_SenRealEstAmPro (java.math.BigDecimal XX_SenRealEstAmPro)  {
		set_Value ("XX_SenRealEstAmPro", XX_SenRealEstAmPro);
	}

	/**
	 * XX_CustomAgentAmountPro
	 * */
	public java.math.BigDecimal getXX_CustomAgentAmountPro()   {
		return get_ValueAsBigDecimal("XX_CustomAgentAmountPro");
	}

	public void setXX_CustomAgentAmountPro (java.math.BigDecimal XX_CustomAgentAmountPro)  {
		set_Value ("XX_CustomAgentAmountPro", XX_CustomAgentAmountPro);
	}


	/**
	 * XX_NacInvoiceAmountPro
	 * */
	public java.math.BigDecimal getXX_NacInvoiceAmountPro()  {
		return get_ValueAsBigDecimal("XX_NacInvoiceAmountPro");
	}

	public void setXX_NacInvoiceAmountPro (java.math.BigDecimal XX_NacInvoiceAmountPro)  {
		set_Value ("XX_NacInvoiceAmountPro", XX_NacInvoiceAmountPro);
	}

	/**
	 * XX_InterFretRealAmountPro
	 * */
	public java.math.BigDecimal getXX_InterFretRealAmountPro()  {
		return get_ValueAsBigDecimal("XX_InterFretRealAmountPro");
	}

	public void setXX_InterFretRealAmountPro (java.math.BigDecimal XX_InterFretRealAmountPro)  {
		set_Value ("XX_InterFretRealAmountPro", XX_InterFretRealAmountPro);
	}

	/**
	 * XX_RealInsuranceAmPro
	 * */
	public java.math.BigDecimal getXX_RealInsuranceAmPro()   {
		return get_ValueAsBigDecimal("XX_RealInsuranceAmPro");
	}

	public void setXX_RealInsuranceAmPro (java.math.BigDecimal XX_RealInsuranceAmPro)  {
		set_Value ("XX_RealInsuranceAmPro", XX_RealInsuranceAmPro);
	}

	/**
	 * XX_CARGOAGENTNAME
	 *  */
	public void setXX_CARGOAGENTNAME(String nombre)  {
		set_Value ("XX_CARGOAGENTNAME", nombre);  
	}

	public String getXX_CARGOAGENTNAME()   {
		return get_ValueAsString("XX_CARGOAGENTNAME");
	}

	/**
	 * XX_VENDORINVOICE
	 *  */
	public void setXX_VENDORINVOICE(String nombre)  {
		set_Value ("XX_VENDORINVOICE", nombre);  
	}

	public String getXX_VENDORINVOICE()   {
		return get_ValueAsString("XX_VENDORINVOICE");
	}

	/**
	 * XX_GuideNumber
	 *  */
	public void setXX_GuideNumber(int id)  {
		if (id <= 0) set_Value ("XX_GuideNumber", null);
		else
			set_Value ("XX_GuideNumber", Integer.valueOf(id));   	
	}

	public int getXX_GuideNumber()  {
		return get_ValueAsInt("XX_GuideNumber");      
	} 

	/**
	 * XX_RUSAD
	 *  */
	public void setXX_RUSAD(int id)  {
		if (id <= 0) set_Value ("XX_RUSAD", null);
		else
			set_Value ("XX_RUSAD", Integer.valueOf(id));   	
	}

	public int getXX_RUSAD()  {
		return get_ValueAsInt("XX_RUSAD");      
	} 

	/**
	 * XX_AAD
	 *  */
	public void setXX_AAD(int id) {
		if (id <= 0) set_Value ("XX_AAD", null);
		else
			set_Value ("XX_AAD", Integer.valueOf(id));   	
	}

	public int getXX_AAD()  {
		return get_ValueAsInt("XX_AAD");      
	} 

	/**
	 * XX_BlockAgeDelEstDate
	 *  */
	public char getXX_BlockAgeDelEstDate()  {
		return get_ValueAsString("XX_BlockAgeDelEstDate").charAt(0);      
	} 

	public void setXX_BlockAgeDelEstDate(boolean block) {
		set_ValueNoCheck ("XX_BlockAgeDelEstDate", Boolean.valueOf(block));

	}

	/**
	 * XX_DefinitiveCargoAg
	 */
	public void setXX_DefinitiveCargoAg (boolean valor)  {
		set_Value ("XX_DefinitiveCargoAg", Boolean.valueOf(valor));

	}

	/** Get Processed.
    @return Processed */
	public boolean isXX_DefinitiveCargoAg()  {
		return get_ValueAsBoolean("XX_DefinitiveCargoAg");   
	}

	/**
	 * XX_Authorized Set
	 */
	public void setXX_Authorized(boolean valor)  {
		set_Value ("XX_Authorized", Boolean.valueOf(valor));

	}

	/** 
	 * XX_Authorized Get.
	 */
	public boolean isXX_Authorized()  {
		return get_ValueAsBoolean("XX_Authorized");   
	}

	/**
	 * XX_NotAuthorized Set
	 */
	public void setXX_NotAuthorized(boolean valor) {
		set_Value ("XX_NotAuthorized", Boolean.valueOf(valor));

	}

	/** 
	 * XX_NotAuthorized Get.
	 */
	public boolean isXX_NotAuthorized()  {
		return get_ValueAsBoolean("XX_NotAuthorized");   
	}

	/**
	 * XX_EntranceDate
	 * */
	public void setXX_EntranceDate(Timestamp fecha)  {
		set_Value ("XX_EntranceDate", fecha);	
	}

	public Timestamp getXX_EntranceDate()   {
		return (Timestamp)get_Value("XX_EntranceDate");      
	}

	/**
	 * XX_ReceptionDate
	 * */
	public void setXX_ReceptionDate(Timestamp fecha)  {
		set_Value ("XX_ReceptionDate", fecha);	
	}

	public Timestamp getXX_ReceptionDate()   {
		return (Timestamp)get_Value("XX_ReceptionDate");      
	}

	/**
	 * XX_NotReadyCM_ID
	 * */
	public void setXX_NotReadyCM_ID(int user)  {
		if (user <= 0) set_Value ("XX_NotReadyCM_ID", null);
		else
			set_Value ("XX_NotReadyCM_ID", Integer.valueOf(user));   	
	}

	public int getXX_NotReadyCM_ID()  {
		return get_ValueAsInt("XX_NotReadyCM_ID");      
	}

	/**
	 * XX_EntranceUser_ID
	 * */
	public void setXX_EntranceUser_ID(int user)   {
		if (user <= 0) set_Value ("XX_EntranceUser_ID", null);
		else
			set_Value ("XX_EntranceUser_ID", Integer.valueOf(user));   	
	}

	public int getXX_EntranceUser_ID()   {
		return get_ValueAsInt("XX_EntranceUser_ID");      
	} 

	/**
	 * XX_ReceptionUser_ID
	 * */
	public void setXX_ReceptionUser_ID(int user)  {
		if (user <= 0) set_Value ("XX_ReceptionUser_ID", null);
		else
			set_Value ("XX_ReceptionUser_ID", Integer.valueOf(user));   	
	}

	public int getXX_ReceptionUser_ID()   {
		return get_ValueAsInt("XX_ReceptionUser_ID");      
	} 

	/**
	 * XX_FirstAppManager_ID
	 * */
	public void setXX_FirstAppManager_ID(int user)
	{
		if (user <= 0) set_Value ("XX_FirstAppManager_ID", null);
		else
			set_Value ("XX_FirstAppManager_ID", Integer.valueOf(user));   	
	}

	public int getXX_FirstAppManager_ID() 
	{
		return get_ValueAsInt("XX_FirstAppManager_ID");      
	} 

	/**
	 * XX_FiscalDataError Set
	 */
	public void setXX_FiscalDataError(boolean valor)  {
		set_Value ("XX_FiscalDataError", Boolean.valueOf(valor));

	}

	/** 
	 * XX_FiscalDataError Get.
	 */
	public boolean isXX_FiscalDataError()  {
		return get_ValueAsBoolean("XX_FiscalDataError");   
	}

	/**
	 * XX_EstimatedDiffersFromReal Set
	 */
	public void setXX_EstimatedDiffersFromReal(boolean valor)  {
		set_Value ("XX_EstimatedDiffersFromReal", Boolean.valueOf(valor));

	}

	/** 
	 * XX_EstimatedDiffersFromReal Get.
	 */
	public boolean isXX_EstimatedDiffersFromReal()  {
		return get_ValueAsBoolean("XX_EstimatedDiffersFromReal");   
	}
	/**
	 * Relación de costos de importaciones
	 * @author aavila
	 * @author ccapote
	 * */
	public BigDecimal getXX_CustomAgentAmountProEst()  {
		return get_ValueAsBigDecimal("XX_CustomAgentAmountProEst");

	}

	public void setXX_CustomAgentAmountProEst (BigDecimal XX_CustomAgentAmountProEst)  {
		set_Value ("XX_CustomAgentAmountProEst", XX_CustomAgentAmountProEst);

	}
	public BigDecimal getXX_InsuranceAmProEst()  {
		return get_ValueAsBigDecimal("XX_InsuranceAmProEst");

	}

	public void setXX_InsuranceAmProEst (BigDecimal XX_InsuranceAmProEst)  {
		set_Value ("XX_InsuranceAmProEst", XX_InsuranceAmProEst);

	}
	public BigDecimal getXX_InterFretAmountProEst()  {
		return get_ValueAsBigDecimal("XX_InterFretAmountProEst");

	}

	public void setXX_InterFretAmountProEst (BigDecimal XX_InterFretAmountProEst) {
		set_Value ("XX_InterFretAmountProEst", XX_InterFretAmountProEst);

	}
	public BigDecimal getXX_NacInvoiceAmountProEst()   {
		return get_ValueAsBigDecimal("XX_NacInvoiceAmountProEst");

	}

	public void setXX_NacInvoiceAmountProEst (BigDecimal XX_NacInvoiceAmountProEst)   {
		set_Value ("XX_NacInvoiceAmountProEst", XX_NacInvoiceAmountProEst);

	}
	public BigDecimal getXX_NatTreasAmProEst()   {
		return get_ValueAsBigDecimal("XX_NatTreasAmProEst");

	}

	public void setXX_NatTreasAmProEst (BigDecimal XX_NatTreasAmProEst)  {
		set_Value ("XX_NatTreasAmProEst", XX_NatTreasAmProEst);

	}
	public BigDecimal getXX_SenEstAmProEst()  {
		return get_ValueAsBigDecimal("XX_SenEstAmProEst");

	}

	public void setXX_SenEstAmProEst(BigDecimal XX_SenEstAmProEst)   {
		set_Value ("XX_SenEstAmProEst", XX_SenEstAmProEst);

	}

	public void setXX_Alert3(boolean b) {
		// TODO Auto-generated method stub
		set_Value ("XX_Alert3", b); 

	}

	public boolean getXX_Alert3()  {
		return get_ValueAsBoolean("XX_Alert3");
	} 


	/*
	 * Status Sincronizacion al AS de Purchase Order
	 */
	public void setXX_Status_Sinc (boolean XX_Status_Sinc)
	{
		set_Value ("XX_Status_Sinc", XX_Status_Sinc);

	} 

	public boolean isXX_Status_Sinc() 
	{
		return get_ValueAsBoolean("XX_Status_Sinc");    
	}
	/***/
	public void setXX_Alert4(boolean b) {
		// TODO Auto-generated method stub
		set_Value ("XX_Alert4", b); 

	}

	public boolean getXX_Alert4() {
		return get_ValueAsBoolean("XX_Alert4");
	} 

	public void setXX_Alert5(boolean b) {
		// TODO Auto-generated method stub
		set_Value ("XX_Alert5", b); 

	}
	public boolean getXX_Alert5() {
		return get_ValueAsBoolean("XX_Alert5");
	} 
	public void setXX_Alert6(boolean b) {
		// TODO Auto-generated method stub
		set_Value ("XX_Alert6", b); 

	}

	public boolean getXX_Alert6() {
		return get_ValueAsBoolean("XX_Alert6");
	}

	public void setXX_Alert7(boolean b) {
		set_Value ("XX_Alert7", b); 

	}

	public boolean isXX_Alert7() {
		return get_ValueAsBoolean("XX_Alert7");
	} 

	public void setXX_Alert8(boolean b) {
		set_Value ("XX_Alert8", b); 

	}

	public boolean isXX_Alert8() {
		return get_ValueAsBoolean("XX_Alert8");
	} 

	public void setXX_Alert9(boolean b) 
	{
		set_Value ("XX_Alert9", b); 

	}

	public boolean isXX_Alert9() 
	{
		return get_ValueAsBoolean("XX_Alert9");
	}

	public boolean getXX_ComesFromCopy() 
	{
		return get_ValueAsBoolean("XX_ComesFromCopy");   
	}

	/**
	 * XX_Authorized Set
	 */
	public void setXX_ComesFromCopy(boolean valor)
	{
		set_Value ("XX_ComesFromCopy", Boolean.valueOf(valor));

	}

	public Timestamp getXX_InvoiceDate() 
	{
		return (Timestamp)get_Value("XX_InvoiceDate");

	}

	/** Set InvoiceDate.
    @param InvoiceDate of the Purchase Order */
	public void setXX_InvoiceDate (Timestamp InvoiceDate)
	{
		set_Value ("XX_InvoiceDate", InvoiceDate);

	}

	public boolean getXX_IsPrintedNote() 
	{
		return get_ValueAsBoolean("XX_IsPrintedNote");   	
	}

	/**
	 * XX_Authorized Set     
	 */
	public void setXX_IsPrintedNote(boolean valor)
	{
		set_Value ("XX_IsPrintedNote", Boolean.valueOf(valor));        
	}

	/*
	 * set de Alert10 para SITME
	 */
	public void setXX_Alert10(String text)
	{
		set_Value ("XX_Alert10", text);   	
	}

	public String getXX_Alert10() 
	{
		return get_ValueAsString("XX_Alert10");      
	} 

	/**
	 * XX_ComesFromSITME get 
	 */
	public boolean getXX_ComesFromSITME() 
	{
		return get_ValueAsBoolean("XX_ComesFromSITME");   	
	}

	/**
	 * XX_ComesFromSITME Set     
	 */
	public void setXX_ComesFromSITME(boolean valor)
	{
		set_Value ("XX_ComesFromSITME", Boolean.valueOf(valor));        
	}

	/** Set Order Ready Date.
    @param orderReadyDate of the Purchase Order */
	public void setXX_OrderReadyDate(Timestamp orderReadyDate)
	{
		set_Value ("XX_OrderReadyDate", orderReadyDate);

	}
	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 006**/

	/** Set XX_IVA.
	@param XX_IVA I.V.A. on MOrder */
	public void setXX_IVA(BigDecimal iva){
		set_Value ("XX_IVA", iva);   	
	}

	/** Get XX_IVA.
	@return XX_IVA I.V.A. on MOrder */
	public BigDecimal getXX_IVA(){
		return get_ValueAsBigDecimal("XX_IVA");      
	}

	/** Set PO Type.
    @param XX_POType PO Type on C_Order*/
	public void setXX_POType (String XX_POType){
		set_Value ("XX_POType", XX_POType);  
	}

	/** Get PO Type.
    @param XX_POType PO Type on C_Order*/
	public String getXX_POType(){
		return (String)get_Value("XX_POType");
	}
	
	 /** Set XX_DistribAllLines.
    @param XX_DistribAllLines XX_DistribAllLines */
    public void setXX_DistribAllLines (boolean XX_DistribAllLines)  {
        set_Value ("XX_DistribAllLines", Boolean.valueOf(XX_DistribAllLines));
        
    }
    
    /** Get XX_DistribAllLines.
    @return XX_DistribAllLines */
    public boolean isXX_DistribAllLines(){
        return get_ValueAsBoolean("XX_DistribAllLines");
        
    }
    
    /** Is test a valid value.
    @param test testvalue
    @return true if valid **/
    public static boolean isXX_DistributionTypeValid(String test) {
         return X_Ref_XX_DistributionType_LV.isValid(test);
         
    }
    
    /** Set Distribution Type.
    @param XX_DistributionType Distribution Type */
    public void setXX_DistributionType (String XX_DistributionType) {
        if (!isXX_DistributionTypeValid(XX_DistributionType))
        throw new IllegalArgumentException ("XX_DistributionType Invalid value - " + XX_DistributionType + " - Reference_ID=1000299 - ME - PI - PO - SA");
        set_Value ("XX_DistributionType", XX_DistributionType);
        
    }
    
    /** Get Distribution Type.
    @return Distribution Type */
    public String getXX_DistributionType()  {
        return (String)get_Value("XX_DistributionType");
        
    }
    
    /** Set XX_NumLinesOrder.
    @param XX_NumLinesOrder XX_NumLinesOrder */
    public void setXX_NumLinesOrder (int XX_NumLinesOrder)
    {
        if (XX_NumLinesOrder <= 0) set_Value ("XX_NumLinesOrder", null);
        else
        set_Value ("XX_NumLinesOrder", Integer.valueOf(XX_NumLinesOrder));
        
    }
    
    /** Get XX_NumLinesOrder.
    @return XX_NumLinesOrder */
    public int getXX_NumLinesOrder() 
    {
        return get_ValueAsInt("XX_NumLinesOrder");
        
    }
    
    /** Set XX_SyncAccounting.
    @param XX_DistribAllLines XX_DistribAllLines */
    public void setXX_SyncAccounting(boolean value)  {
        set_Value ("XX_SyncAccounting", Boolean.valueOf(value));
        
    }
    
    /** Get XX_SyncAccounting.
    @return XX_SyncAccounting */
    public boolean isXX_SyncAccounting(){
        return get_ValueAsBoolean("XX_SyncAccounting");
        
    }
    
    /** Get XX_DefinitiveTradeFactor.
    @return XX_DefinitiveTradeFactor */
	public BigDecimal getXX_DefinitiveTradeFactor()  {
		return get_ValueAsBigDecimal("XX_DefinitiveTradeFactor");

	}

    /** Set XX_DefinitiveTradeFactor.
    @param XX_DefinitiveTradeFactor XX_DefinitiveTradeFactor  */
	public void setXX_DefinitiveTradeFactor(BigDecimal XX_DefinitiveTradeFactor)   {
		set_Value ("XX_DefinitiveTradeFactor", XX_DefinitiveTradeFactor);

	}
	
	/** Set XX_WithholdingNo.
	@param XX_WithholdingNo on MOrder */
	public void setXX_WithholdingNo(int XX_WithholdingNo){
		set_Value ("XX_WithholdingNo", XX_WithholdingNo);   	
	}

	/** Get XX_WithholdingNo.
	@return XX_WithholdingNo on MOrder */
	public int getXX_WithholdingNo(){
		return get_ValueAsInt("XX_WithholdingNo");      
	}

    /** Get XX_WithholdingAmount.
    @return XX_WithholdingAmount */
	public BigDecimal getXX_WithholdingAmount(){
		return get_ValueAsBigDecimal("XX_WithholdingAmount");

	}

    /** Set XX_WithholdingAmount.
    @param XX_WithholdingAmount XX_WithholdingAmount  */
	public void setXX_WithholdingAmount(BigDecimal XX_WithholdingAmount){
		set_Value ("XX_WithholdingAmount", XX_WithholdingAmount);

	}
    
	/*
     * XX_WithholdingDate 
     */
	public Timestamp getXX_WithholdingDate() 
	{
		return (Timestamp)get_Value("XX_WithholdingDate");

	}

	/** Set XX_WithholdingDate.
    @param WithholdingDate of the Sales Order */
	public void setXX_WithholdingDate(Timestamp XX_WithholdingDate)
	{
		set_Value ("XX_WithholdingDate", XX_WithholdingDate);

	}
	
	/*
     * XX_WithholdingCreated 
     */
	public Timestamp getXX_WithholdingCreated() 
	{
		return (Timestamp)get_Value("XX_WithholdingCreated");

	}

	/** Set XX_WithholdingDate.
    @param WithholdingDate of the Sales Order */
	public void setXX_WithholdingCreated(Timestamp XX_WithholdingCreated)
	{
		set_Value ("XX_WithholdingCreated", XX_WithholdingCreated);

	}
	
}
