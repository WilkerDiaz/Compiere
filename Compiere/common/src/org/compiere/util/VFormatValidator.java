package org.compiere.util;

import java.util.*;

public abstract class VFormatValidator
{
	/** HahMap of Functions	*/
	public static final HashMap< Character, VFormatValidator > s_formatChars = new HashMap<Character, VFormatValidator>();

	static {
		//		c	any Letter or Digits or space
		s_formatChars.put('c', 	new VFormatValidator('c'){
			@Override
			public char getConvertedValue(char c) { return c; }
			@Override
			public char getDefault() { return ' '; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c) || Character.isDigit(c) || Character.isSpace(c); }
		});

		//		C	any Letter or Digits or space converted to upper case
		s_formatChars.put('C', 	new VFormatValidator('C'){
			@Override
			public char getConvertedValue(char c) { return Character.toUpperCase( c ); }
			@Override
			public char getDefault() { return ' '; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c) || Character.isDigit(c) || Character.isSpace(c); }
		});

		//		a	any Letter or Digits NO space
		s_formatChars.put('a', 	new VFormatValidator('a'){
			@Override
			public char getConvertedValue(char c) { return c; }
			@Override
			public char getDefault() { return '0'; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c) || Character.isDigit(c); }
		});

		//		A	any Letter or Digits NO space converted to upper case
		s_formatChars.put('A', 	new VFormatValidator('A'){
			@Override
			public char getConvertedValue(char c) { return Character.toUpperCase( c ); }
			@Override
			public char getDefault() { return '0'; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c) || Character.isDigit(c); }
		});

		//		l	any Letter a..Z NO space
		s_formatChars.put('l', 	new VFormatValidator('l'){
			@Override
			public char getConvertedValue(char c) { return c; }
			@Override
			public char getDefault() { return 'z'; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c); }
		});

		//		L	any Letter a..Z NO space converted to upper case
		s_formatChars.put('L', 	new VFormatValidator('L'){
			@Override
			public char getConvertedValue(char c) { return Character.toUpperCase( c ); }
			@Override
			public char getDefault() { return 'Z'; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c); }
		});

		//	o	any Letter a..Z or space
		s_formatChars.put('o', 	new VFormatValidator('o'){
			@Override
			public char getConvertedValue(char c) { return c; }
			@Override
			public char getDefault() { return ' '; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c) || Character.isSpace(c); }
		});

		//	O	any Letter a..Z or space converted to upper case
		s_formatChars.put('O', 	new VFormatValidator('O'){
			@Override
			public char getConvertedValue(char c) { return Character.toUpperCase( c ); }
			@Override
			public char getDefault() { return ' '; }
			@Override
			public boolean isValid(char c) { return Character.isLetter(c) || Character.isSpace(c); }
		});

		//	9	Digits 0..9 or space
		s_formatChars.put('9', 	new VFormatValidator('9'){
			@Override
			public char getConvertedValue(char c) { return c; }
			@Override
			public char getDefault() { return ' '; }
			@Override
			public boolean isValid(char c) { return Character.isDigit(c) || Character.isSpace(c); }
		});

		//	0	Digits 0..9 NO space
		s_formatChars.put('0', 	new VFormatValidator('0'){
			@Override
			public char getConvertedValue(char c) { return c; }
			@Override
			public char getDefault() { return '0'; }
			@Override
			public boolean isValid(char c) { return Character.isDigit(c); }
		});

		//	SPACE	any character
		s_formatChars.put(' ', 	new VFormatValidator(' '){
			@Override
			public char getConvertedValue(char c) { return c; }
			@Override
			public char getDefault() { return ' '; }
			@Override
			public boolean isValid(char c) { return true; }
		});
		//

	}

	public VFormatValidator(char c)
	{
		m_c = c;
	}

	private final char m_c;

	public abstract char getConvertedValue(char c);
	public abstract char getDefault();
	public abstract boolean isValid(char c);

	@Override
	public String toString()
	{
	    return "VFormatValidator[" + m_c + "]";
	}

}
