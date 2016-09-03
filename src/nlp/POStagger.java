package nlp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

public class POStagger {
	private POSModel model;

	public static ArrayList<String[]> tags=new ArrayList<String[]>();
	
	public static ArrayList<String[]> tag(ArrayList<String[]> tokens) throws IOException {
		POSModel model = new POSModelLoader()	
			.load(new File("models/en-pos-maxent.bin"));
		//PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME taggerx = new POSTaggerME(model);
	 
		//perfMon.start();
		for(int i=0;i<tokens.size();i++) {
			String[] sentencetag = taggerx.tag(tokens.get(i));
			tags.add(sentencetag);	 
//			POSSample sample = new POSSample(tokens.get(i), tags);
//			System.out.println(sample.toString());	 
			//perfMon.incrementCounter();
		}
		//perfMon.stopAndPrintFinalResult();
		return tags;
	}
	
	public POStagger() throws InvalidFormatException, IOException{
		model = new POSModelLoader()	
			.load(new File("models/en-pos-maxent.bin"));
		//PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
	}
	
	public ArrayList<String[]> POStagging(ArrayList<String[]> tokens){
		tags.clear();
		for(int i=0;i<tokens.size();i++) {
			POSTaggerME tagger = new POSTaggerME(model);
			String[] sentencetag = tagger.tag(tokens.get(i));
			tags.add(sentencetag);	 
//			POSSample sample = new POSSample(tokens.get(i), tags);
//			System.out.println(sample.toString());	 
			//perfMon.incrementCounter();
		}
		//perfMon.stopAndPrintFinalResult();
		return tags;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			ArrayList<String[]> tokens=new ArrayList<String[]>();
			String[] sentence={"I","like","surface","book","!"};
			tokens.add(sentence);
			ArrayList<String[]> tags=tag(tokens);
			for(int i=0;i<tags.size();i++){
				for(int j=0;j<tags.get(i).length;j++){
					System.out.println(tokens.get(i)[j]+" "+tags.get(i)[j]);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
