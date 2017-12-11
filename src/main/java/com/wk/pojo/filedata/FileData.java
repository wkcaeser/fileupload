package com.wk.pojo.filedata;

/**
 * @author wkgui
 */
public class FileData {
    private String fileName;
    private Long fileSize;
    private String uploadTime;
    private String owner;

    public FileData(String fileName, Long fileSize, String uploadTime, String owner) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadTime = uploadTime;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileData fileData = (FileData) o;

        return (fileName != null ? fileName.equals(fileData.fileName) : fileData.fileName == null) && (fileSize != null ? fileSize.equals(fileData.fileSize) : fileData.fileSize == null) && (uploadTime != null ? uploadTime.equals(fileData.uploadTime) : fileData.uploadTime == null) && (owner != null ? owner.equals(fileData.owner) : fileData.owner == null);
    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (uploadTime != null ? uploadTime.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
