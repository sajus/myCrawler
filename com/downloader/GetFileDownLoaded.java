package com.downloader;

import java.io.*;
import java.net.*; 
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.parser.*;
import com.model.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetFileDownLoaded{

	private String _URL;
	private String _FileName; 
	private String _FilePath;
	private String _domainName;
	private ParseFileList myParser;
	private StoreFileName storeFileName;
	public GetFileDownLoaded() {
		myParser = new ParseFileList();
		storeFileName = new StoreFileName();
	}

	public void setDomainName(String _dName) {
		_domainName = _dName;
	}

	public void getFlileByURL(String url) throws IOException {

		_URL = url;
		_FileName = getFileName(); 
		_FilePath = _domainName+"//"+_FileName;

		new File(_domainName).mkdir();
		

		//SSL IMPLEMENTATION; THIS IS NOT WORKING SEEMS WE NEED T0 ADD SOME FOR PROXY;
		// Create a new trust manager that trust all certificates
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
				}
		};
		//Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}

		System.out.println("_domainName "+_domainName);
		System.out.println("_FilePath "+_FilePath);

		//IGNORE THE HTTPS FILES HERE; BY PASS CODE;
		if(_URL.indexOf("https://") != -1 ){
			return;
		}

		URLConnection http = new URL(_URL).openConnection();
		
		http.setDoInput(true);
		http.setDoOutput(true);

		/*START DOWNLOAD THE FILE INTO YOUR DRIVE*/
		InputStream in = http.getInputStream();
        System.out.println("Start Download "+_FilePath);
		try {
				OutputStream out = new FileOutputStream(_FilePath);
				try {
						byte[] buf = new byte[1024];
						int read;

						while ((read = in.read(buf)) > 0)
								out.write(buf, 0, read);
				} finally {
						out.close();
				}
		} finally {
				in.close();
                System.out.println("\nFile Download Completed finally "+_FilePath);
		}
        
        /*FILE HAS BEEN CREATED HERE*/
        System.out.println("\nFile Download Completed "+_FilePath);
        
		/*START THE RECURSIVE DOWNLOAD OF THE FILE*/        
        Document doc = Jsoup.parse(http.getURL(), 5000);
        Elements links = doc.select("a[href]");
        for (Element link: links) {
            System.out.println("\nlink.attrabs:href "+link.attr("abs:href"));
            if(link.attr("abs:href").indexOf("index") == -1){
                this.getFlileByURL(link.attr("abs:href"));
            }
        };
	} 

	private String getFileName() {
		/*
		Map the file name for the requirements;
		*/
		int hashCodeIndex;
		String hashCode;
		String fileWithOutExt;
		String fileExtension;
		String fileName;
		
		if(_URL.lastIndexOf('/')+1 == _URL.length()){
			/*
			FromURL: http://www.flickr.com/photos/cybagesoftware/
			ToURL: http://www.flickr.com/photos/cybagesoftware
			*/
			_URL = _URL.substring(0, _URL.length()-1);
		}

		fileName = _URL.substring(_URL.lastIndexOf('/')+1, _URL.length());		

		if(fileName.lastIndexOf('.') == -1){
			/*
			FromFileName: cybagesoftware
			ToFileName:cybagesoftware.html
			*/
			fileName = fileName +".html";
		}
		
		//CASE TO CLEAN THE HASH TAGS:
		//-->CASE: 1 'www.cybage.com#'
		//-->CASE: 2 'enterprise-business-solutions.aspx#SOA'

		hashCodeIndex = fileName.lastIndexOf('#')+1;
		
		if(hashCodeIndex!=0){
			if(hashCodeIndex == fileName.length()){
				/*
				FromFileName: www.cybage.com#
				ToFileName:www.cybage.com.html
				*/
				fileName = fileName.replace("#", ".html");				 
			}else{
				/*
				FromFileName: enterprise-business-solutions.aspx#SOA 
				ToFileName: enterprise-business-solutions.SOA.aspx
				*/
				hashCode = fileName.substring(hashCodeIndex, fileName.length());
				fileWithOutExt = fileName.substring(0, fileName.lastIndexOf('.')+1);
				fileExtension = fileName.substring(fileName.lastIndexOf('.')+1, fileName.lastIndexOf('#'));
				fileName = fileWithOutExt+""+hashCode+"."+fileExtension;			 	 
			}			
		}

		if(_URL.indexOf("//") != -1 && _URL.lastIndexOf("/")!= -1){
			if(_URL.indexOf("//")+1 == _URL.lastIndexOf("/")){
				if(fileName.indexOf(".html") == -1 ){
					/*
					FromFileName: www.cybage.com
					ToFileName:www.cybage.com.html
					*/
					fileName += ".html";					
				}				
			}
		}

		//CASE WITH QUERY STRING; REMOVE THE SPECIAL CHARACTERS;
		if(fileName.lastIndexOf('?') != -1){	
			//ADD SPECIAL CHARACTERS IN THE LIST, FILE CREATION WILL FAIL ELSE;		 		
			fileName = fileName.replaceAll("[?=&%:/]","");	
		}
		return fileName;		
	}
	
}