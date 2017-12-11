package com.wk.controller;

import com.wk.config.globaldata.ReponseConfigGlobal;
import com.wk.config.globaldata.UserConfigGlobal;
import com.wk.pojo.response.ResponseData;
import com.wk.pojo.userdata.UserData;
import com.wk.service.util.FileDataUtilBs;
import com.wk.service.util.UserDataUtilBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wkgui
 */
@Controller
public class MainController {

    private final UserDataUtilBs userDataUtilBs;

    private final FileDataUtilBs fileDataUtilBs;

    @Autowired
    public MainController(UserDataUtilBs userDataUtilBs, FileDataUtilBs fileDataUtilBs) {
        this.userDataUtilBs = userDataUtilBs;
        this.fileDataUtilBs = fileDataUtilBs;
    }


    @RequestMapping(value = "/")
    public String indexPage(){
        return "index";
    }

    /**
     * 用户注册
     * @param userData 注册信息
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData register(UserData userData){

        if(userDataUtilBs.checkUserDataIsAllowedAndAdd(userData)) {
            return new ResponseData(ReponseConfigGlobal.STATUS_SUCCESS_CODE, "注册成功！请等待管理员审核", null);
        }

        return new ResponseData(ReponseConfigGlobal.STATUS_ERROR_CODE, "注册失败！请重新填写用户名和姓名", null);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(UserData userData, HttpSession session){
        UserData user = userDataUtilBs.findUser(userData);
        if(user != null){
            if(user.getStatus() != UserConfigGlobal.BANED_USER && user.getStatus()!=UserConfigGlobal.REGISTER_USER) {
                session.setAttribute("userData", user);
                return new ResponseData(ReponseConfigGlobal.STATUS_SUCCESS_CODE, "登陆成功", user);
            }
        }
        return new ResponseData(ReponseConfigGlobal.STATUS_ERROR_CODE, "登陆失败", null);
    }

    /**
     * 文件上传
     * @param multipartFile 文件
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpSession session){
        UserData userData = (UserData) session.getAttribute("userData");

        if(userData != null){
            if(userData.getUploadFileNumber() > 0){
                fileDataUtilBs.saveFile(multipartFile, userData.getName());
                userData.setUploadFileNumber(userData.getUploadFileNumber()-1);
                userDataUtilBs.setUserDataParam(userData);
                return new ResponseData(ReponseConfigGlobal.STATUS_SUCCESS_CODE, "上传成功", fileDataUtilBs.getFileDataList());
            }
        }

        return new ResponseData(ReponseConfigGlobal.STATUS_ERROR_CODE, "上传失败", fileDataUtilBs.getFileDataList());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData deleteFile(String fileName, HttpSession session){
        UserData userData = (UserData) session.getAttribute("userData");

        if(fileDataUtilBs.deleteFile(fileName, userData.getName())){
            return new ResponseData(ReponseConfigGlobal.STATUS_SUCCESS_CODE, "删除成功", fileDataUtilBs.getFileDataList());
        }
        return new ResponseData(ReponseConfigGlobal.STATUS_SUCCESS_CODE, "删除失败", fileDataUtilBs.getFileDataList());
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String downloadFile(String fileName, HttpServletResponse response) throws IOException {
        InputStream inputStream = fileDataUtilBs.readFile(fileName);
        byte[] bytes = new byte[1024];
        int len;
        while((len = inputStream.read(bytes))!=-1){
            response.getOutputStream().write(bytes);
        }
        return null;
    }
}
