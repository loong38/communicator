/**
 * 
 */
package cn.loong38.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuZhiwen
 *
 */
public class HashUser extends ConcurrentHashMap<String, UserSocket> {
    private static final long serialVersionUID = 1L;
    private static final HashUser hashUser = new HashUser();

    /**
     * @return HashUser
     */
    public static HashUser getHashuser() {
	return hashUser;
    }

    private HashUser() {
    }

    public boolean isUUID(String uuid) {
	Set<String> set = new HashSet<>();
	set.addAll(keySet());
	if (set.add(uuid)) {
	    set.remove(uuid);
	    return false;
	}
	return true;
    }
}
