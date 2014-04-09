/*******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution * Copyright (C) 1999-2007
 * ComPiere, Inc. All Rights Reserved. * This program is free software, you can
 * redistribute it and/or modify it * under the terms version 2 of the GNU
 * General Public License as published * by the Free Software Foundation. This
 * program is distributed in the hope * that it will be useful, but WITHOUT ANY
 * WARRANTY, without even the implied * warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. * See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along * with this program, if not, write to the Free Software Foundation,
 * Inc., * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. * For the text
 * or an alternative of this public license, you may reach us * ComPiere, Inc.,
 * 3600 Bridge Parkway #102, Redwood City, CA 94065, USA * or via
 * info@compiere.org or http://www.compiere.org/license.html *
 ******************************************************************************/
package org.compiere.swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.im.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.text.*;

import org.compiere.plaf.*;
import org.compiere.util.*;


/**
 * Compiere TextPane - A ScrollPane with a JTextPane. Manages visibility, opaque
 * and color consistently *
 * @author Jorg Janke
 * @version $Id: CTextPane.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CTextPane extends JScrollPane
    implements CEditor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new TextPane (HTML)
	 */
	public CTextPane()
	{
		this(new JTextPane());
	} // CTextPane

	/**
	 * Constructs a new JTextPane with the given document
	 * @param doc the model to use
	 */
	public CTextPane(StyledDocument doc)
	{
		this(new JTextPane(doc));
	} // CTextPane

	/**
	 * Create a JScrollArea with a JTextEditor
	 * @param textPane
	 */
	public CTextPane(JTextPane textPane)
	{
		super(textPane);
		m_textPane = textPane;
		super.setOpaque(false);
		super.getViewport().setOpaque(false);
		m_textPane.setContentType("text/html");
		m_textPane.setFont(CompierePLAF.getFont_Field());
		m_textPane.setForeground(CompierePLAF.getTextColor_Normal());
	} // CTextPane

	/** The Text Pane		*/
	private JTextPane m_textPane = null;

	/** ********************************************************************** */
	/** Mandatory (default false) */
	private boolean m_mandatory = false;

	/**
	 * Set Editor Mandatory
	 * @param mandatory true, if you have to enter data
	 */
	public void setMandatory(boolean mandatory)
	{
		m_mandatory = mandatory;
		setBackground(false);
	} // setMandatory

	/**
	 * Is Field mandatory
	 * @return true, if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	} // isMandatory

	/**
	 * Enable Editor
	 * @param rw true, if you can enter/select data
	 */
	public void setReadWrite(boolean rw)
	{
		if (m_textPane.isEditable() != rw)
			m_textPane.setEditable(rw);
		setBackground(false);
	} // setReadWrite

	/**
	 * Is it possible to edit
	 * @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_textPane.isEditable();
	} // isReadWrite

	/**
	 * Set Background based on editable / mandatory / error
	 * @param error if true, set background to error color, otherwise
	 *        mandatory/editable
	 */
	public void setBackground(boolean error)
	{
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!isReadWrite())
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (m_mandatory)
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}	// setBackground

	/**
	 * Set Background
	 * @param color color
	 */
	@Override
	public void setBackground(Color color)
	{
		if (color.equals(getBackground()))
			return;
		if (m_textPane == null) // during init
			super.setBackground(color);
		else
			m_textPane.setBackground(color);
	} // setBackground

	/**
	 * Get Background
	 * @return color
	 */
	@Override
	public Color getBackground()
	{
		if (m_textPane == null) // during init
			return super.getBackground();
		else
			return m_textPane.getBackground();
	} // getBackground

	/**
	 * Set Foreground
	 * @param color color
	 */
	@Override
	public void setForeground(Color color)
	{
		if (m_textPane == null) // during init
			super.setForeground(color);
		else
			m_textPane.setForeground(color);
	} // setForeground

	/**
	 * Get Foreground
	 * @return color
	 */
	@Override
	public Color getForeground()
	{
		if (m_textPane == null) // during init
			return super.getForeground();
		else
			return m_textPane.getForeground();
	} // getForeground

	/**
	 * Set Content Type
	 * @param type e.g. text/html
	 */
	public void setContentType(String type)
	{
		if (m_textPane != null) // during init
			m_textPane.setContentType(type);
	} // setContentType

	/**
	 * Set Editor to value
	 * @param value value of the editor
	 */
	public void setValue(Object value)
	{
		if (value == null)
			setText("");
		else
			setText(value.toString());
	} // setValue

	/**
	 * Return Editor value
	 * @return current value
	 */
	public Object getValue()
	{
		return m_textPane.getText();
	} // getValue

	/**
	 * Return Display Value
	 * @return displayed String value
	 */
	public String getDisplay()
	{
		return m_textPane.getText();
	}	// getDisplay

	/***************************************************************************
	 * Set Text and position top
	 * @param text
	 */
	public void setText(String text)
	{
		boolean error = false;
		/**	Java Swing Parser Error
		m_textPane.setText(text);		
		javax.swing.text.ChangedCharSetException
		at javax.swing.text.html.parser.DocumentParser.handleEmptyTag(DocumentParser.java:172)
		at javax.swing.text.html.parser.Parser.startTag(Parser.java:409)
		at javax.swing.text.html.parser.Parser.parseTag(Parser.java:1925)
		***/
        try	 
        {
        	Document doc = m_textPane.getDocument();
        	doc.remove(0, doc.getLength());
        	if (text == null || text.equals(""))
        		return;
        	Reader r = new StringReader(text);
        	EditorKit kit = m_textPane.getEditorKit();
            kit.read(r, doc, 0);
        } 
        catch (Exception ioe) 
        {
        //	ioe.printStackTrace();
        	error = true;
        }
        if (error)
        {
    		try
    		{
    			File file = File.createTempFile("TempHtml", ".html");
    			FileWriter fw = new FileWriter(file, false);
    			fw.write(text);
    			fw.flush();
    			fw.close();
    			//
    			String uriString = "file:///" + file.getAbsolutePath();
    			uriString = Util.replace(uriString, "\\", "/");
    			URI uri = new URI(uriString);
    			m_textPane.setPage(uri.toURL());
    			error = false;
    		}
    		catch (Exception e)
    		{
    		//	log.severe(e.toString());
    		}
        }
        
		m_textPane.setCaretPosition(0);
	}	//	setText

	/**
	 * Get Text
	 * @return text
	 */
	public String getText()
	{
		return m_textPane.getText();
	}	//	getText

	/**
	 * Set Caret Position
	 * @param pos pos
	 */
	public void setCaretPosition(int pos)
	{
		m_textPane.setCaretPosition(pos);
	}

	/**
	 * Get Caret Position
	 * @return position
	 */
	public int getCaretPosition()
	{
		return m_textPane.getCaretPosition();
	}

	/**
	 * Set Editable
	 * @param edit editable
	 */
	public void setEditable(boolean edit)
	{
		m_textPane.setEditable(edit);
	}

	/**
	 * Editable
	 * @return true if editable
	 */
	public boolean isEditable()
	{
		return m_textPane.isEditable();
	}

	/**
	 * Set Text Margin
	 * @param m insets
	 */
	public void setMargin(Insets m)
	{
		if (m_textPane != null)
			m_textPane.setMargin(m);
	} // setMargin

	/**
	 * Set Opaque
	 * @param isOpaque opaque
	 */
	@Override
	public void setOpaque(boolean isOpaque)
	{
		// JScrollPane & Viewport is always not Opaque
		if (m_textPane == null) // during init of JScrollPane
			super.setOpaque(isOpaque);
		else
			m_textPane.setOpaque(isOpaque);
	} // setOpaque

	/**
	 * Add Focus Listener
	 * @param l listener
	 */
	@Override
	public void addFocusListener(FocusListener l)
	{
		if (m_textPane == null) // during init
			super.addFocusListener(l);
		else
			m_textPane.addFocusListener(l);
	}

	/**
	 * Add Mouse Listener
	 * @param l listner
	 */
	@Override
	public void addMouseListener(MouseListener l)
	{
		m_textPane.addMouseListener(l);
	}

	/**
	 * Add Key Listener
	 * @param l listner
	 */
	@Override
	public void addKeyListener(KeyListener l)
	{
		m_textPane.addKeyListener(l);
	}

	/**
	 * Add Input Method Listener
	 * @param l listener
	 */
	@Override
	public void addInputMethodListener(InputMethodListener l)
	{
		m_textPane.addInputMethodListener(l);
	}

	/**
	 * Get Input Method Requests
	 * @return requests
	 */
	@Override
	public InputMethodRequests getInputMethodRequests()
	{
		return m_textPane.getInputMethodRequests();
	}

	/**
	 * Set Input Verifier
	 * @param l verifyer
	 */
	@Override
	public void setInputVerifier(InputVerifier l)
	{
		m_textPane.setInputVerifier(l);
	}
	
	/**
	 * 	Request Focus
	 */
	@Override
	public void requestFocus()
	{
		m_textPane.requestFocus();
	}	//	requestFocus

	/**
	 * 	Request Focus In Window
	 *	@return focus request
	 */
	@Override
	public boolean requestFocusInWindow()
	{
		return m_textPane.requestFocusInWindow();
	}	//	requestFocusInWindow
	
	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		return m_textPane;
	}	//	getFocusComponent

	/**
	 * 	Get Editor Actions
	 *	@return actions
	 */
	public Action[] getActions()
	{
		return m_textPane.getActions();
	}	//	getActions
	
	/**
	 * 	Write content
	 *	@param out writer
	 * 	@throws IOException
	 */
    public void write(Writer out) throws IOException
	{
		m_textPane.write(out);
	}	//	write
	
    /**
     * 	Set Editor to page
     *	@param url url
     *	@throws IOException
     */
    public void setPage(URL url) throws IOException
    {
		m_textPane.setPage(url);
    }	//	setPage
    
}	// CTextPane
