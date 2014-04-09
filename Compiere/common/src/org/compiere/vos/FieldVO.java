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

import java.io.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *  Field Model Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: GridFieldVO.java,v 1.3 2006/07/30 00:58:04 jjanke Exp $
 */
public class FieldVO implements Serializable
{
	/**
	 *  Return the SQL statement used for the MFieldVO.create
	 *  @param ctx context
	 *  @return SQL with or w/o translation and 1 parameter
	 */
	

	static final long serialVersionUID = 4385061125114436797L;
	public static final CompiereLogger	log = new CLoggerSimple(FieldVO.class);

	public static final int MAX_DISPLAY_LENGTH = 40;

	public int maxDisplayLength() {
		// DisplayLength is only Applicable to fields of type String.
		if (FieldType.isText( displayType ) || FieldType.isTextArea( displayType ))	
			return MAX_DISPLAY_LENGTH > DisplayLength ? DisplayLength : MAX_DISPLAY_LENGTH;
		else
			return 0;			
	}
	
	/**	Help			*/
	public String       Help = "";
	/**	Description		*/
	public String       Description = "";
	/** Window No                   */
	public int          WindowNo;
	/** Tab No                      */
	public int          TabNo;
	/** AD_Winmdow_ID               */
	public int          AD_Window_ID;
	/** AD_Tab_ID					*/
	public int			AD_Tab_ID;
	/** Is the Tab Read Only        */
	public boolean      tabReadOnly = false;

	/** Is Process Parameter        */
	public boolean      isProcess = false;

	/**	Column name		*/
	public String       ColumnName = "";
	/**	Column sql		*/
	public String       ColumnSQL;
	/**	Label			*/
	public String       Header = "";
	/**	DisplayType		*/
	public int          displayType = 0;
	/**	Table ID		*/
	public int          AD_Table_ID = 0;
	/**	Clumn ID		*/
	public int          AD_Column_ID = 0;
	/**	Display Length	*/
	public int          DisplayLength = 0;
	/**	Same Line		*/
	public boolean      IsSameLine = false;
	/**	Displayed		*/
	public boolean      IsDisplayed = false;
	/**	Dislay Logic	*/
	public String       DisplayLogic = "";
	/**	Default Value	*/
	public String       DefaultValue = "";
	/**	Mandatory Entry		*/
	public boolean      IsMandatoryUI = false;
	/**	Read Only		*/
	public boolean      IsReadOnly = false;
	/**	Updateable		*/
	public boolean      IsUpdateable = false;
	/**	Always Updateable	*/
	public boolean      IsAlwaysUpdateable = false;
	/**	Heading Only	*/
	public boolean      IsHeading = false;
	/**	Field Only		*/
	public boolean      IsFieldOnly = false;
	/**	Display Encryption	*/
	public boolean      IsEncryptedField = false;
	/**	Storage Encryption	*/
	public boolean      IsEncryptedColumn = false;
	/**	Find Selection		*/
	public boolean		IsSelectionColumn = false;
	/** Selection Seq */
	public int			SelectionSeqNo = 0;
	/**	Order By		*/
	public int          SortNo = 0;
	/**	Field Length		*/
	public int          FieldLength = 0;
	/**	Format enforcement		*/
	public String       VFormat = "";
	/**	Min. Value		*/
	public String       ValueMin = "";
	/**	Max. Value		*/
	public String       ValueMax = "";
	/**	Field Group		*/
	public String       FieldGroup = "";
	/**	PK				*/
	public boolean      IsKey = false;
	/**	FK				*/
	public boolean      IsParent = false;
	/**	Process			*/
	public int          AD_Process_ID = 0;
	/**	Read Only Logic	*/
	public String       ReadOnlyLogic = "";
	/**	Display Obscure	*/
	public String		ObscureType = null;
	/** Default Focus	*/
	public boolean		IsDefaultFocus = false;
	/**	Field Only		*/
	public boolean      IsTextSearch = false;

	/**	Lookup Validation code	*/
	public String		ValidationCode = "";
	/**	Reference Value			*/
	public int			AD_Reference_Value_ID = 0;

	/**	Process Parameter Range		*/
	public boolean      isRange = false;
	/**	Process Parameter Value2	*/
	public String       DefaultValue2 = "";
	

	public int          AD_Field_ID;
	public int          AD_Reference_ID;
	public int          AD_Val_Rule_ID;

	/**	Label			*/
	public String       name = "";
	public String       label = "";

	public int seqNo;
	/**	Table ID		*/
	public boolean      IsIdentifier = false;
	public boolean      IsTranslated = false;
	//*Mandaotyr logic*/
	public String mandatoryLogic;
	public boolean IsVirtualColumn = false;

	public String tableName = null;
	
	public boolean IsCopy = false;
	
	
	public int hotKey = 1;
	public int mrSeqNo = -1;
	

