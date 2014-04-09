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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for C_DocType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_DocType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_DocType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_DocType_ID id
    @param trx transaction
    */
    public X_C_DocType (Ctx ctx, int C_DocType_ID, Trx trx)
    {
        super (ctx, C_DocType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_DocType_ID == 0)
        {
            setC_DocType_ID (0);
            setDocBaseType (null);
            setDocumentCopies (0);	// 1
            setGL_Category_ID (0);
            setHasCharges (false);
            setIsCreateCounter (true);	// Y
            setIsDefault (false);
            setIsDefaultCounterDoc (false);
            setIsDocNoControlled (true);	// Y
            setIsInTransit (false);
            setIsIndexed (false);
            setIsPickQAConfirm (false);
            setIsReturnTrx (false);	// N
            setIsSOTrx (false);
            setIsShipConfirm (false);
            setIsSplitWhenDifference (false);	// N
            setName (null);
            setPrintName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_DocType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346342789L;
    /** Last Updated Timestamp 2008-11-20 14:43:46.0 */
    public static final long updatedMS = 1227221026000L;
    /** AD_Table_ID=217 */
    public static final int Table_ID=217;
    
    /** TableName=C_DocType */
    public static final String Table_Name="C_DocType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Print Format.
    @param AD_PrintFormat_ID Data Print Format */
    public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
    {
        if (AD_PrintFormat_ID <= 0) set_Value ("AD_PrintFormat_ID", null);
        else
        set_Value ("AD_PrintFormat_ID", Integer.valueOf(AD_PrintFormat_ID));
        
    }
    
    /** Get Print Format.
    @return Data Print Format */
    public int getAD_PrintFormat_ID() 
    {
        return get_ValueAsInt("AD_PrintFormat_ID");
        
    }
    
    /** Set Difference Document.
    @param C_DocTypeDifference_ID Document type for generating in dispute Shipments */
    public void setC_DocTypeDifference_ID (int C_DocTypeDifference_ID)
    {
        if (C_DocTypeDifference_ID <= 0) set_Value ("C_DocTypeDifference_ID", null);
        else
        set_Value ("C_DocTypeDifference_ID", Integer.valueOf(C_DocTypeDifference_ID));
        
    }
    
    /** Get Difference Document.
    @return Document type for generating in dispute Shipments */
    public int getC_DocTypeDifference_ID() 
    {
        return get_ValueAsInt("C_DocTypeDifference_ID");
        
    }
    
    /** Set Document Type for Invoice.
    @param C_DocTypeInvoice_ID Document type used for invoices generated from this sales document */
    public void setC_DocTypeInvoice_ID (int C_DocTypeInvoice_ID)
    {
        if (C_DocTypeInvoice_ID <= 0) set_Value ("C_DocTypeInvoice_ID", null);
        else
        set_Value ("C_DocTypeInvoice_ID", Integer.valueOf(C_DocTypeInvoice_ID));
        
    }
    
    /** Get Document Type for Invoice.
    @return Document type used for invoices generated from this sales document */
    public int getC_DocTypeInvoice_ID() 
    {
        return get_ValueAsInt("C_DocTypeInvoice_ID");
        
    }
    
    /** Set Document Type for ProForma.
    @param C_DocTypeProforma_ID Document type used for pro forma invoices generated from this sales document */
    public void setC_DocTypeProforma_ID (int C_DocTypeProforma_ID)
    {
        if (C_DocTypeProforma_ID <= 0) set_Value ("C_DocTypeProforma_ID", null);
        else
        set_Value ("C_DocTypeProforma_ID", Integer.valueOf(C_DocTypeProforma_ID));
        
    }
    
    /** Get Document Type for ProForma.
    @return Document type used for pro forma invoices generated from this sales document */
    public int getC_DocTypeProforma_ID() 
    {
        return get_ValueAsInt("C_DocTypeProforma_ID");
        
    }
    
    /** Set Document Type for Shipment.
    @param C_DocTypeShipment_ID Document type used for shipments generated from this sales document */
    public void setC_DocTypeShipment_ID (int C_DocTypeShipment_ID)
    {
        if (C_DocTypeShipment_ID <= 0) set_Value ("C_DocTypeShipment_ID", null);
        else
        set_Value ("C_DocTypeShipment_ID", Integer.valueOf(C_DocTypeShipment_ID));
        
    }
    
    /** Get Document Type for Shipment.
    @return Document type used for shipments generated from this sales document */
    public int getC_DocTypeShipment_ID() 
    {
        return get_ValueAsInt("C_DocTypeShipment_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
        set_ValueNoCheck ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
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
    
    /** Set Document BaseType.
    @param DocBaseType Logical type of document */
    public void setDocBaseType (String DocBaseType)
    {
        set_Value ("DocBaseType", DocBaseType);
        
    }
    
    /** Get Document BaseType.
    @return Logical type of document */
    public String getDocBaseType() 
    {
        return (String)get_Value("DocBaseType");
        
    }
    
    /** Set Document Sequence.
    @param DocNoSequence_ID Document sequence determines the numbering of documents */
    public void setDocNoSequence_ID (int DocNoSequence_ID)
    {
        if (DocNoSequence_ID <= 0) set_Value ("DocNoSequence_ID", null);
        else
        set_Value ("DocNoSequence_ID", Integer.valueOf(DocNoSequence_ID));
        
    }
    
    /** Get Document Sequence.
    @return Document sequence determines the numbering of documents */
    public int getDocNoSequence_ID() 
    {
        return get_ValueAsInt("DocNoSequence_ID");
        
    }
    
    /** Quotation = OB */
    public static final String DOCSUBTYPESO_Quotation = X_Ref_C_DocType_SubTypeSO.QUOTATION.getValue();
    /** Proposal = ON */
    public static final String DOCSUBTYPESO_Proposal = X_Ref_C_DocType_SubTypeSO.PROPOSAL.getValue();
    /** Prepay Order = PR */
    public static final String DOCSUBTYPESO_PrepayOrder = X_Ref_C_DocType_SubTypeSO.PREPAY_ORDER.getValue();
    /** Standard Order = SO */
    public static final String DOCSUBTYPESO_StandardOrder = X_Ref_C_DocType_SubTypeSO.STANDARD_ORDER.getValue();
    /** On Credit Order = WI */
    public static final String DOCSUBTYPESO_OnCreditOrder = X_Ref_C_DocType_SubTypeSO.ON_CREDIT_ORDER.getValue();
    /** Warehouse Order = WP */
    public static final String DOCSUBTYPESO_WarehouseOrder = X_Ref_C_DocType_SubTypeSO.WAREHOUSE_ORDER.getValue();
    /** POS Order = WR */
    public static final String DOCSUBTYPESO_POSOrder = X_Ref_C_DocType_SubTypeSO.POS_ORDER.getValue();
    /** Set SO Sub Type.
    @param DocSubTypeSO Sales Order Sub Type */
    public void setDocSubTypeSO (String DocSubTypeSO)
    {
        if (!X_Ref_C_DocType_SubTypeSO.isValid(DocSubTypeSO))
        throw new IllegalArgumentException ("DocSubTypeSO Invalid value - " + DocSubTypeSO + " - Reference_ID=148 - OB - ON - PR - SO - WI - WP - WR");
        set_Value ("DocSubTypeSO", DocSubTypeSO);
        
    }
    
    /** Get SO Sub Type.
    @return Sales Order Sub Type */
    public String getDocSubTypeSO() 
    {
        return (String)get_Value("DocSubTypeSO");
        
    }
    
    /** Set Document Copies.
    @param DocumentCopies Number of additional copies to be printed */
    public void setDocumentCopies (int DocumentCopies)
    {
        set_Value ("DocumentCopies", Integer.valueOf(DocumentCopies));
        
    }
    
    /** Get Document Copies.
    @return Number of additional copies to be printed */
    public int getDocumentCopies() 
    {
        return get_ValueAsInt("DocumentCopies");
        
    }
    
    /** Set Document Note.
    @param DocumentNote Additional information for a Document */
    public void setDocumentNote (String DocumentNote)
    {
        set_Value ("DocumentNote", DocumentNote);
        
    }
    
    /** Get Document Note.
    @return Additional information for a Document */
    public String getDocumentNote() 
    {
        return (String)get_Value("DocumentNote");
        
    }
    
    /** Set GL Category.
    @param GL_Category_ID General Ledger Category */
    public void setGL_Category_ID (int GL_Category_ID)
    {
        if (GL_Category_ID < 1) throw new IllegalArgumentException ("GL_Category_ID is mandatory.");
        set_Value ("GL_Category_ID", Integer.valueOf(GL_Category_ID));
        
    }
    
    /** Get GL Category.
    @return General Ledger Category */
    public int getGL_Category_ID() 
    {
        return get_ValueAsInt("GL_Category_ID");
        
    }
    
    /** Set Charges.
    @param HasCharges Charges can be added to the document */
    public void setHasCharges (boolean HasCharges)
    {
        set_Value ("HasCharges", Boolean.valueOf(HasCharges));
        
    }
    
    /** Get Charges.
    @return Charges can be added to the document */
    public boolean isHasCharges() 
    {
        return get_ValueAsBoolean("HasCharges");
        
    }
    
    /** Set Pro forma Invoice.
    @param HasProforma Indicates if Pro Forma Invoices can be generated from this document */
    public void setHasProforma (boolean HasProforma)
    {
        set_Value ("HasProforma", Boolean.valueOf(HasProforma));
        
    }
    
    /** Get Pro forma Invoice.
    @return Indicates if Pro Forma Invoices can be generated from this document */
    public boolean isHasProforma() 
    {
        return get_ValueAsBoolean("HasProforma");
        
    }
    
    /** Set Create Counter Document.
    @param IsCreateCounter Create Counter Document */
    public void setIsCreateCounter (boolean IsCreateCounter)
    {
        set_Value ("IsCreateCounter", Boolean.valueOf(IsCreateCounter));
        
    }
    
    /** Get Create Counter Document.
    @return Create Counter Document */
    public boolean isCreateCounter() 
    {
        return get_ValueAsBoolean("IsCreateCounter");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set Default Counter Document.
    @param IsDefaultCounterDoc The document type is the default counter document type */
    public void setIsDefaultCounterDoc (boolean IsDefaultCounterDoc)
    {
        set_Value ("IsDefaultCounterDoc", Boolean.valueOf(IsDefaultCounterDoc));
        
    }
    
    /** Get Default Counter Document.
    @return The document type is the default counter document type */
    public boolean isDefaultCounterDoc() 
    {
        return get_ValueAsBoolean("IsDefaultCounterDoc");
        
    }
    
    /** Set Document is Number Controlled.
    @param IsDocNoControlled The document has a document sequence */
    public void setIsDocNoControlled (boolean IsDocNoControlled)
    {
        set_Value ("IsDocNoControlled", Boolean.valueOf(IsDocNoControlled));
        
    }
    
    /** Get Document is Number Controlled.
    @return The document has a document sequence */
    public boolean isDocNoControlled() 
    {
        return get_ValueAsBoolean("IsDocNoControlled");
        
    }
    
    /** Set In Transit.
    @param IsInTransit Movement is in transit */
    public void setIsInTransit (boolean IsInTransit)
    {
        set_Value ("IsInTransit", Boolean.valueOf(IsInTransit));
        
    }
    
    /** Get In Transit.
    @return Movement is in transit */
    public boolean isInTransit() 
    {
        return get_ValueAsBoolean("IsInTransit");
        
    }
    
    /** Set Indexed.
    @param IsIndexed Index the document for the internal search engine */
    public void setIsIndexed (boolean IsIndexed)
    {
        set_Value ("IsIndexed", Boolean.valueOf(IsIndexed));
        
    }
    
    /** Get Indexed.
    @return Index the document for the internal search engine */
    public boolean isIndexed() 
    {
        return get_ValueAsBoolean("IsIndexed");
        
    }
    
    /** Set Pick/QA Confirmation.
    @param IsPickQAConfirm Require Pick or QA Confirmation before processing */
    public void setIsPickQAConfirm (boolean IsPickQAConfirm)
    {
        set_Value ("IsPickQAConfirm", Boolean.valueOf(IsPickQAConfirm));
        
    }
    
    /** Get Pick/QA Confirmation.
    @return Require Pick or QA Confirmation before processing */
    public boolean isPickQAConfirm() 
    {
        return get_ValueAsBoolean("IsPickQAConfirm");
        
    }
    
    /** Set Return Transaction.
    @param IsReturnTrx This is a return transaction */
    public void setIsReturnTrx (boolean IsReturnTrx)
    {
        set_Value ("IsReturnTrx", Boolean.valueOf(IsReturnTrx));
        
    }
    
    /** Get Return Transaction.
    @return This is a return transaction */
    public boolean isReturnTrx() 
    {
        return get_ValueAsBoolean("IsReturnTrx");
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
    }
    
    /** Set Ship/Receipt Confirmation.
    @param IsShipConfirm Require Ship or Receipt Confirmation before processing */
    public void setIsShipConfirm (boolean IsShipConfirm)
    {
        set_Value ("IsShipConfirm", Boolean.valueOf(IsShipConfirm));
        
    }
    
    /** Get Ship/Receipt Confirmation.
    @return Require Ship or Receipt Confirmation before processing */
    public boolean isShipConfirm() 
    {
        return get_ValueAsBoolean("IsShipConfirm");
        
    }
    
    /** Set Split when Difference.
    @param IsSplitWhenDifference Split document when there is a difference */
    public void setIsSplitWhenDifference (boolean IsSplitWhenDifference)
    {
        set_Value ("IsSplitWhenDifference", Boolean.valueOf(IsSplitWhenDifference));
        
    }
    
    /** Get Split when Difference.
    @return Split document when there is a difference */
    public boolean isSplitWhenDifference() 
    {
        return get_ValueAsBoolean("IsSplitWhenDifference");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Print Text.
    @param PrintName The label text to be printed on a document or correspondence. */
    public void setPrintName (String PrintName)
    {
        if (PrintName == null) throw new IllegalArgumentException ("PrintName is mandatory.");
        set_Value ("PrintName", PrintName);
        
    }
    
    /** Get Print Text.
    @return The label text to be printed on a document or correspondence. */
    public String getPrintName() 
    {
        return (String)get_Value("PrintName");
        
    }
    
    
}
