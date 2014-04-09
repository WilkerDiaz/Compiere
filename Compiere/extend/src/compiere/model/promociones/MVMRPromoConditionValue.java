package compiere.model.promociones;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.compiere.framework.PO;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.promociones.forms.XX_PromoCondWarehouseForm;

public class MVMRPromoConditionValue extends X_XX_VMR_PromoConditionValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MVMRPromoConditionValue(Ctx ctx, int XX_VMR_PromoConditionValue_ID,
			Trx trx) {
		super(ctx, XX_VMR_PromoConditionValue_ID, trx);
	}
	
	public MVMRPromoConditionValue(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	public MVMRPromoConditionValue (MPromotion promotion)
	{
		this (promotion.getCtx(), 0, promotion.get_Trx());
		if (promotion.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
	}	
	
	protected boolean beforeSave (boolean newRecord)
	{
		String sqlUpdate = "Update XX_VMR_DETAILPROMOTIONEXT set IsActive = ? where XX_VMR_PROMOTION_ID = "+getXX_VMR_Promotion_ID()+" and XX_VMR_PromoConditionValue_ID = "+getXX_VMR_PromoConditionValue_ID()+" ";
		PreparedStatement ps = DB.prepareStatement(sqlUpdate, get_Trx());
		
		try{
			
			MPromotion promocion = new MPromotion(getCtx(), getXX_VMR_Promotion_ID(), get_Trx());
			
			if((promocion.getXX_TypePromotion().compareTo(X_Ref_XX_TypePromotion.A3__DESCUENTO_EN_PRODUCTOS_PUBLICADOS.getValue())==0)){
				
				BigDecimal iva = getIVA();
				
				BigDecimal individualAmount = iva.divide(new BigDecimal(100));
				individualAmount = individualAmount.add(BigDecimal.ONE);
				individualAmount = getXX_ComboAmount().divide(individualAmount, 2,BigDecimal.ROUND_DOWN);
				individualAmount = individualAmount.divide(getXX_QuantityPurchase(), 2, BigDecimal.ROUND_DOWN);
				
				setXX_DiscountAmount(individualAmount);
			}
			
			if (getXX_DiscountAmount()==null || getXX_DiscountAmount() == BigDecimal.ZERO)
					 setXX_DiscountAmount(new BigDecimal(0));
			if (getXX_DiscountRate()==null || getXX_DiscountRate() == BigDecimal.ZERO)
					 setXX_DiscountRate(new BigDecimal(0));
			
			
			if (!newRecord)
			{
				if (isActive())
					ps.setString(1, "Y");
				else
					ps.setString(1, "N");
				
				ps.executeUpdate();
			}		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	finally{
			DB.closeStatement(ps);
		}
		
	return true;
	}
	
	private BigDecimal getIVA(){
		
		BigDecimal iva = BigDecimal.ONE;
		
		String sql = "select RATE from C_TAX where VALIDFROM = " 
				+ "(SELECT MAX(validfrom) FROM C_TAX WHERE VALIDFROM <= sysdate " +
				"AND C_TAXCATEGORY_ID=" 
				+ Env.getCtx().getContext("#XX_L_TAXCATEGORY_IVA_ID") + " GROUP BY C_TaxCategory_ID)";
		
		CPreparedStatement pstmt = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			if(rs.next()){
				iva = rs.getBigDecimal(1); 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return iva;
	}
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{		
		MPromotion promocion = new MPromotion(getCtx(), getXX_VMR_Promotion_ID(), get_Trx());
		
		if (newRecord)
		{
			// Se tiene que cambiar apuntando a la columna de Tipo de Promocion
			if (!(promocion.getXX_TypePromotion().compareTo(X_Ref_XX_TypePromotion.A1__AHORRO_EN_COMPRA.getValue())==0)
			&&	!(promocion.getXX_TypePromotion().compareTo(X_Ref_XX_TypePromotion.A3__DESCUENTO_EN_PRODUCTOS_PUBLICADOS.getValue())==0)
			&&	!(promocion.getXX_TypePromotion().compareTo(X_Ref_XX_TypePromotion.B1__DESCUENTO_EN_EL_PRODUCTO_X_PLUS1.getValue())==0)
			&&	!(promocion.getXX_TypePromotion().compareTo(X_Ref_XX_TypePromotion.E1__REGALO_POR_COMPRA.getValue())==0)
			&&	!(promocion.getXX_TypePromotion().compareTo(X_Ref_XX_TypePromotion.E3__PROMOCIONES_CLÁSICAS.getValue())==0)
			&&	!(promocion.getXX_TypePromotion().compareTo(X_Ref_XX_TypePromotion.F1__PREMIO_ILUSIÓN.getValue())==0)
				)
			{
					
				
				MVMRDetailPromotionExt detallePromocion = new MVMRDetailPromotionExt(getCtx(), 0, get_Trx());
				ArrayList<Object> columnasIn = seleccionarColumnas(promocion.getXX_TypePromotion());
				if (columnasIn.size()<1)
					return false;
				ArrayList<String> columnas = (ArrayList<String>) columnasIn.get(0);
				String in =  (String) columnasIn.get(1);
				ArrayList<Object> valores = buscarValores( in, getXX_VMR_Promotion_ID(), columnas, get_Trx());
				
				detallePromocion.setXX_VMR_Promotion_ID(getXX_VMR_Promotion_ID());
				detallePromocion.setXX_VMR_PromoConditionValue_ID(getXX_VMR_PromoConditionValue_ID());
				
				for (int i=0; i < columnas.size(); i++)
				{
					detallePromocion.set_Value(columnas.get(i), valores.get(i));
				}
				
				detallePromocion.save(get_Trx());
			}	
		}
		else
		{
			//Realizamos el mismo algoritmo para ambos tipo de promocion, buscamos sus campos en condicion promocion y actualizamos los mismos en detalle promocion ext
			ArrayList<Object> columnasIn = seleccionarColumnas(promocion.getXX_TypePromotion());
			if (columnasIn.size()<1)
				return false;
			ArrayList<String> columnas = (ArrayList<String>) columnasIn.get(0);
			String in =  (String) columnasIn.get(1);
			ArrayList<Object> valores = buscarValores( in, getXX_VMR_Promotion_ID(), columnas, get_Trx());
			
			String set = construirUpdate(columnas, valores);
			
			//Ejecutamos el Update
			if (!set.equals(""))
			{
				String sql = "Update XX_VMR_DetailPromotionExt set "+set+" where XX_VMR_PromoConditionValue_ID = "+getXX_VMR_PromoConditionValue_ID()+" ";
				DB.executeUpdate(get_Trx(), sql);
			}
			else
				return false;
		}
		
		String sql = "SELECT COUNT(*) total FROM XX_VMR_PROMOCONDWAREHOUSE WHERE XX_VMR_PROMOCONDITIONVALUE_ID ="+getXX_VMR_PromoConditionValue_ID();
		CPreparedStatement pstmt = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getInt("total")==0){
					XX_PromoCondWarehouseForm.setAllWarehouses(getXX_VMR_PromoConditionValue_ID());
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return true;

	}
	
	private ArrayList<Object> seleccionarColumnas(String tipoPromocion)
	{
		ResultSet rsColumnas = null;
		String in = "";
		//nombres de las columnas dependiendo del tipo de promocio
		ArrayList<String> columnas = new ArrayList<String>();
		ArrayList<Object>  columnasIn = new ArrayList<Object>();
		String sqlCondicionPromocion ="select b.columnname from XX_VMR_ConditionPromotion a " +
        				"inner join AD_Column b on (a.AD_Column_ID = b.AD_Column_ID) where XX_TypePromotion = "+tipoPromocion+" " +
        				"and XX_Position >=1 and XX_Position < 10 order by XX_Position";
		PreparedStatement pstmt = DB.prepareStatement(sqlCondicionPromocion, null);
		try {
			rsColumnas = pstmt.executeQuery();
			while (rsColumnas.next())
			{
				columnas.add(rsColumnas.getString(1));
				in += rsColumnas.getString(1) + ",";
			}
			if(columnas.size()> 0 ) {
				in = in.substring(0, (in.length())-1);
				columnasIn.add(columnas);
				columnasIn.add(in);
				}
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rsColumnas);
			DB.closeStatement(pstmt);
		}
		return columnasIn;
	}
	
	private ArrayList<Object> buscarValores(String in, int promocion, ArrayList<String> nombreColumnas, Trx trans)
	{
		String sql = "select "+in+" from XX_VMR_PromoConditionValue where XX_VMR_Promotion_ID="+promocion+" and XX_VMR_PromoConditionValue_ID = "+getXX_VMR_PromoConditionValue_ID()+" group by "+in+"";
		ArrayList<Object> valores = new ArrayList<Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String valor;
		BigDecimal valorNumerico;
		try{
			ps = DB.prepareStatement(sql, trans);
			rs = ps.executeQuery();
			while (rs.next())
			{
				for (int i=0; i<nombreColumnas.size(); i++){
						valor = rs.getString(i+1);
						try{
							valorNumerico = new BigDecimal(valor);
							valores.add(valorNumerico);
						}catch(Exception e){
							valores.add(valor);
						}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return valores;
	}
	
	private String construirUpdate(ArrayList<String> columnas, ArrayList<Object> valores)
	{
		String set = "";
		Object valor;
		String valorToString = null;
		for(int i=0; i<columnas.size(); i++)
		{
			valor = valores.get(i);
			if(valor != null && (valor.getClass().equals(BigDecimal.class) || valor==null)){
				valorToString = valor.toString();
			} else if(valor!=null){
				valorToString = "'"+valor+"'";
			}
		
			set += columnas.get(i) + "=" + valorToString;
			if (i+1<columnas.size())
				set +=",";
		}
		return set;
	}
	
	private ArrayList<MVMRPromoConditionValue> getConsultConditionPromotion(int promotion_id)
	{
		String sql = "SELECT XX_VMR_PromoConditionValue_ID FROM XX_VMR_PromoConditionValue WHERE XX_VMR_Promotion_ID = " +promotion_id+ " AND isActive = 'Y' and AD_Client_ID = " +getCtx().getAD_Client_ID()+"";
		PreparedStatement psStatement = DB.prepareStatement(sql, null);
		ResultSet resultSet = null;
		ArrayList<MVMRPromoConditionValue> ListlinesConditionPromotion = new ArrayList<MVMRPromoConditionValue>(); 
		MVMRPromoConditionValue linesConditionPromotion_ID = null;
		
		try {
			resultSet = psStatement.executeQuery();
			while (resultSet.next()) {
				linesConditionPromotion_ID = new MVMRPromoConditionValue(getCtx(), resultSet.getInt(1), null);
				ListlinesConditionPromotion.add(linesConditionPromotion_ID);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DB.closeStatement(psStatement);
			DB.closeResultSet(resultSet);
		}
		
		return ListlinesConditionPromotion;
		
	}
	
	public ArrayList<ArrayList<MVMRPromoConditionValue>> getCopyConditionPromotion (MPromotion promotionNew, int promotionOld_ID)
	{
		ArrayList<MVMRPromoConditionValue> detailOld = getConsultConditionPromotion(promotionOld_ID);
		ArrayList<MVMRPromoConditionValue> detailNew = new ArrayList<MVMRPromoConditionValue>();
		MVMRPromoConditionValue lineConditionNew = null;
		ArrayList<ArrayList<MVMRPromoConditionValue>> oldNewDetailCondition = new ArrayList<ArrayList<MVMRPromoConditionValue>>();
		for (MVMRPromoConditionValue lineConditionOld : detailOld)
		{
			lineConditionNew = new MVMRPromoConditionValue (promotionNew);
			PO.copyValues(lineConditionOld, lineConditionNew);
			lineConditionNew.setXX_VMR_Promotion_ID (promotionNew.get_ID());
			lineConditionNew.save(get_Trx());
			detailNew.add(lineConditionNew);
		}
		
			oldNewDetailCondition.add(detailOld);
			oldNewDetailCondition.add(detailNew);
		
		return oldNewDetailCondition;
	}

}
