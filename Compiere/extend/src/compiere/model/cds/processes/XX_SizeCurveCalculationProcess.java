package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MAttributeValue;
import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_SizeCurve;
import compiere.model.cds.X_XX_VMR_SizeCurveDetail;
import compiere.model.cds.X_XX_VMR_SizeCurveReference;
import compiere.model.cds.X_XX_VMR_VendorProdRef;


/**
 * Calculates the Size curve asociated to a Distribution Header
 * @author Javier Pino
 * **/
public class XX_SizeCurveCalculationProcess extends SvrProcess {		
	
	X_XX_VMR_DistributionHeader current_header; 
	Vector<VendorRef> vendor_ref = new Vector<VendorRef>(); 		
	boolean is_order;
	int i = 0;
	final static int size_attribute = Env.getCtx().getContextAsInt("#XX_L_M_ATTRIBUTESIZE_ID");
		
	@Override
	protected void prepare() {

	}
	
	@Override	
	protected String doIt() throws Exception {	
		current_header = new X_XX_VMR_DistributionHeader(getCtx(), getRecord_ID(), get_TrxName());	
		is_order = (current_header.getC_Order_ID() != 0);
	
		try {								
			//Depending on the Distribution Type uses a differente method			
			if (is_order) {
				vendor_ref = getSizeCurveData_Order();
			} else {
				vendor_ref  = getSizeCurveData_Redistribution();			
			}
			
			if (vendor_ref == null) return "";
			
			//If no references have textil products, set it in the header
			//products
			if (vendor_ref.size() == 0) {						
				//current_header.setXX_HasTextilProducts(false);
				//current_header.save();
				ADialog.info(1, new Container(),"XX_NoSizeCurve");
				return "No size Curve for XX_VMR_DistributionHeader " + current_header.get_ID();		    	
			}
			if (current_header.isXX_CalculatedSizeCurve()){
				boolean replace = true;				
				//Asks the user if it wants to delete old records 
				if (current_header.isXX_CalculatedSizeCurve()) {
					replace = ADialog.ask(1, new Container(), "XX_SafeDeleteSizeCurve");			
				}
				if (!replace) return "";
				
				String SQL_a = "DELETE FROM XX_VMR_SIZECURVE WHERE " 
					+"XX_VMR_DISTRIBUTIONHEADER_ID =" +  current_header.get_ID();
				DB.executeUpdate(null,SQL_a);
				
			}							
		} catch (SQLException e) {					
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");
			return "XX_DatabaseAccessError";
		}	
		//Preprocesses the references 
		saveSizeCurve(vendor_ref);	

			//Sets the header to indicate that has already calculated    
		current_header.setXX_CalculatedSizeCurve(true);
		current_header.save();
		ADialog.info(1, new Container(), "XX_LookForSizeCurve");

		return "";
	}
	
/**
 * Look through the products and load the references that has  products with sizes 
 * In a purchase order
 * */
protected Vector<VendorRef> getSizeCurveData_Order() throws SQLException {		
		
		Vector<VendorRef> vendor_ref = new Vector<VendorRef>();		
		String SQL = 
			" SELECT PO_REF.XX_VMR_PO_LINEREFPROV_ID, PO_REF.XX_VMR_VENDORPRODREF_ID, " +
			" PO_REF.XX_CHARACTERISTIC1_ID, PO_REF.XX_CHARACTERISTIC2_ID, " +
			" REF_MATRIX.XX_VALUE1, REF_MATRIX.XX_VALUE2, REF_MATRIX.M_PRODUCT " +
			" FROM (XX_VMR_PO_LINEREFPROV PO_REF LEFT JOIN XX_VMR_REFERENCEMATRIX REF_MATRIX " +
			" ON (PO_REF.XX_VMR_PO_LINEREFPROV_ID = REF_MATRIX.XX_VMR_PO_LINEREFPROV_ID)) " +
			" WHERE C_ORDER_ID = " +  current_header.getC_Order_ID() + 
			" AND (XX_CHARACTERISTIC1_ID = " + size_attribute + " OR " +
			" XX_CHARACTERISTIC2_ID = " + size_attribute + " ) " + 
			" AND (REF_MATRIX.XX_QUANTITYV + REF_MATRIX.XX_QUANTITYO) > 0 ";					
								
		PreparedStatement pstmt = DB.prepareStatement(SQL, null);										
		ResultSet rs = pstmt.executeQuery();		
		while(rs.next()) {
												
			if (rs.getInt("M_PRODUCT") == 0) {
				X_XX_VMR_VendorProdRef vendor = new X_XX_VMR_VendorProdRef(Env.getCtx(),
						rs.getInt("XX_VMR_VENDORPRODREF_ID"), null); 				
				String mss = Msg.getMsg(Env.getCtx(), "XX_UnAssociatedProducts", 
						//reference
						new String[] {vendor.getValue()+ "-" + vendor.getName()});
				rs.close();
				pstmt.close();	
				ADialog.error(1, new Container(), mss);												
				return null;	
			}
						
			Iterator<VendorRef> references = vendor_ref.iterator();
			VendorRef temporal = null;
			boolean found = false;			
			while (references.hasNext()) {
				temporal = references.next();				
				if (temporal.vendor_reference == rs.getInt("XX_VMR_VENDORPRODREF_ID")) {
					found = true;				
					//Verify that the product is assigned a reference
									 
					MAttributeValue atvalue1 = new MAttributeValue(getCtx(), rs.getInt("XX_VALUE1") ,null);
					MAttributeValue atvalue2 = new MAttributeValue(getCtx(), rs.getInt("XX_VALUE2") ,null);
					if (rs.getInt("XX_CHARACTERISTIC1_ID") == size_attribute) {
						if (!temporal.product_sizes.contains(atvalue1.getM_AttributeValue_ID()))
							temporal.product_sizes.add(atvalue1.getM_AttributeValue_ID());
					} else if ( rs.getInt("XX_CHARACTERISTIC2_ID") == size_attribute ) {
						if (!temporal.product_sizes.contains(atvalue2.getM_AttributeValue_ID())) 
							temporal.product_sizes.add(atvalue2.getM_AttributeValue_ID());											
					} 
				}
			}
			if (!found) {
				VendorRef new_vendor = new VendorRef();
				new_vendor.vendor_reference = rs.getInt("XX_VMR_VENDORPRODREF_ID");
				
				MAttributeValue atvalue1 = new MAttributeValue(getCtx(), rs.getInt("XX_VALUE1") ,null);
				MAttributeValue atvalue2 = new MAttributeValue(getCtx(), rs.getInt("XX_VALUE2") , null);
				if (rs.getInt("XX_CHARACTERISTIC1_ID") == size_attribute) {
					if (!new_vendor.product_sizes.contains(atvalue1.getM_AttributeValue_ID())) {  
						new_vendor.product_sizes.add(atvalue1.getM_AttributeValue_ID());
					}
				} else if ( rs.getInt("XX_CHARACTERISTIC2_ID") == size_attribute ) {
					if (!new_vendor.product_sizes.contains(atvalue2.getM_AttributeValue_ID())) 
						new_vendor.product_sizes.add(atvalue2.getM_AttributeValue_ID());
				}
				vendor_ref.add(new_vendor);
			}
		}
		rs.close();
		pstmt.close();	
		return vendor_ref;
	}
	
