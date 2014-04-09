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
package org.compiere.util;

import java.awt.*;
import java.lang.reflect.*;
import java.math.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.*;
import org.compiere.common.constants.*;
import org.compiere.db.*;
import org.compiere.model.*;
import org.compiere.swing.*;


/**
 *  System Environment and static variables
 *
 *  @author     Jorg Janke
 *  @version    $Id: Env.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public final class Env extends EnvConstants
{
	/**	Logging								*/
	private static CLogger				s_log = CLogger.getCLogger(Env.class);

	/** List of Unix browsers - sequence of test of existence */
	private static String[] UNIX_BROWSERS =
	{"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
	/** Google Map search Prefix		*/
	public static String GOOGLE_MAPS_URL_PREFIX =
		"http://local.google.com/maps?q=";
	/** Swing Regression Test active	*/
	public static String 	SWING_RT = "SwingRT";

	/**
	 *	Exit System
	 *  @param status System exit status (usually 0 for no error)
	 */
	public static void exitEnv (int status)
	{
		//	End Session
		MSession session = MSession.get(Env.getCtx());
		if (session != null)
			session.logout();
		//
		closeWindows();

		if (Ini.isClient())
			System.exit (status);
	}	//	close

	/**
	 * 	Reset Cache
	 * 	@param finalCall everything otherwise login data remains
	 */
	public static void reset (boolean finalCall)
	{
		s_log.info("finalCall=" + finalCall);
		closeHiddenWindows();

		//	Clear all Context
		if (finalCall)
			s_ctx.clear();
		else	//	clear window context only
		{
			Object[] keys = s_ctx.keySet().toArray();
			for (Object element : keys) {
				String tag = element.toString();
				if (Character.isDigit(tag.charAt(0)))
					s_ctx.remove(element);
			}
		}

		//	Cache
		CacheMgt.get().reset();
		//	Reset Role Access
		if (!finalCall)
		{
			MRole defaultRole = MRole.getDefault(s_ctx, false);
			if (defaultRole != null)
				defaultRole.loadAccess(true);	//	Reload
		}
		else {
			DB.closeTarget();
		}
	}	//	resetAll


	/**************************************************************************
	 *  Client Application Context
	 */
	private static CContext		s_ctx = new CContext();

	/**
	 *  Get Client Context
	 *  @return Properties
	 */
	public static final Ctx getCtx()
	{
		return s_ctx;
	}   //  getCtx


	/**************************************************************************
	 *	Get Preference.
	 *  <pre>
	 *		0)	Current Setting
	 *		1) 	Window Preference
	 *		2) 	Global Preference
	 *		3)	Login settings
	 *		4)	Accounting settings
	 *  </pre>
	 *  @param  ctx context
	 *	@param	AD_Window_ID window no
	 *	@param	context		Entity to search
	 *	@param	system		System level preferences (vs. user defined)
	 *  @return preference value
	 */
	public static String getPreference (Ctx ctx, int AD_Window_ID, String context, boolean system)
	{
		if (ctx == null || context == null)
			throw new IllegalArgumentException ("Require Context");
		String retValue = null;
		//
		if (!system)	//	User Preferences
		{
			retValue = ctx.getContext("P"+AD_Window_ID+"|"+context);//	Window Pref
			if (retValue.length() == 0)
				retValue = ctx.getContext("P|"+context);  			//	Global Pref
		}
		else			//	System Preferences
		{
			retValue = ctx.getContext("#"+context);   				//	Login setting
			if (retValue.length() == 0)
				retValue = ctx.getContext("$"+context);   			//	Accounting setting
		}
		//
		return retValue == null ? "" : retValue;
	}	//	getPreference

	/**************************************************************************
	 *  Language issues
	 */

	/** Context Language identifier */
	static public final String      LANGUAGE = "#AD_Language";

	/**
	 *  Check Base Language
	 *  @param ctx context
	 * 	@param tableName table to be translated
	 * 	@return true if base language and table not translated
	 */
	public static boolean isBaseLanguage (Ctx ctx, String tableName)
	{
		/**
		if (isBaseTranslation(tableName))
			return Language.isBaseLanguage (getAD_Language(ctx));
		else	//	No AD Table
			if (!isMultiLingualDocument(ctx))
				return true;		//	access base table
		 **/
		return Language.isBaseLanguage (getAD_Language(ctx));
	}	//	isBaseLanguage

	/**
	 *	Check Base Language
	 * 	@param AD_Language language
	 * 	@param tableName table to be translated
	 * 	@return true if base language and table not translated
	 */
	public static boolean isBaseLanguage (String AD_Language, String tableName)
	{
		/**
		if (isBaseTranslation(tableName))
			return Language.isBaseLanguage (AD_Language);
		else	//	No AD Table
			if (!isMultiLingualDocument(s_ctx))				//	Base Context
				return true;		//	access base table
		 **/
		return Language.isBaseLanguage (AD_Language);
	}	//	isBaseLanguage

	/**
	 *	Check Base Language
	 * 	@param language language
	 * 	@param tableName table to be translated
	 * 	@return true if base language and table not translated
	 */
	public static boolean isBaseLanguage (Language language, String tableName)
	{
		/**
		if (isBaseTranslation(tableName))
			return language.isBaseLanguage();
		else	//	No AD Table
			if (!isMultiLingualDocument(s_ctx))				//	Base Context
				return true;		//	access base table
		 **/
		return language.isBaseLanguage();
	}	//	isBaseLanguage

	/**
	 * 	Table is in Base Translation (AD)
	 *	@param tableName table
	 *	@return true if base trl
	 */
	public static boolean isBaseTranslation (String tableName)
	{
		if (tableName.startsWith("AD")
				|| tableName.equals("C_Country_Trl") )
			return true;
		return false;
	}	//	isBaseTranslation

	/**
	 * 	Do we have Multi-Lingual Documents.
	 *  Set in DB.loadOrgs
	 * 	@param ctx context
	 * 	@return true if multi lingual documents
	 */
	public static boolean isMultiLingualDocument (Ctx ctx)
	{
		return MClient.get(ctx).isMultiLingualDocument();
	}	//	isMultiLingualDocument

	/**
	 *  Get System AD_Language
	 *  @param ctx context
	 *	@return AD_Language eg. en_US
	 */
	public static String getAD_Language (Ctx ctx)
	{
		if (ctx != null)
		{
			String lang = ctx.getContext(LANGUAGE);
			if (lang != null && lang.length() > 0)
				return lang;
		}
		return Language.getBaseAD_Language();
	}	//	getAD_Language

	/**
	 *  Get System Language
	 *  @param ctx context
	 *	@return Language
	 */
	public static Language getLanguage (Ctx ctx)
	{
		if (ctx != null)
		{
			String lang = ctx.getContext(LANGUAGE);
			if (lang != null && lang.length() > 0)
				return Language.getLanguage(lang);
		}
		return Language.getBaseLanguage();
	}	//	getLanguage

	/**
	 *  Get Login Language
	 *  @param ctx context
	 *	@return Language
	 */
	public static Language getLoginLanguage (Ctx ctx)
	{
		return Language.getLoginLanguage();
	}	//	getLanguage

	/**
	 *  Verify Language.
	 *  Check that language is supported by the system
	 *  @param ctx might be updated with new AD_Language
	 *  @param language language
	 */
	public static Language verifyLanguage (Language language)
	{
		if (language.isBaseLanguage())
			return language;

		boolean isSystemLanguage = false;
		ArrayList<String> AD_Languages = new ArrayList<String>();
		String sql = "SELECT DISTINCT AD_Language FROM AD_Message_Trl";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String AD_Language = rs.getString(1);
				if (AD_Language.equals(language.getAD_Language()))
				{
					isSystemLanguage = true;
					break;
				}
				AD_Languages.add(AD_Language);
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, "", e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Found it
		if (isSystemLanguage)
			return language;
		//	No Language - set to System
		if (AD_Languages.size() == 0)
		{
			s_log.config ("NO System Language - Set to Base " + Language.getBaseAD_Language());
			return Language.getBaseLanguage();
		}

		for (int i = 0; i < AD_Languages.size(); i++)
		{
			String AD_Language = AD_Languages.get(i);	//	en_US
			String lang = AD_Language.substring(0, 2);			//	en
			//
			String langCompare = language.getAD_Language().substring(0, 2);
			if (lang.equals(langCompare))
			{
				s_log.fine("Found similar Language " + AD_Language);
				return Language.getLanguage( AD_Language );
			}
		}

		//	We found same language
		//	if (!"0".equals(Msg.getMsg(AD_Language, "0")))
		s_log.info ("Not System Language=" + language
				+ " - Set to Base Language " + Language.getBaseAD_Language());
		return Language.getBaseLanguage();
	}   //  verifyLanguage


	/**
	 *	Get Header info (connection, org, user).
	 *	Use -DSwingRT=Y to prevent user/org/etc. info
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @return Header String
	 */
	public static String getHeader(Ctx ctx, int WindowNo)
	{
		StringBuffer sb = new StringBuffer();
		if (WindowNo > 0)
			sb.append(ctx.getContext(WindowNo, "WindowName", false));
		if ("Y".equals(System.getProperty(SWING_RT)))
			;	//	no detail info for swing regtession test
		else
		{
			if (sb.length() > 0)
				sb.append("  ");
			sb.append(ctx.getContext("##AD_User_Name")).append("@")
			.append(ctx.getContext("#AD_Org_Name")).append(".")
			.append(ctx.getContext("#AD_Client_Name"))
			.append(" [").append(CConnection.get().toString()).append("]");
		}
		return sb.toString();
	}	//	getHeader

	/**
	 *	Clean up context for Window (i.e. delete it)
	 *  @param ctx context
	 *  @param WindowNo window
	 */
	public static void clearWinContext(Ctx ctx, int WindowNo)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		//
		Object[] keys = ctx.keySet().toArray();
		for (Object element : keys) {
			String tag = element.toString();
			if (tag.startsWith(WindowNo+"|"))
				ctx.remove(element);
		}
		//  Clear Lookup Cache
		MLookupCache.cacheReset(WindowNo);
		//	MLocator.cacheReset(WindowNo);
		//
		removeWindow(WindowNo);
	}	//	clearWinContext

	/**
	 *	Clean up all context (i.e. delete it)
	 *  @param ctx context
	 */
	public static void clearContext(Ctx ctx)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		ctx.clear();
	}	//	clearContext



	public static class QueryParams
	{
		public String sql = "";
		public final List<Object> params;
		
		/**
		 * 
		 */
		public QueryParams() {
			params = new ArrayList<Object>();
		}
		
		/**
		 * @param sql
		 * @param params
		 */
		public QueryParams(String sql, Object[] params) {
			this.sql = sql;
			this.params = Arrays.asList(params);
		}
	}

	/**
	 *	Parse Context replaces global or Window context @tag@ with actual value.
	 *	The tag may have a | with a fixed value e.g. @C_BPartner_ID|0@
	 *  @param ctx context
	 *	@param WindowNo	Number of Window
	 *	@param value Message to be parsed
	 *  @param onlyWindow if true, no defaults are used
	 * 	@param ignoreUnparsable if true, unsuccessful return parsed String or "" if not successful and ignoreUnparsable
	 *	@return parsed context
	 */
	public static QueryParams parseContextUsingBindVariables (Ctx ctx, int WindowNo, String value,
			boolean onlyWindow, boolean ignoreUnparsable)
	{
		QueryParams result = new QueryParams();
		if (value == null || value.length() == 0)
			return result;

		String token;
		String inStr = new String(value);
		StringBuffer outStr = new StringBuffer();

		int i = inStr.indexOf('@');
		// Check whether the @ is not the last in line (i.e. in EMailAdress or with wrong entries)
		while (i != -1 && i != inStr.lastIndexOf ("@"))
		{
			outStr.append(inStr.substring(0, i));			// up to @
			inStr = inStr.substring(i+1, inStr.length());	// from first @

			int j = inStr.indexOf('@');						// next @
			if (j < 0)
			{
				s_log.log(Level.SEVERE, "No second tag: " + inStr);
				return result;						//	no second tag
			}

			String defaultV = null;
			token = inStr.substring(0, j);
			int idx = token.indexOf("|");	//	or clause
			if (idx  >=  0)
			{
				defaultV = token.substring(idx+1, token.length());
				token = token.substring(0, idx);
			}
			String ctxInfo = ctx.getContext(WindowNo, token, onlyWindow);	// get context
			if (ctxInfo.length() == 0 && (token.startsWith("#") || token.startsWith("$")) )
				ctxInfo = ctx.getContext(token);	// get global context
			if (ctxInfo.length() == 0 && defaultV != null)
				ctxInfo = defaultV;

			if (ctxInfo.length() == 0)
			{
				s_log.config("No Context Win=" + WindowNo + " for: " + token);
				//		+ " Value=" + value);
				if (!ignoreUnparsable)
					return result;						//	token not found
			}
			else
			{

				inStr = inStr.substring(j+1, inStr.length());	// from second @
				// if token is surrounded by single quotes, remove them; also means it's a string
				if (outStr.charAt(outStr.length() - 1) == '\'' && inStr.trim().startsWith("\'")) {
					outStr.deleteCharAt(outStr.length() - 1);
					inStr = inStr.trim().substring(1);
					result.params.add(ctxInfo);
				}
				else
				{
					// attempt to parse as number; if failure, pass as string
					try
					{
						result.params.add(new BigDecimal(ctxInfo));
					}
					catch (Exception e) {
						result.params.add(ctxInfo);
					}
				}

				outStr.append('?');
			}

			i = inStr.indexOf('@');
		}
		outStr.append(inStr);						// add the rest of the string

		result.sql = outStr.toString(); 
		return result;
	}	//	parseContext




	/**
	 *	Parse Context replaces global or Window context @tag@ with actual value.
	 *	The tag may have a | with a fixed value e.g. @C_BPartner_ID|0@
	 *  @param ctx context
	 *	@param WindowNo	Number of Window
	 *	@param value Message to be parsed
	 *  @param onlyWindow if true, no defaults are used
	 * 	@param ignoreUnparsable if true, unsuccessful return parsed String or "" if not successful and ignoreUnparsable
	 *	@return parsed context
	 */
	public static String parseContext (Ctx ctx, int WindowNo, String value,
			boolean onlyWindow, boolean ignoreUnparsable)
	{
		if (value == null || value.length() == 0)
			return "";

		String token;
		String inStr = new String(value);
		StringBuffer outStr = new StringBuffer();

		int i = inStr.indexOf('@');
		// Check whether the @ is not the last in line (i.e. in EMailAdress or with wrong entries)
		while (i != -1 && i != inStr.lastIndexOf ("@"))
		{
			outStr.append(inStr.substring(0, i));			// up to @
			inStr = inStr.substring(i+1, inStr.length());	// from first @

			int j = inStr.indexOf('@');						// next @
			if (j < 0)
			{
				s_log.log(Level.SEVERE, "No second tag: " + inStr);
				return "";						//	no second tag
			}

			String defaultV = null;
			token = inStr.substring(0, j);
			int idx = token.indexOf("|");	//	or clause
			if (idx  >=  0)
			{
				defaultV = token.substring(idx+1, token.length());
				token = token.substring(0, idx);
			}
			String ctxInfo = ctx.getContext(WindowNo, token, onlyWindow);	// get context
			if (ctxInfo.length() == 0 && (token.startsWith("#") || token.startsWith("$")) )
				ctxInfo = ctx.getContext(token);	// get global context
			if (ctxInfo.length() == 0 && defaultV != null)
				ctxInfo = defaultV;

			if (ctxInfo.length() == 0)
			{
				s_log.config("No Context Win=" + WindowNo + " for: " + token);
				//		+ " Value=" + value);
				if (!ignoreUnparsable)
					return "";						//	token not found
			}
			else
				outStr.append(ctxInfo);				// replace context with Context

			inStr = inStr.substring(j+1, inStr.length());	// from second @
			i = inStr.indexOf('@');
		}
		outStr.append(inStr);						// add the rest of the string

		return outStr.toString();
	}	//	parseContext

	/**
	 *	Parse Context replaces global or Window context @tag@ with actual value.
	 *
	 *  @param ctx context
	 *	@param	WindowNo	Number of Window
	 *	@param	value		Message to be parsed
	 *  @param  onlyWindow  if true, no defaults are used
	 *  @return parsed String or "" if not successful
	 */
	public static String parseContext (Ctx ctx, int WindowNo, String value,
			boolean onlyWindow)
	{
		return parseContext(ctx, WindowNo, value, onlyWindow, false);
	}	//	parseContext

	/*************************************************************************/

	//	Array of active Windows
	private static ArrayList<Container>	s_windows = new ArrayList<Container>(20);

	/**
	 *	Add Container and return WindowNo.
	 *  The container is a APanel, AWindow or JFrame/JDialog
	 *  @param win window
	 *  @return WindowNo used for context
	 */
	public static int createWindowNo(Container win)
	{
		int retValue = s_windows.size();
		s_windows.add(win);
		return retValue;
	}	//	createWindowNo

	/**
	 *	Search Window by comparing the Frames
	 *  @param container container
	 *  @return WindowNo of container or 0
	 */
	public static int getWindowNo (Container container)
	{
		if (container == null)
			return 0;
		JFrame winFrame = getFrame(container);
		if (winFrame == null)
			return 0;

		//  loop through windows
		for (int i = 0; i < s_windows.size(); i++)
		{
			Container cmp = s_windows.get(i);
			if (cmp != null)
			{
				JFrame cmpFrame = getFrame(cmp);
				if (winFrame.equals(cmpFrame))
					return i;
			}
		}
		return 0;
	}	//	getWindowNo

	/**
	 *	Return the JFrame pointer of WindowNo - or null
	 *  @param WindowNo window
	 *  @return JFrame of WindowNo
	 */
	public static JFrame getWindow (int WindowNo)
	{
		JFrame retValue = null;
		try
		{
			retValue = getFrame (s_windows.get(WindowNo));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, e.toString());
		}
		return retValue;
	}	//	getWindow

	/**
	 *	Remove window from active list
	 *  @param WindowNo window
	 */
	private static void removeWindow (int WindowNo)
	{
		if (WindowNo < s_windows.size())
			s_windows.set(WindowNo, null);
	}	//	removeWindow

	/**
	 *	Clean up context for Window (i.e. delete it)
	 *  @param WindowNo window
	 */
	public static void clearWinContext(int WindowNo)
	{
		clearWinContext (s_ctx, WindowNo);
	}	//	clearWinContext

	/**
	 *	Clean up all context (i.e. delete it)
	 */
	public static void clearContext()
	{
		s_ctx.clear();
	}	//	clearContext


	/**************************************************************************
	 *	Get Frame of Window
	 *  @param container Container
	 *  @return JFrame of container or null
	 */
	public static JFrame getFrame (Container container)
	{
		Container element = container;
		while (element != null)
		{
			if (element instanceof JFrame)
				return (JFrame)element;
			element = element.getParent();
		}
		return null;
	}	//	getFrame

	/**
	 *	Get Graphics of container or its parent.
	 *  The element may not have a Graphic if not displayed yet,
	 * 	but the parent might have.
	 *  @param container Container
	 *  @return Graphics of container or null
	 */
	public static Graphics getGraphics (Container container)
	{
		Container element = container;
		while (element != null)
		{
			Graphics g = element.getGraphics();
			if (g != null)
				return g;
			element = element.getParent();
		}
		return null;
	}	//	getFrame

	/**
	 *  Return JDialog or JFrame Parent
	 *  @param container Container
	 *  @return JDialog or JFrame of container
	 */
	public static Window getParent (Container container)
	{
		Container element = container;
		while (element != null)
		{
			if (element instanceof JDialog || element instanceof JFrame)
				return (Window)element;
			if (element instanceof Window)
				return (Window)element;
			element = element.getParent();
		}
		return null;
	}   //  getParent

	/**************************************************************************
	 *  Get Image with File name
	 *
	 *  @param fileNameInImageDir full file name in imgaes folder (e.g. Bean16.gif)
	 *  @return image
	 */
	public static Image getImage (String fileNameInImageDir)
	{
		URL url = Compiere.class.getResource("images/" + fileNameInImageDir);
		if (url == null)
		{
			s_log.log(Level.SEVERE, "Not found: " +  fileNameInImageDir);
			return null;
		}
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.getImage(url);
	}   //  getImage

	/**
	 *  Get ImageIcon.
	 *
	 *  @param fileNameInImageDir full file name in images folder (e.g. Bean16.gif)
	 *  @return image
	 */
	public static ImageIcon getImageIcon (String fileNameInImageDir)
	{
		ImageIcon icon = null;

		if (icon == null && fileNameInImageDir.endsWith("24.gif"))
		{
			String png = fileNameInImageDir.substring(0, fileNameInImageDir.length() - 6) + "32.png";
			URL png_url = Compiere.class.getResource("images/" + png);
			if (png_url != null)
				icon = new ImageIcon(png_url);
		}

		if (icon == null && fileNameInImageDir.endsWith("10.gif"))
		{
			String png = fileNameInImageDir.substring(0, fileNameInImageDir.length() - 6) + "12.png";
			URL png_url = Compiere.class.getResource("images/" + png);
			if (png_url != null)
				icon = new ImageIcon(png_url);
		}

		if (icon == null && fileNameInImageDir.endsWith(".gif"))
		{
			String png = fileNameInImageDir.substring(0, fileNameInImageDir.length() - 4) + ".png";
			URL png_url = Compiere.class.getResource("images/" + png);
			if (png_url != null)
				icon = new ImageIcon(png_url);
		}

		if (icon == null)
		{
			URL url = Compiere.class.getResource("images/" + fileNameInImageDir);
			if (url != null)
			{
				icon = new ImageIcon(url);
			}
			else
			{
				s_log.log(Level.WARNING, "Not found: " +  fileNameInImageDir);
			}
		}
		return icon;
	}   //  getImageIcon


	/***************************************************************************
	 *  Start Browser
	 *  @param url url
	 */
	public static void startBrowser (String url)
	{
		s_log.info(url);
		try
		{
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.startsWith("mac"))
			{
				Class<?>fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL =
					fileMgr.getDeclaredMethod("openURL",
							new Class[] {String.class});
				openURL.invoke(null, new Object[] {url});
			}
			else if (osName.startsWith("windows"))
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			else
			{
				//assume Unix or Linux
				String browser = null;
				String[] which = new String[] {"which", null};
				for (int count = 0;
				count < UNIX_BROWSERS.length && browser == null;
				count++)
				{
					which[1] = UNIX_BROWSERS[count];
					if (Runtime.getRuntime().exec(which).waitFor() == 0)
						browser = UNIX_BROWSERS[count];
				}
				if (browser == null)
					throw new Exception("Could not find web browser");
				else
					Runtime.getRuntime().exec(new String[] {browser, url});
			}
		}
		catch (Exception e)
		{
			s_log.severe("Can't locate browser: " + e.getLocalizedMessage());
		}
	}   //  startBrowser

	/**
	 * 	Do we run on Apple
	 *	@return true if Mac
	 */
	public static boolean isMac()
	{
		String osName = System.getProperty ("os.name");
		osName = osName.toLowerCase();
		return osName.indexOf ("mac") != -1;
	}	//	isMac

	/**
	 * 	Do we run on Windows
	 *	@return true if windows
	 */
	public static boolean isWindows()
	{
		String osName = System.getProperty ("os.name");
		osName = osName.toLowerCase();
		return osName.indexOf ("windows") != -1;
	}	//	isWindows


	/** Array of hidden Windows				*/
	private static ArrayList<CFrame>	s_hiddenWindows = new ArrayList<CFrame>();
	/** Closing Window Indicator			*/
	private static boolean 				s_closingWindows = false;

	/**
	 * 	Hide Window
	 *	@param window window
	 *	@return true if window is hidden, otherwise close it
	 */
	static public boolean hideWindow(CFrame window)
	{
		if (!Ini.isCacheWindow() || s_closingWindows)
			return false;
		for (int i = 0; i < s_hiddenWindows.size(); i++)
		{
			CFrame hidden = s_hiddenWindows.get(i);
			s_log.info(i + ": " + hidden);
			if (hidden.getAD_Window_ID() == window.getAD_Window_ID())
				return false;	//	already there
		}
		if (window.getAD_Window_ID() != 0)	//	workbench
		{
			if (s_hiddenWindows.add(window))
			{
				window.setVisible(false);
				s_log.info(window.toString());
				//	window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_ICONIFIED));
				if (s_hiddenWindows.size() > 10)
					s_hiddenWindows.remove(0);		//	sort of lru
				return true;
			}
		}
		return false;
	}	//	hideWindow

	/**
	 * 	Show Window
	 *	@param AD_Window_ID window
	 *	@return true if window re-displayed
	 */
	static public boolean showWindow (int AD_Window_ID)
	{
		for (int i = 0; i < s_hiddenWindows.size(); i++)
		{
			CFrame hidden = s_hiddenWindows.get(i);
			if (hidden.getAD_Window_ID() == AD_Window_ID)
			{
				s_hiddenWindows.remove(i);
				s_log.info(hidden.toString());
				hidden.setVisible(true);
				hidden.toFront();
				return true;
			}
		}
		return false;
	}	//	showWindow

	/**
	 * 	Close hidden windows
	 */
	static void closeHiddenWindows ()
	{
		s_closingWindows = true;
		for (int i = 0; i < s_hiddenWindows.size(); i++)
		{
			CFrame hidden = s_hiddenWindows.get(i);
			hidden.dispose();
		}
		s_hiddenWindows.clear();
		s_closingWindows = false;
	}	//	closeWindows

	/**
	 * 	Close hidden windows
	 */
	static void closeWindows ()
	{
		s_closingWindows = true;
		for( Container win : s_windows )
		{
			if (win instanceof Window)
				((Window) win).dispose();
		}
		s_windows.clear();
		s_closingWindows = false;


	}	//	closeWindows


	/**
	 * 	Sleep
	 *	@param sec seconds
	 */
	public static void sleep (int sec)
	{
		s_log.info("Start - Seconds=" + sec);
		try
		{
			Thread.sleep(sec*1000);
		}
		catch (Exception e)
		{
			s_log.log(Level.WARNING, "", e);
		}
		s_log.info("End");
	}	//	sleep


	/**************************************************************************
	 *  Static Variables
	 */

	/**	Big Decimal 0	 */
	static final public BigDecimal ZERO = BigDecimal.ZERO;
	/**	Big Decimal 1	 */
	static final public BigDecimal ONE = BigDecimal.ONE;
	/**	Big Decimal 100	 */
	static final public BigDecimal ONEHUNDRED = new BigDecimal(100.0);

	/**	New Line 		 */
	public static final String	NL = System.getProperty("line.separator");


	/**
	 *  Static initializer
	 */
	static
	{
		//  Set English as default Language
		s_ctx.put(LANGUAGE, Language.getBaseAD_Language());
	}   //  static

	private static Boolean shutdownHookAlreadyAdded = false;
	public static void addSystemShutdownHook() {
		synchronized(shutdownHookAlreadyAdded) {
			if(!shutdownHookAlreadyAdded)
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						//first write back unused sequence ids if there are any
						s_log.info("System shutting down..");
						MSequence.onSystemShutdown();
						reset(false);	// final cache reset
						//don't close the DB connection 'cuz other shutdown hooks may need to write to db
						//for example, the httpsessions that are not claimed yet would be destroyed in jboss shutdown hook, then it would write the session info back to the db
						DB.showLeakedConnections();
						s_log.info("");
						//
						CLogMgt.shutdown();

					}
				});
			shutdownHookAlreadyAdded = true;
		}
	}
	
	
	private static final ThreadLocal<Ctx> s_threadLocalCtx = new ThreadLocal<Ctx>(); 
	
	/**
	 * Sets the ThreadLocal Ctx to the specified one.  This is used by the App server. 
	 */
	public static void setThreadLocalCtx(Ctx ctx) {
		s_threadLocalCtx.set(ctx);
	}
	
	/**
	 * Returns the Ctx object associated with the current thread.  This is used by the App server.
	 * @return
	 */
	public static Ctx getThreadLocalCtx(){
		return s_threadLocalCtx.get();
	}
	
}   //  Env
