package analyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static void main(String[] args) {
		String text = "hanhan olde?";
		Pattern pattern = Pattern.compile("(o|e)");
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()){
			System.out.println(matcher.groupCount());
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
		}
	}

}
