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
package org.compiere.vos;

import java.io.*;
import java.util.*;

import org.compiere.vos.ResponseVO.Message.*;

/** @author dzhao
 * A common interface for passing back error, warning and success messages from server side
 * AsyncCommand will first intercept the message and display warning/error messages in a modal dialog box
 */
public class ResponseVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static class Message implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MessageType type;
		private String message;
		
		/**
		 * Explicit commands for the client to execute
		 */

		public enum MessageType { SUCCESS, ERROR, WARNING };
		
		public Message()
		{
		}

		public Message(MessageType type, String message)
		{
			this.type = type;
			this.message = message;
		}

		public String getMessage()
		{
			return message;
		}

		public MessageType getType()
		{
			return type;
		}

		@Override
		public String toString()
		{
			if (type == MessageType.SUCCESS)
				return "Success: " + message;
			if (type == MessageType.ERROR)
				return "Error: " + message;
			if (type == MessageType.WARNING)
				return "Warning: " + message;
			return "NO MESSAGE";
		}
		
		public String toDisplay()
		{
			return message;
		}

	}	//	Message

	/**
	 * usually there are only one message
	 */
	protected ArrayList<Message>	messages	= new ArrayList<Message>(1);
	protected boolean	hasError	= false;
	protected boolean	hasWarning	= false;
	protected boolean	hasResults  = false;
	//even if it has errors, should still pass through the rest of the operation.
	//default no
	protected boolean   passThrough = false;

	protected HashMap<String,String>	params		= null;

	public String[]     commands = null;
	
	//if replaceWindowNO is not -1, we will attempt to replace window no identified with replaceWindowNO with the new windowVO supplied
	public int targetWinCmd = -1;
	//reload targetWin with new windowVO


	public void addParam(String var, String v)
	{
		if (params == null)
			params = new HashMap<String, String>();
		params.put(var, v);
	}

	public String getParam(String var)
	{
		if (params == null)
			return null;
		return params.get(var);
	}

	public int getProcessID()
	{
		if (getParam("START_PROCESS_ID") == null)
			return -1;
		return Integer.parseInt(getParam("START_PROCESS_ID"));
	}

	public void setProcessID(int processID)
	{
		addParam("START_PROCESS_ID", Integer.toString(processID));
	}

	public boolean isRefreshAll()
	{
		if (getParam("REFRESH_ALL_ROWS") == null)
			return false;
		return "Y".equals(getParam("REFRESH_ALL_ROWS"));
	}

	public void setRefreshAll(boolean refresh) {
		if(refresh)
			addParam("REFRESH_ALL_ROWS", "Y");
		else
			addParam("REFRESH_ALL_ROWS", "N");
	}

	/**
	 * 	Add Clear Text message
	 *	@param type constants like ERROR, WARNING, SUCCESS 
	 *	@param message clear text message
	 */
	private void addMessage(MessageType type, String message)
	{
		if (type == Message.MessageType.ERROR)
			hasError = true;
		if (type == Message.MessageType.WARNING)
			hasWarning = true;
		messages.add(new Message(type, message));
	}	//	addMessage

	/**
	 * 	Add clear text error message
	 *	@param message clear text message
	 */
	public void addError(String message)
	{
		addMessage(Message.MessageType.ERROR, message);
	}

	/**
	 * 	Add clear text warning message
	 *	@param message clear text message
	 */
	public void addWarning(String message)
	{
		addMessage(Message.MessageType.WARNING, message);
	}

	/**
	 * 	Add clear text success message
	 *	@param message clear text message
	 */
	public void addSuccess(String message)
	{
		addMessage(Message.MessageType.SUCCESS, message);
	}

	/**
	 * usually there are only one message
	 */

	public ArrayList<Message> getMessages()
	{
		return messages;
	}

	public boolean hasError()
	{
		return hasError;
	}

	public boolean hasWarning()
	{
		return hasWarning;
	}

	public boolean hasResults()
	{
		return hasResults;
	}

	/**
	 * Normally success results are not shown in a dialog.
	 * @param hasResults
	 */
	public void showResults( boolean hasResults )
	{
		if (this.messages.size() == 0){
			hasResults = false;
		}
		
		this.hasResults = hasResults;
	}
	
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < messages.size(); i++)
		{
			Message m = messages.get(i);
			if (i > 0)
				sb.append(",");
			sb.append(m.toString());
		}
		return sb.toString();
	}
	
	public String toDisplay() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < messages.size(); i++)
		{
			Message m = messages.get(i);
			if (i > 0)
				sb.append("<br>");
			sb.append(m.toDisplay());
		}
		return sb.toString();
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public boolean isPassThrough() {
		return passThrough;
	}

	public void setPassThrough(boolean passThrough) {
		this.passThrough = passThrough;
	}
	
	public void setRefreshChildRows(boolean refreshChildRows)
	{
		if(refreshChildRows)
			addParam("REFRESH_CHILD_ROWS", "Y");
		else
			addParam("REFRESH_CHILD_ROWS", "N");

	}
	
	public boolean isRefreshChildRows()
	{
		if (getParam("REFRESH_CHILD_ROWS") == null)
			return false;
		return "Y".equals(getParam("REFRESH_CHILD_ROWS"));
	}

}	//	Response
