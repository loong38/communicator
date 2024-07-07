/**
 * 
 */
package cn.loong38.server.link;

/**
 * @author LiuZhiwen
 *
 */
public class LinkObject {
    private String username = "";
    private String password = "";
    private String UUID = "";

    public boolean isNull() {
	return UUID.equals("");
    }

    /**
     * @return UserName
     */
    public String getUsername() {
	return username;
    }

    /**
     * @param username 要设置的 UserName
     */
    public void setUsername(String username) {
	this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
	return password;
    }

    /**
     * @param password 要设置的 password
     */
    public void setPassword(String password) {
	this.password = password;
    }

    /**
     * @return uUID
     */
    public String getUUID() {
	return UUID;
    }

    /**
     * @param uUID 要设置的 uUID
     */
    public void setUUID(String uUID) {
	UUID = uUID;
    }
}