	protected Vector<VendorRef> getSizeCurveData_Redistribution() throws SQLException {
	
		Vector<VendorRef> vendor_ref = new Vector<VendorRef>();
		int product = 0, reference = 0;
		
		String SQL_a = "SELECT T.M_PRODUCT_ID FROM " +
				" XX_VMR_DISTRIBUTIONDETAIL D JOIN XX_VMR_DISTRIBDETAILTEMP T " +
				" ON (D.XX_VMR_DISTRIBUTIONDETAIL_ID = T.XX_VMR_DISTRIBUTIONDETAIL_ID )" +
				" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + getRecord_ID();
			
		PreparedStatement pstmt_a = DB.prepareStatement(SQL_a, null);										
		ResultSet rs_a = pstmt_a.executeQuery();

		while(rs_a.next()) {			

			product = rs_a.getInt("M_PRODUCT_ID");
			MProduct product_info = new MProduct(getCtx(), product ,get_TrxName());
			String SQL_b = "SELECT I.M_ATTRIBUTEVALUE_ID  " +
					"  FROM M_ATTRIBUTEINSTANCE I WHERE " +
					" M_ATTRIBUTESETINSTANCE_ID = "
				+ product_info.getM_AttributeSetInstance_ID()
				+ " AND I.M_ATTRIBUTE_ID = " + size_attribute
				+ " AND I.M_ATTRIBUTEVALUE_ID IS NOT NULL "; 

			reference = product_info.getXX_VMR_VendorProdRef_ID();		
			if (reference == 0) continue;

			PreparedStatement pstmt_b = DB.prepareStatement(SQL_b, null);
			ResultSet rs_b = pstmt_b.executeQuery();
			if (rs_b.next()) {

				MAttributeValue atvalue1 =
					new MAttributeValue(getCtx(), rs_b.getInt("M_ATTRIBUTEVALUE_ID") ,get_TrxName());
				VendorRef temporal;
				boolean insert_new = true;
				Iterator<VendorRef> itr_ref = vendor_ref.iterator();
				while (itr_ref.hasNext()) {
					temporal = itr_ref.next();								
					if (temporal.vendor_reference == reference) {									
						insert_new = false;
						if (!temporal.product_sizes.contains(atvalue1.getM_AttributeValue_ID())) { 
							temporal.product_sizes.add(atvalue1.getM_AttributeValue_ID());							
						}									
					}
				}						
				if (insert_new ) {					
					VendorRef new_vendor = new VendorRef();
					new_vendor.vendor_reference = reference;
					new_vendor.product_sizes.add(atvalue1.getM_AttributeValue_ID());
					vendor_ref.add(new_vendor);
				}
			}			
			rs_b.close();
			pstmt_b.close();
		}
		rs_a.close();
		pstmt_a.close();		
		return vendor_ref;
	}
	
	private class VendorRef {
		int vendor_reference = 0;
		Vector<Integer> product_sizes = new Vector<Integer>();	
		
