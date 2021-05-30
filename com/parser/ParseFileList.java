package com.parser;

import java.io.*;
import java.net.*; 

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ParseFileList {
	public void ParseFileList() {	 
	}

	public String[] getFileLinks(URL fileUrl) throws IOException {
		
		Document doc = Jsoup.parse(fileUrl, 5000);
        Elements links = doc.select("a[href]");
        String [] linkCollection = new String [100];
    
        for (Element link: links) {
            //linkCollection.add(link.attr("abs:href"));
        }
        return linkCollection;
	} 
}