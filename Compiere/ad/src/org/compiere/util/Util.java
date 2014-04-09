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
package org.compiere.util;

import java.awt.*;
import java.awt.font.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.text.AttributedCharacterIterator.*;
import java.util.*;
import java.util.regex.*;

import javax.swing.*;

/**
 *  General Utilities
 *
 *  @version    $Id: Util.java 8414 2010-02-09 19:42:16Z freyes $
 */
public class Util
{
	/**
	 *	Replace all occurrences of search with replace in original
	 *	@param original the original String
	 *	@param search the string to look for
	 *	@param replace the string we will replace search with
	 *	@return StringBuffer with result
	 */
	public static StringBuffer replace(StringBuffer original, String search, String replace) 
	{
		if (original == null || original.length() == 0 || isEmpty(search))
			return original;
		return doReplace (original, search, replace, false, true);
	}	//	replace
	
	/**
	 *	Replace all occurrences of search with replace in original
	 *	@param original the original String
	 *	@param search the string to look for
	 *	@param replace the string we will replace search with
	 *	@return String with result, null will get replaced by ""
	 */
	public static String replace (String original, String search, String replace) 
	{
		if (isEmpty(original) || isEmpty(search))
			return original;
		return doReplace (new StringBuffer(original), search, replace, false, true).toString();
	}	//	replace
	
	/**
	 *	Replace all occurrences of search with replace in original
	 *	@param original the original String
	 *	@param search the character to look for
	 *	@param replace the string we will replace search with
	 *	@return String with result, null will get replaced by ""
	 */
	public static String replace (String original, char search, String replace) 
	{
		if (isEmpty(original) || search == 0)
			return original;
		return doReplace (new StringBuffer(original), search, replace, true).toString();
	}	//	replace
	
    /**
     *	Replace all or one occurrence of search with replace in original
     *	@param original the original StringBuffer
     *	@param search String to get replaced
     *	@param replace String to replace
     *	@param ignoreCase should we ignore cases
     *	@param alloccurrences should all Occurrences get replaced
     *	@return StringBuffer with result
     */
    public static StringBuffer replace(StringBuffer original, String search, String replace, 
    	boolean ignoreCase, boolean alloccurrences) 
    {
		if (original == null || original.length() == 0 || isEmpty(search))
			return original;
		return doReplace (original, search, replace, ignoreCase, alloccurrences);
    }	//	replace

    /**
     * Replace all or one occurrence of search with replace in original
     * @param original the original StringBuffer
     * @param search String to get replaced
     * @param replace String to replace
     * @param ignoreCase should we ignore cases
     * @param alloccurrences should all occurrences get replaced
     * @return StringBuffer with result
     */
    public static String replace(String original, String search, String replace, 
    	boolean ignoreCase, boolean alloccurrences) 
    {
		if (isEmpty(original) || isEmpty(search))
			return original;
		return replace(new StringBuffer(original), search, replace, ignoreCase, alloccurrences).toString();
    }	//	replace

    /**
     * Replace one occurrence of search with replace in original
     * @param original StringBuffer
     * @param search String to look for
     * @param replace String to replace search with
     * @return new StringBuffer with result
     */
    public static StringBuffer replaceOne(StringBuffer original, String search, String replace) 
    {
		if (original == null || original.length() == 0 || isEmpty(search))
			return original;
        if (original.indexOf(search) >= 0) 
            original.replace(original.indexOf(search), original.indexOf(search)+search.length(), replace);
        return original;
    }	//	replaceOne

    /**
     * Replace one occurrence of search with replace in original
     * @param original String to look in
     * @param search String to search for
     * @param replace String to replace search with
     * @return new String with result
     */
    public static String replaceOne(String original, String search, String replace) 
    {
		if (isEmpty(original) || isEmpty(search))
			return original;
        StringBuffer toriginal = new StringBuffer(original);
        if (toriginal.toString().indexOf(search)>=0) 
            toriginal.replace(toriginal.indexOf(search), toriginal.indexOf(search)+search.length(), replace);
        return toriginal.toString();
    }	//	replaceOne

