package com.wk.service.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileDataUtilBs {
    /**
     * 删除文件
     * @param fileName 文件名
     * @param owner 所有者
     */
    boolean deleteFile(String fileName, String owner);

    /**
     * 保存文件
     * @param multipartFile 文件
     *  @param owner 文件所属人
     */
    boolean saveFile(MultipartFile multipartFile, String owner);

    /**
     * 获取文件流
     * @param fileName 文件名
     *
     */
    InputStream readFile(String fileName);

    /**
     * 保存文件信息
     */
    void saveFileData();

    void setFileData();

    List getFileDataList();
}
