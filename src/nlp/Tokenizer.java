package nlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class Tokenizer {
	private static TokenizerModel model;
	public static ArrayList<String[]> tokens=new ArrayList<String[]>();
	
	public static ArrayList<String[]> take(String[] sentences) throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("models/en-token.bin");	 
		TokenizerModel model = new TokenizerModel(is);	 
		TokenizerME tokenizer = new TokenizerME(model);	
		for(int i=0;i<sentences.length;i++){
			String sentencetokens[] = tokenizer.tokenize(sentences[i]);	 
			tokens.add(sentencetokens);
//			for (String a : sentencetokens)
//				System.out.println(a);	 
			is.close();
		}
		return tokens;
	}
	
	public Tokenizer() throws InvalidFormatException, IOException{
		InputStream is = new FileInputStream("models/en-token.bin");	 
		model = new TokenizerModel(is);	 
		is.close();
	}
	
	public ArrayList<String[]> getTokens(String[] sentences){
		tokens.clear();
		for(int i=0;i<sentences.length;i++){
			TokenizerME tokenizer = new TokenizerME(model);
			String sentencetokens[] = tokenizer.tokenize(sentences[i]); 
			tokens.add(sentencetokens);
//			for (String a : sentencetokens)
//				System.out.println(a);	 
		}
		return tokens;
	}
	
	public static void main(String[] args) {
		try {
			String[] sentences={"I like surface book!","But, Mac system is better."};
			ArrayList<String[]> tokens=Tokenizer.take(sentences);
			for(int i=0;i<tokens.size();i++){
				for(int j=0;j<tokens.get(i).length;j++){
					System.out.println(tokens.get(i)[j]);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