    /**
     * Run RegEx Expression against original String 
     * @param original StringBuffer with original context
     * @param regex Regular Expression to run as query
     * @param replace Replace String
     * @param CASE_INSENSITIVE whether we should take care of case or not 
     * @return StringBuffer with result
     */
    public static StringBuffer replaceRegex(StringBuffer original, String regex, String replace, 
    	boolean CASE_INSENSITIVE) 
    {
		if (original == null || original.length() == 0 || isEmpty(regex)) 
			return original;
		int flags = 0;
		if (CASE_INSENSITIVE) 
			flags=Pattern.CASE_INSENSITIVE;
		Pattern p = Pattern.compile(regex, flags);
		Matcher m = p.matcher(original);
		StringBuffer newSB = new StringBuffer();
		boolean result = m.find();
		while(result) 
		{
			m.appendReplacement(newSB, replace);
			result = m.find();
		}
		m.appendTail(newSB);
		return newSB;
	}	//	replaceRegex

    /**
     * Run RegEx Expression against original String 
     * @param original StringBuffer with original context
     * @param regex Regular Expression to run as query
     * @param replace Replace String
     * @param CASE_INSENSITIVE whether we should take care of case or not 
     * @return String with result
     */
	public static String replaceRegex(String original, String regex, String replace, boolean CASE_INSENSITIVE) 
	{
		if (isEmpty(original) || isEmpty(regex))
			return original;
		return replaceRegex(new StringBuffer(original), regex, replace, CASE_INSENSITIVE).toString();
	}	//	replaceRegex

	/**
	 * 	Replace
	 *	@param original original
	 *	@param alt old value
	 *	@param neu new value
	 *	@param ignoreCase ignore case
	 *	@param alloccurrences all
     *	@return string buffer with replaced values
	 */
    private static StringBuffer doReplace(StringBuffer original, String alt, String neu, 
    	boolean ignoreCase, boolean alloccurrences) 
    {
		if (original == null || original.length() == 0) 
			return original;
		int position = -1;
		if (alt == null) 
			alt = "";
		if (neu == null)
			neu = "";
		int altNeuDiff = neu.length() - alt.length();
		if (neu.lastIndexOf(alt) >= altNeuDiff) 
			altNeuDiff = neu.lastIndexOf(alt)+1;
		if (ignoreCase) 
		{
			position = original.toString().toLowerCase().indexOf(alt.toLowerCase());
			while (position>=0) 
			{
				original.replace(position,position + alt.length(), neu);
				position = original.toString().toLowerCase().indexOf(alt.toLowerCase(),position+altNeuDiff);
			}
		} 
		else 
		{
			position = original.toString().indexOf(alt);
			while (position>=0) 
			{
				original.replace(position,position + alt.length(),neu);
				position = original.toString().indexOf(alt,position+altNeuDiff);
			}
		}
        return original;
    }	//	doReplace
	
    /**
     * 	Replace 
     *	@param original original
     *	@param alt old value
     *	@param neu new value
     *	@param alloccurrences all occurrences
     *	@return string buffer with replaced values
     */
	private static StringBuffer doReplace(StringBuffer original, char alt, String neu, boolean alloccurrences) 
	{
		if (original == null || original.length() == 0) 
			return original;
		int position = -1;
		int altNeuDiff = neu.length()-1;
		if (neu.lastIndexOf(alt)>=altNeuDiff) 
			altNeuDiff = neu.lastIndexOf(alt)+1;
		position = original.toString().indexOf(alt);
		while (position>=0) 
		{
			original.replace(position,position + 1,neu);
			position = original.toString().indexOf(alt,position+altNeuDiff);
		}
		return original;
	}	//	doReplace

