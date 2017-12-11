package com.wk.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
    public static String getJsonDataFromFile(String filePath){
        File file = new File(filePath);
        StringBuilder json = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempStr;
            while ((tempStr = reader.readLine()) != null){
                json.append(tempStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
