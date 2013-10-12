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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserFriendList extends Activity {
	
	private String object;
	private ArrayList<SearchFriendsItem> list;
	private JSONParser jParser;
	private SearchFriendsListViewAdapter searchAdapter;
	
	public ListView userFriendsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_friend_list);
		
		userFriendsListView = (ListView)findViewById(R.id.userFriendsListView);
		userFriendsListView.setClickable(true);
		
		DataBaseHandler db = new DataBaseHandler(getApplicationContext());
		HashMap<String, String> userDetails = db.getUserDetails();
		UserFunctions usr = new UserFunctions();
		String uniqueID = userDetails.get("uid");
		JSONObject json = usr.getUserFriendList(uniqueID);
		
		jParser = new JSONParser();
		
		list = new ArrayList<SearchFriendsItem>();
		list = jParser.newInviteJSONParser(json, getApplicationContext());
		searchAdapter = new SearchFriendsListViewAdapter(this, list);
		userFriendsListView.setAdapter(searchAdapter);
		
		userFriendsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				setNewAlertDialog("FriendLocalizer", "Czy chcesz usun¹æ " + Html.fromHtml("<b>"+list.get(arg2).getName() + " " + list.get(arg2).getLastname()+"</b>") + " ze znajomych?", UserFriendList.this, arg2);
			}
		});
	}
	
	private void setNewAlertDialog(String title, String msg, Context context, final int pos)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("Tak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
            		DataBaseHandler db = new DataBaseHandler(getApplicationContext());
            		HashMap<String, String> userDetails = db.getUserDetails();
            		UserFunctions usr = new UserFunctions();
            		String uniqueID = userDetails.get("uid");
            		JSONObject json = usr.removeUserFromFriends(uniqueID, list.get(pos).getUniqueID());
            		try {
    					if(json.getString(Constans.KEY_SUCCESS) != null) {
    						list.remove(pos);
    						searchAdapter = new SearchFriendsListViewAdapter(getApplicationContext(), list);
    						userFriendsListView.setAdapter(searchAdapter);
    						Toast.makeText(getApplicationContext(), "Usuniêto pomyœlnie!", Toast.LENGTH_SHORT).show();
    					}
                	} catch (JSONException e) {
    						e.printStackTrace();
    				}
                }
        });
        alertDialog.setButton2("Nie", new DialogInterface.OnClickListener() {
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
