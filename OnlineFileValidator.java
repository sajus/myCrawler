/*
	DOWNLOAD THE WEB DOCUMENT INTO YOUR LOCAL BEFORE DOING THE VALIDATIONS
*/

import java.io.*;
import java.net.*; 
import com.model.*;
import com.downloader.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.parser.ParseFileList;

public class OnlineFileValidator {
		
	public static void main(String args[]) throws IOException {
			
		System.setProperty("http.proxyHost", "172.27.171.91");
		System.setProperty("http.proxyPort", "8080"); 

		if (args.length != 2) {
			System.err.println("usage: url file name");            
		}

		System.out.print(args[0]);

		
		GetFileDownLoaded fileDownloader = new GetFileDownLoaded();
		
		fileDownloader.setDomainName(args[0]);
		fileDownloader.getFlileByURL("http://" + args[0]);
		 
	}

	public static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    public static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }    

}
