package extraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.ComparableObj;

public class RegEx {
	public void regularExpressionPractice(){
		String s = "abccabccd bcd cccd de dfkobccd dfabce ef dcbccd dcd fccd";
		
		// Define the search pattern bc+
		Pattern pattern = Pattern.compile("bc+");
		
		Matcher matcher = pattern.matcher(s);
		
		Map<String,Integer> pattern2count = new HashMap<String, Integer>();
		
		// Iterate through all matched substrings and print their positions.
		// Keep in mind that character indices start with 0.
		System.out.println("substring [start_position, end_position]");
		while(matcher.find()){
			String substring = matcher.group();
			System.out.println(substring + "\t[" + matcher.start() + " , " + matcher.end() + "]" );
			if(pattern2count.containsKey(substring)){
				pattern2count.put(substring, pattern2count.get(substring) + 1);
			}else{
				pattern2count.put(substring, 1);
			}
		}
		System.out.println("\nFrequencies of substrings.");
		System.out.println(pattern2count);
		
		// sort them in descending order
		List<ComparableObj<String,Integer>> rank  = new ArrayList<ComparableObj<String,Integer>>();
		for(Map.Entry<String, Integer> entry : pattern2count.entrySet()){
			rank.add(new ComparableObj<String,Integer>(entry.getKey(),entry.getValue()));
		}
		
		Collections.sort(rank);
		System.out.println();
		System.out.println("Rank substrings in descending order.");
		System.out.println(rank);
	}
	
	public static void main(String[] args) {
		RegEx regEx = new RegEx();
		regEx.regularExpressionPractice();
	}

}