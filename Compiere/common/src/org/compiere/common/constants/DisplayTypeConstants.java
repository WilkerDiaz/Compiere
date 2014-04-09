package org.compiere.common.constants;

/**
 * 	Display Type Constants
 *	@author Jorg Janke
 */
public class DisplayTypeConstants
{

	/** Display Type 10	String	*/
	public static final int String = 10;
	/** Display Type 11	Integer	*/
	public static final int Integer = 11;
	/** Display Type 12	Amount	*/
	public static final int Amount = 12;
	/** Display Type 13	ID	*/
	public static final int ID = 13;
	/** Display Type 14	Text	*/
	public static final int Text = 14;
	/** Display Type 15	Date	*/
	public static final int Date = 15;
	/** Display Type 16	DateTime	*/
	public static final int DateTime = 16;
	/** Display Type 17	List	*/
	public static final int List = 17;
	/** Display Type 18	Table	*/
	public static final int Table = 18;
	/** Display Type 19	TableDir	*/
	public static final int TableDir = 19;
	/** Display Type 20	YN	*/
	public static final int YesNo = 20;
	/** Display Type 21	Location	*/
	public static final int Location = 21;
	/** Display Type 22	Number	*/
	public static final int Number = 22;
	/** Display Type 23	BLOB	*/
	public static final int Binary = 23;
	/** Display Type 24	Time	*/
	public static final int Time = 24;
	/** Display Type 25	Account	*/
	public static final int Account = 25;
	/** Display Type 26	RowID	*/
	public static final int RowID = 26;
	/** Display Type 27	Color   */
	public static final int Color = 27;
	/** Display Type 28	Button	*/
	public static final int Button = 28;
	/** Display Type 29	Quantity	*/
	public static final int Quantity = 29;
	/** Display Type 30	Search	*/
	public static final int Search = 30;
	/** Display Type 31	Locator	*/
	public static final int Locator = 31;
	/** Display Type 32 Image	*/
	public static final int Image = 32;
	/** Display Type 33 Assignment	*/
	public static final int Assignment = 33;
	/** Display Type 34	Memo	*/
	public static final int Memo = 34;
	/** Display Type 35	PAttribute	*/
	public static final int PAttribute = 35;
	/** Display Type 36	CLOB	*/
	public static final int TextLong = 36;
	/** Display Type 37	CostPrice	*/
	public static final int CostPrice = 37;
	/** Display Type 36	File Path	*/
	public static final int FilePath = 38;
	/** Display Type 39 File Name	*/
	public static final int FileName = 39;
	/** Display Type 40	URL	*/
	public static final int URL = 40;
	/** Display Type 42	PrinterName	*/
	public static final int PrinterName = 42;
	
	/** Display Type 101 Label */
	public static final int Label = 101;
	
	/** Display Type 90 Graph */
	public static final int Graph = 90;
	//	Candidates: 
	
	// Used for ADE web services
	public static String FIELD_TEXT = "Text";
	public static String FIELD_TEXTAREA = "TextArea";
	public static String FIELD_DATETIME = "DateTime";
	public static String FIELD_LIST = "Lookup";
	public static String FIELD_SEARCH = "Search";
	public static String FIELD_NUMERIC = "Numeric";
	public static String FIELD_BUTTON = "Button";
	public static String FIELD_CHECKBOX = "Checkbox";
	
	/**
	 *	- New Display Type
		INSERT INTO AD_REFERENCE
		(AD_REFERENCE_ID, AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,CREATED,CREATEDBY,UPDATED,UPDATEDBY,
		NAME,DESCRIPTION,HELP, VALIDATIONTYPE,VFORMAT,ENTITYTYPE)
		VALUES (90, 0,0,'Y',SysDate,0,SysDate,0,
		'Graph','Graph',null,'D',null,'D');
	 *
	 *  - org.compiere.model.MModel (??)
	 *	- org.compiere.grid.ed.VEditor/Dialog
	 *	- org.compiere.grid.ed.VEditorFactory
	 *	- RColumn, WWindow
	 *  add/check 0_cleanupAD.sql
	 */


}	//	DisplayTypeConstants
