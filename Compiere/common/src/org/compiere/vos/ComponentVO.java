/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.vos;
import java.util.*;

import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *  Model Tab Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: GridTabVO.java,v 1.4 2006/07/30 00:58:38 jjanke Exp $
 */
public class ComponentVO extends TabFieldsVO
{
	/**
	 * 
	 * 
	 */
	public static final CompiereLogger	log = new CLoggerSimple(ComponentVO.class);
	private static final long serialVersionUID = 1L;
	/** Context - replicated    */
	//public  Properties      ctx;
	/** Window No - replicated  */
	public  int				windowNO;

	/** AD Window - replicated  */
	// gwu: AD_Window_ID has been replaced with UWindowID below.
	public  UWindowID       uid;


	public  int    			Referenced_Tab_ID;
	/** Tab No (not AD_Tab_ID) 0.. */
	public  int				tabNo;

	/**	Tab	ID			*/
	public	int		    AD_Tab_ID;
	/** Name			*/
	public	String	    name = "";
	/** Description		*/
	public	String	    description = "";
	/** Help			*/
	public	String	    help = "";
	/** Single Row		*/
	public	boolean	    isSingleRow = false;
	public boolean	isInfoTab = false;
	public long seqNo;
	/** Read Only		*/
	public  boolean     isReadOnly = false;
	public boolean isDisplayed = true;
	public boolean isTextSearch = false;

	/** Insert Record	*/
	public 	boolean		isInsertRecord = true;
	public boolean isTranslationTab;
	public boolean isAdvancedTab;
	/** Tree			*/
	public  boolean	    hasTree = false;
	/** Table			*/
	public  int		    AD_Table_ID;
	/** Primary Parent Column   */
	public  int		    AD_Column_ID = 0;
	/** Table Name		*/
	public  String	    tableName;
	/** Table is View	*/
	public  boolean     isView = false;
	/** Table Access Level	*/
	public  String	    accessLevel;
	/** Security		*/
	public  boolean	    isSecurityEnabled = false;
	/** Table Deleteable	*/
	public  boolean	    isDeleteable = false;
	/** Table High Volume	*/
	public  boolean     isHighVolume = false;
	/** Process			*/
	public	int		    AD_Process_ID = 0;
	/** Commot Warning	*/
	public  String	    commitWarning;
	/** Where			*/
	public  String	    whereClause;
	/** Order by		*/
	public  String      orderByClause;
	/** Tab Read Only	*/
	public  String      readOnlyLogic;
	/** Tab Display		*/
	public  String      displayLogic;
	/** Level			*/
	public  int         tabLevel = 0;
	/** Image			*/
	public int          AD_Image_ID = 0;
	/** Included Tab	*/
	public int          Included_Tab_ID = 0;
	/** Replication Type	*/
	public String		replicationType = "L";

	/** Sort Tab			*/
	public boolean		isSortTab = false;
	/** Column Sort			*/
	public int			AD_ColumnSortOrder_ID = 0;
	/** Column Displayed	*/
	public int			AD_ColumnSortYesNo_ID = 0;

	/** Only Current Rows - derived	*/
	public  boolean     onlyCurrentRows = true;
	/**	Only Current Days - derived	*/
	public int			onlyCurrentDays = 0;

	/** Fields contain MFieldVO entities    */
	public String linkColumnName;



	/** Number of columns for a SearchComponent */
	public int numColumns = 3;



	public boolean canExport = false;
	public boolean canReport = false;

	public int AD_InfoWindow_ID = -1;

	/**
	 * Indicates how a particular component should be rendered in the UI.
	 * Possible values are SEARCH, TABLE_SINGLE, TABLE_MULTI, and
	 * TABLE_NOSELECT.
	 * 
	 * @author gwu
	 * 
	 */
	public enum ComponentType {
		/**
		 * A component that renders its FieldVOs as a list of input fields that
		 * the users can use to enter search criteria.
		 */
		SEARCH,
		/**
		 * A component that renders search results in a tabular layout, and
		 * allows only a single row to be selected at a time. Uses the specified
		 * FieldVOs as the specification for the column layout and data type.
		 */
		TABLE_SINGLE, 
		/**
		 * A component that renders search results in a tabular layout, and
		 * allows multiple rows to be selected at a time. Uses the specified
		 * FieldVOs as the specification for the column layout and data type.
		 */
		TABLE_MULTI,
		/**
		 * A component that renders search results in a tabular layout, and does
		 * not allow any row to be selected. Uses the specified FieldVOs as the
		 * specification for the column layout and data type.
		 */
		TABLE_NOSELECT
	};
	//if it is a TABLE_MULTI, whether to disable select all button
	public boolean disableSelectAll = true;
	/**
	 * Indicates how a particular component should be rendered in the UI. 
	 */
	public ComponentType componentType;


	public static String SELECTED_ROWS = "###SELECTED_ROWS###";
	public static String SELECTED_ROW_NOS = "###SELECTED_ROW_NOS###";
	public static final String SELECTED_ROW_INDEXES = "###SELECTED_ROW_INDEXES###";


	public ArrayList<String>	savedQueries = new ArrayList<String>();

	/**
	 * If set, the table will automatically execute search on itself
	 */
	public boolean isAutoRunSearch = false;
	public int AD_Tree_ID = 0;




	public boolean isPrinted()
	{
		return AD_Process_ID != 0;
	}	//	isPrinted

	public boolean isDisplayed(Ctx ctx, Evaluatee eval) {
		//  ** static content **
		//  not displayed
		if (!isDisplayed)
			return false;
		//  no restrictions
		String logic = displayLogic;
		if (logic == null || logic.equals(""))
			return returnDisplayed(ctx);

		//  ** dynamic content **
		if (eval != null)
		{
			boolean retValue = Evaluator.evaluateLogic(eval, logic);
			if(Build.isVerbose())
				log.finest(tableName 
						+ " (" + logic + ") => " + retValue);
			if(!retValue)
				return false;
		}
		return returnDisplayed(ctx);
	}
	private boolean returnDisplayed(Ctx ctx) {
		return (!isAdvancedTab || (isAdvancedTab && "Y".equals(ctx.getContext("#ShowAdvanced"))))
		&&(!isTranslationTab || (isTranslationTab && "Y".equals(ctx.getContext("#ShowTrl")) && "Y".equals(ctx.getContext("#MultiLingual"))))
		&&(!isInfoTab || (isInfoTab && "Y".equals(ctx.getContext("#ShowAcct"))));
	}
	public boolean isReadOnly(Evaluatee e) {
		//if("Y".equals(e.get_ValueAsString("Processed")))
		//return true;
		if(isReadOnly || isView)
			return true;

		//  no restrictions
		if (readOnlyLogic == null || readOnlyLogic.equals(""))
			return isReadOnly;

		//  ** dynamic content **  uses get_ValueAsString
		boolean retValue = Evaluator.evaluateLogic(e, readOnlyLogic);
		//log.finest(m_vo.Name 
		//		+ " (" + m_vo.ReadOnlyLogic + ") => " + retValue);
		return retValue;
	}


	public static final int SEARCH_TAB_NO = 10000;
	@Override
	public boolean equals(Object o) {
		if(o instanceof ComponentVO) {
			ComponentVO oVO = (ComponentVO)o;
			if(oVO.tableName.equals(tableName) && oVO.AD_Tab_ID == AD_Tab_ID )
				return true;
		}
		return false;

	}
	public long getUniqueTableID() {
		//assume tabNO doesn't exceed 100, that's a safe assumption
		return uid.getUniqueID()*100+tabNo;
	}

}

