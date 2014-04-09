package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import org.compiere.model.MConversionRate;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRPOLineRefProv extends X_XX_VMR_PO_LineRefProv {

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRPOLineRefProv.class);
	
	public MVMRPOLineRefProv (Ctx ctx, int PO_LineRefProv_ID, Trx trx)
	{
		super (ctx, PO_LineRefProv_ID, trx);
		if (PO_LineRefProv_ID == 0)
		{
		//	setM_Requisition_ID (0);
//			setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_RequisitionLine WHERE M_Requisition_ID=@M_Requisition_ID@
//			setLineNetAmt (Env.ZERO);
//			setPriceActual (Env.ZERO);
//			setQty (Env.ONE);	// 1
//			setMargin (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setSalePrice (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setLinePVPAmount (Env.ZERO);	/////////////////////////////////////////////////////////////////
//			setLinePlusTaxAmount (Env.ZERO);	////////////////////////////////////////////////////////////
//			setPackageMultiple (Env.ONE);				///////////////////////////////////////////

		}
		
	}	
	
	public MVMRPOLineRefProv (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	
	
	BigDecimal XX_Rebate1;
	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		
		
		// si es sin características valido que no exista ya con anterioridad
		if (!get_ValueAsBoolean("XX_WithCharacteristic"))
		{
			String SQL ="Select * from xx_vmr_po_linerefprov where xx_vmr_vendorprodref_id=" + getXX_VMR_VendorProdRef_ID() + " and c_order_id=" + getC_Order_ID() + " and XX_VMR_PO_LineRefProv_ID<>" + getXX_VMR_PO_LineRefProv_ID();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try 
			{
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();
				
				if(rs.next())
				{	
					log.saveError(null, Msg.getMsg(Env.getCtx(), "Referencia duplicada"));
					return false;
				}
					
			}
			catch(Exception a)
			{	
				log.log(Level.SEVERE,SQL,a);
				return false;
			}finally{
				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
			
		if (get_ValueAsBigDecimal("XX_UnitPurchasePrice").compareTo(new BigDecimal(0))==0)
		{
			log.saveError(null, Msg.getMsg(Env.getCtx(), "XX_CostIsMandatory"));
			return false;
		}

		System.out.println();
		if (get_ValueAsBigDecimal("XX_Margin").compareTo(Env.ZERO)<0)
		{
			log.saveError(null, Msg.getMsg(Env.getCtx(), "XX_InvalidPriceBeco"));
			return false;
		}
		
		
		//valido que no se repita una caracteristica
		if (get_ValueAsBoolean("XX_WithCharacteristic")){
			
			Integer duplicatedCharacteristic=-1;
			
			//Caracateristica 1 Value 1
			if(getXX_Characteristic1Value1_ID()!=0){
				if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value2_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value3_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value4_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}else if(getXX_Characteristic1Value1_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value1_ID();
				}
			}
			
			//Caracateristica 1 Value 2
			if(getXX_Characteristic1Value2_ID()!=0){
				if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value3_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}else if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value4_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}else if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}else if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}else if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}else if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}else if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}else if(getXX_Characteristic1Value2_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value2_ID();
				}
			}
			
			//Caracateristica 1 Value 3
			if(getXX_Characteristic1Value3_ID()!=0){
				if(getXX_Characteristic1Value3_ID()==getXX_Characteristic1Value4_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value3_ID();
				}else if(getXX_Characteristic1Value3_ID()==getXX_Characteristic1Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value3_ID();
				}else if(getXX_Characteristic1Value3_ID()==getXX_Characteristic1Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value3_ID();
				}else if(getXX_Characteristic1Value3_ID()==getXX_Characteristic1Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value3_ID();
				}else if(getXX_Characteristic1Value3_ID()==getXX_Characteristic1Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value3_ID();
				}else if(getXX_Characteristic1Value3_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value3_ID();
				}else if(getXX_Characteristic1Value3_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value3_ID();
				}
			}
			
			//Caracateristica 1 Value 4
			if(getXX_Characteristic1Value4_ID()!=0){
				if(getXX_Characteristic1Value4_ID()==getXX_Characteristic1Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value4_ID();
				}else if(getXX_Characteristic1Value4_ID()==getXX_Characteristic1Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value4_ID();
				}else if(getXX_Characteristic1Value4_ID()==getXX_Characteristic1Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value4_ID();
				}else if(getXX_Characteristic1Value4_ID()==getXX_Characteristic1Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value4_ID();
				}else if(getXX_Characteristic1Value4_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value4_ID();
				}else if(getXX_Characteristic1Value4_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value4_ID();
				}
			}
			
			//Caracateristica 1 Value 5
			if(getXX_Characteristic1Value5_ID()!=0){
				if(getXX_Characteristic1Value5_ID()==getXX_Characteristic1Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value5_ID();
				}else if(getXX_Characteristic1Value5_ID()==getXX_Characteristic1Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value5_ID();
				}else if(getXX_Characteristic1Value5_ID()==getXX_Characteristic1Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value5_ID();
				}else if(getXX_Characteristic1Value5_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value5_ID();
				}else if(getXX_Characteristic1Value5_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value5_ID();
				}
			}
			
			//Caracateristica 1 Value 6
			if(getXX_Characteristic1Value6_ID()!=0){
				if(getXX_Characteristic1Value6_ID()==getXX_Characteristic1Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value6_ID();
				}else if(getXX_Characteristic1Value6_ID()==getXX_Characteristic1Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value6_ID();
				}else if(getXX_Characteristic1Value6_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value6_ID();
				}else if(getXX_Characteristic1Value6_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value6_ID();
				}
			}
			
			//Caracateristica 1 Value 7
			if(getXX_Characteristic1Value7_ID()!=0){
				if(getXX_Characteristic1Value7_ID()==getXX_Characteristic1Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value7_ID();
				}else if(getXX_Characteristic1Value7_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value7_ID();
				}else if(getXX_Characteristic1Value7_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value7_ID();
				}
			}
			
			//Caracateristica 1 Value 8
			if(getXX_Characteristic1Value8_ID()!=0){
				if(getXX_Characteristic1Value8_ID()==getXX_Characteristic1Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value8_ID();
				}else if(getXX_Characteristic1Value8_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value8_ID();
				}
			}
			
			//Caracateristica 1 Value 9
			if(getXX_Characteristic1Value9_ID()!=0){
				if(getXX_Characteristic1Value9_ID()==getXX_Characteristic1Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic1Value9_ID();
				}
			}
			
			
			//Caracateristica 2 Value 1
			if(getXX_Characteristic2Value1_ID()!=0){
				if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value2_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value3_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value4_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}else if(getXX_Characteristic2Value1_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value1_ID();
				}
			}
			
			//Caracateristica 2 Value 2
			if(getXX_Characteristic2Value2_ID()!=0){
				if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value3_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}else if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value4_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}else if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}else if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}else if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}else if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}else if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}else if(getXX_Characteristic2Value2_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value2_ID();
				}
			}
			
			//Caracateristica 2 Value 3
			if(getXX_Characteristic2Value3_ID()!=0){
				if(getXX_Characteristic2Value3_ID()==getXX_Characteristic2Value4_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value3_ID();
				}else if(getXX_Characteristic2Value3_ID()==getXX_Characteristic2Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value3_ID();
				}else if(getXX_Characteristic2Value3_ID()==getXX_Characteristic2Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value3_ID();
				}else if(getXX_Characteristic2Value3_ID()==getXX_Characteristic2Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value3_ID();
				}else if(getXX_Characteristic2Value3_ID()==getXX_Characteristic2Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value3_ID();
				}else if(getXX_Characteristic2Value3_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value3_ID();
				}else if(getXX_Characteristic2Value3_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value3_ID();
				}
			}
			
			//Caracateristica 2 Value 4
			if(getXX_Characteristic2Value4_ID()!=0){
				if(getXX_Characteristic2Value4_ID()==getXX_Characteristic2Value5_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value4_ID();
				}else if(getXX_Characteristic2Value4_ID()==getXX_Characteristic2Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value4_ID();
				}else if(getXX_Characteristic2Value4_ID()==getXX_Characteristic2Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value4_ID();
				}else if(getXX_Characteristic2Value4_ID()==getXX_Characteristic2Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value4_ID();
				}else if(getXX_Characteristic2Value4_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value4_ID();
				}else if(getXX_Characteristic2Value4_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value4_ID();
				}
			}
			
			//Caracateristica 2 Value 5
			if(getXX_Characteristic2Value5_ID()!=0){
				if(getXX_Characteristic2Value5_ID()==getXX_Characteristic2Value6_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value5_ID();
				}else if(getXX_Characteristic2Value5_ID()==getXX_Characteristic2Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value5_ID();
				}else if(getXX_Characteristic2Value5_ID()==getXX_Characteristic2Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value5_ID();
				}else if(getXX_Characteristic2Value5_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value5_ID();
				}else if(getXX_Characteristic2Value5_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value5_ID();
				}
			}
			
			//Caracateristica 2 Value 6
			if(getXX_Characteristic2Value6_ID()!=0){
				if(getXX_Characteristic2Value6_ID()==getXX_Characteristic2Value7_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value6_ID();
				}else if(getXX_Characteristic2Value6_ID()==getXX_Characteristic2Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value6_ID();
				}else if(getXX_Characteristic2Value6_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value6_ID();
				}else if(getXX_Characteristic2Value6_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value6_ID();
				}
			}
			
			//Caracateristica 2 Value 7
			if(getXX_Characteristic2Value7_ID()!=0){
				if(getXX_Characteristic2Value7_ID()==getXX_Characteristic2Value8_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value7_ID();
				}else if(getXX_Characteristic2Value7_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value7_ID();
				}else if(getXX_Characteristic2Value7_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value7_ID();
				}
			}
			
			//Caracateristica 2 Value 8
			if(getXX_Characteristic2Value8_ID()!=0){
				if(getXX_Characteristic2Value8_ID()==getXX_Characteristic2Value9_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value8_ID();
				}else if(getXX_Characteristic2Value8_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value8_ID();
				}
			}
			
			//Caracateristica 2 Value 9
			if(getXX_Characteristic2Value9_ID()!=0){
				if(getXX_Characteristic2Value9_ID()==getXX_Characteristic2Value10_ID()){
					duplicatedCharacteristic=getXX_Characteristic2Value9_ID();
				}
			}
			
			if(duplicatedCharacteristic!=-1){
				String SQL ="Select NAME from M_ATTRIBUTEVALUE where M_ATTRIBUTEVALUE_ID="+duplicatedCharacteristic;
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				try 
				{
					pstmt = DB.prepareStatement(SQL, null);
					rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						log.saveError("Error", Msg.getMsg(Env.getCtx(), "DuplicatedCharacteristic",new String[] {rs.getString("NAME")}));
						return false;
					}
						
				}
				catch(Exception a)
				{	
					log.log(Level.SEVERE,SQL,a);
					return false;
				}finally{
					
					try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
					try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
				}
			}
		}
		
		//verifica que no se repita una combinacion de caracreristicas dinamicas
		//repetidas para la misma referencia en una misma linea
		if (get_ValueAsBoolean("XX_WithCharacteristic"))
		{
			
		
			int char1=0;
			int char2=0;
			
			for(int j=1;j<11;j++){
				
				if(j==1){
					char1=getXX_Characteristic1Value1_ID();
				}else if(j==2){
					char1=getXX_Characteristic1Value2_ID();
				}else if(j==3){
					char1=getXX_Characteristic1Value3_ID();
				}else if(j==4){
					char1=getXX_Characteristic1Value4_ID();
				}else if(j==5){
					char1=getXX_Characteristic1Value5_ID();
				}else if(j==6){
					char1=getXX_Characteristic1Value6_ID();
				}else if(j==7){
					char1=getXX_Characteristic1Value7_ID();
				}else if(j==8){
					char1=getXX_Characteristic1Value8_ID();
				}else if(j==9){
					char1=getXX_Characteristic1Value9_ID();
				}else if(j==10){
					char1=getXX_Characteristic1Value10_ID();
				}
				
				boolean onlyChar1=true;
				
				for(int i=1;i<11;i++){
					if(i==1){
						char2=getXX_Characteristic2Value1_ID();
						if (char2!=0){
							onlyChar1=false;
						}
					}else if(i==2){
						char2=getXX_Characteristic2Value2_ID();
					}else if(i==3){
						char2=getXX_Characteristic2Value3_ID();
					}else if(i==4){
						char2=getXX_Characteristic2Value4_ID();
					}else if(i==5){
						char2=getXX_Characteristic2Value5_ID();
					}else if(i==6){
						char2=getXX_Characteristic2Value6_ID();
					}else if(i==7){
						char2=getXX_Characteristic2Value7_ID();
					}else if(i==8){
						char2=getXX_Characteristic2Value8_ID();
					}else if(i==9){
						char2=getXX_Characteristic2Value9_ID();
					}else if(i==10){
						char2=getXX_Characteristic2Value10_ID();
					}
				
					if(char1!=0 || char2!=0){
						
						String SQL = "";
						if(!onlyChar1){
							
							SQL ="Select XX_VMR_PO_LineRefProv_ID from XX_VMR_PO_LineRefProv "+
								"where XX_VMR_VENDORPRODREF_ID="+getXX_VMR_VendorProdRef_ID()+
								" AND C_ORDER_ID="+getC_Order_ID()+
								" AND XX_VMR_PO_LineRefProv_ID!="+getXX_VMR_PO_LineRefProv_ID()+
								" AND "+ 
								"(("+
								"(XX_CHARACTERISTIC1VALUE1_ID="+char1+" OR XX_CHARACTERISTIC1VALUE2_ID="+char1+" OR "+
							     "XX_CHARACTERISTIC1VALUE3_ID="+char1+" OR XX_CHARACTERISTIC1VALUE4_ID="+char1+" OR "+
								 "XX_CHARACTERISTIC1VALUE5_ID="+char1+" OR XX_CHARACTERISTIC1VALUE6_ID="+char1+" OR "+
							     "XX_CHARACTERISTIC1VALUE7_ID="+char1+" OR XX_CHARACTERISTIC1VALUE8_ID="+char1+" OR "+
								 "XX_CHARACTERISTIC1VALUE9_ID="+char1+" OR XX_CHARACTERISTIC1VALUE10_ID="+char1+") "+
								 "AND "+
								"(XX_CHARACTERISTIC2VALUE1_ID="+char2+" OR XX_CHARACTERISTIC2VALUE2_ID="+char2+" OR "+
								 "XX_CHARACTERISTIC2VALUE3_ID="+char2+" OR XX_CHARACTERISTIC2VALUE4_ID="+char2+" OR "+
								 "XX_CHARACTERISTIC2VALUE5_ID="+char2+" OR XX_CHARACTERISTIC2VALUE6_ID="+char2+" OR "+
								 "XX_CHARACTERISTIC2VALUE7_ID="+char2+" OR XX_CHARACTERISTIC2VALUE8_ID="+char2+" OR "+
								 "XX_CHARACTERISTIC2VALUE9_ID="+char2+" OR XX_CHARACTERISTIC2VALUE10_ID="+char2+") "+
								")"+
								" OR ("+
								"(XX_CHARACTERISTIC1VALUE1_ID="+char2+" OR XX_CHARACTERISTIC1VALUE2_ID="+char2+" OR "+
							     "XX_CHARACTERISTIC1VALUE3_ID="+char2+" OR XX_CHARACTERISTIC1VALUE4_ID="+char2+" OR "+
								 "XX_CHARACTERISTIC1VALUE5_ID="+char2+" OR XX_CHARACTERISTIC1VALUE6_ID="+char2+" OR "+
							     "XX_CHARACTERISTIC1VALUE7_ID="+char2+" OR XX_CHARACTERISTIC1VALUE8_ID="+char2+" OR "+
								 "XX_CHARACTERISTIC1VALUE9_ID="+char2+" OR XX_CHARACTERISTIC1VALUE10_ID="+char2+") "+
								 "AND "+
								"(XX_CHARACTERISTIC2VALUE1_ID="+char1+" OR XX_CHARACTERISTIC2VALUE2_ID="+char1+" OR "+
								 "XX_CHARACTERISTIC2VALUE3_ID="+char1+" OR XX_CHARACTERISTIC2VALUE4_ID="+char1+" OR "+
								 "XX_CHARACTERISTIC2VALUE5_ID="+char1+" OR XX_CHARACTERISTIC2VALUE6_ID="+char1+" OR "+
								 "XX_CHARACTERISTIC2VALUE7_ID="+char1+" OR XX_CHARACTERISTIC2VALUE8_ID="+char1+" OR "+
								 "XX_CHARACTERISTIC2VALUE9_ID="+char1+" OR XX_CHARACTERISTIC2VALUE10_ID="+char1+") "+
								 "))";
						}
						else{
							
							SQL ="Select XX_VMR_PO_LineRefProv_ID from XX_VMR_PO_LineRefProv "+
							"where XX_VMR_VENDORPRODREF_ID="+getXX_VMR_VendorProdRef_ID()+
							" AND C_ORDER_ID="+getC_Order_ID()+
							" AND XX_VMR_PO_LineRefProv_ID!="+getXX_VMR_PO_LineRefProv_ID()+
							" AND "+ 
							"(("+
							"(XX_CHARACTERISTIC1VALUE1_ID="+char1+" OR XX_CHARACTERISTIC1VALUE2_ID="+char1+" OR "+
						     "XX_CHARACTERISTIC1VALUE3_ID="+char1+" OR XX_CHARACTERISTIC1VALUE4_ID="+char1+" OR "+
							 "XX_CHARACTERISTIC1VALUE5_ID="+char1+" OR XX_CHARACTERISTIC1VALUE6_ID="+char1+" OR "+
						     "XX_CHARACTERISTIC1VALUE7_ID="+char1+" OR XX_CHARACTERISTIC1VALUE8_ID="+char1+" OR "+
							 "XX_CHARACTERISTIC1VALUE9_ID="+char1+" OR XX_CHARACTERISTIC1VALUE10_ID="+char1+") "+
							 "AND "+
							"(XX_CHARACTERISTIC2VALUE1_ID IS NULL AND XX_CHARACTERISTIC2VALUE2_ID IS NULL AND "+
							 "XX_CHARACTERISTIC2VALUE3_ID IS NULL AND XX_CHARACTERISTIC2VALUE4_ID IS NULL AND "+
							 "XX_CHARACTERISTIC2VALUE5_ID IS NULL AND XX_CHARACTERISTIC2VALUE6_ID IS NULL AND "+
							 "XX_CHARACTERISTIC2VALUE7_ID IS NULL AND XX_CHARACTERISTIC2VALUE8_ID IS NULL AND "+
							 "XX_CHARACTERISTIC2VALUE9_ID IS NULL AND XX_CHARACTERISTIC2VALUE10_ID IS NULL) "+
							")"+
							")";
						}
				
						PreparedStatement pstmt = null;
						ResultSet rs = null;
						try 
						{							
							pstmt = DB.prepareStatement(SQL, null);
							rs = pstmt.executeQuery();
							
							while(rs.next())
							{	
								String name1="";
								String name2="";
								
								String SQL1 ="Select NAME from M_ATTRIBUTEVALUE where M_ATTRIBUTEVALUE_ID="+char1;
								PreparedStatement pstmt2 = DB.prepareStatement(SQL1, null);
								ResultSet rs2 = pstmt2.executeQuery();
									
								while(rs2.next())
								{	
									name1=rs2.getString("NAME");	
								}
								rs2.close();
								pstmt2.close();
								
								String SQL2 ="Select NAME from M_ATTRIBUTEVALUE where M_ATTRIBUTEVALUE_ID="+char2;
								PreparedStatement pstmt3 = DB.prepareStatement(SQL2, null);
								ResultSet rs3 = pstmt3.executeQuery();
								
								while(rs3.next())
								{	
									name2=rs3.getString("NAME");	
								}
								rs3.close();
								pstmt3.close();
								
								log.saveError("Error", Msg.getMsg(Env.getCtx(), "SameCombination",new String[] {name1+"",name2+"",""+rs.getInt("XX_VMR_PO_LineRefProv_ID")+""}));
								return false;
							}
							
						}
						catch(Exception a)
						{	
							log.log(Level.SEVERE,SQL,a);
							return false;
						}finally{
							
							try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
							try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
						}
						
					}
				}
			}  
		}
			
			
		if (!get_ValueAsBoolean("XX_WithCharacteristic") & get_ValueAsInt("Qty")<1)
		{
			log.saveError(null, Msg.getMsg(Env.getCtx(), "XX_PurchaseQuantity"));
			return false;
		}
		
		if (get_ValueAsInt("XX_VMR_VendorProdRef_ID")==0)
		{
			log.saveError(null, Msg.getMsg(Env.getCtx(), "XX_InvalidProductReference"));
			return false;
		}
		
		
		//Impide colocar mas de N piezas en la O/C
