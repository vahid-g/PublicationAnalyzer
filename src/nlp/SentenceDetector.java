package nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class SentenceDetector {
	private static SentenceModel model;

	public static String[] detect(String text) throws InvalidFormatException,
			IOException {

		InputStream is = new FileInputStream("models/en-sent.bin");
		SentenceModel model = new SentenceModel(is);
		SentenceDetectorME sdetector = new SentenceDetectorME(model);

		String sentences[] = sdetector.sentDetect(text);
//		for(int i=0;i<sentences.length;i++){
//			System.out.println("["+(i+1)+"]:"+sentences[i]);
//		}
		is.close();
		return sentences;
	}
	
	public SentenceDetector() throws InvalidFormatException, IOException{
		InputStream is = new FileInputStream("models/en-sent.bin");
		model = new SentenceModel(is);
		is.close();
	}
	
	public String[] sentences(String text){
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		String sentences[] = sdetector.sentDetect(text);
//		for(int i=0;i<sentences.length;i++){
//			System.out.println("["+(i+1)+"]:"+sentences[i]);
//		}
		return sentences;
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		DocumentReader dr=new DocumentReader();
//		try {
//			String text= dr.readFileToString(FileLocation);
//			System.out.println(text);
//			String[] sentences=detect(text);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

}
