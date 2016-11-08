package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

	final static String URL = "https://www.semanticscholar.org/paper/Spark-SQL-Relational-Data-Processing-in-Spark-Armbrust-Xin/080ed793c12d97436ae29851b5e34c54c07e3816";
	final static String URL_BASE = "https://www.semanticscholar.org/search?year%5B%5D=2014&year%5B%5D=2015&venue%5B%5D=SIGMOD%20Conference&q=sigmod&sort=relevance&ae=false";

	public static void main(String[] args) throws Exception {
		String html = readUrl(URL_BASE);
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("div.search-result-title > a");
		for (Element e : links) {
			String paperUrl = e.attr("href");
			System.out.println(extractAbstractFromHtml(readUrl("https://www.semanticscholar.org" + paperUrl)));
			System.out.println("=====");
		}
	}

	static String readUrl(String url) {
		URL oracle;
		String html = "";
		try {
			oracle = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			StringBuilder sb = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine + "\n");
			}
			in.close();
			html = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}

	static String extractAbstractFromHtml(String html) {
		String result = "";
		Document doc = Jsoup.parse(html);
		Elements par = doc.select("section.paper-abstract > p");
		result = par.html();
		return result;

	}

}
