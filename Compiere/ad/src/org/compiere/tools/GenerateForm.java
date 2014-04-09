/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.tools;

import java.util.*;

import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Generate HTML Form
 *	
 *  @author Jorg Janke
 *  @version $Id: GenerateForm.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class GenerateForm
{
	/**
	 * 	Generate Form
	 *	@param AD_Client_ID client for data
	 *	@param AD_Table_ID table
	 *	@param ignoreColumns columns to be ignored
	 */
	public GenerateForm (int AD_Client_ID, int AD_Table_ID, 
		String[] ignoreColumns, String[] hideColumns)
	{
		m_AD_Client_ID = AD_Client_ID;
		m_ignoreColumns = ignoreColumns;
		m_hideColumns = hideColumns;
		String s = createForm(AD_Table_ID);
		System.out.println(s);
	}	//	GenerateForm
	
	/**	Data Client				*/
	private int 		m_AD_Client_ID = 0;
	/** Columns to be ignored	*/
	private String[]	m_ignoreColumns = null;
	/** Columns to be hidden	*/
	private String[]	m_hideColumns = null;
	
	/**
	 * 	Create Form
	 *	@param AD_Table_ID
	 *	@return
	 */
	private String createForm (int AD_Table_ID)
	{
		MTable table = MTable.get(Env.getCtx(), AD_Table_ID);
		MColumn[] columns = table.getColumns(true);
		
		form f = new form("/wstore/", form.METHOD_POST.concat (form.ENC_DEFAULT));
		f.setID(table.getTableName());
		//
		table t = new table(0);
		for (MColumn element : columns)
			createColumn (f, t, element);
		
		//	Fini
		tr row = new tr();
		row.addElement (new input(input.TYPE_RESET, "Reset", "Reset"));
		row.addElement (new input(input.TYPE_SUBMIT, "Submit", "Submit"));
		t.addElement(row);
		f.addElement(t);
		return f.toString();
	}	//	createForm
	
	/**
	 * 	Create Column
	 *	@param f form
	 *	@param t table
	 *	@param column column
	 */
	private void createColumn (form f, table t, MColumn column)
	{
		String columnName = column.getColumnName();
		int dt = column.getAD_Reference_ID();

		//	Ignore
		if (m_ignoreColumns != null && m_ignoreColumns.length > 0)
		{
			int index = Arrays.binarySearch (m_ignoreColumns, columnName); 
			if (index > -1)
				return;
		}
		if (m_hideColumns != null && m_hideColumns.length > 0)
		{
			int index = Arrays.binarySearch (m_hideColumns, columnName); 
			if (index > -1)
			{
				f.addElement (new input(input.TYPE_HIDDEN, columnName, 0));
				return;
			}
		}
		//	Default Ignore
		if (columnName.startsWith("Created")
			|| columnName.startsWith("Updated")
			|| columnName.equals("IsActive")
			|| columnName.equals("Processed")
			|| dt == DisplayTypeConstants.Button
			)
			return;
		//	Default Hidden Columns
		if (columnName.equals ("AD_Client_ID")
			|| columnName.equals ("AD_Org_ID")
			)
		{
			f.addElement (new input(input.TYPE_HIDDEN, columnName, 0));
			return;
		}
		
		tr row = new tr();
		if (DisplayTypeConstants.YesNo == dt)
		{
			row.addElement(new td("&sbsp;"));
			input i = new input(input.TYPE_CHECKBOX, columnName, "").setChecked (true);
			row.addElement(new td(i)
				.setAlign(AlignType.LEFT)
				.setVAlign(AlignType.TOP));
		}
		else
		{
			createLabel (row, column);
			createField (row, column);
		}
		//
		t.addElement(row);
	}	//	createColumn

	/**
	 * 	Create Label
	 *	@param row row
	 *	@param column column
	 */
	private void createLabel (tr row, MColumn column)
	{
		M_Element element = column.getElement();
		label myLabel = new label(column.getColumnName(), null, Util.maskHTML(element.getName()));
		myLabel.setID(column.getColumnName() + "L");
		if (element.getDescription() != null)
			myLabel.setTitle(Util.maskHTML(element.getDescription()));
		row.addElement(new td(myLabel)
			.setAlign(AlignType.RIGHT)
			.setVAlign(AlignType.TOP));
	}	//	createLabel

	/**
	 * 	Create Field
	 *	@param row row
	 *	@param column column
	 */
	private void createField (tr row, MColumn column)
	{
		String columnName = column.getColumnName();
		int dt = column.getAD_Reference_ID();
		Element e = null;
		//	String
		if (FieldType.isText(dt))
		{
			if (column.getFieldLength() > 60)
				e = new textarea(columnName, 3, 60);
			else if (column.getFieldLength() > 255)
				e = new textarea(columnName, 5, 60);
			else
			{
				int size = column.getFieldLength();
				if (size > 60)
					size = 60;
				e = new input (input.TYPE_TEXT, columnName, "").setSize(size);
			}
		}
		//	Date
		else if (FieldType.isDate (dt))
			e = new input (input.TYPE_TEXT, columnName, "");
		//	Number
		else if (FieldType.isNumeric (dt))
			e = new input (input.TYPE_TEXT, columnName, "");
		//
		else if (DisplayTypeConstants.List == dt)
		{
			ValueNamePair[] options = MRefList.getList (Env.getCtx(),column.getAD_Reference_Value_ID(), column.isMandatoryUI());
			e = new select(columnName, getOptions(options, column.isMandatoryUI()));
		}
		else if (FieldType.isLookup (dt))
		{
			MTable table = column.getFKTable();
			if (table != null)
			{
				String displayColumn = null;
				String[] ids = table.getIdentifierColumns();
				if (ids.length > 0)
					displayColumn = ids[0];
				else
					displayColumn = columnName;
				String where = "AD_Client_ID IN (0," + m_AD_Client_ID + ")";
				PO[] options = table.getPOs (Env.getCtx(), where, displayColumn, null);
				e = new select(columnName, getOptions(options, displayColumn, column.isMandatoryUI()));
			}
		}

		if (e != null)
			row.addElement(new td(e)
				.setAlign(AlignType.LEFT)
				.setVAlign(AlignType.TOP));
	}	//	createField
	
	/**
	 * 	Get Options
	 *	@param in in
	 *	@param mandatory mandatory list
	 *	@return options
	 */
	private option[] getOptions(ValueNamePair[] in, boolean mandatory)
	{
		option[] out = null;
		int add = 0;
		if (!mandatory)
		{
			out = new option[in.length+1];
			out[0] = new option("", "");
			add = 1;
		}
		else
			out = new option[in.length];
		//
		for (int i = 0; i < in.length; i++)
			out[i+add] = new option(in[i].getValue())
				.addElement(in[i].getName());
		return out;
	}	//	getOptions
	
	/**
	 * 	Get Options
	 *	@param in in
	 *	@param mandatory mandatory list
	 *	@return options
	 */
	private option[] getOptions(PO[] in, String displayColumn, boolean mandatory)
	{
		option[] out = null;
		StringBuffer pairs = new StringBuffer();
		if (in.length > 0)
			pairs.append(in[0].get_TableName()).append("{");
		int add = 0;
		if (!mandatory)
		{
			out = new option[in.length+1];
			out[0] = new option("", "");
			add = 1;
			pairs.append("new KeyNamePair(0, \"\"),")
				.append(Env.NL);
		}
		else
		{
			out = new option[in.length];
		}
		//
		for (int i = 0; i < in.length; i++)
		{
			Object display = in[i].get_Value(displayColumn);
			if (display == null)
				display = "?";
			Integer id = in[i].get_ID();
			out[i+add] = new option(id.toString())
				.addElement(display.toString());
			pairs.append("new KeyNamePair(").append(id).append(", \"").append(display).append("\"),")
				.append(Env.NL);
		}
		pairs.append("};");
		
		return out;
	}	//	getOptions
	
	/**
	 * 	main
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup (true, "GenerateForm");
		String[] ignoreColumns = new String[]
		{
			"AD_User_ID", "C_BPartner_ID", "C_BPartner_Location_ID",
			"SalesRep_ID", "C_BP_Status_ID",
			"C_LeadQualification_ID", "LeadRating", "R_Status_ID",
			"C_Region_ID", "C_City_ID", "C_SalesRegion_ID",
			"C_Project_ID", "R_Request_ID", "DocumentNo",
			"C_Lead_ID", "C_Project_ID"
		};
		String[] hideColumns = new String[]
		{
			"R_InterestArea_ID", "R_Source_ID", "C_BPartnerSR_ID",
			"C_Campaign_ID"
		};
	//	int AD_Client_ID = 11;	//	GardenWorld
		int AD_Client_ID = 1000000;
		new GenerateForm (AD_Client_ID, 923, ignoreColumns, hideColumns);
	}	//	main
	
}	//	GenerateForm
