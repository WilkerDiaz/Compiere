package compiere.model.cds;

import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class X_I_BPartner extends org.compiere.model.X_I_BPartner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public X_I_BPartner(Ctx ctx, int I_BPartner_ID, Trx trx) {
		super(ctx, I_BPartner_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public X_I_BPartner(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Set Employee
	 */
	
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
	    
		public final void setXX_IsEmployee (boolean XX_IsEmployee)
		{
			set_Value("XX_IsEmployee", Boolean.valueOf(XX_IsEmployee));
		}	
		
		  public Boolean getXX_IsEmployee() 
		    {
		        return (Boolean)get_Value("XX_IsEmployee");
		    }
		
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
	    
	    public int getM_Warehouse_ID() 
	    {
	        return get_ValueAsInt("M_Warehouse_ID");      
	    }
	    
	/////////////////////////////////////////////////////////////////////	
		public final void setXX_IsCostumer (boolean XX_IsCostumer)
		{
			set_Value("XX_IsCostumer", Boolean.valueOf(XX_IsCostumer));
		}
		
		  public Boolean getXX_IsCostumer() 
		    {
		        return (Boolean)get_Value("XX_IsCostumer");
		    }
		
	////////////////////////////////////////////////////////////////////	
		public void setXX_Registrado (String XX_Registrado)
	    {
	        set_Value ("XX_Registrado", XX_Registrado);
	        
	    }
		
		public String getXX_Registrado() 
	    {
	        return (String)get_Value("XX_Registrado");
	        
	    }
	////////////////////////////////////////////////////////////////////	
		public void setXX_EstadoCivil (String XX_EstadoCivil)
	    {
	        set_Value ("XX_EstadoCivil", XX_EstadoCivil);
	        
	    }
		
		public String getXX_EstadoCivil() 
	    {
	        return (String)get_Value("XX_EstadoCivil");
	        
	    }
	////////////////////////////////////////////////////////////////////
	    public void setXX_FechaNac (Timestamp XX_FechaNac)
	    {
	        set_Value ("XX_FechaNac", XX_FechaNac);
	        
	    }
	    
	    public Timestamp getXX_FechaNac() 
		{
	    	return (Timestamp)get_Value("XX_FechaNac");
		        
		}
	    
	///////////////////////////////////////////////////////////////////    
		public void setXX_Genero (String XX_Genero)
	    {
	        set_Value ("XX_Genero", XX_Genero);
	        
	    }
		
		public String getXX_Genero() 
	    {
	        return (String)get_Value("XX_Genero");
	        
	    }
		
	//////////////////////////////////////////////////////////////////
		public final void setXX_Iscontac (boolean XX_Iscontac)
		{
			set_Value("XX_Iscontac", Boolean.valueOf(XX_Iscontac));
		}
		
		 public Boolean getXX_Iscontac() 
		    {
		        return (Boolean)get_Value("XX_Iscontac");
		    }
		
	///////////////////////////////////////////////////////////////////
		public void setXX_Email (String XX_Email)
	    {
	        set_Value ("XX_Email", XX_Email);
	        
	    }
		
		public String getXX_Email() 
	    {
	        return (String)get_Value("XX_Email");
	        
	    }
	///////////////////////////////////////////////////////////////////
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
    @param XX_CI_RIF Cédula / RIF on C_BPartner */
    public void setXX_CI_RIF (String XX_CI_RIF)
    {
        set_Value ("XX_CI_RIF", XX_CI_RIF);
        
    }
    
    /** Get XX_CI_RIF.
    @return XX_CI_RIF Cédula / RIF on C_BPartner */
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
    
    /** Set IsVendor.
    @param IsVendor IsVendor */
    public void setIsVendor (Boolean IsVendor)
    {
        set_Value ("IsVendor", IsVendor);
        
    }
    
    /** Get IsVendor.
    @return IsVendor IsVendor */
    public String getIsVendor() 
    {
        return (String)get_Value("IsVendor");     
    }
    
    
    
    /** Set XX_VendorType_ID.
    @param XX_VendorType_ID */
    public void setXX_VendorType_ID (String XX_VendorType_ID)
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
    public void setXX_ProductClass_ID (String XX_ProductClass_ID)
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
    public void setXX_EconomicActivities_ID (String XX_EconomicActivities_ID)
    {
        set_Value ("XX_EconomicActivities_ID", XX_EconomicActivities_ID);
        
    }
    
    /** Get XX_EconomicActivities_ID.
    @return XX_EconomicActivities_ID */
    public Integer getXX_EconomicActivities_ID() 
    {
        return (Integer)get_Value("XX_EconomicActivities_ID");
    }
    
    /** Set PaymentRulePO.
    @param PaymentRulePO Forma de Pago del Business Partner */
    public void set_PaymentRulePO (String PaymentRulePO)
    {
        set_Value ("PaymentRulePO", PaymentRulePO);
        
    }
    
    /** Get PaymentRulePO.
    @return PaymentRulePO Forma de Pago del Business Partner*/
    public String get_PaymentRulePO() 
    {
        return (String)get_Value("PaymentRulePO");
    }
    
    /** Set PO_PaymentTerm_ID.
    @param PO_PaymentTerm_ID Condiciones de Pago del Business Partner */
    public void set_PO_PaymentTerm_ID (String PO_PaymentTerm_ID)
    {
        set_Value ("PO_PaymentTerm_ID", PO_PaymentTerm_ID);
        
    }
    
    /** Get PO_PaymentTerm_ID.
    @return PO_PaymentTerm_ID Condiciones de Pago del Business Partner */
    public Integer get_PO_PaymentTerm_ID() 
    {
        return (Integer)get_Value("PO_PaymentTerm_ID");
    }
    
    /** Set XX_TypeTaxPayer_ID.
    @param XX_TypeTaxPayer_ID */
    public void setXX_TypeTaxPayer_ID (String XX_TypeTaxPayer_ID)
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
    public void setXX_PercentajeRetention_ID (String XX_PercentajeRetention_ID)
    {
        set_Value ("XX_PercentajeRetention_ID", XX_PercentajeRetention_ID);
        
    }
    
    /** Get XX_PercentajeRetention_ID.
    @return XX_PercentajeRetention_ID */
    public Integer getXX_PercentajeRetention_ID() 
    {
        return (Integer)get_Value("XX_PercentajeRetention_ID");
    }
    
    /** Set XX_TypeTax_ID.
    @param XX_TypeTax_ID */
    public void setXX_TypeTax_ID (String XX_TypeTax_ID)
    {
        set_Value ("XX_TypeTax_ID", XX_TypeTax_ID);
        
    }
    
    /** Get XX_TypeTax_ID.
    @return XX_TypeTax_ID */
    public Integer getXX_TypeTax_ID() 
    {
        return (Integer)get_Value("XX_TypeTax_ID");
    }
    
    /** Set XX_CauseRetention_ID.
    @param XX_CauseRetention_ID */
    public void setXX_CauseRetention_ID (String XX_CauseRetention_ID)
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
    public void setXX_RetentionIndicator (String XX_RetentionIndicator)
    {
        set_Value ("XX_RetentionIndicator", XX_RetentionIndicator);
        
    }
    
    /** Get XX_RetentionIndicator.
    @return XX_RetentionIndicator */
    public Boolean getXX_RetentionIndicator() 
    {
        return (Boolean)get_Value("XX_RetentionIndicator");
    }
    
    /** Get City.
    @return Identifies a City */
    public Integer getCity_ID() 
    {
        return (Integer)get_Value("City_ID");
        
    }
    
    public void setCity_ID(int City_ID)
    {
    	set_Value ("City_ID", City_ID);
    }
    
    /** Set XX_ContactType.
    @param XX_ContactType */
    public void setXX_ContactType (String XX_ContactType)
    {
        set_Value ("XX_ContactType", XX_ContactType);
        
    }
    
    /** Get XX_ContactType.
    @return XX_ContactType */
    public String getXX_ContactType() 
    {
        return (String)get_Value("XX_ContactType");
    }
    
    /** Set C_BP_BankAccount_ID.
    @param C_BP_BankAccount_ID  */
    public void setC_BP_BankAccount_ID (int C_BP_BankAccount_ID)
    {
        if (C_BP_BankAccount_ID <= 0) set_Value ("C_BP_BankAccount_ID", null);
        else
        set_Value ("C_BP_BankAccount_ID", Integer.valueOf(C_BP_BankAccount_ID));
        
    }
    
    /** Get C_BP_BankAccount_ID.
    @return C_BP_BankAccount_ID */
    public int getC_BP_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BP_BankAccount_ID");
        
    }
    
    /** Set AccountNo.
    @param AccountNo */
    public void set_AccountNo (String AccountNo)
    {
        set_Value ("AccountNo", AccountNo);
        
    }
    
    /** Get AccountNo.
    @return AccountNo */
    public String get_AccountNo() 
    {
        return (String)get_Value("AccountNo");
    }
    
    /** Set XX_IsPrimary.
    @param XX_IsPrimary */
    public void setXX_IsPrimary (String XX_IsPrimary)
    {
        set_Value ("XX_IsPrimary", XX_IsPrimary);
        
    }
    
    /** Get XX_IsPrimary.
    @return XX_IsPrimary */
    public Boolean getXX_IsPrimary() 
    {
        return (Boolean)get_Value("XX_IsPrimary");
    }
    
    /** Set BankAccountType.
    @param BankAccountType */
    public void set_BankAccountType (String BankAccountType)
    {
        set_Value ("BankAccountType", BankAccountType);
        
    }
    
    /** Get BankAccountType.
    @return BankAccountType */
    public String get_BankAccountType()
    {
        return (String)get_Value("BankAccountType");
    }
    
    /** Set C_Bank_ID.
    @param C_Bank_ID */
    public void setC_Bank_ID (String C_Bank_ID)
    {
        set_Value ("C_Bank_ID", C_Bank_ID);
        
    }
    
    /** Get C_Bank_ID.
    @return C_Bank_ID */
    public Integer getC_Bank_ID()
    {
        return (Integer)get_Value("C_Bank_ID");
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
    
    /** Set XX_FigureVendor.
    @param XX_FigureVendor */
    public void setXX_FigureVendor (String XX_FigureVendor)
    {
        set_Value ("XX_FigureVendor", XX_FigureVendor);
        
    }
    
    /** Get XX_FigureVendor.
    @return XX_FigureVendor */
    public String getXX_FigureVendor()
    {
        return (String)get_Value("XX_FigureVendor");
    }
    
    /** Set XX_Phone.
    @param XX_Phone Identifies a telephone number */
    public void setXX_Phone (String XX_Phone)
    {
        set_Value ("XX_Phone", XX_Phone);
        
    }
    
    /** Get XX_Phone.
    @return Identifies a telephone number */
    public String getXX_Phone() 
    {
        return (String)get_Value("XX_Phone");
        
    }
    
    /** Set XX_Phone2.
    @param XX_Phone2 Identifies a telephone number */
    public void setXX_Phone2 (String XX_Phone2)
    {
        set_Value ("XX_Phone2", XX_Phone2);
        
    }
    
    /** Get XX_Phone2.
    @return Identifies a telephone number */
    public String getXX_Phone2() 
    {
        return (String)get_Value("XX_Phone2");
        
    }
    
    /** Set XX_Fax.
    @param XX_Fax Identifies a telephone number */
    public void setXX_Fax (String XX_Fax)
    {
        set_Value ("XX_Fax", XX_Fax);
        
    }
    
    /** Get Phone.
    @return Identifies a telephone number */
    public String getXX_Fax() 
    {
        return (String)get_Value("XX_Fax");
        
    }
}
