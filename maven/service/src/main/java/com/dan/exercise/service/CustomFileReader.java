package com.dan.exercise.service;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;

public class CustomFileReader {
	private String path;

	public CustomFileReader(String pathToFile){
		path = pathToFile;
	}

	public List<String> openFile() throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		int numberOfLines = readLines();
		List<String> textData =	new ArrayList<>();

		for(int i = 0; i<numberOfLines; i++){
			textData.add(textReader.readLine());
		}
		textReader.close();
		return textData;
	}

	public int readLines() throws IOException{
		FileReader fileToRead = new FileReader(path);
		BufferedReader bf = new BufferedReader(fileToRead);

		String aLine;
		int numberOfLines = 0;

		while (( aLine = bf.readLine()) != null) {
			numberOfLines++;
		}
		bf.close();

		return numberOfLines;
	}
} 