package com.dan.exercise.service;

import java.io.IOException;
import java.io.*;
import java.util.*;

public class CustomFileWriter{
    private String path;

	public CustomFileWriter(String pathToFile){
        path = pathToFile;
    }

    public void write(List<Map<String,String>> list) {
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuffer row = null;
            String key = null;
            String value = null;
            for (Map<String,String> map : list){
                row = new StringBuffer();
                for (Map.Entry<String,String> entry : map.entrySet()){
                    row.append(entry.getKey() + "-equals-" + entry.getValue() + " ");
                }
                bw.write(row.toString());
                bw.newLine();
            }
            
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}