package com.novadart.novabill.report;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.TransportDocument;

public class ReportUtils {
	
	public static final int FILENAME_MAX_LENGTH = 120;

	private static final BigDecimal HUNDRED_PERCENT = new BigDecimal(100);
	
	public static String removeTrailingZeros(BigDecimal decimal, Locale locale){
		DecimalFormat format = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		format.applyPattern("0.#");
		return format.format(decimal.doubleValue());
	}
	
	public static List<TransportDetails> createTransportDetailsDataSource(TransportDocument transDoc){
		TransportDetails tranDetails = new TransportDetails(transDoc);
		List<TransportDetails> result = new ArrayList<>(1);
		result.add(tranDetails);
		return result;
	}
	
	public static String join(Object ... args){
		if(args == null || args.length == 0)
			throw new IllegalArgumentException();
		String separator = args[0].toString();
		List<String> tokens = new ArrayList<>(args.length - 1);
		for(int i = 1; i < args.length; ++i)
			tokens.add(Strings.emptyToNull(args[i].toString()));
		Joiner joiner = Joiner.on(separator).skipNulls();
		return joiner.join(tokens);
	}
	
	public static String transformIfNotBlank(String pattern, Object arg){
		if(Strings.isNullOrEmpty(pattern.trim()))
			throw new IllegalArgumentException();
		return arg == null || Strings.isNullOrEmpty(arg.toString())? "": String.format(pattern, arg);
	}
	
	public static CharSequence replaceNonAlphaNumeric(CharSequence seq, char newChar){
		StringBuffer buff = new StringBuffer(seq.length());
		for(int i = 0; i < seq.length(); ++i){
			char ch = seq.charAt(i);
			if(Character.isDigit(ch) || Character.isLetter(ch))
				buff.append(ch);
			else
				buff.append(newChar);
		}
		return buff.toString();
	}
	
	public static String convertToASCII(String text){
		List<CharSequence> tokens = new ArrayList<>();
		try (TokenStream stream = new ASCIIFoldingFilter(new LowerCaseFilter(Version.LUCENE_35, new WhitespaceTokenizer(Version.LUCENE_35, new StringReader(text)))) ){
			stream.reset();
			while(stream.incrementToken())
				tokens.add(replaceNonAlphaNumeric(stream.getAttribute(CharTermAttribute.class).toString(), '_'));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return Joiner.on("_").join(tokens).toString();
	}
	
	public static String cutFileName(String fileName){
		return fileName.length() <= FILENAME_MAX_LENGTH ? fileName : fileName.substring(0, FILENAME_MAX_LENGTH);
	}
	
	public static boolean isFreeUser(Business business) {
		Long premiumExpirationTime = business.getSettings().getNonFreeAccountExpirationTime();
		return premiumExpirationTime == null || premiumExpirationTime < new Date().getTime();
	}

	public static String formatPensionContributionLabel(String pattern, BigDecimal percent, Locale locale){
		return String.format(pattern, removeTrailingZeros(percent, locale));
	}

	public static String formatWitholdTaxLabel(String patternBothLevels, String patternSecondLevel,
											   BigDecimal percentFirstLevel, BigDecimal percentSecondLevel,
											   Locale locale){
		if(HUNDRED_PERCENT.compareTo(percentFirstLevel) == 0)
			return String.format(patternSecondLevel, removeTrailingZeros(percentSecondLevel, locale));
		else
			return String.format(patternBothLevels, removeTrailingZeros(percentSecondLevel, locale),
					removeTrailingZeros(percentFirstLevel, locale));
	}

}
