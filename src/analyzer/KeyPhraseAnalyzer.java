package analyzer;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class KeyPhraseAnalyzer {
	public static void main(String[] args) throws IOException {
		String filename = "data/small.pdf";
		PDFTextStripper reader = new PDFTextStripper();
		PDDocument document = PDDocument.load(new File(filename));
		String pageText = reader.getText(document);
		System.out.println(pageText);
	}
}
