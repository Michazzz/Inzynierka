package pl.edu.zut.mwojtalewicz.friendlocalizerv2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.Library.Constans;
import pl.edu.zut.mwojtalewicz.Library.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.Library.JSONParser;
import pl.edu.zut.mwojtalewicz.Library.Notification;
import pl.edu.zut.mwojtalewicz.Library.UserFunctions;
import pl.edu.zut.mwojtalewicz.Utils.SearchFriendsItem;
import pl.edu.zut.mwojtalewicz.Utils.SearchFriendsListViewAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
			if(json == null)
			{
				Notification not = new Notification(this);
				not.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", this);
			}
			else
			{
				list = new ArrayList<SearchFriendsItem>();
				list = jParser.searchFriendsJSONParser(json, getApplicationContext());
				searchAdapter = new SearchFriendsListViewAdapter(this, list);
				searchFriendsListView.setAdapter(searchAdapter);
			}
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
				if(json2 == null)
				{
					Notification not2 = new Notification(getApplicationContext());
					not2.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", getApplicationContext());
				}
				else
				{
			    	try {
						if(json2.getString(Constans.KEY_SUCCESS) != null) {
							String res = json2.getString(Constans.KEY_SUCCESS);
							String errRes = json2.getString(Constans.KEY_ERROR);
							
							if(Integer.parseInt(res) == 1)
							{
									setNewAlertDialog("FriendLocalizer", "Zaproszenie wysłano!", SearchFriendsList.this);
							}else
							{
								switch(Integer.parseInt(errRes))
								{
									case 5:
										setNewAlertDialog("FriendLocalizer", "Zaprosiłeś już tego użytkownika, poczekaj na akceptację.", SearchFriendsList.this);	
										break;
										
									case 6:
										setNewAlertDialog("FriendLocalizer", "Nie możesz zaprosić swojego znajomego jeszcze raz", SearchFriendsList.this);
										break;
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	dialog.dismiss();
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
