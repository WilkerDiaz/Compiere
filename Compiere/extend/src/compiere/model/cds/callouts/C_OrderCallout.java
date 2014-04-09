package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.CalloutOrder;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MPriceList;
import org.compiere.model.MProductPricing;
import org.compiere.model.MRole;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.Utilities;
import compiere.model.suppliesservices.X_XX_Contract;

/**
 * 
 * @author José G. Trías, Patricia Ayuso M., Jose Luis Castillo
 */
public class C_OrderCallout extends CalloutEngine {

	int id_orden;
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());
	
	/**
	 * Set Country and Currency if Order Type is changed.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String orderType(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {

		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			String orderType = (String) mField.getValue();
	
			if(orderType!=null){
				if (orderType.equals("Nacional")) { // Tipo de orden 10000003 (Nacional)
		
					// Cambiar el pais
					Integer ii = (Integer) 339; // Pais Venezuela (339)
					mTab.setValue("C_Country_ID", ii);
		
					// Cambiar la moneda
					ii = (Integer) 205; // Moneda Bolivares (205)
					mTab.setValue("C_Currency_ID", ii);
				} else // si es Internacional
				{
					// Cambiar la moneda
					Integer ii = (Integer) 100; // Moneda Dollar (100)
					mTab.setValue("C_Currency_ID", ii);
				}
			}
	
			return "";
		}
	}
	
	/**
	 * Dependiendo del tipo de moneda que tenga el contrato, 
	 * actualiza su tipo de contrato, bien sea nacional o importada.
	 * Dicho callout está ubicado en el atributo C_Currency_ID 
	 * de la tabla XX_Contract
	 * @author Jessica Mendoza
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value  compiere.model.cds.callouts.C_OrderCallout.contractType
	 */
	public String contractType(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		Integer moneda = (Integer) mTab.getValue("C_Currency_ID");
		Integer idContrato = (Integer) mTab.getValue("XX_Contract_ID");
		if (idContrato != null){
			X_XX_Contract contrato = new X_XX_Contract(Env.getCtx(), idContrato, null);
			if (moneda > 0){
				if (moneda == 205)
					contrato.setXX_ContractType("Nacional");
				else
					contrato.setXX_ContractType("Importada");
				contrato.save();
			}
		}
		return "";
	}
	
	/**
	 * Busca la información del: socio de negocio, pais, tienda,
	 * departamento y condición de pago, a través de la O/C seleccionada
	 * @author Jessica Mendoza
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value compiere.model.cds.callouts.C_OrderCallout.summaryOC
	 * @return
	 * @throws SQLException 
	 */
	public String summaryOC(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		Integer idOrder = (Integer) mTab.getValue("C_Order_ID");
		String sql = "select C_BPartner_ID, C_Country_ID, M_Warehouse_ID, " +
					 "XX_VMR_Department_ID, C_PaymentTerm_ID " +
					 "from C_Order " +
					 "where C_Order_ID = " + idOrder;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if (rs.next()){
				mTab.setValue("C_BPartner_ID", rs.getInt("C_BPartner_ID"));
				mTab.setValue("C_Country_ID", rs.getInt("C_Country_ID"));
				mTab.setValue("M_Warehouse_ID", rs.getInt("M_Warehouse_ID"));
				mTab.setValue("XX_VMR_Department_ID", rs.getInt("XX_VMR_Department_ID"));
				mTab.setValue("C_PaymentTerm_ID", rs.getInt("C_PaymentTerm_ID"));
				if (rs.getInt("M_Warehouse_ID") == 1000053){
					mTab.setValue("XX_DistributionByStore", true);
				}
			}			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return "";
	}
	
	public String onSelectTypeDelivery(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		
		//Warehouse
		if(value!=null){
			if(value.equals("PD") || value.equals("CD") ){
				
				String SQL = "Select M_Warehouse_ID " +
							 "FROM M_Warehouse " +
							 "WHERE VALUE = '001' AND ISACTIVE = 'Y' AND AD_CLIENT_ID IN (0,"+ctx.getAD_Client_ID()+")";
		
				int warehouse=0;
				try{
				
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				ResultSet rs = pstmt.executeQuery();
					 
				if(rs.next())
				{
					warehouse = rs.getInt("M_Warehouse_ID");
				}
				
				rs.close();
				pstmt.close();
				   
				}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
				}
				
				mTab.setValue("M_Warehouse_ID", warehouse);
			}
		}
		
		return "";
	}
	
	/*public String onSelectCollection(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		

		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer collection = (Integer)mTab.getValue("C_Campaign_ID");
		Integer season = 0;

		String SQL = "SELECT XX_VMA_Season_ID " +
					 "FROM C_Campaign " +
					 "WHERE C_Campaign_ID=" + collection+" "+
					 "AND AD_CLIENT_ID IN (0,"+ctx.getAD_Client_ID()+")";

		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				season = rs.getInt("XX_VMA_Season_ID");
			} 
			rs.close();
			pstmt.close();
		}catch (Exception e) {
				log.log(Level.SEVERE, SQL, e);
		}

		mTab.setValue("XX_VMA_Season_ID", season);
				
		setCalloutActive(false);
		
		return "";

	}*/
	
	public String dispatchDate(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		if(mTab.getValue("C_Order_ID")!=null){
			mTab.setValue("XX_ShowMotiveChange","Y");
		}
		
		if (mTab.getValue("XX_OrderStatus").equals("PRO"))
			mTab.setValue("XX_EstimatedDispatchDate", mTab.getValue("XX_DispatchDate"));
		
		BigDecimal dias = new BigDecimal(0);
		
		String SQL = "Select XX_IMPORTSLOGISTICSTIMETLI" +
		" FROM  XX_VLO_LeadTimes" +
		" WHERE C_BPartner_ID=" + mTab.getValue("C_BPartner_ID") +
			" AND C_Country_ID=" + mTab.getValue("C_Country_ID") +
			" AND XX_VLO_ArrivalPort_ID=" + mTab.getValue("XX_VLO_ArrivalPort_ID") + 
			" AND isactive='Y'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dias = rs.getBigDecimal("XX_IMPORTSLOGISTICSTIMETLI");
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
		} finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//System.out.println("dias " + dias);
		
		Calendar dateFrom = Calendar.getInstance();			
		dateFrom.setTimeInMillis(((Timestamp)mField.getValue()).getTime());
		dateFrom.add(Calendar.DATE, dias.intValue());
		
		Timestamp withLeapTime = new Timestamp(dateFrom.getTimeInMillis());
