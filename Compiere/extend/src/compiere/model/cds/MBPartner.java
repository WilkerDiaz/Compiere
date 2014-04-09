package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.model.X_I_BPartner;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MBPartner extends org.compiere.model.MBPartner{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartner.class);

	public MBPartner(Ctx ctx, int C_BPartner_ID, Trx trx) {
		super(ctx, C_BPartner_ID, trx);
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MBPartner(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}//MBPartner
	
	
    public MBPartner(X_I_BPartner impBP) {
    	super(impBP);
	}

    
    //dpellegrino
    
    /**
	 * Set Number Tap
	 @param XX_NumberTap Numero de ficha CR*/

		public void setXX_NumberTap (String XX_NumberTap)
	    {
	        set_Value ("XX_NumberTap", XX_NumberTap);
	        
	    }
		
	    public String getXX_NumberTap() 
	    {
	        return (String)get_Value("XX_NumberTap");
	        
	    }
	////////////////////////////////////////////////////////////////////
	    
		/*public final void setXX_IsEmployee (boolean XX_IsEmployee)
		{
			set_Value("XX_IsEmployee", Boolean.valueOf(XX_IsEmployee));
		}	
		
		  public Boolean getXX_IsEmployee() 
		    {
		        return (Boolean)get_Value("XX_IsEmployee");
		    }*/
		
	/////////////////////////////////////////////////////////////////////	
		public void setXX_Codcargo (String XX_Codcargo)
	    {
	        set_Value ("XX_Codcargo", XX_Codcargo);
	        
	    }
		
		 public String getXX_Codcargo() 
		    {
		        return (String)get_Value("XX_Codcargo");
		        
		    }
		 
		 
/////////////////////////////////////////////////////////////////////
		 /** Set C_Job_ID.
	    @param  */
	    public void setC_Job_ID (int C_Job_ID)
	    {
	        if (C_Job_ID <= 0) set_Value ("C_Job_ID", null);
	        else
	        set_Value ("C_Job_ID", Integer.valueOf(C_Job_ID));
	        
	    }
	    
	    public int getC_Job_ID() 
	    {
	        return get_ValueAsInt("C_Job_ID");      
	    }
	    
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
		 /** Set Warehouse.
	    @param M_Warehouse_ID Storage Warehouse and Service Point */
	    public void setM_Warehouse_ID (int M_Warehouse_ID)
	    {
	        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
	        else
	        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
	        
	    }

	/////////////////////////////////////////////////////////////////////	
		/*public final void setXX_IsCostumer (boolean XX_IsCostumer)
		{
			set_Value("XX_IsCostumer", Boolean.valueOf(XX_IsCostumer));
		}*/

	////////////////////////////////////////////////////////////////////	
		public void setXX_Registrado (String XX_Registrado)
	    {
	        set_Value ("XX_Registrado", XX_Registrado);
	        
	    }

	////////////////////////////////////////////////////////////////////	
		public void setXX_EstadoCivil (String XX_EstadoCivil)
	    {
	        set_Value ("XX_EstadoCivil", XX_EstadoCivil);
	        
	    }

	////////////////////////////////////////////////////////////////////
	    public void setXX_FechaNac (Timestamp XX_FechaNac)
	    {
	        set_Value ("XX_FechaNac", XX_FechaNac);
	        
	    }
	    
	///////////////////////////////////////////////////////////////////    
		public void setXX_Genero (String XX_Genero)
	    {
	        set_Value ("XX_Genero", XX_Genero);
	        
	    }

	//////////////////////////////////////////////////////////////////
		public final void setXX_Iscontac (boolean XX_Iscontac)
		{
			set_Value("XX_Iscontac", Boolean.valueOf(XX_Iscontac));
		}
		

	///////////////////////////////////////////////////////////////////
		public void setXX_Email (String XX_Email)
	    {
	        set_Value ("XX_Email", XX_Email);
	        
	    }
		
	///////////////////////////////////////////////////////////////////

		public void setXX_CollaboratorClass (String XX_CollaboratorClass)
	    {
	        set_Value ("XX_CollaboratorClass", XX_CollaboratorClass);
	        
	    }
		
	/** Purchase of Supplies and Services
	 * Maria Vintimilla **/
		
	/** Set Assets Provider.
	@param XX_Assets Assets Provider on C_BPartner */
	public void setXX_Assets (String XX_Assets){
		set_Value ("XX_Assets", XX_Assets);   
	}
	    
	/** Get Assets Provider.
	@return XX_Assets Assets Provider on C_BPartner */
	public Boolean getXX_Assets(){
		return (Boolean)get_Value("XX_Assets");
	}

	/** Set Services Provider.
	@param XX_Services Services Provider on C_BPartner  */
	public void setXX_Services (String XX_Services){
		set_Value ("XX_Services", XX_Services);   
	}
	    
	/** Get Services Provider.
	@return XX_Services Services Provider on C_BPartner */
	public Boolean getXX_Services(){
		return (Boolean)get_Value("XX_Services");
	}

	/** Set Merchandise Provider.
	@param XX_Merchandise Merchandise Provider on C_BPartner  */
	public void setXX_Merchandise(String XX_Merchandise){
		set_Value ("XX_Merchandise", XX_Merchandise);   
	}
	    
	/** Get Merchandise Provider.
	@return XX_Merchandise Merchandise Provider on C_BPartner */
	public Boolean getXX_Merchandise(){
		return (Boolean)get_Value("XX_Merchandise");
	}
	
    /** Set Cause of Deactivation Assets Provider.
    @param XX_AssetsReason_ID Cause of Deactivation for Assets on C_BPartner  */
    public void setXX_AssetsReason_ID (Integer XX_AssetsReason_ID){
        set_Value ("XX_AssetsReason_ID", XX_AssetsReason_ID);  
    }
    
    /** Get Cause of Deactivation Assets Provider.
    @return XX_AssetsReason_ID Cause of Deactivation for Assets on C_BPartner */
    public String getXX_AssetsReason_ID(){
        return (String)get_Value("XX_AssetsReason_ID");
    }
    
    /** Set Cause of Deactivation Services Provider.
    @param XX_ServicesReason_ID Cause of Deactivation for Servicess on C_BPartner */
    public void setXX_ServicesReason_ID (Integer XX_ServicesReason_ID){
        set_Value ("XX_ServicesReason_ID", XX_ServicesReason_ID);
    }
    
    /** Get Cause of Deactivation Services Provider.
    @return XX_ServicesReason_ID Cause of Deactivation for Servicess on C_BPartner */
    public String getXX_ServicesReason_ID(){
        return (String)get_Value("XX_ServicesReason_ID");
    }
    
    /** Set Cause of Deactivation Merchandise Provider.
    @param XX_MerchandiseReason_ID Cause of Deactivation for Merchandise on C_BPartner */
    public void setXX_MerchandiseReason_ID (Integer XX_MerchandiseReason_ID){
        set_Value ("XX_MerchandiseReason_ID", XX_MerchandiseReason_ID);  
    }
    
    /** Get Cause of Deactivation Merchandise Provider.
    @return XX_MerchandiseReason_ID Cause of Deactivation for Merchandise on C_BPartner */
    public String getXX_MerchandiseReason_ID(){
        return (String)get_Value("XX_MerchandiseReason_ID");
    }
    
    /** Set Enter Other Reason for Deactivation of a Vendor Category.
    @param XX_EnterOtherReason Other Cause of Deactivation for a Vendor Category on C_BPartner */
    public void setXX_EnterOtherReason(String valor){
    	set_Value ("XX_EnterOtherReason", valor);    
    }
        
    /** Get Enter Other Reason for Deactivation of a Vendor Category.
    @return XX_EnterOtherReason Other Cause of Deactivation for a Vendor Category on C_BPartner */
    public String getXX_EnterOtherReason(){
    	return (String)get_Value("EnterOtherReason");
    }

    /** Set Other Reason for Deactivation of a Vendor Category.
    @param XX_OtherReason Other Cause of Deactivation for a Vendor Category on C_BPartner */
    public void setXX_OtherReason(String razon)
    {
        set_Value ("XX_OtherReason", razon);   
    }
    
    /** Get Other Reason for Deactivation of a Vendor Category.
    @return XX_OtherReason Other Cause of Deactivation for a Vendor Category on C_BPartner */
    public String getXX_OtherReason(){
    	return get_ValueAsString("XX_OtherReason");
    }
    
	/** Set RIF Expiration Date.
	@param XX_RIFExpirationDate RIF Expiration Date on C_BPartner */
    public void setXX_RIFExpirationDate (Timestamp XX_RIFExpirationDate){
        set_Value ("XX_RIFExpirationDate", XX_RIFExpirationDate); 
    }
	    
	/** Get RIF Expiration Date.
	@return XX_RIFExpirationDate RIF Expiration Date on C_BPartner */
    public Timestamp getXX_RIFExpirationDate(){
        return (Timestamp)get_Value("XX_RIFExpirationDate");
    }
    
    /** Set Percentage Retention ISLR for Assets Provider.
    @param XX_PercentageRetentionISLRA Percentage Retention ISLR for Assets Provider on C_BPartner */
    public void setXX_PercentageRetentionISLRA(Integer XX_PercentageRetentionISLRA){
        set_Value ("XX_PercentageRetentionISLRA", XX_PercentageRetentionISLRA);  
    }
    
    /** Get Percentage Retention ISLR for Assets Provider.
    @return XX_PercentageRetentionISLRA Percentage Retention ISLR for Assets Provider on C_BPartner */
    public String getXX_PercentageRetentionISLRA(){
        return (String)get_Value("XX_PercentageRetentionISLRA");
    }
    
    /** Set Percentage Retention ISLR for Services Provider.
    @param XX_PercentageRetentionISLRS Percentage Retention ISLR for Services Provider on C_BPartner */
    public void setXX_PercentageRetentionISLRS(Integer XX_PercentageRetentionISLRS){
        set_Value ("XX_PercentageRetentionISLRS", XX_PercentageRetentionISLRS);  
    }
    
    /** Get Percentage Retention ISLR for Services Provider.
    @return XX_PercentageRetentionISLRS Percentage Retention ISLR for Services Provider on C_BPartner */
    public String getXX_PercentageRetentionISLRS(){
        return (String)get_Value("XX_PercentageRetentionISLRS");
    }
    
    /** Set XX_CBPartner_ProductType.
    @param XX_CBPartner_ProductType */
    public void setXX_CBPartner_ProductType (String XX_CBPartner_ProductType) {
        set_Value ("XX_CBPartner_ProductType", XX_CBPartner_ProductType);
    }
    
    /** Get XX_CBPartner_ProductType.
    @return XX_CBPartner_ProductType */
    public String getXX_CBPartner_ProductType(){
        return (String)get_Value("XX_CBPartner_ProductType");
    }
    
   /** Fin Purchase of Supplies and Services **/
    
	/** Get Is Active.
    @return valor de is active */
    public String getisActive()
    {
    	return get_ValueAsString("IsActive");
    }
    
    /** Set XX_TypePerson.
    @param XX_TypePerson Type of Person on C_BPartner */
    public void setXX_TypePerson (String XX_TypePerson)
    {
        set_Value ("XX_TypePerson", XX_TypePerson);
        
    }
    
    /** Get XX_TypePerson.
    @return XX_TypePerson Type of Person on C_BPartner */
    public String getXX_TypePerson() 
    {
        return (String)get_Value("XX_TypePerson");
        
    }
    
    /** Set XX_CI_RIF.
    @param XX_CI_RIF Cédula o RIF on C_BPartner */
    public void setXX_CI_RIF (String XX_CI_RIF)
    {
        set_Value ("XX_CI_RIF", XX_CI_RIF);
        
    }
    
    /** Get XX_CI_RIF.
    @return XX_CI_RIF Cédula o RIF on C_BPartner */
    public String getXX_CI_RIF() 
    {
        return (String)get_Value("XX_CI_RIF");
        
    }
    
    /** Set XX_NIT.
    @param XX_NIT NIT on C_BPartner */
    public void setXX_NIT (String XX_NIT)
    {
        set_Value ("XX_NIT", XX_NIT);
        
    }
    
    /** Get XX_NIT.
    @return XX_NIT NIT on C_BPartner */
    public String getXX_NIT() 
    {
        return (String)get_Value("XX_NIT");
        
    }
    
    /** Set XX_RegistrationData.
    @param XX_RegistrationData Registration Data of C_BPartner */
    public void setXX_RegistrationData (String XX_RegistrationData)
    {
        set_Value ("XX_RegistrationData", XX_RegistrationData);
        
    }
    
    /** Get XX_RegistrationData.
    @return XX_RegistrationData Registration Data of C_BPartner */
    public String getXX_RegistrationData() 
    {
        return (String)get_Value("XX_RegistrationData");
    }
    
    /** Set XX_EntryDate.
    @param XX_EntryDate Entry Date */
    public void setXX_EntryDate (Timestamp XX_EntryDate)
    {
        set_Value ("XX_EntryDate", XX_EntryDate);
        
    }
    
    /** Get XX_EntryDate.
    @return XX_EntryDate Entry Date */
    public Timestamp getXX_EntryDate() 
    {
        return (Timestamp)get_Value("XX_EntryDate");
        
    }

    /** Set XX_ExitDate.
    @param XX_ExitDate Exit Date */
    public void setXX_ExitDate (Timestamp XX_ExitDate)
    {
        set_Value ("XX_ExitDate", XX_ExitDate);
        
    }
    
    /** Get XX_ExitDate.
    @return XX_ExitDate Exit Date */
    public Timestamp getXX_ExitDate() 
    {
        return (Timestamp)get_Value("XX_ExitDate");
        
    }
    
    /** Set XX_VendorEmail.
    @param XX_VendorEmail Vendor Email */
    public void setXX_VendorEmail (String XX_VendorEmail)
    {
        set_Value ("XX_VendorEmail", XX_VendorEmail);
        
    }
    
    /** Get XX_VendorEmail.
    @return XX_VendorEmail Vendor Email */
    public String getXX_VendorEmail() 
    {
        return (String)get_Value("XX_VendorEmail");
    }
    
    /** Set XX_Observation.
    @param XX_Observation */
    public void setXX_Observation (String XX_Observation)
    {
        set_Value ("XX_Observation", XX_Observation);
        
    }
    
    /** Get XX_Observation.
    @return XX_Observation*/
    public String getXX_Observation() 
    {
        return (String)get_Value("XX_Observation");
    }
    
    /** Set IsVendor.
    @param IsVendor IsVendor */
    public void setIsVendor (String IsVendor)
    {
        set_Value ("IsVendor", IsVendor);
        
    }
    
    /** Get IsVendor.
    @return IsVendor IsVendor */
    public Boolean getIsVendor() 
    {
        return (Boolean)get_Value("IsVendor");
    }
    
    /** Set XX_VendorType_ID.
    @param XX_VendorType_ID */
    public void setXX_VendorType_ID (Integer XX_VendorType_ID)
    {
        set_Value ("XX_VendorType_ID", XX_VendorType_ID);
        
    }
    
    /** Get XX_VendorType_ID.
    @return XX_VendorType_ID */
    public Integer getXX_VendorType_ID() 
    {
        return (Integer)get_Value("XX_VendorType_ID");
    }
    
    /** Set XX_ProductClass_ID.
    @param XX_ProductClass_ID */
    public void setXX_ProductClass_ID (Integer XX_ProductClass_ID)
    {
        set_Value ("XX_ProductClass_ID", XX_ProductClass_ID);
        
    }
    
    /** Get XX_ProductClass_ID.
    @return XX_ProductClass_ID */
    public Integer getXX_ProductClass_ID() 
    {
        return (Integer)get_Value("XX_ProductClass_ID");
    }
    
    /** Set XX_EconomicActivities_ID.
    @param XX_EconomicActivities_ID */
    public void setXX_EconomicActivities_ID (Integer XX_EconomicActivities_ID)
    {
        set_Value ("XX_EconomicActivities_ID", XX_EconomicActivities_ID);
        
    }
    
    /** Get XX_EconomicActivities_ID.
    @return XX_EconomicActivities_ID */
    public Integer getXX_EconomicActivities_ID() 
    {
        return (Integer)get_Value("XX_EconomicActivities_ID");
    }
    
    /** Set XX_TypeTaxPayer_ID.
    @param XX_TypeTaxPayer_ID */
    public void setXX_TypeTaxPayer_ID (Integer XX_TypeTaxPayer_ID)
    {
        set_Value ("XX_TypeTaxPayer_ID", XX_TypeTaxPayer_ID);
        
    }
    
    /** Get XX_TypeTaxPayer_ID.
    @return XX_TypeTaxPayer_ID */
    public Integer getXX_TypeTaxPayer_ID() 
    {
        return (Integer)get_Value("XX_TypeTaxPayer_ID");
    }
    
    /** Set XX_PercentajeRetention_ID.
    @param XX_PercentajeRetention_ID */
    public void setXX_PercentajeRetention_ID (Integer XX_PercentajeRetention_ID)
    {
        set_Value ("XX_PercentajeRetention_ID", XX_PercentajeRetention_ID);
        
    }
    
    /** Get XX_PercentajeRetention_ID.
    @return XX_PercentajeRetention_ID */
    public Integer getXX_PercentajeRetention_ID() 
    {
        return (Integer)get_Value("XX_PercentajeRetention_ID");
    }
    
    /** Set C_TaxCategory_ID.
    @param C_TaxCategory_ID */
    public void setC_TaxCategory_ID (Integer C_TaxCategory_ID)
    {
        set_Value ("C_TaxCategory_ID", C_TaxCategory_ID);
        
    }
    
    /** Get C_TaxCategory_ID.
    @return C_TaxCategory_ID */
    public Integer getC_TaxCategory_ID() 
    {
        return (Integer)get_Value("C_TaxCategory_ID");
    }    
    
    /** Set XX_TypeTax_ID.
    @param XX_TypeTax_ID */
    public void setXX_TypeTax_ID (Integer XX_TypeTax_ID)
    {
        set_Value ("XX_TypeTax_ID", XX_TypeTax_ID);
        
    }
    
    /** Get XX_TypeTax_ID.
    @return XX_TypeTax_ID */
    public Integer getXX_TypeTax_ID() 
    {
        return (Integer)get_Value("XX_TypeTax_ID");
    }
    
    /** Get XX_CancellationMotive_ID.
    @return XX_CancellationMotive_ID */
    public String getXX_CancellationMotive_ID() 
    {
        return (String)get_Value("XX_CancellationMotive_ID");
    }
    
    /** Set XX_CancellationMotive_ID.
    @param XX_CancellationMotive_ID */
    public void setXX_CancellationMotive_ID (Integer XX_CancellationMotive_ID)
    {
        set_Value ("XX_CancellationMotive_ID", XX_CancellationMotive_ID);
        
    }
    
    /** Set XX_CauseRetention_ID.
    @param XX_CauseRetention_ID */
    public void setXX_CauseRetention_ID (Integer XX_CauseRetention_ID)
    {
        set_Value ("XX_CauseRetention_ID", XX_CauseRetention_ID);
        
    }
    
    /** Get XX_CauseRetention_ID.
    @return XX_CauseRetention_ID */
    public Integer getXX_CauseRetention_ID() 
    {
        return (Integer)get_Value("XX_CauseRetention_ID");
    }
    
    /** Set XX_RetentionIndicator.
    @param XX_RetentionIndicator */
    public void setXX_RetentionIndicator (Boolean XX_RetentionIndicator)
    {
        set_Value ("XX_RetentionIndicator", XX_RetentionIndicator);
        
    }
    
    /** Get XX_RetentionIndicator.
    @return XX_RetentionIndicator */
    public String getXX_RetentionIndicator() 
    {
        return (String)get_Value("XX_RetentionIndicator");
    }
    
    
    /** Set XX_VendorClass.
    @param XX_VendorClass */
    public void setXX_VendorClass (String XX_VendorClass)
    {
        set_Value ("XX_VendorClass", XX_VendorClass);
        
    }
    
    /** Get XX_VendorClass.
    @return XX_VendorClass */
    public String getXX_VendorClass() 
    {
        return (String)get_Value("XX_VendorClass");
    }
    
    public String getXX_FigureVendor() 
    {
        return (String)get_Value("XX_FigureVendor");
    }

    /** Set XX_FigureVendor.
    @param XX_FigureVendor */
    public void setXX_FigureVendor (String XX_FigureVendor)
    {
        set_Value ("XX_FigureVendor", XX_FigureVendor);
        
    }
    
    /** Get XX_IsValid.
    @return XX_IsValid */
    public Boolean getXX_IsValid() 
    {
        return (Boolean)get_Value("XX_IsValid");
    }
    
    /** Set XX_IsValid.
    @param XX_IsValid */
    public void setXX_IsValid (Boolean XX_IsValid)
    {
        set_Value ("XX_IsValid", XX_IsValid);
    }
    
    /** Get XX_SynchronizationBank.
    @return XX_SynchronizationBank */
    public Boolean getXX_SynchronizationBank() 
    {
        return (Boolean)get_Value("XX_SynchronizationBank");
    }
    
    /** Set XX_SynchronizationBank.
    @param XX_SynchronizationBank */
    public void setXX_SynchronizationBank (Boolean XX_SynchronizationBank)
    {
        set_Value ("XX_SynchronizationBank", XX_SynchronizationBank);
    }
    
    /** Set XX_VendorCategory.
    @param XX_VendorCategory */
    public void setXX_VendorCategory (String XX_VendorCategory)
    {
        set_Value ("XX_VendorCategory", XX_VendorCategory);
        
    }
    
    /** Get XX_VendorCategory.
    @return XX_VendorCategory */
    public String getXX_VendorCategory()
    {
        return (String)get_Value("XX_VendorCategory");
    }
    
    /** Get XX_InvoicePayments.
    @return XX_InvoicePayments */
    public Boolean getXX_InvoicePayments() 
    {
        return (Boolean)get_Value("XX_InvoicePayments");
    }
    
    /** Set XX_InvoicePayments.
    @param XX_InvoicePayments */
    public void setXX_InvoicePayments (Boolean XX_InvoicePayments)
    {
        set_Value ("XX_InvoicePayments", XX_InvoicePayments);
    }
        
	/**
	 * 	Consulta la tabla C_BPARTNER, devuelve si esta activo
	 */
	private String ConsultaTablaC_BPartnerActive(){
		String aux = "VACIO";
		
		String sql = "SELECT ISACTIVE "
				   + "FROM C_BPARTNER "
				   + "WHERE C_BPARTNER_ID = " + getC_BPartner_ID();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			//System.out.println(sql);
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				aux = rs.getString("ISACTIVE");
				
			}
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "ERROR";
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return aux;
	}//ConsultaTablaC_BPartnerActive
	
	/*
	 * Chequea que sea numeros un String
	 */
	private boolean isNumeric(String cadena)
	{	try {		
			Integer.parseInt(cadena);	
			return true;
		} catch (NumberFormatException nfe){
			return false;	
		}
		
	}
	/** checkCategory
	 Verify if a Vendor have more than one vendor category associated
	 @param assets Assets provider
	 @param services Services provider
	 @param merchandise Merchandise provider
	 @return boolean 
	 */
	private boolean checkCategory(Boolean Assets, Boolean Service, Boolean Merchandise){
		Integer Cont = 0;
		if (Assets ||Service || Merchandise){ 
			Cont++;
		}
		if (Cont == 0){
			return true;
		}
		else {
			/** Set XX_CBPartner_ProductType according the active Vendor's categories **/
			if (Assets && Service && Merchandise){
				setXX_CBPartner_ProductType("W");
			}
			else if(Assets && !Service && Merchandise){
				setXX_CBPartner_ProductType("X");
			}
			else if(Assets && Service && !Merchandise){
				setXX_CBPartner_ProductType("Y");
			}
			else if(!Assets && Service && Merchandise){
				setXX_CBPartner_ProductType("Z");
			}
			else if(Assets && !Service && !Merchandise){
				setXX_CBPartner_ProductType("A");
			}
			else if(!Assets && !Service && Merchandise){
				setXX_CBPartner_ProductType("I");
			}
			else if(!Assets && Service && !Merchandise ){
				setXX_CBPartner_ProductType("S");
			}
			return false;
		}
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)	{	
		if(super.beforeSave(newRecord)){
			
			Boolean bool = true;
			int idPartner = getC_BPartner_ID();
			
			if (getXX_TypePerson().equals("G") || getXX_TypePerson().equals("J")){
				if (getXX_CI_RIF().length() != 9){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_NotExceedNine"));
					bool = false;
					return false;
				}
				if (! isNumeric(getXX_CI_RIF())){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_OnlyNumber",new String[] {"CI/RIF"}));
					bool = false;
					return false;
				}
			} 
			else if (getXX_TypePerson().equals("E") || getXX_TypePerson().equals("V")){
				if (! isNumeric(getXX_CI_RIF())){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_OnlyNumber",new String[] {"CI/RIF"}));
					bool = false;
					return false;
				}
			}// Fin
			
			//Proveedor Unico
			String sql_Unique = "SELECT * FROM c_bpartner WHERE XX_CI_RIF = '"+ getXX_CI_RIF()+"' and C_BPARTNER_ID <> " + get_ID();
			ResultSet rs = null;
			PreparedStatement prst = null;

			try {
				
				prst = DB.prepareStatement(sql_Unique, null);
				rs = prst.executeQuery();
				
				if(rs.next()){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_DuplicatedPartner"));
					return false;
				}
				
				rs.close();
				prst.close();
				
			} catch (SQLException e){
				log.log(Level.SEVERE, e.getMessage());
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}
			
			
			if (getIsVendor()){
				if(getXX_VendorEmail() == null){
					log.saveError("Error", Msg.getMsg(getCtx(), "XX_EmailValid"));
					bool = false;
					return false;	
				}
				/** Purchase of Supplies and Services 
				 * Maria Vintimilla 
				 * Business Partner must have at least one Vendor Category associated **/
				if (checkCategory(getXX_Assets(),getXX_Services(),getXX_Merchandise())){
					log.saveError("Error", Msg.getMsg(Env.getCtx(), "Debe tener al menos una categoría asociada"));
					bool = false;
					return false;	
				}// Fin checkCategory	
				
				// Si el proveedor es nacional debe introducir la fecha de
				// vencimiento del RIF
				// Maria Vintimilla
				if(getXX_VendorClass() != null){
					if(getXX_VendorClass().equals(X_Ref_XX_Ref_VendorClass.NACIONAL.getValue())){
						if(getXX_RIFExpirationDate()== null){
							log.saveError("Error", Msg.translate(Env.getCtx(), "XX_RIFExpirationDate"));
							bool = false;
							return false;
						}//rif
					}//nacional
				}//if vendorClass
				
//				deactivateCatService
//				Funcion 109: Al desactivar la categoria de servicio, se debe desmarcar el check de 
//				"Pago por factura"
//				@author Maria Vintimilla
//				@since 21/05/2011
				if(!getXX_Services()){
					setXX_InvoicePayments(false);
				}

			}// fin getIsVendor
			
			/****Jessica Mendoza****/
			if (getisActive().equals("Y")){
				if (getIsVendor()){
					if (getXX_SynchronizationBank() && bool){ //actualización del registro
						callInterfaceVendor("A", idPartner);
					}else if (!getXX_SynchronizationBank() && bool){ //nuevo registro
						callInterfaceVendor("A", idPartner);
					}
				}
			}else
				callInterfaceVendor("E", idPartner); //eliminar registro	
			/****Fincódigo - Jessica Mendoza****/
			
			if (!newRecord){
				String auxActivoViejo = ConsultaTablaC_BPartnerActive();
				String auxActivoNuevo = getisActive();
				
				if(auxActivoViejo.equals(auxActivoNuevo)){
					return true;
				}else if(auxActivoViejo.equals("Y") && auxActivoNuevo.equals("N")){	
					return true;
				}else if(auxActivoViejo.equals("N") && auxActivoNuevo.equals("Y")){
					String sql =  "UPDATE XX_VCN_VENDORRATING  set " +
								  "ISACTIVE = 'N' "+ 
								  "WHERE C_BPARTNER_ID = "+getC_BPartner_ID()+" and " +
								  "IsActive = 'Y'";			

						DB.executeUpdate(get_Trx(), sql.toString());							
					return true;
				}
				else
					return true;
				
			}// Fin !newRecord
			else{
				setName(getName().toUpperCase());	
				return true;
			}

		}else
			return false;
	}//beforeSave
	
	/**
	 * Se encarga de llamar al proceso de interfaz de actualización de proveedores
	 * @param operacion tipo de operación (A:Agregar, S:Sustituir, E:Eliminar)
	 * @param idPartner identificador del socio de negocio
	 */
	public void callInterfaceVendor(String operacion, int idPartner){
		try{
			MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_INTERFACEVENDOR_ID"), 0); 
			mpi.save();
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_INTERFACEVENDOR_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_INTERFACEVENDOR_ID")); 			
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add (new ProcessInfoParameter("Operacion", operacion, null, operacion, null));
			list.add (new ProcessInfoParameter("C_BPartner_ID", String.valueOf(idPartner), null, String.valueOf(idPartner), null));
			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
			ProcessCtl pc = new ProcessCtl(null ,pi,null); 
			pc.start();
		}catch(Exception e){
			log.log(Level.SEVERE,e.getMessage());
		}
	}
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success){	
		boolean save = super.afterSave(newRecord, success);

		// Funcion 109 Revision de socios de negocio
		// Si se cambia el tipo de persona y el proveedor tiene categoria de Servicios
		// se debe indicar al usuario que debe indicar el ISLR
		// María Vintimilla 07/06/2012
		if(save){ 
			if(is_ValueChanged("XX_TypePerson")){
//				setXX_VCN_ISLRRetention_ID(new BigDecimal(bd));
				String UpdateBP = "UPDATE C_BPARTNER " +
						" SET XX_VCN_ISLRRETENTION_ID = NULL " +
						" WHERE C_BPARTNER_ID = " + get_ID();
				try { DB.executeUpdateEx(UpdateBP, get_Trx()); } 
				catch (SQLException e) { e.printStackTrace(); 	}
			}
					
			if(is_ValueChanged("XX_TypePerson") && (getXX_Services() || getXX_Assets())){
				log.saveInfo("Info", Msg.translate(Env.getCtx(), "XX_ISRLMessage"));
				ADialog.info(1, new Container(), Msg.translate(Env.getCtx(), "XX_ISRLMessage"));
				return true;
			}
		} // save
		return save;
	} // Fin afterSave
}
