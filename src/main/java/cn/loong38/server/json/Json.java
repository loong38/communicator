/**
 * 
 */
package cn.loong38.server.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author LiuZhiwen
 *
 */
public class Json {
    public static boolean isJson(String Json) {
	JsonElement jsonelement;
	try {
	    jsonelement = new JsonParser().parse(Json);
	} catch (Exception e) {
	    return false;
	}

	if (jsonelement == null)
	    return false;

	if (jsonelement.isJsonArray())
	    return true;

	if (jsonelement.isJsonObject())
	    return true;
	return false;
    }
}