//		System.out.println("mField.getValue()).getTime() " + ((Timestamp)mField.getValue()).getTime());
//		withLeapTime.setTime(((Timestamp)mField.getValue()).getTime() + (86400000*(dias.intValue()+7)));
		
		//Verifico que la fecha de llegada no sea un dia no laborable, y si es le sumo 1 dia mas
		Utilities check = new Utilities();
		while(check.isNonBusinessDay(withLeapTime)){
			withLeapTime.setTime(withLeapTime.getTime() + 86400000*(1));
		}
		
		if(mField.getValue()!=null){
			if (mTab.getValue("XX_ArrivalDate")==null | mTab.getValue("C_Order_ID")==null | "PRO".equals((String)mTab.getValue("XX_OrderStatus")))
				mTab.setValue("XX_ArrivalDate", withLeapTime);
			mTab.setValue("XX_EstimatedDate", withLeapTime);
		}
		
		BigDecimal repFactor;

		Object objetoFactor = mTab.getValue("XX_ReplacementFactor");
		
		if (objetoFactor.getClass() == new Integer(0).getClass()){
			repFactor = new BigDecimal((Integer)objetoFactor);
		}else{
			repFactor = (BigDecimal)objetoFactor;
		}
		
		mTab.setValue("XX_ReplacementFactor",new BigDecimal(0));
		
		//Calcular el Replacement Factor
		//*****************************
		GridField department = mTab.getField("XX_VMR_Department_ID");
		GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
		GridField country = mTab.getField("C_Country_ID");
		GridField dispatchDate = mTab.getField("XX_DispatchDate");
		GridField currency = mTab.getField("C_Currency_ID");
		boolean  tipoOrden = mTab.getField("XX_POType").getValue().equals("POA");
		GridField estimatedDate = mTab.getField("XX_EstimatedDate");
		
		if(currency.getValue()!= null && dispatchDate.getValue()!=null && dispatchRoute.getValue()!=null && 
				(department.getValue()!=null ||  tipoOrden) && country.getValue()!=null)
		{

			String fecha =  estimatedDate.getValue().toString();

			//se trunca la fecha a 10 caracteres
			fecha=fecha.substring(0, 10);
			    
		    try
		    {    
	          	Date date ; 
	          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	          	date = (Date)formatter.parse(fecha);
	          	Calendar cal=Calendar.getInstance();
	          	cal.setTime(date);
	          	cal.add(Calendar.MONTH,3);
	          	formatter = new SimpleDateFormat("yyyy-MM-dd");
	          	// Calendar to Date Conversion
	          	int year = cal.get(Calendar.YEAR);
	          	int month = cal.get(Calendar.MONTH);
	          	int day = cal.get(Calendar.DATE);
	          	Date auxDate = new Date((year-1900), month, day);
	          	fecha=formatter.format(auxDate);
		    }
		    catch (ParseException e)
		    {
		    	log.log(Level.SEVERE, "Fecha", e);   
		    }    
	     
			BigDecimal increaseFactor = null;
			SQL = "Select XX_INCREASEFACTOR "
				+ "FROM XX_VMR_INCREASEFACTOR "
				+ "WHERE XX_VMR_DEPARTMENT_ID="
				+ (Integer) department.getValue()
				+ " AND ISACTIVE='Y'" 
				+ " AND XX_VLO_DispatchRoute_ID="
				+ (Integer) dispatchRoute.getValue()
				+ " AND C_Country_ID=" + (Integer) country.getValue();

			try {
				// Obtengo el valor de increaseFactor
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
				}

				// Si el factor de incremento es nulo lo igualo a 0
				if (increaseFactor == null) {
					increaseFactor = new BigDecimal(0);
					log.info("Increase Factor es Nulo");
				}

			} catch (Exception e) {
				log.log(Level.SEVERE, SQL, e);
			} finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			//obtengo el valor de Replacement Factor
			BigDecimal replacementFactor_aux= new BigDecimal(0);
			BigDecimal multiplyRate = new BigDecimal(0); //AGREGADO GHUCHET - Cambio en fórmula de Factor de Reposición
			SQL = "Select XX_ReplacementFactor, MultiplyRate " +
			"FROM C_CONVERSION_RATE " +
			"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND C_CURRENCY_ID="+(Integer)currency.getValue()+" AND STARTDATE<= TO_DATE('"+fecha+"','YYYY-MM-DD') AND ENDDATE >= TO_DATE('"+fecha+"','YYYY-MM-DD'))";
			System.out.println(SQL);
			try 
			{	
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();

				int i = 0;
				while (rs.next()) {
					replacementFactor_aux = rs.getBigDecimal("XX_ReplacementFactor");
					multiplyRate =  rs.getBigDecimal("MultiplyRate");
					i++;
				}

				if (i == 0) {
					//myreturn= "No existe factor de reposición para la fecha Y monedas seleccionadas.";
				}

			} catch (Exception e) {
				log.log(Level.SEVERE, SQL, e);
			} finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			// Set Replacement Factor
			BigDecimal ReplacementFactor = new BigDecimal(0);
			if(increaseFactor!=null){
				ReplacementFactor = replacementFactor_aux.add(increaseFactor.multiply(multiplyRate)); //MODIFICADO GHUCHET - Cambio en fórmula de Factor de Reposición
			}else{
				ReplacementFactor = replacementFactor_aux;
			}
			mTab.setValue("XX_ReplacementFactor", ReplacementFactor);
			
			if(!repFactor.equals(ReplacementFactor)){
				// Modifying Costs
				//modifyNacCost(ctx, WindowNo, mTab, mField, value);
			}
		}
		//***************************************
		// fin del calculo del replacement factor
	    
		
		
		//Fechas de importacion
		//************************
		//GridField country = mTab.getField("C_Country_ID");
		//GridField dispatchDate = mTab.getField("XX_DispatchDate");
		GridField partner = mTab.getField("C_BPartner_ID");
		GridField arrivalPort = mTab.getField("XX_VLO_ArrivalPort_ID");
		
		importDates(mTab, ctx, country, dispatchDate, partner, arrivalPort);	

		setCalloutActive(false);
		return "";
	}
		
	
	/**
	 * Set Estimated Date when Arrival Date is changed.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String arrivalDate(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {

		if (isCalloutActive() || value == null)
			return "";
		
		setCalloutActive(true);
		
		Utilities check = new Utilities();
		if(check.isNonBusinessDay((Timestamp)value)){
			mTab.setValue("XX_EstimatedDate", (Timestamp)mField.getValue());
			setCalloutActive(false);
			return "NonBusinessDay";
		}
		
		mTab.setValue("XX_EstimatedDate", (Timestamp)mField.getValue());
		setCalloutActive(false);
/**
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
		//<----------- PARA ORDEN DE VENTA ----------->
		//**********************************************
		
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			if(mField.getValue()!=null){
				mTab.setValue("XX_EstimatedDate", mField.getValue());
			}
			
			BigDecimal repFactor;
			Object objetoFactor = mTab.getValue("XX_ReplacementFactor");
			
			if (objetoFactor.getClass() == new Integer(0).getClass()){
				repFactor = new BigDecimal((Integer)objetoFactor);
			}else{
				repFactor = (BigDecimal)objetoFactor;
			}
			
			mTab.setValue("XX_ReplacementFactor",new BigDecimal(0));
			
			//Calcular el Replacement Factor
			//*****************************
			GridField department = mTab.getField("XX_VMR_DEPARTMENT_ID");
			GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
			GridField country = mTab.getField("C_Country_ID");
			GridField estimatedDate = mTab.getField("XX_arrivalDate");
			GridField currency = mTab.getField("C_Currency_ID");
	
			if(currency.getValue()!= null && estimatedDate.getValue()!=null && dispatchRoute.getValue()!=null && department.getValue()!=null && country.getValue()!=null){
	
				String fecha = estimatedDate.getValue().toString();
				
				//se trunca la fecha a 10 caracteres
				fecha=fecha.substring(0, 10);
				    
			    try
			    {    
		          	Date date ; 
		          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	date = (Date)formatter.parse(fecha);
		          	Calendar cal=Calendar.getInstance();
		          	cal.setTime(date);
		          	cal.add(Calendar.MONTH,3);
		          	formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	// Calendar to Date Conversion
		          	int year = cal.get(Calendar.YEAR);
		          	int month = cal.get(Calendar.MONTH);
		          	int day = cal.get(Calendar.DATE);
		          	Date auxDate = new Date((year-1900), month, day);
		          	fecha=formatter.format(auxDate);
			    }
			    catch (ParseException e)
			    {
			    	log.log(Level.SEVERE, "Fecha", e);   
			    }    
		     
	
				BigDecimal increaseFactor = null;
				String SQL = "Select XX_INCREASEFACTOR "
					+ "FROM XX_VMR_INCREASEFACTOR "
					+ "WHERE XX_VMR_DEPARTMENT_ID="
					+ (Integer) department.getValue()
					+ " AND XX_VLO_DispatchRoute_ID="
					+ (Integer) dispatchRoute.getValue()
					+ " AND C_Country_ID=" + (Integer) country.getValue();
	
				try {
					// Obtengo el valor de increaseFactor
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
	
					while (rs.next()) {
						increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
					}
	
					rs.close();
					pstmt.close();
	
					// Si el factor de incremento es nulo lo igualo a 0
					if (increaseFactor == null) {
						increaseFactor = new BigDecimal(0);
						log.info("Increase Factor es Nulo");
					}
	
				} catch (Exception e) {
					log.log(Level.SEVERE, SQL, e);
				}
				
				
				//obtengo el valor de Replacement Factor
				BigDecimal replacementFactor_aux= new BigDecimal(0);
				SQL = "Select XX_ReplacementFactor " +
				"FROM C_CONVERSION_RATE " +
				"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND C_CURRENCY_ID="+(Integer)currency.getValue()+" AND STARTDATE<= TO_DATE('"+fecha+"','YYYY-MM-DD') AND ENDDATE >= TO_DATE('"+fecha+"','YYYY-MM-DD'))";
				
				try 
				{	
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
	
					int i = 0;
					while (rs.next()) {
						replacementFactor_aux = rs
								.getBigDecimal("XX_ReplacementFactor");
						i++;
					}
	
					if (i == 0) {
						//myreturn= "No existe factor de reposición para la fecha Y monedas seleccionadas.";
					}
	
					rs.close();
					pstmt.close();
	
				} catch (Exception e) {
					log.log(Level.SEVERE, SQL, e);
				}
	
				// Set Replacement Factor
				BigDecimal ReplacementFactor = new BigDecimal(0);
				if(increaseFactor!=null){
					ReplacementFactor = replacementFactor_aux.add(increaseFactor.multiply(replacementFactor_aux));
				}else{
					ReplacementFactor = replacementFactor_aux;
				}
				mTab.setValue("XX_ReplacementFactor", ReplacementFactor);
				
				if(!repFactor.equals(ReplacementFactor)){
					// Modifying Costs
					modifyNacCost(ctx, WindowNo, mTab, mField, value);
				}
			}
			//***************************************
			// fin del calculo del replacement factor
			
			return "";
		}
		
		*/
		return "";
	}

	/**
	 * Set Country after the original bPartner Callout, and set the Estimated
	 * Margin.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String bPartner_New(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {

		CalloutOrder X = new CalloutOrder();
		String oldCallout = X.bPartner(ctx, WindowNo, mTab, mField, value);

		if (isCalloutActive() || value == null)
			return "";
		
		setCalloutActive(true);
		
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
		
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			GridField orderType = mTab.getField("XX_OrderType");
			String orderType_value = (String) orderType.getValue();
			
			if (orderType_value.equalsIgnoreCase("Nacional")) { // si es nacional
				Integer ii = (Integer) 339;
				mTab.setValue("C_Country_ID", ii);
			}
	
			Integer vendor_ID = (Integer) mField.getValue();
	
			if (vendor_ID != null) { // si el proveedor no es nulo
				
				boolean nacional=true;
				
				try {
					// Tipo de Proveedor
					String SQL = "SELECT (select Name from AD_ref_list where AD_Reference_ID=(select AD_Reference_ID from AD_Reference where UPPER(Name)='XX_REF_VENDORCLASS') AND value=XX_VendorClass) as name"
							+" FROM C_BPartner "
							+ "WHERE C_BPartner_ID="+ vendor_ID;
	
					PreparedStatement pstmt_a = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt_a.executeQuery();
	
					while (rs.next()) {
						
						String aux = rs.getString("name");
						
						if(aux.equalsIgnoreCase("Importado")){
							nacional=false;
						}
						
					}
	
					rs.close();
					pstmt_a.close();
				}
				catch(Exception e)
				{
					return e.getMessage();
				}
				
				if(nacional==true){
					mTab.setValue("XX_OrderType","Nacional");
				}else{
					mTab.setValue("XX_OrderType","Importada");
					mTab.setValue("XX_ImportingCompany_ID", ctx.getContextAsInt("#XX_L_VSI_CLIENTCENTROBECO_ID"));
				}
				
				// Selecciona el contacto de ventas (si lo tiene)
				String SQL = "SELECT AD_USER_ID "+
					         "FROM AD_USER ADU "+
					         "WHERE XX_CONTACTTYPE = "+ ctx.getContext("#XX_L_CONTACTTYPESALES")+" "+
					         "AND C_BPARTNER_ID = "+ vendor_ID +" AND ROWNUM=1";

				try{
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
		
					while (rs.next()) {
						mTab.setValue("AD_User_ID", rs.getInt("AD_USER_ID"));
					}
		
					rs.close();
					pstmt.close();
				}
				catch (Exception e) {
					setCalloutActive(false);
					return "Error al Cargar el contacto del Proveedor";
				}
				
			}
			
			/**AGREGADO GHUCHET  Al estar creando una O/C donde el proveedor posee un representante
			 *  comercial debemos asociar de inmediato el representante comercial a la O/C si este tiene fee obligatorio */
			
			String sql = "\nSELECT NVL(BP.XX_MANDATORYFEE,'N') XX_MANDATORYFEE, T.XX_VMR_TRADEREPRESENTATIVE_ID " +
					"\nFROM  XX_VMR_TRADEREPRESENTATIVE T, C_BPARTNER BP " +
					"\nWHERE T.XX_VMR_TRADEREPRESENTATIVE_ID =  BP.XX_VMR_TRADEREPRESENTATIVE_ID AND BP.C_BPARTNER_ID = "+ vendor_ID+ " AND  T.ISACTIVE = 'Y' ";
			mTab.setValue("XX_VMR_TRADEREPRESENTATIVE_ID", null);
			try{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
	
				while (rs.next()) {
					if(rs.getString("XX_MANDATORYFEE").compareTo("Y")==0){
						mTab.setValue("XX_VMR_TRADEREPRESENTATIVE_ID", rs.getInt("XX_VMR_TRADEREPRESENTATIVE_ID"));
					}
				}
	
				rs.close();
				pstmt.close();
			}
			catch (Exception e) {
				setCalloutActive(false);
				return "Error al Cargar el Representante Comercial";
			}
			
			/**FIN AGREGADO POR GHUCHET */
			setCalloutActive(false);
			
			return (oldCallout + "");
		}

	}
	
	/**
	 * Set Currency after the original priceList Callout.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String priceList_New(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {

		CalloutOrder X = new CalloutOrder();
		String OldCallout = X.priceList(ctx, WindowNo, mTab, mField, value);
		
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
		
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************

			GridField OrderType = mTab.getField("XX_OrderType");
			String OrderType_value = (String) OrderType.getValue();
			
			if (OrderType_value.equalsIgnoreCase("Importada")) { // si es
				// Internacional
				Integer ii = (Integer) 100; // Moneda Dollar (100)
				mTab.setValue("C_Currency_ID", ii);
			}
	
			return (OldCallout + "");
		}

	}

	/**
	 * Set the Estimated Margin.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
/**	
	public String category(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			Integer category_ID = (Integer) mField.getValue();
			GridField department = mTab.getField("XX_VMR_Department_ID");
			GridField vendor_ID = mTab.getField("C_BPartner_ID");
	
			if (vendor_ID != null) { // si el proveedor no es nulo
	
				try {
					// Margen Estimado (O/C mas reciente del proveedor)
					BigDecimal margin_Aux = null;
	
					String SQL = "Select a.margin "
							+ "FROM (Select XX_EstimatedMargin margin from C_Order where C_BPartner_ID="
							+ (Integer) vendor_ID.getValue()
							+ " Order by Created desc) a " + "WHERE rownum<2";
	
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
	
					while (rs.next()) {
						mTab.setValue("XX_EstimatedMargin", rs
								.getBigDecimal("margin"));
						margin_Aux = rs.getBigDecimal("margin");
					}
	
					rs.close();
					pstmt.close();
	
					// Si el proveedor no tiene margen anterior (por departamento y
					// mes/año)
					if (margin_Aux == null) {
	
						SQL = "SELECT XX_MARGACCORDINGBUDPURCH "
								+ "FROM XX_VMR_PRLD01 B, XX_VMR_DEPARTMENT C, XX_VMR_CATEGORY D "
								+ "WHERE TO_CHAR (SYSDATE, 'YYYYMM') = B.XX_BUDGETYEARMONTH "
	
								+ "AND B.XX_CODEDEPARTMENT = C.VALUE "
								+ "AND C.XX_VMR_DEPARTMENT_ID ="
								+ (Integer) department.getValue() + " "
	
								+ "AND B.XX_CATEGORYCODE = D.VALUE "
								+ "AND D.XX_VMR_CATEGORY_ID = " + category_ID + " "
	
								+ "AND B.XX_CODESTORE=999 "
								+ "AND B.XX_LINECODE=99 "
								+ "AND B.XX_VMR_Section_ID=99 ";
	
						pstmt = DB.prepareStatement(SQL, null);
						rs = pstmt.executeQuery();
	
						while (rs.next()) {
							mTab.setValue("XX_EstimatedMargin", rs
									.getBigDecimal("XX_MARGACCORDINGBUDPURCH"));
						}
	
						rs.close();
						pstmt.close();
	
					}
	
				} catch (Exception e) {
					return e.getMessage();
				}
	
			}
	
			return "";
		}
	}
	*/

	/**
	 * Set the Estimated Margin.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String department (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			String myreturn="";
			
			Integer category = 0;

			String SQL = "SELECT XX_VMR_CATEGORY_ID FROM XX_VMR_Department WHERE XX_VMR_Department_ID=" + mField.getValue();

			try {
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					category = rs.getInt("XX_VMR_CATEGORY_ID");
				} 
				rs.close();
				pstmt.close();
			}catch (Exception e) {
					System.out.println(e + " " + SQL);
			}

			mTab.setValue("XX_VMR_Category_ID", category);

			GridField vendor_ID = mTab.getField("C_BPartner_ID");
			
			mTab.setValue("XX_EstimatedFactor",0);
			mTab.setValue("XX_ReplacementFactor",0);
			mTab.setValue("XX_UserBuyer_ID",0);
			
			GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
			GridField country = mTab.getField("C_Country_ID");
			GridField estimatedDate = mTab.getField("XX_EstimatedDate");
			GridField currency = mTab.getField("C_Currency_ID");
			GridField convertionRate = mTab.getField("XX_ConversionRate_ID");
			GridField department = mTab.getField("XX_VMR_Department_ID");
			
			//User Buyer
			//*****************************
			
			/** Purchase of Supplies and Services
			 * Maria Vintimilla Funcion 007**/
			GridField DocTypePurchase= mTab.getField("XX_POType");
			//System.out.println("Tipo de OC: "+DocTypePurchase.getValue());
			
			if(!DocTypePurchase.getValue().equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
				if(department.getValue()!=null){
					
					SQL = "Select XX_UserBuyer_ID "
						+"FROM XX_VMR_DEPARTMENT "
						+"WHERE XX_VMR_DEPARTMENT_ID="+(Integer)department.getValue();
			
					int buyer_id=0;
					
					try 
					{
						//Obtengo el valor de increaseFactor
						PreparedStatement pstmt = DB.prepareStatement(SQL, null);
						ResultSet rs = pstmt.executeQuery();
						
						while(rs.next())
						{	
							buyer_id = rs.getInt("XX_UserBuyer_ID");
						}
						
						mTab.setValue("XX_UserBuyer_ID",buyer_id);
						
						rs.close();
						pstmt.close();
						
					}
					catch(Exception e)
					{	
						log.log(Level.SEVERE,SQL,e);
					}
				
			}
			}//Fin Departamento User
			
			//Calcular el Estimated Factor
			//*****************************
			myreturn+=myreturn+=estimatedFactor(mTab, currency, department, convertionRate, country, dispatchRoute);
			
			
			//Calcular el Replacement Factor
			//*****************************
			if(currency.getValue()!= null && estimatedDate.getValue()!=null && dispatchRoute.getValue()!=null && department.getValue()!=null && country.getValue()!=null)
			{
							
				String fecha = estimatedDate.getValue().toString();
				
				//se trunca la fecha a 10 caracteres
				fecha=fecha.substring(0, 10);
				    
			    try
			    {    
		          	Date date ; 
		          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	date = (Date)formatter.parse(fecha);
		          	Calendar cal=Calendar.getInstance();
		          	cal.setTime(date);
		          	cal.add(Calendar.MONTH,3);
		          	
		          	formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	
		          	// Calendar to Date Conversion
		          	int year = cal.get(Calendar.YEAR);
		          	int month = cal.get(Calendar.MONTH);
		          	int day = cal.get(Calendar.DATE);
		          	Date auxDate = new Date((year-1900), month, day);
		          	fecha=formatter.format(auxDate);
			    }
			    catch (ParseException e)
			    {
			    	log.log(Level.SEVERE,"Fecha", e);   
			    }    

	
				BigDecimal increaseFactor = null;
				SQL = "Select XX_INCREASEFACTOR "
					+"FROM XX_VMR_INCREASEFACTOR "
					+"WHERE XX_VMR_DEPARTMENT_ID="+(Integer)department.getValue()+
					" AND ISACTIVE='Y'" +
					" AND XX_VLO_DispatchRoute_ID="+(Integer)dispatchRoute.getValue()+
					" AND C_Country_ID="+(Integer)country.getValue();
				
				try 
				{
					//Obtengo el valor de increaseFactor
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
					}
					
					rs.close();
					pstmt.close();
					
					//Si el factor de incremento es nulo lo igualo a 0
					if(increaseFactor==null){
						increaseFactor= new BigDecimal(0);
						log.info("Increase Factor es Nulo");
					}
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE,SQL,e);
				}
		
				
				//obtengo el valor de Replacement Factor
				BigDecimal replacementFactor_aux= new BigDecimal(0);
				BigDecimal multiplyRate = new BigDecimal(0);    //AGREGADO GHUCHET - Cambio en fórmula de Factor de Reposición
				SQL = "Select XX_ReplacementFactor, MultiplyRate " +
				"FROM C_CONVERSION_RATE " +
				"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND C_CURRENCY_ID="+(Integer)currency.getValue()+" AND STARTDATE<= TO_DATE('"+fecha+"','YYYY-MM-DD') AND ENDDATE >= TO_DATE('"+fecha+"','YYYY-MM-DD'))";
				System.out.println(SQL);
				try 
				{	
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					int i=0;
					while(rs.next())
					{	
						replacementFactor_aux = rs.getBigDecimal("XX_ReplacementFactor");
						multiplyRate = rs.getBigDecimal("MultiplyRate");
						i++;
					}
					
					if(i==0){
						//myreturn= "No existe factor de reposición para la fecha Y monedas seleccionadas.";
					}
					
					rs.close();
					pstmt.close();
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
		
				//Set Replacement Factor
				BigDecimal ReplacementFactor = new BigDecimal(0);
				
				if(increaseFactor!=null){
					ReplacementFactor = replacementFactor_aux.add(increaseFactor.multiply(multiplyRate)); //MODIFICADO GHUCHET - Cambio en fórmula de Factor de Reposición
				}else{
					ReplacementFactor = replacementFactor_aux;
				}
				
				mTab.setValue("XX_ReplacementFactor",ReplacementFactor);
			}
			//***************************************
			// fin del calculo del replacement factor
