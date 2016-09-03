package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class reader {
	public static ArrayList<File> files=new ArrayList<File>();
	
	public static ArrayList<File> listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            if (!fileEntry.getName().contains("awd")){
	            	listFilesForFolder(fileEntry);
	            } else if (fileEntry.getName().contains("_0")){
	            	listFilesForFolder(fileEntry);
	            }
	        } else {
	            files.add(fileEntry);
	        }
	    }
	    return files;
	}
	
	public static String readAbstract(final File txt) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(txt));	 
		String line = null;
		String Abstract="";
		while ((line = br.readLine()) != null) {
			if(line.length()>8){
				if( line.substring(0, 8).equals("Abstract")){
					break;
				}
			}
		}
		while ((line = br.readLine()) != null) {
			if(!line.isEmpty()){
				Abstract=Abstract+line+" ";
			}
		}
	 
		br.close();
		return Abstract.replaceAll("\\s+", " ").trim();
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final File folder = new File("data/awards_1994");
		ArrayList<File> texts=listFilesForFolder(folder);
		System.out.println(texts.size());
		try {
			for(int i=0;i<texts.size();i++){
			String text=readAbstract(texts.get(i));
			System.out.println(text);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
