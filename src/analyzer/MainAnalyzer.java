package analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nlp.POStagger;
import nlp.Tokenizer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class MainAnalyzer {

	public static final String[] STOP_WORDS = { "great", "numerous", "several", "year", "just", "good", "property",
			"variety", "large", "student", "opportunity", "e .g", "%", "important", "other", "same", "thin" };

	public static final int C_VALUE_THRESHOLD = 0;

	public static final int FREQ_THRESHOLD = 3;

	public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	public static void main(String[] args) throws IOException {
		// extracting paper texts from pdf of proceedings
		// String pdfText = extractText("data/SIGMOD2015.pdf");
		// String pdfText = extractText("data/small.pdf");
		// System.out.println(pdfText);

		// writing paper text to .txt file
		// try (FileWriter fw = new FileWriter("data/sigmod.txt")) {
		// fw.write(pdfText);
		// }

		// reading papers from the txt file
		byte[] data;
		File file = new File("data/sigmod.txt");
		try (FileInputStream fis = new FileInputStream(file)) {
			data = new byte[(int) file.length()];
			fis.read(data);
		}
		String pdfText = new String(data, "UTF-8");

		// cleanup string
		pdfText = pdfText.replaceAll("-\\r?\\n", "").replaceAll(
				"Permission to make(?s:.)+?15.00", "");
	

		// extracting abstracts of papers
		Pattern pattern = Pattern.compile("ABSTRACT\\r?\\n((?s:.)+?)\\r?\\n(1\\.|Categories and Subject Descriptors)+?");
		Matcher matcher = pattern.matcher(pdfText);
		ArrayList<String> abstracts = new ArrayList<String>();
		while (matcher.find()) {
			abstracts.add(matcher.group(1) + "\n");
		}
		System.out.println(abstracts.size());
		System.out.println(abstracts);

		List<Phrase> phraseList = getPhraseFrequency(abstracts.toArray(new String[0]));
		removeStopWords(phraseList);
		List<Phrase> phrases = computeCvalue(phraseList);
		System.out.println(sdf.format(new Date()) + " - Printing Results..");
		Collections.sort(phrases, new Comparator<Phrase>() {
			@Override
			public int compare(Phrase o1, Phrase o2) {
				return o1.cValue.compareTo(o2.cValue);
			}
		});
		Set<Phrase> topPhrases = new HashSet<Phrase>();
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/output.txt"));
		System.out.println("phraseList size: " + phraseList.size());
		for (int i = phraseList.size() - 1; i >= phraseList.size() - 20; i--) {
			if (i < 0)
				break;
			topPhrases.add(phraseList.get(i));
			Phrase phrase = phraseList.get(i);
			bw.write(phrase.text + "& " + phrase.cValue + "\\\\ \\hline \n");
		}
		System.out.println(topPhrases);
		bw.close();
	}

	public static String extractText(String filename) throws IOException {
		PDFTextStripper reader = new PDFTextStripper();
		PDDocument document = PDDocument.load(new File(filename));
		String pageText = reader.getText(document);
		document.close();
		return pageText;
	}

	public static List<Phrase> getPhraseFrequency(String[] abstractArray) throws IOException {
		System.out.println(sdf.format(new Date()) + " - Computing phrase frequencies..");
		System.out.println("  Tokenizing " + sdf.format(new Date()));
		ArrayList<String[]> tokenList = new Tokenizer().getTokens(abstractArray);
		System.out.println("  Flattening " + sdf.format(new Date()));
		// ArrayList<String[]> tagList = POStagger.tag(tokenList);
		ArrayList<String[]> tagList = new POStagger().POStagging(tokenList);
		String[] tags = flattenArray(tagList);
		String[] tokens = flattenArray(tokenList);
		System.out.println("    tag token size: " + tags.length + " " + tokens.length);
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/tagged.txt"));
		for (int i = 0; i < tags.length; i++) {
			bw.write(tags[i] + " " + tokens[i] + "\n");
		}
		bw.close();
		System.out.println("  building tags " + sdf.format(new Date()));
		HashMap<Integer, Integer> tagTokenMap = new HashMap<Integer, Integer>();
		StringBuilder tagBuilder = new StringBuilder();
		StringBuilder tokenBuilder = new StringBuilder();
		int tagStartPos = 0;
		int tokenStartPos = 0;
		for (int i = 0; i < tags.length; i++) {
			tagTokenMap.put(tagStartPos, tokenStartPos);
			tagBuilder.append(tags[i]);
			tagBuilder.append(" ");
			tokenBuilder.append(tokens[i]);
			tokenBuilder.append(" ");
			tagStartPos = tagBuilder.length();
			tokenStartPos = tokenBuilder.length();
		}
		tagTokenMap.put(tagStartPos, tokenStartPos);
		String tagString = tagBuilder.toString();
		String tokenString = tokenBuilder.toString();
		System.out.println("  regex " + sdf.format(new Date()));
		Pattern pattern = Pattern.compile(
				"((NN)|(NNS)|(JJ)|(VBG))+ (NN|NNS) (IN ((NN)|(NNS)|(JJ)|(VBG))+ (NN|NNS) )*");
		Matcher matcher = pattern.matcher(tagString);
		HashMap<String, Integer> pattern2count = new HashMap<String, Integer>();
		// Iterate through all matched substrings and print their positions.
		// Keep in mind that character indices start with 0.
		// System.out.println(" substring [start_position, end_position]");
		while (matcher.find()) {
			int tokenStart = tagTokenMap.get(matcher.start());
			int tokenEnd = tagTokenMap.get(matcher.end());
			String substring = tokenString.substring(tokenStart, tokenEnd);
			if (pattern2count.containsKey(substring)) {
				pattern2count.put(substring, pattern2count.get(substring) + 1);
			} else {
				pattern2count.put(substring, 1);
			}
		}
		System.out.println("  making list " + sdf.format(new Date()));
		List<Phrase> phraseList = new ArrayList<Phrase>();
		for (String key : pattern2count.keySet()) {
			if (pattern2count.get(key) > FREQ_THRESHOLD)
				phraseList.add(new Phrase(key, pattern2count.get(key)));
		}
		return phraseList;
	}

	private static String[] flattenArray(ArrayList<String[]> list) {
		String[] array = new String[0];
		for (String[] tmp : list) {
			array = ArrayUtils.addAll(array, tmp);
		}
		return array;
	}

	public static void removeStopWords(List<Phrase> list) {
		System.out.println(sdf.format(new Date()) + " - Removing stopwords..");
		List<Phrase> removable = new ArrayList<Phrase>();
		for (Phrase phrase : list) {
			for (String stopword : STOP_WORDS) {
				if (phrase.text.contains(stopword)) {
					removable.add(phrase);
					break;
				}
			}
		}
		for (Phrase phraseKey : removable) {
			list.remove(phraseKey);
		}
	}

	public static List<Phrase> computeCvalue(List<Phrase> phraseList) {
		System.out.println(sdf.format(new Date()) + " - Computing CValue..");
		Collections.sort(phraseList, new Comparator<Phrase>() {
			@Override
			public int compare(Phrase o1, Phrase o2) {
				if (o1.text.length() > o2.text.length())
					return +1;
				else if (o1.text.length() < o2.text.length())
					return -1;
				else
					return o1.text.compareTo(o2.text);
			}
		});
		for (int i = phraseList.size() - 1; i >= 0; i--) {
			Phrase phrase = phraseList.get(i);
			if (phrase.tValue == 0) {
				phrase.cValue = (Math.log(phrase.text.split(" ").length) / Math.log(2)) * phrase.freq;
			} else {
				phrase.cValue = (Math.log(phrase.text.split(" ").length) / Math.log(2))
						* (phrase.freq - (1.0 / phrase.ctValue) * phrase.tValue);
			}
			for (int j = 0; j < i; j++) {
				Phrase subPhrase = phraseList.get(j);
				if (phrase.text.contains(subPhrase.text)) {
					subPhrase.tValue += phrase.freq - phrase.tValue;
					subPhrase.ctValue++;
				}
			}
		}
		return phraseList;
	}

	public static class Phrase {
		String text;
		Integer freq;
		int tValue = 0;
		int ctValue = 0;
		Double cValue;

		public Phrase(String text, int freq) {
			this.text = text;
			this.freq = freq;
		}

		@Override
		public String toString() {
			return text;
		}

		@Override
		public boolean equals(Object obj) {
			Phrase phrase = (Phrase) obj;
			return this.text.equals(phrase.text);
		}
	}
}