	public ListBoxVO listBoxVO=null;
	
	public boolean isImpactsValue=false;
	public boolean isImpactsUITab=false;
	public boolean isDependentValue = false;
	public boolean isImpactsUI=false;
	
	public boolean hasDisplayLogic() {
		return (DisplayLogic!= null && !DisplayLogic.trim().equals(""));
	}

	public boolean canDisplay() {
		return (hasDisplayLogic() || IsDisplayed);
	}
	public boolean hasReadOnlyLogic() {
		return (ReadOnlyLogic!= null && !ReadOnlyLogic.trim().equals(""));
	}
	
	/**
	 *	Is it Editable - checks IsActive, IsUpdateable, and isDisplayed
	 *  @param ctx optional checks context for Active, IsProcessed, LinkColumn
	 *  @param WindowNo window
	 *  @param tab sibling tab
	 *  @param inserting 
	 *  @return true, if editable
	 */
	public boolean isEditable (Ctx ctx, Evaluatee eval, boolean tabReadOnly, boolean rowReadOnly, String tabLinkColumn, boolean inserting)
	{
		
		if (IsVirtualColumn)
			return false;
		//  Fields always enabled (are usually not updateable)
		if ((ColumnName.equals("Posted")
			|| ColumnName.equals("Record_ID")) && displayType == DisplayTypeConstants.Button)	//  Zoom
			return true;

		//  Fields always updareable
		if (IsAlwaysUpdateable && !tabReadOnly)      //  Zoom
			return true;
		
		//  Tab or field is R/O
		if (tabReadOnly || IsReadOnly)
		{
			if(Build.isVerbose())
			log.finest(ColumnName + " NO - TabRO=" + tabReadOnly + ", FieldRO=" + IsReadOnly);
			return false;
		}

		//	Not Updateable - only editable if new updateable row
		if (!IsUpdateable && !inserting)
		{
			if(Build.isVerbose())
			log.finest(ColumnName + " NO - FieldUpdateable=" + IsUpdateable);
			return false;
		}

		//	Field is the Link Column of the tab
		String linkColumn = tabLinkColumn;
		if (ColumnName.equals(linkColumn))
		{
			if(Build.isVerbose())
			log.finest(ColumnName + " NO - LinkColumn");
			return false;
		}

		//FIXME comment this out need to go back server for this information
		//	Role Access & Column Access			
	//	if (ctx != null)
	//	{
	//		int AD_Client_ID = ctx.getAD_Client_ID(WindowNo);
	//		int AD_Org_ID = ctx.getAD_Org_ID(WindowNo);
	//		String keyColumn = tabKeyColumn;
	//		if ("EntityType".equals(keyColumn))
	//			keyColumn = "AD_EntityType_ID";
	//		if (!keyColumn.endsWith("_ID"))
	//			keyColumn += "_ID";			//	AD_Language_ID
	//		int Record_ID = ctx.getContextAsInt(keyColumn);
		//	MRole role = MRole.getDefault(ctx, false); 
			//if (!role.canUpdate(AD_Client_ID, AD_Org_ID, AD_Table_ID, Record_ID, false))
				//return false;
			//if (!role.isColumnAccess(AD_Table_ID, AD_Column_ID, false))
		//		return false;
	//	}
		if(rowReadOnly)
			return false;

		if(!isColumnAccessReadOnly)
			return false;
		//  Do we have a readonly rule
		String logic = ReadOnlyLogic;
		if (logic != null && logic.length() > 0)
		{
			boolean retValue = !Evaluator.evaluateLogic(eval, logic);
			if(Build.isVerbose())
			log.finest(ColumnName + " R/O(" + logic + ") => R/W-" + retValue);
			if (!retValue)
				return false;
		}

		//  Always editable if Active
		if (ColumnName.equals("Processing")
				|| ColumnName.equals("DocAction")
				|| ColumnName.equals("GenerateTo"))
			return true;

		//  Record is Processed	***	
		if (ctx != null 
			&& (ctx.isProcessed(WindowNo)
				|| ctx.isProcessing(WindowNo)))
			return false;

		//  IsActive field is editable, if record not processed
		if (ColumnName.equals("IsActive"))
			return true;

		//  Record is not Active
		if (ctx != null 
			&& !ctx.isActive(WindowNo)) {
			if(Build.isVerbose())
			log.finest(ColumnName + " Record not active");
			return false;
		}

		
		if(inserting)
			return true;

		//  ultimately visibily decides

		// We used to return isDisplayed (eval) here, but that logic is now
		// moved outside of this function to prevent the relatively expensive
		// call from being made multiple times on the same field
		
		// isDisplayed (eval)
		return true;
	}	//	isEditable
	