/**
			if(vendor_ID!=null){ //si el proveedor no es nulo
				
				try 
				{
					//Margen Estimado (O/C mas reciente del proveedor)
					BigDecimal margin_Aux = null;
	
					String SQL = "Select a.margin "
							+ "FROM (Select XX_EstimatedMargin margin from C_Order where C_BPartner_ID="
							+ (Integer) vendor_ID.getValue()
							+ " Order by Created desc) a " + "WHERE rownum<2";
	
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
	
					while (rs.next()) {
						mTab.setValue("XX_EstimatedMargin", rs
								.getBigDecimal("margin"));
						margin_Aux = rs.getBigDecimal("margin");
					}
	
					rs.close();
					pstmt.close();
					
					//Si el proveedor no tiene margen anterior (por departamento y mes/año)
					if(margin_Aux==null){
						
						SQL = "SELECT XX_MARGACCORDINGBUDPURCH "
						+"FROM XX_VMR_PRLD01 B, XX_VMR_DEPARTMENT C, XX_VMR_CATEGORY D "
						+"WHERE TO_CHAR (SYSDATE, 'YYYYMM') = B.XX_BUDGETYEARMONTH "
						
						+"AND C.XX_VMR_DEPARTMENT_ID = "+(Integer)department.getValue()+" "
						+"AND B.XX_CODEDEPARTMENT = C.VALUE "
						
						+"AND D.XX_VMR_CATEGORY_ID = "+(Integer)category_ID.getValue()+" "
						+"AND B.XX_CATEGORYCODE = D.VALUE "
		
						+"AND B.XX_CODESTORE=999 "
						+"AND B.XX_LINECODE=99 "
						+"AND B.XX_VMR_Sec=99 ";
						
						pstmt = DB.prepareStatement(SQL, null);
						rs = pstmt.executeQuery();
	
						while (rs.next()) {
							mTab.setValue("XX_EstimatedMargin", rs
									.getBigDecimal("XX_MARGACCORDINGBUDPURCH"));
						}
	
						rs.close();
						pstmt.close();
					}
	
				} catch (Exception e) {
					return e.getMessage();
				}
		
			}
*/	
			return ""+myreturn;
		}
	}

	/**
	 * Set the Estimated Factor.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String convertionRate(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			Integer convertionRate = (Integer) mField.getValue();
			GridField department = mTab.getField("XX_VMR_Department_ID");
			GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
			GridField country = mTab.getField("C_Country_ID");
			GridField currency = mTab.getField("C_Currency_ID");
			
			if(currency.getValue()!=null && convertionRate!=null && department.getValue()!=null && dispatchRoute.getValue()!=null && country.getValue()!=null)
			{ //si el tipo de cambio no es nulo
				
					BigDecimal increaseFactor = null;
					try 
					{
						//Obtengo el valor de increaseFactor
							
						String SQL = "Select XX_INCREASEFACTOR "
						+"FROM XX_VMR_INCREASEFACTOR "
						+"WHERE XX_VMR_DEPARTMENT_ID="+(Integer)department.getValue()+
						" AND ISACTIVE='Y'" +
						" AND XX_VLO_DispatchRoute_ID="+(Integer)dispatchRoute.getValue()+
						" AND C_Country_ID="+(Integer)country.getValue();
						
						PreparedStatement pstmt = DB.prepareStatement(SQL, null);
						ResultSet rs = pstmt.executeQuery();
						
						while(rs.next())
						{	
							increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
						}
						
						rs.close();
						pstmt.close();
						
						//Si el factor de incremento es nulo lo igualo a 0
						if(increaseFactor==null){
							increaseFactor= new BigDecimal(0);
							log.info("Increase Factor es Nulo");
						}
	
				} catch (Exception e) {
					return e.getMessage();
				}
	
				// obtengo el valor de convertionRate
				BigDecimal convertionRate_aux = new BigDecimal(0);
	
				try {
					String SQL = "Select MULTIPLYRATE " + "FROM C_CONVERSION_RATE "
							+ "WHERE C_CONVERSION_RATE_ID=" + convertionRate;
	
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
	
					while (rs.next()) {
						convertionRate_aux = rs.getBigDecimal("MULTIPLYRATE");
					}
	
					rs.close();
					pstmt.close();
	
				} catch (Exception e) {
					return e.getMessage();
				}
	
				// Set Estimated Factor
				BigDecimal estimatedFactor = new BigDecimal(0);
				if(increaseFactor!=null){
					estimatedFactor = convertionRate_aux.add(increaseFactor.multiply(convertionRate_aux));
				}else{
					estimatedFactor = convertionRate_aux;
				}
				mTab.setValue("XX_EstimatedFactor", estimatedFactor);
	
			}
	
			return "";
		}
	}

	/**
	 * arrivalPort Callout.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String arrivalPort (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
			
		if(mTab.getValue("XX_DispatchDate")!=null){
			
			BigDecimal dias = new BigDecimal(0);
			
			String SQL2 = "Select XX_IMPORTSLOGISTICSTIMETLI" +
			" FROM  XX_VLO_LeadTimes" +
			" WHERE C_BPartner_ID=" + mTab.getValue("C_BPartner_ID") +
				" AND C_Country_ID=" + mTab.getValue("C_Country_ID") +
				" AND XX_VLO_ArrivalPort_ID=" + mTab.getValue("XX_VLO_ArrivalPort_ID") + 
				" AND isactive='Y'";
			
			PreparedStatement pstmt2 = null;
			ResultSet rs2 = null;
			try 
			{	
				pstmt2 = DB.prepareStatement(SQL2, null);
				rs2 = pstmt2.executeQuery();

				while (rs2.next()) {
					dias = rs2.getBigDecimal("XX_IMPORTSLOGISTICSTIMETLI");
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, SQL2, e);
			} finally 
			{
				DB.closeResultSet(rs2);
				DB.closeStatement(pstmt2);
			}
			
			//System.out.println("dias " + dias);
			
			Calendar dateFrom = Calendar.getInstance();			
			dateFrom.setTimeInMillis(((Timestamp)mTab.getValue("XX_DispatchDate")).getTime());
			dateFrom.add(Calendar.DATE, dias.intValue());
			
			Timestamp withLeapTime = new Timestamp(dateFrom.getTimeInMillis());
//			System.out.println("mField.getValue()).getTime() " + ((Timestamp)mField.getValue()).getTime());
//			withLeapTime.setTime(((Timestamp)mField.getValue()).getTime() + (86400000*(dias.intValue()+7)));
			
			//Verifico que la fecha de llegada no sea un dia no laborable, y si es le sumo 1 dia mas
			Utilities check = new Utilities();
			while(check.isNonBusinessDay(withLeapTime)){
				withLeapTime.setTime(withLeapTime.getTime() + 86400000*(1));
			}
			
			
				if (mTab.getValue("XX_ArrivalDate")==null | mTab.getValue("C_Order_ID")==null | "PRO".equals((String)mTab.getValue("XX_OrderStatus")))
					mTab.setValue("XX_ArrivalDate", withLeapTime);
				mTab.setValue("XX_EstimatedDate", withLeapTime);
		}
			
			
			//Fechas de importacion
			//************************
			GridField country = mTab.getField("C_Country_ID");
			GridField dispatchDate = mTab.getField("XX_DispatchDate");
			GridField partner = mTab.getField("C_BPartner_ID");
			GridField arrivalPort = mTab.getField("XX_VLO_ArrivalPort_ID");
			
			importDates(mTab, ctx, country, dispatchDate, partner, arrivalPort);
			
			return "";
		}
	}
	
	/**
	 * Set the conversion Rate to null.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String country (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			mTab.setValue("XX_EstimatedFactor",0);
			mTab.setValue("XX_ReplacementFactor",0);
			String myreturn="";
			GridField department = mTab.getField("XX_VMR_Department_ID");
			GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
			GridField country = mTab.getField("C_Country_ID");
			GridField estimatedDate = mTab.getField("XX_EstimatedDate");
			GridField currency = mTab.getField("C_Currency_ID");
			GridField convertionRate = mTab.getField("XX_ConversionRate_ID");
			
			
			//Calcular el Estimated Factor
			//*****************************
			myreturn+=myreturn+=estimatedFactor(mTab, currency, department, convertionRate, country, dispatchRoute);
			
			
			//Calcular el Replacement Factor
			//*****************************
			if(currency.getValue()!= null && estimatedDate.getValue()!=null && dispatchRoute.getValue()!=null && department.getValue()!=null && country.getValue()!=null)
			{
							
				String fecha = estimatedDate.getValue().toString();
				
				//se trunca la fecha a 10 caracteres
				fecha=fecha.substring(0, 10);
				    
			    try
			    {    
		          	Date date ; 
		          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	date = (Date)formatter.parse(fecha);
		          	Calendar cal=Calendar.getInstance();
		          	cal.setTime(date);
		          	cal.add(Calendar.MONTH,3);
		          	formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	// Calendar to Date Conversion
		          	int year = cal.get(Calendar.YEAR);
		          	int month = cal.get(Calendar.MONTH);
		          	int day = cal.get(Calendar.DATE);
		          	Date auxDate = new Date((year-1900), month, day);
		          	fecha=formatter.format(auxDate);
			    }
			    catch (ParseException e)
			    {
			    	log.log(Level.SEVERE,"Fecha", e);
			    }    
		     

				BigDecimal increaseFactor = null;
			
				String SQL = "Select XX_INCREASEFACTOR "
					+"FROM XX_VMR_INCREASEFACTOR "
					+"WHERE XX_VMR_DEPARTMENT_ID="+(Integer)department.getValue()+
					" AND ISACTIVE='Y'" +
					" AND XX_VLO_DispatchRoute_ID="+(Integer)dispatchRoute.getValue()+
					" AND C_Country_ID="+(Integer)country.getValue();
			
				try 
				{
					//Obtengo el valor de increaseFactor
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
					}
					
					rs.close();
					pstmt.close();
					
					//Si el factor de incremento es nulo lo igualo a 0
					if(increaseFactor==null){
						increaseFactor= new BigDecimal(0);
						log.info("Increase Factor es Nulo");
					}
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
				
				//obtengo el valor de Replacement Factor
				BigDecimal replacementFactor_aux= new BigDecimal(0);
				BigDecimal multiplyRate = new BigDecimal(0);    //AGREGADO GHUCHET - Cambio en fórmula de Factor de Reposición
				SQL = "Select XX_ReplacementFactor, MultiplyRate  " +
				"FROM C_CONVERSION_RATE " +
				"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND C_CURRENCY_ID="+(Integer)currency.getValue()+" AND STARTDATE<= TO_DATE('"+fecha+"','YYYY-MM-DD') AND ENDDATE >= TO_DATE('"+fecha+"','YYYY-MM-DD'))";
				
				try 
				{	
			
					
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					int i=0;
					while(rs.next())
					{	
						replacementFactor_aux = rs.getBigDecimal("XX_ReplacementFactor");
						multiplyRate = rs.getBigDecimal("MultiplyRate");
						i++;
					}
					
					if(i==0){
						//return "No existe factor de reposición para la fecha Y monedas seleccionadas.";
					}
					
					rs.close();
					pstmt.close();
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
				
				//Set Replacement Factor
				BigDecimal ReplacementFactor = new BigDecimal(0);
				if(increaseFactor!=null){
					ReplacementFactor = replacementFactor_aux.add(increaseFactor.multiply(multiplyRate)); //MODIFICADO GHUCHET - Cambio en fórmula de Factor de Reposición
				}else{
					ReplacementFactor = replacementFactor_aux;
				}
				mTab.setValue("XX_ReplacementFactor",ReplacementFactor);
			}
			//***************************************
			// fin del calculo del replacement factor
		
			//Fechas de importacion
			//************************
			//GridField country = mTab.getField("C_Country_ID");
			GridField dispatchDate = mTab.getField("XX_DispatchDate");
			GridField partner = mTab.getField("C_BPartner_ID");
			GridField arrivalPort = mTab.getField("XX_VLO_ArrivalPort_ID");
			
			importDates(mTab, ctx, country, dispatchDate, partner, arrivalPort);
			
			return "";
		}
	}

	/**
	 * Set the conversion Rate to null.
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * @return null or error message
	 */
	public String currency (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{

		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
		
			return "";
		
		}
		else
		{

			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			mTab.setValue("XX_EstimatedFactor",0);
			mTab.setValue("XX_ReplacementFactor",0);
			String myreturn="";
			Integer conversionID=null;
			GridField orderType = mTab.getField("XX_OrderType");
			GridField department = mTab.getField("XX_VMR_Department_ID");
			GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
			GridField country = mTab.getField("C_Country_ID");
			GridField estimatedDate = mTab.getField("XX_EstimatedDate");
			GridField currency = mTab.getField("C_Currency_ID");
			//GridField convertionRate = mTab.getField("XX_ConversionRate_ID");
			
			
			//******************************
			//set del conversion Rate
			if(mField.getValue()!=null && orderType.getValue().equals("Importada")){ //si es internacional
				int j=0;
				try 
				{
					//Obtengo el valor de increaseFactor
						
					String SQL1 = "Select C_Conversion_Rate_ID " +
							"from C_Conversion_rate " +
							"where C_Currency_ID="+(Integer)mField.getValue()+" AND C_CONVERSIONTYPE_ID=(SELECT C_CONVERSIONTYPE_ID FROM C_CONVERSIONTYPE WHERE name='ESTIMADO') " +
							"AND C_CURRENCY_TO_ID=205 AND XX_PERIOD_ID=(Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND TO_DATE(STARTDATE,'DD-MM-YYYY')<=TO_DATE(SYSDATE,'DD-MM-YYYY') AND TO_DATE(ENDDATE,'DD-MM-YYYY') >= TO_DATE(SYSDATE,'DD-MM-YYYY'))";
					
					/*String SQL1 = "Select C_Conversion_Rate_ID " +
					"from C_Conversion_rate " +
					"where C_Currency_ID="+(Integer)mField.getValue()+" " +
					"AND C_CONVERSIONTYPE_ID="+ Env.getCtx().getContext("#C_ConversionTypeEstimado_ID") +
					"AND C_CURRENCY_TO_ID=205 AND XX_PERIOD_ID=(Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND TO_DATE(STARTDATE,'DD-MM-YYYY')<=TO_DATE(SYSDATE,'DD-MM-YYYY') AND TO_DATE(ENDDATE,'DD-MM-YYYY') >= TO_DATE(SYSDATE,'DD-MM-YYYY'))";
					 */
					
					PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
					ResultSet rs1 = pstmt1.executeQuery();
					
					
					while(rs1.next())
					{	
						j++;
						conversionID = rs1.getInt("C_Conversion_Rate_ID");
					}
					
					rs1.close();
					pstmt1.close();
					
				}catch (Exception e) {
					return e.getMessage();
				}
				
				if(j!=0){
					mTab.setValue("XX_ConversionRate_ID",conversionID);
				}
				else{
					//myreturn+=" No existe una tasa de conversión para esta moneda en el mes actual. ";
				}
			}
			
			//Calcular el Replacement Factor
			//****************************
			
			if(currency.getValue()!= null && estimatedDate.getValue()!=null && dispatchRoute.getValue()!=null && department.getValue()!=null && country.getValue()!=null)
			{
							
				String fecha = estimatedDate.getValue().toString();
				
				//se trunca la fecha a 10 caracteres
				fecha=fecha.substring(0, 10);
				    
			    try
			    {    
		          	Date date ; 
		          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	date = (Date)formatter.parse(fecha);
		          	Calendar cal=Calendar.getInstance();
		          	cal.setTime(date);
		          	cal.add(Calendar.MONTH,3); 	
		          	formatter = new SimpleDateFormat("yyyy-MM-dd");	
		          	// Calendar to Date Conversion
		          	int year = cal.get(Calendar.YEAR);
		          	int month = cal.get(Calendar.MONTH);
		          	int day = cal.get(Calendar.DATE);
		          	Date auxDate = new Date((year-1900), month, day);
		          	fecha=formatter.format(auxDate);
			    }
			    catch (ParseException e)
			    {
			    	log.log(Level.SEVERE,"Fecha", e); 
			    }    
		     
				BigDecimal increaseFactor = null;
				try 
				{
					//Obtengo el valor de increaseFactor
						
					String SQL = "Select XX_INCREASEFACTOR "
					+"FROM XX_VMR_INCREASEFACTOR "
					+"WHERE XX_VMR_DEPARTMENT_ID="+(Integer)department.getValue()+
					" AND ISACTIVE='Y'" +
					" AND XX_VLO_DispatchRoute_ID="+(Integer)dispatchRoute.getValue()+
					" AND C_Country_ID="+(Integer)country.getValue();
					
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
					}
					
					rs.close();
					pstmt.close();
					
					//Si el factor de incremento es nulo lo igualo a 0
					if(increaseFactor==null){
						log.info("Increase Factor es Nulo");
					}
					
				}
				catch(Exception e)
				{	
					return e.getMessage();
				}
				
				//obtengo el valor de Replacement Factor
				BigDecimal replacementFactor_aux= new BigDecimal(0);
				BigDecimal multiplyRate = new BigDecimal(0);    //AGREGADO GHUCHET - Cambio en fórmula de Factor de Reposición
				String SQL = "Select XX_ReplacementFactor, MultiplyRate  " +
				"FROM C_CONVERSION_RATE " +
				"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND C_CURRENCY_ID="+(Integer)currency.getValue()+" AND STARTDATE<= TO_DATE('"+fecha+"','YYYY-MM-DD') AND ENDDATE >= TO_DATE('"+fecha+"','YYYY-MM-DD'))";
				System.out.println(SQL);
				try 
				{	
			
					
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					int i=0;
					while(rs.next())
					{	
						replacementFactor_aux = rs.getBigDecimal("XX_ReplacementFactor");
						multiplyRate = rs.getBigDecimal("MultiplyRate");
						i++;
					}
					
					if(i==0){
						//return "No existe factor de reposición para la fecha Y monedas seleccionadas.";
					}
					
					rs.close();
					pstmt.close();
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
				
				//Set Replacement Factor
				BigDecimal ReplacementFactor = new BigDecimal(0);
				if(increaseFactor!=null){
					ReplacementFactor = replacementFactor_aux.add(increaseFactor.multiply(multiplyRate)); //MODIFICADO GHUCHET - Cambio en fórmula de Factor de Reposición
				}else{
					ReplacementFactor = replacementFactor_aux;
				}
			
				mTab.setValue("XX_ReplacementFactor",ReplacementFactor);

			}
			//***************************************
			// fin del calculo del replacement factor
			
			return ""+myreturn;
		
		}//fin de la logica de orden de compra
		
	}
	
	public String dispatchRoute (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
			
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
					
			
			mTab.setValue("XX_EstimatedFactor",0);
			mTab.setValue("XX_ReplacementFactor",0);
			String myreturn="";
			GridField department = mTab.getField("XX_VMR_Department_ID");
			GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
			GridField country = mTab.getField("C_Country_ID");
			GridField estimatedDate = mTab.getField("XX_EstimatedDate");
			GridField currency = mTab.getField("C_Currency_ID");
			GridField convertionRate = mTab.getField("XX_ConversionRate_ID");
			
			
			//calcular el Edtimated Factor
			myreturn+=estimatedFactor(mTab, currency, department, convertionRate, country, dispatchRoute);
				
			//Calcular el Replacement Factor
			//*****************************
			if(currency.getValue()!= null && estimatedDate.getValue()!=null && dispatchRoute.getValue()!=null && department.getValue()!=null && country.getValue()!=null)
			{
							
				String fecha = estimatedDate.getValue().toString();
				
				//se trunca la fecha a 10 caracteres
				fecha=fecha.substring(0, 10);
				    
			    try
			    {    
		          	Date date ; 
		          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	date = (Date)formatter.parse(fecha);
		          	Calendar cal=Calendar.getInstance();
		          	cal.setTime(date);
		          	cal.add(Calendar.MONTH,3);
		          	formatter = new SimpleDateFormat("yyyy-MM-dd");      	
		          	// Calendar to Date Conversion
		          	int year = cal.get(Calendar.YEAR);
		          	int month = cal.get(Calendar.MONTH);
		          	int day = cal.get(Calendar.DATE);
		          	Date auxDate = new Date((year-1900), month, day);
		          	fecha=formatter.format(auxDate);
			    }
			    catch (ParseException e)
			    {
			    	log.log(Level.SEVERE, "Fecha", e);   
			    }    
		     

				BigDecimal increaseFactor = null;
				try 
				{
					//Obtengo el valor de increaseFactor
						
					String SQL = "Select XX_INCREASEFACTOR "
					+"FROM XX_VMR_INCREASEFACTOR "
					+"WHERE XX_VMR_DEPARTMENT_ID="+(Integer)department.getValue()+
					" AND ISACTIVE='Y'" +
					" AND XX_VLO_DispatchRoute_ID="+(Integer)dispatchRoute.getValue()+
					" AND C_Country_ID="+(Integer)country.getValue();
					
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
					}
					
					rs.close();
					pstmt.close();
					
					//Si el factor de incremento es nulo lo igualo a 0
					if(increaseFactor==null){
						increaseFactor= new BigDecimal(0);
						log.info("Increase Factor es Nulo");
					}
					
				}
				catch(Exception e)
				{	
					return e.getMessage();
				}
				
				//obtengo el valor de Replacement Factor
				BigDecimal replacementFactor_aux= new BigDecimal(0);
				BigDecimal multiplyRate = new BigDecimal(0);    //AGREGADO GHUCHET - Cambio en fórmula de Factor de Reposición
				String SQL = "Select XX_ReplacementFactor, MultiplyRate  " +
				"FROM C_CONVERSION_RATE " +
				"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND C_CURRENCY_ID="+(Integer)currency.getValue()+" AND STARTDATE<= TO_DATE('"+fecha+"','YYYY-MM-DD') AND ENDDATE >= TO_DATE('"+fecha+"','YYYY-MM-DD'))";
				
				try 
				{	
			
					
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					int i=0;
					while(rs.next())
					{	
						replacementFactor_aux = rs.getBigDecimal("XX_ReplacementFactor");
						multiplyRate = rs.getBigDecimal("MultiplyRate");
						i++;
					}
					
					if(i==0){
						//return "No existe factor de reposición para la fecha Y monedas seleccionadas.";
					}
					
					rs.close();
					pstmt.close();
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
				
				//Set Replacement Factor
				BigDecimal ReplacementFactor = new BigDecimal(0);
				if(increaseFactor!=null){
					ReplacementFactor = replacementFactor_aux.add(increaseFactor.multiply(multiplyRate)); //MODIFICADO GHUCHET - Cambio en fórmula de Factor de Reposición
				}else{
					ReplacementFactor = replacementFactor_aux;
				}
				
				mTab.setValue("XX_ReplacementFactor",ReplacementFactor);
			}
			//***************************************
			// fin del calculo del replacement factor
			
			return ""+myreturn;
		}
	}
	
	/** Estimated Date */
	public String estimatedDate(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		
		if (isCalloutActive() || value == null)
			return "";
		
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
			
			Utilities check = new Utilities();
			if(check.isNonBusinessDay((Timestamp)value)){
				return "NonBusinessDay";
			}
		
			String myreturn="";
			BigDecimal repFactor;
			Object objetoFactor = mTab.getValue("XX_ReplacementFactor");
			
			if (objetoFactor.getClass() == new Integer(0).getClass()){
				repFactor = new BigDecimal((Integer)objetoFactor);
			}else{
				repFactor = (BigDecimal)objetoFactor;
			}
			
			mTab.setValue("XX_ReplacementFactor",new BigDecimal(0));
			
			//Calcular el Replacement Factor
			//*****************************
			GridField department = mTab.getField("XX_VMR_Department_ID");
			GridField dispatchRoute = mTab.getField("XX_VLO_DispatchRoute_ID");
			GridField country = mTab.getField("C_Country_ID");
			GridField estimatedDate = mTab.getField("XX_EstimatedDate");
			GridField currency = mTab.getField("C_Currency_ID");
	
			if(currency.getValue()!= null && estimatedDate.getValue()!=null && dispatchRoute.getValue()!=null && department.getValue()!=null && country.getValue()!=null){
	
				String fecha = estimatedDate.getValue().toString();
				
				//se trunca la fecha a 10 caracteres
				fecha=fecha.substring(0, 10);
				    
			    try
			    {    
		          	Date date ; 
		          	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	date = (Date)formatter.parse(fecha);
		          	Calendar cal=Calendar.getInstance();
		          	cal.setTime(date);
		          	cal.add(Calendar.MONTH,3);
		          	formatter = new SimpleDateFormat("yyyy-MM-dd");
		          	// Calendar to Date Conversion
		          	int year = cal.get(Calendar.YEAR);
		          	int month = cal.get(Calendar.MONTH);
		          	int day = cal.get(Calendar.DATE);
		          	Date auxDate = new Date((year-1900), month, day);
		          	fecha=formatter.format(auxDate);
			    }
			    catch (ParseException e)
			    {
			    	log.log(Level.SEVERE, "Fecha", e);   
			    }    
		     
	
				BigDecimal increaseFactor = null;
				String SQL = "Select XX_INCREASEFACTOR "
					+ "FROM XX_VMR_INCREASEFACTOR "
					+ "WHERE XX_VMR_DEPARTMENT_ID="
					+ (Integer) department.getValue()
					+ " AND ISACTIVE='Y'"
					+ " AND XX_VLO_DispatchRoute_ID="
					+ (Integer) dispatchRoute.getValue()
					+ " AND C_Country_ID=" + (Integer) country.getValue();
	
				try {
					// Obtengo el valor de increaseFactor
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
	
					while (rs.next()) {
						increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
					}
	
					rs.close();
					pstmt.close();
	
					// Si el factor de incremento es nulo lo igualo a 0
					if (increaseFactor == null) {
						increaseFactor = new BigDecimal(0);
						log.info("Increase Factor es Nulo");
					}
	
				} catch (Exception e) {
					log.log(Level.SEVERE, SQL, e);
				}
				
				//obtengo el valor de Replacement Factor
				BigDecimal replacementFactor_aux= new BigDecimal(0);
				BigDecimal multiplyRate = new BigDecimal(0);    //AGREGADO GHUCHET - Cambio en fórmula de Factor de Reposición
				SQL = "Select XX_ReplacementFactor, MultiplyRate  " +
				"FROM C_CONVERSION_RATE " +
				"WHERE XX_PERIOD_ID = (Select C_PERIOD_ID from C_PERIOD where IsActive='Y' AND AD_CLIENT_ID = "+ctx.getAD_Client_ID()+" AND C_CURRENCY_ID="+(Integer)currency.getValue()+" AND STARTDATE<= TO_DATE('"+fecha+"','YYYY-MM-DD') AND ENDDATE >= TO_DATE('"+fecha+"','YYYY-MM-DD'))";
				
				try 
				{	
			
					
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					int i=0;
					while(rs.next())
					{	
						replacementFactor_aux = rs.getBigDecimal("XX_ReplacementFactor");
						multiplyRate = rs.getBigDecimal("MultiplyRate");
						i++;
					}
					
					if(i==0){
						//return "No existe factor de reposición para la fecha Y monedas seleccionadas.";
					}
					
					rs.close();
					pstmt.close();
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
				
				//Set Replacement Factor
				BigDecimal ReplacementFactor = new BigDecimal(0);
				if(increaseFactor!=null){
					ReplacementFactor = replacementFactor_aux.add(increaseFactor.multiply(multiplyRate)); //MODIFICADO GHUCHET - Cambio en fórmula de Factor de Reposición
				}else{
					ReplacementFactor = replacementFactor_aux;
				}
				
				mTab.setValue("XX_ReplacementFactor", ReplacementFactor);
				
				if(!repFactor.equals(ReplacementFactor)){
					// Modifying Costs
					//modifyNacCost(ctx, WindowNo, mTab, mField, value);
				}
			}
			//***************************************
			// fin del calculo del replacement factor
			return ""+myreturn;
		}
	}
	

	/**
	 *  Set Estimated Factor.
	 *	@param GridTab mTab
	 *	@param GridField currency
	 *	@param GridField department
	 *	@param GridField convertionRate
	 *	@param GridField country
	 *  @param GridField dispatchRoute
	 *	@return "" or error message
	 */
	private String estimatedFactor(GridTab mTab,GridField currency, GridField department,GridField convertionRate,GridField country, GridField dispatchRoute){
		
		//Verifico si es una orden compra o de venta
		GridField salesOrder= mTab.getField("IsSOTrx");
		
		if(salesOrder.getValue().equals(true))
		{
			
			//<----------- PARA ORDEN DE VENTA ----------->
			//**********************************************
			
			return "";
		
		}
		else
		{
		
			//<----------- PARA ORDEN DE COMPRA ----------->
			//**********************************************
		
			if(currency.getValue()!=null && convertionRate.getValue()!=null && department.getValue()!=null && dispatchRoute.getValue()!=null && country.getValue()!=null){ //si el tipo de cambio no es nulo	
				
				BigDecimal increaseFactor = null;
				String SQL = "Select XX_INCREASEFACTOR "
					+"FROM XX_VMR_INCREASEFACTOR "
					+"WHERE XX_VMR_DEPARTMENT_ID="+(Integer)department.getValue()+
					" AND ISACTIVE='Y'" +
					" AND XX_VLO_DispatchRoute_ID="+(Integer)dispatchRoute.getValue()+
					" AND C_Country_ID="+(Integer)country.getValue();
					
				try 
				{
					//Obtengo el valor de increaseFactor
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						increaseFactor = rs.getBigDecimal("XX_INCREASEFACTOR");
					}
					
					rs.close();
					pstmt.close();
					
					//Si el factor de incremento es nulo lo igualo a 0
					if(increaseFactor==null){
						increaseFactor= new BigDecimal(0);
						//System.out.println("increaseFactor es nulo2");
					}
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
				
				//obtengo el valor de convertionRate
				BigDecimal convertionRate_aux= new BigDecimal(0);
				SQL = "Select MULTIPLYRATE "
					+"FROM C_CONVERSION_RATE "
					+"WHERE C_CONVERSION_RATE_ID="+(Integer)convertionRate.getValue();
					
				try 
				{	
					PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						convertionRate_aux = rs.getBigDecimal("MULTIPLYRATE");
					}
					
					rs.close();
					pstmt.close();
					
				}
				catch(Exception e)
				{	
					log.log(Level.SEVERE, SQL, e);
				}
				
				//Set Estimated Factor
				BigDecimal estimatedFactor = new BigDecimal(0);
				if(increaseFactor!=null){
					estimatedFactor = convertionRate_aux.add(increaseFactor.multiply(convertionRate_aux));
				}else{
					estimatedFactor = convertionRate_aux;
				}
				mTab.setValue("XX_EstimatedFactor",estimatedFactor);
				
				
			}
			return "";
		}
	}

	
	/**
	 * Detail costs modifier
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * 
	 */
	public void modifyNacCost(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {		
		
		Integer orderID = (Integer) mTab.getValue("C_Order_ID");
		String orderType = (String) mTab.getValue("XX_OrderType");
		BigDecimal factor = new BigDecimal(0);
		BigDecimal defFactor = (BigDecimal) mTab.getValue("XX_DefinitiveFactor");
		int StdPrecision = ctx.getStdPrecision();
		
		//Validando que sea una orden internacional 
		if (orderType.equals("Importada"))	
		{
			// Verificando si hay o no factor definitivo
			if ((defFactor == null)
					|| (0 == (defFactor.compareTo(new BigDecimal(0))))) {
				factor = (BigDecimal) mTab.getValue("XX_ReplacementFactor");
	
				if ((1 == (factor.compareTo(new BigDecimal(0))))) {
						
					// Obteniendo los costos para luego multiplicarlos por el Factor
					// de Reposición y actualizar
					String SQL0 = "SELECT p.XX_VMR_PO_LINEREFPROV_ID id, p.XX_UNITPURCHASEPRICE costo, " 
						+ "p.XX_REBATE1 desc1, p.XX_REBATE2 desc2, p.XX_REBATE3 desc3, p.XX_REBATE4 desc4, " 
						+ "c.XX_UnitConversion unidadCompra, v.XX_UnitConversion unidadVenta, " 
						+ "p.XX_MARGIN margen, p.XX_SALEPRICE PVP FROM XX_VMR_PO_LINEREFPROV p " 
						+ "INNER JOIN XX_VMR_UnitConversion c on c.XX_VMR_UnitConversion_ID = p.XX_VMR_UnitConversion_ID " 
						+ "INNER JOIN XX_VMR_UnitConversion v on v.XX_VMR_UnitConversion_ID = p.XX_PiecesBySale_ID " 
						+ "WHERE p.C_ORDER_ID = " + orderID;
					
					PreparedStatement pstmt0 = DB.prepareStatement(SQL0, null);
					ResultSet rs0;
	
					try {
						rs0 = pstmt0.executeQuery();
	
						while (rs0.next()) {
							Integer id = rs0.getInt("id");
							BigDecimal costo = rs0.getBigDecimal("costo");
							BigDecimal desc1 = rs0.getBigDecimal("desc1");
							BigDecimal desc2 = rs0.getBigDecimal("desc2");
							BigDecimal desc3 = rs0.getBigDecimal("desc3");
							BigDecimal desc4 = rs0.getBigDecimal("desc4");
							BigDecimal margen = rs0.getBigDecimal("margen");
							Integer unidadCompra = rs0.getInt("unidadCompra");
							Integer unidadVenta = rs0.getInt("unidadVenta");
							BigDecimal PVP = rs0.getBigDecimal("PVP");
							/*
							System.out.println("ID " + id + " Costo " + costo);
							System.out.println("Descuentos " + desc1 + " " + desc2 + " " + desc3 + " " + desc4);
							System.out.println("Margen " + margen + " Unidad de compra " + unidadCompra 
									+ " Unidad de Venta " + unidadVenta + " PVP " + PVP);
							*/
							Integer compraUnit = 0;
							
							Integer ventaUnit = 0;
									
							// Multiplying Dolar Cost * Exchange Factor to get Nacional Cost
							BigDecimal costoNac = costo.multiply(factor);
							
							if (costoNac.scale() > StdPrecision)
								costoNac = costoNac.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
						
							if (desc1 != null && 0 != (desc1.compareTo(new BigDecimal(0)))) {
								costoNac = costoNac.subtract(costoNac.multiply(desc1.multiply(new BigDecimal(0.01))));
							}
							if (desc2 != null && 0 != (desc2.compareTo(new BigDecimal(0)))) {
								costoNac = costoNac.subtract(costoNac.multiply(desc2.multiply(new BigDecimal(0.01))));
							}
							if (desc3 != null && 0 != (desc3.compareTo(new BigDecimal(0)))) {
								costoNac = costoNac.subtract(costoNac.multiply(desc3.multiply(new BigDecimal(0.01))));
							}
							if (desc4 != null && 0 != (desc4.compareTo(new BigDecimal(0)))) {
								costoNac = costoNac.subtract(costoNac.multiply(desc4.multiply(new BigDecimal(0.01))));
							}
	
							BigDecimal costoUnit = new BigDecimal(0);
							if ((unidadCompra != 0) && (unidadVenta != 0)){
								
								System.out.println("Entro");
								costoUnit = costoNac.divide(new BigDecimal(compraUnit*ventaUnit));
								
								if (costoUnit.scale() > StdPrecision)
									costoUnit = costoUnit.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
							}
							//System.out.println(costoUnit);							
							
							BigDecimal margin = new BigDecimal(0);
							if (0 == (PVP.compareTo(new BigDecimal(0)))){
								margin = ((PVP.subtract(costoUnit)).divide(PVP,2, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));;
							
								if (margin.scale() > StdPrecision)
									margin = margin.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
							}
							//System.out.println("Nuevo Margen " + margin);
							
							String sql = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_MARGIN = " + margin 
								+ ", PRICEACTUAL = " + costoNac
								+ " WHERE XX_VMR_PO_LINEREFPROV_ID = " + id;
						
							DB.executeUpdate(null, sql );
						}
						pstmt0.close();
						rs0.close();
						
					} catch (SQLException e) {
						log.log(Level.SEVERE, SQL0, e);
					}					
				}
			}
		}
		
	}

	/**
	 * Detail costs modifier
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * 
	 */
	public void modifyCosts(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {

		Integer orderID = (Integer) mTab.getValue("C_Order_ID");
		String orderType = (String) mTab.getValue("XX_OrderType");
		BigDecimal factor = new BigDecimal(0);
		BigDecimal defFactor = (BigDecimal) mTab.getValue("XX_DefinitiveFactor");
		int StdPrecision = ctx.getStdPrecision();
		
		//Validando que sea una orden internacional 
		if (orderType.equals("Importada"))	
		{
		// Verificando si hay o no factor definitivo
		if ((defFactor == null)
				|| (0 == (defFactor.compareTo(new BigDecimal(0))))) {
			factor = (BigDecimal) mTab.getValue("XX_ReplacementFactor");

			if ((1 == (factor.compareTo(new BigDecimal(0))))) {

				// Obteniendo los costos para luego multiplicarlos por el Factor
				// de Reposición y actualizar
				String SQL0 = "SELECT XX_VMR_PO_LINEREFPROV_ID AS id, XX_UNITPURCHASEPRICE AS costo, "
						+ "XX_REBATE1 AS desc1, XX_REBATE2 AS desc2, XX_REBATE3 AS desc3, XX_REBATE4 AS desc4, "
						+ "XX_MARGIN as margen, C_TAX_ID as taxID, XX_PIECESBY AS compraUnit, " 
						+ "XX_PIECESBYSALE AS ventaUnit, QTY " 
						+ "FROM XX_VMR_PO_LINEREFPROV WHERE C_ORDER_ID = "
						+ orderID;
				
				PreparedStatement pstmt0 = DB.prepareStatement(SQL0, null);
				ResultSet rs0;

				try {
					rs0 = pstmt0.executeQuery();

					while (rs0.next()) {
						Integer id = rs0.getInt("id");
						BigDecimal costo = rs0.getBigDecimal("costo");
						BigDecimal desc1 = rs0.getBigDecimal("desc1");
						BigDecimal desc2 = rs0.getBigDecimal("desc2");
						BigDecimal desc3 = rs0.getBigDecimal("desc3");
						BigDecimal desc4 = rs0.getBigDecimal("desc4");
						BigDecimal margen = rs0.getBigDecimal("margen");
						Integer taxID = rs0.getInt("taxID");
						BigDecimal compraUnit = rs0.getBigDecimal("compraUnit");
						BigDecimal ventaUnit = rs0.getBigDecimal("ventaUnit");
						BigDecimal qty = rs0.getBigDecimal("QTY");

						BigDecimal costoNac = costo.multiply(factor);
						if (desc1 != null
								&& 0 != (desc1.compareTo(new BigDecimal(0)))) {
							costoNac = costoNac.subtract(costoNac
									.multiply(desc1.multiply(new BigDecimal(
											0.01))));
						}
						if (desc2 != null
								&& 0 != (desc2.compareTo(new BigDecimal(0)))) {
							costoNac = costoNac.subtract(costoNac
									.multiply(desc2.multiply(new BigDecimal(
											0.01))));
						}
						if (desc3 != null
								&& 0 != (desc3.compareTo(new BigDecimal(0)))) {
							costoNac = costoNac.subtract(costoNac
									.multiply(desc3.multiply(new BigDecimal(
											0.01))));
						}
						if (desc4 != null
								&& 0 != (desc4.compareTo(new BigDecimal(0)))) {
							costoNac = costoNac.subtract(costoNac
									.multiply(desc4.multiply(new BigDecimal(
											0.01))));
						}

						if (costoNac.scale() > StdPrecision)
							costoNac = costoNac.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);

						BigDecimal costoUnit = costoNac.divide(compraUnit.multiply(ventaUnit));
						
						if (costoUnit.scale() > StdPrecision)
							costoUnit = costoUnit.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
						
						//creo que falta recalcular el margen :S
						
						// Validación del Margen para evitar luego una división entre 0
						if (0 == (margen.compareTo(new BigDecimal(100)))) {
							margen = new BigDecimal(99);
						}
						BigDecimal precioVenta = costoUnit.divide((new BigDecimal(1.00)).subtract(margen.
								divide(new BigDecimal(100.00), 2, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);
							
						if (precioVenta.scale() > StdPrecision)
							precioVenta = precioVenta.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
												
						String sql = "SELECT Rate FROM C_Tax WHERE C_Tax_ID=" + taxID;

						PreparedStatement prst = DB.prepareStatement(sql, null);
						BigDecimal Tax = new BigDecimal(1);

						try {
							ResultSet rs = prst.executeQuery();

							if (rs.next()) {
								Tax = rs.getBigDecimal("Rate");
								
								BigDecimal PVP = precioVenta.add(precioVenta.multiply(Tax.multiply(new BigDecimal(0.01))));
								
								if (PVP.scale() > StdPrecision)
									PVP = PVP.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
								
								BigDecimal PVPbeco = priceBeco(PVP);
								
								//Si el PVP es distinto al PVPbeco, recarcular todos los valores
								if (!PVP.equals(PVPbeco)){
									/*
									 * SalePricePlusTax = PVPbeco 
									 * CostoNacional = costoNac 
									 * Tax = Tax 
									 * PurchaseUnit = compraUnit
									 * SaleUnit = ventaUnit
									 * Qty = qty
									 * */ 
									ModifyPVP(ctx, PVPbeco, costoNac, Tax, compraUnit, ventaUnit, qty, id);
								}
							}
						} catch (SQLException e) {
							log.log(Level.SEVERE, sql, e);
							// e.printStackTrace();

						}
					}

				} catch (SQLException e) {
					log.log(Level.SEVERE, SQL0, e);
				}

			} 
		}
		}

	}

	/**
	 * Change a price for a Beco's Price
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * 
	 */
	public BigDecimal priceBeco(BigDecimal precio) {
		
		try 
		{
				
				String priceRuleSQL = "select xx_lowrank,xx_highrank,xx_termination,xx_increase,xx_infinitevalue " +
						"from xx_vme_pricerule order by (xx_lowrank)";
				PreparedStatement priceRulePstmt = DB.prepareStatement(priceRuleSQL, null);
				ResultSet priceRuleRs = priceRulePstmt.executeQuery();
	
				Integer precioInt = precio.intValue();
				BigDecimal precioBig = new BigDecimal(precioInt);
				while(priceRuleRs.next())
				{	
					 if(precioBig.compareTo(priceRuleRs.getBigDecimal("xx_lowrank"))>=0 && precioBig.compareTo(priceRuleRs.getBigDecimal("xx_highrank"))<=0) 
				     {
				    	 Integer incremento = priceRuleRs.getInt("xx_increase");
				    	  
				    	 for(Integer i=priceRuleRs.getInt("xx_lowrank")-1;i<=priceRuleRs.getInt("xx_highrank");i=i+incremento)
				    	 {
				    		 BigDecimal var =new BigDecimal(i);
				    		 
				    		 if(precioBig.compareTo(var) <= 0)
				    		 {
				    			  BigDecimal beco=var;
				    			  
				    			 BigDecimal terminacion = priceRuleRs.getBigDecimal("xx_termination");
				    			 if(terminacion.intValue()==0)
				    			 {
				    				 beco = var.add(terminacion);
				    			 }
				    			 else
				    			 {
				    				var = var.divide(new BigDecimal(10));
				    				Integer aux= var.intValue()*10;
				    				beco = new BigDecimal(aux).add(terminacion);
				    			 }
				    			 //mTab.setValue("PriceList", beco);
				    			 priceRuleRs.close();
				 				 priceRulePstmt.close();
				 				 
				 				 if(beco.compareTo(precio)==0)
				 				 {
				 					 return precio;
				 				 }
				 				 else
				 				 {
				 					 return beco;
				 				 }
				    		 }
 
				    	 }
				     }
				}
				priceRuleRs.close();
				priceRulePstmt.close();
				
				return new BigDecimal(0);
		}		

		catch(Exception e)
		{	
			return precio;
		}
	}

	/**
	 * PVP(with Tax) modifier
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * 
	 */
	public void ModifyPVP(Ctx ctx, BigDecimal SalePricePlusTax, BigDecimal CostoNacional, 
			BigDecimal Tax, BigDecimal PurchaseUnit, BigDecimal SaleUnit, BigDecimal Qty, Integer id ) {

		int StdPrecision = ctx.getStdPrecision();
		
		BigDecimal Costo = (CostoNacional.divide(PurchaseUnit,
				2, RoundingMode.HALF_UP)).multiply(SaleUnit);
		if (Costo.scale() > StdPrecision)
			Costo = Costo.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		
		BigDecimal SalePrice = (SalePricePlusTax.multiply(new BigDecimal(100)))
				.divide((Tax.add(new BigDecimal(100))), 2, RoundingMode.HALF_UP);
		if (SalePrice.scale() > StdPrecision)
			SalePrice = SalePrice.setScale(StdPrecision,
					BigDecimal.ROUND_HALF_UP);
		
		BigDecimal TaxAmount = SalePricePlusTax.subtract(SalePrice);
		if (TaxAmount.scale() > StdPrecision)
			TaxAmount = TaxAmount.setScale(StdPrecision,
					BigDecimal.ROUND_HALF_UP);
		
		BigDecimal Margin = ((SalePrice.subtract(Costo)).divide(SalePrice, 2,
				RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
		if (Margin.scale() > StdPrecision)
			Margin = Margin.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		
		BigDecimal saleQty = (Qty.multiply(PurchaseUnit)).divide(SaleUnit); 
		Integer SaleQty = saleQty.intValue();
		
		BigDecimal LinePVPAmount = SalePrice.multiply(new BigDecimal(SaleQty)); 
		if (LinePVPAmount.scale() > StdPrecision)
			LinePVPAmount = LinePVPAmount.setScale(StdPrecision,
					BigDecimal.ROUND_HALF_UP);
		
		BigDecimal LinePlusTaxAmount = SalePricePlusTax
				.multiply(new BigDecimal(SaleQty));
		if (LinePlusTaxAmount.scale() > StdPrecision)
			LinePlusTaxAmount = LinePlusTaxAmount.setScale(StdPrecision,
					BigDecimal.ROUND_HALF_UP);
		
		BigDecimal LineQty = (Qty.multiply(PurchaseUnit));
		if (LineQty.scale() > StdPrecision)
			LineQty = LineQty.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		
		String SQL1 = "UPDATE XX_VMR_PO_LINEREFPROV SET XX_SALEPRICE = "
				+ SalePrice + ", XX_TAXAMOUNT = " + TaxAmount
				+ ", XX_LINEPVPAMOUNT = " + LinePVPAmount 
				+ ", XX_LINEPLUSTAXAMOUNT = " + LinePlusTaxAmount
				+ " WHERE XX_VMR_PO_LINEREFPROV_ID = "
				+ id;
		
		DB.executeUpdate(null, SQL1 );
	}
	
	/**
	 * Cargo Agent Delivery Estimated Date Callout
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * 
	 */
	public String cargoDate(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		GridField aShipDATE = mTab.getField("XX_CustDisEstiDatePO");
		GridField bToVeDate = mTab.getField("XX_EstArrivalDateToVzla");
	    GridField cPayDate = mTab.getField("XX_CustDutEstPayDatePO");
		GridField dCustShipDate = mTab.getField("XX_CustDutShipEstiDatePO");
		GridField eCDDate = mTab.getField("XX_CDArrivalEstimatedDatePO");
		
		GridField country = mTab.getField("C_Country_ID");
		GridField partner = mTab.getField("C_BPartner_ID");
		GridField arrivalPort = mTab.getField("XX_VLO_ArrivalPort_ID");
		
		if(aShipDATE.getValue()!=null && bToVeDate.getValue()!=null && cPayDate.getValue()!=null 
				&& eCDDate.getValue()!=null && value!=null && arrivalPort.getValue()!=null
				&& country.getValue()!=null && partner.getValue()!=null && dCustShipDate.getValue()!=null){
			
			//**************Fechas****************
			 BigDecimal TEI_B = new BigDecimal(0);
			 BigDecimal TT_B = new BigDecimal(0);
			 BigDecimal TRC_B = new BigDecimal(0);
			 BigDecimal TNAC_B = new BigDecimal(0);
			 BigDecimal TEN_B = new BigDecimal(0);
			 BigDecimal TLI_B = new BigDecimal(0);

			String SQL1 = "Select MAX(XX_VLO_LEADTIMES_ID) "+
		    	  "FROM XX_VLO_LEADTIMES "+
		          "WHERE "+
		          "XX_VLO_ARRIVALPORT_ID = " + arrivalPort.getValue() +
		          " AND C_BPARTNER_ID = " + partner.getValue() +
		          " AND C_COUNTRY_ID = " + country.getValue() +
		          " AND AD_Client_ID IN(0,"+ctx.getAD_Client_ID()+") ";
				
			String SQL = ("SELECT XX_TRANSITTIMETT, XX_TIMEREGISTCELLATIONTRC, XX_NATIONALIZATIONTIMETNAC, XX_NACARRIVALTIMETEN, XX_INTERNACARRIVALTIMETEI, XX_IMPORTSLOGISTICSTIMETLI "+
				   "FROM XX_VLO_LEADTIMES "+
				   "WHERE "+
				   "XX_VLO_LEADTIMES_ID = "+
				   "("+SQL1+") "+
			       "AND AD_Client_ID IN(0,"+ctx.getAD_Client_ID()+") "
				   );

			try{
					
				PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
				ResultSet rs = pstmt.executeQuery();
					 
				if(rs.next())
				{
				    TEI_B = rs.getBigDecimal("XX_INTERNACARRIVALTIMETEI");
				    TT_B = rs.getBigDecimal("XX_TRANSITTIMETT");
				    TRC_B = rs.getBigDecimal("XX_TIMEREGISTCELLATIONTRC");
				    TNAC_B = rs.getBigDecimal("XX_NATIONALIZATIONTIMETNAC");
				    TEN_B = rs.getBigDecimal("XX_NACARRIVALTIMETEN");
				    TLI_B = rs.getBigDecimal("XX_IMPORTSLOGISTICSTIMETLI");
				}
				
				rs.close();
				pstmt.close();
				    
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
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
			Timestamp cargoDate= (Timestamp)value;
			
			//XX_CARAGENTDELIVESTEMEDDATE
			date.setTimeInMillis(cargoDate.getTime());
			
	        //XX_SHIPPREALESTEEMEDDATE
	        date.add(Calendar.DATE, TEI_B.intValue());
	        dateSUM = new Timestamp(date.getTimeInMillis());
	        mTab.setValue("XX_CustDisEstiDatePO", dateSUM);
	        
	        //XX_VZLAARRIVALESTEMEDDATE
	        date.add(Calendar.DATE, TT_B.intValue());
	        dateSUM = new Timestamp(date.getTimeInMillis());
	        mTab.setValue("XX_EstArrivalDateToVzla", dateSUM);
	        
	        //XX_CustDutEstPayDate
	        date.add(Calendar.DATE, TRC_B.intValue());
	        dateSUM = new Timestamp(date.getTimeInMillis());
	        mTab.setValue("XX_CustDutEstPayDatePO", dateSUM);
	        
	        //XX_CustDutEstShipDate
	        date.add(Calendar.DATE, TNAC_B.intValue());
	        dateSUM = new Timestamp(date.getTimeInMillis());
	        mTab.setValue("XX_CustDutShipEstiDatePO", dateSUM);      
	        
	        //XX_CDARRIVALESTEEMEDDATE
	        date.add(Calendar.DATE, TEN_B.intValue());
	        dateSUM = new Timestamp(date.getTimeInMillis());
	        
	        
	        //se verifica que no sea un dia no laborable
	        Utilities check = new Utilities();
			while(check.isNonBusinessDay(dateSUM)){
				dateSUM.setTime(dateSUM.getTime() + 86400000*(1));
			}
	        
	        mTab.setValue("XX_CDArrivalEstimatedDatePO", dateSUM);
			
		}

		BigDecimal dias = new BigDecimal(0);
		
		String SQL = "Select XX_IMPORTSLOGISTICSTIMETLI" +
		" FROM  XX_VLO_LeadTimes" +
		" WHERE C_BPartner_ID=" + mTab.getValue("C_BPartner_ID") +
			" AND C_Country_ID=" + mTab.getValue("C_Country_ID") +
			" AND XX_VLO_ArrivalPort_ID=" + mTab.getValue("XX_VLO_ArrivalPort_ID") + 
			" AND isactive='Y'";
		
		try 
		{	
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				dias = rs.getBigDecimal("XX_IMPORTSLOGISTICSTIMETLI");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, SQL, e);
		}
		
		GridField CarAgeDelEstDate = mTab.getField("XX_CARAGENTDELIVESTEMEDDATE");
		
		if(CarAgeDelEstDate.getValue()!=null){
		
			Calendar dateFrom = Calendar.getInstance();			

			dateFrom.setTimeInMillis(((Timestamp)mTab.getField("XX_CARAGENTDELIVESTEMEDDATE").getValue()).getTime());
			dateFrom.add(Calendar.DATE, (dias.intValue()));
			
			Timestamp withLeapTime = new Timestamp(dateFrom.getTimeInMillis());
			
			if(mField.getValue()!=null){
				Utilities check1 = new Utilities();
				while(check1.isNonBusinessDay(withLeapTime)){
					withLeapTime.setTime(withLeapTime.getTime() + 86400000*(1));
				}
				mTab.setValue("XX_EstimatedDate", withLeapTime);
			}
		}

		
		return "";
	}
	
	
	/**
	 * warehouse
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * 
	 */
	public String warehouse(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		CalloutOrder X = new CalloutOrder();
		String oldCallout = X.warehouse(ctx, WindowNo, mTab, mField, value);

		if (isCalloutActive() || value == null)			
			return "";
		setCalloutActive(true);
		
		MWarehouse warehouse = new MWarehouse(Env.getCtx(),(Integer)value,null);
		
		 mTab.setValue("AD_Org_ID", warehouse.getAD_Org_ID());
		 setCalloutActive(false);
		return oldCallout+"";
	}
	
	/**
	 * TypeDelivery
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param mTab
	 *            tab
	 * @param mField
	 *            field
	 * @param value
	 *            value
	 * 
	 */
	public String typeDelivery(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		String SQL = "Select M_Warehouse_ID " +
				     "FROM M_Warehouse" +
				     "WHERE " +
				     "VALUE = 001 AND ISACTIVE = 'Y' AND AD_CLIENT_ID IN(0,"+ctx.getAD_Client_ID()+")";
		
		int warehouse=0;
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				warehouse = rs.getInt("M_Warehouse_ID");
			}
			
			rs.close();
			pstmt.close();
			    
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		}
		
		 mTab.setValue("M_Warehouse_ID", warehouse);
		
		return "";
	}
	
		
	private void importDates(GridTab mTab, Ctx ctx, GridField country, GridField dispatchDate, GridField partner, GridField arrivalPort){
		
		if(country.getValue()!= null && dispatchDate.getValue()!=null && partner.getValue()!=null && arrivalPort.getValue()!=null){
			
			BigDecimal TEI_B = new BigDecimal(0);
			BigDecimal TT_B = new BigDecimal(0);
			BigDecimal TRC_B = new BigDecimal(0);
			BigDecimal TNAC_B = new BigDecimal(0);
			BigDecimal TEN_B = new BigDecimal(0);
			BigDecimal TLI_B = new BigDecimal(0);
	
			String SQL_B = "Select MAX(XX_VLO_LEADTIMES_ID) "+
		    	  "FROM XX_VLO_LEADTIMES "+
		          "WHERE "+
		          "XX_VLO_ARRIVALPORT_ID = " + arrivalPort.getValue() +
		          " AND C_BPARTNER_ID = " + partner.getValue() +
		          " AND C_COUNTRY_ID = " + country.getValue() +
		          " AND AD_Client_ID IN(0,"+ctx.getAD_Client_ID()+") ";
				
			String SQL_A = ("SELECT XX_TRANSITTIMETT, XX_TIMEREGISTCELLATIONTRC, XX_NATIONALIZATIONTIMETNAC, XX_NACARRIVALTIMETEN, XX_INTERNACARRIVALTIMETEI, XX_IMPORTSLOGISTICSTIMETLI "+
				   "FROM XX_VLO_LEADTIMES "+
				   "WHERE "+
				   "XX_VLO_LEADTIMES_ID = "+
				   "("+SQL_B+") "+
			       "AND AD_Client_ID IN(0,"+ctx.getAD_Client_ID()+") "
				   );
	
			try{
					
				PreparedStatement pstmt = DB.prepareStatement(SQL_A, null); 
				ResultSet rs = pstmt.executeQuery();
					 
				if(rs.next())
				{
				    TEI_B = rs.getBigDecimal("XX_INTERNACARRIVALTIMETEI");
				    TT_B = rs.getBigDecimal("XX_TRANSITTIMETT");
				    TRC_B = rs.getBigDecimal("XX_TIMEREGISTCELLATIONTRC");
				    TNAC_B = rs.getBigDecimal("XX_NATIONALIZATIONTIMETNAC");
				    TEN_B = rs.getBigDecimal("XX_NACARRIVALTIMETEN");
				    TLI_B = rs.getBigDecimal("XX_IMPORTSLOGISTICSTIMETLI");
				}
				
				rs.close();
				pstmt.close();
				    
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL_A);
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
			
			Timestamp dispatchDate_Aux = (Timestamp)dispatchDate.getValue();
			date.setTimeInMillis(dispatchDate_Aux.getTime());
			
			//XX_CARAGENTDELIVESTEMEDDATE
//			date.add(Calendar.DATE, 7);          Esto fue comentado ya que abraham dice que no hay que sumarle 7 dias
		    dateSUM = new Timestamp(date.getTimeInMillis());
		    mTab.setValue("XX_CARAGENTDELIVESTEMEDDATE", dateSUM);
			
	       //XX_SHIPPREALESTEEMEDDATE
	       date.add(Calendar.DATE, TEI_B.intValue());
	       dateSUM = new Timestamp(date.getTimeInMillis());
	       mTab.setValue("XX_SHIPPREALESTEEMEDDATE", dateSUM);
	       mTab.setValue("XX_CustDisEstiDatePO", dateSUM);
	       
	       //XX_VZLAARRIVALESTEMEDDATE
	       date.add(Calendar.DATE, TT_B.intValue());
	       dateSUM = new Timestamp(date.getTimeInMillis());
	       mTab.setValue("XX_VZLAARRIVALESTEMEDDATE", dateSUM);
	       mTab.setValue("XX_EstArrivalDateToVzla", dateSUM);
	       
	       //XX_CustDutEstPayDate
	       date.add(Calendar.DATE, TRC_B.intValue());
	       dateSUM = new Timestamp(date.getTimeInMillis());
	       mTab.setValue("XX_CustDutEstPayDate", dateSUM);
	       mTab.setValue("XX_CustDutEstPayDatePO", dateSUM);
	       
	       //XX_CustDutEstShipDate
	       date.add(Calendar.DATE, TNAC_B.intValue());
	       dateSUM = new Timestamp(date.getTimeInMillis());
	       mTab.setValue("XX_CustDutEstShipDate", dateSUM); 
	       mTab.setValue("XX_CustDutShipEstiDatePO", dateSUM);      
	       
	       //XX_CDARRIVALESTEEMEDDATE
	       date.add(Calendar.DATE, TEN_B.intValue());
	       dateSUM = new Timestamp(date.getTimeInMillis());
	       
	        //se verifica que no sea un dia no laborable
	        Utilities check = new Utilities();
			while(check.isNonBusinessDay(dateSUM)){
				dateSUM.setTime(dateSUM.getTime() + 86400000*(1));
			}
	       
	       mTab.setValue("XX_CDARRIVALESTEEMEDDATE", dateSUM);
	       mTab.setValue("XX_CDArrivalEstimatedDatePO", dateSUM);
		}
		//fin Fechas de importacion **
	}
	
	
	/***
	 *	Order Line - Amount.
	 *		- called from QtyOrdered, Discount and PriceActual
	 *		- calculates Discount or Actual Amount
	 *		- calculates LineNetAmt
	 *		- enforces PriceLimit
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String lineDiscount(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		mTab.setValue("PriceList", value);
		mTab.setValue("Discount", BigDecimal.ZERO);
		
		return "";
	}	//	amt

}
