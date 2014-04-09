package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import org.compiere.model.MInvoiceTax;
import org.compiere.model.X_AD_Client;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import compiere.model.payments.X_XX_VCN_ISLRAmount;
import compiere.model.suppliesservices.X_XX_Contract;


/**
 *	Invoice Line Model
 *
 *  @author Patricia Ayuso, Modificado por WDIAZ
 */
public class MInvoiceLine extends org.compiere.model.MInvoiceLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idTax;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoiceLine.class);
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInvoiceTax.class);
	
	public MInvoiceLine(Ctx ctx, int C_InvoiceLine_ID, Trx trx) {
		super(ctx, C_InvoiceLine_ID, trx);
	}
	
	/**
	 * 	Parent Constructor
	 * 	@param invoice parent
	 */
	public MInvoiceLine (MInvoice invoice)
	{
		super(invoice);
	}	//	MInvoiceLine

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MInvoiceLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInvoiceLine

	/**@UICallout public void setPriceEntered (String oldPriceEntered,
			String newPriceEntered, int windowNo) throws Exception
	{
		
	}*/

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		boolean save = super.afterSave(newRecord, success);
		
		if(save){			
			
			String sql = "";
			int no=0;
			sql = "UPDATE C_Invoice i"
				+ " SET XX_TAXAMOUNT="
					+ " (SELECT COALESCE(SUM(TaxAmt),0) FROM C_InvoiceLine it WHERE i.C_Invoice_ID=it.C_Invoice_ID) "
				+ " WHERE C_Invoice_ID=" + getC_Invoice_ID();
		no = DB.executeUpdate(get_Trx(), sql);
		if (no != 1)
			log.warning("(1) #" + no);
		}
	
		/****Jessica Mendoza****/
		if (getXX_Contract_ID() == 0){
			String sql = "update XX_ProductPercentDistrib " +
						 "set C_Invoice_ID = " + getC_Invoice_ID() + ", " +
					 	 "C_InvoiceLine_ID = " + getC_InvoiceLine_ID() + " " + 
					 	 "where C_OrderLine_ID = " + getC_OrderLine_ID();
			DB.executeUpdate(get_Trx(), sql);
		}else{
			//Actualizar el impuesto en la línea de la factura, por el seleccionado en la ventana
			String sqlU = "update C_InvoiceLine " +
						 "set C_Tax_ID = " + idTax + " " +
						 "where C_InvoiceLine_ID = " + getC_InvoiceLine_ID();
			DB.executeUpdate(get_Trx(), sqlU);	
		}
		/****Fin código - Jessica Mendoza****/

		return save;
	}
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if save
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		
		idTax = getC_Tax_ID(); //Jessica Mendoza
		MInvoice invoice = new MInvoice( Env.getCtx(), getC_Invoice_ID(), get_Trx());
		
		if(invoice.getC_Order_ID()==0 && invoice.getXX_Contract_ID()==0 && invoice.getXX_InvoiceType()!=null 
				&& !invoice.getXX_InvoiceType().equalsIgnoreCase("I")){
			
			if(getXX_Product_ID()==0){
				log.saveError("Error",Msg.getMsg(getCtx(),"Debe agregar un producto"));
				return false;
			}
			
			setM_Product_ID(getXX_Product_ID());
		}
		
		if(getXX_Contract_ID()!=0 && getXX_Product_ID()!=0)
			setM_Product_ID(getXX_Product_ID());
		
		//Coloca el XX_VCN_ISLRRetention_ID del proveedor por defecto
		if(newRecord){
		
			if(invoice.getXX_InvoiceType()!=null && (invoice.getXX_InvoiceType().equalsIgnoreCase("A") || invoice.getXX_InvoiceType().equalsIgnoreCase("S"))){
				if(invoice.getC_BPartner_ID()!=0){
					MBPartner partner = new MBPartner( Env.getCtx(), invoice.getC_BPartner_ID(), null);
					Integer retention = partner.get_ValueAsInt("XX_VCN_ISLRRetention_ID");
					if(retention!=0)
						setXX_VCN_ISLRRetention_ID(retention);
				}
			}
			
			/** Updating Product Description, Vendor Product Reference & Long Characteristic */
			if(getM_Product_ID() != 0)
			{			
				MProduct mProduct = new MProduct(Env.getCtx(), getM_Product_ID(), null);
				//Jessica Mendoza
				//Setea el id del producto (XX_ProductName_ID) para los productos de bienes y servicios
				if (invoice.getXX_InvoiceType()!=null && (invoice.getXX_InvoiceType().equals("A") || invoice.getXX_InvoiceType().equals("S"))){					
					setXX_Product_ID(getM_Product_ID());
				}else{
					setXX_VMR_LongCharacteristic_ID(mProduct.getXX_VMR_LongCharacteristic_ID());
					setName(mProduct.getName());
					if (mProduct.getXX_VMR_VendorProdRef_ID() > 0) //Modificado por Jessica Mendoza
						setXX_VMR_VendorProdRef_ID(mProduct.getXX_VMR_VendorProdRef_ID());
				}				
			}
			
		}
			
		
		/** Updating Tax */
		if(invoice.getC_DocTypeTarget_ID()!=Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTNEG_ID") 
				&& invoice.getC_DocTypeTarget_ID()!=Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEADJUSTPOS_ID"))
			setTax();
		
		/** Monto a retener ISLR */
		//Si tiene un codigo ISLR
		if(invoice.getXX_InvoiceType()!=null && (invoice.getXX_InvoiceType().equalsIgnoreCase("A") || invoice.getXX_InvoiceType().equalsIgnoreCase("S"))){
		
			if(getXX_VCN_ISLRRetention_ID()!=0){
				
				if(get_ValueOldAsInt("XX_VCN_ISLRRetention_ID")!=getXX_VCN_ISLRRetention_ID())
					ISLRFormulas(get_ValueOldAsInt("XX_VCN_ISLRRetention_ID"), true);
				
				ISLRFormulas(getXX_VCN_ISLRRetention_ID(), false);
			}
			else{
				
				if(get_ValueOldAsInt("XX_VCN_ISLRRetention_ID")!=0)
					ISLRFormulas(get_ValueOldAsInt("XX_VCN_ISLRRetention_ID"), true);
			}
		}else{
			
			if(getM_Product_ID() != 0)
			{			
				MProduct mProduct = new MProduct(Env.getCtx(), getM_Product_ID(), null);
				setXX_VMR_LongCharacteristic_ID(mProduct.getXX_VMR_LongCharacteristic_ID());
				setName(mProduct.getName());
				if (mProduct.getXX_VMR_VendorProdRef_ID() > 0)
					setXX_VMR_VendorProdRef_ID(mProduct.getXX_VMR_VendorProdRef_ID());
			}
		}
			//FIN ISLR

		//MInvoice invoice = new MInvoice( Env.getCtx(), getC_Invoice_ID(), get_Trx());
		MOrder order = new MOrder( Env.getCtx(), invoice.getC_Order_ID(), null);
		
		BigDecimal auxQty= BigDecimal.ZERO;
		if(!newRecord){
			if(order.getXX_OrderType().equalsIgnoreCase("Importada")){			
				setXX_PriceActualInvoice(getPriceActual().multiply(order.getXX_DefinitiveFactor())); //Jessica Mendoza							
				/*setPriceActual(getXX_PriceActualInvoice().multiply(order.getXX_DefinitiveFactor()));
				setPriceEntered(getXX_PriceEnteredInvoice().multiply(order.getXX_DefinitiveFactor()));*/
			}else{				
				setXX_PriceActualInvoice(getPriceActual()); //Jessica Mendoza				
				/*setPriceActual(getXX_PriceActualInvoice());
				setPriceEntered(getXX_PriceEnteredInvoice());*/
			}
		}else{
			if(order.getXX_OrderType().equalsIgnoreCase("Importada")){				
				setXX_PriceActualInvoice(getPriceActual().multiply(order.getXX_DefinitiveFactor())); //Jessica Mendoza				
				setXX_PriceEnteredInvoice(getPriceEntered());
				/*setXX_PriceActualInvoice(getPriceActual());
				setPriceActual(getXX_PriceActualInvoice().multiply(order.getXX_DefinitiveFactor()));
				setPriceEntered(getXX_PriceEnteredInvoice().multiply(order.getXX_DefinitiveFactor()));*/
			}else{
				if(getC_OrderLine_ID() == 0){
					auxQty = getQtyInvoiced();
					//setPriceActual(getXX_PriceActualInvoice());
				}else{
					setXX_PriceActualInvoice(getPriceActual());
					setXX_PriceEnteredInvoice(getPriceEntered());	
				}
			}
		}

		boolean save = super.beforeSave(newRecord);
		if(auxQty.intValue()!=0){
			setQtyInvoiced(auxQty);
		}
		if ((newRecord) && (getC_OrderLine_ID() == 0)){
			if (invoice.isSOTrx())
				setLineNetAmt(getQtyEntered().multiply(getPriceActual())); 
			else
				setLineNetAmt(getQtyInvoiced().multiply(getPriceActual())); 
		}
		
		return save;
	} //	beforeSave
	
	@Override
	protected boolean beforeDelete ()
	{
    	//Se borran las distribuciones
    	String delete = "UPDATE XX_PRODUCTPERCENTDISTRIB SET C_INVOICELINE_ID = NULL WHERE C_INVOICELINE_ID =" + get_ID(); 
    	DB.executeUpdate(null, delete);
    	
    	return super.beforeDelete();
	}	
	
	protected boolean afterDelete(boolean success)
	{
		boolean aux = super.afterDelete(success);
		if (!aux)
			return aux;
		
		if(getXX_VCN_ISLRRetention_ID()!=0)
			ISLRFormulas(getXX_VCN_ISLRRetention_ID(), true);
		
			
		return updateHeaderFactura();
		
	}
	
	
	private boolean updateHeaderFactura()
	{
		String sql = null;
		sql = "UPDATE C_Invoice i"
			+ " SET XX_TAXAMOUNT="
				+ " (SELECT COALESCE(SUM(TaxAmt),0) FROM C_InvoiceLine it WHERE i.C_Invoice_ID=it.C_Invoice_ID) "
			+ " WHERE C_Invoice_ID=" + getC_Invoice_ID();
	    DB.executeUpdate(get_Trx(), sql);
		
		return get_Trx().commit();
	}
	
						
	/** Set XX_Discount1
     * @param Discount1
     */
    public void setXX_Discount1(java.math.BigDecimal Discount1)
    {
    	set_Value ("XX_Discount1", Discount1);
    	
    }
    
    /** Get XX_Discount1
     * @return Discount1
     */
    public java.math.BigDecimal getXX_Discount1()
    {
    	BigDecimal discount1 = get_ValueAsBigDecimal("XX_Discount1");
    	
    	return discount1;
    	
    }    
    
    /** Set XX_Discount2
     * @param Discount2
     */
    public void setXX_Discount2(java.math.BigDecimal Discount2)
    {
    	set_Value ("XX_Discount2", Discount2);
    	
    }
    
    /** Get XX_Discount2
     * @return Discount2
     */
    public java.math.BigDecimal getXX_Discount2()
    {
    	BigDecimal discount2 = get_ValueAsBigDecimal("XX_Discount2");
    	
    	return discount2;
    	
    }  
    
    /** Set XX_Discount3
     * @param Discount3
     */
    public void setXX_Discount3(java.math.BigDecimal Discount3)
    {
    	set_Value ("XX_Discount3", Discount3);
    	
    }
    
    /** Get XX_Discount3
     * @return Discount3
     */
    public java.math.BigDecimal getXX_Discount3()
    {
    	BigDecimal discount3 = get_ValueAsBigDecimal("XX_Discount3");
    	
    	return discount3;
    	
    }  
    
    /** Set XX_Discount4
     * @param Discount4
     */
    public void setXX_Discount4(java.math.BigDecimal Discount4)
    {
    	set_Value ("XX_Discount4", Discount4);
    	
    }
    
    /** Get XX_Discount4
     * @return Discount4
     */
    public java.math.BigDecimal getXX_Discount4()
    {
    	BigDecimal discount4 = get_ValueAsBigDecimal("XX_Discount4");
    	
    	return discount4;
    	
    }  
        
    /** 
     * Get XX_InvoicePrice
     * @return XX_InvoicePrice
     */
    public java.math.BigDecimal getXX_InvoicePrice()
    {
    	return get_ValueAsBigDecimal("XX_InvoicePrice");
    	
    }  
    
    /** 
     * Set XX_InvoicePrice
     * @param InvoicePrice
     */
    public void setXX_InvoicePrice(java.math.BigDecimal InvoicePrice)
    {
    	set_Value ("XX_InvoicePrice", InvoicePrice);
    	
    }
    
    /** 
     * Get XX_InvoicePrice
     * @return XX_InvoicePrice
     */
    public int getXX_VMR_LongCharacteristic_ID() 
    {    	
    	return get_ValueAsInt("XX_VMR_LongCharacteristic_ID");
    }
    
    /** 
     * Set XX_InvoicePrice
     * @param InvoicePrice
     */
    public void setXX_VMR_LongCharacteristic_ID(int id)
    {
    	if (id <= 0) set_Value ("XX_VMR_LongCharacteristic_ID", null);
        else
        	set_Value ("XX_VMR_LongCharacteristic_ID", Integer.valueOf(id));    	
    }
   
    /**
	 * 	Calculate Extended Amt.
	 * 	May or may not include tax
	 
	public void setLineNetAmt ()
	{
		//	Calculations & Rounding
		BigDecimal net = getPriceEntered().multiply(getQtyEntered());
		if (net.scale() > getPrecision())
			net = net.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		super.setLineNetAmt (net);
	}	//	setLineNetAmt*/
		
	/** 
	 * Set Product Reference.
	 * @param XX_VMR_PO_LineRefProv_ID 
	 */
    public void setXX_VMR_PO_LineRefProv_ID (int XX_VMR_PO_LineRefProv_ID)
    {
        if (XX_VMR_PO_LineRefProv_ID <= 0) 
        	set_Value ("XX_VMR_PO_LineRefProv_ID", null);
        else
        	set_Value ("XX_VMR_PO_LineRefProv_ID", 
        			Integer.valueOf(XX_VMR_PO_LineRefProv_ID));
        
    }
    
    /** 
     * Get Product Reference.
     * @return XX_VMR_PO_LineRefProv 
     */
    public int getXX_VMR_PO_LineRefProv_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_LineRefProv_ID");
        
    }    
    
    public BigDecimal getXX_TaxAmount() 
    {
        return get_ValueAsBigDecimal("XX_TaxAmount");
        
    }  
    
    
    
    /** 
     * Set Vendor Product Reference.
     * @param XX_VMR_VendorProdRef_ID Vendor Product Reference 
     */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID <= 0) 
        	set_Value ("XX_VMR_VendorProdRef_ID", null);
        else 
        	set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));        
    }
    
    /** 
     * Get Vendor Product Reference.
     * @return Vendor Product Reference 
     */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");        
    }
    
    /**
	 *	Set Tax - requires ProductID
	 *	@return true if found
	 */
	public boolean setTax()
	{
		MInvoice mInvoice = new MInvoice(Env.getCtx(), getC_Invoice_ID(), get_Trx());
		MOrder order = new MOrder( Env.getCtx(), mInvoice.getC_Order_ID(), null);
		Date fechaDevolu = null;
		String fecha = Env.getCtx().getContext("#FECHA");
		if (!(fecha.equals("")))
		      fechaDevolu = new java.sql.Timestamp(Env.getCtx().getContextAsTime("#FECHA"));
		
		if(order.getXX_OrderType().equalsIgnoreCase("Nacional") || mInvoice.getC_DocTypeTarget_ID()== Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPERETURN_ID")){
		
			org.compiere.model.MProduct mProduct = getProduct(); 
			Date invoiceDate = mInvoice.getDateInvoiced();
			
			if(fechaDevolu!= null)
				invoiceDate = fechaDevolu;
				
			int cTaxCategoryID = -1;
			 if (mProduct != null)
				 cTaxCategoryID = mProduct.getC_TaxCategory_ID();
			int C_Tax_ID = getC_Tax_ID();		
				
			String sql="";
			
			if(getC_OrderLine_ID()>0)
				sql = "select C_Tax_ID from C_TAX where VALIDFROM = " 
					+ "(SELECT MAX(validfrom) FROM C_TAX A, C_ORDERLINE OL WHERE VALIDFROM <= To_Timestamp('" 
					+ invoiceDate + "', 'yyyy-mm-dd hh24:mi:ss:ff') "
					+ "AND A.C_TAX_ID=OL.C_TAX_ID AND OL.C_ORDERLINE_ID= " + getC_OrderLine_ID() + " "
					+ "GROUP BY C_TaxCategory_ID)";
			else
				sql = "select C_Tax_ID from C_TAX where VALIDFROM = " 
					+ "(SELECT MAX(validfrom) FROM C_TAX WHERE VALIDFROM <= To_Timestamp('" 
					+ invoiceDate + "', 'yyyy-mm-dd hh24:mi:ss:ff') AND C_TAXCATEGORY_ID=" 
					+ cTaxCategoryID + " GROUP BY C_TaxCategory_ID)";
			
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					C_Tax_ID = rs.getInt("C_TAX_ID");
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				s_log.log(Level.SEVERE, sql, e); 
			}
			//
			if (C_Tax_ID == 0)
			{
				log.log(Level.SEVERE, "No Tax found");
				return false;
			}
			
			setC_Tax_ID (C_Tax_ID);
		
		}else{ 
			
			if(mInvoice.getXX_Contract_ID()!=0 || mInvoice.getC_Order_ID()!=0){
				
				/****Agregado por Jessica Mendoza****/
				if (mInvoice.getXX_Contract_ID() != 0){
					X_XX_Contract contrat = new X_XX_Contract(Env.getCtx(), getXX_Contract_ID(), get_Trx());
					if (contrat.getXX_ContractType().equals("Nacional"))
						;
						//setC_Tax_ID (Env.getCtx().getContextAsInt("#XX_L_TAX_IVA_ID"));
					else
						setC_Tax_ID (Env.getCtx().getContextAsInt("#XX_L_TAX_EXENTO_ID")); 
				}/****Fin - Jessica Mendoza****/
				else	
					setC_Tax_ID (Env.getCtx().getContextAsInt("#XX_L_TAX_EXENTO_ID"));
				
			}
			else{
				
				Date invoiceDate = mInvoice.getDateInvoiced();
				org.compiere.model.MProduct mProduct = getProduct(); 
				int cTaxCategoryID = mProduct.getC_TaxCategory_ID();
				int C_Tax_ID = 0;
				
				String sql = "select C_Tax_ID from C_TAX where VALIDFROM = " 
						+ "(SELECT MAX(validfrom) FROM C_TAX WHERE VALIDFROM <= To_Timestamp('" 
						+ invoiceDate + "', 'yyyy-mm-dd hh24:mi:ss:ff') AND C_TAXCATEGORY_ID=" 
						+ cTaxCategoryID + " GROUP BY C_TaxCategory_ID)";
				
				PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement (sql, null);
					ResultSet rs = pstmt.executeQuery ();
					if (rs.next ())
					{
						C_Tax_ID = rs.getInt("C_TAX_ID");
					}
					rs.close ();
					pstmt.close ();
					pstmt = null;
				}
				catch (Exception e)
				{
					s_log.log(Level.SEVERE, sql, e); 
				}
				
				setC_Tax_ID (C_Tax_ID);
			}
		}
		
		return true;
	}	//	setTax
	
    /** 
     * Get XX_PriceActual
     * @return XX_PriceActual
     */
    public java.math.BigDecimal getXX_PriceActualInvoice()
    {
    	return get_ValueAsBigDecimal("XX_PriceActualInvoice");
    	
    }  
    
    /** 
     * Set XX_PriceActual
     * @param XX_PriceActual
     */
    public void setXX_PriceActualInvoice(java.math.BigDecimal XX_PriceActual)
    {
    	set_Value ("XX_PriceActualInvoice", XX_PriceActual);
    	
    }
    
    /** 
     * Get XX_PriceEntered
     * @return XX_PriceEntered
     */
    public java.math.BigDecimal getXX_PriceEnteredInvoice()
    {
    	return get_ValueAsBigDecimal("XX_PriceEnteredInvoice");
    	
    }  
    
    /** 
     * Set XX_PriceEntered
     * @param XX_PriceEntered
     */
    public void setXX_PriceEnteredInvoice(java.math.BigDecimal XX_PriceEntered)
    {
    	set_Value ("XX_PriceEnteredInvoice", XX_PriceEntered);
    	
    }
    
    /** 
     * Set ISLR Code.
     * @param XX_VCN_ISLRRetention_ID ISLR 
     */
    public void setXX_VCN_ISLRRetention_ID (int XX_VCN_ISLRRetention_ID)
    {
        set_Value ("XX_VCN_ISLRRetention_ID", Integer.valueOf(XX_VCN_ISLRRetention_ID));        
    }
    
    /** 
     * Get ISLR Retention
     * @return ISLR 
     */
    public int getXX_VCN_ISLRRetention_ID() 
    {
        return get_ValueAsInt("XX_VCN_ISLRRetention_ID");        
    }
    
    /** 
     * Get XX_RetainedAmount
     * @return XX_RetainedAmount
     */
    public java.math.BigDecimal getXX_RetainedAmount()
    {
    	return get_ValueAsBigDecimal("XX_RetainedAmount");
    	
    }  
    
    /** 
     * Set XX_RetainedAmount
     * @param XX_RetainedAmount
     */
    public void setXX_RetainedAmount(java.math.BigDecimal XX_RetainedAmount)
    {
    	set_Value ("XX_RetainedAmount", XX_RetainedAmount);
    	
    }
    
    /** 
     * Set XX_Contract_ID
     * @param XX_Contract_ID 
     */
    public void setXX_Contract_ID (int XX_Contract_ID){
    	if (XX_Contract_ID <= 0) set_ValueNoCheck ("XX_Contract_ID", null);
        else
        set_ValueNoCheck ("XX_Contract_ID", Integer.valueOf(XX_Contract_ID));      
    }
    
    /** 
     * Get XX_Contract_ID
     * @return XX_Contract_ID
     */
    public int getXX_Contract_ID(){
        return get_ValueAsInt("XX_Contract_ID");      
    }
    
    protected BigDecimal getAmountsISLR(int ISLR, boolean delete){
    	
    	BigDecimal amount = BigDecimal.ZERO;
    	
    	if(!delete)
    		if(getXX_VCN_ISLRRetention_ID()!=0)
    			amount = getLineNetAmt();
   
    	String SQL = "SELECT LINENETAMT FROM C_INVOICELINE " +
    				 "WHERE C_INVOICE_ID = " + getC_Invoice_ID() + " AND C_INVOICELINE_ID <> " + get_ID() + " AND XX_VCN_ISLRRetention_ID = " + ISLR;
    	
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
	
			pstmt = DB.prepareStatement(SQL, get_Trx());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				amount = amount.add(rs.getBigDecimal("LINENETAMT"));
			}
		}
		catch (SQLException e){

			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));

		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
    	
    	return amount;
    }
    
    protected BigDecimal getTotalAmountsISLR(int ISLR){
    	
    	BigDecimal amount = BigDecimal.ZERO;
    	
    	if(getXX_VCN_ISLRRetention_ID()!=0)
    		amount = getLineTotalAmt();

    	String SQL = "SELECT LineTotalAmt FROM C_INVOICELINE " +
    				 "WHERE C_INVOICE_ID = " + getC_Invoice_ID() + " AND C_INVOICELINE_ID <> " + get_ID() + " AND XX_VCN_ISLRRetention_ID = " + ISLR;
    	
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
	
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				amount = amount.add(rs.getBigDecimal("LINENETAMT"));
			}
		}
		catch (SQLException e){

			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));

		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
    	
    	return amount;
    }
    
    protected void setISLRAmount(BigDecimal amount){
    	
    	if(getXX_VCN_ISLRRetention_ID()>0){
	    	String sqlU = "UPDATE XX_VCN_ISLRAMOUNT " +
			  "SET XX_RETAINEDAMOUNT =  " + amount + " " +
			  "WHERE C_INVOICE_ID = " + getC_Invoice_ID() + " AND XX_VCN_ISLRRetention_ID = " + getXX_VCN_ISLRRetention_ID();
	
			//Si no habia registro mara modificar se crea uno nuevo
			if (DB.executeUpdate( null, sqlU) < 1){
				
				X_XX_VCN_ISLRAmount ISLRAmount = new X_XX_VCN_ISLRAmount( Env.getCtx(), 0, null);
				ISLRAmount.setC_Invoice_ID(getC_Invoice_ID());
				ISLRAmount.setXX_VCN_ISLRRetention_ID(getXX_VCN_ISLRRetention_ID());
				ISLRAmount.setXX_RetainedAmount(amount);
				ISLRAmount.save();
			}
    	}
    }
    
    protected void ISLRFormulas(int ISLR, boolean delete){
    	
    	X_XX_VCN_ISLRRetention islr = new X_XX_VCN_ISLRRetention( Env.getCtx(), getXX_VCN_ISLRRetention_ID(), null);
		X_AD_Client client = new X_AD_Client( Env.getCtx(), Env.getCtx().getAD_Client_ID(), null);
		BigDecimal zero = new BigDecimal(0);
		BigDecimal rAmount = new BigDecimal(0);
		BigDecimal linesAmount = new BigDecimal(0);
		BigDecimal hundred = new BigDecimal(100);

		
		//Seleccionar todos los montos de las lineas que tengan el mismo tipo de retencion
		linesAmount = getAmountsISLR(ISLR, delete);
		
		if(linesAmount.compareTo(zero)==0){
			//Borro el detalle (de tenerlo)
			String sqlD = "DELETE FROM XX_VCN_ISLRAMOUNT " +
						  "WHERE C_INVOICE_ID = " + getC_Invoice_ID() + " AND XX_VCN_ISLRRetention_ID = " + ISLR;

			//Si no habia registro mara modificar se crea uno nuevo
			DB.executeUpdate( null, sqlD);
			
			return;
		}
			
		//UT
		BigDecimal ut = new BigDecimal(client.get_Value("XX_UT").toString());
		
		//Para los casos que los códigos no tenga marcado Tarifa 2, 
		//ni Pago minino en U.T., ni sustraendo: (Neto de línea * % Base) * % de Retención)
		if(!islr.isXX_ISLRUT() && !islr.isXX_Subtrahend() && islr.getXX_MinimumPayment().equals(zero)){
			
			rAmount = (linesAmount.multiply(islr.getXX_PercentBasis().divide(hundred)))
					   .multiply(islr.getXX_PercentOfRetention().divide(hundred));
			
			setISLRAmount(rAmount);
		
		}
		else if(!islr.isXX_ISLRUT() && !islr.isXX_Subtrahend() && !islr.getXX_MinimumPayment().equals(zero)){
			
			//Para los casos que los códigos tienen marcado Pago mínimo en UT 
			//y no tienen marcado Tarifa 2, ni sustraendo: 
			//Se deben multiplicar las U.T. del pago mínimo por el valor de la U.T. 
			//(tabla AD_Client campo XX_UT), 
			//si este resultado es menor o igual que el Neto de Línea  no aplica retención, 
			//de lo contrario hacer el cálculo igual al ejemplo anterior 
			//(Neto de línea * % Base) * % de Retención). 
			BigDecimal calcUT = new BigDecimal(0);

			calcUT = islr.getXX_MinimumPayment().multiply(ut);
			
			if(calcUT.compareTo(linesAmount)< 0){
				
				rAmount = (linesAmount.multiply(islr.getXX_PercentBasis().divide(hundred)))
				          .multiply(islr.getXX_PercentOfRetention().divide(hundred));

				setISLRAmount(rAmount);
				
			}else{

				setISLRAmount(zero);
			}
			
		}else if(!islr.isXX_ISLRUT() && islr.isXX_Subtrahend() && !islr.getXX_MinimumPayment().equals(zero)){
		
			//Para los casos que los códigos tienen marcado 
			//Pago mínimo en UT, sustraendo y no aplica Tarifa 2: 
			//Se deben multiplicar las U.T. del pago mínimo por el valor de la U.T. 
			//(tabla AD_Client campo XX_UT), 
			//si este resultado es menor o igual que el Neto de Línea no aplica retención, 
			//de lo contrario 
			//(((Neto de línea * % Base) * % de Retención) – (Pago mínimo en U.T. * monto de la U.T.) * % de Retención).
		
			BigDecimal calcUT = new BigDecimal(0);
			BigDecimal rAmount2 = new BigDecimal(0);
			
			calcUT = islr.getXX_MinimumPayment().multiply(ut);
			
			if(calcUT.compareTo(linesAmount)< 0){
				
				rAmount = (linesAmount.multiply(islr.getXX_PercentBasis().divide(hundred)))
						  .multiply(islr.getXX_PercentOfRetention().divide(hundred));
				rAmount2 = (islr.getXX_MinimumPayment().multiply(ut))
							.multiply(islr.getXX_PercentOfRetention().divide(hundred));
				
				setISLRAmount(rAmount.subtract(rAmount2));
				
			}else{
				
				setISLRAmount(zero);
			}
			
		}else if(islr.isXX_ISLRUT()){
			
			//Para los casos que los códigos tienen marcado Tarifa 2: 
			//(Neto de línea * % Base) / monto U.T.   el resultado evaluar en la 
			//tabla 2 que % de Retención le corresponde y el sustraendo en U.T. 
			//una vez ubicado el % se aplica la siguiente formula:  
			//((Neto de línea * % Base) * % de Retención Tabla2) 
			//– (Sustraendo Tabla 2 en U.T. * monto de la U.T.).
			
			BigDecimal first = new BigDecimal(0);
			BigDecimal percUT = new BigDecimal(0);
			BigDecimal subtrahend = new BigDecimal(0);
			BigDecimal rAmount2 = new BigDecimal(0);
			BigDecimal tAmount = BigDecimal.ZERO;
			
			//Obtener todos los montos brutos
			tAmount = getTotalAmountsISLR(getXX_VCN_ISLRRetention_ID());
			
			first = (tAmount.multiply( islr.getXX_PercentBasis().divide(hundred))).divide(ut, 2);

			//Se busca el valor de la tarifa 2 que le corresponde
			String tariff2 = "select XX_SUBTRAHEND, XX_PERCENTOFRETENTION " +
			                 "from XX_VCN_ISLRUT a, AD_Client b " +
			                 "where ((a.XX_UTMIN*b.XX_UT) <= " + first +
			                 " AND (a.XX_UTMAX*b.XX_UT) >= " + first + ") " +
			                 " AND b.AD_CLIENT_ID = " + getAD_Client_ID() + " " +
			                 "order by XX_UTMIN";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				//System.out.println(sql);
				pstmt = DB.prepareStatement(tariff2, null);
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					percUT = rs.getBigDecimal("XX_PERCENTOFRETENTION");
					subtrahend = rs.getBigDecimal("XX_SUBTRAHEND");
				}
			}
			catch (SQLException e){

				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));

			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			rAmount = (linesAmount.multiply(islr.getXX_PercentBasis().divide(hundred))).multiply(percUT.divide(hundred));
			rAmount2 = subtrahend.multiply(ut);
			
			//Insertar Monto
			setISLRAmount(rAmount.subtract(rAmount2));
		}
		else{
			
			setISLRAmount(zero);
		}
    }
    
    /** Set XX_Product_ID.
    @param XX_Product_ID XX_Product_ID Identifier */
    public void setXX_Product_ID (int XX_Product_ID)
    {
        if (XX_Product_ID < 1) throw new IllegalArgumentException ("XX_Product_ID is mandatory.");
        set_ValueNoCheck ("XX_Product_ID", Integer.valueOf(XX_Product_ID));
        
    }
    
    /** Get XX_Product_ID.
    @return XX_Product_ID Identifier */
    public int getXX_Product_ID() 
    {
        return get_ValueAsInt("XX_Product_ID");
        
    }

}
