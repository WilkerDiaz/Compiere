package compiere.model;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

public class VerificarCantidades extends CalloutEngine{
	
	public String cantidadDeProductos (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
			if (Integer.parseInt(mTab.get_ValueAsString("NroPagar"))>
				Integer.parseInt(mTab.get_ValueAsString("CantidadProducto"))){
				mTab.setValue("NroPagar", 0);
				return "¡El campo debe ser menor a la cantidad de productos a comprar!";
			}
			return "";
		}
	public String unicidadEnCombos (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		String codigo=mTab.get_ValueAsString("XX_PROMOCIONES_ID");
		String detalle=mTab.get_ValueAsString("XX_COMBO_ID");
		String sql = "SELECT grupo, NroPagar, CantidadProducto, tienda, porcdescuento from XX_Combo " +
				"where XX_Promociones_ID="+codigo+" and xx_combo_id<>"+detalle+" order by  xx_combo_id desc";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			//
			if (rs.next()){
				
				String grupo = rs.getString("grupo");
				String cant2 = rs.getString("NroPagar");
				String cant1 = rs.getString("CantidadProducto");
				String tienda = rs.getString("tienda");
				String porcdescuento = rs.getString("porcdescuento");

				if (mTab.get_ValueAsString("tienda").equals(tienda) && mTab.get_ValueAsString("grupo")==grupo){
					if(mTab.get_ValueAsString("NroPagar").equals(cant2)
							&& mTab.get_ValueAsString("CantidadProducto").equals(cant1)
							&& mTab.get_ValueAsString("porcdescuento").equals(porcdescuento))
						return "";
					else {
						mTab.setValue("CantidadProducto", Integer.parseInt(cant1));
						mTab.setValue("NroPagar", Integer.parseInt(cant2));
						mTab.setValue("porcdescuento", Double.parseDouble(porcdescuento));
						mTab.setValue("grupo", Integer.parseInt(grupo));
						return "";
					} 
				}
				rs.close();
				pstmt.close();
			}
		}catch (SQLException e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "";
	}
	
	public String unicidadEnRegaloPorCompra (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		String codigo=mTab.get_ValueAsString("XX_PROMOCIONES_ID");
		String sql = "SELECT XX_MONTOMINIMO, PORCDESCUENTO, CANTPRODCOMPRAR, tienda, regalo, ArtExistentes from XX_REGALOPPORCOMPRA " +
				"where XX_Promociones_ID="+codigo;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			//
			if (rs.next()){
				
				String monto = rs.getString("XX_MONTOMINIMO");
				BigDecimal porcDescuento = rs.getBigDecimal("PORCDESCUENTO");
				String cantidad = rs.getString("CANTPRODCOMPRAR");
				String tienda = rs.getString("tienda");
				String regalo = rs.getString("regalo");
				BigDecimal artexistentes= rs.getBigDecimal("artexistentes");

				if (mTab.get_ValueAsString("tienda").equals(tienda)){
					if(mTab.get_ValueAsString("XX_MontoMinimo ").equals(monto)
							&& mTab.get_ValueAsString("PorcDescuento").equals(porcDescuento)
							&& mTab.get_ValueAsString("CantProdComprar").equals(cantidad))
						return "";
					else {
						if(mTab.get_ValueAsString("TipoPromocion").equals("1000100")){
							mTab.setValue("XX_MontoMinimo", Integer.parseInt(monto));
							mTab.setValue("PorcDescuento", porcDescuento);
						}
						else if(mTab.get_ValueAsString("TipoPromocion").equals("1000100")){
							mTab.setValue("PorcDescuento",porcDescuento);
							mTab.setValue("CantProdComprar", cantidad);
						}
						else if(mTab.get_ValueAsString("TipoPromocion").equals("1000300")){
							mTab.setValue("CantProdComprar", Integer.parseInt(cantidad));
							mTab.setValue("PorcDescuento",porcDescuento);
						}	
						else if(mTab.get_ValueAsString("TipoPromocion").equals("1001200")){
							mTab.setValue("Regalo",regalo);
							mTab.setValue("ArtExistentes",artexistentes);
						}
						return "";
					} 
				}
				rs.close();
				pstmt.close();
			}
		}catch (SQLException e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "";
	}
	
	public String validarFecha (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		 if(new Date().after(new Date(ctx.getContextAsTime(WindowNo, "FechaInicio")))){
			 mTab.setValue("FechaInicio", null);
			 return "¡La fecha de inicio de la promoción debe ser posterior a la fecha de hoy!";
		 } else if (ctx.getContextAsTime(WindowNo,"FechaFin") <= ctx.getContextAsTime(WindowNo, "FechaInicio") &&
				 !ctx.get_ValueAsString("FechaFin").isEmpty()){
			 mTab.setValue("FechaInicio",oldValue);
			 return "¡La fecha de inicio de la promoción debe ser anterior a la fecha de fin!";
		 }//else return "¡La fecha de inicio de la promoción debe ser posterior a la fecha de hoy!";
		 return "";
	}
	public String validarFecha2 (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		if(ctx.getContextAsTime(WindowNo,"FechaFin") < ctx.getContextAsTime(WindowNo, "FechaInicio")){
			if (mTab.get_ValueAsString("FechaFin").isEmpty())
				return "";
			mTab.setValue("FechaFin",null);
			return "¡La fecha final de la promoción debe ser posterior a la fecha de inicio!";
		}
		return "";
	}
	public String validarHoras (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		if(ctx.getContextAsTime(WindowNo,"HoraFin") < ctx.getContextAsTime(WindowNo, "HoraInicio")){
			if (mTab.get_ValueAsString("HoraFin").isEmpty())
				return "";
			mTab.setValue("FechaFin",null);
			return "¡La hora final de la promoción debe ser posterior a la hora de inicio!";
		}
		return "";
	}
	public String validarFechaHora (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		 if(new Date().after(new Date(ctx.getContextAsTime(WindowNo, "FechaHoraSorteo")))){
			 mTab.setValue("FechaHoraSorteo", null);
			 return "¡La fecha del sorteo debe ser posterior a la fecha de hoy!";
		 }else return "";
	}
}