	public boolean isMandatory (Evaluatee eval)
	{
		//	Mandatory Logic
		if (mandatoryLogic != null && mandatoryLogic.length() > 0)
		{
			if (Evaluator.evaluateLogic (eval, mandatoryLogic))
				return true;
		}
		
		//	Not Mandatory
		if (!IsMandatoryUI || IsVirtualColumn)
			return false;

		//  Numeric Keys and Created/Updated as well as 
		//	DocumentNo/Value/ASI ars not mandatory (persistency layer manages them)
		if ((IsKey && ColumnName.endsWith("_ID"))
				|| ColumnName.startsWith("Created") || ColumnName.startsWith("Updated")
				|| ColumnName.equals("Value") 
				|| ColumnName.equals("DocumentNo")
				|| ColumnName.equals("M_AttributeSetInstance_ID"))	//	0 is valid
			return false;

		
		//  Mandatory only if displayed
		
		// We used to return isDisplayed (eval) here, but that logic is now
		// moved outside of this function to prevent the relatively expensive
		// call from being made multiple times on the same field
		
		// return isDisplayed (eval);		
		return true;
	}	//	isMandatory
	
	
	/**
	 *	Field is Displayed
	 *  @param ctx optional checks context
	 *	@return true if displayed
	 */
	public boolean isDisplayed (Evaluatee eval)
	{
		//  ** static content **
		//  not displayed
		if (!IsDisplayed)
			return false;
		if (!isColumnAccessDisplay)
			return false;
		//  no restrictions
		String logic = DisplayLogic;
		if (logic == null || logic.equals(""))
			return true;

		//  ** dynamic content **
		if (eval != null)
		{
			//if(logic.indexOf("@OrderType") == 0)
				//System.out.println(logic);

			boolean retValue = Evaluator.evaluateLogic(eval, logic);
			if(Build.isVerbose())
			log.finest(ColumnName 
				+ " (" + logic + ") => " + retValue);
			return retValue;
		}
		return true;
	}	//	isDisplayed
	
	
	
	public boolean isQueryCriteria = false;
	public String  selectClause = null;
	
	
	public FieldVO(String columnName, String name, int displayType) {
		this.ColumnName = columnName;
		this.name = name.replaceAll( "&", "" );
		this.displayType = displayType;
		this.IsDisplayed = true;
	}
	
	public FieldVO(String columnName, String name, int displayType, boolean isMandatoryUI ) 
	{
		this( columnName, name, displayType );
		this.IsMandatoryUI = isMandatoryUI;
	}
	
	
	public FieldVO(String columnName, String name, String colSQL, int displayType,  boolean readOnly) {
		this(columnName, name, colSQL, displayType, readOnly, true, false);
	}

	
	public FieldVO(String columnName, String name, String colSQL, int displayType,  boolean readOnly, boolean isDisplayed, boolean isKey) {
		this(columnName, name, displayType);
		this.ColumnSQL = colSQL;
		this.selectClause = colSQL;
		this.IsReadOnly = readOnly;
		this.IsDisplayed = isDisplayed;
		this.IsKey = isKey;
	}
	
	public FieldVO(String columnName, String name, String colSQL, int displayType,  boolean readOnly, boolean isDisplayed, boolean isKey, boolean isMandatoryUI) {
		this(columnName, name, colSQL, displayType, readOnly, isDisplayed, isKey);
		this.IsMandatoryUI = isMandatoryUI;
	}

