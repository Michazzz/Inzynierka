package pl.edu.zut.mwojtalewicz.friendLocalizerLibrary;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * 
	 * @param email
	 * @param checkbox
	 * @return
	 */
	public JSONObject searchFriends(String email){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.searchFriends_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("flag", "true"));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	/**
	 * Funkcja, która odpytuje bazê o u¿ytkowników o danym imieniu i nazwisku ( wyszukiwanie znajomych )
	 * @param name
	 * @param lastname
	 * @param checkbox 
	 * @return lista osób o danym imieniu i nazwisku
	 */
	public JSONObject searchFriends(String name, String lastname){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.searchFriends_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("lastname", lastname));
		params.add(new BasicNameValuePair("flag", "false"));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	/**
	 * funkcja s³u¿¹ca do logowania siê usera
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	/**
	 * Funkcja tworz¹ca zapytanie do zarejestrowania nowego u¿ytkownika
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String lastname, String email, String password){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("lastname", lastname));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		
		JSONObject json = jsonParser.getJSONFromUrl(Constans.registerURL, params);

		return json;
	}
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DataBaseHandler db = new DataBaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			return true;
		}
		return false;
	}
	
	public boolean logoutUser(Context context){
		DataBaseHandler db = new DataBaseHandler(context);
		db.resetTables();
		return true;
	}
}