	/**
	 * This function will split a string based on a split character.
	 * @param searchIn The string to split
	 * @param splitter The separator
	 * @return String array of split values
	 */
	public static String[] split(String searchIn, String splitter) 
	{
		String[] results = new String[count(searchIn,splitter)+1];
		int position = 0;
		int i = 0;
		while (searchIn.indexOf(splitter,position)>=0) 
		{
			results[i]=searchIn.substring(position,searchIn.indexOf(splitter,position+2));
			position = searchIn.indexOf(splitter,position)+1;
			i++;
		}
		results[(results.length-1)]=searchIn.substring(position);
		return results;
	}	//	split
	
	/**
	 * Remove String toBeRemoved from original
	 * @param original String to look in
	 * @param toBeRemoved String to get removed
	 * @param ignoreCase should we take care of case
	 * @return String without toBeRemoved
	 */
	public static String remove(String original, String toBeRemoved, boolean ignoreCase) 
	{
		if (!isEmpty(toBeRemoved)) 
			return replace(original, toBeRemoved, "", ignoreCase, true);
		return original;
	}	//	remove

	/**
	 * To split for indexes we will look for the next Word in tempstr
	 * @param tempStr to look into
	 * @return nextWord in String
	 */
	public static String getNextWord(String tempStr) 
	{
		String word = "";
		if (tempStr.indexOf(" ") >= 0) 
		{
			word=tempStr.substring(0,tempStr.indexOf(" "));
		} 
		else 
		{
			word = tempStr;
		}
		return word;
	}	//	getNextWord

	/**
	 * For some save scenarios and analysis we should remove special characters, i.e. HTML
	 * @param tempStr to remove Special Char
	 * @return new String without special chars
	 */
	public static String removeSpecialChar(String tempStr) 
	{
		if (tempStr!=null) 
		{
			tempStr=replace(tempStr,",", "", true, true);
			tempStr=replace(tempStr,".", "", true, true);
			tempStr=replace(tempStr,"!", "", true, true);
			tempStr=replace(tempStr,"?", "", true, true);
			tempStr=replace(tempStr,"'", "", true, true);
			tempStr=replace(tempStr,":", "", true, true);
			tempStr=replace(tempStr,"(", "", true, true);
			tempStr=replace(tempStr,")", "", true, true);
			tempStr=replace(tempStr,"+", "", true, true);
			tempStr=replace(tempStr,"-", "", true, true);
			tempStr=replace(tempStr,">", "", true, true);
			tempStr=replace(tempStr,"<", "", true, true);
			tempStr=replace(tempStr,"/", "", true, true);
			while (tempStr.indexOf("  ") > 0) 
			{
				tempStr = replace(tempStr, "  "," ", true, true);
			}
			tempStr = replace(tempStr,"\t", "", true, true);
		}
		return tempStr;
	}	//	removeSpecialChar
	
	/**
	 * Should return you the number of occurrences of "find" in orig
	 * @param orig The String to look in
	 * @param find The String to look for
	 * @return Number of occurrences, 0 if none
	 */
	public static int count(String orig, String find) 
	{
		int retVal = 0;
		int pos = 0;
		while (orig.indexOf(find,pos) > 0) 
		{
			pos = orig.indexOf(find,pos) + 1;
			retVal++;
		}
		return retVal;
	}	//	count
	
	/**
	 * 	Count Words
	 *	@param str string
	 *	@return number of words
	 */
	public static int countWords(String str)
	{
		if (str == null || str.length() == 0)
			return 0;
		int words = 0;
		boolean lastWasWhiteSpace = false;
		char[] chars = str.trim().toCharArray();
		for (char c : chars) {
	        if (Character.isWhitespace(c))
	        {
	        	if (!lastWasWhiteSpace)
	        		words++;
	        	lastWasWhiteSpace = true;
	        }
	        else
	        	lastWasWhiteSpace = false;
        }
		return words + 1;
	}	//	countWords
	
