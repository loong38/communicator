/**
 * 
 */
package cn.loong38.server.link;

import java.io.IOException;
import java.io.StringReader;

import cn.loong38.server.json.Json;
import com.google.gson.stream.JsonReader;

/**
 * @author LiuZhiwen
 *
 */
public class Link {

    public static LinkObject readLinkObj(String jsontext) {
	if (!Json.isJson(jsontext))
	    return new LinkObject();

	JsonReader jsonreader = new JsonReader(new StringReader(jsontext));
	LinkObject obj = new LinkObject();
	try {
	    jsonreader.beginObject();
	    String name = "";
	    while (jsonreader.hasNext()) {
		name = jsonreader.nextName();
		if (name.equals("username")) {
		    obj.setUsername(jsonreader.nextString());
		} else if (name.equals("password")) {
		    obj.setPassword(jsonreader.nextString());
		} else if (name.equals("UUID")) {
		    obj.setUUID(jsonreader.nextString());
		} else {
		    jsonreader.skipValue();
		}
	    }
	    jsonreader.endObject();
	} catch (IOException e) {
	    e.printStackTrace();
	    return new LinkObject();
	} finally {
	    try {
		jsonreader.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	return obj;
    }
}
