package com.wk.service.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wk.pojo.filedata.FileData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wkgui
 */
@Service
public class FileDataUtilImpl implements FileDataUtilBs {

    private String path;
    private String fileDataPath;
    private ReadWriteLock lock;
    private ArrayList<FileData> fileDataArrayList;

    public FileDataUtilImpl() {
        path = FileDataUtilBs.class.getResource("/").getPath();
        path = path.substring(0, path.length()-"/WEB-INF/classes/".length());
        path += "/WEB-INF/upload/";

        fileDataPath = path.substring(0, path.length()-"classes/".length())+"filedata/files.json";

        lock = new ReentrantReadWriteLock();

        setFileData();
    }

    @Override
    public boolean deleteFile(String fileName, String owner) {
        try {
            lock.writeLock().lock();
            File file = new File(path + fileName);
            if(file.exists()){
                for(FileData data : fileDataArrayList){
                    if(data.getFileName().equals(fileName) && data.getOwner().equals(owner)){
                        fileDataArrayList.remove(data);
                    }
                }
            }
            saveFileData();
            return file.delete();
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean saveFile(MultipartFile multipartFile, String owner) {
        File file = new File(path + multipartFile.getOriginalFilename());
        try {
            lock.writeLock().lock();
            if(file.exists()){
                return false;
            }
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            fileDataArrayList.add(new FileData(multipartFile.getOriginalFilename(), multipartFile.getSize(), new Date().toString(), owner));
            saveFileData();
        }finally {
            lock.writeLock().unlock();
        }

        return true;
    }

    @Override
    public InputStream readFile(String fileName) {
        File file = new File(path+fileName);
        if(file.exists()){
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void saveFileData() {
        try {
            lock.writeLock().lock();
            Gson gson = new Gson();
            String userDataJson = gson.toJson(fileDataArrayList);
            try (BufferedWriter out = new BufferedWriter(new FileWriter(path, false))) {
                out.write(userDataJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setFileData() {
        String fileDataJson = FileUtil.getJsonDataFromFile(fileDataPath);
        Gson gson = new Gson();
        fileDataArrayList = gson.fromJson(fileDataJson, new TypeToken<List<FileData>>(){}.getType());
    }

    @Override
    public List getFileDataList() {
        return fileDataArrayList;
    }
}
