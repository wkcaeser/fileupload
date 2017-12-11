package com.wk.service.util;

import com.wk.pojo.userdata.UserData;

import java.util.List;

/**
 * @author wkgui
 */
public interface UserDataUtilBs {
    /**
     * 读取用户json数据转为userdataList
     *
     * @return
     */
    List<UserData> listReadUsersDataToObject();

    /**
     * 将用户数据写入json文件
     *
     * @param
     */
    void writeUserDataListToFile();

    /**
     * 检查用户信息是否被允许然后写入
     *
     * @param userData 新注册用户数据
     * @return
     */
    boolean checkUserDataIsAllowedAndAdd(UserData userData);

    /**
     * 修改用户信息
     * @param userDataParam 用户信息
     */
    void setUserDataParam(UserData userDataParam);

    UserData findUser(UserData userData);
}
