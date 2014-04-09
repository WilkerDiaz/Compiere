package compiere.model.dynamic;

import java.util.Vector;

/**  XX_VMA_ReportResults
 * Clase que permite crear los items de un reporte de análisis de Resultados
 * 
 * @author mvintimilla
 * */

public class XX_VMA_ReportResults {
	public Integer Season_ID = 0;
	public Integer Campaign_ID = 0;
	public Integer Activity_ID = 0;
	public Integer Brochure_ID = 0;
	public Integer BrochurePage_ID = 0;
	public Integer Element_ID = 0;
	public Integer Product_ID = 0;
	public Integer Category_ID = 0;
	public Integer Department_ID = 0;
	public Integer Line_ID = 0;
	public Integer Section_ID = 0;
	public Double VentasB = 0.0; 
	public Integer VentasP = 0;
	public Double InvIniB = 0.0;
	public Integer InvIniP = 0;
	public Integer InvIniPresC = 0;
	public Integer InvFinPresC = 0;
	public Double Compras = 0.0;
	public Double PVP = 0.0;
	public Double Costo = 0.0;
	public int Mes = 0;
	public int Anio = 0;
	public String NombreTemporada = "";
	public String NombreCampana = "";
	public String NombreActividad = "";
	public String NombreFolleto = "";
	public String NombrePagina = "";
	public String NombreElemento = "";
	public String NombreCategoria = "";
	public String NombreDepartamento = "";
	public String NombreLinea = "";
	public String NombreSeccion = "";
	public String Tipo = "";
	
	/** XX_VMA_ReportResults
	 * Resultados del Reporte de Análisis
	 * @param Season_ID Temporada
	 * @param Campaign_ID Colección
	 * @param Activity_ID Actividad de Mercadeo
	 * @param Brochure_ID Folleto
	 * @param BrochurePage_ID Pagina de Folleto
	 * @param Element_ID Elemento
	 * @param Product_ID Producto
	 * @param Category_ID Categoría
	 * @param Department_ID Departamento
	 * @param Line_ID Línea
	 * @param Section_ID Sección
	 * @param VentasB Ventas en BsF
	 * @param VentasP Ventas en Piezas
	 * @param InvIniB Inventario Inicial en BsF
	 * @param InvIniP Inventario Inicial en Piezas
	 * @param InvIniPresC Inventario Inicial Pres en BsF
	 * @param InvFinPresC Inventario Final Pres en BsF
	 * @param Compras Compras
	 * @param PVP PVP
	 * @param Costo Costo
	 * @param FechaMes Mes
	 * @param FechaAnio Anio 
	 * @param NombreTemporada Nombre de Temporada
	 * @param NombreCampana Nombre de Campaña
	 * @param NombreActividad Nombre de actividad de Mercadeo
	 * @param NombreFolleto Nombre de folleto
	 * @param NombrePagina Nombre de Pagina
	 * @param NombreElemento Nombre de elemento
	 * @param NombreCategoria Nombre de cartegoría
	 * @param NombreDepartamento Nombre de departamento
	 * @param NombreLinea Nombre de línea
	 * @param NombreSeccion Nombre de Sección
	 **/
	public XX_VMA_ReportResults(Integer PSeason_ID, Integer PCampaign_ID,
			Integer PActivity_ID, Integer PBrochure_ID, Integer PBrochurePage_ID,
			Integer PElement_ID, Integer PProduct_ID, Integer PCategory_ID, 
			Integer PDepartment_ID, Integer PLine_ID, Integer PSection_ID, 
			Double PVentasB,Integer PVentasP, Double PInvIniB,Integer PInvIniP, 
			Integer PInvIniPresC,Integer PInvFinPresC, Double PCompras, Double PPVP, 
			Double PCosto, int PMes, int PAnio,	String PNombreTemporada,
			String PNombreCampana, String PNombreActividad,String PNombreFolleto,
			String PNombrePagina, String PNombreElemento,String PNombreCategoria,
			String PNombreDepartamento,String PNombreLinea,	String PNombreSeccion,
			String PTipo){
		Season_ID = PSeason_ID;
		Campaign_ID = PCampaign_ID;
		Activity_ID = PActivity_ID;
		Brochure_ID = PBrochure_ID;
		BrochurePage_ID = PBrochurePage_ID;
		Element_ID = PElement_ID;
		Product_ID = PProduct_ID;
		Category_ID = PCategory_ID;
		Department_ID = PDepartment_ID;
		Line_ID = PLine_ID;
		Section_ID = PSection_ID;
		VentasB = PVentasB; 
		VentasP = PVentasP;
		InvIniB = PInvIniB;
		InvIniP = PInvIniP;
		InvIniPresC = PInvIniPresC;
		InvFinPresC = PInvFinPresC;
		Compras = PCompras;
		PVP = PPVP;
		Costo = PCosto;
		Mes = PMes;
		Anio = PAnio;
		NombreTemporada = PNombreTemporada;
		NombreCampana = PNombreCampana;
		NombreActividad = PNombreActividad;
		NombreFolleto = PNombreFolleto;
		NombrePagina = PNombrePagina;
		NombreElemento = PNombreElemento;
		NombreCategoria = PNombreCategoria;
		NombreDepartamento = PNombreDepartamento;
		NombreLinea = PNombreLinea;
		NombreSeccion = PNombreSeccion;
		Tipo = PTipo;
		
	}//constructor
	
