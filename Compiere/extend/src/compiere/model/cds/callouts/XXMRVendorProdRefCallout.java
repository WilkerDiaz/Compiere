package compiere.model.cds.callouts;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class XXMRVendorProdRefCallout extends CalloutEngine {
	
	public String SetAttribute(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer Department_ID = (Integer)mTab.getValue("XX_VMR_Department_ID");
		Integer Line_ID = (Integer)mTab.getValue("XX_VMR_Line_ID");
		Integer Section_ID = (Integer)mTab.getValue("XX_VMR_Section_ID");
		
		String sql = "SELECT M_AttributeSet_ID"
			+ " FROM M_AttributeSet"
			+ " WHERE M_AttributeSet_ID=(select M_Attributeset_ID from XX_VMR_DynamicCharact  where  XX_VMR_Department_ID="+Department_ID+" AND XX_VMR_Line_ID="+Line_ID+" AND XX_VMR_Section_ID="+Section_ID+")";						//////////////////////
		
	
		PreparedStatement prst = DB.prepareStatement(sql,null);
		Integer Attribute_ID = 1;
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				Attribute_ID = rs.getInt("M_AttributeSet_ID");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al captar el atributte set al seleccionar la linea en funcion SetAttribute");			
		}

		sql = "SELECT NAME"
			+ " FROM XX_VMR_SECTION"
			+ " WHERE XX_VMR_SECTION_ID=" + Section_ID;						
		
	
		prst = DB.prepareStatement(sql,null);
		String sectionName = "";
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				sectionName = rs.getString("NAME");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al captar el nombre de la seccion");			
		}
		
		if(Attribute_ID==1){
			Attribute_ID=null;
		}
		
		mTab.setValue("M_AttributeSet_ID", Attribute_ID);
		mTab.setValue("Name", sectionName);
		
		
		Integer section_id = (Integer)mTab.getValue("XX_VMR_Section_ID");
		Integer TypeTax = 0;
	
		sql = "SELECT C_TaxCategory_ID"											//////////////////////
			+ " FROM C_TaxCategory"
			+ " WHERE name='EXENTO'";						//////////////////////
	
		prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				TypeTax = rs.getInt("C_TaxCategory_ID");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al buscar el tipo de impuesto " + e);
		}
		
		sql = "SELECT name"											//////////////////////
			+ " FROM xx_vmr_section"
			+ " WHERE xx_vmr_section_id=" + section_id;						//////////////////////
		String nombreSeccion = "";
		prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				nombreSeccion = rs.getString("name");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al buscar el tipo de impuesto " + e);
		}
		
		
		// line_id tiene el id de la linea libros del departamento 22
		if (nombreSeccion.contains("LIBRO"))
		{
			mTab.setValue("C_TaxCategory_ID", TypeTax);
		}
		
		
		
		setCalloutActive(false);
		return "";
	}
	
	public String SetLongCharacteristic(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		
		Integer Department_ID = (Integer)mTab.getValue("XX_VMR_Department_ID");
		Integer Line_ID = (Integer)mTab.getValue("XX_VMR_Line_ID");
		Integer Section_ID = (Integer)mTab.getValue("XX_VMR_Section_ID");
		
		String sql = "SELECT XX_VMR_LONGCHARACTERISTIC_ID"
			+ " FROM XX_VMR_LONGCHARACTERISTIC"
			+ " WHERE XX_VMR_Department_ID="+Department_ID+" AND XX_VMR_Line_ID="+Line_ID+" AND XX_VMR_Section_ID="+Section_ID;						//////////////////////
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		Integer LongCharacteristic_ID = 1;
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				LongCharacteristic_ID = rs.getInt("XX_VMR_LONGCHARACTERISTIC_ID");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al capturar la caracteristica larga");			
		}

		mTab.setValue("XX_VMR_LongCharacteristic_ID", LongCharacteristic_ID);
		
		setCalloutActive(false);
		return "";
	}
	
	public String SetVendor(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer Vendor_ID = (Integer)mTab.getValue("C_BPartner_ID");
		
		String sql = "SELECT XX_VendorClass"
			+ " FROM C_BPartner"
			+ " WHERE C_BPartner_ID=" + Vendor_ID;
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		Integer vendorClass = new Integer(0);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				vendorClass = rs.getInt(1);
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al capturar si es importado o nacional");			
		}

		if (vendorClass==10000006)			
		{	
			mTab.setValue("IsImported", "Y");
		}
		else
		{	
			mTab.setValue("IsImported", "N");
		}

		setCalloutActive(false);
		return "";
	}
	
	
	public String SetSalePieces(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer SaleUnit_ID = (Integer)mTab.getValue("XX_SALEUNIT_ID");
	
		String sql = "SELECT xx_unitconversion"											//////////////////////
			+ " FROM xx_vmr_unitconversion"
			+ " WHERE XX_VMR_UNITPURCHASE_ID=" + SaleUnit_ID;						//////////////////////
		
	
		PreparedStatement prst = DB.prepareStatement(sql,null);
		Integer SaleUnit = 1;
		try {
			ResultSet rs = prst.executeQuery();
			int i = 1;
			if (rs.next() & i==1){
				SaleUnit = rs.getInt("xx_unitconversion");
				i++;
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			
		}
		mTab.setValue("XX_PiecesBySale", SaleUnit);
		
		setCalloutActive(false);
		return "";
	}
	
	public String SetConceptValue(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		
		Integer Brand_ID = (Integer)mTab.getValue("XX_VMR_Brand_ID");
		Integer Department_ID = (Integer)mTab.getValue("XX_VMR_Department_ID");
		Integer ConceptValue_ID = 0;
	
		String sql = "SELECT XX_VME_CONCEPTVALUE_ID"											//////////////////////
			+ " FROM XX_VMR_CONCEPTVALDPTBRAND"
			+ " WHERE ISACTIVE = 'Y' AND XX_VMR_DEPARTMENT_ID=" + Department_ID + " AND XX_VMR_BRAND_ID=" + Brand_ID;						//////////////////////
	
		PreparedStatement prst = DB.prepareStatement(sql,null);
		Integer SaleUnit = 1;
		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				ConceptValue_ID = rs.getInt("XX_VME_CONCEPTVALUE_ID");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al buscar el concepto de valor " + e);
		}
		if (ConceptValue_ID==0)
		{
			ADialog.info(1, new Container(), "XX_ConceptNotFound");
			mTab.setValue("XX_VME_ConceptValue_ID", 0);
		} else
		{
			mTab.setValue("XX_VME_ConceptValue_ID", ConceptValue_ID);
		}
		
		setCalloutActive(false);
		return "";
	}
	
	public String SetTypeTax(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		Integer section_id = (Integer)mTab.getValue("XX_VMR_Section_ID");
		Integer TypeTax = 0;
	
		String sql = "SELECT C_TaxCategory_ID"											//////////////////////
			+ " FROM C_TaxCategory"
			+ " WHERE name='EXENTO'";						//////////////////////
	
		PreparedStatement prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				TypeTax = rs.getInt("C_TaxCategory_ID");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al buscar el tipo de impuesto " + e);
		}
		
		sql = "SELECT name"											//////////////////////
			+ " FROM xx_vmr_section"
			+ " WHERE xx_vmr_section_id=" + section_id;						//////////////////////
		String nombreSeccion = "";
		prst = DB.prepareStatement(sql,null);

		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				nombreSeccion = rs.getString("name");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al buscar el tipo de impuesto " + e);
		}
		
		
		// line_id tiene el id de la linea libros del departamento 22
		if (nombreSeccion.contains("LIBRO"))
		{
			mTab.setValue("C_TaxCategory_ID", TypeTax);
		}
		
		setCalloutActive(false);
		return "";
	}
	
	

}
