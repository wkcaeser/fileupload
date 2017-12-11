package com.wk.service.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wk.pojo.userdata.UserData;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wkgui
 */
@Service
public class UserDataUtilImpl implements UserDataUtilBs {

    private String path;
    private ArrayList<UserData> userDataArrayList;
    private ReadWriteLock lock;
    private ThreadPoolExecutor threadPoolExecutor;

    public UserDataUtilImpl() {
        path = UserDataUtilImpl.class.getResource("/").getPath();
        path += "/userdata/users.json";

        lock = new ReentrantReadWriteLock();

        int corePoolSize = 1;
        int maximumPoolSize = 1;
        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedTransferQueue<>(),
                Thread::new,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        listReadUsersDataToObject();
    }


    @Override
    public List<UserData> listReadUsersDataToObject() {
        String usersJson = FileUtil.getJsonDataFromFile(path);
        Gson gson = new Gson();
        userDataArrayList = gson.fromJson(usersJson, new TypeToken<List<UserData>>(){}.getType());
        return userDataArrayList;
    }

    @Override
    public void writeUserDataListToFile() {
        try {
            lock.readLock().lock();
            Gson gson = new Gson();
            String userDataJson = gson.toJson(userDataArrayList);
            try (BufferedWriter out = new BufferedWriter(new FileWriter(path, false))) {
                out.write(userDataJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally {
            lock.readLock().unlock();
        }
    }


    public ArrayList<UserData> getUserDataArrayList() {
        return userDataArrayList;
    }

    @Override
    public boolean checkUserDataIsAllowedAndAdd(UserData userData) {
        try {
            lock.writeLock().lock();

            Set<UserData> userDataSet = new HashSet<>();

            userDataSet.addAll(userDataArrayList);

            if(userDataSet.contains(userData)){
                return false;
            }

            userDataArrayList.add(userData);

        }finally {
            lock.writeLock().unlock();
        }

        threadPoolExecutor.execute(this::writeUserDataListToFile);

        return true;
    }

    @Override
    public void setUserDataParam(UserData userDataParam) {
        try {
            lock.writeLock().lock();

            for (UserData temp : userDataArrayList){
                if(temp.equals(userDataParam)){
                    temp.setLevel(userDataParam.getLevel());
                    temp.setStatus(userDataParam.getStatus());
                    temp.setUploadFileNumber(userDataParam.getUploadFileNumber());
                    temp.setPassword(userDataParam.getPassword());
                }
            }

        }finally {
            lock.writeLock().unlock();
        }
        threadPoolExecutor.execute(this::writeUserDataListToFile);
    }

    @Override
    public UserData findUser(UserData userData) {
        try {
            lock.readLock().lock();
            for (UserData temp : userDataArrayList) {
                if (temp.equals(userData)) {
                    userData.setStatus(temp.getStatus());
                    userData.setUploadFileNumber(temp.getUploadFileNumber());
                    userData.setLevel(temp.getLevel());
                    userData.setName(temp.getName());
                    userData.setPassword(null);
                    return userData;
                }
            }
        }finally {
            lock.readLock().unlock();
        }
        return null;
    }
}