		@Override
		public String toString() {			
			return vendor_reference + " " + product_sizes.toString();
		}
	}

	/**
	 * Given an vector of Vendor References each one asociates with their sizes
	 * Transforms it, and creates the Size Curves 
	 * */
	protected void saveSizeCurve(Vector<VendorRef> vendor_ref) {
			//Each item in this vector will represent a Size Curve
				//The inner Vector represent the sizes asociated to the Size curve
		Vector<Vector<Integer>> sizes = new Vector<Vector<Integer>> ();
			
				//The inner Vector here, represents the references 
		Vector<Vector<Integer>> references = new Vector<Vector<Integer>> ();
		
			//Traverse the Vendor References
		Iterator<VendorRef> itr = vendor_ref.iterator();
			
			//This vector is always nonempty
		VendorRef vendor = itr.next();
			
			//First Size Curve to be created
		sizes.add(new Vector<Integer>());					
		references.add(new Vector<Integer>());
			//Adds the first reference
		references.lastElement().add(vendor.vendor_reference);
			//Adds their sizes to the Size Curve
		for (int i = 0 ; i < vendor.product_sizes.size() ; i++) {
			sizes.lastElement().add(vendor.product_sizes.elementAt(i));			
		}
				
		while (itr.hasNext()) { //Traversing the rest of the Vendor References
			int use_curve = -1;
			vendor = itr.next();
			for (int i = 0; i < sizes.size() ; i++) {				
				boolean has_all = true;
				
					//Checking if there´s a previous Size Curve with the same sizes
				if (sizes.elementAt(i).size() == vendor.product_sizes.size()) {
					for (int j = 0 ; j < vendor.product_sizes.size() ; j++) {
						if (!sizes.elementAt(i).contains(vendor.product_sizes.elementAt(j))) 
							has_all = false;						
					}
					if (has_all) { //Use it, if exist
						references.elementAt(i).add(vendor.vendor_reference);
						use_curve = i;
						break;
					}										
				}								
			}
					//No curve has the same sizes, so create a new one
			if (use_curve == -1) {
				sizes.add(new Vector<Integer>());				
				references.add(new Vector<Integer>());
				references.lastElement().add(vendor.vendor_reference);
				for (int j = 0 ; j < vendor.product_sizes.size() ; j++) {
					sizes.lastElement().add(vendor.product_sizes.elementAt(j));										
				}
			} 			
		}
			//Once through with the process, of creating the curve, proceed to save it in the database
		for (int i = 0 ; i < sizes.size() ; i++) {				
			
			X_XX_VMR_SizeCurve curve = 	//New Size Curve 
				new X_XX_VMR_SizeCurve(getCtx(), 0, get_TrxName());
			
			curve.setXX_VMR_DistributionHeader_ID(current_header.get_ID());
			String curve_references = "[";
			String curve_sizes = "[";
			
			for (int j = 0 ; j < sizes.elementAt(i).size(); j++) {
				
					//Getting the Sizes Name
				MAttributeValue value = new MAttributeValue(getCtx(), sizes.elementAt(i).elementAt(j), get_TrxName());
				curve_sizes += value.getName();
				if (j != sizes.elementAt(i).size() - 1) curve_sizes += "/  " ;
			}
			
			for (int j = 0; j < references.elementAt(i).size() ; j++) {
				X_XX_VMR_VendorProdRef reference = new X_XX_VMR_VendorProdRef(getCtx(), references.elementAt(i).elementAt(j), get_TrxName());
				curve_references += reference.getValue();
				if (j != references.elementAt(i).size() - 1) curve_references += "/  " ;
			}
			curve_references += "]";
			curve_sizes += "]";
			
				//Setting the description information 
			if (curve_references.length() > 255) 
				curve_references = curve_references.substring(0, 254);
			if (curve_sizes.length() > 255) 
				curve_sizes = curve_sizes.substring(0, 254);
			
			curve.setXX_ReferencesDescription(curve_references);
			curve.setXX_SizesDescription(curve_sizes);
			curve.save();

				//Saving in a hidden table the values of the references
			Iterator<Integer> reference_detail = references.elementAt(i).iterator();
			while (reference_detail.hasNext()) {
				X_XX_VMR_SizeCurveReference curve_reference 
				= new X_XX_VMR_SizeCurveReference(getCtx(), 0, get_TrxName());
				curve_reference.setXX_VMR_SIZECURVE_ID(curve.get_ID());
				curve_reference.setXX_VMR_VendorProdRef_ID(reference_detail.next());
				curve_reference.save();					
			}				
				//Saving the details of the sizes of the size curve
			Iterator<Integer> size_detail = sizes.elementAt(i).iterator();
			while (size_detail.hasNext()) {
				X_XX_VMR_SizeCurveDetail curve_detail 
				= new X_XX_VMR_SizeCurveDetail(getCtx(), 0, get_TrxName());
				curve_detail.setXX_VMR_SIZECURVE_ID(curve.get_ID());
				curve_detail.setM_AttributeValue_ID(size_detail.next());
				curve_detail.save();
			}
		}
	}
}