	/** obtainValues
	 * Obtiene los valores iniciales para poder calcular los valores
	 * (Inventario inicial en Bsf, Inventario inicial en Piezas, 
	 * Compras, Ventas en BsF, Ventas en Piezas, PVP y Costo) 
	 * para el reporte de resultados por temporada 
	 * @param Report Items del reporte
	 * @param Type Tipo de calculo para hacer
	 * @param ID Identificador dependiendo del calculo
	 * @return Item con las cantidades calculadas 
	 * */
	//public XX_VMA_ReportResults obtainValues (Vector <XX_VMA_ReportResults> vectorReport,
	//Integer Type, XX_VMA_ReportResults item){
	public static Vector obtainValues (Vector <XX_VMA_ReportResults> vectorReport,
			Integer Type, Integer ID, XX_VMA_ReportResults item){
		// Variable que se devuelve con los resultados calculados
		Vector results = new Vector();
		Double InvIniB = 0.0;
		Integer InvIniP = 0;
		Double Compras = 0.0;
		Double VentasB = 0.0; 
		Integer VentasP = 0;
		Double PVP = 0.0;
		Double Costo = 0.0;
		switch (Type) {
		// Calcular Por Temporada
		case 1:
			for(int j = 0; j < vectorReport.size(); j++){
				/*System.out.println("Reporte Temporada\nElemento: "+vectorReport.get(j).Element_ID+
						" Producto: "+vectorReport.get(j).Product_ID+ " InvIniB: "+vectorReport.get(j).InvIniB+
						" InvIniP: "+vectorReport.get(j).InvIniP+" Compras: "+vectorReport.get(j).Compras+
						" VentasB: "+vectorReport.get(j).VentasB+" VentasP: "+vectorReport.get(j).VentasP+
						" PVP: "+vectorReport.get(j).PVP +" Costo: "+vectorReport.get(j).Costo);*/
				if(vectorReport.get(j).Season_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
					
				} // if Season 
			} // for season
			break;
		// Calcular Por Campaña
		case 2:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).Campaign_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Campaña 
			} // for Campaña
			break;
		// Calcular Por Actividad de Mercadeo
		case 3:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).Activity_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Actividad de Mercadeo 
			} // for Actividad de Mercadeo
			break;
		// Calcular Por Folleto
		case 4:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).Brochure_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Folleto
			} // for Folleto
			break;
		// Calcular Por Pagina de Folleto
		case 5:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).BrochurePage_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Pagina de Folleto
			} // for Pagina de Folleto
			break;
		// Calcular Por Elemento
		case 6:
			break;
		// Calcular Por Categoria
		case 7:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).Category_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Categoria
			} // for Categoria
			break;
		// Calcular Por Departmento
		case 8:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).Department_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Departmento
			} // for Departmento
			break;
		// Calcular Por Linea
		case 9:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).Line_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Linea
			} // for Linea
			break;
		// Calcular Por Seccion
		case 10:
			for(int j = 0; j < vectorReport.size(); j++){
				if(vectorReport.get(j).Section_ID.equals(ID)) {
					InvIniB = InvIniB + vectorReport.get(j).InvIniB;
					InvIniP = InvIniP + vectorReport.get(j).InvIniP;
					Compras = Compras + vectorReport.get(j).Compras;
					VentasB = VentasB + vectorReport.get(j).VentasB;
					VentasP = VentasP + vectorReport.get(j).VentasP;
					PVP = PVP + vectorReport.get(j).PVP;
					Costo = Costo + vectorReport.get(j).Costo;
				} // if Seccion
			} // for Seccion
			break;

		default:
			break;
		}
		results.add(InvIniB);
		results.add(InvIniP);
		results.add(Compras);
		results.add(VentasB); 
		results.add(VentasP);
		return results;
	} // obtainSeason

}//Fin XX_VMA_ReportResults
