package com.alec.TwitterAPI;

/*
 * Static String formatting and query routines.
 * Copyright (C) 2001-2005 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for String formatting, manipulation, and queries.
 * More information about this class is available from <a target="_top" href=
 * "http://ostermiller.org/utils/StringHelper.html">ostermiller.org</a>.
 *
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.00.00
 */
public class StringHelper {
	/**
	 * Replaces characters that may be confused by an SQL
	 * parser with their equivalent escape characters.
	 * <p>
	 * Any data that will be put in an SQL query should be be escaped. This is especially important for data that comes from untrusted sources such as
	 * Internet users.
	 * <p>
	 * For example if you had the following SQL query:<br>
	 * <code>"SELECT * FROM addresses WHERE name='" + name + "' AND private='N'"</code><br>
	 * Without this function a user could give <code>" OR 1=1 OR ''='"</code> as their name causing the query to be:<br>
	 * <code>"SELECT * FROM addresses WHERE name='' OR 1=1 OR ''='' AND private='N'"</code><br>
	 * which will give all addresses, including private ones.<br>
	 * Correct usage would be:<br>
	 * <code>"SELECT * FROM addresses WHERE name='" + StringHelper.escapeSQL(name) + "' AND private='N'"</code><br>
	 * <p>
	 * Another way to avoid this problem is to use a PreparedStatement with appropriate placeholders.
	 *
	 * @param s
	 *            String to be escaped
	 * @return escaped String
	 * @throws NullPointerException
	 *             if s is null.
	 *
	 * @since ostermillerutils 1.00.00
	 */
	public static String escapeSQL(String s) {
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\\':
				case '\"':
				case '\'':
				case '\0': {
					newLength += 1;
				}
					break;
			}
		}
		if (length == newLength) {
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\\': {
					sb.append("\\\\");
				}
					break;
				case '\"': {
					sb.append("\\\"");
				}
					break;
				case '\'': {
					sb.append("\'\'");
				}
					break;
				case '\0': {
					sb.append("\\0");
				}
					break;
				default: {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	public static String escapeHtml(String aText) {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			}
			else if (character == '>') {
				result.append("&gt;");
			}
			else if (character == '&') {
				result.append("&amp;");
			}
			else if (character == '\"') {
				result.append("&quot;");
			}
			else if (character == '\t') {
				addCharEntity(9, result);
			}
			else if (character == '!') {
				addCharEntity(33, result);
			}
			else if (character == '#') {
				addCharEntity(35, result);
			}
			else if (character == '$') {
				addCharEntity(36, result);
			}
			else if (character == '%') {
				addCharEntity(37, result);
			}
			else if (character == '\'') {
				addCharEntity(39, result);
			}
			else if (character == '(') {
				addCharEntity(40, result);
			}
			else if (character == ')') {
				addCharEntity(41, result);
			}
			else if (character == '*') {
				addCharEntity(42, result);
			}
			else if (character == '+') {
				addCharEntity(43, result);
			}
			else if (character == ',') {
				addCharEntity(44, result);
			}
			else if (character == '-') {
				addCharEntity(45, result);
			}
			else if (character == '.') {
				addCharEntity(46, result);
			}
			else if (character == '/') {
				addCharEntity(47, result);
			}
			else if (character == ':') {
				addCharEntity(58, result);
			}
			else if (character == ';') {
				addCharEntity(59, result);
			}
			else if (character == '=') {
				addCharEntity(61, result);
			}
			else if (character == '?') {
				addCharEntity(63, result);
			}
			else if (character == '@') {
				addCharEntity(64, result);
			}
			else if (character == '[') {
				addCharEntity(91, result);
			}
			else if (character == '\\') {
				addCharEntity(92, result);
			}
			else if (character == ']') {
				addCharEntity(93, result);
			}
			else if (character == '^') {
				addCharEntity(94, result);
			}
			else if (character == '_') {
				addCharEntity(95, result);
			}
			else if (character == '`') {
				addCharEntity(96, result);
			}
			else if (character == '{') {
				addCharEntity(123, result);
			}
			else if (character == '|') {
				addCharEntity(124, result);
			}
			else if (character == '}') {
				addCharEntity(125, result);
			}
			else if (character == '~') {
				addCharEntity(126, result);
			}
			else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public static String escapeJson(String aText) {
		final StringBuilder result = new StringBuilder();
		StringCharacterIterator iterator = new StringCharacterIterator(aText);
		char character = iterator.current();
		while (character != StringCharacterIterator.DONE) {
			if (character == '\"') {
				result.append("\\\"");
			}
			else if (character == '\\') {
				result.append("\\\\");
			}
			else if (character == '/') {
				result.append("\\/");
			}
			else if (character == '\b') {
				result.append("\\b");
			}
			else if (character == '\f') {
				result.append("\\f");
			}
			else if (character == '\n') {
				result.append("\\n");
			}
			else if (character == '\r') {
				result.append("\\r");
			}
			else if (character == '\t') {
				result.append("\\t");
			}
			else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public static String escapeForRegex(String aRegexFragment) {
		final StringBuilder result = new StringBuilder();

		final StringCharacterIterator iterator =
				new StringCharacterIterator(aRegexFragment);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			/* All literals need to have backslashes doubled. */
			if (character == '.') {
				result.append("\\.");
			}
			else if (character == '\\') {
				result.append("\\\\");
			}
			else if (character == '?') {
				result.append("\\?");
			}
			else if (character == '*') {
				result.append("\\*");
			}
			else if (character == '+') {
				result.append("\\+");
			}
			else if (character == '&') {
				result.append("\\&");
			}
			else if (character == ':') {
				result.append("\\:");
			}
			else if (character == '{') {
				result.append("\\{");
			}
			else if (character == '}') {
				result.append("\\}");
			}
			else if (character == '[') {
				result.append("\\[");
			}
			else if (character == ']') {
				result.append("\\]");
			}
			else if (character == '(') {
				result.append("\\(");
			}
			else if (character == ')') {
				result.append("\\)");
			}
			else if (character == '^') {
				result.append("\\^");
			}
			else if (character == '$') {
				result.append("\\$");
			}
			else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	private String removeUrl(String commentstr)
	{
		String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(commentstr);
		int i = 0;
		while (m.find()) {
			commentstr = commentstr.replaceAll(m.group(i), "").trim();
			i++;
		}
		return commentstr;
	}

	private static void addCharEntity(Integer aIdx, StringBuilder aBuilder) {
		String padding = "";
		if (aIdx <= 9) {
			padding = "00";
		}
		else if (aIdx <= 99) {
			padding = "0";
		}
		else {
			// no prefix
		}
		String number = padding + aIdx.toString();
		aBuilder.append("&#" + number + ";");
	}
}
