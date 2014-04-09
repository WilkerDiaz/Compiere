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
package org.compiere.grid.ed;

import javax.swing.event.*;
import javax.swing.text.*;

import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	String Input Verification.
 *  <pre>
 *	-	Length is set to length of VFormat or FieldLength if no format defined
 *	Control Characters
 *			(Space) any character
 *		_	Space (fixed character)
 *
 *		l	any Letter a..Z NO space
 *		L	any Letter a..Z NO space converted to upper case
 *		o	any Letter a..Z or space
 *		O	any Letter a..Z or space converted to upper case
 *
 *		0	Digits 0..9 NO space
 *      9   Digits 0..9 or space
 *
 *		a	any Letters & Digits NO space
 *		A	any Letters & Digits NO space converted to upper case
 *		c	any Letters & Digits or space
 *		C	any Letters & Digits or space converted to upper case
 *  </pre>
 *  @see VString
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MDocString.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public final class MDocString extends CTextFieldDocument implements CaretListener
{
    private static final long serialVersionUID = -1256771210202228487L;

	/**
	 *	Constructor
	 *  @param VFormat
	 *  @param fieldLength
	 *  @param tc VPassword or VString
	 */
	public MDocString(String VFormat, int fieldLength, JTextComponent tc)
	{
		super(fieldLength);
		setFormat (VFormat);
		m_tc = tc;
		if (tc != null)
			m_tc.addCaretListener(this);
	}	//	MDocNumber

	private String 			m_VFormat;		//	Value Format String
	private String 			m_mask;			//	Fixed Elements
	private int				m_maxLength;
	private final JTextComponent 	m_tc;
	private int				m_lastDot = 0;	//	last dot position
	//
	private static final char SPACE = ' ';
	private static final char SPACE_IND = '_';
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MDocString.class);

	/**
	 *	Set Format
	 *  @param VFormat
	 */
	public final void setFormat (String VFormat)
	{
		m_VFormat = VFormat;
		if (m_VFormat == null)
			m_VFormat = "";
		m_mask = m_VFormat;
		//	Set Length
		m_maxLength = getMaxLength();
		if (m_VFormat.length() == 0)
			return;
		//
		log.fine(VFormat);
		if (m_maxLength > m_VFormat.length())
			m_maxLength = m_VFormat.length();

		//	Create Mask
		m_mask = m_mask.replace('c', SPACE);		//	c	any Letter or Digit or space
		m_mask = m_mask.replace('C', SPACE);		//	C	any Letter or Digit or space converted to upper case
		m_mask = m_mask.replace('a', SPACE);		//	a	any Letters or Digits NO space
		m_mask = m_mask.replace('A', SPACE);		//	A	any Letters or Digits NO space converted to upper case
		m_mask = m_mask.replace('l', SPACE);		//	l	any Letter a..Z NO space
		m_mask = m_mask.replace('L', SPACE);		//	L	any Letter a..Z NO space converted to upper case
		m_mask = m_mask.replace('o', SPACE);		//	o	any Letter a..Z or space
		m_mask = m_mask.replace('O', SPACE);		//	O	any Letter a..Z or space converted to upper case
		m_mask = m_mask.replace('0', SPACE);		//	0	Digits 0..9 NO space
		m_mask = m_mask.replace('9', SPACE);		//	9	Digits 0..9 or space

		//	Check Caret
		if (m_tc == null || m_tc.getCaret() instanceof VOvrCaret)
			return;
		else
			m_tc.setCaret(new VOvrCaret());
	}	//	setFormat

	/**
	 *	Insert String
	 *  @param offset
	 *  @param string
	 *  @param attr
	 *  @throws BadLocationException
	 */
	@Override
	public void insertString(int offset, String string, AttributeSet attr)
		throws BadLocationException
	{
		//	Max Length
		if (offset >= m_maxLength)
			return;
		//	We have no Format or inserted not manually (assuming correct Format)
		if (m_VFormat.length() == 0)
		{
			log.finest("Offset=" + offset + " String=" + string
				+ ", Text=" + getText() + ", Length=" + getText().length());
			super.insertString(offset, string, attr);
			if (m_maxLength > 0 && getLength() > m_maxLength)
				super.remove(m_maxLength, getLength()-m_maxLength);
			return;
		}

		/**	Formating required **/
		log.finer("Offset=" + offset
			+ ", String=" + string + ", MaxLength=" + m_maxLength
			+ ", Format=" + m_VFormat + ", Mask=" + m_mask
			+ ", Text=" + getText() + ", Length=" + getText().length());
		/** **/
		String text = getText();

		//	Apply Mask, if not target length
		if (m_VFormat.length() != text.length())
		{
			char[] result = m_mask.toCharArray();
			for (int i = 0; i < result.length; i++)
			{
				if (result[i] == SPACE && text.length() > i)
					result[i] = text.charAt(i);
				else if (result[i] == SPACE_IND)
					result[i] = SPACE;
			}
			//
			super.remove(0, text.length());
			super.insertString(0, new String(result), attr);
			m_tc.setCaretPosition(offset);
			text = getText();
		}

		//	positioned before a mask character - jump over it
		if (offset+1 < text.length() && m_mask.charAt(offset+1) != SPACE)
			if (offset+2 < getText().length())
				m_tc.setCaretPosition(offset+2);

		//	positioned at the mask character
		if (m_mask.charAt(offset) != SPACE)
		{
			if (offset+1 == m_mask.length())
				return;
			offset++;
			m_tc.setCaretPosition(offset+1);
		}

		int i = 0;
		StringBuffer newString = new StringBuffer();
		for (i = 0; i < string.length() && i + offset < m_maxLength; ++i)
		{
			//	Conversion
			char c = string.charAt(i);
			char cmd = m_VFormat.charAt(i+offset);

			VFormatValidator val = VFormatValidator.s_formatChars.get(cmd);
			log.finer("c=" + c + ", cmd=" + cmd + " Val=" + val);

			if (val == null)
				newString.append(cmd);
			else
			{
				if (!val.isValid(c))
				{
					if (string.length() == 1)	//	single char entry
						return;
					else						//	display existing is invalid -> default
						newString.append(val.getDefault());
				}
				newString.append(val.getConvertedValue(c));
			}
		}

		super.remove(offset, string.length());	//	replace current position
		super.insertString(offset, newString.toString(), attr);
	}	//	insertString

	/**
	 *	Delete String
	 *  @param offset
	 *  @param length
	 *  @throws BadLocationException
	 */
	@Override
	public void remove (int offset, int length)
		throws BadLocationException
	{
		//	No format or non manual entry
		if (m_VFormat.length() == 0)
		{
			log.finest("Offset=" + offset + " Length=" + length);
			super.remove(offset, length);
			return;
		}


		//	begin of string
		if (offset == 0)
		{
			//	empty the field
			if (length == m_mask.length()){
				super.remove(offset, length);
				return;
			}
		}

		if( offset >= m_VFormat.length() )
		{
			super.remove(offset, length);
			return;
		}

		char replacement = ' ';
		char cmd = m_VFormat.charAt(offset);
		VFormatValidator val = VFormatValidator.s_formatChars.get(cmd);
		if( val != null )
			replacement = val.getDefault();

		super.remove(offset, 1);
		super.insertString(offset, Character.toString(replacement), null);

		//	one position behind delimiter
		if (offset-1 >= 0 && m_mask.charAt(offset-1) != SPACE)
		{
			if (offset-2 >= 0)
				m_tc.setCaretPosition(offset-1);
			else
				return;
		}
		else
			m_tc.setCaretPosition(offset);
	}	//	deleteString

	/**
	 *	Get Full Text
	 *  @return text
	 */
	private String getText()
	{
		String str = "";
		try
		{
			str = getContent().getString(0, getContent().length()-1);		//	cr at end
		}
		catch (Exception e)
		{
			str = "";
		}
		return str;
	}	//	getString


	/**************************************************************************
	 *	Caret Listener
	 *  @param e
	 */
	public void caretUpdate(CaretEvent e)
	{
		//	No Format
		if (m_VFormat.length() == 0
		//  Format error - all fixed characters
			|| m_VFormat.equals(m_mask))
			return;
		//	Selection
		if (e.getDot() != e.getMark())
		{
			m_lastDot = e.getDot();
			return;
		}
		//
	//	log.fine( "MDocString.caretUpdate -" + m_VFormat + "-" + m_mask + "- dot=" + e.getDot() + ", mark=" + e.getMark());

		//	Is the current position a fixed character?
		if (e.getDot()+1 > m_mask.length() || m_mask.charAt(e.getDot()) == SPACE)
		{
			m_lastDot = e.getDot();
			return;
		}

		//	Direction?
		int newDot = -1;
		if (m_lastDot > e.getDot())			//	<-
			newDot = e.getDot() - 1;
		else								//	-> (or same)
			newDot = e.getDot() + 1;
		//
		if (e.getDot() == 0)						//	first
			newDot = 1;
		else if (e.getDot() == m_mask.length()-1)	//	last
			newDot = e.getDot() - 1;
		//
	//	log.fine( "OnFixedChar=" + m_mask.charAt(e.getDot()) + ", newDot=" + newDot + ", last=" + m_lastDot);

		m_lastDot = e.getDot();
		if (newDot >= 0 && newDot < getText().length())
			m_tc.setCaretPosition(newDot);
	}	//	caretUpdate

}	//	MDocString
