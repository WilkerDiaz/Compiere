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
package org.compiere.translate;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;


/**
 *	Spanish Amount in Words
 *	
 *  @version $Id: AmtInWords_ES.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class AmtInWords_ES implements AmtInWords
{

	private static final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("es"));

	/**
	 * 	AmtInWords_ES
	 */
	public AmtInWords_ES ()
	{
		super ();
	} //	AmtInWords_ES

	private static final String[]	majorNames	= {
		"", 
		" MIL", 
		" MILLON",
		" BILLON", 
		" TRILLON", 
		" CUATRILLON", 
		" QUINTRILLON"
	};

	private static final String[]	tensNames	= { 
		"", 
		" DIEZ", 
		" VEINTE",
		" TREINTA", 
		" CUARENTA", 
		" CINCUENTA", 
		" SESENTA", 
		" SETENTA",
		" OCHENTA", 
		" NOVENTA"
	};

	private static final String[]	numNames	= { 
		"", 
		" UNO", 
		" DOS",
		" TRES", 
		" CUATRO", 
		" CINCO", 
		" SEIS", 
		" SIETE", 
		" OCHO", 
		" NUEVE",
		" DIEZ", 
		" ONCE", 
		" DOCE", 
		" TRECE", 
		" CATORCE", 
		" QUINCE",
		" DIECISEIS", 
		" DIECISIETE", 
		" DIECIOCHO", 
		" DIECINUEVE"
	};

	/**
	 * 	Convert Less Than One Thousand
	 *	@param number
	 *	@return amt
	 */
	private String convertLessThanOneThousand (int number)
	{
		String soFar;
		// Esta dentro de los 1os. diecinueve?? ISCAP
		if (number % 100 < 20)
		{
			soFar = numNames[number % 100];
			number /= 100;
		}
		else
		{
			soFar = numNames[number % 10];
			number /= 10;
			String s = Integer.toString (number);
			if (s.endsWith ("2") && soFar != "")
				soFar = " VEINTI" + soFar.trim ();
			else if (soFar == "")
				soFar = tensNames[number % 10] + soFar;
			else
				soFar = tensNames[number % 10] + " Y" + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		if (number > 1)
			soFar = "S" + soFar;
		if (number == 1 && soFar != "")
			number = 0;
		return numNames[number] + " CIENTO" + soFar;
	}	//	convertLessThanOneThousand

	/**
	 * 	Convert
	 *	@param number
	 *	@return amt
	 */
	private String convert (int number)
	{
		/* special case */
		if (number == 0)
			return "CERO";
		String prefix = "";
		if (number < 0)
		{
			number = -number;
			prefix = "MENOS";
		}
		String soFar = "";
		int place = 0;
		do
		{
			int n = number % 1000;
			if (n != 0)
			{
				String s = convertLessThanOneThousand (n);
				if (s.startsWith ("UNO CIENTO", 1))
				{
					s = s.replaceFirst ("UNO CIENTO", "CIEN");
				}
				if (s.startsWith ("DOS CIENTOS", 1))
				{
					s = s.replaceFirst ("DOS CIENTOS", "DOSCIENTOS");
				}
				if (s.startsWith ("TRES CIENTOS", 1))
				{
					s = s.replaceFirst ("TRES CIENTOS", "TRESCIENTOS");
				}
				if (s.startsWith ("CUATRO CIENTOS", 1))
				{
					s = s.replaceFirst ("CUATRO CIENTOS", "CUATROCIENTOS");
				}
				if (s.startsWith ("CINCO CIENTOS", 1))
				{
					s = s.replaceFirst ("CINCO CIENTOS", "QUINIENTOS");
				}
				if (s.startsWith ("SEIS CIENTOS", 1))
				{
					s = s.replaceFirst ("SEIS CIENTOS", "SEISCIENTOS");
				}
				if (s.startsWith ("SIETE CIENTOS", 1))
				{
					s = s.replaceFirst ("SIETE CIENTOS", "SETECIENTOS");
				}
				if (s.startsWith ("OCHO CIENTOS", 1))
				{
					s = s.replaceFirst ("OCHO CIENTOS", "OCHOCIENTOS");
				}
				if (s.startsWith ("NUEVE CIENTOS", 1))
				{
					s = s.replaceFirst ("NUEVE CIENTOS", "NOVECIENTOS");
				}
				if ((s.endsWith("UNO")) && (place > 0))
				{
					s = s.replaceFirst ("UNO", "UN");
				}				
				if ((place > 1) && (n > 1))
				{
					soFar = s + majorNames[place] + "ES" + soFar;
				}
				else
				{
					soFar = s + majorNames[place] + soFar;
				}
			}
			place++;
			number /= 1000;
		}
		while (number > 0);
		return (prefix + soFar).trim ();
	}	//	convert



	/**************************************************************************
	 * 	Get Amount in Words
	 * 	@param amount numeric amount (352.80)
	 * 	@return amount in words (three*five*two 80/100)
	 * 	@throws Exception
	 */
	public String getAmtInWords (String amount) throws Exception
	{
		if (amount == null)
			return amount;
		//
		StringBuffer sb = new StringBuffer ();
		int pos = amount.lastIndexOf (symbols.getDecimalSeparator());
		int pos2 = amount.lastIndexOf (symbols.getGroupingSeparator());
		if (pos2 > pos)
			pos = pos2;
		String oldamt = amount;
		amount = amount.replace(symbols.getGroupingSeparator(), ' ');
		amount = amount.replaceAll(" ", "");
		int newpos = amount.lastIndexOf (symbols.getDecimalSeparator());
		if( newpos > -1 ){
			int pesos = Integer.parseInt(amount.substring(0, newpos));
			sb.append(convert(pesos));
			for (int i = 0; i < oldamt.length(); i++) {
				if (pos == i) // we are done
				{
					String cents = oldamt.substring(i + 1);
					sb.append(' ').append(cents).append("/100");
					// .append ("/100 PESOS");
					break;
				}
			}
		} else {
			int pesos = Integer.parseInt(amount);
			sb.append(convert(pesos));
			String cents = "00";
			sb.append(' ').append(cents).append("/100");
		}		
		return sb.toString ();
	}	//	getAmtInWords


	/**
	 * 	Test Print
	 *	@param amt amount
	 */
	private void print (String amt)
	{
		try
		{
			System.out.println(amt + " = " + getAmtInWords(amt));
		}
		catch (Exception e)
		{
			System.err.println( "Error printing \"" + amt + "\"");
			e.printStackTrace();
		}
	}	//	print

	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main(String[] argv){

		Locale locale = new Locale("es");
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		System.out.println(nf.format(11111));

		AmtInWords_ES aiw = new AmtInWords_ES();
		aiw.print ("0,00");		
		aiw.print ("0,23");
		aiw.print ("1,23876787");
		aiw.print ("11,45");
		aiw.print ("121,45");
		aiw.print ("1231,56");
		aiw.print ("10341,78");
		aiw.print ("12341,78");
		aiw.print ("123451,89");
		aiw.print ("12234571,90");
		aiw.print ("123234571,90");
		aiw.print ("123.234.571,90");
		aiw.print ("111.111.111");
		aiw.print ("111.111.111,12");
	}


}	//	AmtInWords_ES
