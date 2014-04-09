package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ProcessCtl;
import org.compiere.model.MCountry;
import org.compiere.model.MCurrency;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVLOBoardingGuide extends X_XX_VLO_BoardingGuide {

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	public MVLOBoardingGuide(Ctx ctx, int XX_VLO_BoardingGuide_ID, Trx trx) {
		super(ctx, XX_VLO_BoardingGuide_ID, trx);
	}
	
	public MVLOBoardingGuide(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		
		
	}

    public void setXX_MonthArrival (String XX_MonthArrival)
    {
        set_Value ("XX_MonthArrival", XX_MonthArrival);
        
    }
 
    public String getXX_MonthArrival() 
    {
        return (String)get_Value("XX_MonthArrival");
        
    }
	
    protected boolean beforeDelete()
	{
    	String SQL = "SELECT XX_ProcessedImport AS PROCESSED " +
    			"FROM XX_VLO_BOARDINGGUIDE " +
    			"WHERE XX_VLO_BOARDINGGUIDE_ID = "+ get_ID();
    	
    	String pro;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;    	
    	try
    	{
    		pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				pro = rs.getString("PROCESSED");
				
				if(pro.equalsIgnoreCase("Y"))
				{
					log.saveError("Error", Msg.getMsg(getCtx(), "La Guía de Embarque no puede ser borrada por que se encuentra Aprovada"));
					return false;
				}
				else
				{
					return true;	
				}
				
			}

    	}
    	catch (Exception e) {
			log.log(Level.SEVERE, SQL);
			return false;
    	} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
   
    	return false;
	} // end before Delete
    
	
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		
		String SQL = "Select C_Order_ID " +
					 "from C_Order " +
					 "where isSOTrx='N' AND XX_VLO_BOARDINGGUIDE_ID = "+ get_ID();
		
		Vector<Integer> orders = new Vector<Integer>();
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try{
			
			pstmt = DB.prepareStatement(SQL, null); 
			rs = pstmt.executeQuery();
			  
			while(rs.next())
			{
				orders.add(rs.getInt("C_Order_ID"));
		    }
		  	
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
			return false;
		} finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		
		for(int i=0; i<orders.size() ;i++){
			
			MOrder order = new MOrder(getCtx(), orders.get(i), get_Trx());
			
			 //**************Fechas****************
			 BigDecimal TEI_B = new BigDecimal(0);
			 BigDecimal TT_B = new BigDecimal(0);
			 BigDecimal TRC_B = new BigDecimal(0);
			 BigDecimal TNAC_B = new BigDecimal(0);
			 BigDecimal TEN_B = new BigDecimal(0);
			 BigDecimal TLI_B = new BigDecimal(0);
			 
			String SQL1 = "Select MAX(XX_VLO_LEADTIMES_ID) "+
		    	  "FROM XX_VLO_LEADTIMES A,  C_ORDER C "+
		          "WHERE C.isSOTrx='N' AND C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
		          "AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID "+
		          "AND C.C_BPARTNER_ID = A.C_BPARTNER_ID "+
		          "AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
		          "AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
		          "AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")";
				
			 SQL = ("SELECT XX_TRANSITTIMETT, XX_TIMEREGISTCELLATIONTRC, XX_NATIONALIZATIONTIMETNAC, XX_NACARRIVALTIMETEN, XX_INTERNACARRIVALTIMETEI, XX_IMPORTSLOGISTICSTIMETLI, C.XX_DISPATCHDATE "+
				   "FROM XX_VLO_LEADTIMES A, C_ORDER C "+
				   "WHERE C.isSOTrx='N' AND C.DOCUMENTNO = '"+order.getDocumentNo()+"'"+
				   "AND A.XX_VLO_LEADTIMES_ID ="+
				   "("+SQL1+") "+
			       "AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
			       "AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")"
				   );

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
			} finally{
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
			
			//XX_ShippingDate			
			if(getXX_SHIPPINGREALDATE()==null){
				order.setXX_BlockAgeDelEstDate(false);
				System.out.println("Se desbloquea");
			}
			
			if(isXX_DefinitiveShippingDate() && !isXX_DefinitiveVzlaDate()){
				
				order.setXX_BlockAgeDelEstDate(true);
				date.setTimeInMillis(getXX_SHIPPINGREALDATE().getTime());
	    		
				//Hago las sumas para las fechas restantes
				
				//XX_VZLAARRIVALESTEMEDDATE
		        date.add(Calendar.DATE, TT_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_EstArrivalDateToVzla(dateSUM);
		        
		        //XX_CustDutEstPayDate
		        date.add(Calendar.DATE, TRC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstPayDatePO(dateSUM);
		        
		        //XX_CustDutEstShipDate
		        date.add(Calendar.DATE, TNAC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstShipDatePO(dateSUM);
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);
	        }
			
	        if(getXX_SHIPPINGREALDATE()!=null && !isXX_DefinitiveShippingDate()){
	        	
	        	order.setXX_BlockAgeDelEstDate(true);
	        	
	        	//XX_SHIPPREALESTEEMEDDATE
		        order.setXX_CustDisEstiDatePO(getXX_SHIPPINGREALDATE());
		        date.setTimeInMillis(getXX_SHIPPINGREALDATE().getTime());
				
				//XX_VZLAARRIVALESTEMEDDATE
		        date.add(Calendar.DATE, TT_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_EstArrivalDateToVzla(dateSUM);
		        
		        //XX_CustDutEstPayDate
		        date.add(Calendar.DATE, TRC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstPayDatePO(dateSUM);
		        
		        //XX_CustDutEstShipDate
		        date.add(Calendar.DATE, TNAC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstShipDatePO(dateSUM);
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);	
		        
	        }
	        
	        /************************************/
	        //XX_VZLAARRIVALREALDATE
	        if(isXX_DefinitiveVzlaDate() && !isXX_DefinitiveDA()){
				
	        	date.setTimeInMillis(getXX_VZLAARRIVALREALDATE().getTime());
	    		
				//Hago las sumas para las fechas restantes
		        
		        //XX_CustDutEstPayDate
		        date.add(Calendar.DATE, TRC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstPayDatePO(dateSUM);
		        
		        //XX_CustDutEstShipDate
		        date.add(Calendar.DATE, TNAC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstShipDatePO(dateSUM);
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);
	        }
			
	        //Si no hay fechas definitivas
	        if(getXX_VZLAARRIVALREALDATE()!=null && !isXX_DefinitiveVzlaDate() && !isXX_DefinitiveShippingDate()){
	        		
		        order.setXX_EstArrivalDateToVzla(getXX_VZLAARRIVALREALDATE());
		        date.setTimeInMillis(getXX_VZLAARRIVALREALDATE().getTime());
		        
		        //XX_CustDutEstPayDate
		        date.add(Calendar.DATE, TRC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstPayDatePO(dateSUM);
		        
		        //XX_CustDutEstShipDate
		        date.add(Calendar.DATE, TNAC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstShipDatePO(dateSUM);
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);	
	        }

	        /***************************/
	        //XX_CUSTOMRIGHTSCANCELDATE
	        
	        if(isXX_DefinitiveDA() && !isXX_DefinitiveDispatch()){
				
	        	date.setTimeInMillis(getXX_CUSTOMRIGHTSCANCELDATE().getTime());
	    		
				//Hago las sumas para las fechas restantes
		        
		        //XX_CustDutEstShipDate
		        date.add(Calendar.DATE, TNAC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstShipDatePO(dateSUM);
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);
	        }
			
	        if(getXX_CUSTOMRIGHTSCANCELDATE()!=null && !isXX_DefinitiveDA() && !isXX_DefinitiveShippingDate()){
	        		
		        order.setXX_CustDutEstPayDatePO(getXX_CUSTOMRIGHTSCANCELDATE());
		        date.setTimeInMillis(getXX_CUSTOMRIGHTSCANCELDATE().getTime());
		       
		        //XX_CustDutEstShipDate
		        date.add(Calendar.DATE, TNAC_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CustDutEstShipDatePO(dateSUM);
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);	
	        }
	        
			/**********************/
	        //XX_REALDISPATCHDATE()
	        
	        if(isXX_DefinitiveDispatch() && !isXX_DefinitiveCD()){
				
	        	date.setTimeInMillis(getXX_CUSTOMRIGHTSCANCELDATE().getTime());
	    		
				//Hago las sumas para las fechas restantes
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);
	        }
			
	        if(getXX_REALDISPATCHDATE()!=null && !isXX_DefinitiveDispatch() && !isXX_DefinitiveShippingDate()){
	        		
		        order.setXX_CustDutEstShipDatePO(getXX_REALDISPATCHDATE());
		        date.setTimeInMillis(getXX_REALDISPATCHDATE().getTime());
		        
		        //XX_CDARRIVALESTEEMEDDATE
		        date.add(Calendar.DATE, TEN_B.intValue());
		        dateSUM = new Timestamp(date.getTimeInMillis());
		        order.setXX_CDArrivalEstimatedDatePO(dateSUM);	
	        }      
	        
	        //XX_CDARRIVALREALDATE()
	        if(getXX_CDARRIVALREALDATE()!=null && !isXX_DefinitiveCD() && !isXX_DefinitiveShippingDate()){
	        	order.setXX_CDArrivalEstimatedDatePO(getXX_CDARRIVALREALDATE());
	        }
	        
	       	        
				//Guardo la orden de compra
			order.save();
			get_Trx().commit();
		}
		/** Generacion de Tarea Critica Calculo de Factores a Partir de que se recibe la preliquidacion de Gastos
		 * @uthor Rebecca Principal 
		 * 
		 * */
		
		if((getXX_DAPRELIQUIDARECEPREALDATE()!=null)&&(isXX_Alert()==false))		
		{
			//Generacion de Alerta de Tarea Critica para Calcular Factores  Rebecca Principal 17/05/2010.			
			Env.getCtx().setContext("#XX_TypeAlertCF","CF");
			Env.getCtx().setContext("#XX_BoardingGuideCT",get_ID());
			
			MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"), get_ID()); 
			mpi.save();
			
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
			pi.setClassName(""); 
			pi.setTitle(""); 
			
			ProcessCtl pc = new ProcessCtl(null ,pi,null); 
			pc.start();
		}
		 
		return true;
	}
	
	
	
	
	/**
	 * Daniel Pellegrino --> Funcion  
	 * 
	 **/	
	private boolean EnviarCorreosCambioStatusCoor(MOrder order,String Attachment, String status , String date) {
					
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Cuentas por Pagar'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
						 "Vendor: "+vendor.getName()+" \n"+
						 "Amount: "+order.getTotalLines()+" \n"+
						 "Currency: "+currency.getDescription()+"\n"+
						 "Payment Term: "+paymentTerm.getName()+"\n"+
						 "Country: "+country.getDescription()+"\n"+
						 date;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next())
				{	
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
					
				}
				
			}

		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally
		{
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}//Enviar
	
	private boolean EnviarCorreosCambioStatusJefeImport(MOrder order,String Attachment, String status, String date ) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Importaciones'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
		 				"Vendor: "+vendor.getName()+" \n"+
		 				"Amount: "+order.getTotalLines()+" \n"+
		 				"Currency: "+currency.getDescription()+"\n"+
		 				"Payment Term: "+paymentTerm.getName()+"\n"+
		 				"Country: "+country.getDescription()+"\n"+
		 				date;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();

				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;

				}

			}

		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally
		{
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}//Enviar
	
	
	private boolean EnviarCorreosCambioStatusCoorImport(MOrder order,String Attachment, String status, String date ) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Coordinador de Importación'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
		 				"Vendor: "+vendor.getName()+" \n"+
		 				"Amount: "+order.getTotalLines()+" \n"+
		 				"Currency: "+currency.getDescription()+"\n"+
		 				"Payment Term: "+paymentTerm.getName()+"\n"+
		 				"Country: "+country.getDescription()+"\n"+
		 				date;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();

				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
				}
			}
		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally
		{
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}//Enviar

    
	private boolean EnviarCorreosCambioStatusJefeCat(MOrder order,String Attachment, String status , String date) {
		
		String sql = "SELECT B.C_BPARTNER_ID AS COOR " +
				"FROM C_BPARTNER B " +
				"WHERE ISACTIVE = 'Y' AND IsEmployee = 'Y' " +
				"AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE UPPER(NAME) = UPPER('Jefe de Categoria'))";
		
		MCurrency currency = new MCurrency( Env.getCtx(), order.getC_Currency_ID(), null);
		MBPartner vendor = new MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		MPaymentTerm paymentTerm = new MPaymentTerm( Env.getCtx(), order.getC_PaymentTerm_ID(), null);
		MCountry country = new MCountry( Env.getCtx(), order.getC_Country_ID(), null);
		
		String Mensaje = order.getDocumentNo()+" "+status+" \n\n" +
						 "Vendor: "+vendor.getName()+" \n"+
						 "Amount: "+order.getTotalLines()+" \n"+
						 "Currency: "+currency.getDescription()+"\n"+
						 "Payment Term: "+paymentTerm.getName()+"\n"+
						 "Country: "+country.getDescription()+"\n"+
						 date;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				Integer coordinador = rs.getInt("COOR");
				
				String sql2 = "SELECT u.AD_User_ID "
					   + "FROM AD_User u "
					   + "where IsActive = 'Y' and "
					   + "C_BPARTNER_ID = " + coordinador;
				
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
			
			
				while(rs2.next()){
					
					Integer UserAuxID = rs2.getInt("AD_User_ID");
					Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_STACHANGE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
					
				}
				DB.closeResultSet(rs2);
				DB.closeStatement(pstmt2);

			}

		}
		catch (Exception e){
			//log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return false;
		} finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;
	}//Enviar
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		
		Calendar cal = Calendar.getInstance();
	    
	    int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int nano = cal.get(Calendar.SECOND);
	    
	    Timestamp XXExitDate = new Timestamp(year-1900,month,day,hour,min,sec,nano);
		
		
		Timestamp ShippingRealDate = getXX_SHIPPINGREALDATE();
		Timestamp VzlaRealDate = getXX_VZLAARRIVALREALDATE();
		Timestamp CustomCancelDate = getXX_CUSTOMRIGHTSCANCELDATE();
		Timestamp RealDispachDate = getXX_REALDISPATCHDATE();
		Timestamp CDArrivalDate = getXX_CDARRIVALREALDATE();
		
		//  Validar que la fecha shipping date sea mayor o igual que 
		//  el definitivo de cargo agent en la O/C
		
		String SQL = "Select C_Order_ID " +
		 "from C_Order " +
		 "where isSOTrx='N' AND XX_VLO_BOARDINGGUIDE_ID = "+ get_ID();

		Vector<Integer> orders = new Vector<Integer>();
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try{
		
		pstmt = DB.prepareStatement(SQL, null); 
		rs = pstmt.executeQuery();
		 
		while(rs.next())
		{
			orders.add(rs.getInt("C_Order_ID"));
		}
		
	    
	    // valido que si no hay O/C asociada a la guia no pueda meter una fecha de despacho real 
	    
	    if(orders.size()== 0 && getXX_SHIPPINGREALDATE() != null)
	    {
	    	log.saveError("Error", Msg.getMsg(getCtx(), "No puede colocar una Fecha Real de Despacho por que la Guia de Embarque no tiene O/C Asociada"));
	    	return false;
	    }

	    
		}catch (Exception e) {
		//log.log(Level.SEVERE, SQL);
		return false;
		} finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		
		
		for(int i=0; i<orders.size() ;i++){
			
			MOrder order = new MOrder(getCtx(), orders.get(i), get_Trx());
		
			if(ShippingRealDate != null & order.getXX_CARAGENTDELIVREALDATE() != null)
			{
				if (order.getXX_CARAGENTDELIVREALDATE().compareTo(ShippingRealDate) == 1)
				{
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_BGCargShip",new String[] {order.getDocumentNo()}));
					return false;
				}
			}
		}
		
		//******************
		
		if(ShippingRealDate != null & VzlaRealDate != null)
		{
			if (ShippingRealDate.compareTo(VzlaRealDate) == 1)
			{
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), "XX_BGShipVzla"));
				return false;
			}
		}
		if(VzlaRealDate != null & CustomCancelDate != null)
		{
			if(VzlaRealDate.compareTo(CustomCancelDate) == 1)
			{
				//System.out.println("La fecha de llegada a vzla es mayor a la de cancelacion de derechos aduanales");
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), "XX_BGVzlaCustom"));
				return false;
			}
		}
		if(CustomCancelDate != null & RealDispachDate != null)
		{
			if(CustomCancelDate.compareTo(RealDispachDate) == 1)
			{
				//System.out.println("La de cancelacion de derechos aduanales es mayor a la fecha real de despacho");
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), "XX_BGCustomDispach"));
				return false;
			}
		}
		if(RealDispachDate != null & CDArrivalDate != null)
		{
			if(RealDispachDate.compareTo(CDArrivalDate) == 1)
			{
				//System.out.println("La fecha real de despacho es mayor a la fecha de llegada al centro de distrubucion");
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), "XX_BGDispachCDArrival"));
				return false;
			}
		}
		
		
		if(getXX_SHIPPINGREALDATE() != null && is_ValueChanged("XX_DefinitiveShippingDate"))
		{
			String Attachment = null;
			String status = "is now in 'En Tránsito Internacional' Status";
			Integer orderid = 0;
			String yearAux = null;
	    	String monthAux = null;
	    	String dayAux = null;
	    	String date = null;
	    	
	    	String sp =  getXX_SHIPPINGREALDATE().toString();
	    		    	
	    	yearAux = sp.substring(0,4);
	    	monthAux = sp.substring(5,7);
	    	dayAux = sp.substring(8,10);
	    	date = "Shipping Real Date: "+dayAux+"/"+monthAux+"/"+yearAux;
	    	
	    	String SQL1 = ("SELECT A.C_ORDER_ID AS ORDERID " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
					"WHERE A.isSOTrx='N' AND A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
					"AND (A.XX_ORDERSTATUS = 'EAC' OR A.XX_ORDERSTATUS = 'EP') " +
					"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
					"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+getXX_VLO_BoardingGuide_ID()+"' ");
			
    		PreparedStatement pstmt1 = null; 
		    ResultSet rs1 = null;
	    	try
	    	{
	    		pstmt1 = DB.prepareStatement(SQL1, null); 
			    rs1 = pstmt1.executeQuery();
			    
			    while(rs1.next())
			    {
			    	orderid = rs1.getInt("ORDERID");
			    	
			    	MOrder order = new MOrder(Env.getCtx(),orderid,get_Trx());
			    	
			    	order.setXX_OrderStatus("ETI");
			    	order.setXX_InsertedStatusDate(XXExitDate);
				    order.save();
				    
				    EnviarCorreosCambioStatusCoor(order, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeImport(order, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeCat(order, Attachment, status, date);
				    EnviarCorreosCambioStatusCoorImport(order, Attachment, status, date);
				    
			    }
	    	}
	    	catch (Exception e) {
	    		log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			} finally
			{
				DB.closeResultSet(rs1);
				DB.closeStatement(pstmt1);
			}
			
		}// end if
		//else
			//setXX_DefinitiveShippingDate(false);
		
		if(getXX_VZLAARRIVALREALDATE() != null && is_ValueChanged("XX_DefinitiveVzlaDate"))
		{
			String Attachment = null;
			String status = "is now in 'Llegada a Venezuela' Status";
			String country = getXX_VZLAARRIVALREALDATE().toString();
			Integer orderid = 0;
			
			String yearAux = null;
	    	String monthAux = null;
	    	String dayAux = null;
	    	String date = null;
	    	
	    	yearAux = country.substring(0,4);
	    	monthAux = country.substring(5,7);
	    	dayAux = country.substring(8,10);
	    	date = "VZLA Arrival Real Date: "+dayAux+"/"+monthAux+"/"+yearAux;
	    	
	    	String SQL2 = ("SELECT A.C_ORDER_ID AS ORDERID " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
					"WHERE A.isSOTrx='N' AND A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
					"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
					"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+getXX_VLO_BoardingGuide_ID()+"' ");
			
    		PreparedStatement pstmt2 = null; 
		    ResultSet rs2 = null;
	    	try
	    	{
	    		pstmt2 = DB.prepareStatement(SQL2, null); 
			    rs2 = pstmt2.executeQuery();
			    
			    while(rs2.next())
			    {
			    	orderid = rs2.getInt("ORDERID");
			    	
			    	MOrder orden = new MOrder(Env.getCtx(),orderid,get_Trx());
			    	
			    	orden.setXX_OrderStatus("LVE");
			    	orden.setXX_InsertedStatusDate(XXExitDate);
				    orden.save();
				    
				    EnviarCorreosCambioStatusCoor(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
			    	
			    }
			    
	    	}
	    	catch (Exception e) {
	    		log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			} finally
			{
				DB.closeResultSet(rs2);
				DB.closeStatement(pstmt2);
			}
		}
		
		if(getXX_CUSTOMRIGHTSCANCELDATE() != null && is_ValueChanged("XX_DefinitiveDA"))
		{
			String Attachment = null;
			String status = "is now in 'En Proceso de Nacionalización' Status";
			String sp = getXX_CUSTOMRIGHTSCANCELDATE().toString();
			String yearAux = null;
	    	String monthAux = null;
	    	String dayAux = null;
	    	String date = null;
	    	Integer orderid = 0;
	    	
	    	yearAux = sp.substring(0,4);
	    	monthAux = sp.substring(5,7);
	    	dayAux = sp.substring(8,10);
	    	date = "Custom Rights Cancel Date: "+dayAux+"/"+monthAux+"/"+yearAux;
	    	
	    	String SQL3 = ("SELECT A.C_ORDER_ID AS ORDERID " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
					"WHERE A.isSOTrx='N' AND A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
					"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
					"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+getXX_VLO_BoardingGuide_ID()+"' ");
			
    		PreparedStatement pstmt3 = null; 
		    ResultSet rs3 = null;
	    	try
	    	{
	    		pstmt3 = DB.prepareStatement(SQL3, null); 
			    rs3 = pstmt3.executeQuery();
			    
			    while(rs3.next())
			    {
			    	orderid = rs3.getInt("ORDERID");
			    	
			    	MOrder orden = new MOrder(Env.getCtx(),orderid,get_Trx());
			    	
			    	orden.setXX_OrderStatus("EPN");
			    	orden.setXX_InsertedStatusDate(XXExitDate);
					orden.save();
					
					EnviarCorreosCambioStatusCoor(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
				    
			    }
	    	}
	    	catch (Exception e) {
	    		log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			} finally
			{
				DB.closeResultSet(rs3);
				DB.closeStatement(pstmt3);
			} 			
		}
		
		if(getXX_REALDISPATCHDATE() != null && is_ValueChanged("XX_DefinitiveDispatch"))
		{
			String Attachment = null;
			String status = "is now in 'En Tránsito Nacional' Status";
			Integer orderid = 0;
			String sp = getXX_REALDISPATCHDATE().toString();
			
			String yearAux = null;
	    	String monthAux = null;
	    	String dayAux = null;
	    	String date = null;
	    	
	    	yearAux = sp.substring(0,4);
	    	monthAux = sp.substring(5,7);
	    	dayAux = sp.substring(8,10);
	    	date = "Customs Dispatch Real Date: "+dayAux+"/"+monthAux+"/"+yearAux;
	    	
	    	String SQL4 = ("SELECT A.C_ORDER_ID AS ORDERID " +
					"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
					"WHERE A.isSOTrx='N' AND A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
					"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
					"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+getXX_VLO_BoardingGuide_ID()+"' ");

    		PreparedStatement pstmt4 = null; 
			ResultSet rs4 = null;
	    	try
	    	{
	    		pstmt4 = DB.prepareStatement(SQL4, null); 
				rs4 = pstmt4.executeQuery();
				
				while(rs4.next())
				{
					orderid = rs4.getInt("ORDERID");
			    	
					MOrder orden = new MOrder(Env.getCtx(),orderid,get_Trx());
					
					orden.setXX_OrderStatus("ETN");
					orden.setXX_InsertedStatusDate(XXExitDate);
				    orden.save();
				    
				    EnviarCorreosCambioStatusCoor(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
				    EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
				    
				}
	    	}
	    	catch (Exception e) {
	    		log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			} finally
			{
				DB.closeResultSet(rs4);
				DB.closeStatement(pstmt4);
			}
	    	
		}
		
		if(getXX_CDARRIVALREALDATE() != null && is_ValueChanged("XX_DefinitiveCD"))
		{
			String Attachment = null;
			String status = "is now in 'Llegada a CD' Status";
			Integer orderid = 0;
			String sp = getXX_CDARRIVALREALDATE().toString();
			
			String yearAux = null;
	    	String monthAux = null;
	    	String dayAux = null;
	    	String date = null;
	    	
	    	yearAux = sp.substring(0,4);
	    	monthAux = sp.substring(5,7);
	    	dayAux = sp.substring(8,10);
	    	date = "CD Arrival Real Date: "+dayAux+"/"+monthAux+"/"+yearAux;
	    	
	    	String SQL2 = ("Select TO_CHAR (TO_TIMESTAMP ('"+sp+"','YYYY-MM-DD HH24:MI:SS.FF1'), 'MM-YYYY') AS FECHA from DUAL");
	    	
    		PreparedStatement pstmt2 = null; 
		    ResultSet rs2 = null;
			 PreparedStatement pstmt5 = null; 
			 ResultSet rs5 = null;
	    	try
	    	{
	    		pstmt2 = DB.prepareStatement(SQL2, null); 
			    rs2 = pstmt2.executeQuery();
			    
			    if(rs2.next())
			     {
			    	String SQL3 = ("UPDATE XX_VLO_BOARDINGGUIDE SET XX_MonthArrival = '"+rs2.getString("FECHA")+"' WHERE XX_VLO_BOARDINGGUIDE_ID = '"+getXX_VLO_BoardingGuide_ID()+"'");
			    	DB.executeUpdate(get_Trx(), SQL3 );
			     }
			     rs2.close();
				 pstmt2.close();
				 
				 String SQL5 = ("SELECT A.C_ORDER_ID AS ORDERID " +
							"FROM C_ORDER A, XX_VLO_BOARDINGGUIDE B " +
							"WHERE A.isSOTrx='N' AND A.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_ID " +
							"AND A.XX_CARAGENTDELIVREALDATE IS NOT NULL " +
							"AND B.XX_VLO_BOARDINGGUIDE_ID = '"+getXX_VLO_BoardingGuide_ID()+"' ");
						
				 pstmt5 = DB.prepareStatement(SQL5, null); 
				 rs5 = pstmt5.executeQuery();
				 
				 while(rs5.next())
				 {
					 orderid = rs5.getInt("ORDERID");
				    	
					 MOrder orden = new MOrder(Env.getCtx(),orderid,get_Trx());
					 
					 orden.setXX_OrderStatus("LCD");
					 orden.setXX_InsertedStatusDate(XXExitDate);
					 orden.save();
					    
					 EnviarCorreosCambioStatusCoor(orden, Attachment, status, date);
					 EnviarCorreosCambioStatusJefeImport(orden, Attachment, status, date);
					 EnviarCorreosCambioStatusJefeCat(orden, Attachment, status, date);
					 EnviarCorreosCambioStatusCoorImport(orden, Attachment, status, date);
					 
				 }
					 
	    	}
	    	catch (Exception e) {
	    		log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			} finally
			{
				DB.closeResultSet(rs2);
				DB.closeStatement(pstmt2);
				DB.closeResultSet(rs5);
				DB.closeStatement(pstmt5);
			}
	    	
		}

		return true;
		
	}

	public void setXX_DefinitiveAgent(String XX_DefinitiveAgent) {
		set_Value("XX_DefinitiveAgent", XX_DefinitiveAgent);
		
	}

	public void setXX_DefinitiveInsurance(String XX_DefinitiveInsurance) {
		set_Value("XX_DefinitiveInsurance", XX_DefinitiveInsurance);
		
	}

	public void setXX_DefinitiveInterFret(String XX_DefinitiveInterFret) {
		set_Value("XX_DefinitiveInterFret", XX_DefinitiveInterFret);
		
	}

	public void setXX_DefinitiveNacInv(String XX_DefinitiveNacInv) {
		set_Value("XX_DefinitiveNacInv", XX_DefinitiveNacInv);
		
	}

	public void setXX_DefinitiveNacTreas(String XX_DefinitiveNacTreas) {
		set_Value("XX_DefinitiveNacTreas", XX_DefinitiveNacTreas);
		
	}

	public void setXX_DefinitiveSeniat(String XX_DefinitiveSeniat) {
		set_Value("XX_DefinitiveSeniat", XX_DefinitiveSeniat);
		
	}	
}