	/**
	 * Generate CRC String for tempStr
	 * @param tempStr
	 * @return CRC Code for tempStr
	 * @throws IOException
	 */
	public static String crc(String tempStr) throws IOException 
	{
		java.util.zip.Adler32 inChecker = new java.util.zip.Adler32();
		java.util.zip.CheckedInputStream in = null;
		in = new java.util.zip.CheckedInputStream(new java.io.ByteArrayInputStream(tempStr.getBytes()), inChecker);
		while ((in.read()) != -1) 
		{
		}
		String myCheckSum = "" + inChecker.getValue();
		return myCheckSum;
	}	//	crc

	/**
	 * 	Remove CR / LF from String
	 * 	@param in input
	 * 	@return cleaned string
	 */
	public static String removeCRLF (String in)
	{
		char[] inArray = in.toCharArray();
		StringBuffer out = new StringBuffer (inArray.length);
		for (char c : inArray) {
			if (c == '\n' || c == '\r')
				;
			else
				out.append(c);
		}
		return out.toString();
	}	//	removeCRLF

	
	/**
	 * 	Clean - Remove all white spaces
	 *	@param in in
	 *	@return cleaned string
	 */
	public static String cleanWhitespace (String in)
	{
		if (in == null)
			return null;
		char[] inArray = in.toCharArray();
		StringBuffer out = new StringBuffer(inArray.length);
		boolean lastWasSpace = false;
		for (char c : inArray) {
			if (Character.isWhitespace(c))
			{
				if (!lastWasSpace)
					out.append (' ');
				lastWasSpace = true;
			}
			else
			{
				out.append (c);
				lastWasSpace = false;
			}
		}
		return out.toString();
	}	//	cleanWhitespace


	/**
	 * 	Mask HTML content.
	 *  i.e. replace characters with &values;
	 *  CR is not masked
	 * 	@param content content
	 * 	@return masked content
	 */
	public static String maskHTML (String content)
	{
		return maskHTML (content, false);
	}	//	maskHTML
	
	/**
	 * 	Mask HTML content.
	 *  i.e. replace characters with &values;
	 * 	@param content content
	 * 	@param maskCR convert CR into <br>
	 * 	@return masked content
	 */
	public static String maskHTML (String content, boolean maskCR)
	{
		if (content == null || content.length() == 0 || content.equals(" "))
			return "&nbsp";
		//
		StringBuffer out = new StringBuffer();
		char[] chars = content.toCharArray();
		for (char c : chars) {
			switch (c)
			{
				case '<':
					out.append ("&lt;");
					break;
				case '>':
					out.append ("&gt;");
					break;
				case '&':
					out.append ("&amp;");
					break;
				case '"':
					out.append ("&quot;");
					break;
				case '\'':
					out.append ("&#039;");
					break;
				case '\n':
					if (maskCR)
						out.append ("<br>");
					out.append(c);
					break;
					
				//
				default:
					int ii =  c;
					if (ii > 255)		//	Write Unicode
						out.append("&#").append(ii).append(";");
					else
						out.append(c);
					break;
			}
		}
		return out.toString();
	}	//	maskHTML

	/**
	 * 	Get the number of occurances of countChar in string.
	 * 	@param string String to be searched
	 * 	@param countChar to be counted character
	 * 	@return number of occurances
	 */
	public static int getCount (String string, char countChar)
	{
		if (string == null || string.length() == 0)
			return 0;
		int counter = 0;
		char[] array = string.toCharArray();
		for (char element : array) {
			if (element == countChar)
				counter++;
		}
		return counter;
	}	//	getCount

	/**
	 * 	Is String Empty
	 *	@param str string
	 *	@return true if >= 1 char
	 */
	public static boolean isEmpty (String str)
	{
		return (str == null || str.length() == 0);
	}	//	isEmpty
	
