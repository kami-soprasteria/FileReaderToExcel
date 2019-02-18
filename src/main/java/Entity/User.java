package Entity;

import java.util.List;
import java.util.Map;

/**
 * Create by Kami Hassanzadeh on 2019-02-18.
 */
public class User {

    private Map<String, Long> userMap;
    private List<String> userList;
    private String fileName;

    public User() {
    }

    public Map<String, Long> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, Long> userMap) {
        this.userMap = userMap;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