/**		if (!get_ValueAsBoolean("XX_WithCharacteristic") && exceedsQty())
		{
			log.saveError(null, Msg.getMsg(Env.getCtx(), "XX_ExceedsQTY"));
			return false;
		}
*/		
		
		return true;
	}
	
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		/*// Primero envio un mensaje de alerta en caso de que el margen sea mayor a 80%
		if (getXX_Margin().intValue()>80)
			ADialog.info(1, new Container(), "XX_BigMargin");*/
		
		if (!success)
			return success;

		//Actualizo los valores de la reference matrix en caso que sea sin caracteristicas dinamicas
		if(!isXX_WithCharacteristic())
			actReferenceMatrix();
			
		return updateHeader();

	}	//	afterSave
	
	private void actReferenceMatrix(){
	
		String SQL =
			"UPDATE XX_VMR_REFERENCEMATRIX SET "
		    +"XX_QUANTITYC = "+getQty()+
		    ",XX_QUANTITYO = "+getXX_GiftsQty()+
			",XX_QUANTITYV = "+getSaleQty()+
		    " WHERE XX_VMR_PO_LINEREFPROV_ID="+get_ID();

		DB.executeUpdate(get_Trx(), SQL);
	
	}
	
	Date fechaActual = new Date();
	private boolean updateHeader()
	{
		
		MOrder order = new MOrder(Env.getCtx(), getC_Order_ID(), null);
		
		// mejoras a esta parte empleando el modelo y no los updates
		
		MConversionRate conversionRate = new MConversionRate(Env.getCtx(), order.getXX_ConversionRate_ID(), null);
		BigDecimal multiplyRate = conversionRate.getMultiplyRate();
			
		
		String SQL;

		String sql = "";

		sql = "UPDATE C_Order po"											//////////////////////
		+ " SET (XX_ProductQuantity, TotalPVP, XX_TotalPVPPlusTax, TotalLines, XX_TotalCostBs, XX_EstimatedMargin ) ="											//////////////////////
			+ "(SELECT COALESCE(SUM(XX_LineQty),0), COALESCE(SUM(XX_LinePVPAmount),0), COALESCE(SUM(XX_LinePlusTaxAmount),0), COALESCE(SUM(LineNetAmt),0), COALESCE(SUM(LineNetAmt),0)*" + multiplyRate + ", round(avg(xx_margin),2)  FROM XX_VMR_PO_LINEREFPROV line "
			+ "WHERE po.C_Order_ID=line.C_Order_ID ) "
		+ "WHERE po.C_Order_ID=" + getC_Order_ID();						//////////////////////
				//////////////////////
		DB.executeUpdate(get_Trx(), sql);

		String sql3 = "UPDATE C_Order po"											//////////////////////
			+ " SET XX_TotalCostBs = TotalLines"											//////////////////////
			+ " WHERE XX_TotalCostBs<1 and po.C_Order_ID=" + getC_Order_ID();						//////////////////////
		
		DB.executeUpdate(get_Trx(), sql3);
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;

		
		
		if  (order.getXX_DispatchDate()!=null || order.getXX_ArrivalDate()!=null)
		{					
			
			/** 
			 * 
			 * Logica de la data de imports
			 * Por: JTrias
			 * 
			 */
			
			
			if (order.getXX_OrderType().equals("Importada"))
			{
				
				//XX_INTNACESTMEDAMOUNT
				SQL = ("SELECT A.XX_INTERFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFI " +
				    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
				    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
				    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
				    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
				    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
				    		"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
				    		"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")");
				
				pstmt = null; 
			    rs = null;
				try{
					pstmt = DB.prepareStatement(SQL, null); 
					rs = pstmt.executeQuery();
					  
					if(rs.next())
					{
						//debo dividir el valor entre 100 porq es un porcentaje
						BigDecimal aux = rs.getBigDecimal("MEFI").divide(new BigDecimal(100));
						//Lo rendondeo  a 2 decimales
						aux=aux.setScale(2,BigDecimal.ROUND_UP);
						//seteo
						
						sql = "UPDATE C_Order po"											
							+ " SET XX_INTNACESTMEDAMOUNT=" + aux
							+ " WHERE po.C_Order_ID=" + getC_Order_ID();						
						DB.executeUpdate(get_Trx(), sql);
				    }
					
				}catch (Exception e) {
					log.log(Level.SEVERE, SQL);
				} finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				
				//XX_NationalEsteemedAmount
				SQL = ("SELECT DISTINCT A.XX_NACFREESTIMATEPERT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEFN " +
				    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
				    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
				    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
				    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
				    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
				    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
				    		"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
				    		"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")");
				    
				pstmt = null; 
			    rs = null;
				 try{
					
					pstmt = DB.prepareStatement(SQL, null); 
				    rs = pstmt.executeQuery();
					 
				    if(rs.next())
				    {
				    	//debo dividir el valor entre 100 porq es un porcentaje
						BigDecimal aux = rs.getBigDecimal("MEFN").divide(new BigDecimal(100));
						//Lo rendondeo  a 2 decimales
						aux=aux.setScale(2,BigDecimal.ROUND_UP);
						//seteo
						sql = "UPDATE C_Order po"											
							+ " SET XX_NationalEsteemedAmount=" + aux
							+ " WHERE po.C_Order_ID=" + getC_Order_ID();						
						DB.executeUpdate(get_Trx(), sql);
				    }
				    
				 }catch (Exception e) {
					 log.log(Level.SEVERE, SQL);
				 } finally {
					 DB.closeResultSet(rs);
					 DB.closeStatement(pstmt);
				 }
		
				//XX_CustomsAgentEsteemedAmount
				SQL = ("SELECT A.XX_ESTIMATEDPERTUSAGENT * (C.TOTALLINES * G.MULTIPLYRATE) AS MEAA " +
			    		"FROM XX_VLO_COSTSPERCENT A, C_ORDER C, C_CONVERSION_RATE G " +
			    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
			    		"AND G.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " +
			    		"AND C.XX_VLO_ARRIVALPORT_ID = A.XX_VLO_ARRIVALPORT_ID " +
			    		"AND C.C_BPARTNER_ID = A.C_BPARTNER_ID " +
			    		"AND C.C_COUNTRY_ID = A.C_COUNTRY_ID " +
			    		"AND A.AD_Client_ID IN(0,"+getAD_Client_ID()+") " +
			    		"AND C.AD_Client_ID IN(0,"+getAD_Client_ID()+")");
					
				pstmt = null; 
			    rs = null;
				 try{
						
					pstmt = DB.prepareStatement(SQL, null); 
				    rs = pstmt.executeQuery();
						 
				    if(rs.next())
				    {
				    	//debo dividir el valor entre 100 porq es un porcentaje
						BigDecimal aux = rs.getBigDecimal("MEAA").divide(new BigDecimal(100));
						//Lo rendondeo  a 2 decimales
						aux=aux.setScale(2,BigDecimal.ROUND_UP);
						//seteo
						sql = "UPDATE C_Order po"											
							+ " SET XX_CustomsAgentEsteemedAmount=" + aux
							+ " WHERE po.C_Order_ID=" + getC_Order_ID();						
						DB.executeUpdate(get_Trx(), sql);
				    }
				 }catch (Exception e) {
					 log.log(Level.SEVERE, SQL);
				 } finally {
					 DB.closeResultSet(rs);
					 DB.closeStatement(pstmt);
				 }
				 
				//XX_EsteemedInsuranceAmount
				 BigDecimal costo = new BigDecimal(0);
				 BigDecimal impuesto = new BigDecimal(0);
				 
				 SQL = ("SELECT G.XX_RATE,(C.TOTALLINES * H.MULTIPLYRATE) AS Costo " +
				    		"FROM C_ORDER C, XX_VLO_DISPATCHROUTE G, C_CONVERSION_RATE H " +
				    		"WHERE C.DOCUMENTNO = '"+order.getDocumentNo()+"' " +
				    		"AND C.XX_VLO_DISPATCHROUTE_ID = G.XX_VLO_DISPATCHROUTE_ID " +
				    		"AND H.C_CONVERSION_RATE_ID = C.XX_CONVERSIONRATE_ID " );
						    
				 pstmt = null; 
				 rs = null;
				 try{
							
					 pstmt = DB.prepareStatement(SQL, null); 
					 rs = pstmt.executeQuery();
					    
					 if(rs.next())
					 {
						 costo = rs.getBigDecimal("Costo");
					     costo = costo.add(order.getXX_NationalEsteemedAmount());
					     costo = costo.add(order.getXX_INTNACESTMEDAMOUNT());
					     costo = costo.add(impuesto);
					     costo = costo.multiply(rs.getBigDecimal("XX_RATE"));
					     //debo dividir el valor entre 100 porq es un porcentaje
					     costo = costo.divide(new BigDecimal(100));
					     
					     BigDecimal aux = costo;
						 //Lo rendondeo  a 2 decimales
						 aux=aux.setScale(2,BigDecimal.ROUND_UP);
						 //seteo
							sql = "UPDATE C_Order po"											
								+ " SET XX_ESTEEMEDINSURANCEAMOUNT=" + aux
								+ " WHERE po.C_Order_ID=" + getC_Order_ID();						
							DB.executeUpdate(get_Trx(), sql);
					 }
						    
				}catch (Exception e) {
					log.log(Level.SEVERE, SQL);
				} finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				
				//Guardo la orden de compra
				get_Trx().commit();
			}
			
//			POLimits(order);
			
		}//fin del Arrival date null
		
		return true;
	}	//	updateHeader
	

	@Override
	protected boolean beforeDelete()
	{
		String SQL = "SELECT XX_OrderStatus"
					+ " FROM C_Order"
					+ " WHERE C_Order_ID=" + getC_Order_ID();
		PreparedStatement prst = DB.prepareStatement(SQL,get_Trx());
		String status = "";
		ResultSet rs = null;
		try {
			rs = prst.executeQuery();
			if (rs.next()){
				status = rs.getString("XX_OrderStatus");
			}
		} catch (SQLException e){
			System.out.println(e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		
		if (!status.equals("PRO"))
		{
			log.saveError(null, Msg.getMsg(Env.getCtx(), "XX_OrderIsNotProforma"));
			return false;
		}	
		
		SQL =
			"Delete XX_VMR_REFERENCEMATRIX " 
			+"where XX_VMR_PO_LINEREFPROV_ID="+getXX_VMR_PO_LineRefProv_ID();
		
		try 
		{
			DB.executeUpdate(null, SQL);											
		}
		catch(Exception e)
		{	
			return false;
		} 
		
		return true;
	}	//	beforeDelete
	
	@Override
	protected boolean afterDelete(boolean success)
	{
		if (!success)
			return success;
	
		return updateHeader();
	}	//	beforeDelete

	
	/**
	 * Esta funcion indica si la O/C posee mas de N piezas
	 */
/**	private boolean exceedsQty(){
		
		int max = 5000;
		int actual = 0;
		
		String SQL = "Select sum(lp.qty*uc.XX_UNITCONVERSION/us.XX_UNITCONVERSION) SALEQTY " +
					 "from XX_VMR_PO_LINEREFPROV lp, XX_VMR_UnitConversion uc, XX_VMR_UnitConversion us " +
					 "where " +
					 "lp.XX_PiecesBySale_ID = us.XX_VMR_UnitConversion_ID " +
					 "and lp.XX_VMR_UnitConversion_ID = uc.XX_VMR_UnitConversion_ID " +
					 "and lp.XX_VMR_PO_LINEREFPROV_ID IN " +
					 "(SELECT XX_VMR_PO_LINEREFPROV_ID FROM XX_VMR_PO_LINEREFPROV WHERE C_ORDER_ID = " + getC_Order_ID(); 
					 
		if(get_ID()>0)
			SQL += " AND XX_VMR_PO_LINEREFPROV_ID <> " + get_ID();
					 
		SQL += ")";
		
		PreparedStatement prst = DB.prepareStatement(SQL, null);
		ResultSet rs = null;
		
		try {
			
			rs = prst.executeQuery();
			
			while (rs.next()){
				actual = actual + rs.getInt(1);
			}
			
		} catch (SQLException e){
			System.out.println(e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		
		actual = actual + getQty();
		
		if(actual > max)
			return true;
		
		return false;
	}
*/	
}
