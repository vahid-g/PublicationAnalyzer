package paper_reader;

import java.awt.print.Book;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfReader {
	public static void main(String[] args) {
		String filename = "data/small.pdf";

	}

	private static void printBookmark(PDOutlineNode bookmark, String indentation)
			throws IOException {
		PDOutlineItem current = bookmark.getFirstChild();
		while (current != null) {
			System.out.println(indentation + current.getTitle());
			PDDestination destination = current.getDestination();
			System.out.println("+ " + destination);
			printBookmark(current, indentation + "    ");
			current = current.getNextSibling();
		}

	}

	public static String readWholePDF(String filename) {
		String text = null;
		try {
			RandomAccessRead rar = new RandomAccessFile(new File(filename), "r");
			PDFParser parser = new PDFParser(rar);
			System.out.println("Parsing the PDF file..");
			parser.parse();
			System.out.println("Parsing Done!");
			PDDocument document = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(document);
			System.out.println(text);
			// PDDocumentOutline outline = document.getDocumentCatalog()
			// .getDocumentOutline();
			// printBookmark(outline, "");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
}
