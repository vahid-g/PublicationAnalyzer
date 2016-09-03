package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RemoveTag {

	
	public static HashMap<String,Integer> removeTags(List<ComparableObj<String,Integer>> rank){
		HashMap<String,Integer> terms=new HashMap<String,Integer>();
		for(ComparableObj<String,Integer> node:rank){
			String term=node.input.replace("_JJ", "");
			term=term.replace("_NN", "");
			terms.put(term, node.occurrences);
		}
		return terms;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
