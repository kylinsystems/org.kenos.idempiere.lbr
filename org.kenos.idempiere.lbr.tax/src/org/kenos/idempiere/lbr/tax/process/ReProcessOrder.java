package org.kenos.idempiere.lbr.tax.process;

import java.util.Map;
import java.util.logging.Level;

import org.adempiere.model.POWrapper;
import org.adempierelbr.model.MLBRTax;
import org.adempierelbr.model.MLBRTaxLine;
import org.adempierelbr.wrapper.I_W_AD_OrgInfo;
import org.adempierelbr.wrapper.I_W_C_BPartner;
import org.adempierelbr.wrapper.I_W_C_Order;
import org.adempierelbr.wrapper.I_W_M_Product;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MProduct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * 		ReProcess Order
 * 	@author Ricardo Santana (Kenos, www.kenos.com.br)
 *	@version $Id: ReProcessOrder.java, v1.0 2018/01/11 12:58:16 PM, ralexsander Exp $
 */
public class ReProcessOrder extends SvrProcess
{
	/**	Order	*/
	private int p_C_Order_ID;

	private boolean p_ReCalculateTax	= false;
	private boolean p_ReDefineTax 		= false;
	private boolean p_ReDefineCFOP 		= false;
	private boolean p_DistributeFreight	= false;
	private boolean p_EnforcePrice 		= false;
	
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			
			else if (name.equals("lbr_RecalculateTax"))
				p_ReCalculateTax = "Y".equals(para[i].getParameter());
			
			else if (name.equals("LBR_RedefineTax"))
				p_ReDefineTax = "Y".equals(para[i].getParameter());
			
			else if (name.equals("LBR_RedefineCFOP"))
				p_ReDefineCFOP = "Y".equals(para[i].getParameter());
			
			else if (name.equals("LBR_DistributeFreight"))
				p_DistributeFreight = "Y".equals(para[i].getParameter());
			
			else if (name.equals("LBR_EnforcePriceList"))
				p_EnforcePrice = "Y".equals(para[i].getParameter());
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_Order_ID = getRecord_ID();
	}	//	prepare

	@Override
	protected String doIt() throws Exception
	{
		MOrder order = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
		I_W_AD_OrgInfo oi = POWrapper.create(MOrgInfo.get(Env.getCtx(), order.getAD_Org_ID(), null), I_W_AD_OrgInfo.class);
		I_W_C_Order o = POWrapper.create(order, I_W_C_Order.class);
		I_W_C_BPartner bp = POWrapper.create(new MBPartner (Env.getCtx(), o.getC_BPartner_ID(), null), I_W_C_BPartner.class);
		MBPartnerLocation bpLoc = (MBPartnerLocation) order.getBill_Location(); 
		//
		if (p_ReDefineTax || p_ReDefineCFOP)
		{
			for (MOrderLine ol : order.getLines())
			{
				I_W_M_Product p = POWrapper.create(new MProduct (Env.getCtx(), ol.getM_Product_ID(), null), I_W_M_Product.class);
				Object[] taxation = MLBRTax.getTaxes (o.getC_DocTypeTarget_ID(), o.isSOTrx(), o.getlbr_TransactionType(), p, oi, bp, bpLoc, o.getDateAcct());
				//
				if (p_ReDefineCFOP && ((Integer) taxation[2]) > 0)
					ol.set_CustomColumn ("LBR_CFOP_ID", ((Integer) taxation[2]));
				
				@SuppressWarnings("unchecked")
				Map<Integer, MLBRTaxLine> taxes = (Map<Integer, MLBRTaxLine>) taxation[0];
				if (p_ReDefineTax && taxes.size() > 0)
				{
					MLBRTax tax = new MLBRTax (Env.getCtx(), 0, null);
					tax.save();
					//
					for (Integer key : taxes.keySet())
					{
						MLBRTaxLine tl = taxes.get(key);
						tl.setLBR_Tax_ID(tax.getLBR_Tax_ID());
						tl.save();
					}
					//
					tax.setDescription();
					tax.save();
					//
					ol.set_CustomColumn("LBR_Tax_ID", tax.getLBR_Tax_ID());
				}
				ol.save();
			}
		}
		return "@Success@";
	}	//	doIt
}	//	ReProcessOrder