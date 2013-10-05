package pl.edu.zut.mwojtalewicz.friendlocalizer;

import java.util.HashMap;

import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.DataBaseHandler;
import pl.edu.zut.mwojtalewicz.friendLocalizerLibrary.UserFunctions;
import android.os.Bundle;
import android.app.Activity;
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
}
