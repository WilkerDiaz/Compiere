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
/** Generated Model for AD_PrintLabelLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintLabelLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintLabelLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintLabelLine_ID id
    @param trx transaction
    */
    public X_AD_PrintLabelLine (Ctx ctx, int AD_PrintLabelLine_ID, Trx trx)
    {
        super (ctx, AD_PrintLabelLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintLabelLine_ID == 0)
        {
            setAD_LabelPrinterFunction_ID (0);
            setAD_PrintLabelLine_ID (0);
            setAD_PrintLabel_ID (0);
            setLabelFormatType (null);	// F
            setName (null);
            setSeqNo (0);
            setXPosition (0);
            setYPosition (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintLabelLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=569 */
    public static final int Table_ID=569;
    
    /** TableName=AD_PrintLabelLine */
    public static final String Table_Name="AD_PrintLabelLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Label printer Function.
    @param AD_LabelPrinterFunction_ID Function of Label Printer */
    public void setAD_LabelPrinterFunction_ID (int AD_LabelPrinterFunction_ID)
    {
        if (AD_LabelPrinterFunction_ID < 1) throw new IllegalArgumentException ("AD_LabelPrinterFunction_ID is mandatory.");
        set_Value ("AD_LabelPrinterFunction_ID", Integer.valueOf(AD_LabelPrinterFunction_ID));
        
    }
    
    /** Get Label printer Function.
    @return Function of Label Printer */
    public int getAD_LabelPrinterFunction_ID() 
    {
        return get_ValueAsInt("AD_LabelPrinterFunction_ID");
        
    }
    
    /** Set Print Label Line.
    @param AD_PrintLabelLine_ID Print Label Line Format */
    public void setAD_PrintLabelLine_ID (int AD_PrintLabelLine_ID)
    {
        if (AD_PrintLabelLine_ID < 1) throw new IllegalArgumentException ("AD_PrintLabelLine_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintLabelLine_ID", Integer.valueOf(AD_PrintLabelLine_ID));
        
    }
    
    /** Get Print Label Line.
    @return Print Label Line Format */
    public int getAD_PrintLabelLine_ID() 
    {
        return get_ValueAsInt("AD_PrintLabelLine_ID");
        
    }
    
    /** Set Print Label.
    @param AD_PrintLabel_ID Label Format to print */
    public void setAD_PrintLabel_ID (int AD_PrintLabel_ID)
    {
        if (AD_PrintLabel_ID < 1) throw new IllegalArgumentException ("AD_PrintLabel_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintLabel_ID", Integer.valueOf(AD_PrintLabel_ID));
        
    }
    
    /** Get Print Label.
    @return Label Format to print */
    public int getAD_PrintLabel_ID() 
    {
        return get_ValueAsInt("AD_PrintLabel_ID");
        
    }
    
    /** Field = F */
    public static final String LABELFORMATTYPE_Field = X_Ref_AD_Print_Label_Line_Type.FIELD.getValue();
    /** Text = T */
    public static final String LABELFORMATTYPE_Text = X_Ref_AD_Print_Label_Line_Type.TEXT.getValue();
    /** Set Label Format Type.
    @param LabelFormatType Label Format Type */
    public void setLabelFormatType (String LabelFormatType)
    {
        if (LabelFormatType == null) throw new IllegalArgumentException ("LabelFormatType is mandatory");
        if (!X_Ref_AD_Print_Label_Line_Type.isValid(LabelFormatType))
        throw new IllegalArgumentException ("LabelFormatType Invalid value - " + LabelFormatType + " - Reference_ID=280 - F - T");
        set_Value ("LabelFormatType", LabelFormatType);
        
    }
    
    /** Get Label Format Type.
    @return Label Format Type */
    public String getLabelFormatType() 
    {
        return (String)get_Value("LabelFormatType");
        
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
    
    /** Set Print Text.
    @param PrintName The label text to be printed on a document or correspondence. */
    public void setPrintName (String PrintName)
    {
        set_Value ("PrintName", PrintName);
        
    }
    
    /** Get Print Text.
    @return The label text to be printed on a document or correspondence. */
    public String getPrintName() 
    {
        return (String)get_Value("PrintName");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    /** Set X Position.
    @param XPosition Absolute X (horizontal) position in 1/72 of an inch */
    public void setXPosition (int XPosition)
    {
        set_Value ("XPosition", Integer.valueOf(XPosition));
        
    }
    
    /** Get X Position.
    @return Absolute X (horizontal) position in 1/72 of an inch */
    public int getXPosition() 
    {
        return get_ValueAsInt("XPosition");
        
    }
    
    /** Set Y Position.
    @param YPosition Absolute Y (vertical) position in 1/72 of an inch */
    public void setYPosition (int YPosition)
    {
        set_Value ("YPosition", Integer.valueOf(YPosition));
        
    }
    
    /** Get Y Position.
    @return Absolute Y (vertical) position in 1/72 of an inch */
    public int getYPosition() 
    {
        return get_ValueAsInt("YPosition");
        
    }
    
    
}
