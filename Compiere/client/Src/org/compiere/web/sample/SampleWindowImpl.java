/**
 * 
 */
package org.compiere.web.sample;

import java.util.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.intf.*;
import org.compiere.layout.*;
import org.compiere.util.*;
import org.compiere.vos.*;
import org.compiere.vos.WindowVO.*;

/**
 * Sample WindowImpl class for creating custom Forms for the Compiere Web UI.
 * 
 * @author gwu
 * 
 */
public class SampleWindowImpl implements WindowImplIntf {

	private static final CLogger s_log = CLogger.getCLogger( SampleWindowImpl.class );

	private final int windowNO;
	private final Ctx serverCtx;
	private final WindowCtx windowCtx;
	private final UWindowID uid;

	/**
	 * 
	 */
	public SampleWindowImpl(int windowNO, Ctx serverCtx, WindowCtx windowCtx, UWindowID uid) {
		this.windowNO = windowNO;
		this.serverCtx = serverCtx;
		this.windowCtx = windowCtx;
		this.uid = uid;
		
		s_log.fine("Language:" + Env.getLanguage(this.serverCtx) + " windowNO:" + this.windowNO + " AD_Form_ID:"
				+ this.uid.getAD_Form_ID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.WindowImplIntf#getClientWindowType()
	 */
	public ClientWindowType getClientWindowType() {
		return ClientWindowType.GENERIC_STACK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.WindowImplIntf#getComponents()
	 */
	public ArrayList<ComponentImplIntf> getComponents() {
		ArrayList<ComponentImplIntf> components = new ArrayList<ComponentImplIntf>();
		components.add(new SampleSearchComponentImpl());
		components.add(new SampleTableComponentImpl());
		return components;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.WindowImplIntf#getLayout()
	 */
	public Box getLayout() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.WindowImplIntf#getName()
	 */
	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.WindowImplIntf#processCallback(java.lang.String)
	 */
	public ChangeVO processCallback(String sender) {
		if( sender.equals("Search") )
		{
			ChangeVO changeVO = new ChangeVO();

			changeVO.queryComponents = new HashMap<Integer, QueryVO>();
			QueryVO queryVO = new QueryVO();
			if( null != windowCtx.get("C_BPartner_ID"))
			{
				queryVO.addRestriction(new QueryRestrictionVO("C_BPartner_ID", QueryRestrictionVO.EQUAL, windowCtx
						.get("C_BPartner_ID"), null, null, DisplayTypeConstants.ID));
			}
			changeVO.queryComponents.put(1, queryVO);
			
			return changeVO;
		}
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.WindowImplIntf#validateResponse(org.compiere.vos.ResponseVO)
	 */
	public void validateResponse(ResponseVO responseVO) {
	}

}
