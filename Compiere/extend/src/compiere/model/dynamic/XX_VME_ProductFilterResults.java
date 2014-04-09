package compiere.model.dynamic;

import java.util.Vector;

import org.compiere.util.KeyNamePair;

/** XX_VME_ProductFilterResults
 * Clase que representa el resultado de aplicar el Filtro de productos 
 * Para obtener información sobre los campos usados en el filtro se 
 * recorre cada uno de los vectores
 * **/
public class XX_VME_ProductFilterResults {
	public Vector<KeyNamePair> Category = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Department = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Line = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Section = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Reference = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Brand = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Vendor = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Partner = new Vector<KeyNamePair>();
	public Vector<KeyNamePair> Product = new Vector<KeyNamePair>();

	/** Constructor de los resultados del filtro de busqueda de productos **/
	public XX_VME_ProductFilterResults(Vector<KeyNamePair> PCategory, Vector<KeyNamePair> 
		PDepartment, Vector<KeyNamePair> PLine, Vector<KeyNamePair> PSection, Vector<KeyNamePair> 
		PReference, Vector<KeyNamePair> PBrand, Vector<KeyNamePair> PVendor, Vector<KeyNamePair> 
		PPartner, Vector<KeyNamePair> PProduct){
		Category = PCategory;
		Department = PDepartment;
		Line = PLine;
		Section = PSection;
		Reference = PReference;
		Brand = PBrand;
		Vendor = PVendor;
		Partner = PPartner;
		Product = PProduct;
	}//constructor
	
} // Fin XX_VME_ProductFilterResults
