package com.model;

import java.io.*;
import java.net.*; 

public class StoreFileName {

	private String[] _FileListArray;
	
	public StoreFileName() {
		_FileListArray = new String[1000];
	}
	
	public void storeFiles(String[] _list) {
		System.out.print("_list"+_list);
	} 
}