	/**
	 * 	Is String Alpha Numeric
	 *	@param str string
	 *	@return false if other than A..Za..z0..9 - true also if empty
	 */
	public static boolean isAlphaNumeric (String str)
	{
		boolean isAlphaNumeric = true;
	
		if(str == null || str.length() == 0)
			return false;
		
		final char[] chars = str.toCharArray();
		for (char c : chars) {
			if (!Character.isLetterOrDigit(c)) 
			{
				isAlphaNumeric = false;
				break;
			}
		}
		
		return isAlphaNumeric;
	}	//	isAlphaNumeric
	
	/**
	 * 	Is String Numeric
	 *	@param str string
	 *	@return false if other than 0..9+-.,() or empty
	 */
	public static boolean isNumeric (String str)
	{
		if (str == null || str.length() == 0)
			return false;
		
		final char[] chars = str.toCharArray();
		for (char c : chars) {
			if (Character.isDigit(c)
				|| c == '(' || c == ')'
				|| c == '+' || c == '-'
				|| c == '.' || c == ',')
				;
			else
				return false;
		}
		return true;
	}	//	isNumeric


	/**
	 * 	Get Backup Name from time
	 *	@return yyyy_mm_dd_hh_mm_ss
	 */
	public static String getBackupName()
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String nowString = now.toString();		//	yyyy-mm-dd hh:mm:ss.fffffffff
		nowString = nowString.substring(0,19);	//	yyyy-mm-dd hh:mm:ss
		nowString = replace(nowString, "-", "_");
		nowString = replace(nowString, " ", "_");
		nowString = replace(nowString, ":", "_");
		return nowString;
	}	//	getBackupName

	/**
	 * 	Create Backup of file
	 *	@param file original
	 *	@return backup file or null if no success
	 */
	public static File createBackup(File file)
	{
		if (!file.exists())
			return null;
		String backupName = file.getAbsolutePath() + getBackupName();
		File backup = new File(backupName);
		try
		{
			FileOutputStream fo = new FileOutputStream(backup);
			FileInputStream fi = new FileInputStream(file);
			boolean eof = false;		//	could be more efficient with buffered ...
			while (!eof) 
			{
				int c = fi.read();
				if( c == -1)
					eof = true;
				else
					fo.write(c);
			}
			fi.close();
			fo.flush();
			fo.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		if (file.length() == backup.length())
			return backup;
		return null;	//	not the same length
	}	//	createBackup

	/**************************************************************************
	 *  Find index of search character in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search search character
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, char search)
	{
		return findIndexOf(str, search, search);
	}   //  findIndexOf

	/**
	 *  Find index of search characters in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search1 first search character
	 *  @param search2 second search character (or)
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, char search1, char search2)
	{
		if (str == null)
			return -1;
		//
		int endIndex = -1;
		int parCount = 0;
		boolean ignoringText = false;
		int size = str.length();
		while (++endIndex < size)
		{
			char c = str.charAt(endIndex);
			if (c == '\'')
				ignoringText = !ignoringText;
			else if (!ignoringText)
			{
				if (parCount == 0 && (c == search1 || c == search2))
					return endIndex;
				else if (c == ')')
						parCount--;
				else if (c == '(')
					parCount++;
			}
		}
		return -1;
	}   //  findIndexOf

	/**
	 *  Find index of search character in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search search character
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, String search)
	{
		if (str == null || search == null || search.length() == 0)
			return -1;
		//
		int endIndex = -1;
		int parCount = 0;
		boolean ignoringText = false;
		int size = str.length();
		while (++endIndex < size)
		{
			char c = str.charAt(endIndex);
			if (c == '\'')
				ignoringText = !ignoringText;
			else if (!ignoringText)
			{
				if (parCount == 0 && c == search.charAt(0))
				{
					if (str.substring(endIndex).startsWith(search))
						return endIndex;
				}
				else if (c == ')')
						parCount--;
				else if (c == '(')
					parCount++;
			}
		}
		return -1;
	}   //  findIndexOf

	
	/**************************************************************************
	 *  Return Hex String representation of byte b
	 *  @param b byte
	 *  @return Hex
	 */
	static public String toHex (byte b)
	{
		char hexDigit[] = {
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);
	}

	/**
	 *  Return Hex String representation of char c
	 *  @param c character
	 *  @return Hex
	 */
	static public String toHex (char c)
	{
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return toHex(hi) + toHex(lo);
	}   //  toHex

	/**
	 * Convert the binary to hex string 
	 * @param bytes byte array
	 * @return hex string
	 */
	static public String toHexString(byte[] bytes)
	{
		if (bytes == null || bytes.length <= 0)
			return null;
		
		StringBuffer retString = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; ++i)
		{
			retString.append(
				Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		return retString.toString();
	}  // toHexString()
	
	/**
	 * Convert the hex string to byte array
	 * @param s hex string
	 * @return byte array 
	 */
	static public byte[] toByteArray(String s)
	{
		if (s == null || s.length() <= 0)
			return null;
		
		byte[] bytes = new byte[s.length() / 2];
		int j = 0;
		for (int i = 0; i < s.length(); i += 2)
		{
			int ii = Integer.parseInt(s.substring(i, i + 2), 16);
			bytes[j++] = (byte)(ii & 0xFF);
		}
		return bytes;
	}  // toByteArray()
	
	/**************************************************************************
	 * 	Init Cap Words With Spaces
	 * 	@param in string
	 * 	@return init cap
	 */
	public static String initCap (String in)
	{
		if (in == null || in.length() == 0)
			return in;
		//
		boolean capitalize = true;
		char[] data = in.toCharArray();
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == ' ' || Character.isWhitespace(data[i]))
				capitalize = true;
			else if (capitalize)
			{
				data[i] = Character.toUpperCase (data[i]);
				capitalize = false;
			}
			else
				data[i] = Character.toLowerCase (data[i]);
		}
		return new String (data);
	}	//	initCap

	
	/**************************************************************************
	 * 	Return a Iterator with only the relevant attributes.
	 *  Fixes implementation in AttributedString, which returns everything
	 * 	@param aString attributed string
	 * 	@param relevantAttributes relevant attributes
	 * 	@return iterator
	 */
	static public AttributedCharacterIterator getIterator (AttributedString aString, 
		AttributedCharacterIterator.Attribute[] relevantAttributes)
	{
		AttributedCharacterIterator iter = aString.getIterator();
		Set<AttributedCharacterIterator.Attribute> set = iter.getAllAttributeKeys();
	//	System.out.println("AllAttributeKeys=" + set);
		if (set.size() == 0)
			return iter;
		//	Check, if there are unwanted attributes
		Set<AttributedCharacterIterator.Attribute> unwanted = new HashSet<AttributedCharacterIterator.Attribute>(iter.getAllAttributeKeys());
		for (Attribute element : relevantAttributes)
			unwanted.remove(element);
		if (unwanted.size() == 0)
			return iter;

		//	Create new String
		StringBuffer sb = new StringBuffer();
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next())
			sb.append(c);
		aString = new AttributedString(sb.toString());

		//	copy relevant attributes
		Iterator<AttributedCharacterIterator.Attribute> it = iter.getAllAttributeKeys().iterator();
		while (it.hasNext())
		{
			AttributedCharacterIterator.Attribute att = it.next();
			if (!unwanted.contains(att))
			{
				for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next())
				{
					Object value = iter.getAttribute(att);
					if (value != null)
					{
						int start = iter.getRunStart(att);
						int limit = iter.getRunLimit(att);
					//	System.out.println("Attribute=" + att + " Value=" + value + " Start=" + start + " Limit=" + limit);
						aString.addAttribute(att, value, start, limit);
						iter.setIndex(limit);
					}
				}
			}
		//	else
		//		System.out.println("Unwanted: " + att);
		}
		return aString.getIterator();
	}	//	getIterator


	/**
	 * 	Dump a Map (key=value) to out
	 * 	@param map Map
	 */
	static public void dump (Map<?,?> map)
	{
		System.out.println("Dump Map - size=" + map.size());
		Iterator<?> it = map.keySet().iterator();
		while (it.hasNext())
		{
			Object key = it.next();
			Object value = map.get(key);
			System.out.println(key + "=" + value);
		}
	}	//	dump (Map)

	/**
	 *  Print Action and Input Map for component
	 * 	@param comp  Component with ActionMap
	 */
	public static void printActionInputMap (JComponent comp)
	{
		//	Action Map
		ActionMap am = comp.getActionMap();
		Object[] amKeys = am.allKeys(); //  including Parents
		if (amKeys != null)
		{
			System.out.println("-------------------------");
			System.out.println("ActionMap for Component " + comp.toString());
			for (Object element : amKeys) {
				Action a = am.get(element);

				StringBuffer sb = new StringBuffer("- ");
				sb.append(a.getValue(Action.NAME));
				if (a.getValue(Action.ACTION_COMMAND_KEY) != null)
					sb.append(", Cmd=").append(a.getValue(Action.ACTION_COMMAND_KEY));
				if (a.getValue(Action.SHORT_DESCRIPTION) != null)
					sb.append(" - ").append(a.getValue(Action.SHORT_DESCRIPTION));
				System.out.println(sb.toString() + " - " + a);
			}
		}
		/**	Same as below
		KeyStroke[] kStrokes = comp.getRegisteredKeyStrokes();
		if (kStrokes != null)
		{
		System.out.println("-------------------------");
			System.out.println("Registered Key Strokes - " + comp.toString());
			for (int i = 0; i < kStrokes.length; i++)
			{
				System.out.println("- " + kStrokes[i].toString());
			}
		}
		/** Focused				*/
		InputMap im = comp.getInputMap(JComponent.WHEN_FOCUSED);
		KeyStroke[] kStrokes = im.allKeys();
		if (kStrokes != null)
		{
			System.out.println("-------------------------");
			System.out.println("InputMap for Component When Focused - " + comp.toString());
			for (KeyStroke element : kStrokes) {
				System.out.println("- " + element.toString() + " - "
					+ im.get(element).toString());
			}
		}
		/** Focused in Window	*/
		im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		kStrokes = im.allKeys();
		if (kStrokes != null)
		{
			System.out.println("-------------------------");
			System.out.println("InputMap for Component When Focused in Window - " + comp.toString());
			for (KeyStroke element : kStrokes) {
				System.out.println("- " + element.toString() + " - "
					+ im.get(element).toString());
			}
		}
		/** Focused when Ancester	*/
		im = comp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		kStrokes = im.allKeys();
		if (kStrokes != null)
		{
			System.out.println("-------------------------");
			System.out.println("InputMap for Component When Ancestor - " + comp.toString());
			for (KeyStroke element : kStrokes) {
				System.out.println("- " + element.toString() + " - "
					+ im.get(element).toString());
			}
		}
		System.out.println("-------------------------");
	}   //  printActionInputMap

	/**
	 * 	Is 8 Bit
	 *	@param str string
	 *	@return true if string contains chars <= 255
	 */
	public static boolean is8Bit (String str)
	{
		if (str == null || str.length() == 0)
			return true;
		char[] cc = str.toCharArray();
		for (char element : cc) {
			if (element > 255)
			{
			//	System.out.println("Not 8 Bit - " + str);
				return false;
			}
		}
		return true;
	}	//	is8Bit
	
	/**
	 * 	Is 7 Bit
	 *	@param str string
	 *	@return true if string contains chars <= 127
	 */
	public static boolean is7Bit (String str)
	{
		if (str == null || str.length() == 0)
			return true;
		char[] cc = str.toCharArray();
		for (char element : cc) {
			if (element > 127)
				return false;
		}
		return true;
	}	//	is8Bit

	/**
	 * 	Clean mnemonic Ampersand (used to indicate shortcut) 
	 *	@param in input
	 *	@return cleaned string
	 */
	public static String cleanMnemonic (String in)
	{
		if (in == null || in.length() == 0)
			return in;
		int pos = in.indexOf('&');
		if (pos == -1)
			return in;
		//	Single & - '&&' or '& ' -> &
		if (pos+1 < in.length() && in.charAt(pos+1) != ' ')
			in = in.substring(0, pos) + in.substring(pos+1);
		return in;
	}	//	cleanMnemonic
	
	/**
	 * 	Trim to max character length
	 *	@param str string
	 *	@param length max (incl) character length
	 *	@return string
	 */
	public static String trimLength (String str, int length)
	{
		if (str == null)
			return str;
		if (length <= 0)
			throw new IllegalArgumentException("Trim length invalid: " + length);
		if (str.length() > length) 
			return str.substring(0, length);
		return str;
	}	//	trimLength
	
	/**
	 * 	Size of String in bytes
	 *	@param str string
	 *	@return size in bytes
	 */
	public static int size (String str)
	{
		if (str == null)
			return 0;
		int length = str.length();
		int size = length;
		try
		{
			size = str.getBytes("UTF-8").length;
		}
		catch (UnsupportedEncodingException e)
		{
			e.getStackTrace();
		}
		return size;
	}	//	size

	/**
	 * 	Trim to max byte size
	 *	@param str string
	 *	@param size max size in bytes
	 *	@return string
	 */
	public static String trimSize (String str, int size)
	{
		if (str == null)
			return str;
		if (size <= 0)
			throw new IllegalArgumentException("Trim size invalid: " + size);
		//	Assume two byte code
		int length = str.length();
		if (length < size/2)
			return str;
		try
		{
			byte[] bytes = str.getBytes("UTF-8");
			if (bytes.length <= size)
				return str;
			//	create new - may cut last character in half
			byte[] result = new byte[size];
			System.arraycopy(bytes, 0, result, 0, size);
			return new String(result, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.getStackTrace();
		}
		return str;
	}	//	trimSize


	/**
	 * 	Is Equal.
	 *  (including null compare, binary array)
	 *	@param o1 one
	 *	@param o2 two
	 *	@return true if one == two
	 */
	public static boolean isEqual (Object o1, Object o2)
	{
		if (o1 == null && o2 == null)
			return true;
		if (o1 == null && o2 != null)
			return false;
		if (o1 != null && o2 == null)
			return false;
		//
		if (o1 instanceof byte[] && o2 instanceof byte[])
		{
			byte[] b1 = (byte[])o1;
			byte[] b2 = (byte[])o2;
			return Arrays.equals(b1, b2);
		}
		return o1.equals(o2);
	}	//	isEqual
	
	/**************************************************************************
	 * 	Test
	 * 	@param args args
	 */
	public static void main (String[] args)
	{
		System.out.println(countWords(" a test  sentence ") );
		System.out.println(countWords(" a ") );
		System.out.println(countWords("a") );
		System.out.println(countWords("a a") );
		
		System.exit(0);
		
		String str = "a�b�c?d?e?f?g?";
		System.out.println(str + " = " + str.length() + " - " + size(str));
		String str1 = trimLength(str, 10);
		System.out.println(str1 + " = " + str1.length() + " - " + size(str1));
		String str2 = trimSize(str, 10);
		System.out.println(str2 + " = " + str2.length() + " - " + size(str2));
		//
		AttributedString aString = new AttributedString ("test test");
		aString.addAttribute(TextAttribute.FOREGROUND, Color.blue);
		aString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 2, 4);
		getIterator (aString, new AttributedCharacterIterator.Attribute[] {TextAttribute.UNDERLINE});
	}	//	main

}   //  Util
