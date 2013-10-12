package pl.edu.zut.mwojtalewicz.friendlocalizer;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.Constans;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.JSONParser;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.SearchFriendsItem;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.SearchFriendsListViewAdapter;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class SearchFriendsList extends Activity {
	
	private String object;
	private ArrayList<SearchFriendsItem> list;
	private JSONParser jParser;
	private SearchFriendsListViewAdapter searchAdapter;
	
	public ListView searchFriendsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends_list);
		
		searchFriendsListView = (ListView)findViewById(R.id.searchFriendsListView);
		searchFriendsListView.setClickable(true);
		
		
		Intent intent = getIntent();
		object = intent.getStringExtra("jsonobject");
		jParser = new JSONParser();
		
		try {
			JSONObject json = new JSONObject(object);
			list = new ArrayList<SearchFriendsItem>();
			list = jParser.searchFriendsJSONParser(json, getApplicationContext());
			searchAdapter = new SearchFriendsListViewAdapter(this, list);
			searchFriendsListView.setAdapter(searchAdapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		searchFriendsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				  	
				DataBaseHandler db = new DataBaseHandler(getApplicationContext());
		    	HashMap<String, String> userDetails = db.getUserDetails();
		    	UserFunctions usr = new UserFunctions();
		    	JSONObject json2 = usr.inviteFriend(userDetails.get("uid"), list.get(arg2).getUniqueID());
		    	
		    	try {
					if(json2.getString(Constans.KEY_SUCCESS) != null) {
						String res = json2.getString(Constans.KEY_SUCCESS);
						String errRes = json2.getString(Constans.KEY_ERROR);
						
						if(Integer.parseInt(res) == 1)
						{
								setNewAlertDialog("FriendLocalizer", "Zaproszenie wys³ano!", SearchFriendsList.this);
						}else
						{
							switch(Integer.parseInt(errRes))
							{
								case 5:
									setNewAlertDialog("FriendLocalizer", "Zaprosi³eœ ju¿ tego u¿ytkownika, poczekaj na akceptacjê½.", SearchFriendsList.this);	
									break;
									
								case 6:
									setNewAlertDialog("FriendLocalizer", "Nie mo¿esz zaprosiæ swojego znajomego jeszcze raz", SearchFriendsList.this);
									break;
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setNewAlertDialog(String title, String msg, Context context)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	
                }
        });
        alertDialog.show();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
