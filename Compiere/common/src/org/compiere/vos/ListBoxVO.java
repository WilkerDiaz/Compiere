/**
 * 
 */
package org.compiere.vos;

import java.io.*;
import java.util.*;

import org.compiere.common.constants.*;
import org.compiere.util.*;
/**
 * @author gwu
 *
 */
public class ListBoxVO implements Serializable 
{
	public static final CompiereLogger	log = new CLoggerSimple(ListBoxVO.class);
	/** SQL Query       */
	public String       Query = null;
	/** Table Name      */
	public String       TableName = "";
	/** Key Column      */
	public String       KeyColumn = "";
	/** Zoom Window     */
	public int          zoomWindow;
	/** Zoom Window     */
	public int          ZoomWindowPO;
	/** Direct Access Query 	*/
	public String       QueryDirect = "";
	/** Parent Flag     */
	public boolean      IsParent = false;
	/** Key Flag     	*/
	public boolean      IsKey = false;
	/** Validation code */
	public String       ValidationCode = "";
	/** Validation flag */
	public boolean      IsValidated = false;

	/**	AD_Column_Info or AD_Process_Para	*/
	public int          Column_ID;
	/** Real AD_Reference_ID				*/
	public int 			AD_Reference_Value_ID;
	/** CreadedBy?updatedBy					*/
	public boolean		IsCreadedUpdatedBy = false;





	private static final long serialVersionUID = 1L;

	private String m_defaultKey = null;

	// do not change to HashMap; using an array to maintain sorted order
	private ArrayList<NamePair> m_options = null;
	private ArrayList<NamePair> m_exception_options = null;

	public void setExceptionOptions(ArrayList<NamePair> exceptionOptions) {
		if(m_exception_options == null)
			m_exception_options = new ArrayList<NamePair>();
		concatNamePairArray(m_exception_options, exceptionOptions);
	}

	public void setOptions(ArrayList<NamePair> validOptions) {
		if(Build.isDebug())
			log.finest("setOptions(before):"+this);
		m_options = validOptions;
		if(Build.isDebug())
			log.finest("setOptions(after):"+this);
	}

	public void addOption(NamePair option) {
		addOption(m_options, option);
	}

	public static void addOption(ArrayList<NamePair> options, NamePair option) {
		if(option != null) {
			int idx = NamePair.indexOfKey(options, option.getID()); 
			if( idx != -1) {
				options.remove(idx);
				options.add(idx, option);
			}
			else 
				options.add(option);
		}
		else
			throw new IllegalArgumentException("option should not be null");
	}

	public void updateOption(ArrayList<NamePair> options, NamePair option) {
		if(option != null) {
			int idx = NamePair.indexOfKey(options, option.getID()); 
			if( idx != -1) {
				options.remove(idx);
				options.add(idx, option);
			}
		}
		else
			throw new IllegalArgumentException("option should not be null");
	}

	public void clearExceptionOptions() {
		if(m_exception_options != null)
			m_exception_options.clear();
	}
	public void updateExceptionOption(NamePair option) {
		if(m_exception_options == null)
			return;
		updateOption(m_exception_options, option);
	}
	public void pushExceptionOption(NamePair option) {

		if(m_exception_options == null)
			m_exception_options = new ArrayList<NamePair>(10);
		addOption(m_exception_options, option);
	}

	public void pushExceptionOptions(ArrayList<NamePair> options) {
		if(Build.isDebug())
			log.finest("pushExceptionOptions(before):"+this);

		for(int i=0; i<options.size(); i++) {
			pushExceptionOption(options.get(i));
		}
		if(Build.isDebug())
			log.finest("pushExceptionOptions(before):"+this);

	}


	public static void concatNamePairArray(ArrayList<NamePair> options, ArrayList<NamePair> c) {
		if(c == null) 
			return;
		if(options == null)
			throw new IllegalArgumentException("options cannot be null");
		if(options != null)
			for(int i=0; i<c.size(); i++)
				addOption(options, c.get(i));
	}



	public ListBoxVO()
	{
		this(true);
	}

	public ListBoxVO(boolean init) {
		if(init) {
			m_options = new ArrayList<NamePair>(4);
			this.m_defaultKey = "";
		}
	}
	public ListBoxVO(NamePair[] p_options, String p_defaultKey )
	{
		ArrayList<NamePair> options = new ArrayList<NamePair>(p_options.length);
		for (NamePair element : p_options)
			options.add(element);
		create(options, p_defaultKey);
	}

	public ListBoxVO(ArrayList<NamePair> p_options, String p_defaultKey ) {
		create(p_options, p_defaultKey);
	}

	private void create(ArrayList<NamePair> p_options, String p_defaultKey )
	{
		m_options = p_options;
		if( null == m_options ) {
			m_options = new ArrayList<NamePair>(); 
		}
		m_defaultKey = p_defaultKey;
	}

	public String getDefaultKey()
	{
		return m_defaultKey;
	}

	public void setDefaultKey( String defaultKey )
	{
		m_defaultKey = defaultKey;
	}

	public ArrayList<NamePair> getOptions()
	{
		return m_options;
	}

	/**
	 * Returns the index of the first option found for the key, or -1 if not found.
	 * @param key
	 * @return
	 */
	public int indexOfKey( String key )
	{
		return NamePair.indexOfKey(m_options, key);
	}

	/**
	 * Returns the index of the first option found for the key, or -1 if not found.
	 * @param value
	 * @return
	 */
	public int indexOfValue( String value )
	{
		return NamePair.indexOfValue(m_options, value);
	}


	public String getValue( String key )
	{
		NamePair option = getOption(key);
		if(option == null)
			return null;
		return option.getName();
	}

	public NamePair getValidOption( String key ) {
		return getFromOptions(key, m_options);
	}

	public NamePair getExceptionOption( String key ) {
		return getFromOptions(key, m_exception_options);
	}

	public NamePair getFromOptions( String key, ArrayList<NamePair> options) {
		int index = NamePair.indexOfKey(options, key );
		if( index >= 0 )
			return options.get(index);
		return null;
	}

	public NamePair getOption( String key ) {
		NamePair option = getValidOption(key);

		if(option == null)
			option = getExceptionOption(key);

		return option;
	}

	public ArrayList<NamePair> getExceptionOptions() {
		return this.m_exception_options;
	}
	@Override
	public String toString() {
		return "m_options:"+m_options+"m_exception_options:"+m_exception_options;
	}

	public boolean isOptionsSet() {
		return m_options != null;
	}
}