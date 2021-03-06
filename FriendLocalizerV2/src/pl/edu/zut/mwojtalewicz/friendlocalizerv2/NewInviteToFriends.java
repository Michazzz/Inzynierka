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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewInviteToFriends extends Activity {
	
	private String object;
	private ArrayList<SearchFriendsItem> list;
	private JSONParser jParser;
	private SearchFriendsListViewAdapter searchAdapter;
	public ListView newinviteToFriend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_invite_to_friends);
		
		newinviteToFriend = (ListView)findViewById(R.id.newInviteToFriends);
		newinviteToFriend.setClickable(true);
		
		
		Intent intent = getIntent();
		object = intent.getStringExtra("friendlistjson");
		jParser = new JSONParser();
		
		try {
			JSONObject json = new JSONObject(object);
			list = new ArrayList<SearchFriendsItem>();
			list = jParser.newInviteJSONParser(json, getApplicationContext());
			
			if(json == null)
			{
				Toast.makeText(this, "Wystąpił problem z pobieraniem danych.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				searchAdapter = new SearchFriendsListViewAdapter(this, list);
				newinviteToFriend.setAdapter(searchAdapter);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		newinviteToFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				setNewAlertDialog("FriendLocalizer", "Co zrobić z zaproszeniem?", NewInviteToFriends.this, arg2);
			} 
		});
	}
	
	private void setNewAlertDialog(String title, String msg, Context context, final int pos)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher);
        
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Akceptuj", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DataBaseHandler db = new DataBaseHandler(getApplicationContext());
                	HashMap<String, String> userDetails = db.getUserDetails();
                	UserFunctions usr = new UserFunctions();
                	JSONObject json2 = usr.acceptInvite(userDetails.get("uid"), list.get(pos).getUniqueID());
                	if(json2 == null)
                	{
                		dialog.dismiss();
                		Notification not = new Notification(getApplicationContext());
        				not.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", getApplicationContext());
                	}
                	else
                	{
	                	try {
	    					if(json2.getString(Constans.KEY_SUCCESS) != null) {
	    						list.remove(pos);
	    						searchAdapter = new SearchFriendsListViewAdapter(getApplicationContext(), list);
	    						newinviteToFriend.setAdapter(searchAdapter);
	    						Toast.makeText(getApplicationContext(), "Zaakceptowano", Toast.LENGTH_SHORT).show();
	    						dialog.dismiss();
	    					}
	                	} catch (JSONException e) {
	    						e.printStackTrace();
	    				}
                	}
                }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Odrzuć", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DataBaseHandler db = new DataBaseHandler(getApplicationContext());
            	HashMap<String, String> userDetails = db.getUserDetails();
            	UserFunctions usr = new UserFunctions();
            	JSONObject json2 = usr.declineInvite(userDetails.get("uid"), list.get(pos).getUniqueID());
            	if(json2 == null)
            	{
            		Notification not = new Notification(getApplicationContext());
    				not.setNewAlertDialog("Błąd", "Wystąpił problem przy przesyłaniu danych. Spróbuj ponownie później.", getApplicationContext());
            	}
            	else
            	{
	            	try {
						if(json2.getString(Constans.KEY_SUCCESS) != null) {
							list.remove(pos);
							Toast.makeText(getApplicationContext(), "Odrzucono", Toast.LENGTH_SHORT).show();
						}
	            	} catch (JSONException e) {
							e.printStackTrace();
					}
            	}
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
