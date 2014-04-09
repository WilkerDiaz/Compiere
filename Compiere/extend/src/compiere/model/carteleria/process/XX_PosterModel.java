package compiere.model.carteleria.process;

import java.awt.Container;
import java.math.BigDecimal;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import compiere.model.carteleria.X_XX_VMR_PTYPE;

/**
 * 
 * @author Ana Paradas
 * FECHA 28/07/2011
 */

/*
 * EL modelo permite insertar los modelos de carteles  en la base de datos
 * 
 * */

public class XX_PosterModel extends SvrProcess{
	

	/**
	 * Atributos
	 * 
	 * @param name
	 *            Nombre del cartel
	 * @param minAmount
	 *            Registra la cantidad minima de productos por cartel
	 * @param maxAmount
	 *            Registra la cantidad maxima de productos por cartel
	 *
	 */
	
	@Override
	protected String doIt() throws Exception {
		
		
		X_XX_VMR_PTYPE poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			
			/**  POSTER 1 .- ELISA **/
			poster.setName("Elisa /Jota");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(1));
			poster.save();
			
			/**  POSTER 2 .- CUBO **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Cubo");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(2));
			poster.save();
			
			/**  POSTER 3 .- CUBO ÚNICO **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Cubo único producto");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(1));
			poster.save();
			
			/**  POSTER 4 .- BASE PLANA **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Base Plana");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(2));
			poster.save();
			
			/**  POSTER 5 .- BASE PLANA UNICO **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Base Plana único");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(1));
			poster.save();
			
			/**  POSTER 6 .- BASE PLANA FAMILIA DE PRODUCTOS CAMA **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Base Plana Familia de Productos de Cama");
			poster.setminAmount(new BigDecimal(4));
			poster.setmaxAmount(new BigDecimal(5));
			poster.save();
			
			/**  POSTER 7 .- Base plana familia de productos de toallas **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Base Plana Familia de Productos de Toallas");
			poster.setminAmount(new BigDecimal(4));
			poster.setmaxAmount(new BigDecimal(5));
			poster.save();
			
			/**  POSTER 8 .- Vitrinas**/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Vitrinas");
			poster.setminAmount(new BigDecimal(3));
			poster.setmaxAmount(new BigDecimal(10));
			poster.save();
			
			/**  POSTER 9 .- Display / Tarimas **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Display / Tarimas");
			poster.setminAmount(new BigDecimal(3));
			poster.setmaxAmount(new BigDecimal(10));
			poster.save();
			
			/**  POSTER 10 .- CUBO COLGADO**/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Cubo Colgado");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(2));
			poster.save();
		
			/**  POSTER 11 .- LIBRE CARTA**/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Libre - Carta");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(2));
			poster.save();
	
			/**  POSTER 12 .- LIBRE CUBO **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Libre - Cubo");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(2));
			poster.save();
			
			/**  POSTER 13 .- LIBRE BASE PLANA **/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Libre - Base Plana");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(2));
			poster.save();
			
			/**  POSTER 14 .- LIBRE VITRINA**/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Libre - Vitrinas");
			poster.setminAmount(new BigDecimal(3));
			poster.setmaxAmount(new BigDecimal(10));
			poster.save();
			
			/**  POSTER 11 .- LIBRE CARTA HORIZONTAL**/
			poster = new X_XX_VMR_PTYPE(getCtx(), 0, null);
			poster.setName("Libre - Carta Horizontal");
			poster.setminAmount(new BigDecimal(1));
			poster.setmaxAmount(new BigDecimal(1));
			poster.save();
		
		
		ADialog.info(1, new Container(), "Inserto los datos en carteleria");
		return null;
	}

	@Override
	protected void prepare() {
				
	}

}