	public FieldVO() {
		
	}
	
	
	public FieldVO copySearch()
	{
		FieldVO f = new FieldVO();

		f.AD_Column_ID = this.AD_Column_ID;
		f.AD_Field_ID = this.AD_Field_ID;
		f.AD_Tab_ID = this.AD_Tab_ID;
		f.AD_Table_ID = this.AD_Table_ID;
		f.ColumnName = this.ColumnName;
		f.tableName = this.tableName;
		if(this.displayType == DisplayTypeConstants.Text)
			f.displayType = DisplayTypeConstants.String;
		else
			f.displayType = this.displayType;
		
		f.DisplayLength = this.DisplayLength;
		f.FieldLength = this.FieldLength;
		f.IsDisplayed = this.IsDisplayed;
		f.ValidationCode = this.ValidationCode;
		
		if(this.listBoxVO != null){
			ListBoxVO searchList = new ListBoxVO( this.listBoxVO.getOptions(), this.listBoxVO.getDefaultKey() );
			searchList.Column_ID = this.listBoxVO.Column_ID;
			searchList.TableName = this.listBoxVO.TableName;
			searchList.AD_Reference_Value_ID = this.listBoxVO.AD_Reference_Value_ID;
			searchList.IsCreadedUpdatedBy = this.listBoxVO.IsCreadedUpdatedBy;
			// created and updated by -> search instead of simple lookup
			if (this.listBoxVO.IsCreadedUpdatedBy)
				f.displayType = DisplayTypeConstants.Search;
			searchList.IsValidated = this.listBoxVO.IsValidated;
			searchList.IsParent = this.listBoxVO.IsParent;
			searchList.KeyColumn = this.listBoxVO.KeyColumn;
			searchList.QueryDirect = this.listBoxVO.QueryDirect;
			searchList.ValidationCode = this.listBoxVO.ValidationCode;
			searchList.zoomWindow = this.listBoxVO.zoomWindow;
			searchList.ZoomWindowPO = this.listBoxVO.ZoomWindowPO;
			f.listBoxVO = searchList;
		}

		// these are needed by InfoWindow
		f.name = this.name;
		f.Description = this.Description;
		f.Help = this.Help;
		f.isQueryCriteria = this.isQueryCriteria;
		f.AD_Reference_ID = this.AD_Reference_ID;
		f.isRange = this.isRange;
		f.selectClause = this.selectClause;
		f.DefaultValue = this.DefaultValue;
		f.isImpactsValue = this.isImpactsValue;		
		f.IsIdentifier = this.IsIdentifier;

		f.WindowNo = this.WindowNo;
		f.TabNo = ComponentVO.SEARCH_TAB_NO;
		
		return f;
	}
	public FieldVO copySearchAdvanced()
	{
		FieldVO f = new FieldVO();

		f.AD_Column_ID = this.AD_Column_ID;
		f.AD_Field_ID = this.AD_Field_ID;
		f.AD_Tab_ID = this.AD_Tab_ID;
		f.AD_Table_ID = this.AD_Table_ID;
		f.ColumnName = this.ColumnName;
		f.tableName = this.tableName;
		if(this.displayType == DisplayTypeConstants.Text)
			f.displayType = DisplayTypeConstants.String;
		else
			f.displayType = this.displayType;
		
		f.DisplayLength = this.DisplayLength;
		f.FieldLength = this.FieldLength;
		f.IsDisplayed = true;
		f.IsReadOnly = false;
		f.IsAlwaysUpdateable = this.IsAlwaysUpdateable;
		f.IsMandatoryUI = false;
		f.isDependentValue = this.isDependentValue;
		f.AD_Reference_Value_ID = this.AD_Reference_Value_ID;
		f.AD_Window_ID = this.AD_Window_ID;
		
		
		if(this.listBoxVO != null){
			ListBoxVO searchList = new ListBoxVO(false);
			searchList.Column_ID = this.listBoxVO.Column_ID;
			searchList.TableName = this.listBoxVO.TableName;
			searchList.AD_Reference_Value_ID = this.listBoxVO.AD_Reference_Value_ID;
			searchList.IsCreadedUpdatedBy = this.listBoxVO.IsCreadedUpdatedBy;
			// created and updated by -> search instead of simple lookup
			if (this.listBoxVO.IsCreadedUpdatedBy)
				f.displayType = DisplayTypeConstants.Search;
			
			searchList.IsValidated = this.listBoxVO.IsValidated;
			searchList.IsParent = this.listBoxVO.IsParent;
			searchList.KeyColumn = this.listBoxVO.KeyColumn;
			searchList.QueryDirect = this.listBoxVO.QueryDirect;
			searchList.ValidationCode = this.listBoxVO.ValidationCode;
			searchList.zoomWindow = this.listBoxVO.zoomWindow;
			searchList.ZoomWindowPO = this.listBoxVO.ZoomWindowPO;
			/*
			List searchOptions = this.listBoxVO.getOptions();
			if(searchOptions != null)
				for(int i=0; i < searchOptions.size(); i++){
					searchList.addOption((NamePair)searchOptions.get(i));
				}
				*/
			f.listBoxVO = searchList;
		}

		// these are needed by InfoWindow
		f.name = this.name;
		f.Description = this.Description;
		f.Help = this.Help;
		f.isQueryCriteria = this.isQueryCriteria;
		f.AD_Reference_ID = this.AD_Reference_ID;
		f.isRange = this.isRange;
		f.selectClause = this.selectClause;
		f.DefaultValue = this.DefaultValue;
		f.isImpactsValue = this.isImpactsValue;		
		f.IsIdentifier = this.IsIdentifier;
		f.SelectionSeqNo = this.SelectionSeqNo;

		f.WindowNo = this.WindowNo;
		f.TabNo = ComponentVO.SEARCH_TAB_NO;
		return f;
	}
	
	public static String getToColumnName( String columnName )
	{
		return columnName + "_2";
	}
	
	
	
	public String numberFormatPattern = "###,###,###,###,###,###.######";
	public String dateFormatPattern = "yyyy-MM-dd";
	public boolean isColumnAccessReadOnly = true;
	public boolean isColumnAccessDisplay = true;
	@Override
	public String toString() {
		return ColumnName;
	}
	
	
}
