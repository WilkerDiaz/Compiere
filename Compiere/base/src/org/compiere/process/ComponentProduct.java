/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

import java.math.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Create/Maintain Component Product
 *	@author Jorg Janke
 */
public class ComponentProduct extends SvrProcess
{
	/**	Registration ID				*/
	private int				p_AD_ComponentReg_ID = 0;
	/** Registration				*/
	private MComponentReg	m_reg = null;
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare()
	{
		p_AD_ComponentReg_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 * 	@return summary
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_ComponentReg_ID=" + p_AD_ComponentReg_ID);
		if (p_AD_ComponentReg_ID == 0)
			throw new CompiereSystemException("No Registration ID");
		m_reg = new MComponentReg(getCtx(), p_AD_ComponentReg_ID, get_TrxName());
		if (m_reg.get_ID() != p_AD_ComponentReg_ID)
			throw new CompiereSystemException("Registration not found ID=" + p_AD_ComponentReg_ID);
		if (m_reg.getM_Product_Category_ID() == 0)
			throw new CompiereSystemException("@NotFound@ @M_Product_Category_ID@");
		
		StringBuffer info = new StringBuffer();
		int C_UOM_ID = MUOM.Each_ID;
		
		//	Product
		MProduct product = new MProduct(getCtx(), m_reg.getM_Product_ID(), get_TrxName());
		if (m_reg.getM_Product_ID() == 0)
		{
			product.setClientOrg(m_reg);
			product.setC_UOM_ID(C_UOM_ID);
			MTaxCategory tc = MTaxCategory.getDefault(getCtx());
			product.setC_TaxCategory_ID(tc.getC_TaxCategory_ID());
		}
		product.setValue("Component " + m_reg.getComponentName());
		StringBuffer name = new StringBuffer(m_reg.getName());
		if (m_reg.getVersion() != null)
			name.append(" ").append(m_reg.getVersion());
		product.setName(name.toString());
		product.setDescription(m_reg.getDescription());
		if (m_reg.getM_Product_Category_ID() != 0)
			product.setM_Product_Category_ID(m_reg.getM_Product_Category_ID());
		product.setLicenseInfo(m_reg.getComponentName());
		product.setTrialPhaseDays(m_reg.getTrialPhaseDays());
		product.setIsActive(m_reg.isActive());
		product.setIsSelfService(true);
		product.setIsWebStoreFeatured(true);
		//	Documentation
		String doc = m_reg.getDocumentationText();
		if (!Util.isEmpty(doc))
		{
			String descriptionURL = "/wstore" 
				+ "/documentServlet/" + m_reg.getComponentName() 
				+ ".html?ID=" + m_reg.getAD_ComponentReg_ID();
			product.setDescriptionURL(descriptionURL);
		}

		if (!product.save())
		{
			if (m_reg.getM_Product_ID() == 0)
				throw new CompiereSystemException("Cannot create Product");
			throw new CompiereSystemException("Cannot update Product");
		}
		//	Product Info
		info.append(product.getName());
		if (m_reg.getM_Product_ID() == 0)
		{
			m_reg.setM_Product_ID(product.getM_Product_ID());
			m_reg.save();
		}
		//	Vendor
		MBPartner partner = new MBPartner(getCtx(), m_reg.getC_BPartner_ID(), get_TrxName());
		if (!partner.isVendor())
		{
			partner.isVendor();
			partner.save();
		}

		BigDecimal price = m_reg.getPriceStd();
		int C_Currency_ID = 0;
		//	Price List
		if (m_reg.getM_PriceList_Version_ID() != 0 && price.signum() != 0)
		{
			MPriceListVersion plv = new MPriceListVersion(getCtx(), m_reg.getM_PriceList_Version_ID(), get_TrxName());
			C_Currency_ID = plv.getPriceList().getC_Currency_ID();
			MProductPrice pp = MProductPrice.get(getCtx(), 
					m_reg.getM_PriceList_Version_ID(), product.getM_Product_ID(), get_TrxName());
			if (pp == null)
				pp = new MProductPrice(plv, product.getM_Product_ID(), price, price, price);
			else
				pp.setPrices(price, price, price);
			pp.save();
			info.append(" - ").append(plv.getName()).append(" ").append(price);
		}
		else
			C_Currency_ID = MClient.get(getCtx(), m_reg.getAD_Client_ID()).getC_Currency_ID();

		//	Product Vendor
		MProductPO[] vendors = MProductPO.getOfProduct(getCtx(), product.getM_Product_ID(), get_TrxName());
		MProductPO vendor = null;
		for (MProductPO v : vendors) {
			if (v.getC_BPartner_ID() == partner.getC_BPartner_ID())
			{
				vendor = v;
				break;
			}
			else if (v.isCurrentVendor())
			{
				v.setIsCurrentVendor(false);
				v.save();
			}
		}
		if (vendor == null)
		{
			vendor = new MProductPO(getCtx(), 0, get_TrxName());
			vendor.setClientOrg(product);
			vendor.setM_Product_ID(product.getM_Product_ID());
			vendor.setC_BPartner_ID(partner.getC_BPartner_ID());
		}
		vendor.setIsCurrentVendor(true);
		vendor.setVendorProductNo(m_reg.getComponentName());
		vendor.setC_Currency_ID(C_Currency_ID);
		vendor.setC_UOM_ID(C_UOM_ID);
		vendor.setPriceList(price);
		vendor.save();
		
		//	Download
		MAttachment attachment = m_reg.getAttachment(true);	//	requery
		if (attachment != null && attachment.getEntryCount() > 0)
		{
			MAttachmentEntry entry = attachment.getEntry(0);	//	first only
			//
			MProductDownload[] downloads = product.getProductDownloads(true);
			MProductDownload download = null;
			if (downloads.length > 0)
				download = downloads[0];
			if (download == null)
			{
				download = new MProductDownload(getCtx(), 0, get_TrxName());
				download.setClientOrg(product);
				download.setM_Product_ID(product.getM_Product_ID());
			}
			download.setName(product.getName());
			download.setDownloadURL(entry.getName());
			download.save();
			if (download.saveDownload(entry))		//	deploy car file in DocumentDir
				info.append(" - Download: " + entry.toStringX());
			else
				info.append(" - NO Download!");
		}
		
		return info.toString();
	}	//	doIt

}	//	ComponentProduct
