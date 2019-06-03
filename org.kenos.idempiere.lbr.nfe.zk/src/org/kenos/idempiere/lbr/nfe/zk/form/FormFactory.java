package org.kenos.idempiere.lbr.nfe.zk.form;

import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.kenos.apps.form.WNotaFiscal;
import org.kenos.apps.form.WNotaFiscalAdditional;

/**
 * 	@author Ricardo Santana (Kenos, www.kenos.com.br)
 *	@version $Id: NFeFromFactory.java, v1.0 2017/08/28 6:32:03 PM, ralexsander Exp $
 */
public class FormFactory implements IFormFactory
{
	@Override
	public ADForm newFormInstance (String formName)
	{
		if ("org.kenos.apps.form.VNotaFiscal".equals(formName)
				|| "org.kenos.apps.form.WNotaFiscal".equals(formName))
		{
			WNotaFiscal invoice = new WNotaFiscal();
			IFormController controller = (IFormController) invoice;
			ADForm form = controller.getForm();
			form.setICustomForm(controller);
			return form;
		}
		else if ("org.compiere.apps.form.VAllocation".equals(formName)
				|| "org.compiere.apps.form.WAllocation".equals(formName))
		{
			WAllocation alloc = new WAllocation();
			IFormController controller = (IFormController) alloc;
			ADForm form = controller.getForm();
			form.setICustomForm(controller);
			return form;
		}
		else if ("org.kenos.apps.form.VNotaFiscalAdditional".equals(formName))
		{
			WNotaFiscalAdditional alloc = new WNotaFiscalAdditional();
			IFormController controller = (IFormController) alloc;
			ADForm form = controller.getForm();
			form.setICustomForm(controller);
			return form;
		}
		return null;
	}	//	newFormInstance
}	//	FormFactory
