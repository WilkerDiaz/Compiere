package compiere.model.dynamic;

import java.math.BigDecimal;
import java.util.HashMap;

/** XX_VME_BrochureItemIndepabis
 * Elementos de un folleto para el reporte de Indepabis 
 * @author mvintimilla
 * */
public class XX_VME_BrochureItemIndepabis {

	public String NombrePromocion = "";
	public String Pagina = "";	
	public String Desde = "";
	public String Hasta = "";       	
	public String Codigo = "";  
	public String Product_ID = "";
	public String Type = "";
	public String Descripcion = "";   
	public BigDecimal PVPRegular = new BigDecimal(0);   	
	public BigDecimal PorcentajeDescuento = new BigDecimal(0); 
	public BigDecimal PVPDescuento = new BigDecimal(0);  	
	public BigDecimal Quantity = new BigDecimal(0); 
	public String Elemento = "";
	
	public HashMap<String, BigDecimal> tiendaCantidad = new HashMap<String, BigDecimal>();
	
	/*public BigDecimal PuenteYanes =new BigDecimal(0); 	
	public BigDecimal Chacaito = new BigDecimal(0);   
	public BigDecimal Tamanaco = new BigDecimal(0); 
	public BigDecimal LaGranja = new BigDecimal(0);   
	public BigDecimal LasTrinitarias = new BigDecimal(0);   	
	public BigDecimal LaTrinidad = new BigDecimal(0);  
	public BigDecimal Maracaibo = new BigDecimal(0);   
	public BigDecimal Millenium = new BigDecimal(0);   */
	public BigDecimal TotalInventario = new BigDecimal(0);  
		
	public XX_VME_BrochureItemIndepabis(String PNombrePromocion, String PPagina,
			String PDesde, String PHasta, String PCodigo, String PProduct_ID, String PType,
			String PDescripcion,BigDecimal PPVPRegular, BigDecimal PPorcentajeDescuento, 
			BigDecimal PPVPDescuento, BigDecimal PQuantity , /*BigDecimal 
			PPuenteYanes, BigDecimal PChacaito, BigDecimal PTamanaco, BigDecimal PLaGranja, 
			BigDecimal PLasTrinitarias, BigDecimal PLaTrinidad, BigDecimal PMaracaibo, BigDecimal 
			PMillenium,*/ BigDecimal PTotalInventario, String idElemento){
		NombrePromocion = PNombrePromocion;
		Pagina = PPagina;	
		Desde = PDesde;
		Hasta = PHasta;       	
		Codigo = PCodigo; 
		Product_ID = PProduct_ID;
		Type = PType;
		Descripcion = PDescripcion;     	
		PVPRegular = PPVPRegular;   	
		PorcentajeDescuento = PPorcentajeDescuento;  	
		PVPDescuento = PPVPDescuento; 
		Quantity = PQuantity;
		/*PuenteYanes = PPuenteYanes;	
		Chacaito = PChacaito;
		Tamanaco = PTamanaco;
		LaGranja = PLaGranja;
		LasTrinitarias = PLasTrinitarias;	
		LaTrinidad = PLaTrinidad;
		Maracaibo = PMaracaibo;
		Millenium = PMillenium;*/
		TotalInventario= PTotalInventario;
		Elemento=idElemento;
		
	}//constructor
	
}//XX_VME_BrochureItemIndepabis


