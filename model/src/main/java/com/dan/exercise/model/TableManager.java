package com.dan.exercise.model;

import com.dan.exercise.service.*;
import java.util.*;
import java.io.*;
import org.apache.commons.lang3.*;

public class TableManager {
	private List<Map<String,String>> valueList;

	public TableManager(){
		valueList = new ArrayList<>();
	}

	public void showTable(){
    	String cell = "";
    	System.out.println();
		for (Map<String,String> map : valueList){
			System.out.print("|");
			for (Map.Entry<String,String> entry : map.entrySet()){
        		cell = entry.getKey() + "=" + entry.getValue();
				System.out.print(String.format("%-15s",cell));
				System.out.print("|");
			}
			System.out.println();
		}
	}

	public void showMenu(){
		showTable();
		System.out.println();
		System.out.println("1. Edit");
    	System.out.println("2. Search");
    	System.out.println("3. Add");
    	System.out.println("4. Sort");
    	System.out.println("5. Exit");
	}

	public String getFilePath(){
		Properties props = new Properties();
		String filePath = "";
    	try {
			// InputStream inputStream = new FileInputStream("config.properties");
	  		props.load(ClassLoader.getSystemResourceAsStream("config.properties"));
	  		filePath = props.getProperty("path");
        } catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public void readFile() throws IOException{
		
	    CustomFileReader file = new CustomFileReader(getFilePath());
	    List<String> textFileData = file.openFile();
	    valueList.clear();
	    for(String text : textFileData){
	      Map<String,String> valueMap = new LinkedHashMap<>();
	      for (String textSplit : text.split(" ")){
	        valueMap.put(textSplit.split("-equals-")[0],textSplit.split("-equals-")[1]);
	      }
	      valueList.add(valueMap);
	    }
	}

	public void searchTable(){
    	Scanner in = new Scanner(System.in);
    	int row = 1;
    	int column = 1;
    	int numberOfOccurrence = 0;
    	String keyword;

    	System.out.print("Enter keyword to search... ");
    	keyword = in.next().trim();

    	for (Map<String,String> map : valueList){
			column = 1;
    		for (Map.Entry<String,String> entry : map.entrySet()){
    			//string util
    			numberOfOccurrence = 0;
    			numberOfOccurrence += StringUtils.countMatches(entry.getKey(),keyword);
    			numberOfOccurrence += StringUtils.countMatches(entry.getValue(),keyword);
    			if(numberOfOccurrence > 0){
    				System.out.println("row " + row + " column " + column + " : " + numberOfOccurrence + " occurence");	
    			}
    			column++;
    		}
    		row++;
    	}
    	if(numberOfOccurrence == 0){
    		System.out.println("No occurence found");
    	}
    	System.out.print("Press enter to continue...");
    	Scanner keyboard = new Scanner(System.in);
    	keyboard.nextLine();
    	showMenu();
	}

  	public boolean isExisting(int row, String key){
  		return valueList.get(row-1).containsKey(key);
  	}

  	public void editTable(){
    	int row;
    	String key;
    	String newKey;
    	String newVal;
    	Scanner in = new Scanner(System.in);
              
		do { //validate row
	      System.out.print("Row number... ");
	        while (!in.hasNextInt()) {
	          in.next();
	          System.out.print("Not a number... ");
	        }
	        row = in.nextInt();
	    } while (row < 0 || row > valueList.size());

	    //validate key
	    System.out.print("Select key... ");
	    key = in.next();
	    while(!isExisting(row,key)){
	    	System.out.print("Key is not existing... ");
	    	key = in.next();
	    }

	    //validate new key
	    System.out.println("5 characters only... ");
	    System.out.print("New KEY... ");
	    newKey = in.next();
	    boolean valid = false;

	    while(!valid){
	    	while(isExisting(row,newKey)){
	    		System.out.println("Key already exist");
	    		System.out.print("New KEY... ");
	    		newKey = in.next();
	    	}
	    	if(newKey.length() > 5){
	    		System.out.println("Invalid KEY");
	    		System.out.print("New KEY... ");
	    		newKey = in.next();
	    		valid = false;
	    	}
	    	else{valid = true;}
	    }

	    //validate new value
	    System.out.print("New VALUE... ");
	    newVal = in.next();
	    while(newVal.length() > 5){
	    	System.out.println("Invalid VALUE");
	    	System.out.print("New VALUE... ");
	    	newVal = in.next();
	    }

	    valueList.get(row-1).remove(key);
    	valueList.get(row-1).put(newKey,newVal);
    	try{
    		saveFile();
    	}
    	catch(IOException e){
    		System.out.println("Error thrown : " + e.getMessage());
    	}
		try{
			readFile();		
		}
		catch(IOException e){
			System.out.println("Error thrown : " + e.getMessage());		
		}
    	showMenu();
  	}

	public void saveFile() throws IOException{
	    CustomFileWriter fw = new CustomFileWriter(getFilePath());
	    fw.write(valueList);
	}

	public void addRow(){
		Map<String,String> newRow = new LinkedHashMap<>();
		
		Scanner in  = new Scanner(System.in);
		String key, val;
		boolean valid;
		List<String> list = new ArrayList<>();
		for (int i = 1; i <= valueList.get(0).size(); i++){
			valid = false;
			System.out.println("Column " + i);
			System.out.print("Key... ");
			key = in.next();

			while(!valid){
				while(key.length() > 5 || key.contains("=")){
					System.out.print("Invalid key... ");
					key = in.next();
				}
				if(list.size()>0 && list.contains(key)){
					System.out.print("Key already exist... ");
					key = in.next();
					valid = false;
				}
				else{
					valid = true;
				}
			}
			
			list.add(key);
			System.out.print("Value... ");
			val = in.next();
			while(val.length() > 5 || val.contains("=")){
				System.out.println("Invalid value... ");
				val = in.next();
			}
			newRow.put(key,val);
		}
		valueList.add(newRow);
		try{
			saveFile();	
		}catch(IOException e){
			System.out.println("Error saving to file!");
		}
		finally{
			showMenu();	
		}
	}

	public void sortTable(){
		List<Map<String,String>> list = new ArrayList<>();
		Map<String,String> sorted;
		for (int i=0; i<valueList.size(); i++){
			sorted = new TreeMap<>(valueList.get(i));
			list.add(sorted);
		}
		valueList.clear();
		valueList.addAll(list);
		
		try{
			saveFile();	
		}catch(IOException e){
			System.out.println("Cannot save to file");
		}
		showMenu();
	}

	public void select(){
	    Scanner in = new Scanner(System.in);
		int selection;
		boolean cont = true;
	  	while(cont){
	  		do {
				System.out.print("Select... ");
        		while (!in.hasNextInt()) {
            		in.next();
            		System.out.print("Invalid... ");
        		}
        		selection = in.nextInt();
			} while (selection <= 0 || selection > 5);
	  		
	      	switch(selection){
	  			case 1: editTable();
	  				break;
	  			case 2: searchTable();
	  				break;
	  			case 3: addRow();
	  				break;
	  			case 4: sortTable();
	  				break;
	  			case 5: cont = false;
	  				break;			
	  			default:
	  				System.out.println("Invalid");			
	  		}	
	  	}
	}
}