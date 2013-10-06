package pl.edu.zut.mwojtalewicz.friendlocalizer;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.Constans;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.Notification;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoggedMainScreen extends Activity implements OnClickListener {

	private TextView tvHelloPerson;
	private ImageView ivPerson;
	private Button ivAddContacts;
	private Button ivMap;
	private Button ivLogout;
	private Button ivFriendList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_main_screen);
		
		tvHelloPerson = (TextView) findViewById(R.id.tvHelloPerson);
		ivAddContacts = (Button)findViewById(R.id.ivAddContacts);
		ivLogout = (Button)findViewById(R.id.ivLogout);
		ivFriendList = (Button)findViewById(R.id.ivFriendList);
		ivMap = (Button) findViewById(R.id.ivMap);
		ivPerson = (ImageView)findViewById(R.id.ivPerson);
		
		ivAddContacts.setOnClickListener(this);
		ivFriendList.setOnClickListener(this);
		ivMap.setOnClickListener(this);
		ivLogout.setOnClickListener(this);
		ivPerson.setOnClickListener(this);
		
		DataBaseHandler db = new DataBaseHandler(getApplicationContext());
    	HashMap<String, String> userDetails = db.getUserDetails();
    	
    	tvHelloPerson.setText("Witaj " + userDetails.get("name") + "!");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		DataBaseHandler db = new DataBaseHandler(getApplicationContext());
		HashMap<String, String> userDetails = db.getUserDetails();
		UserFunctions usr = new UserFunctions();
		String uniqueID = userDetails.get("uid");
		JSONObject json = usr.refreshFriendsList(uniqueID);
		
		try {
			if(json.get(Constans.KEY_SUCCESS) != null)
			{
				String res = json.getString(Constans.KEY_SUCCESS);
				String errRes = json.getString(Constans.KEY_ERROR);
				
				if(Integer.parseInt(res) == 1)
				{
					Intent tokenIntent = new Intent(getApplicationContext(), NewInviteToFriends.class);
					Notification not = new Notification(tokenIntent);
					not.displayOwnNotification(getApplicationContext(), "Nowe zaproszenie!", "Zosta³eœ zaproszony do znajomych...");
				}else
				{
					switch(Integer.parseInt(errRes))
					{
						case 7:
							//setNewAlertDialog("FriendLocalizer", "Zaprosi³eœ ju¿ tego u¿ytkownika, poczekaj na akceptacjê.", SearchFriendsList.this);
							break;
							
						case 8:
							break;
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
			case R.id.ivAddContacts:
				Intent i = new Intent(getApplicationContext(), SearchFriendsActivity.class);
				startActivity(i);
				break;
				
			case R.id.ivFriendList:
				break;
				
				
			case R.id.ivMap:
				break;
				
			case R.id.ivLogout:
				UserFunctions usr = new UserFunctions();
				usr.logoutUser(getApplicationContext());
				Intent mainintent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(mainintent);
				break;
				
			case R.id.ivPerson:
				break;
				
			default:
				break;
		}
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
}
