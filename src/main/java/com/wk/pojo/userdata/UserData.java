package com.wk.pojo.userdata;

/**
 * @author wkgui
 */
public class UserData {

    private String name;
    private String username;
    private String password;
    private int level;
    private int uploadFileNumber;
    private int status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserData userData = (UserData) o;

        return name == null || username == null || name.equals(userData.name) || username.equals(userData.username);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUploadFileNumber() {
        return uploadFileNumber;
    }

    public void setUploadFileNumber(int uploadFileNumber) {
        this.uploadFileNumber = uploadFileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", level='" + level + '\'' +
                ", uploadFileNumber=" + uploadFileNumber +
                ", status=" + status +
                '}';
    }
}
