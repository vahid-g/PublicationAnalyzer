package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfParser {
	public static void main(String[] args) {
		extractPapersFromProceedings(new File("data/sigmod.txt"));
		String[] papers = extractPapersFromStructuredProceedings(new File("data/sigmod_struct.txt"));
		System.out.println(papers[0]);
		System.out.println();
		System.out.println(papers[papers.length - 1]);
	}

	public static void printBookmark(PDOutlineNode bookmark, String indentation) throws IOException {
		PDOutlineItem current = bookmark.getFirstChild();
		while (current != null) {
			System.out.println(indentation + current.getTitle());
			PDDestination destination = current.getDestination();
			System.out.println("+ " + destination);
			printBookmark(current, indentation + "    ");
			current = current.getNextSibling();
		}
	}

	public static String parsePdfToString(String filename) {
		String text = null;
		try {
			System.out.println("Parsing the PDF file..");
			RandomAccessRead rar = new RandomAccessFile(new File(filename), "r");
			PDFParser parser = new PDFParser(rar);
			parser.parse();
			System.out.println("Parsing Done!");
			PDDocument document = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(document);
			System.out.println(text);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	public static String loadPdfToString(String filename) throws IOException {
		PDDocument document = PDDocument.load(new File(filename));
		PDFTextStripper reader = new PDFTextStripper();
		String pdfText = reader.getText(document);
		document.close();

		// writing pdf text to a text file
		// try (FileWriter fw = new FileWriter("data/sigmod.txt")) {
		// fw.write(pdfText);
		// }

		return pdfText;
	}

	public static String[] extractPapersFromProceedings(File proceedingsFile) {
		// reading proceedings txt file
		byte[] data = null;
		try (FileInputStream fis = new FileInputStream(proceedingsFile)) {
			data = new byte[(int) proceedingsFile.length()];
			fis.read(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String proceedingsText = null;
		try {
			proceedingsText = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return extractPapersFromProceedings(proceedingsText);
	}

	public static String[] extractPapersFromProceedings(String proceedingsText) {
		// cleanup string
		proceedingsText = proceedingsText.replaceAll("-\\r\\n", "")
				.replaceAll("Permission to make(?s:.)+?15.00", "")
				.replaceAll("\\t\\r", "\\r\\n");

		// extracting text of papers
		String abstractPattern = "ABSTRACT\\r?\\n((?s:.)+?)\\r?\\n(1\\.|Categories and Subject Descriptors)+?";
		String documentPattern = "ABSTRACT\\r\\n((?s:.)+?)REFERENCES\\r\\n";
		Pattern pattern = Pattern.compile(documentPattern);
		Matcher matcher = pattern.matcher(proceedingsText);
		ArrayList<String> documents = new ArrayList<String>();
		while (matcher.find()) {
			documents.add(matcher.group(1) + "\n");
		}
		System.out.println("number of loaded documents: " + documents.size());
		try (FileWriter fw = new FileWriter("data/sigmod_struct.txt")) {
			for (String doc : documents) {
				String cleanDoc = doc.replaceAll("\\r\\n.{1,20}\\r\\n", "");
				fw.write("<==\n" + cleanDoc + "\n==>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documents.toArray(new String[0]);
	}

	public static String[] extractPapersFromStructuredProceedings(File proceedingsFile) {
		// reading proceedings txt file
		byte[] data = null;
		try (FileInputStream fis = new FileInputStream(proceedingsFile)) {
			data = new byte[(int) proceedingsFile.length()];
			fis.read(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String proceedingsText = null;
		try {
			proceedingsText = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// extracting text of papers
		String documentPattern = "<==((?s:.)+?)==>";
		Pattern pattern = Pattern.compile(documentPattern);
		Matcher matcher = pattern.matcher(proceedingsText);
		ArrayList<String> documents = new ArrayList<String>();
		while (matcher.find()) {
			documents.add(matcher.group(1) + "\n");
		}
		System.out.println("number of loaded documents: " + documents.size());
		return documents.toArray(new String[0]);
	}
}
