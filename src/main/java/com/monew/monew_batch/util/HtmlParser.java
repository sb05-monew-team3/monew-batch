package com.monew.monew_batch.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

	public static String extractLastParagraph(String html) {
		if (html == null || html.trim().isEmpty())
			return "";

		try {
			Document doc = Jsoup.parse(html);
			Elements pTags = doc.select("p");

			if (pTags.isEmpty())
				return "";

			Element lastP = pTags.last();
			return lastP != null ? lastP.text() : "";
		} catch (Exception e) {
			return "";
		}
	}

}
