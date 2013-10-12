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
	
	public JSONObject removeUserFromFriends(String uid_invited, String uid_inviting)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.removeUserFromFriends));
		params.add(new BasicNameValuePair("uid_invited", uid_invited));
		params.add(new BasicNameValuePair("uid_inviting", uid_inviting));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	public JSONObject getUserFriendList(String unique_id)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.userFriendList));
		params.add(new BasicNameValuePair("unique_id", unique_id));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	public JSONObject declineInvite(String uid_invited, String uid_inviting)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.declineInvite));
		params.add(new BasicNameValuePair("uid_invited", uid_invited));
		params.add(new BasicNameValuePair("uid_inviting", uid_inviting));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	public JSONObject acceptInvite(String uid_invited, String uid_inviting)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.acceptInvite));
		params.add(new BasicNameValuePair("uid_invited", uid_invited));
		params.add(new BasicNameValuePair("uid_inviting", uid_inviting));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	public JSONObject refreshFriendsList(String uid_user)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.refreshFriendsList));
		params.add(new BasicNameValuePair("uid_user", uid_user));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	/** 
	 * Funkcja, kt�ra wysy�a zapytanie w JSON do serwera, z parametrami koniecznymi do utworzenia nowego zaproszenia.
	 * @param uid_inviting
	 * @param uid_invited
	 * @return
	 */
	public JSONObject inviteFriend(String uid_inviting, String uid_invited)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.inviteFriend_tag));
		params.add(new BasicNameValuePair("uid_inviting", uid_inviting));
		params.add(new BasicNameValuePair("uid_invited", uid_invited));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
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
	 * Funkcja, kt�ra odpytuje baz� o u�ytkownik�w o danym imieniu i nazwisku ( wyszukiwanie znajomych )
	 * @param name
	 * @param lastname
	 * @param checkbox 
	 * @return lista os�b o danym imieniu i nazwisku
	 */
	public JSONObject searchFriends(String name, String lastname){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constans.searchFriends_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("flag", "false"));
		JSONObject json = jsonParser.getJSONFromUrl(Constans.loginURL, params);
		return json;
	}
	
	/**
	 * funkcja s�u��ca do logowania si� usera
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
	 * Funkcja tworz�ca zapytanie do zarejestrowania nowego u�ytkownika
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
	 * Funkcja sprawdza czy u�ytkownik jest nadal zalogowany.
	 * */
	public boolean isUserLoggedIn(Context context){
		DataBaseHandler db = new DataBaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			return true;
		}
		return false;
	}
	/**
	 * Funckja, kt�ra resetuj�c tabele bazy danych wylogowuje u�ytkownika.
	 * @param context
	 * @return
	 */
	public boolean logoutUser(Context context){
		DataBaseHandler db = new DataBaseHandler(context);
		db.resetTables();
		return true;
	}
	
	
}
