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
package org.compiere.swing;

import javax.swing.text.*;

/**
 * 	Max Length Text Document
 * 	@author Jorg Janke
 */
public class CTextFieldDocument extends PlainDocument
{
    private static final long serialVersionUID = 1991390646001244465L;

	/**
     * 	Max Length Text Document
     *	@param maxLength max length or 0 for none
     */
	public CTextFieldDocument(int maxLength)
    {
    	m_maxLength = maxLength;
    }

	/**	Length			*/
    private int m_maxLength = 0;

    /**
     * 	Set Max Length
     *	@param maxLength max length or 0 for none
     */
    public void setMaxLength (int maxLength)
    {
    	m_maxLength = maxLength;
    }	//setMaxLength

    /**
     * 	Get Max Length
     *	@return length
     */
    public int getMaxLength()
    {
    	return m_maxLength;
    }	//	getMaxLength

    /**
     * 	Enforce Length
     */
    @Override
    public void insertString(int offset, String str, AttributeSet a)
        throws BadLocationException
    {
        if (str != null && str.length() > 0)
        {
            super.insertString(offset, str, a);
			if (m_maxLength > 0 && getLength() > m_maxLength)
				super.remove(m_maxLength, getLength()-m_maxLength);
        }
    }	//	insertString

    /**
     * 	String Representation
     *	@return info
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer("CTextFieldDocument[")
        	.append(m_maxLength).append("]");
        return sb.toString();
    }	//	toString

}	//	CTextFieldDocument
