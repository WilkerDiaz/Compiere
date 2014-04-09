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
package org.compiere.framework;


/**
 *  Info Column Details
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Info_Column.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class Info_Column implements Comparable<Info_Column>
{
	/**
	 *  Create Info Column (r/o and not color column)
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 */
	public Info_Column (String colHeader, String colSQL, Class<?> colClass)
	{
		this(colHeader, colSQL, colClass, true, false, null);
	}   //  Info_Column

	/**
	 *  Create Info Column (r/o and not color column)
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 *  @param IDcolSQL  SQL select for the ID of the for the displayed column (KeyNamePair)
	 */
	public Info_Column (String colHeader, String colSQL, Class<?> colClass, String IDcolSQL)
	{
		this(colHeader, colSQL, colClass, true, false, IDcolSQL);
	}   //  Info_Column

	/**
	 *  Create Info Column
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 *  @param readOnly  column is read only
	 *  @param colorColumn   if true, value of column determines foreground color
	 *  @param IDcolSQL  SQL select for the ID of the for the displayed column
	 */
	public Info_Column (String colHeader, String colSQL, Class<?> colClass, 
		boolean readOnly, boolean colorColumn, String IDcolSQL)
	{
		setColHeader(colHeader);
		setColSQL(colSQL);
		setColClass(colClass);
		setReadOnly(readOnly);
		setColorColumn(colorColumn);
		setIDcolSQL(IDcolSQL);
	}   //  Info_Column


	private String      m_colHeader;
	private String      m_colSQL;
	private Class<?>    m_colClass;
	private boolean     m_readOnly;
	private boolean     m_colorColumn;
	private String      m_IDcolSQL = "";
	private int			m_sequence = 0;
	private Integer		m_width = null;

	/**
	 * 	Set Sequence
	 *	@param sequence sequence no
	 *	@return this
	 */
	public Info_Column seq (int sequence)
	{
		m_sequence = sequence;
		return this;
	}	//	seq
	
	/**
	 * 	Get Sequence
	 *	@return sequence
	 */
	public int getSequence()
	{
		return m_sequence;
	}	//	getSequence
	
	/**
	 * 	CompareTo
	 *	@param o other
	 *	@return
	 */
	public int compareTo(Info_Column oo)
	{
		Integer ii = Integer.valueOf(m_sequence);
		return ii.compareTo(Integer.valueOf(oo.getSequence()));
	}	//	compareTo
	
	public Class<?> getColClass()
	{
		return m_colClass;
	}
	public String getColHeader()
	{
		return m_colHeader;
	}
	public String getColSQL()
	{
		return m_colSQL;
	}
	public boolean isReadOnly()
	{
		return m_readOnly;
	}
	public void setColClass(Class<?> colClass)
	{
		m_colClass = colClass;
	}
	public void setColHeader(String colHeader)
	{
		m_colHeader = colHeader;
		if (colHeader != null)
		{
			int index = colHeader.indexOf('&');
			if (index != -1)
				m_colHeader = colHeader.substring(0, index) + colHeader.substring(index+1); 
		}
	}
	public void setColSQL(String colSQL)
	{
		m_colSQL = colSQL;
	}
	public void setReadOnly(boolean readOnly)
	{
		m_readOnly = readOnly;
	}
	public void setColorColumn(boolean colorColumn)
	{
		m_colorColumn = colorColumn;
	}
	public boolean isColorColumn()
	{
		return m_colorColumn;
	}
	/**
	 *  Add ID column SQL for the displayed column
	 *  The Class for this should be KeyNamePair
	 */
	public void setIDcolSQL(String IDcolSQL)
	{
		m_IDcolSQL = IDcolSQL;
		if (m_IDcolSQL == null)
			m_IDcolSQL = "";
	}
	public String getIDcolSQL()
	{
		return m_IDcolSQL;
	}
	public boolean isIDcol()
	{
		return m_IDcolSQL.length() > 0;
	}
	
	/**
	 * 	Set Width in pixels
	 *	@param width width
	 */
	public void setWidth (Integer width)
	{
		m_width = width;
	}

	/**
	 * 	Get Optional Width in pixels
	 *	@return pixels or null
	 */
	public Integer getWidth()
	{
		return m_width;
	}
}   //  infoColumn
