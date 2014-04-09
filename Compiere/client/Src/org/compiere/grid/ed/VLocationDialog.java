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

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Dialog to enter Location Info (Address)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VLocationDialog.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VLocationDialog extends CDialog implements ActionListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 *
	 * @param frame parent
	 * @param title title (field name)
	 * @param location Model Location
	 */
	public VLocationDialog (Frame frame, String title, MLocation location)
	{
		super(frame, title, true);
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, ex.getMessage());
		}

		MTable table = MTable.get(Env.getCtx(), "C_Location");
		MColumn col1 = table.getColumn("Address1");
		MColumn col2 = table.getColumn("Address2");
		MColumn col3 = table.getColumn("Address3");
		MColumn col4 = table.getColumn("Address4");
		fAddress1 = new VString(col1.getColumnName(), col1.isMandatoryUI(), !col1.isUpdateable(), col1.isUpdateable(),
				20, col1.getFieldLength(), col1.getVFormat(), null);
		fAddress2 = new VString(col2.getColumnName(), col2.isMandatoryUI(), !col2.isUpdateable(), col2.isUpdateable(),
				20, col2.getFieldLength(), col2.getVFormat(), null);
		fAddress3 = new VString(col3.getColumnName(), col3.isMandatoryUI(), !col3.isUpdateable(), col3.isUpdateable(),
				20, col3.getFieldLength(), col3.getVFormat(), null);
		fAddress4 = new VString(col4.getColumnName(), col4.isMandatoryUI(), !col4.isUpdateable(), col4.isUpdateable(),
				20, col4.getFieldLength(), col4.getVFormat(), null);
		
		
		m_location = location;
		if (m_location == null) {
			m_location = m_tempLocation;
		}

		//	Overwrite title
		if (m_location.getC_Location_ID() == 0)
			setTitle(Msg.getMsg(Env.getCtx(), "LocationNew"));
		else
			setTitle(Msg.getMsg(Env.getCtx(), "LocationUpdate"));

		//	Current Country
		MCountry.setDisplayLanguage(Env.getAD_Language(Env.getCtx()));
		fCountry = new CComboBox(MCountry.getCountries(Env.getCtx()));
		fCountry.setSelectedItem(m_location.getCountry());
		m_origCountry_ID = m_location.getC_Country_ID();
		//	Current Region
		fRegion = new CComboBox(MRegion.getRegions(Env.getCtx(), m_origCountry_ID));
		if (m_location.getCountry().isHasRegion())
			lRegion.setText(m_location.getCountry().getRegionName());	//	name for region
		fRegion.setSelectedItem(m_location.getRegion());
		//
		fCity = new CComboBox(loadAllCity(m_location.getCountryName()));
		initLocation();
		
		/* Jorge Pires */
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		fCountry.addActionListener(this);
		AEnv.positionCenterWindow(frame, this);
	}	//	VLocationDialog

	private boolean 	m_change = false;
	private MLocation	m_location;
	private final MLocation	m_tempLocation = new MLocation (Env.getCtx(), 0, null);
	private final int			m_origCountry_ID;
	private int			s_oldCountry_ID = 0;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VLocationDialog.class);

	private final CPanel panel = new CPanel();
	private final CPanel mainPanel = new CPanel();
	private final CPanel southPanel = new CPanel();
	private final BorderLayout panelLayout = new BorderLayout();
	private final GridBagLayout gridBagLayout = new GridBagLayout();
	private final ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private final BorderLayout southLayout = new BorderLayout();
	//
	private final CLabel		lAddress1   = new CLabel(Msg.getMsg(Env.getCtx(), "Address")+ " 1");
	private final CLabel		lAddress2   = new CLabel(Msg.getMsg(Env.getCtx(), "Address")+ " 2");
	private final CLabel		lAddress3   = new CLabel(Msg.getMsg(Env.getCtx(), "Address")+ " 3");
	private final CLabel		lAddress4   = new CLabel(Msg.getMsg(Env.getCtx(), "Address")+ " 4");
	private final CLabel		lCity       = new CLabel(Msg.getMsg(Env.getCtx(), "City"));
	private final CLabel		lCountry    = new CLabel(Msg.getMsg(Env.getCtx(), "Country"));
	private final CLabel		lRegion     = new CLabel(Msg.getMsg(Env.getCtx(), "Region"));
	private final CLabel		lPostal     = new CLabel(Msg.getMsg(Env.getCtx(), "Postal"));
	private final CLabel		lPostalAdd  = new CLabel(Msg.getMsg(Env.getCtx(), "PostalAdd"));
	private final CTextField	fAddress1;
	private final CTextField	fAddress2;
	private final CTextField	fAddress3;
	private final CTextField	fAddress4;
	
	private int countrySelected = 0;
	private int citySelected = 0;
	
	
	//private final CTextField	fCity  		= new CTextField(15);		//	length=60
	private final CComboBox	fCountry;
	private CComboBox	fRegion;
	private final CTextField	fPostal 	= new CTextField(5);		//	length=10
	private final CTextField	fPostalAdd	= new CTextField(5);		//	length=10
	private CComboBox 	fCity;
	/** The "to link" key  */
	private static final String TO_LINK = "ToLink";

	/** The "to link" Button  */
	private final AppsAction 	m_toMapAction =
		new AppsAction(TO_LINK, null, Msg.getMsg(Env.getCtx(), TO_LINK));

	//
	private final GridBagConstraints gbc = new GridBagConstraints();
	private final Insets labelInsets = new Insets(2,15,2,0);		// 	top,left,bottom,right
	private final Insets fieldInsets = new Insets(2,5,2,10);

	/*
	 * Parte de Codigo que se esta modificando
	 * 
	 */
	

	/**
	 * 	Load active Countries (no summary).
	 * 	Set Default Language to Client Language
	 *	@param ctx context
	 */
	private KeyNamePair[] loadAllCity(String Valor)
	{
		/*MLanguage lang = MLanguage.get(ctx, ctx.getContext("#AD_Language"));
		MCountry usa = null;*/
		//
		
		KeyNamePair[] VCity;
		
		String sql = "SELECT NAME, C_CITY_ID FROM C_CITY WHERE IsActive='Y' and C_COUNTRY_ID = (select C_COUNTRY_ID " +
																							   "from C_COUNTRY " +
																							   "where name = '" + Valor +"' )" ;
		try
		{
			Statement stmt = DB.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
			ResultSet rs = stmt.executeQuery(sql);
			
			rs.last();
			VCity = new KeyNamePair[rs.getRow()+1];
			
			rs.beforeFirst();
			
			while(rs.next()){
				String AuxCityName = rs.getString("NAME");
				Integer AuxCityID = rs.getInt("C_CITY_ID");
				VCity[rs.getRow()] = new KeyNamePair(AuxCityID,AuxCityName);
				
				//VCity.add(new KeyNamePair(AuxCityID,AuxCityName));
				/*MCountry c = new MCountry (ctx, rs, null);
				s_countries.put(String.valueOf(c.getC_Country_ID()), c);
				//	Country code of Client Language
				if (lang != null && lang.getCountryCode().equals(c.getCountryCode()))
					s_default = c;
				if (c.getC_Country_ID() == 100)		//	USA
					usa = c;*/
			}
			rs.close();
			stmt.close();
			return VCity;
		}
		catch (SQLException e){
			e.printStackTrace();
			log.saveError("SQLError", Msg.getMsg(Env.getCtx(), e.getMessage()));
			return null;
		}
		/*		if (s_default == null)
			s_default = usa;
		s_log.fine("#" + s_countries.size() 
			+ " - Default=" + s_default);*/
	}	//	loadAllCity
	
	
	
	
	/**
	 *	Static component init
	 *  @throws Exception
	 */
	void jbInit() throws Exception
	{
		panel.setLayout(panelLayout);
		southPanel.setLayout(southLayout);
		mainPanel.setLayout(gridBagLayout);
		panelLayout.setHgap(5);
		panelLayout.setVgap(10);
		getContentPane().add(panel);
		panel.add(mainPanel, BorderLayout.CENTER);
		panel.add(southPanel, BorderLayout.SOUTH);
		southPanel.add(confirmPanel, BorderLayout.CENTER);

		m_toMapAction.getButton().setMargin(ConfirmPanel.s_insets);
		m_toMapAction.setDelegate(this);
		confirmPanel.addComponent(m_toMapAction.getButton());
		//
		confirmPanel.addActionListener(this);
	}	//	jbInit

	/**
	 * 	Set Country Format (City-Postal-..)
	 */
	private void renderCountrySpecificFields()
	{
		mainPanel.remove(lCity);
		mainPanel.remove(fCity);
		mainPanel.remove(lPostal);
		mainPanel.remove(fPostal);
		mainPanel.remove(lPostalAdd);
		mainPanel.remove(fPostalAdd);
		mainPanel.remove(lRegion);
		mainPanel.remove(fRegion);

		MCountry country = m_location.getCountry();
		if ((country.getC_Country_ID() != s_oldCountry_ID) && country.isHasRegion())
		{
			fRegion = new CComboBox(MRegion.getRegions(Env.getCtx(), country.getC_Country_ID()));
			if (m_location.getRegion() != null)
				fRegion.setSelectedItem(m_location.getRegion());
			lRegion.setText(country.getRegionName());
			s_oldCountry_ID = m_location.getC_Country_ID();
		}
		//  sequence of City Postal Region - @P@ @C@ - @C@, @R@ @P@
		String ds = country.getDisplaySequence();
		if ((ds == null) || (ds.length() == 0))
		{
			log.log(Level.SEVERE, "DisplaySequence empty - " + country);
			ds = "";	//	@C@,  @P@
		}
		int line = 5;
		StringTokenizer st = new StringTokenizer(ds, "@", false);
		while (st.hasMoreTokens())
		{
			String s = st.nextToken();
			/*if (s.startsWith("C"))
				addLine(line++, lCity, fCity);
			else*/ if (s.startsWith("P"))
				addLine(line++, lPostal, fPostal);
			else if (s.startsWith("A"))
				addLine(line++, lPostalAdd, fPostalAdd);
			else if (s.startsWith("R") && m_location.getCountry().isHasRegion())
				addLine(line++, lRegion, fRegion);
		}
	}	//	renderCountrySpecificFields

	/**
	 *	Dynanmic Init & fill fields - Called when Country changes!
	 */
	private void initLocation()
	{
		MCountry country = m_location.getCountry();
		log.fine(country.getName() + ", Region=" + country.isHasRegion() + " " + country.getDisplaySequence()
			+ ", C_Location_ID=" + m_location.getC_Location_ID());
		//	new Region
		if ((m_location.getC_Country_ID() != s_oldCountry_ID) && country.isHasRegion())
		{
			fRegion = new CComboBox(MRegion.getRegions(Env.getCtx(), country.getC_Country_ID()));
			if (m_location.getRegion() != null)
				fRegion.setSelectedItem(m_location.getRegion());
			lRegion.setText(country.getRegionName());
			s_oldCountry_ID = m_location.getC_Country_ID();
		}

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridy = 0;			//	line
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.insets = fieldInsets;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0;

		mainPanel.add(Box.createVerticalStrut(5), gbc);    	//	top gap

		int line = 1;
		addLine(line++, lAddress1, fAddress1);
		addLine(line++, lAddress2, fAddress2);
		addLine(line++, lAddress3, fAddress3);
		addLine(line++, lAddress4, fAddress4);

		renderCountrySpecificFields();

		//  Country Last
		line = 9;
		addLine(line++, lCountry, fCountry);
		addLine(line++, lCity, fCity);

		//	Set Max Length
		MTable table = MTable.get(Env.getCtx(), X_C_Location.Table_ID);
		MColumn col = table.getColumn("Address1");
		fAddress1.setMaxLength(col.getFieldLength());
		col = table.getColumn("Address2");
		fAddress2.setMaxLength(col.getFieldLength());
		col = table.getColumn("Address3");
		fAddress3.setMaxLength(col.getFieldLength());
		col = table.getColumn("Address4");
		fAddress4.setMaxLength(col.getFieldLength());
		//
		//col = table.getColumn("City");
		//fCity.setMaxLength(col.getFieldLength());
		col = table.getColumn("Postal");
		fPostal.setMaxLength(col.getFieldLength());
		col = table.getColumn("Postal_Add");
		fPostalAdd.setMaxLength(col.getFieldLength());

		//	Fill it
		if (m_location.getC_Location_ID() != 0)
		{
			fAddress1.setText(m_location.getAddress1());
			fAddress2.setText(m_location.getAddress2());
			fAddress3.setText(m_location.getAddress3());
			fAddress4.setText(m_location.getAddress4());
			Integer AuxCityID = null;
			String AuxCityName = null;
			
			String sql = "SELECT NAME, C_CITY_ID FROM C_CITY WHERE IsActive='Y' and name = '" + m_location.getCity() +"'";
			try
			{
				Statement stmt = DB.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
				ResultSet rs = stmt.executeQuery(sql);
				
				if (rs.next()){
					AuxCityID = rs.getInt("C_CITY_ID");
					AuxCityName = rs.getString("NAME");
				}
				rs.close();
				stmt.close();

			}
			catch (SQLException e){
				log.saveError("SQLError", Msg.getMsg(Env.getCtx(), e.getMessage()));
			}
			
			if(AuxCityID != null && AuxCityName != null){
				fCity.setSelectedItem( new KeyNamePair(AuxCityID,AuxCityName) );
			}else{
				
			}
			
			fPostal.setText(m_location.getPostal());
			fPostalAdd.setText(m_location.getPostal_Add());
			if (m_location.getCountry().isHasRegion())
			{
				lRegion.setText(m_location.getCountry().getRegionName());
				fRegion.setSelectedItem(m_location.getRegion());
			}
			fCountry.setSelectedItem(country);
		}
		//	Update UI
		pack();
		
		countrySelected = fCountry.getSelectedIndex();
		citySelected= fCity.getSelectedIndex();
	}	//	initLocation

	/**
	 *	Add Line to screen
	 *
	 *  @param line line number (zero based)
	 *  @param label label
	 *  @param field field
	 */
	private void addLine(int line, JLabel label, JComponent field)
	{
		gbc.gridy = line;
		//	label
		gbc.insets = labelInsets;
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		mainPanel.add(label, gbc);
		//	Field
		gbc.insets = fieldInsets;
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.NONE;
		mainPanel.add(field, gbc);
	}	//	addLine


	/**
	 *	ActionListener
	 *  @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println(e.getActionCommand());
		System.out.println(e.getSource());
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			action_OK();
			m_change = true;
			dispose();
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			m_change = false;
			fCountry.setSelectedIndex(countrySelected);
			fCity.setSelectedIndex(citySelected);
			dispose();
		}
		else if (e.getSource() == fCountry)
		{
			//	Country Changed - display in new Format
			//	Modifier for Mouse selection is 16  - for any key selection 0
			MCountry c = (MCountry)fCountry.getSelectedItem();
			m_location.setCountry(c);
			renderCountrySpecificFields();
			
			fCity = new CComboBox(loadAllCity(m_location.getCountryName()));
			

			//fCity.setSelectedItem( new KeyNamePair(AuxCityID,AuxCityName) );
			//initLocation();
			addLine(11, lCity, fCity);
			pack();
		}
		else
		{
			//  Country/Region
			Env.startBrowser(Env.GOOGLE_MAPS_URL_PREFIX + getCurrentLocation());
		}
	}	//	actionPerformed

	/**
	 * 	OK - check for changes (save them) & Exit
	 */
	private void action_OK()
	{
		m_location.setAddress1(fAddress1.getText());
		m_location.setAddress2(fAddress2.getText());
		m_location.setAddress3(fAddress3.getText());
		m_location.setAddress4(fAddress4.getText());
		if ( ((KeyNamePair)fCity.getSelectedItem()) != null ){
			m_location.setCity( ((KeyNamePair)fCity.getSelectedItem()).getName() );
		}else{
			m_location.setCity("");
		}
		
		m_location.setPostal(fPostal.getText());
		m_location.setPostal_Add(fPostalAdd.getText());
		//  Country/Region
		MCountry c = (MCountry)fCountry.getSelectedItem();
		m_location.setCountry(c);
		if (m_location.getCountry().isHasRegion())
		{
			MRegion r = (MRegion)fRegion.getSelectedItem();
			m_location.setRegion(r);
		}
		else
			m_location.setC_Region_ID(0);
		//	Save chnages
		m_location.save();
	}	//	actionOK

	/**
	 *	Get result
	 *  @return true, if changed
	 */
	public boolean isChanged()
	{
		return m_change;
	}	//	getChange

	/**
	 * 	Get edited Value (MLocation)
	 *	@return location
	 */
	public MLocation getValue()
	{
		return m_location;
	}	//	getValue

	/**
	 * 	Get edited Value (MLocation)
	 *	@return location
	 */
	private String getCurrentLocation() {
		m_tempLocation.setAddress1(fAddress1.getText());
		m_tempLocation.setAddress2(fAddress2.getText());
		m_tempLocation.setAddress3(fAddress3.getText());
		m_tempLocation.setAddress4(fAddress4.getText());
		m_tempLocation.setCity( ((KeyNamePair)fCity.getSelectedItem()).getName() );
		m_tempLocation.setPostal(fPostal.getText());
		m_tempLocation.setPostal_Add(fPostalAdd.getText());
		//  Country/Region
		MCountry c = (MCountry)fCountry.getSelectedItem();
		m_tempLocation.setCountry(c);
		if (m_tempLocation.getCountry().isHasRegion())
		{
			MRegion r = (MRegion)fRegion.getSelectedItem();
			m_tempLocation.setRegion(r);
		}
		else
			m_tempLocation.setC_Region_ID(0);

		return m_tempLocation.toString();
	}

}	//	VLocationDialog
