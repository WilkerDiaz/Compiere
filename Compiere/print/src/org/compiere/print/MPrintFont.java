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
package org.compiere.print;

import java.awt.*;
import java.awt.font.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;

/**
 *	AD_PrintFont Print Font Model
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFont.java,v 1.3 2006/07/30 00:53:02 jjanke Exp $
 */
public class MPrintFont extends X_AD_PrintFont
{
    /** Logger for class MPrintFont */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPrintFont.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param AD_PrintFont_ID ID
	 *  @param trx transaction
	 */
	public MPrintFont(Ctx ctx, int AD_PrintFont_ID, Trx trx)
	{
		super (ctx, AD_PrintFont_ID, trx);
		if (AD_PrintFont_ID == 0)
			setIsDefault(false);
	}	//	MPrintFont

	/**
	 *	Constructor
	 * 	@param ctx context
	 *  @param rs ResultSet
	 *	@param trx transaction
	 */
	public MPrintFont (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPrintFont
	
	/** Font cached					*/
	private Font 	m_cacheFont = null;

	/*************************************************************************/

	/**
	 * 	Get Font
	 * 	@return Font
	 */
	public Font getFont()
	{
		if (m_cacheFont != null)
			return m_cacheFont;
		String code = (String)get_Value("Code");
		if (code == null || code.equals("."))
			m_cacheFont = new Font (null);
		try
		{
			if (code != null && !code.equals("."))
			//	fontfamilyname-style-pointsize
				m_cacheFont = Font.decode(code);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Code=" + code, e);
		}
		if (code == null)
			m_cacheFont = new Font (null);		
		//	family=dialog,name=Dialog,style=plain,size=12
	//	log.fine(code + " - " + m_cacheFont);
		return m_cacheFont;
	}	//	getFont

	/**
	 * 	Set Font
	 * 	@param font Font
	 */
	public void setFont (Font font)
	{
		//	fontfamilyname-style-pointsize
		StringBuffer sb = new StringBuffer();
		sb.append(font.getFamily()).append("-");
		int style = font.getStyle();
		if (style == Font.PLAIN)
			sb.append("PLAIN");
		else if (style == Font.BOLD)
			sb.append("BOLD");
		else if (style == Font.ITALIC)
			sb.append("ITALIC");
		else if (style == (Font.BOLD + Font.ITALIC))
			sb.append("BOLDITALIC");
		sb.append("-").append(font.getSize());
		setCode(sb.toString());
	}	//	setFont

	
	/**************************************************************************
	 * 	Create Font in Database and save
	 * 	@param font font
	 * 	@return PrintFont
	 */
	static MPrintFont create (Font font)
	{
		MPrintFont pf = new MPrintFont(Env.getCtx(), 0, null);
		StringBuffer name = new StringBuffer (font.getName());
		if (font.isBold())
			name.append(" bold");
		if (font.isItalic())
			name.append(" italic");
		name.append(" ").append(font.getSize());
		pf.setName(name.toString());
		pf.setFont(font);
		pf.save();
		return pf;
	}	//	create

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPrintFont[");
		sb.append("ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append("PSName=").append(getFont().getPSName())
			.append(getFont())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Get PostScript Level 2 definition.
	 *  e.g. /dialog 12 selectfont
	 * 	@return PostScript command
	 */
	public String toPS()
	{
		StringBuffer sb = new StringBuffer("/");
		sb.append(getFont().getPSName());
		if (getFont().isBold())
			sb.append(" Bold");
		if (getFont().isItalic())
			sb.append(" Italic");
		sb.append(" ").append(getFont().getSize())
			.append(" selectfont");
		return sb.toString();
	}	//	toPS

	/**
	 * 	Dump Font
	 * 	@param font font
	 */
	static void dump (Font font)
	{
		System.out.println("Family=" + font.getFamily());
		System.out.println("FontName=" + font.getFontName());
		System.out.println("Name=" + font.getName());
		System.out.println("PSName=" + font.getPSName());
		System.out.println("Style=" + font.getStyle());
		System.out.println("Size=" + font.getSize());
		System.out.println("Attributes:");
		Map<TextAttribute, ?> map = font.getAttributes();
		Iterator<TextAttribute> keys = map.keySet().iterator();
		while (keys.hasNext())
		{
			Object key = keys.next();
			Object value = map.get(key);
			System.out.println(" - " + key + "=" + value);
		}
		System.out.println(font);
	}	//	dump

	
	/*************************************************************************/

	/** Cached Fonts						*/
	static private final CCache<Integer,MPrintFont> s_fonts = 
		new CCache<Integer,MPrintFont>("AD_PrintFont", 20);

	/**
	 * 	Get Font
	 * 	@param ctx context
	 * 	@param AD_PrintFont_ID id
	 * 	@return Font
	 */
	static public MPrintFont get (Ctx ctx, int AD_PrintFont_ID)
	{
		Integer key = new Integer(AD_PrintFont_ID);
		MPrintFont pf = s_fonts.get(ctx, key);
		if (pf == null)
		{
			pf = new MPrintFont (ctx, AD_PrintFont_ID, null);
			s_fonts.put(key, pf);
		}
		return pf;
	}	//	get

	static public BaseFont getITextFont (Ctx ctx, int AD_PrintFont_ID) {
		MPrintFont printFont = get (ctx, AD_PrintFont_ID);
		BaseFont bf = new DefaultFontMapper().awtToPdf(printFont.getFont());
		return bf;
	}
	
	/**************************************************************************
	 * 	Seed Fonts
	 * 	@param args args
	 */
	public static void main(String[] args)
	{
		System.out.println("Available Fonts:");
		String[] family = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String element : family)
			System.out.println(" - " + element);

		org.compiere.Compiere.startup(true);
		MPrintFont pf = new MPrintFont(Env.getCtx(), 100, null);
		dump( pf.getFont() );

		//String[] systemLocical = new String[] {"Dialog", "DialogInput", "Monospaced", "Serif", "SansSerif"};
		//for (String element : systemLocical) {
		//	create(new Font(systemLocical[i], Font.BOLD, 13));
		//	create(new Font(systemLocical[i], Font.PLAIN, 11));
		//	create(new Font(systemLocical[i], Font.BOLD, 11));
		//	create(new Font(systemLocical[i], Font.ITALIC, 11));
		//	create(new Font(systemLocical[i], Font.PLAIN, 10));
		//	create(new Font(systemLocical[i], Font.BOLD, 10));
		//	create(new Font(systemLocical[i], Font.ITALIC, 10));
		//	create(new Font(systemLocical[i], Font.PLAIN, 9));
		//	create(new Font(systemLocical[i], Font.BOLD, 9));
		//	create(new Font(systemLocical[i], Font.ITALIC, 9));
		//	create(new Font(systemLocical[i], Font.PLAIN, 8));
		//	create(new Font(systemLocical[i], Font.BOLD, 8));
		//	create(new Font(systemLocical[i], Font.ITALIC, 8));
		//}

		//	Read All Fonts
		int[] IDs = PO.getAllIDs ("AD_PrintFont", null, null);
		for (int element : IDs) {
			pf = new MPrintFont(Env.getCtx(), element, null);
			System.out.println(element + " = " + pf.getFont());
		}

	}	//	main

	/**
	 * 	Get XML for all Print Fonts
	 * 	@param ctx context
	 * 	@param rw read write
	 * 	@return print fonts
	 */
	public static StringBuffer getPrintFontsXML( Ctx ctx, boolean rw )
	{
		StringBuffer fonts_xml = new StringBuffer("<AD_PrintFonts>");

		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		
		String pf_sql = role.addAccessSQL("SELECT PF.AD_PrintFont_ID, PF.Name, PF.Code FROM AD_PrintFont PF " +
						"ORDER BY Name ASC",
						"AD_PrintFont", MRole.SQL_NOTQUALIFIED, rw);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(pf_sql, (Trx) null);
			ResultSet rs = pstmt.executeQuery();
			while( rs.next() )
			{
				fonts_xml.append("<AD_PrintFont>");

				fonts_xml.append("<AD_PrintFont_ID>");
				fonts_xml.append(rs.getInt(1));
				fonts_xml.append("</AD_PrintFont_ID>");

				fonts_xml.append("<Name>");
				fonts_xml.append(rs.getString(2));
				fonts_xml.append("</Name>");	

				MPrintFont pf = MPrintFont.get(ctx, rs.getInt(1));
				Font f = pf.getFont();
				fonts_xml.append("<FontStyle>");
				fonts_xml.append(f.isItalic() ? "italic" : "normal");
				fonts_xml.append("</FontStyle>");	
				fonts_xml.append("<FontWeight>");
				fonts_xml.append(f.isBold() ? "bold" : "normal");
				fonts_xml.append("</FontWeight>");	

				fonts_xml.append("</AD_PrintFont>");
			}
			fonts_xml.append("</AD_PrintFonts>");
			rs.close();
			pstmt.close();
		}
		catch(SQLException e)
		{
			log.log(Level.SEVERE, pf_sql, e);
		}
		return fonts_xml;
	}

}	//	MPrintFont
