package org.compiere.intf;

import java.util.*;

import org.compiere.layout.*;
import org.compiere.vos.*;
import org.compiere.vos.WindowVO.*;

/**
 * This is the interface to implement when you want to create a custom Form for
 * the web UI.
 * <p>
 * 
 * Note that the class that implements this interface needs to have a
 * constructor accepting the parameters (int windowNO, Ctx serverCtx, WindowCtx
 * windowCtx, UWindowID uid), since this is the constructor that the class
 * loading system will look for using the Java reflection API.
 * <p>
 * 
 * Once the class is created, the fully qualified Java class name needs to be
 * specified in AD_Form.WebClassname for the appropriate record.
 * <p>
 * 
 * @author gwu
 * 
 */
public interface WindowImplIntf {

	/**
	 * 
	 * @return The list of ComponentImplIntf objects that handle the data
	 *         retrieval for each component in the window.
	 */
	public abstract ArrayList<ComponentImplIntf> getComponents();

	/**
	 * This method allows the WindowImplIntf to handle callbacks from the
	 * client. By default, the fields that are buttons or have
	 * FieldVO.isImpactsValue = <b>true</b> will automatically trigger a
	 * callback to this method to allow server-side processing, e.g. dynamically
	 * changing the window layout, or perform database operations.
	 * 
	 * @param sender
	 *            The name of the field that initiated the callback.
	 * @return The ChangeVO indicating the types of changes that should be
	 *         reflected in the client UI, e.g. changed field values, message
	 *         pop-ups, new window layouts, etc.
	 */
	public abstract ChangeVO processCallback(String sender);

	/**
	 * Implement this method to return the translated name for the window. The
	 * language should be identified via the Ctx object passed in through the
	 * constructor.
	 * 
	 * @return The translated name of the window, or <b>null</b> to not display
	 *         the name.
	 */
	public abstract String getName();

	/**
	 * This method provides a mechanism for the server-side code to signal
	 * errors or warnings to the client upon the initial loading of the window.
	 * This is done by calling ResponseVO.addError() or ResponseVO.addWarning()
	 * if error conditions are detected. Note that if you want to use this
	 * method for validation during processCallback(), you need to call this
	 * method explicitly yourself.
	 * 
	 * @param responseVO
	 *            The ResponseVO object that will be passed back to the client.
	 */
	public abstract void validateResponse(ResponseVO responseVO);

	/**
	 * Implement this method to return a custom layout for the components. Note
	 * that this layout mechanism is currently only supported for client window
	 * type of GENERIC_STACK.
	 * 
	 * @return The layout specified using the Box model, or <b>null</b> to use
	 *         the default layout.
	 */
	public abstract Box getLayout();

	/**
	 * Implement this method to indicate the type of client-side UI that should
	 * be used to render this window. Only GENERIC_STACK is officially supported
	 * for 3rd party custom windows in this release.
	 * 
	 * @return The client window type, or <b>null</b> to use the default.
	 */
	public abstract ClientWindowType getClientWindowType();
}
