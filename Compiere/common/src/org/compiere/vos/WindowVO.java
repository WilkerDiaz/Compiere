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

import org.compiere.layout.*;

/**
 *  Model Window Value Object
 *
 *  @author dzhao
 *  
 */
public class WindowVO extends ResponseVO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public enum ClientWindowType {
		GENERIC_STACK, SELECTION, SELECTIONAUTO, INVOICE_MATCHING, PAYMENT_ALLOCATION, PAYMENT_SELECTION, PAYMENT_PRINT, BOM_DROP, MATERIAL_TRANSACTION, IMPORT_FILE_LOADER, ADHOC_PRINT_FORMAT
	};
	public ClientWindowType clientWindowType = null;
	
	
	/** Window Number	*/
	public int 		    windowNO;

	/** Window				*/
	
	// gwu:  The AD_Window_ID has been removed.  Use Window.uid instead.
	
	/** Name				*/
	public	String		name = "";
	/** Desription			*/
	public	String		description = "";
	/** Help				*/
	public	String		help = "";
	/** Window Type			*/
	public	String		windowType = "";
	/** Image				*/
	public int          AD_Image_ID = 0;
	/** Color				*/
	public int          AD_Color_ID = 0;
	/** Read Write			*/
	public boolean		isReadWrite = false;
	/** Window Width		*/
	public int			winWidth = 0;
	/** Window Height		*/
	public int			winHeight = 0;
	/** Sales Order Trx		*/
	public boolean		isSOTrx = false;

	/** Tabs contains MTabVO elements   */
	
	public ArrayList<ComponentVO> componentVOs = new ArrayList<ComponentVO>();
	
	/** Layout descriptor */
	public Box layout = null;
	
	
	/** Base Table		*/
	public int 			AD_Table_ID = 0;
	public int 			AD_Role_ID = 0;

	/** Qyery				*/
	public static final String	WINDOWTYPE_QUERY = "Q";
	/** Transaction			*/
	public static final String	WINDOWTYPE_TRX = "T";
	/** Maintenance			*/
	
	public static final String	WINDOWTYPE_MMAINTAIN = "M";
	

	public static final String AD_TABLE_ID = "##AD_Table_ID";
	public static final String RECORD_ID = "##RECORD_ID";
	public static final String TABLE_NAME = "##TABLE_NAME";
	public static final String AD_TAB_ID = "##AD_TAB_ID";
	public static final String QUERY_RESULT_ID = "##QUERY_RESULT_ID";
	public static final String CUR_ROW = "##CUR_ROW";
	public static final String WINDOW_NO = "##WINDOW_NO";
	public static final String DATE_SCHEDULED_START = "##DateScheduledStart";
	 
	public FieldVO getFieldVO(String colName) {
		for(int i=0; i<componentVOs.size(); i++) {
			ComponentVO cVO = componentVOs.get(i);
			for(int j=0; j<cVO.fieldVOs.size(); j++) {
				FieldVO fVO = cVO.fieldVOs.get(j);
				if(colName.equals(fVO.ColumnName))
					return fVO;
			}
		}
		return null;
	}
	
	
} 

