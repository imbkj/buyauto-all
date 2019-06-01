package com.buyauto.util.method;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.util.StringUtils;

/**
 * 
 * @ClassName: CustomDateEditor
 * @Description: 日期类型转换器
 * @author cxz
 * @date 2016年5月18日 下午4:32:09
 *
 */
public class CustomDateEditor extends PropertyEditorSupport {

	private final DateFormat[] dateFormat = { 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
			new SimpleDateFormat("yyyy-MM-dd"), 
			new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK),
			new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
			};

	private final boolean allowEmpty;

	private final int exactDateLength;

	/**
	 * Create a new CustomDateEditor instance, using the given DateFormat for
	 * parsing and rendering.
	 * <p>
	 * The "allowEmpty" parameter states if an empty String should be allowed
	 * for parsing, i.e. get interpreted as null value. Otherwise, an
	 * IllegalArgumentException gets thrown in that case.
	 * 
	 * @param dateFormat
	 *            DateFormat to use for parsing and rendering
	 * @param allowEmpty
	 *            if empty strings should be allowed
	 */
	public CustomDateEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
		this.exactDateLength = -1;
	}

	/**
	 * Create a new CustomDateEditor instance, using the given DateFormat for
	 * parsing and rendering.
	 * <p>
	 * The "allowEmpty" parameter states if an empty String should be allowed
	 * for parsing, i.e. get interpreted as null value. Otherwise, an
	 * IllegalArgumentException gets thrown in that case.
	 * <p>
	 * The "exactDateLength" parameter states that IllegalArgumentException gets
	 * thrown if the String does not exactly match the length specified. This is
	 * useful because SimpleDateFormat does not enforce strict parsing of the
	 * year part, not even with {@code setLenient(false)}. Without an
	 * "exactDateLength" specified, the "01/01/05" would get parsed to
	 * "01/01/0005". However, even with an "exactDateLength" specified,
	 * prepended zeros in the day or month part may still allow for a shorter
	 * year part, so consider this as just one more assertion that gets you
	 * closer to the intended date format.
	 * 
	 * @param dateFormat
	 *            DateFormat to use for parsing and rendering
	 * @param allowEmpty
	 *            if empty strings should be allowed
	 * @param exactDateLength
	 *            the exact expected length of the date String
	 */
	public CustomDateEditor(boolean allowEmpty, int exactDateLength) {
		this.allowEmpty = allowEmpty;
		this.exactDateLength = exactDateLength;
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
			throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength
					+ "characters long");
		} else {
			Date parse = null;
			for (int i = 0; i < dateFormat.length; i++) {
				try {
					parse = this.dateFormat[i].parse(text);
				} catch (ParseException ex) {
					continue;
				}
				break;
			}
			if (parse != null) {
				setValue(parse);
			} else {
				throw new IllegalArgumentException("Could not parse date: " + text);
			}
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return value.getTime() + "";
	}

}
