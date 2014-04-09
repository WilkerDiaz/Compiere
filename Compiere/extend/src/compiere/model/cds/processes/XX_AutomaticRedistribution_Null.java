package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import java.util.Vector;


import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_Brand;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_TypeInventory;

public class XX_AutomaticRedistribution_Null extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		Vector<MProduct> productos_basicos=new Vector<MProduct>();
		String sql = "SELECT M_Product_Id" //aqui buscare los productos correspondientes a la orden de compra para mandarlos al pedido...
			+ " FROM M_Product P where P.XX_TypeInventory_Id=1000005";
		PreparedStatement prst = DB.prepareStatement(sql,null);
		Vector<X_XX_VMR_Brand> marcas=new Vector<X_XX_VMR_Brand>();
		System.out.println("Va a entrar al ciclo!");
		try {
			ResultSet rs = prst.executeQuery();
			while(rs.next()){
				MProduct producto_temporal=new MProduct(getCtx(), rs.getInt("M_Product_Id"), get_TrxName());
				X_XX_VMR_TypeInventory tipo_producto= new X_XX_VMR_TypeInventory(getCtx(), producto_temporal.getXX_VMR_TypeInventory_ID(), get_TrxName());
				if(tipo_producto.getXX_VMR_TypeInventory_ID()==1000005){
					productos_basicos.add(producto_temporal);
					if(marcas.size()==0){
						X_XX_VMR_Brand marca_producto= new X_XX_VMR_Brand(getCtx(),producto_temporal.getXX_VMR_Brand_ID(),get_TrxName()); 
						marcas.add(marca_producto);
					}
					else{
						boolean agregar=true;
						X_XX_VMR_Brand marca_producto= new X_XX_VMR_Brand(getCtx(),producto_temporal.getXX_VMR_Brand_ID(),get_TrxName());
						for(int i=0; i<marcas.size();i++){
							if(marcas.elementAt(i).equals(marca_producto)){
								agregar=false;
							}
						}
						if(agregar){
							marcas.add(marca_producto);
						}
					}
				}
			}
			rs.close();
			prst.close();
			
			//System.out.println("Va a calcular los pedidos x producto! tamaño de productos_basicos= "+productos_basicos.size()+" tamaño de marcas= "+marcas.size());
			int[] cantidades_pedidoxproducto= new int[productos_basicos.size()];
			//Se calculan los pedidos colocados de basicos por marca...
			
			//Primero, lo hago por productos... y los agrego todos en un arreglo de cantidades pedidas por producto
			for(int i=0;i<productos_basicos.size();i++){
				String sql2="SELECT XX_ORDERBECOCORRELATIVE FROM XX_VMR_ORDER WHERE XX_ORDERREQUESTSTATUS=1000000 OR XX_ORDERREQUESTSTATUS=1000005";
				PreparedStatement prst2 = DB.prepareStatement(sql2, null);
				ResultSet rs2=prst2.executeQuery();
				while(rs2.next()){
					sql= "SELECT XX_PRODUCTQUANTITY FROM XX_VMR_ORDERREQUESTDETAIL WHERE M_PRODUCT_ID="+productos_basicos.elementAt(i).getM_Product_ID()+" AND XX_ORDERBECOCORRELATIVE='"+rs2.getString("XX_ORDERBECOCORRELATIVE")+"'";
					prst=DB.prepareStatement(sql,null);
					rs=prst.executeQuery();
					while(rs.next()){
						cantidades_pedidoxproducto[i]=rs.getInt("XX_PRODUCTQUANTITY")+cantidades_pedidoxproducto[i];
					}
					rs.close();
					prst.close();
				}
				rs2.close();
				prst2.close();
			}
			
			//al tener las cantidades de productos basicos en pedidos, los uno por marcas.
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//          DATO 3 de la formula!!!!!!!!!!!!!!!
			
			int[] cantidades_pedidoxmarcas=new int[marcas.size()];
			System.out.println("Va a calcular los pedidos x marca!");
			for(int i=0;i<marcas.size();i++){
				for(int j=0;j<productos_basicos.size();j++){
					X_XX_VMR_Brand marca_producto= new X_XX_VMR_Brand(getCtx(),productos_basicos.elementAt(j).getXX_VMR_Brand_ID(),get_TrxName());
					if(marcas.elementAt(i).equals(marca_producto)){
						cantidades_pedidoxmarcas[i]=cantidades_pedidoxmarcas[i]+cantidades_pedidoxproducto[j];
					}
				}
			}
			
			//           DATO 3 de la formula!!!!!!!!!!!!!!!
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			int[] inv_actualxproducto= new int[productos_basicos.size()];
			int[] ventas_actualxproducto= new int[productos_basicos.size()];
			
			int mes= Calendar.getInstance().get(Calendar.MONTH);
			int year= Calendar.getInstance().get(Calendar.YEAR);
			//ASUMO QUE EN VCN INVENTORY EL CENTRO DE DISTRIBUCION ESTA REPRESENTADO POR EL XX_STORE=1
			System.out.println("Va a calcular inventario y ventas!");
			for(int i=0; i<productos_basicos.size();i++){
				sql="SELECT * FROM XX_VCN_INVENTORY WHERE XX_CODEPRODUCT="+productos_basicos.elementAt(i).getM_Product_ID()+" AND XX_STORE=1 AND XX_INVENTORYMONTH="+(mes+1)+" AND XX_INVENTORYYEAR="+year;
				prst=DB.prepareStatement(sql, null);
				rs=prst.executeQuery();
				if(rs.next()){//asumo que solo hay una instancia de producto en CD para el mes y año que corresponde
					inv_actualxproducto[i]=inv_actualxproducto[i]+rs.getInt("XX_INITIALINVENTORYQUANTITY")+rs.getInt("XX_SHOPPINGQUANTITY")-rs.getInt("XX_SALESQUANTITY")+rs.getInt("XX_MOVEMENTQUANTITY")+rs.getInt("XX_ADJUSTMENTQUANTITY");
					ventas_actualxproducto[i]=ventas_actualxproducto[i]+rs.getInt("XX_SALESQUANTITY");
				}
				rs.close();
				prst.close();
			}
			
			//al tener las cantidades de productos basicos en INVENTARIO, los uno por marcas.
			int[] productos_basicosxmarca= new int[productos_basicos.size()];
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//          DATOS 2 Y 4 de la formula!!!!!!!!!!!!!!!
			
			System.out.println("Va a calcuar inventario y ventas por marcas");
			int[] cantidades_inventarioxmarcas=new int[marcas.size()];
			int[] cantidades_ventasxmarcas=new int[marcas.size()];
			for(int i=0;i<marcas.size();i++){
				for(int j=0;j<productos_basicos.size();j++){
					X_XX_VMR_Brand marca_producto= new X_XX_VMR_Brand(getCtx(),productos_basicos.elementAt(j).getXX_VMR_Brand_ID(),get_TrxName());
					if(marcas.elementAt(i).equals(marca_producto)){
						cantidades_inventarioxmarcas[i]=cantidades_inventarioxmarcas[i]+inv_actualxproducto[j];
						productos_basicosxmarca[i]++;
						int venta_actual= (ventas_actualxproducto[j]*Calendar.getInstance().get(Calendar.DAY_OF_MONTH))/Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
						cantidades_ventasxmarcas[i]=cantidades_ventasxmarcas[i]+venta_actual;
					}
				}
			}
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			int[] inv_finalpresupuesdatoxproducto= new int[productos_basicos.size()];
			
			//AHORA CALCULO EL INVENTARIO POR PRODUCTO DEPENDIENDO DE LA LINEA, SECCION Y CATEGORIA...
			System.out.println("Va a calcular inventario final presupuestado por producto");
			for(int i=0;i<productos_basicos.size();i++){
				String SQL13 = "SELECT XX_FINALINVAMOUNTBUD2 FROM XX_VMR_PRLD01 " +
	                    "WHERE XX_CODEDEPARTMENT = '"+productos_basicos.elementAt(i).getXX_VMR_Department_ID()+"' AND XX_LINECODE = '"+productos_basicos.elementAt(i).getXX_VMR_Line_ID()+"' AND XX_CODESECTION = '"+productos_basicos.elementAt(i).getXX_VMR_Section_ID()+"' AND XX_CODESTORE = 1";
				PreparedStatement pstmt13 = DB.prepareStatement(SQL13, null);
	            ResultSet rs13 = pstmt13.executeQuery();
	            if(rs13.next()){
	            	inv_finalpresupuesdatoxproducto[i]=inv_finalpresupuesdatoxproducto[i]+rs13.getInt("XX_FINALINVAMOUNTBUD2");
	            }
	            rs13.close();
	            pstmt13.close();
			}
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			//           Aqui debo calcular el numero de tiendas...
			//
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//          DATOS 1 de la formula!!!!!!!!!!!!!!!
			
			System.out.println("Va a calcular el inventario final presupuestado por marcas");
			int[] inv_finalpresupuesdatoxmarcas=new int[marcas.size()];
			for(int i=0;i<marcas.size();i++){
				for(int j=0;j<productos_basicos.size();j++){
					X_XX_VMR_Brand marca_producto= new X_XX_VMR_Brand(getCtx(),productos_basicos.elementAt(j).getXX_VMR_Brand_ID(),get_TrxName());
					if(marcas.elementAt(i).equals(marca_producto)){
						inv_finalpresupuesdatoxmarcas[i]=inv_finalpresupuesdatoxmarcas[i]+inv_finalpresupuesdatoxproducto[j];
					}
				}
			}
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// TOTAL A DISTRIBUIR POR MARCAS!!!
			
			System.out.println("Va a calcular el total a distribuir por marcas");
			int[] cantidades_a_distribuirxmarcas=new int[marcas.size()];
			for(int i=0;i<marcas.size();i++){
				cantidades_a_distribuirxmarcas[i]=inv_finalpresupuesdatoxmarcas[i]-cantidades_ventasxmarcas[i]-cantidades_pedidoxmarcas[i]-cantidades_inventarioxmarcas[i]+ 0;
				System.out.println("Esta es la cantidad["+i+"] "+cantidades_a_distribuirxmarcas[i]+" marca: "+marcas.elementAt(i).getName());
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			System.out.println("Sale de todo!");
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Total a distribuir por producto! y faltantes!!
			
			int[] cantidades_a_distribuirxproductos=new int[productos_basicos.size()];
			int[] faltantesxproductos=new int[productos_basicos.size()];
			for(int i=0; i<marcas.size();i++){
				int distribucionxbasico=cantidades_a_distribuirxmarcas[i]/productos_basicosxmarca[i];
				for(int j=0; i<productos_basicos.size();j++){
					if(productos_basicos.elementAt(j).getXX_VMR_Brand_ID()==marcas.elementAt(i).getXX_VMR_Brand_ID()){
						if(distribucionxbasico>=0){
							cantidades_a_distribuirxproductos[j]=distribucionxbasico;
							if(cantidades_a_distribuirxproductos[j]>inv_actualxproducto[j]){
								faltantesxproductos[j]=cantidades_a_distribuirxproductos[j]-inv_actualxproducto[j];
							}
							else{
								faltantesxproductos[j]=0;
							}
						}
						else{
							cantidades_a_distribuirxproductos[j]=0;
						}
						System.out.println("Cantidad a distribuir: "+cantidades_a_distribuirxproductos[j]+" producto: "+productos_basicos.elementAt(j).getName());
					}
				}
			}
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//Genero las ditribuciones por producto...
			
			X_XX_VMR_DistributionHeader distributionHeader= new X_XX_VMR_DistributionHeader(getCtx(),0,get_TrxName());
			distributionHeader.setXX_IsAutomaticRedistribution(true);
			distributionHeader.setXX_DistributionStatus(X_XX_VMR_DistributionHeader.XX_DISTRIBUTIONSTATUS_Pendiente);
			distributionHeader.setXX_HasTextilProducts(true);
			distributionHeader.save();
			
			
			
			
		}
		catch(SQLException e){
				e.printStackTrace();
			}
		return null;
	}

	@Override
	protected void prepare() {
		
		
	}

}
