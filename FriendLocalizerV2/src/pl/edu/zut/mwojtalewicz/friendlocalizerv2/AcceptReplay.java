package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.Constans;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.JSONParser;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import pl.edu.zut.mwojtalewicz.Utils.SearchFriendsItem;
import pl.edu.zut.mwojtalewicz.Utils.SearchFriendsListViewAdapter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class AcceptReplay extends Activity {
	
	private String object;
	private ArrayList<SearchFriendsItem> list;
	private JSONParser jParser;
	private SearchFriendsListViewAdapter searchAdapter;
	public ListView newinviteToFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accept_replay);
		
		newinviteToFriend = (ListView)findViewById(R.id.acceptReplaysFriends);
		
		Intent intent = getIntent();
		object = intent.getStringExtra("friendlistjson");
		DataBaseHandler db = new DataBaseHandler(this);
		HashMap<String, String> userDetails = db.getUserDetails();
		UserFunctions usr = new UserFunctions();
		String uniqueID = userDetails.get("uid");
		jParser = new JSONParser();
		
		try {
			JSONObject json = new JSONObject(object);
			list = new ArrayList<SearchFriendsItem>();
			list = jParser.acceptedInviteJSONParser(json, getApplicationContext());
			
			if(json == null)
			{
				Toast.makeText(this, "Wystąpił problem z pobieraniem danych.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				searchAdapter = new SearchFriendsListViewAdapter(this, list);
				newinviteToFriend.setAdapter(searchAdapter);
			}
			
			int userNumber = Integer.parseInt(json.getString("usersNumber"));
			JSONObject jsonUser;
			JSONObject jsonAccpetReplay;
		
			String uID;
			for(int i = 0; i < userNumber; i++)
			{
				jsonUser = json.getJSONObject(""+i);
				uID = jsonUser.getString("unique_id");
				jsonAccpetReplay = usr.acceptReplay(uniqueID, uID);
				
				try{
					if(jsonAccpetReplay.get(Constans.KEY_SUCCESS) != null){
						
					} else {
						
					}
				} catch(Exception e){
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
