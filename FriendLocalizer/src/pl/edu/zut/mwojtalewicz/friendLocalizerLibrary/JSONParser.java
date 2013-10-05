package pl.edu.zut.mwojtalewicz.friendLocalizerLibrary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
		try{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
			
			jObj = new JSONObject(json);			
			return jObj;
			} catch(Exception e)	
			{
				Log.e("LOL", e.getMessage());
				return null;
			}
	}
	
	public ArrayList<SearchFriendsItem> searchFriendsJSONParser(JSONObject json, Context context)
	{
		ArrayList<SearchFriendsItem> list = new ArrayList<SearchFriendsItem>();
		SearchFriendsItem item;
		DataBaseHandler db = new DataBaseHandler(context);
    	HashMap<String, String> userDetails = db.getUserDetails();
		String uniqueID, name, lastname, email, uid;
		try {
			int userNum = Integer.parseInt(json.getString("usersNumber"));
			JSONObject jsonUser;
			for(int i = 0; i < userNum; i++)
			{
				jsonUser = json.getJSONObject(""+i);
				uniqueID = jsonUser.getString("uid");
				name = jsonUser.getString("name");
				lastname = jsonUser.getString("lastname");
				email = jsonUser.getString("email");
				uid = ""+i;
				if(!uniqueID.equals(userDetails.get("uid")))
				{
					item = new SearchFriendsItem(uniqueID, name, lastname, email, uid);
					list.add(item);
				}
			}
		} catch (Exception e) {}
		
		return list;
	}
}
