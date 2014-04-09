/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_DistributionHeader
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DistributionHeader extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DistributionHeader_ID id
    @param trx transaction
    */
    public X_XX_VMR_DistributionHeader (Ctx ctx, int XX_VMR_DistributionHeader_ID, Trx trx)
    {
        super (ctx, XX_VMR_DistributionHeader_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DistributionHeader_ID == 0)
        {
            setXX_CalculatedPOSPercentages (false);	// N
            setXX_CalculatedSizeCurve (false);
            setXX_DistributionStatus (null);
            setXX_VMR_DistributionHeader_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_DistributionHeader (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27636844393789L;
    /** Last Updated Timestamp 2012-12-05 10:21:17.0 */
    public static final long updatedMS = 1354719077000L;
    /** AD_Table_ID=1000150 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DistributionHeader");
        
    }
    ;
    
    /** TableName=XX_VMR_DistributionHeader */
    public static final String Table_Name="XX_VMR_DistributionHeader";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        throw new IllegalArgumentException ("C_BPartner_ID is virtual column");
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
        else
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Target Doc Type.
    @param C_DocTypeTarget_ID Target document type for documents */
    public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID)
    {
        if (C_DocTypeTarget_ID <= 0) set_Value ("C_DocTypeTarget_ID", null);
        else
        set_Value ("C_DocTypeTarget_ID", Integer.valueOf(C_DocTypeTarget_ID));
        
    }
    
    /** Get Target Doc Type.
    @return Target document type for documents */
    public int getC_DocTypeTarget_ID() 
    {
        return get_ValueAsInt("C_DocTypeTarget_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** <None> = -- */
    public static final String DOCACTION_None = X_Ref__Document_Action.NONE.getValue();
    /** Approve = AP */
    public static final String DOCACTION_Approve = X_Ref__Document_Action.APPROVE.getValue();
    /** Close = CL */
    public static final String DOCACTION_Close = X_Ref__Document_Action.CLOSE.getValue();
    /** Complete = CO */
    public static final String DOCACTION_Complete = X_Ref__Document_Action.COMPLETE.getValue();
    /** Invalidate = IN */
    public static final String DOCACTION_Invalidate = X_Ref__Document_Action.INVALIDATE.getValue();
    /** Post = PO */
    public static final String DOCACTION_Post = X_Ref__Document_Action.POST.getValue();
    /** Prepare = PR */
    public static final String DOCACTION_Prepare = X_Ref__Document_Action.PREPARE.getValue();
    /** Reverse - Accrual = RA */
    public static final String DOCACTION_Reverse_Accrual = X_Ref__Document_Action.REVERSE__ACCRUAL.getValue();
    /** Reverse - Correct = RC */
    public static final String DOCACTION_Reverse_Correct = X_Ref__Document_Action.REVERSE__CORRECT.getValue();
    /** Re-activate = RE */
    public static final String DOCACTION_Re_Activate = X_Ref__Document_Action.RE__ACTIVATE.getValue();
    /** Reject = RJ */
    public static final String DOCACTION_Reject = X_Ref__Document_Action.REJECT.getValue();
    /** Void = VO */
    public static final String DOCACTION_Void = X_Ref__Document_Action.VOID.getValue();
    /** Wait Complete = WC */
    public static final String DOCACTION_WaitComplete = X_Ref__Document_Action.WAIT_COMPLETE.getValue();
    /** Unlock = XL */
    public static final String DOCACTION_Unlock = X_Ref__Document_Action.UNLOCK.getValue();
    /** Set Document Action.
    @param DocAction The targeted status of the document */
    public void setDocAction (String DocAction)
    {
        if (!X_Ref__Document_Action.isValid(DocAction))
        throw new IllegalArgumentException ("DocAction Invalid value - " + DocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
        set_Value ("DocAction", DocAction);
        
    }
    
    /** Get Document Action.
    @return The targeted status of the document */
    public String getDocAction() 
    {
        return (String)get_Value("DocAction");
        
    }
    
    /** Unknown = ?? */
    public static final String DOCSTATUS_Unknown = X_Ref__Document_Status.UNKNOWN.getValue();
    /** Approved = AP */
    public static final String DOCSTATUS_Approved = X_Ref__Document_Status.APPROVED.getValue();
    /** Closed = CL */
    public static final String DOCSTATUS_Closed = X_Ref__Document_Status.CLOSED.getValue();
    /** Completed = CO */
    public static final String DOCSTATUS_Completed = X_Ref__Document_Status.COMPLETED.getValue();
    /** Drafted = DR */
    public static final String DOCSTATUS_Drafted = X_Ref__Document_Status.DRAFTED.getValue();
    /** Invalid = IN */
    public static final String DOCSTATUS_Invalid = X_Ref__Document_Status.INVALID.getValue();
    /** In Progress = IP */
    public static final String DOCSTATUS_InProgress = X_Ref__Document_Status.IN_PROGRESS.getValue();
    /** Not Approved = NA */
    public static final String DOCSTATUS_NotApproved = X_Ref__Document_Status.NOT_APPROVED.getValue();
    /** Reversed = RE */
    public static final String DOCSTATUS_Reversed = X_Ref__Document_Status.REVERSED.getValue();
    /** Voided = VO */
    public static final String DOCSTATUS_Voided = X_Ref__Document_Status.VOIDED.getValue();
    /** Waiting Confirmation = WC */
    public static final String DOCSTATUS_WaitingConfirmation = X_Ref__Document_Status.WAITING_CONFIRMATION.getValue();
    /** Waiting Payment = WP */
    public static final String DOCSTATUS_WaitingPayment = X_Ref__Document_Status.WAITING_PAYMENT.getValue();
    /** Set Document Status.
    @param DocStatus The current status of the document */
    public void setDocStatus (String DocStatus)
    {
        if (!X_Ref__Document_Status.isValid(DocStatus))
        throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_Value ("DocStatus", DocStatus);
        
    }
    
    /** Get Document Status.
    @return The current status of the document */
    public String getDocStatus() 
    {
        return (String)get_Value("DocStatus");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (String IsApproved)
    {
        set_Value ("IsApproved", IsApproved);
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public String getIsApproved() 
    {
        return (String)get_Value("IsApproved");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_Value ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Alert.
    @param XX_Alert Alert */
    public void setXX_Alert (boolean XX_Alert)
    {
        set_Value ("XX_Alert", Boolean.valueOf(XX_Alert));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert() 
    {
        return get_ValueAsBoolean("XX_Alert");
        
    }
    
    /** Set Alert.
    @param XX_Alert2 Alert */
    public void setXX_Alert2 (boolean XX_Alert2)
    {
        set_Value ("XX_Alert2", Boolean.valueOf(XX_Alert2));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert2() 
    {
        return get_ValueAsBoolean("XX_Alert2");
        
    }
    
    /** Set Alert.
    @param XX_Alert3 Alert */
    public void setXX_Alert3 (boolean XX_Alert3)
    {
        set_Value ("XX_Alert3", Boolean.valueOf(XX_Alert3));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert3() 
    {
        return get_ValueAsBoolean("XX_Alert3");
        
    }
    
    /** Set Todos calculados.
    @param XX_AllCalculated Todos calculados */
    public void setXX_AllCalculated (boolean XX_AllCalculated)
    {
        set_Value ("XX_AllCalculated", Boolean.valueOf(XX_AllCalculated));
        
    }
    
    /** Get Todos calculados.
    @return Todos calculados */
    public boolean isXX_AllCalculated() 
    {
        return get_ValueAsBoolean("XX_AllCalculated");
        
    }
    
    /** Set Approve Distribution.
    @param XX_ApproveDistribution Approve Distribution */
    public void setXX_ApproveDistribution (String XX_ApproveDistribution)
    {
        set_Value ("XX_ApproveDistribution", XX_ApproveDistribution);
        
    }
    
    /** Get Approve Distribution.
    @return Approve Distribution */
    public String getXX_ApproveDistribution() 
    {
        return (String)get_Value("XX_ApproveDistribution");
        
    }
    
    /** Set Automatic Adjustment.
    @param XX_AutoAdjustment Automatic Adjustment */
    public void setXX_AutoAdjustment (boolean XX_AutoAdjustment)
    {
        set_Value ("XX_AutoAdjustment", Boolean.valueOf(XX_AutoAdjustment));
        
    }
    
    /** Get Automatic Adjustment.
    @return Automatic Adjustment */
    public boolean isXX_AutoAdjustment() 
    {
        return get_ValueAsBoolean("XX_AutoAdjustment");
        
    }
    
    /** Set Business Partner.
    @param XX_BPartner_Name Business Partner */
    public void setXX_BPartner_Name (String XX_BPartner_Name)
    {
        set_Value ("XX_BPartner_Name", XX_BPartner_Name);
        
    }
    
    /** Get Business Partner.
    @return Business Partner */
    public String getXX_BPartner_Name() 
    {
        return (String)get_Value("XX_BPartner_Name");
        
    }
    
    /** Set Brand.
    @param XX_Brand_Name Brand */
    public void setXX_Brand_Name (String XX_Brand_Name)
    {
        set_Value ("XX_Brand_Name", XX_Brand_Name);
        
    }
    
    /** Get Brand.
    @return Brand */
    public String getXX_Brand_Name() 
    {
        return (String)get_Value("XX_Brand_Name");
        
    }
    
    /** Set XX_BudgetDistribution.
    @param XX_BudgetDistribution XX_BudgetDistribution */
    public void setXX_BudgetDistribution (String XX_BudgetDistribution)
    {
        set_Value ("XX_BudgetDistribution", XX_BudgetDistribution);
        
    }
    
    /** Get XX_BudgetDistribution.
    @return XX_BudgetDistribution */
    public String getXX_BudgetDistribution() 
    {
        return (String)get_Value("XX_BudgetDistribution");
        
    }
    
    /** Set Buyer's Comments.
    @param XX_BuyersComments Comentarios del Comprador */
    public void setXX_BuyersComments (String XX_BuyersComments)
    {
        throw new IllegalArgumentException ("XX_BuyersComments is virtual column");
        
    }
    
    /** Get Buyer's Comments.
    @return Comentarios del Comprador */
    public String getXX_BuyersComments() 
    {
        return (String)get_Value("XX_BuyersComments");
        
    }
    
    /** Set Calculated Store Percentages.
    @param XX_CalculatedPOSPercentages Calculated Store Percentages */
    public void setXX_CalculatedPOSPercentages (boolean XX_CalculatedPOSPercentages)
    {
        set_Value ("XX_CalculatedPOSPercentages", Boolean.valueOf(XX_CalculatedPOSPercentages));
        
    }
    
    /** Get Calculated Store Percentages.
    @return Calculated Store Percentages */
    public boolean isXX_CalculatedPOSPercentages() 
    {
        return get_ValueAsBoolean("XX_CalculatedPOSPercentages");
        
    }
    
    /** Set Calculated PO Store Quantities.
    @param XX_CalculatedPOSQuantities Calculated PO Store Quantities */
    public void setXX_CalculatedPOSQuantities (boolean XX_CalculatedPOSQuantities)
    {
        set_Value ("XX_CalculatedPOSQuantities", Boolean.valueOf(XX_CalculatedPOSQuantities));
        
    }
    
    /** Get Calculated PO Store Quantities.
    @return Calculated PO Store Quantities */
    public boolean isXX_CalculatedPOSQuantities() 
    {
        return get_ValueAsBoolean("XX_CalculatedPOSQuantities");
        
    }
    
    /** Set Calculated Size Curve.
    @param XX_CalculatedSizeCurve Calculated Size Curve */
    public void setXX_CalculatedSizeCurve (boolean XX_CalculatedSizeCurve)
    {
        set_Value ("XX_CalculatedSizeCurve", Boolean.valueOf(XX_CalculatedSizeCurve));
        
    }
    
    /** Get Calculated Size Curve.
    @return Calculated Size Curve */
    public boolean isXX_CalculatedSizeCurve() 
    {
        return get_ValueAsBoolean("XX_CalculatedSizeCurve");
        
    }
    
    /** Set Collection.
    @param XX_CollectionName Collection */
    public void setXX_CollectionName (String XX_CollectionName)
    {
        set_Value ("XX_CollectionName", XX_CollectionName);
        
    }
    
    /** Get Collection.
    @return Collection */
    public String getXX_CollectionName() 
    {
        return (String)get_Value("XX_CollectionName");
        
    }
    
    /** Set Definitive Prices Setted.
    @param XX_DefinitivePricesSetted Definitive Prices Setted */
    public void setXX_DefinitivePricesSetted (String XX_DefinitivePricesSetted)
    {
        set_Value ("XX_DefinitivePricesSetted", XX_DefinitivePricesSetted);
        
    }
    
    /** Get Definitive Prices Setted.
    @return Definitive Prices Setted */
    public String getXX_DefinitivePricesSetted() 
    {
        return (String)get_Value("XX_DefinitivePricesSetted");
        
    }
    
    /** Set Department.
    @param XX_Department_Name Department Name */
    public void setXX_Department_Name (String XX_Department_Name)
    {
        set_Value ("XX_Department_Name", XX_Department_Name);
        
    }
    
    /** Get Department.
    @return Department Name */
    public String getXX_Department_Name() 
    {
        return (String)get_Value("XX_Department_Name");
        
    }
    
    /** Set Distribute All.
    @param XX_DistributeAll Distribute All */
    public void setXX_DistributeAll (String XX_DistributeAll)
    {
        set_Value ("XX_DistributeAll", XX_DistributeAll);
        
    }
    
    /** Get Distribute All.
    @return Distribute All */
    public String getXX_DistributeAll() 
    {
        return (String)get_Value("XX_DistributeAll");
        
    }
    
    /** Set XX_DistributionOCReplacement.
    @param XX_DistributionOCReplacement XX_DistributionOCReplacement */
    public void setXX_DistributionOCReplacement (String XX_DistributionOCReplacement)
    {
        set_Value ("XX_DistributionOCReplacement", XX_DistributionOCReplacement);
        
    }
    
    /** Get XX_DistributionOCReplacement.
    @return XX_DistributionOCReplacement */
    public String getXX_DistributionOCReplacement() 
    {
        return (String)get_Value("XX_DistributionOCReplacement");
        
    }
    
    /** Set XX_DistributionOCSales.
    @param XX_DistributionOCSales XX_DistributionOCSales */
    public void setXX_DistributionOCSales (String XX_DistributionOCSales)
    {
        set_Value ("XX_DistributionOCSales", XX_DistributionOCSales);
        
    }
    
    /** Get XX_DistributionOCSales.
    @return XX_DistributionOCSales */
    public String getXX_DistributionOCSales() 
    {
        return (String)get_Value("XX_DistributionOCSales");
        
    }
    
    /** Set XX_DistributionOCSalesBudget.
    @param XX_DistributionOCSalesBudget Distribution Purchase Order by Sales/Budget */
    public void setXX_DistributionOCSalesBudget (String XX_DistributionOCSalesBudget)
    {
        set_Value ("XX_DistributionOCSalesBudget", XX_DistributionOCSalesBudget);
        
    }
    
    /** Get XX_DistributionOCSalesBudget.
    @return Distribution Purchase Order by Sales/Budget */
    public String getXX_DistributionOCSalesBudget() 
    {
        return (String)get_Value("XX_DistributionOCSalesBudget");
        
    }
    
    /** Aprobada - Pendiente por chequeo de la O/C = AC */
    public static final String XX_DISTRIBUTIONSTATUS_Aprobada_PendientePorChequeoDeLaOC = X_Ref_XX_DistributionStatus.APROBADA__PENDIENTE_POR_CHEQUEO_DE_LA_OC.getValue();
    /** Anulada = AN */
    public static final String XX_DISTRIBUTIONSTATUS_Anulada = X_Ref_XX_DistributionStatus.ANULADA.getValue();
    /** Aprobada = AP */
    public static final String XX_DISTRIBUTIONSTATUS_Aprobada = X_Ref_XX_DistributionStatus.APROBADA.getValue();
    /** Pendiente por fijar Precios Definitivos = FP */
    public static final String XX_DISTRIBUTIONSTATUS_PendientePorFijarPreciosDefinitivos = X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue();
    /** Pendiente = PE */
    public static final String XX_DISTRIBUTIONSTATUS_Pendiente = X_Ref_XX_DistributionStatus.PENDIENTE.getValue();
    /** Lista para Aprobar = QR */
    public static final String XX_DISTRIBUTIONSTATUS_ListaParaAprobar = X_Ref_XX_DistributionStatus.LISTA_PARA_APROBAR.getValue();
    /** Pendiente por Redistribucion y Aprobacion = QT */
    public static final String XX_DISTRIBUTIONSTATUS_PendientePorRedistribucionYAprobacion = X_Ref_XX_DistributionStatus.PENDIENTE_POR_REDISTRIBUCION_Y_APROBACION.getValue();
    /** Set Distribuition Status.
    @param XX_DistributionStatus Distribuition Status */
    public void setXX_DistributionStatus (String XX_DistributionStatus)
    {
        if (XX_DistributionStatus == null) throw new IllegalArgumentException ("XX_DistributionStatus is mandatory");
        if (!X_Ref_XX_DistributionStatus.isValid(XX_DistributionStatus))
        throw new IllegalArgumentException ("XX_DistributionStatus Invalid value - " + XX_DistributionStatus + " - Reference_ID=1000148 - AC - AN - AP - FP - PE - QR - QT");
        set_Value ("XX_DistributionStatus", XX_DistributionStatus);
        
    }
    
    /** Get Distribuition Status.
    @return Distribuition Status */
    public String getXX_DistributionStatus() 
    {
        return (String)get_Value("XX_DistributionStatus");
        
    }
    
    /** Set Distribution Type Applied.
    @param XX_DistributionTypeApplied Distribution Type Applied */
    public void setXX_DistributionTypeApplied (int XX_DistributionTypeApplied)
    {
        set_Value ("XX_DistributionTypeApplied", Integer.valueOf(XX_DistributionTypeApplied));
        
    }
    
    /** Get Distribution Type Applied.
    @return Distribution Type Applied */
    public int getXX_DistributionTypeApplied() 
    {
        return get_ValueAsInt("XX_DistributionTypeApplied");
        
    }
    
    /** Set Set Definitive Prices.
    @param XX_FixPrices Set Definitive Prices */
    public void setXX_FixPrices (String XX_FixPrices)
    {
        set_Value ("XX_FixPrices", XX_FixPrices);
        
    }
    
    /** Get Set Definitive Prices.
    @return Set Definitive Prices */
    public String getXX_FixPrices() 
    {
        return (String)get_Value("XX_FixPrices");
        
    }
    
    /** Set Has Textil Products.
    @param XX_HasTextilProducts Has Textil Products */
    public void setXX_HasTextilProducts (boolean XX_HasTextilProducts)
    {
        set_ValueNoCheck ("XX_HasTextilProducts", Boolean.valueOf(XX_HasTextilProducts));
        
    }
    
    /** Get Has Textil Products.
    @return Has Textil Products */
    public boolean isXX_HasTextilProducts() 
    {
        return get_ValueAsBoolean("XX_HasTextilProducts");
        
    }
    
    /** Set Ignore Package Multiple.
    @param XX_IgnorePackageMultiple If selected, the distribution will ignore the package multiple for all selected products. If there's a size curve selected this check will be ignored. */
    public void setXX_IgnorePackageMultiple (boolean XX_IgnorePackageMultiple)
    {
        set_Value ("XX_IgnorePackageMultiple", Boolean.valueOf(XX_IgnorePackageMultiple));
        
    }
    
    /** Get Ignore Package Multiple.
    @return If selected, the distribution will ignore the package multiple for all selected products. If there's a size curve selected this check will be ignored. */
    public boolean isXX_IgnorePackageMultiple() 
    {
        return get_ValueAsBoolean("XX_IgnorePackageMultiple");
        
    }
    
    /** Set Is Automatic Redistribution.
    @param XX_IsAutomaticRedistribution Is Automatic Redistribution */
    public void setXX_IsAutomaticRedistribution (boolean XX_IsAutomaticRedistribution)
    {
        set_Value ("XX_IsAutomaticRedistribution", Boolean.valueOf(XX_IsAutomaticRedistribution));
        
    }
    
    /** Get Is Automatic Redistribution.
    @return Is Automatic Redistribution */
    public boolean isXX_IsAutomaticRedistribution() 
    {
        return get_ValueAsBoolean("XX_IsAutomaticRedistribution");
        
    }
    
    /** Set Price Is Definitive (Whole Reference).
    @param XX_IsDefinitive Price Is Definitive (Whole Reference) */
    public void setXX_IsDefinitive (boolean XX_IsDefinitive)
    {
        set_Value ("XX_IsDefinitive", Boolean.valueOf(XX_IsDefinitive));
        
    }
    
    /** Get Price Is Definitive (Whole Reference).
    @return Price Is Definitive (Whole Reference) */
    public boolean isXX_IsDefinitive() 
    {
        return get_ValueAsBoolean("XX_IsDefinitive");
        
    }
    
    /** Set XX_IsPrintedLabels.
    @param XX_IsPrintedLabels Indica si han sido impresas las etiquetas consolidadas de los productos de la distribución. */
    public void setXX_IsPrintedLabels (boolean XX_IsPrintedLabels)
    {
        set_Value ("XX_IsPrintedLabels", Boolean.valueOf(XX_IsPrintedLabels));
        
    }
    
    /** Get XX_IsPrintedLabels.
    @return Indica si han sido impresas las etiquetas consolidadas de los productos de la distribución. */
    public boolean isXX_IsPrintedLabels() 
    {
        return get_ValueAsBoolean("XX_IsPrintedLabels");
        
    }
    
    /** Set Line Name.
    @param XX_Line_Name Line Name */
    public void setXX_Line_Name (String XX_Line_Name)
    {
        set_Value ("XX_Line_Name", XX_Line_Name);
        
    }
    
    /** Get Line Name.
    @return Line Name */
    public String getXX_Line_Name() 
    {
        return (String)get_Value("XX_Line_Name");
        
    }
    
    /** Set Manual Distribution.
    @param XX_ManualDistribution Manual Distribution */
    public void setXX_ManualDistribution (String XX_ManualDistribution)
    {
        set_Value ("XX_ManualDistribution", XX_ManualDistribution);
        
    }
    
    /** Get Manual Distribution.
    @return Manual Distribution */
    public String getXX_ManualDistribution() 
    {
        return (String)get_Value("XX_ManualDistribution");
        
    }
    
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (int XX_Month)
    {
        set_Value ("XX_Month", Integer.valueOf(XX_Month));
        
    }
    
    /** Get Month.
    @return Month */
    public int getXX_Month() 
    {
        return get_ValueAsInt("XX_Month");
        
    }
    
    /** ANULADA = AN */
    public static final String XX_ORDERSTATUS_ANULADA = X_Ref_XX_OrderStatus.ANULADA.getValue();
    /** APROBADA = AP */
    public static final String XX_ORDERSTATUS_APROBADA = X_Ref_XX_OrderStatus.APROBADA.getValue();
    /** CHEQUEADA = CH */
    public static final String XX_ORDERSTATUS_CHEQUEADA = X_Ref_XX_OrderStatus.CHEQUEADA.getValue();
    /** EN ADUANA = EA */
    public static final String XX_ORDERSTATUS_ENADUANA = X_Ref_XX_OrderStatus.ENADUANA.getValue();
    /** ENTREGADA AL AGENTE DE CARGA = EAC */
    public static final String XX_ORDERSTATUS_ENTREGADAALAGENTEDECARGA = X_Ref_XX_OrderStatus.ENTREGADAALAGENTEDECARGA.getValue();
    /** EN PRODUCCIÓN = EP */
    public static final String XX_ORDERSTATUS_ENPRODUCCIÓN = X_Ref_XX_OrderStatus.ENPRODUCCIÓN.getValue();
    /** EN PROCESO DE NACIONALIZACIÓN = EPN */
    public static final String XX_ORDERSTATUS_ENPROCESODENACIONALIZACIÓN = X_Ref_XX_OrderStatus.ENPROCESODENACIONALIZACIÓN.getValue();
    /** EN TRÁNSITO INTERNACIONAL = ETI */
    public static final String XX_ORDERSTATUS_ENTRÁNSITOINTERNACIONAL = X_Ref_XX_OrderStatus.ENTRÁNSITOINTERNACIONAL.getValue();
    /** EN TRÁNSITO NACIONAL = ETN */
    public static final String XX_ORDERSTATUS_ENTRÁNSITONACIONAL = X_Ref_XX_OrderStatus.ENTRÁNSITONACIONAL.getValue();
    /** LLEGADA A CD = LCD */
    public static final String XX_ORDERSTATUS_LLEGADAACD = X_Ref_XX_OrderStatus.LLEGADAACD.getValue();
    /** LLEGADA A VENEZUELA = LVE */
    public static final String XX_ORDERSTATUS_LLEGADAAVENEZUELA = X_Ref_XX_OrderStatus.LLEGADAAVENEZUELA.getValue();
    /** PENDIENTE = PEN */
    public static final String XX_ORDERSTATUS_PENDIENTE = X_Ref_XX_OrderStatus.PENDIENTE.getValue();
    /** PROFORMA = PRO */
    public static final String XX_ORDERSTATUS_PROFORMA = X_Ref_XX_OrderStatus.PROFORMA.getValue();
    /** RECIBIDA = RE */
    public static final String XX_ORDERSTATUS_RECIBIDA = X_Ref_XX_OrderStatus.RECIBIDA.getValue();
    /** SITME = SIT */
    public static final String XX_ORDERSTATUS_SITME = X_Ref_XX_OrderStatus.SITME.getValue();
    /** Set PO Status.
    @param XX_OrderStatus Estado de la Orden de Compra */
    public void setXX_OrderStatus (String XX_OrderStatus)
    {
        if (!X_Ref_XX_OrderStatus.isValid(XX_OrderStatus))
        throw new IllegalArgumentException ("XX_OrderStatus Invalid value - " + XX_OrderStatus + " - Reference_ID=1000103 - AN - AP - CH - EA - EAC - EP - EPN - ETI - ETN - LCD - LVE - PEN - PRO - RE - SIT");
        throw new IllegalArgumentException ("XX_OrderStatus is virtual column");
        
    }
    
    /** Get PO Status.
    @return Estado de la Orden de Compra */
    public String getXX_OrderStatus() 
    {
        return (String)get_Value("XX_OrderStatus");
        
    }
    
    /** Importada = Importada */
    public static final String XX_ORDERTYPE_Importada = X_Ref_XX_OrderType.IMPORTADA.getValue();
    /** Nacional = Nacional */
    public static final String XX_ORDERTYPE_Nacional = X_Ref_XX_OrderType.NACIONAL.getValue();
    /** Set Order Type.
    @param XX_OrderType Tipo de Orden (Nacional / Internacional) */
    public void setXX_OrderType (String XX_OrderType)
    {
        if (!X_Ref_XX_OrderType.isValid(XX_OrderType))
        throw new IllegalArgumentException ("XX_OrderType Invalid value - " + XX_OrderType + " - Reference_ID=1000049 - Importada - Nacional");
        throw new IllegalArgumentException ("XX_OrderType is virtual column");
        
    }
    
    /** Get Order Type.
    @return Tipo de Orden (Nacional / Internacional) */
    public String getXX_OrderType() 
    {
        return (String)get_Value("XX_OrderType");
        
    }
    
    /** Set Package Name.
    @param XX_PackageName Package Name */
    public void setXX_PackageName (String XX_PackageName)
    {
        set_Value ("XX_PackageName", XX_PackageName);
        
    }
    
    /** Get Package Name.
    @return Package Name */
    public String getXX_PackageName() 
    {
        return (String)get_Value("XX_PackageName");
        
    }
    
    /** Set Apply Percentual Distribution Method .
    @param XX_Percentual_Distribution Apply Percentual Distribution Method  */
    public void setXX_Percentual_Distribution (String XX_Percentual_Distribution)
    {
        set_Value ("XX_Percentual_Distribution", XX_Percentual_Distribution);
        
    }
    
    /** Get Apply Percentual Distribution Method .
    @return Apply Percentual Distribution Method  */
    public String getXX_Percentual_Distribution() 
    {
        return (String)get_Value("XX_Percentual_Distribution");
        
    }
    
    /** Set Product Name.
    @param XX_Product_Name Product Name */
    public void setXX_Product_Name (String XX_Product_Name)
    {
        set_Value ("XX_Product_Name", XX_Product_Name);
        
    }
    
    /** Get Product Name.
    @return Product Name */
    public String getXX_Product_Name() 
    {
        return (String)get_Value("XX_Product_Name");
        
    }
    
    /** Set Product Quantity.
    @param XX_ProductQuantity Product Quantity */
    public void setXX_ProductQuantity (java.math.BigDecimal XX_ProductQuantity)
    {
        throw new IllegalArgumentException ("XX_ProductQuantity is virtual column");
        
    }
    
    /** Get Product Quantity.
    @return Product Quantity */
    public java.math.BigDecimal getXX_ProductQuantity() 
    {
        return get_ValueAsBigDecimal("XX_ProductQuantity");
        
    }
    
    /** Set Section Name.
    @param XX_Section_Name Section Name */
    public void setXX_Section_Name (String XX_Section_Name)
    {
        set_Value ("XX_Section_Name", XX_Section_Name);
        
    }
    
    /** Get Section Name.
    @return Section Name */
    public String getXX_Section_Name() 
    {
        return (String)get_Value("XX_Section_Name");
        
    }
    
    /** Set Show Percentages .
    @param XX_ShowPercentages Show Percentages  */
    public void setXX_ShowPercentages (String XX_ShowPercentages)
    {
        set_Value ("XX_ShowPercentages", XX_ShowPercentages);
        
    }
    
    /** Get Show Percentages .
    @return Show Percentages  */
    public String getXX_ShowPercentages() 
    {
        return (String)get_Value("XX_ShowPercentages");
        
    }
    
    /** Set Size Curve.
    @param XX_SizeCurve Size Curve */
    public void setXX_SizeCurve (String XX_SizeCurve)
    {
        set_Value ("XX_SizeCurve", XX_SizeCurve);
        
    }
    
    /** Get Size Curve.
    @return Size Curve */
    public String getXX_SizeCurve() 
    {
        return (String)get_Value("XX_SizeCurve");
        
    }
    
    /** Set Use Last Year Sales.
    @param XX_UseLastYearSales Use Last Year Sales */
    public void setXX_UseLastYearSales (boolean XX_UseLastYearSales)
    {
        set_Value ("XX_UseLastYearSales", Boolean.valueOf(XX_UseLastYearSales));
        
    }
    
    /** Get Use Last Year Sales.
    @return Use Last Year Sales */
    public boolean isXX_UseLastYearSales() 
    {
        return get_ValueAsBoolean("XX_UseLastYearSales");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Department_ID is virtual column");
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        if (XX_VMR_DistributionHeader_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionHeader_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistributionHeader_ID", Integer.valueOf(XX_VMR_DistributionHeader_ID));
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set Distribution Type.
    @param XX_VMR_DistributionType_ID Distribution Type */
    public void setXX_VMR_DistributionType_ID (int XX_VMR_DistributionType_ID)
    {
        if (XX_VMR_DistributionType_ID <= 0) set_Value ("XX_VMR_DistributionType_ID", null);
        else
        set_Value ("XX_VMR_DistributionType_ID", Integer.valueOf(XX_VMR_DistributionType_ID));
        
    }
    
    /** Get Distribution Type.
    @return Distribution Type */
    public int getXX_VMR_DistributionType_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionType_ID");
        
    }
    
    /** Set XX_Void.
    @param XX_Void XX_Void */
    public void setXX_Void (String XX_Void)
    {
        set_Value ("XX_Void", XX_Void);
        
    }
    
    /** Get XX_Void.
    @return XX_Void */
    public String getXX_Void() 
    {
        return (String)get_Value("XX_Void");
        
    }
    
    /** Set Year.
    @param XX_Year Year */
    public void setXX_Year (int XX_Year)
    {
        set_Value ("XX_Year", Integer.valueOf(XX_Year));
        
    }
    
    /** Get Year.
    @return Year */
    public int getXX_Year() 
    {
        return get_ValueAsInt("XX_Year");
        
    }
    
    
}
