package compiere.model.suppliesservices.processes;

import java.awt.Container;
import java.text.DecimalFormat;
import java.lang.Boolean;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.MBPartner;
import org.compiere.model.X_AD_User;

public class XX_OtherReason extends SvrProcess {
	
	/** Purchase of Supplies and Services 
	 * Maria Vintimilla Funcion 002**/
	// Reason and Vendor's Categories
	private String reason, Assets, Service, Merchandise, Deactivation = "";
	private String category = "";
	
	/** When a Vendor Category is deactivated it's required a motive 
	 * Other Reason contains one or more reasons for the deactivation 
	 * of a Vendor Category for a Vendor, keeping the user that made 
	 * the deactivation, the date and the category **/
	
	private Integer checkCategories (MBPartner Vendor){
		Boolean CatAssets = Vendor.getXX_Assets();
		Boolean CatServices = Vendor.getXX_Services();
		Boolean CatMerchandise = Vendor.getXX_Merchandise();
		Integer Cont = 0;
		
		// All Vendor's Categories
		if (CatAssets && CatServices  && CatMerchandise){
			Cont= 3;
		}
		// Only two Vendor's Categories
		else if((CatAssets  && !CatServices && CatMerchandise) ||
				(CatAssets && CatServices&& !CatMerchandise) ||
				(!CatAssets && CatServices && CatMerchandise)){
			Cont = 2;
		}
		// Only one Vendor's Categories
		else if((CatAssets && !CatServices && !CatMerchandise) || 
				(!CatAssets && !CatServices && CatMerchandise) ||
				(!CatAssets && CatServices && !CatMerchandise)){
			Cont = 1;
		}

		return Cont;
	}// Fin checkCategories
	
