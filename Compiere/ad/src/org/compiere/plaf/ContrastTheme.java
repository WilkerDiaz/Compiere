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
package org.compiere.plaf;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

/**
 * This class describes a higher-contrast Metal Theme.
 * 
 * @version 1.10 07/26/04
 * @author Michael C. Albers, Jorg Janke
 */
public class ContrastTheme extends DefaultMetalTheme
{

	@Override
	public String getName ()
	{
		return "Contrast";
	}

	private final ColorUIResource primary1
		= new ColorUIResource (0, 0, 0);

	private final ColorUIResource primary2
		= new ColorUIResource (204, 204, 204);

	private final ColorUIResource primary3
		= new ColorUIResource (255, 255, 255);

	private final ColorUIResource primaryHighlight
		= new ColorUIResource (102, 102, 102);

	private final ColorUIResource secondary2
		= new ColorUIResource (204, 204, 204);

	private final ColorUIResource secondary3
		= new ColorUIResource (255, 255, 255);

	@Override
	protected ColorUIResource getPrimary1 ()
	{
		return primary1;
	}

	@Override
	protected ColorUIResource getPrimary2 ()
	{
		return primary2;
	}

	@Override
	protected ColorUIResource getPrimary3 ()
	{
		return primary3;
	}

	@Override
	public ColorUIResource getPrimaryControlHighlight ()
	{
		return primaryHighlight;
	}

	@Override
	protected ColorUIResource getSecondary2 ()
	{
		return secondary2;
	}

	@Override
	protected ColorUIResource getSecondary3 ()
	{
		return secondary3;
	}

	@Override
	public ColorUIResource getControlHighlight ()
	{
		return super.getSecondary3 ();
	}

	@Override
	public ColorUIResource getFocusColor ()
	{
		return getBlack ();
	}

	@Override
	public ColorUIResource getTextHighlightColor ()
	{
		return getBlack ();
	}

	@Override
	public ColorUIResource getHighlightedTextColor ()
	{
		return getWhite ();
	}

	@Override
	public ColorUIResource getMenuSelectedBackground ()
	{
		return getBlack ();
	}

	@Override
	public ColorUIResource getMenuSelectedForeground ()
	{
		return getWhite ();
	}

	@Override
	public ColorUIResource getAcceleratorForeground ()
	{
		return getBlack ();
	}

	@Override
	public ColorUIResource getAcceleratorSelectedForeground ()
	{
		return getWhite ();
	}

	@Override
	public void addCustomEntriesToTable (UIDefaults table)
	{
		Border blackLineBorder = new BorderUIResource 
			(new LineBorder(getBlack ()));
		//
		Object textBorder = new BorderUIResource (new CompoundBorder 
			(blackLineBorder, new BasicBorders.MarginBorder()));

		//	Enhancements
		Object[] defaults = new Object[]
		{
			"ToolTip.border", blackLineBorder, 
			"TitledBorder.border", blackLineBorder, 
			"TextField.border", textBorder,
			"PasswordField.border", textBorder, 
			"TextArea.border", textBorder,
			"TextPane.border", textBorder, 
			"EditorPane.border", textBorder,
			//
			"ComboBox.background", getWindowBackground (),
			"ComboBox.foreground", getUserTextColor (),
			"ComboBox.selectionBackground", getTextHighlightColor (),
			"ComboBox.selectionForeground", getHighlightedTextColor (),
			"ProgressBar.foreground", getUserTextColor (),
			"ProgressBar.background", getWindowBackground (),
			"ProgressBar.selectionForeground", getWindowBackground (),
			"ProgressBar.selectionBackground", getUserTextColor (),
			"OptionPane.errorDialog.border.background", getPrimary1 (),
			"OptionPane.errorDialog.titlePane.foreground", getPrimary3 (),
			"OptionPane.errorDialog.titlePane.background", getPrimary1 (),
			"OptionPane.errorDialog.titlePane.shadow", getPrimary2 (),
			"OptionPane.questionDialog.border.background", getPrimary1 (),
			"OptionPane.questionDialog.titlePane.foreground", getPrimary3 (),
			"OptionPane.questionDialog.titlePane.background", getPrimary1 (),
			"OptionPane.questionDialog.titlePane.shadow", getPrimary2 (),
			"OptionPane.warningDialog.border.background", getPrimary1 (),
			"OptionPane.warningDialog.titlePane.foreground", getPrimary3 (),
			"OptionPane.warningDialog.titlePane.background", getPrimary1 (),
			"OptionPane.warningDialog.titlePane.shadow", getPrimary2 (),
		};
		table.putDefaults (defaults);
	}
}
