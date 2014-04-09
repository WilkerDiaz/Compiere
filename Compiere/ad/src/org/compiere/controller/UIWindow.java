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
package org.compiere.controller;

import java.util.*;

import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;


/**
 *	User Interface Window
 *
 *  @author Jorg Janke
 *  @version $Id: UIWindow.java 8653 2010-04-20 21:12:05Z ategawinata $
 */
public class UIWindow extends UIWindowVO
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	UI Window with Tabs and Fields
	 */
	public UIWindow(UIWindowVO vo)
	{
		super(vo);
	}	//	UIWindow

	/** The Context				*/
	//private CContext			m_ctx = null;
	/** The Fields				*/
	private ArrayList<UIField>	m_fields = null;
	/** Unique ID for Fields - Over 900k used by migration/new customers */
	private static int			s_AD_Field_ID = 800000;

    /**
     * 	Get Tabs
     *	@return tabs
     */
    public ArrayList<UITab> getTabs()
    {
    	if (p_vos == null)
    		return null;
    	ArrayList<UITab> retValue = new ArrayList<UITab>(p_vos.size());
    	for (int i = 0; i < p_vos.size(); i++)
		{
			UITab tab = (UITab)p_vos.get(i);
			retValue.add(tab);
		}
    	return retValue;
    }	//	getTabs

    /**
     * 	Set Tabs
     *	@param tabsVO tabs as VO
     *	@param WindowNo window
     */
    public void setTabVOsWithFieldsUninitialized(CContext ctx, ArrayList<UITabVO> tabsVO, int WindowNo)
    {
    	if (tabsVO == null)
    		p_vos = null;
    	else
    	{
        	p_vos = new ArrayList<VO>(tabsVO.size());
        	for (int i = 0; i < tabsVO.size(); i++)
        	{
        		UITab tab = new UITab(tabsVO.get(i), this);
        		ArrayList<UITab> previousTabs = getTabs();
        		if (tab.getReferenced_Tab_ID() != 0)
        		{
        			ArrayList<UIField> fields = getReferencedFields(ctx, tab.getReferenced_Tab_ID());
        			tab.initialize (fields, previousTabs, ctx, WindowNo, i, isSOTrx());
        		}
        		else
        		{
        			ArrayList<UIField> fields = getFields(ctx, tab);
        			tab.initialize (fields, previousTabs, ctx, WindowNo, i, isSOTrx());
        		}
        		p_vos.add (tab);
        	}
    	}
    }	//	setTabs

    /**
	 * 	Get All Fields
	 *	@return fields
	 */
    public ArrayList<UIField> getFields()
    {
    	return m_fields;
    }	//	getFields

    /**
	 * 	Get Tab Fields
	 * 	@param AD_Tab_ID tab
	 *	@return fields
	 */
    private ArrayList<UIField> getFields(CContext ctx, UITab tab)
    {
    	int AD_Tab_ID = tab.getAD_Tab_ID();
    	UIField created = null;
    	UIField createdBy = null;
    	UIField updated = null;
    	UIField updatedBy = null;
    	//
    	ArrayList<UIField> retValue = new ArrayList<UIField>();
    	for (int i = 0; i < m_fields.size(); i++)
        {
    		UIField field = m_fields.get(i);
    		//	Only for selected tab
    		if (field.getAD_Tab_ID() == AD_Tab_ID)
    		{
    			retValue.add(field);
    			String columnName = field.getColumnName();
    			if (columnName.equals("Created"))
    				created = field;
    			else if (columnName.equals("Updated"))
    				updated = field;
    			else if (columnName.equals("CreatedBy"))
    				createdBy = field;
    			else if (columnName.equals("UpdatedBy"))
    				updatedBy = field;
    		}
        }
    	if (!tab.isView()) {
        	if (created == null)
        		retValue.add(createStdColumn(ctx, AD_Tab_ID, true, false));
        	if (createdBy == null)
        		retValue.add(createStdColumn(ctx, AD_Tab_ID, true, true));
        	if (updated == null)
        		retValue.add(createStdColumn(ctx, AD_Tab_ID, false, false));
        	if (updatedBy == null)
        		retValue.add(createStdColumn(ctx, AD_Tab_ID, false, true));
    	}
    	//
    	return retValue;
    }	//	getFields

    /**
	 * 	Get referenced Tab Fields
	 * 	@param AD_Tab_ID referenced tab
	 *	@return fields
	 */
    private ArrayList<UIField> getReferencedFields(CContext ctx, int AD_Tab_ID)
    {
		UIFieldVOFactory fieldFactory = new UIFieldVOFactory();
		ArrayList<UIFieldVO> fields = fieldFactory.getReferenced(ctx, AD_Tab_ID, getAD_UserDef_Win_ID());
		//
		UIField created = null;
    	UIField createdBy = null;
    	UIField updated = null;
    	UIField updatedBy = null;
    	//
    	ArrayList<UIField> retValue = new ArrayList<UIField>();
    	for (int i = 0; i < fields.size(); i++)
        {
    		UIField field = new UIField(fields.get(i));
    		field.setAD_Window_ID(getAD_Window_ID());
    		//
   			retValue.add(field);
   			String columnName = field.getColumnName();
   			if (columnName.equals("Created"))
   				created = field;
   			else if (columnName.equals("Updated"))
   				updated = field;
   			else if (columnName.equals("CreatedBy"))
   				createdBy = field;
   			else if (columnName.equals("UpdatedBy"))
   				updatedBy = field;
   		}
    	if (created == null)
    		retValue.add(createStdColumn(ctx, AD_Tab_ID, true, false));
    	if (createdBy == null)
    		retValue.add(createStdColumn(ctx, AD_Tab_ID, true, true));
    	if (updated == null)
    		retValue.add(createStdColumn(ctx, AD_Tab_ID, false, false));
    	if (updatedBy == null)
    		retValue.add(createStdColumn(ctx, AD_Tab_ID, false, true));
    	//
    	return retValue;
    }	//	getReferencedFields

    /**
     * 	Create Standard Column
     *	@param AD_Tab_ID tab
     *	@param created false if updated
     *	@param by false if date
     *	@return field
     */
    private UIField createStdColumn(CContext ctx, int AD_Tab_ID, boolean created, boolean by)
    {
    	UIFieldVO vo = new UIFieldVO();
    	String columnName = created ? "Created" : "Updated";
    	if (by)
    		columnName += "By";
    	vo.setColumnName(columnName);
    	vo.setName(Msg.getElement(ctx, columnName));
    	//
    	vo.setIsDisplayed(false);
    	vo.setIsReadOnly(true);
    	//
    	if (by)
    	{
    		vo.setAD_Reference_ID(DisplayTypeConstants.Table);
    		int AD_Reference_Value_ID = 110;	//	AD_User
    		vo.setAD_Reference_Value_ID(AD_Reference_Value_ID);
    	}
    	else
    		vo.setAD_Reference_ID(DisplayTypeConstants.DateTime);
    	//
    	vo.setAD_Window_ID(getAD_Window_ID());
    	vo.setAD_Tab_ID(AD_Tab_ID);
    	//add a field id for standard columns, it is used by gwt client to locate the fields back
    	int AD_Field_ID = s_AD_Field_ID++;
    	vo.setAD_Field_ID(AD_Field_ID);
    	//
    	UIField ui = new UIField(vo);
    	return ui;
    }	//	createStdColumn

    /**
     * 	Set Fields
     *	@param fields fields
     */
    public void setFields(ArrayList<UIFieldVO> fields)
    {
    	m_fields = new ArrayList<UIField>(fields.size());
    	addFields(fields);
    }	//	setFields

    /**
     * 	Add Fields
     *	@param fields fields
     */
    public void addFields(ArrayList<UIFieldVO> fields)
    {
    	for (int i = 0; i < fields.size(); i++)
			m_fields.add (new UIField(fields.get(i)));
    }	//	setFields

    /**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("UIWindow[")
			.append (getName());
		if (p_vos != null)
			sb.append (";#Tabs=").append(p_vos.size());
		if (m_fields != null)
			sb.append (";#Fields=").append(m_fields.size());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * Get the index of the tab
	 * @param tab GridTab
	 * @return index of the tab
	 */
	public int getTabIndex(UITab tab)
	{
		return p_vos.indexOf(tab);
	}
	

	/**
	 *	Get i-th UITab - null if not valid
	 *  @param i index
	 *  @return UITab
	 */
	public UITab getTab (int i)
	{
		if (i < 0 || i+1 > p_vos.size())
			return null;
		return (UITab)p_vos.get(i);
	}	//	getTab

}	//	UIWindow