	/** checkDeactivation
	 * Check the reason for Deactivation of a Vendor's category
	 * @param reason 
	 * @return reasonDeact Reason for deactivation
	 */
	private String checkDeactivation(String reason, String otherReason){
		String reasonDeact = "";
	
		if (reason.equals("I")){
			reasonDeact = "Incumplimiento condiciones de la Orden de Compra/Contrato";
		}
		else if (reason.equals("C")){
			reasonDeact = "Calidad del Producto/Servicio";
		}
		else if (reason.equals("D")){
			reasonDeact = "Condiciones desfavorables para el negocio";
		}
		else if (reason.equals("O")){
			reasonDeact = otherReason;
		}

		return reasonDeact;
	}// Fin checkDeactivation
	
	
	/** checkCategories
	 * Check Vendor's category for Deactivation
	 * @param Vendor Business Partner
	 * @return check 
	 */
	private boolean checkCat(MBPartner Vendor){
		boolean check = false;
		String error_msg = "No puede desactivar una categoría que está inactiva";
	
		if ((Assets.equals("Y") && !Vendor.getXX_Assets()) || 
			(Service.equals("Y") && !Vendor.getXX_Services()) ||
			(Merchandise.equals("Y") && !Vendor.getXX_Merchandise())){
			ADialog.info(1, new Container(),error_msg);
		}
		else {
			check = true;
		}

		return check;
	}// Fin checkDeactivation
	
	
	@Override
	protected String doIt() throws Exception {
		X_AD_User user = new X_AD_User(getCtx(), Env.getCtx().getAD_User_ID(), null);
		Calendar c = Calendar.getInstance();
		String day = Integer.toString(c.get(Calendar.DATE));
		Integer month = c.get(Calendar.MONTH)+1;
		DecimalFormat format = new DecimalFormat("00");
		String year = Integer.toString(c.get(Calendar.YEAR));
		String date = day+"/"+format.format(month)+"/"+year;
		MBPartner Vendor = new MBPartner(Env.getCtx(), getRecord_ID(), null);
		String other_reason = Vendor.getXX_OtherReason();
		Integer Cont2 = 0;
		boolean verify = false;
		Cont2 = checkCategories (Vendor);
		verify = checkCat(Vendor);
		
		if(verify){
			if (Assets.equals("Y") && Service.equals("Y")  && Merchandise.equals("Y")){
				ADialog.info(1, new Container(), "No puede desactivar todas las categorías");
			}
			else {
				if (Cont2.equals(1)){
					if((Vendor.getXX_Assets() && Assets.equals("Y")) || 
							(Vendor.getXX_Merchandise() && Merchandise.equals("Y")) ||
					   (Vendor.getXX_Services() && Service.equals("Y"))){
						ADialog.info(1, new Container(),
								"Debe tener al menos una categoría activada");
					}//Fin categories
				}
				else if(Cont2.equals(2) || Cont2.equals(3)){
					if(Assets.equals("Y")  && !Service.equals("Y") && Merchandise.equals("Y")){
							category = "Assets - Merchandise";
							Vendor.setXX_Services("N");
							Vendor.setXX_Merchandise("N");
							//Jessica Mendoza
							if (Vendor.getXX_InvoicePayments())
								Vendor.setXX_InvoicePayments(false);
					}// If AM
					else if(Assets.equals("Y") && Service.equals("Y")&& !Merchandise.equals("Y")){
							category = "Assets - Service";
							Vendor.setXX_Merchandise("N");
							Vendor.setXX_Services("N");
					}//If AS
					else if(!Assets.equals("Y") && Service.equals("Y") && Merchandise.equals("Y")){
							category = "Service - Merchandise";
							Vendor.setXX_Services("N");
							Vendor.setXX_Merchandise("N");
							//Jessica Mendoza
							if (Vendor.getXX_InvoicePayments())
								Vendor.setXX_InvoicePayments(false);
					}// If SM
					else if(Assets.equals("Y") && !Service.equals("Y")  && !Merchandise.equals("Y")){
							category = "Assets";
							Vendor.setXX_Assets("N");
							//Jessica Mendoza
							if (Vendor.getXX_InvoicePayments())
								Vendor.setXX_InvoicePayments(false);
					}// If A
					else if(!Assets.equals("Y")&& !Service.equals("Y") && Merchandise.equals("Y")){
							category = "Merchandise";
							Vendor.setXX_Merchandise("N");
					}// If M
					else if(!Assets.equals("Y") && Service.equals("Y") && !Merchandise.equals("Y")){
							category = "Service";
							Vendor.setXX_Services("N");
							//Jessica Mendoza
							if (Vendor.getXX_InvoicePayments())
								Vendor.setXX_InvoicePayments(false);
					}//If M
					// Verify reason
					if(!Deactivation.equals(null)){
						reason = checkDeactivation(Deactivation,reason);
					}// If Deactivation
					
					// Set the reason according the user, date and category
					if(other_reason.length()==0){
						other_reason += "("+user.getName()+" - "+ date+" - "+category+"): "+reason;
						Vendor.setXX_OtherReason(other_reason);
					}
					else if((other_reason.length()+reason.length())>500){
						other_reason = "+...\n"+"("+user.getName()+" - "+ date+category+"): "+reason;
						Vendor.setXX_OtherReason(other_reason);
					}
					else{
						other_reason += "\n"+"("+user.getName()+" - "+ date+" - "+category+"): "+reason;
						Vendor.setXX_OtherReason(other_reason);	
					}
					Vendor.save();
				}//Fin else Cont2
	
			}//Fin else 
		}// Fin if Verify

		return "";
	}// Fin doIt

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_OtherReason"))
				reason = element.getParameter().toString();
			else if(name.equals("XX_Assets"))
				Assets = element.getParameter().toString();
			else if(name.equals("XX_Services"))
				Service = element.getParameter().toString();
			else if(name.equals("XX_Merchandise"))
				Merchandise = element.getParameter().toString();
			else if(name.equals("XX_ReasonDeactivation_ID"))
				Deactivation = element.getParameter().toString();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}// Fin prepare
	
	
}//Fin XX_OtherReason
